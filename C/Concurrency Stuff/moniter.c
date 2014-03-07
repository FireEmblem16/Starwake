#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>

#pragma region Infrastructure

#ifndef FALSE
#define FALSE 0
#endif

#ifndef TRUE
#define TRUE 1
#endif

typedef struct qnode
{
	struct qnode* next;
	void* data;
} QNODE, *PQNODE;

typedef struct queue
{
	PQNODE head;
	PQNODE tail;
} QUEUE, *PQUEUE;

typedef struct pthread
{
	pthread_t thread;

	pthread_mutex_t m;
	int suspended;
} PTHREAD, *PPTHREAD;

typedef struct cond_var
{
	pthread_cond_t var;

	sem_t lock;
	int locked;
} COND_VAR, *PCOND_VAR;

typedef struct m_buffer
{
	QUEUE buffer;

	sem_t busy;

	COND_VAR produce;
	COND_VAR consume;
} M_BUFFER, *PM_BUFFER;

typedef struct m_string
{
	char* str;

	int wc;
	int rc;

	sem_t lock;
	sem_t rlock;
	sem_t wlock;
	
	COND_VAR write;
	COND_VAR read;
} M_STRING, *PM_STRING;

void queue_init(PQUEUE q);
void queue_push(PQUEUE q, void* data);
void* queue_poll(PQUEUE q);
void* queue_get(PQUEUE q, int index);
int queue_empty(PQUEUE q);
void queue_destroy(PQUEUE q, int free_data); // free_data != 0 will call free on every data stored in the nodes, otherwise no frees will be called

void thread_init(PPTHREAD thread);
void thread_start(PPTHREAD thread, void* (*func) (int, void**), int argc, void** argv);
void thread_cond_wait(PPTHREAD thread, PCOND_VAR cond);
void thread_destroy(PPTHREAD thread);

void cond_var_init(PCOND_VAR cv, int locked);
void cond_var_wait(PCOND_VAR cv, PPTHREAD thread);
void cond_var_signal(PCOND_VAR cv);
void cond_var_destroy(PCOND_VAR cv);

void m_buffer_init(PM_BUFFER buff);
void m_buffer_push(PPTHREAD thread, PM_BUFFER buff, void* data);
void* m_buffer_pop(PPTHREAD thread, PM_BUFFER buff);
void m_buffer_destroy(PM_BUFFER buff);

void m_string_init(PM_STRING str, int max_size);
void m_string_write(PPTHREAD thread, PM_STRING str, char* s);
void m_string_append(PPTHREAD thread, PM_STRING str, char* s);
void m_string_read(PPTHREAD thread, PM_STRING str, char** buff);
void m_string_destroy(PM_STRING str);

#pragma endregion

// Globals
M_STRING mstring;
M_BUFFER buffer;

void* read(int argc, void** argv)
{
	char* str;
	m_string_read((PPTHREAD)argv[0],&mstring,&str);

	printf("%s\n",str);
	return NULL;
}

void* write(int argc, void** argv)
{
	m_string_write((PPTHREAD)argv[0],&mstring,(char*)argv[1]);
	return NULL;
}

void* append(int argc, void** argv)
{
	m_string_append((PPTHREAD)argv[0],&mstring,(char*)argv[1]);
	return NULL;
}

void* consume(int argc, void** argv)
{
	printf("%s",(char*)m_buffer_pop((PPTHREAD)argv[0],&buffer));
	return NULL;
}

void* produce(int argc, void** argv)
{
	int i = 0;
	while(i++ < 100000); // Waste some time

	m_buffer_push((PPTHREAD)argv[0],&buffer,(char*)argv[1]);	
	return NULL;
}

