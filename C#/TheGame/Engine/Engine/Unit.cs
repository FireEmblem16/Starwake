using System;
using System.Collections.Generic;
using TheGame.Engine.Abilities;
using TheGame.Engine.Abilities.ActivatableAbility;
using TheGame.Engine.Abilities.ActivatableAbility.Attack;
using TheGame.Engine.Abilities.Health;
using TheGame.Engine.Abilities.Movement;
using TheGame.Engine.Abilities.PassiveAbilities;
using TheGame.Engine.Abilities.PassiveAbilities.Ailment;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.Abilities.Visibility;
using TheGame.Engine.Abilities.Vision;
using TheGame.Engine.ActivatedAbilities;
using TheGame.Parsers;
using TheGame.Utility;

namespace TheGame.Engine
{
	/// <summary>
	/// Represents a unit in the game.
	/// </summary>
	public class Unit
	{
		/// <summary>
		/// Creates a new unit with the given name and point total.
		/// </summary>
		/// <param name="name">The name of this unit.</param>
		/// <param name="points">The number of points this unit has to work with.</param>
		public Unit(string name, int points)
		{
			Name = name;
			PointTotal = points;

			AbilityPassiveAbilities = new List<PassiveAbility>();
			AbilityActivatableAbilities = new List<ActivationAbility>();

			// Set up all the default abilities skipping over the add function since they are all free and have nothing to replace
			AddAbility(new NormalHealth());
			AddAbility(new GroundMovement());
			AddAbility(new NormalVision());
			AddAbility(new Visible());
			AddAbility(new NormalAttack());

			return;
		}

		/// <summary>
		/// Creates a unit from the provided information.
		/// </summary>
		/// <param name="tokens">The logged unit.</param>
		public Unit(StringTokenizer tokens)
		{
			Name = tokens.NextToken();
			PointTotal = int.Parse(tokens.NextToken());

			AbilityPassiveAbilities = new List<PassiveAbility>();
			AbilityActivatableAbilities = new List<ActivationAbility>();

			while(tokens.TokensLeft > 1)
			{
				try
				{
					Ability a = AbilityFactory.GenerateAbility(tokens.NextToken());
					int level = int.Parse(tokens.NextToken());

					while(level > a.Level)
						if(!a.LevelUp())
							break; // If we can't level up the ability then something's wrong and we'll just forget it

					// It should never happen that we need to unlevel from the base but just in case
					while(level < a.Level)
						if(!a.Unlevel())
							break;

					AddAbility(a);
				}
				catch
				{} // This only happens if a level could not be parsed or much more likely if an ability is not registered with the factory
			}

			return;
		}

		/// <summary>
		/// Clones this unit.
		/// </summary>
		/// <param name="clone_hidden">If true then hidden abilities should be cloned as well. This does not allow abilities that are not registered with the factory to be cloned explicitly although they should be cloned implicitly.</param>
		/// <returns>Returns a deep copy of this unit.</returns>
		public Unit Clone(bool clone_hidden = true)
		{return new Unit(new StringTokenizer(ToString(clone_hidden),"\n"));}

		/// <summary>
		/// Adds an ability to this unit.
		/// If the ability is mutually exclusive with another ability then the old one will be removed.
		/// If there are not enough points then no change will be made.
		/// </summary>
		/// <param name="a">The ability to add.</param>
		/// <returns>Returns true if the ability is added and false otherwise.</returns>
		public bool AddAbility(Ability a)
		{
			int point_change = a.PointCost - AbilityCost(a.GetType());

			if(a is Health && AbilityHealth != null)
				point_change -= AbilityHealth.PointCost;
			else if(a is Movement && AbilityMovement != null)
				point_change -= AbilityMovement.PointCost;
			else if(a is Visibility && AbilityVisibility != null)
				point_change -= AbilityVisibility.PointCost;
			else if(a is Vision && AbilityVision != null)
				point_change -= AbilityVision.PointCost;
			else if(a is ActivationAbility && (a as ActivationAbility).IsDefaultAbility && AbilityDefault != null )
				point_change -= AbilityDefault.PointCost;

			if(PointsFree < point_change)
				return false;

			if(a is Health)
			{
				if(AbilityHealth != null)
					RemoveAbility(AbilityHealth.GetType());
				
				AbilityHealth = (Health)a;
			}
			else if(a is Movement)
			{
				if(AbilityMovement != null)
					RemoveAbility(AbilityMovement.GetType());
				
				AbilityMovement = (Movement)a;
			}
			else if(a is Visibility)
			{
				if(AbilityVisibility != null)
					RemoveAbility(AbilityVisibility.GetType());

				AbilityVisibility = (Visibility)a;
			}
			else if(a is Vision)
			{
				if(AbilityVision != null)
					RemoveAbility(AbilityVision.GetType());

				AbilityVision = (Vision)a;
			}
			else if(a is PassiveAbility)
			{
				RemoveAbility(a.GetType());
				AbilityPassiveAbilities.Add((PassiveAbility)a);
			}
			else if(a is ActivationAbility)
			{
				ActivationAbility act = a as ActivationAbility;

				if(act.IsDefaultAbility)
				{
					RemoveAbility(a.GetType());
					AbilityDefault = act;
				}
				else
				{
					RemoveAbility(a.GetType());
					AbilityActivatableAbilities.Add((ActivationAbility)a);
				}
			}

			a.OnAdd(this);
			return true;
		}

