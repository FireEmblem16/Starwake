#include <stdlib.h>
#include <GL/glut.h>

#include "utility.h"
#include "Object.h"
#include "Model Library.h"
#include "Texture Library.h"
#include "Light.h"

char* working_directory;
Object* a,* b,* c;
ModelLibrary* m_lib;
TextureLibrary* t_lib;
Light* l;

void init()
{
	glClearColor(0.0f,0.0f,0.0f,0.0f);
	glEnable(GL_TEXTURE_2D);
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LEQUAL);
	glShadeModel(GL_FLAT);

	char buf[1024];
	strcpy(buf,working_directory);
	strcat(buf,"\\Test Model Data\\Block.model");

	char buf2[1024];
	strcpy(buf2,working_directory);
	strcat(buf2,"\\Test Model Data\\texture.bmp");

	char buf3[1024];
	strcpy(buf3,working_directory);
	strcat(buf3,"\\Test Model Data\\texture2.bmp");

	m_lib = new ModelLibrary();
	m_lib->LoadModel(buf);

	t_lib = new TextureLibrary();
	t_lib->LoadTexture(buf2);
	t_lib->LoadTexture(buf3);

	a = new Object(m_lib->GetModel(buf),t_lib->GetTexture(buf2));
	b = new Object(m_lib->GetModel(buf),t_lib->GetTexture(buf3));
	c = a;

	l = new Light(Vector(1.0f,1.0f,1.0f),Vector(1.0f,1.0f,1.0f));

	return;
}

void display()
{
	glMatrixMode(GL_MODELVIEW);

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

	glPushMatrix();
	{
		glLoadIdentity();
		gluLookAt(Vector(0.0,0.0,5.0),Vector(0.0,0.0,0.0),Vector(0.0,1.0,0.0));
		a->draw();
		b->draw();
	}
	glPopMatrix();

	glutSwapBuffers();
	return;
}

// Windows reshape command called by moving the window or resizing.
void reshape(int w, int h)
{
	glMatrixMode(GL_PROJECTION);
	glViewport(0,0,(GLsizei)w,(GLsizei)h);
	glLoadIdentity();
	gluPerspective(30.0,((GLdouble)w)/((GLdouble)h),1.0,50.0);

	return;
}

// x and y hold the current mouse location
void keyboard(unsigned char key, int x, int y)
{
	switch(key)
	{
	case 27:
		exit(0);
		break;
	case 100:
		c->rotate(Vector(-1.0f,0.0f,0.0f));

		glutPostRedisplay();
		break;
	case 97:
		c->rotate(Vector(1.0f,0.0f,0.0f));

		glutPostRedisplay();
		break;
	case 115:
		c->rotate(Vector(0.0f,-1.0f,0.0f));

		glutPostRedisplay();
		break;
	case 119:
		c->rotate(Vector(0.0f,1.0f,0.0f));

		glutPostRedisplay();
		break;
	case 101:
		c->rotate(Vector(0.0f,0.0f,-1.0f));

		glutPostRedisplay();
		break;
	case 113:
		c->rotate(Vector(0.0f,0.0f,1.0f));

		glutPostRedisplay();
		break;
	case 120:
		c->move(Vector(0.0f,0.0f,0.1f));

		glutPostRedisplay();
		break;
	case 122:
		c->move(Vector(0.0f,0.0f,-0.1f));

		glutPostRedisplay();
		break;
	case 99:
		c == a ? c = b : c = a;

		break;
   }
}

// x and y hold the current mouse location
void special_key(int key, int x, int y)
{
	switch(key)
	{
	case GLUT_KEY_LEFT:
		c->move(Vector(-0.1f,0.0f,0.0f));

		glutPostRedisplay();
		break;
	case GLUT_KEY_RIGHT:
		c->move(Vector(0.1f,0.0f,0.0f));

		glutPostRedisplay();
		break;
	case GLUT_KEY_DOWN:
		c->move(Vector(0.0f,-0.1f,0.0f));

		glutPostRedisplay();
		break;
	case GLUT_KEY_UP:
		c->move(Vector(0.0f,0.1f,0.0f));

		glutPostRedisplay();
		break;
	}

	return;
}

void mouse(int button, int state, int x, int y)
{
	switch(button)
	{
	case GLUT_LEFT_BUTTON:
		break;
	case GLUT_MIDDLE_BUTTON:
		break;
	case GLUT_RIGHT_BUTTON:
		break;
	default:
		break;
	}

	return;
}

void main(int argc, char** args)
{
	*strchrlast(args[0],'\\') = '\0';
	working_directory = args[0];
	
	glutInit(&argc,args);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA | GLUT_DEPTH);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("Objects!");
	init();

	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(special_key);
	glutMouseFunc(mouse);
	glutIdleFunc(NULL);

	glutMainLoop();
	return;
}