int main(int argc, char** argv)
{
	m_string_init(&mstring,0xFFFF);
	m_buffer_init(&buffer);
	int i = 0; // Stupid error

	int test_mstring = FALSE; // Set to TRUE to test, FALSE to not test
	int test_mbuffer = FALSE; // Set to TRUE to test, FALSE to not test

	if(test_mstring)
	{
		// Test monitered read/write string
		for(;i < 250;i++)
		{
			if(i % 5)
			{
				PPTHREAD thread = (PPTHREAD)malloc(sizeof(PTHREAD));
				void** params = (void**)malloc(sizeof(void*));
				params[0] = (void*)thread;

				thread_init(thread);
				thread_start(thread,&read,1,params);
			}
			else if(!(i % 21))
			{
				PPTHREAD thread = (PPTHREAD)malloc(sizeof(PTHREAD));
				void** params = (void**)malloc(sizeof(void*) * 2);
				params[0] = (void*)thread;
				params[1] = malloc(sizeof(char) * 5);

				sprintf((char*)params[1],"");

				thread_init(thread);
				thread_start(thread,&write,2,params);
			}
			else
			{
				PPTHREAD thread = (PPTHREAD)malloc(sizeof(PTHREAD));
				void** params = (void**)malloc(sizeof(void*) * 2);
				params[0] = (void*)thread;
				params[1] = malloc(sizeof(char) * 5);

				sprintf((char*)params[1],"%i ",i);

				thread_init(thread);
				thread_start(thread,&append,2,params);
			}
		}

		sleep(1);
	}
	
	if(test_mbuffer)
	{
		// Test monitered produce/consume buffer
		for(;i < 50;i++)
		{
			if(i % 2)
			{
				PPTHREAD thread = (PPTHREAD)malloc(sizeof(PTHREAD));
				void** params = (void**)malloc(sizeof(void*));
				params[0] = (void*)thread;

				thread_init(thread);
				thread_start(thread,&consume,1,params);
			}
			else
			{
				PPTHREAD thread = (PPTHREAD)malloc(sizeof(PTHREAD));
				void** params = (void**)malloc(sizeof(void*) * 2);
				params[0] = (void*)thread;
				params[1] = malloc(sizeof(char) * 5);

				sprintf((char*)params[1],"%i\n",i);

				thread_init(thread);
				thread_start(thread,&produce,2,params);
			}
		}

		sleep(1);
	}
	
	// We're just going to accept things not being destroyed and memory leaks everywhere becuase we're done anyways

	return 0;
}

#pragma region Queue Functions

void queue_init(PQUEUE q)
{
	if(!q)
		return;

	q->head = NULL;
	q->tail = NULL;

	return;
}

void queue_push(PQUEUE q, void* data)
{
	if(!q)
		return;

	if(!q->head)
		q->tail = q->head = (PQNODE)malloc(sizeof(QNODE));
	else
	{
		q->tail->next = (PQNODE)malloc(sizeof(QNODE));
		q->tail = q->tail->next;
	}

	q->tail->next = NULL;
	q->tail->data = data;

	return;
}

void* queue_poll(PQUEUE q)
{
	if(!q || !q->head)
		return NULL;

	PQNODE nret = q->head;
	void* ret = nret->data;

	q->head = nret->next;
	free((void*)nret);

	if(!q->head)
		q->tail = NULL;

	return ret;
}

void* queue_get(PQUEUE q, int index)
{
	if(!q)
		return NULL;

	if(index == 0)
		return q->head->data;

	PQNODE n = q->head;
	
	while(n)
		if(!index--)
			return n->data;
		else
			n = n->next;

	return NULL;
}

int queue_empty(PQUEUE q)
{
	// We will assume if the queue is not initialized it is better to say it is empty
	if(!q || !q->head)
		return TRUE;

	return FALSE;
}

void queue_destroy(PQUEUE q, int free_data)
{
	if(!q || !q->head)
		return;

	if(free_data)
		while(q->head)
		{
			void* ptr = queue_poll(q);
			
			// We allow NULL in the queue
			if(ptr)
				free(ptr);
		}
	else
		while(q->head)
			queue_poll(q);

	return;
}

#pragma endregion

#pragma region Thread Functions