		/// <summary>
		/// Removes the ability with the given type from this unit. If necessary it replaces the ability with the default ability.
		/// </summary>
		/// <param name="t">The type of the ability to remove.</param>
		public void RemoveAbility(Type t)
		{
			Ability a = null;
			Type[] interfaces = t.GetInterfaces();

			foreach(Type type in interfaces)
				if(type == typeof(PassiveAbility))
				{
					bool found = false;

					for(int i = 0;i < AbilityPassiveAbilities.Count;i++)
						if(t == AbilityPassiveAbilities[i].GetType())
						{
							a = AbilityPassiveAbilities[i];
							AbilityPassiveAbilities.RemoveAt(i);

							found = true;
							break;
						}

					if(found)
						break;
				}
				else if(type == typeof(ActivationAbility))
				{
					bool found = false;

					if(AbilityDefault != null && t == AbilityDefault.GetType())
					{
						a = AbilityDefault;
						AbilityDefault = new NormalAttack(); // Costs 0 so there's no harm in adding it

						found = true;
					}
					else
						for(int i = 0;i < AbilityActivatableAbilities.Count;i++)
							if(t == AbilityActivatableAbilities[i].GetType())
							{
								a = AbilityActivatableAbilities[i];
								AbilityActivatableAbilities.RemoveAt(i);

								found = true;
								break;
							}
			
					if(found)
						break;
				}
				else if(type == typeof(Health))
				{
					a = AbilityHealth;
					AbilityHealth = new NormalHealth(); // Costs 0 so there's no harm in adding it

					break;
				}
				else if(type == typeof(Movement))
				{
					a = AbilityMovement;
					AbilityMovement = new GroundMovement(); // Costs 0 so there's no harm in adding it

					break;
				}
				else if(type == typeof(Visibility))
				{
					a = AbilityVisibility;
					AbilityVisibility = new Visible(); // Costs 0 so there's no harm in adding it

					break;
				}
				else if(type == typeof(Vision))
				{
					a = AbilityVision;
					AbilityVision = new NormalVision(); // Costs 0 so there's no harm in adding it

					break;
				}

			if(a != null)
				a.OnRemove(this);

			return;
		}

		/// <summary>
		/// Returns the cost of the ability with the given type.
		/// </summary>
		/// <param name="t">The type of the ability to get the cost of.</param>
		/// <returns>Returns the cost of the ability in question if this character has it or zero if not.</returns>
		protected int AbilityCost(Type t)
		{
			Ability a = null;
			Type[] interfaces = t.GetInterfaces();

			foreach(Type type in interfaces)
				if(type == typeof(PassiveAbility))
				{
					bool found = false;

					for(int i = 0;i < AbilityPassiveAbilities.Count;i++)
						if(t == AbilityPassiveAbilities[i].GetType())
						{
							a = AbilityPassiveAbilities[i];

							found = true;
							break;
						}

					if(found)
						break;
				}
				else if(type == typeof(ActivationAbility))
				{
					bool found = false;

					if(AbilityDefault != null && t == AbilityDefault.GetType()) // Never null so this is a fine call
					{
						a = AbilityDefault;
						found = true;
					}
					else
						for(int i = 0;i < AbilityActivatableAbilities.Count;i++)
							if(t == AbilityActivatableAbilities[i].GetType())
							{
								a = AbilityActivatableAbilities[i];

								found = true;
								break;
							}
			
					if(found)
						break;
				}
				else if(type == typeof(Health))
				{
					a = AbilityHealth;
					break;
				}
				else if(type == typeof(Movement))
				{
					a = AbilityMovement;
					break;
				}
				else if(type == typeof(Visibility))
				{
					a = AbilityVisibility;
					break;
				}
				else if(type == typeof(Vision))
				{
					a = AbilityVision;
					break;
				}

			if(a != null)
				return a.PointCost;

			return 0;
		}

