///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Sentry.cpp ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the Sentry class.                                                ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _SENTRY_CPP
#define _SENTRY_CPP

#include "../Headers/Sentry.h"

Sentry::Sentry() : Behaviour()
{
	MoveBehaviour* newaction = new Centurion();
	ChangeMoveBehaviour(newaction);
	delete newaction;

	return;
}

Behaviour* Sentry::CreateNew()
{
	return new Sentry();
}

#endif