void thread_init(PPTHREAD thread)
{
	if(!thread)
		return;

	pthread_mutex_init(&thread->m,NULL);
	thread->suspended = TRUE;

	return;
}

void* thread_transition(void* args)
{
	void** rargs = (void**)args;

	PPTHREAD thread = (PPTHREAD)rargs[0];
	void* (*f) (int, void**) = rargs[1];
	int count = *(int*)rargs[2];
	void** real_args = (void**)rargs[3];

	free(rargs[2]);
	free(args);

	thread->thread = pthread_self();
	thread->suspended = FALSE;

	return f(count,real_args);
}

void thread_start(PPTHREAD thread, void* (*func) (int, void**), int argc, void** argv)
{
	if(!thread || !func)
		return;

	void** transition_args = malloc(sizeof(void*) * 4);
	transition_args[0] = (void*)thread;
	transition_args[1] = (void*)func;
	transition_args[2] = malloc(sizeof(int));
	(*(int*)transition_args[2]) = argc;
	transition_args[3] = (void*)argv;

	pthread_create(&thread->thread,NULL,&thread_transition,transition_args);
	return;
}

void thread_cond_wait(PPTHREAD thread, PCOND_VAR cond)
{
	if(!thread || !cond)
		return;

	cond_var_wait(cond,thread);
	return;
}

void thread_destroy(PPTHREAD thread)
{
	if(!thread)
		return;

	thread->suspended = TRUE;
	pthread_mutex_destroy(&thread->m);

	return;
}

#pragma endregion

#pragma region Condition Variable Functions

void cond_var_init(PCOND_VAR cv, int locked)
{
	if(!cv)
		return;

	cv->locked = locked;

	sem_init(&cv->lock,FALSE,1);
	pthread_cond_init(&cv->var,NULL);
	return;
}

void cond_var_wait(PCOND_VAR cv, PPTHREAD thread)
{
	if(!cv)
		return;

	pthread_mutex_lock(&thread->m);
	thread->suspended = TRUE;

	sem_wait(&cv->lock);

	if(cv->locked)
	{
		sem_post(&cv->lock);
		pthread_cond_wait(&cv->var,&thread->m);
		sem_wait(&cv->lock);	
	}
	
	cv->locked = TRUE;
	sem_post(&cv->lock);

	thread->suspended = FALSE;
	pthread_mutex_unlock(&thread->m);

	return;
}

void cond_var_signal(PCOND_VAR cv)
{
	if(!cv || !cv->locked)
		return;

	sem_wait(&cv->lock);
	
	cv->locked = FALSE;
	pthread_cond_signal(&cv->var);

	sem_post(&cv->lock);

	return;
}

void cond_var_destroy(PCOND_VAR cv)
{
	if(!cv)
		return;

	cv->locked = FALSE;

	sem_destroy(&cv->lock);
	pthread_cond_destroy(&cv->var);
	return;
}

#pragma endregion

#pragma region Monitered Buffer Functions

void m_buffer_init(PM_BUFFER buff)
{
	if(!buff)
		return;

	queue_init(&buff->buffer);

	sem_init(&buff->busy,FALSE,1);

	cond_var_init(&buff->produce,FALSE);
	cond_var_init(&buff->consume,TRUE); // It's not okay to consume until we've produced

	return;
}

void m_buffer_push(PPTHREAD thread, PM_BUFFER buff, void* data)
{
	if(!buff)
		return;

	cond_var_wait(&buff->produce,thread);
	
	sem_wait(&buff->busy);
	queue_push(&buff->buffer,data);
	sem_post(&buff->busy);

	cond_var_signal(&buff->produce);
	cond_var_signal(&buff->consume);

	return;
}

void* m_buffer_pop(PPTHREAD thread, PM_BUFFER buff)
{
	if(!buff)
		return;

	cond_var_wait(&buff->consume,thread);
	
	sem_wait(&buff->busy);
	void* ret = queue_poll(&buff->buffer);
	sem_post(&buff->busy);

	if(!queue_empty(&buff->buffer))
		cond_var_signal(&buff->consume);

	return ret;
}