		/// <summary>
		/// Gets the ability attached to this unit of the given type if any.
		/// </summary>
		/// <param name="ability">The ability to get.</param>
		/// <returns>Returns the ability if the unit has it and null if it does not.</returns>
		/// <remarks>Do not call until the basic abilities are initialized.</remarks>
		public Ability GetAbility(Type ability)
		{
			Type[] interfaces = ability.GetInterfaces();

			foreach(Type type in interfaces)
				if(type == typeof(PassiveAbility))
				{
					for(int i = 0;i < AbilityPassiveAbilities.Count;i++)
						if(ability == AbilityPassiveAbilities[i].GetType())
							return AbilityPassiveAbilities[i];
				}
				else if(type == typeof(ActivationAbility))
				{
					if(ability == AbilityDefault.GetType())
						return AbilityDefault;

					for(int i = 0;i < AbilityActivatableAbilities.Count;i++)
						if(ability == AbilityActivatableAbilities[i].GetType())
							return AbilityActivatableAbilities[i];
				}
				else if(type == typeof(Health))
				{
					if(ability == AbilityHealth.GetType())
						return AbilityHealth;
				}
				else if(type == typeof(Movement))
				{
					if(ability == AbilityMovement.GetType())
						return AbilityMovement;
				}
				else if(type == typeof(Visibility))
				{
					if(ability == AbilityVisibility.GetType())
						return AbilityVisibility;
				}
				else if(type == typeof(Vision))
				{
					if(ability == AbilityVision.GetType())
						return AbilityVision;
				}
			
			return null;
		}

		/// <summary>
		/// Determines if this unit has the provided ability.
		/// </summary>
		/// <param name="ability">The ability to check for.</param>
		/// <returns>Returns true if the unit has the ability and false otherwise.</returns>
		public bool HasAbility(Type ability)
		{
			Type[] interfaces = ability.GetInterfaces();

			foreach(Type type in interfaces)
				if(type == typeof(PassiveAbility))
				{
					for(int i = 0;i < AbilityPassiveAbilities.Count;i++)
						if(ability == AbilityPassiveAbilities[i].GetType())
							return true;
				}
				else if(type == typeof(ActivationAbility))
				{
					if(ability == AbilityDefault.GetType())
						return true;

					for(int i = 0;i < AbilityActivatableAbilities.Count;i++)
						if(ability == AbilityActivatableAbilities[i].GetType())
							return true;
				}
				else if(type == typeof(Health))
				{
					if(ability == AbilityHealth.GetType())
						return true;
				}
				else if(type == typeof(Movement))
				{
					if(ability == AbilityMovement.GetType())
						return true;
				}
				else if(type == typeof(Visibility))
				{
					if(ability == AbilityVisibility.GetType())
						return true;
				}
				else if(type == typeof(Vision))
				{
					if(ability == AbilityVision.GetType())
						return true;
				}
			
			return false;
		}

