#ifndef _LIB_ULT_CPP
#define _LIB_ULT_CPP

#include <cstdlib>
#include <ucontext.h>

#pragma region Node

template <class T> class Node
{
public:
	Node(T val, int p);
	~Node();

	Node<T>* next;
	int Pval;

	T data;
};

template <class T> Node<T>::Node(T val, int p)
{
	next = NULL;
	Pval = p;

	data = val;
	return;
}

template <class T> Node<T>::~Node()
{
	if(data != NULL)
		delete data;

	if(next != NULL)
		delete next;

	return;
}

#pragma endregion

#pragma region PQueue

template <class T> class PQueue
{
public:
	PQueue();
	~PQueue();

	void Push(T item, int priority);
	T Peek();
	T Poll();

	bool Reprioritize(T item, int priority);
	bool Remove(T item);

	inline bool Empty();
	inline int Size();
protected:
	Node<T>* head;
	int size;
};

template <class T> PQueue<T>::PQueue()
{
	head = NULL;
	size = 0;

	return;
}

template <class T> PQueue<T>::~PQueue()
{
	if(head != NULL)
	{
		delete head;
		head = NULL;
	}

	size = 0;
	return;
}

template <class T> void PQueue<T>::Push(T item, int priority)
{
	if(head == NULL)
		head = new Node<T>(item,priority);
	else if(head->Pval > priority)
	{
		Node<T>* n = head;

		head = new Node<T>(item,priority);
		head->next = n;
	}
	else
	{
		Node<T>* n = head;
		Node<T>* last = NULL;

		while(n != NULL && n->Pval <= priority)
		{
			last = n;
			n = n->next;
		}

		last->next = new Node<T>(item,priority);

		if(n != NULL)
			last->next->next = n;
	}

	++size;
	return;
}

template <class T> T PQueue<T>::Peek()
{
	if(head == NULL)
		return NULL;

	return head->data;
}

template <class T> T PQueue<T>::Poll()
{
	if(head == NULL)
		return NULL;

	Node<T>* n = head;
	head = head->next;
	--size;

	T ret = n->data;
	n->data = NULL;
	n->next = NULL;

	delete n;
	return ret;
}

template <class T> bool PQueue<T>::Reprioritize(T item, int priority)
{
	Node<T>* n = head;
	Node<T>* last = NULL;

	while(n != NULL)
	{
		if(n->data == item)
		{
			T data = n->data;

			last->next = n->next;
			n->next = NULL;
			n->data = NULL;
			
			delete n;
			--size;
			
			Push(data,priority);
			return true;
		}

		last = n;
		n = n->next;
	}

	return false;
}

template <class T> bool PQueue<T>::Remove(T item)
{
	Node<T>* n = head;
	Node<T>* last = NULL;

	while(n != NULL)
	{
		if(n->data == item)
		{
			last->next = n->next;
			n->next = NULL;
			
			delete n;
			--size;
			
			return true;
		}

		last = n;
		n = n->next;
	}

	return false;
}

template <class T> inline bool PQueue<T>::Empty()
{return size == 0;}

template <class T> inline int PQueue<T>::Size()
{return size;}

#pragma endregion

#pragma region UThread

void system_init();
int uthread_create(void (*func)(), int priority);
int uthread_yield(int priority);
void uthread_exit();

class Thread
{
public:
	Thread(bool base)
	{
		not_base = !base;

		context = new ucontext_t();
		exit_context = new ucontext_t();

		getcontext(context);
		getcontext(exit_context);

		exit_context->uc_stack.ss_sp = new char[0xFF];
		exit_context->uc_stack.ss_size = 0xFF;
		exit_context->uc_link = NULL;
		makecontext(exit_context,uthread_exit,0);

		if(not_base)
		{
			context->uc_stack.ss_sp = new char[0xFFFF];
			context->uc_stack.ss_size = 0xFFFF;
		}

		context->uc_link = exit_context;
		return;
	}

	~Thread()
	{
		if(not_base)
			delete[] (char*)context->uc_stack.ss_sp;

		delete[] (char*)exit_context->uc_stack.ss_sp;

		delete context;
		delete exit_context;

		return;
	}

	ucontext_t* context;
	ucontext_t* exit_context;
protected:
	bool not_base;
};

PQueue<Thread*> processes;
Thread* current;

void system_init()
{
	current = new Thread(true);
	current->context->uc_link = current->exit_context; // Doesn't seem to work so we require the main context to call uthread_exit()

	return;
}

int uthread_create(void (*func)(), int priority)
{
	Thread* t = new Thread(false);
	makecontext(t->context,func,0);

	processes.Push(t,priority);
	return 0;
}

int uthread_yield(int priority)
{
	if(processes.Empty())
		return -1;

	// Even if current would become the next highest priority function we want to switch to something new
	Thread* next = processes.Poll();
	processes.Push(current,priority);

	Thread* last = current;
	current = next;
	
	return swapcontext(last->context,next->context); // Returns -1 on error and 0 on success
}

void uthread_exit()
{
	if(processes.Empty())
		exit(0);

	current = processes.Poll();
	setcontext(current->context); // For some reason this seems to be magically handling the deletion of contexts, so the delete calls have been removed
} // There is NO return

#pragma endregion

#endif