void m_buffer_destroy(PM_BUFFER buff)
{
	if(!buff)
		return;

	queue_destroy(&buff->buffer,TRUE);

	sem_destroy(&buff->busy);

	cond_var_destroy(&buff->produce);
	cond_var_destroy(&buff->consume);

	return;
}

#pragma endregion

#pragma region Monitered String Functions

void m_string_init(PM_STRING str, int max_size)
{
	if(!str)
		return;

	str->str = (char*)malloc(sizeof(char) * max_size);

	str->wc = 0;
	str->rc = 0;

	sem_init(&str->lock,FALSE,1);
	sem_init(&str->rlock,FALSE,1);
	sem_init(&str->wlock,FALSE,1);

	cond_var_init(&str->write,FALSE);
	cond_var_init(&str->read,TRUE); // There's nothing to read until we've written something

	return;
}

void m_string_write(PPTHREAD thread, PM_STRING str, char* s)
{
	if(!str)
		return;

	sem_wait(&str->wlock);
	++str->wc; // Let everyone know we're waiting and mean business
	sem_post(&str->wlock);

	cond_var_wait(&str->write,thread);

	sem_wait(&str->lock); // Fight pesky readers (and other writers) slipping by for domination	
	strcpy(str->str,s);
	sem_wait(&str->wlock);

	if(--str->wc)
		cond_var_signal(&str->write); // More writers so let the next one in
	else
	{
		cond_var_signal(&str->read); // Out of writers so let readers in again
		cond_var_signal(&str->write); // More writers are also fine
	}

	sem_post(&str->wlock);
	sem_post(&str->lock);

	return;
}

void m_string_append(PPTHREAD thread, PM_STRING str, char* s)
{
	if(!str)
		return;

	sem_wait(&str->wlock);
	++str->wc; // Let everyone know we're waiting and mean business
	sem_post(&str->wlock);

	cond_var_wait(&str->write,thread);

	sem_wait(&str->lock); // Fight pesky readers (and other writers) slipping by for domination	
	strcat(str->str,s);
	sem_wait(&str->wlock);

	if(--str->wc)
		cond_var_signal(&str->write); // More writers so let the next one in
	else
	{
		cond_var_signal(&str->read); // Out of writers so let readers in again
		cond_var_signal(&str->write); // More writers are also fine
	}

	sem_post(&str->wlock);
	sem_post(&str->lock);

	return;
}

void m_string_read(PPTHREAD thread, PM_STRING str, char** buff)
{
	if(!str)
		return;

	cond_var_wait(&str->read,thread);

	sem_wait(&str->lock); // Fight a potential writer for the right to continue (we still give writers priority, but one reader may go before it)
	sem_wait(&str->wlock);

	// If no writers are waiting then let another reader in (if a writer is trying to get in, it will very likely wrest control of busy and keep it until it is done)
	if(!str->wc)
		cond_var_signal(&str->read);

	sem_post(&str->wlock);
	sem_post(&str->lock);

	sem_wait(&str->rlock);
	++str->rc; // We do this here instead of before the cond_var_wait so that we don't force readers to have priority
	sem_post(&str->rlock);

	(*buff) = (char*)malloc(sizeof(char) * strlen(str->str));
	strcpy((*buff),str->str);

	sem_wait(&str->rlock);
	
	if(!--str->rc)
		cond_var_signal(&str->write); // We have no more readers so it's cool to write

	sem_post(&str->rlock);

	return;
}

void m_string_destroy(PM_STRING str)
{
	if(!str)
		return;

	free((void*)str->str);

	str->wc = 0;
	str->rc = 0;

	sem_destroy(&str->lock);
	sem_destroy(&str->rlock);
	sem_destroy(&str->wlock);

	cond_var_destroy(&str->read);
	cond_var_destroy(&str->write);

	return;
}

#pragma endregion