		/// <summary>
		/// Obtains all the abilities that this unit has (that are visible to users).
		/// </summary>
		/// <param name="get_hidden">If true then hidden abilities will also be added to the list.</param>
		/// <param name="append_level_suffix">If true then after every ability name the following will be appended ([Level]/[MaxLevel]).</param>
		/// <param name="append_cost_suffix">If true then after every ability name the following will be appended [[Point Cost]]. This is after the level suffix if it is present.</param>
		/// <returns>Returns a list of all visible abilities. Order is garunteed to be the same as a call to GetDocumentation.</returns>
		public List<string> GetAbilities(bool get_hidden = false, bool append_level_suffix = false, bool append_cost_suffix = false)
		{
			List<string> ret = new List<string>();

			// The following four should always be visible but just in case we'll check anyways
			if(AbilityHealth.VisibleToUser || get_hidden)
				ret.Add(AbilityHealth.Name + (append_level_suffix ? " (" + AbilityHealth.Level + "/" + AbilityHealth.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + AbilityHealth.PointCost + "]" : ""));

			if(AbilityMovement.VisibleToUser || get_hidden)
				ret.Add(AbilityMovement.Name + (append_level_suffix ? " (" + AbilityMovement.Level + "/" + AbilityMovement.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + AbilityMovement.PointCost + "]" : ""));
			
			if(AbilityVision.VisibleToUser || get_hidden)
				ret.Add(AbilityVision.Name + (append_level_suffix ? " (" + AbilityVision.Level + "/" + AbilityVision.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + AbilityVision.PointCost + "]" : ""));
			
			if(AbilityVisibility.VisibleToUser || get_hidden)
				ret.Add(AbilityVisibility.Name + (append_level_suffix ? " (" + AbilityVisibility.Level + "/" + AbilityVisibility.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + AbilityVisibility.PointCost + "]" : ""));
			
			if(AbilityDefault.VisibleToUser || get_hidden)
				ret.Add(AbilityDefault.Name + (append_level_suffix ? " (" + AbilityDefault.Level + "/" + AbilityDefault.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + AbilityDefault.PointCost + "]" : ""));

			foreach(Ability a in AbilityActivatableAbilities)
				if(a.VisibleToUser || get_hidden)
					ret.Add(a.Name + (append_level_suffix ? " (" + a.Level + "/" + a.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + a.PointCost + "]" : ""));

			foreach(Ability a in AbilityPassiveAbilities)
				if(a.VisibleToUser || get_hidden)
					ret.Add(a.Name + (append_level_suffix ? " (" + a.Level + "/" + a.MaxLevel + ")" : "") + (append_cost_suffix ? " [" + a.PointCost + "]" : ""));

			return ret;
		}

		/// <summary>
		/// Obtains all the documentation for all abilities that this unit has (that are visible to users).
		/// </summary>
		/// <param name="get_hidden">If true then hidden ability's documentation will also be added to the list.</param>
		/// <returns>Returns a list of all documentation for this unit. Order is garunteed to be the same as a call to GetAbilities.</returns>
		public List<string> GetDocumentation(bool get_hidden = false)
		{
			List<string> ret = new List<string>();

			// The following four should always be visible but just in case we'll check anyways
			if(AbilityHealth.VisibleToUser || get_hidden)
				ret.Add(AbilityHealth.Documentation);

			if(AbilityMovement.VisibleToUser || get_hidden)
				ret.Add(AbilityMovement.Documentation);
			
			if(AbilityVision.VisibleToUser || get_hidden)
				ret.Add(AbilityVision.Documentation);
			
			if(AbilityVisibility.VisibleToUser || get_hidden)
				ret.Add(AbilityVisibility.Documentation);

			if(AbilityDefault.VisibleToUser || get_hidden)
				ret.Add(AbilityDefault.Documentation);
			
			foreach(Ability a in AbilityActivatableAbilities)
				if(a.VisibleToUser || get_hidden)
					ret.Add(a.Documentation);

			foreach(Ability a in AbilityPassiveAbilities)
				if(a.VisibleToUser || get_hidden)
					ret.Add(a.Documentation);

			return ret;
		}

		/// <summary>
		/// Changes the name of this unit.
		/// </summary>
		/// <param name="new_name">The new name for this unit.</param>
		public void Rename(string new_name)
		{
			Name = new_name;
			return;
		}

		/// <summary>
		/// Changes the points available to this unit.
		/// The change will not be made if the unit currently costs more than the new total.
		/// </summary>
		/// <param name="new_total">The new number of points this unit has.</param>
		/// <returns>Returns true if the change was made and false if it could not be made.</returns>
		public bool ChangePoints(int new_total)
		{
			if(new_total < PointsUsed)
				return false;

			PointTotal = new_total;
			return true;
		}

		/// <summary>
		/// Determines if this unit is the leader of a team.
		/// </summary>
		/// <returns>Returns true if this unit is a leader and false otherwise.</returns>
		public bool IsLeader()
		{
			foreach(PassiveAbility pa in AbilityPassiveAbilities)
				if(pa.GetType() == typeof(Leadership))
					return true;

			return false;
		}

