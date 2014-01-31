///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// Target.h ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Essentially a point.                                                        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _TARGET_H
#define _TARGET_H

#include "Defines.h"

// Essentially a point.
class Target
{
public:
	Target(int x_init, int y_init, int e_init = 0);
	Target(const Target& rhs);
	Target& operator =(const Target& rhs);

	float Distance(const Target& to) const;
	float Magnitude() const;

	bool operator ==(const Target& rhs) const;
	bool operator !=(const Target& rhs) const;
	bool CloserThan(const Target& t2, const Target& point) const; // dist < dist2
	bool AsCloseAs(const Target& t2, const Target& point) const; // dist <= dist2
	bool FurtherThan(const Target& t2, const Target& point) const; // dist > dist2
	bool AsFarAs(const Target& t2, const Target& point) const; // dist >= dist2

	Target operator +(const Target& rhs) const; // Also adds e
	Target operator -(const Target& rhs) const; // Also subtracts e

	// There's no real reason to not expose these unless you want to cause huge overhead
	int e;
	int x;
	int y;

	static float sqrt(float X); // Returns the sqrt of positive X if X < 0.0f
};

#endif
