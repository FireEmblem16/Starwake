///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Block.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the block class.                                                 ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _BLOCK_CPP
#define _BLOCK_CPP

#include "../Headers/Block.h"

Block::Block(string Type, string Display, string Name, string Color) : Geometry(Type,Display,Name,Color)
{
	SoftPlace* newcol = new SoftPlace();
	nature->ChangeCollisionBehaviour(newcol);
	delete newcol;

	return;
}

Block::Block(const Block& rhs) : Geometry(rhs.type,rhs.disp,rhs.name,rhs.color)
{return;}

Block& Block::operator =(const Block& rhs)
{
	if(this == &rhs)
		return *this;

	Geometry::operator =(rhs);
	return *this;
}

Item* Block::Clone()
{
	Block* ret = new Block("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