		/// <summary>
		/// Calls the appropriate function of all passive abilities this unit has.
		/// If a parameter is not used by the appropriate phase's passive ability it is garunteed not to be used.
		/// </summary>
		/// <param name="phase">Turn phase (or event) of the turn.</param>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of this unit.</param>
		/// <param name="f">An additional parameter which represents something like damage or attack strength.</param>
		/// <param name="type">The type of attack. If not important, the parameter will not be used.</param>
		/// <param name="u">An additional parameter for some special unit such as the unit that kills this unit or a unit this unit kills. If not important, the parameter will not be used.</param>
		public void TriggerPassives(TurnPhase phase, GameState state, Move m, ref float f, ref ATTACK_TYPE type, Unit u = null)
		{
			switch(phase)
			{
			case TurnPhase.AFTER_ABILITY:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.AfterAbility(state,m);

				break;
			case TurnPhase.AFTER_ATTACK:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.AfterAttack(state,m,u,f);

				break;
			case TurnPhase.AFTER_DAMAGE:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.AfterDamage(state,m,u,f);

				break;
			case TurnPhase.AFTER_HEAL:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.AfterHeal(state,m,f);

				break;
			case TurnPhase.AFTER_MOVE:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.AfterMove(state,m);

				break;
			case TurnPhase.AFTER_POST_MOVE:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.AfterPostMove(state,m);

				break;
			case TurnPhase.BEFORE_ABILITY:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.BeforeAbility(state,m);

				break;
			case TurnPhase.BEFORE_ATTACK:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.BeforeAttack(state,m,ref f,ref type);

				break;
			case TurnPhase.BEFORE_DAMAGE:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.BeforeDamage(state,m,ref f,type);

				break;
			case TurnPhase.BEFORE_HEAL:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.BeforeHeal(state,m,ref f);

				break;
			case TurnPhase.BEFORE_MOVE:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.BeforeMove(state,m);

				break;
			case TurnPhase.BEFORE_POST_MOVE:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.BeforePostMove(state,m);

				break;
			case TurnPhase.ON_DEATH:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.OnDeath(state,m,u);

				break;
			case TurnPhase.ON_KILL:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.OnKill(state,m,u);

				break;
			case TurnPhase.EOT:
				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					pa.EndOfTurn(state,m);

				break;
			}

			return;
		}

		/// <summary>
		/// Activates the default ability of this unit.
		/// </summary>
		/// <param name="target">The target of the ability.</param>
		/// <returns>Returns an activation of the default ability of this unit.</returns>
		public ActivatedAbility TriggerDefaultAbility(Pair<int,int> target)
		{return AbilityDefault.Activate(target);}

		/// <summary>
		/// Activates the ability with the given name if such an ability exists and returns null otherwise.
		/// </summary>
		/// <param name="name">The name of the ability to use.</param>
		/// <param name="target">The target of the ability.</param>
		/// <returns>Returns an activation of the appropriate ability or null if no such ability exists.</returns>
		public ActivatedAbility ActivateAbility(string name, Pair<int,int> target)
		{
			if(AbilityDefault.Name == name)
				return TriggerDefaultAbility(target);

			foreach(ActivationAbility a in AbilityActivatableAbilities)
				if(a.Name == name)
					return a.Activate(target);

			return null;
		}

		/// <summary>
		/// Obtains a list of the names of all abilities that this unit can activate.
		/// </summary>
		/// <returns>Returns a list of the names of all abilities that this unit can activate.</returns>
		public List<string> GetActivatableAbilityNames()
		{
			List<string> ret = new List<string>();
			ret.Add(AbilityDefault.Name);

			foreach(ActivationAbility a in AbilityActivatableAbilities)
				ret.Add(a.Name);

			return ret;
		}

		/// <summary>
		/// Removes all status effects placed on this unit.
		/// </summary>
		public void RemoveAllStatusEffects()
		{
			for(int i = 0;i < AbilityPassiveAbilities.Count;i++)
				if(AbilityPassiveAbilities[i] is StatusEffect)
					AbilityPassiveAbilities.RemoveAt(i--);

			return;
		}

		/// <summary>
		/// Serializes this unit into a string.
		/// </summary>
		/// <returns>Returns a string that contains all the information needed to recreate this unit.</returns>
		public override string ToString()
		{return ToString(false);}

