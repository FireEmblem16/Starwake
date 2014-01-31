using System;
using System.Collections.Generic;

namespace HittingSet
{
	public class Program
	{
		public static void Main(string[] args)
		{
			UnionFind u = new UnionFind();
			
			SetElement[] possible_elements = new SetElement[50];
			Random rand = new Random();

			for(int i = 0;i < 50;i++)
				possible_elements[i] = new SetElement(42);

			for(int i = 0;i < 30;i++)
			{
				List<SetElement> elements = new List<SetElement>();
				int num_to_add = rand.Next(5) + 1;

				for(int j = 0;j < num_to_add;j++)
				{
					int next = rand.Next(20);

					// It does matter that we don't double add
					if(!elements.Contains(possible_elements[next]))
						elements.Add(possible_elements[next]);
					else
						j--;
				}

				u.AddSet(new RepresentativeSet(elements));
			}

			Console.WriteLine(u + "\n");

			for(int i = 0;i < u.NumSets();i++)
				for(int j = i + 1;j < u.NumSets();j++)
				{
					RepresentativeSet r = u.Union(u.GetSet(i),u.GetSet(j));

					if(r != null)
						j--;
				}

			Console.WriteLine(u);
			return;
		}
	}

	public class UnionFind
	{
		public UnionFind()
		{
			sets = new List<RepresentativeSet>();
			return;
		}

		public RepresentativeSet GetSet(int i)
		{
			if(i < 0 || i >= sets.Count)
				return null;

			return sets[i];
		}

		public void AddSet(RepresentativeSet s)
		{
			sets.Add(s);
			return;
		}

		public RepresentativeSet Find(RepresentativeSet s)
		{
			if(s.parent == s)
				return s;

			return s.parent = Find(s.parent);
		}

		public RepresentativeSet Union(RepresentativeSet s, RepresentativeSet t)
		{
			if(s.parent == t.parent)
				return null;

			if(!s.CanContain(t.representative))
				return null;

			if(!sets.Remove(t))
				return null;

			s.Union(t);
			t.parent = Find(s.parent);
			
			return t.parent;
		}

		public int NumSets()
		{
			return sets.Count;
		}

		public override string ToString()
		{
			return ToString(true,true,false);
		}

		public string ToString(bool use_newlines, bool show_elements, bool show_objvals)
		{
			if(sets.Count == 0)
				return "{}";

			string ret = "{" + sets[0].ToString(show_elements,show_objvals);

			if(sets.Count == 1)
				return ret + "}";

			for(int i = 1;i < sets.Count;i++)
				if(use_newlines)
					ret += ",\n " + sets[i].ToString(show_elements,show_objvals);
				else
					ret += ", " + sets[i].ToString(show_elements,show_objvals);

			return ret + "}";
		}

		protected List<RepresentativeSet> sets;
	}

	public class RepresentativeSet
	{
		public RepresentativeSet(List<SetElement> Elements)
		{
			elements = new List<SetElement>(Elements.Count);
			elements.AddRange(Elements);

			SetID = GetSetID();
			
			sets_in_set = new List<long>();
			sets_in_set.Add(SetID);

			representative = 1;

			foreach(SetElement e in elements)
				representative *= e.representing_value;

			parent = this;
			return;
		}

		public bool CanContain(long e)
		{
			if(GCD(representative,e) == 1)
				return false;

			return true;
		}

		public bool Union(RepresentativeSet s)
		{
			if(!CanContain(s.representative))
				return false;

			representative = GCD(representative,s.representative);
			sets_in_set.Add(s.SetID);

			// Do book keeping
			foreach(SetElement e in s.elements)
				if(!elements.Contains(e))
					elements.Add(e);
			
			return true;
		}

		public override string ToString()
		{
			return ToString(true,false);
		}

		public string ToString(bool show_elements, bool show_objvals)
		{
			string sets_string = "{";

			if(sets_in_set.Count == 1)
				sets_string += sets_in_set[0] + "}";
			else
			{
				sets_string += sets_in_set[0];

				for(int i = 1;i < sets_in_set.Count;i++)
					sets_string += ", " + sets_in_set[i];

				sets_string += "}";
			}

			if(!show_elements)
				return "{" + representative + " | " + SetID + " | " + sets_string + "}";

			// There is at least one element
			string ret = "{" + representative + " | " + SetID + " | " + sets_string + " | " + elements[0].ToString(show_objvals);
			
			if(elements.Count == 1)
				return ret + "}";

			for(int i = 1;i < elements.Count;i++)
				ret += ", " + elements[i].ToString(show_objvals);

			return ret + "}";
		}

		public long GCD(long a, long b)
		{
			long left_i;
			long left;
			long right;

			if(a > b)
			{
				left_i = a;
				left = a;
				right = b;
			}
			else
			{
				left_i = b;
				left = b;
				right = a;
			}

			while(true)
			{
				while(left >= right)
					left -= right;

				if(left == 0)
					break;

				left_i = right;
				right = left;
				left = left_i;
			}

			return right;
		}

		protected static long GetSetID()
		{
			return ++last_set_ID;
		}

		protected static long last_set_ID = -1;

		public long representative
		{get; protected set;}

		protected long SetID;
		protected List<long> sets_in_set;

		public RepresentativeSet parent;
		protected List<SetElement> elements;
	}

	public class SetElement
	{
		public SetElement(object val)
		{
			value = val;
			representing_value = GenerateRepresentingValue();

			return;
		}

		public SetElement(SetElement e)
		{
			value = e.value;
			representing_value = e.representing_value;

			return;
		}

		protected static long GenerateRepresentingValue()
		{
			return representatives[++last_representative_generated];
		}

		public override string ToString()
		{
			return ToString(false);
		}

		public string ToString(bool show_obj)
		{
			if(show_obj)
				return "(" + value.ToString() + ", " + representing_value.ToString() + ")";

			return representing_value.ToString();
		}

		protected object value;
		public long representing_value
		{get; protected set;}

		// I don't feel like writing a prime generator so here we go
		protected static long last_representative_generated = -1;
		protected static long[] representatives = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211,223,227,229,233,239,241,251,257,263,269,271,277,281,283,293,301,311,313,317,331,337,347,349};
	}
}
