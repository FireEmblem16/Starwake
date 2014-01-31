using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TheGame.Engine.Abilities.Visibility
{
	/// <summary>
	/// Represents how visible the unit is.
	/// </summary>
	public interface Visibility : Ability
	{
		/// <summary>
		/// If true the unit is invisible.
		/// </summary>
		bool Invisible
		{get;}
	}
}