		/// <summary>
		/// Serializes this unit into a string.
		/// </summary>
		/// <param name="add_hidden">If true then hidden abilities will be added to the string and if false they will be ignored.</param>
		/// <returns>Returns a string that contains all the information needed to recreate this unit.</returns>
		public string ToString(bool add_hidden)
		{
			string ret = Name + "\n" + PointTotal.ToString() + "\n";

			if(AbilityHealth.VisibleToUser || add_hidden)
				ret += AbilityHealth.Name + "\n" + AbilityHealth.Level.ToString() + "\n";

			if(AbilityMovement.VisibleToUser || add_hidden)
				ret += AbilityMovement.Name + "\n" + AbilityMovement.Level.ToString() + "\n";
			
			if(AbilityVision.VisibleToUser || add_hidden)
				ret += AbilityVision.Name + "\n" + AbilityVision.Level.ToString() + "\n";
			
			if(AbilityVisibility.VisibleToUser || add_hidden)
				ret += AbilityVisibility.Name + "\n" + AbilityVisibility.Level.ToString() + "\n";

			if(AbilityDefault.VisibleToUser || add_hidden)
				ret += AbilityDefault.Name + "\n" + AbilityDefault.Level.ToString() + "\n";

			foreach(Ability a in AbilityPassiveAbilities)
				if(a.VisibleToUser || add_hidden)
					ret += a.Name + "\n" + a.Level + "\n";

			foreach(Ability a in AbilityActivatableAbilities)
				if(a.VisibleToUser || add_hidden)
					ret += a.Name + "\n" + a.Level + "\n";

			// The last character is always an unwanted new line
			return ret.Substring(0,ret.Length - 1);
		}

		/// <summary>
		/// The name of the unit.
		/// </summary>
		public string Name
		{get; set;}

		/// <summary>
		/// The total number of points this unit can have in abilities.
		/// </summary>
		public int PointTotal
		{get; protected set;}

		/// <summary>
		/// The number of points this unit has unallocated.
		/// </summary>
		public int PointsFree
		{
			get
			{
				int used = 0;

				if(AbilityHealth != null)
					used += AbilityHealth.PointCost;
				
				if(AbilityMovement != null)
					used += AbilityMovement.PointCost;
				
				if(AbilityVision != null)
					used += AbilityVision.PointCost;
				
				if(AbilityVisibility != null)
					used += AbilityVisibility.PointCost;

				if(AbilityDefault != null)
					used += AbilityDefault.PointCost;

				foreach(PassiveAbility pa in AbilityPassiveAbilities)
					used += pa.PointCost;

				foreach(ActivationAbility aa in AbilityActivatableAbilities)
					used += aa.PointCost;

				return PointTotal - used;
			}
		}

		/// <summary>
		/// The total point cost of all abilities this unit has.
		/// </summary>
		public int PointsUsed
		{
			get
			{return PointTotal - PointsFree;}
		}

		/// <summary>
		/// This unit's health is represented by this.
		/// </summary>
		public Health AbilityHealth
		{get; protected set;}

		/// <summary>
		/// This unit's ability to move is represented by this.
		/// </summary>
		public Movement AbilityMovement
		{get; protected set;}

		/// <summary>
		/// This unit's ability to see is represented by this.
		/// </summary>
		public Vision AbilityVision
		{get; protected set;}

		/// <summary>
		/// This unit's ability to stay undercover.
		/// </summary>
		public Visibility AbilityVisibility
		{get; protected set;}

		/// <summary>
		/// This unit's default ability.
		/// </summary>
		public ActivationAbility AbilityDefault
		{get; protected set;}

		/// <summary>
		/// Contains all passive abilities this unit has.
		/// </summary>
		public List<PassiveAbility> AbilityPassiveAbilities
		{get; protected set;}

		/// <summary>
		/// Contains all the activatable abilities this unit has.
		/// </summary>
		public List<ActivationAbility> AbilityActivatableAbilities
		{get; protected set;}

		/// <summary>
		/// If true then this unit can move during the post movement phase.
		/// </summary>
		public bool MovesDuringPostMovement
		{
			get
			{
				if(AbilityMovement != null)
					return AbilityMovement.Mounted;

				return false;
			}
		}
	}
}
