using System;
using System.Collections.Generic;
using System.Reflection;

namespace TheGame.Utility
{
	/// <summary>
	/// A generic factory of types.
	/// S is the type used to indentify which construction function to use.
	/// </summary>
	public class Factory<S> where S : IComparable
	{
		/// <summary>
		/// Initializes the class data.
		/// </summary>
		public Factory()
		{
			generators = new List<Pair<S,MethodInfo>>();
			comp = new SComparer();
			
			return;
		}

		/// <summary>
		/// Registers a new command in the factory.
		/// </summary>
		/// <param name="p">The item to add. The first value is the identifier. The second value is the generator that will produce the command.</param>
		public void Register(Pair<S,MethodInfo> p)
		{
			p = new Pair<S,MethodInfo>(p.val1,p.val2);
			int index = generators.BinarySearch(p,comp);

			// We don't want duplicates
			if(index >= 0)
				return;

			generators.Insert(~index,p);
			return;
		}

		/// <summary>
		/// Remvoes an item from the factory.
		/// </summary>
		/// <param name="s">The item to remove.</param>
		public void Unregister(S s)
		{
			int index = generators.BinarySearch(new Pair<S,MethodInfo>(s,null),comp);

			if(index >= 0)
				generators.RemoveAt(index);

			return;
		}

		/// <summary>
		/// Unregisters all items.
		/// </summary>
		public void UnregisterAll()
		{
			generators.Clear();
			return;
		}

		/// <summary>
		/// Creates a new item from the given information.
		/// </summary>
		/// <param name="s">The item to create.</param>
		/// <param name="parameters">Any additional parameters the item will need to be created.</param>
		/// <returns>Returns an instance of the appropriate item or null if the item type was not registered.</returns>
		public object CreateItem(S s, object[] parameters)
		{
			int index = generators.BinarySearch(new Pair<S,MethodInfo>(s,null),comp);
			
			if(index >= 0)
				return generators[index].val2.Invoke(null,parameters);

			return null;
		}

		/// <summary>
		/// Creates a list of items using ToString on the identifiers.
		/// </summary>
		/// <returns>Returns a string listing all items.</returns>
		public string List()
		{
			string ret = "";

			for(int i = 0;i < generators.Count;i++)
				ret += generators[i].val1.ToString() + ", ";

			return ret.Substring(0,ret.Length - 2);
		}

		/// <summary>
		/// Creates a list of available items.
		/// </summary>
		/// <returns>Returns a list of identifiers listing all the items.</returns>
		public List<S> Items()
		{
			List<S> ret = new List<S>();

			for(int i = 0;i < generators.Count;i++)
				ret.Add(generators[i].val1);

			return ret;
		}

		/// <summary>
		/// All the item generators.
		/// </summary>
		protected List<Pair<S,MethodInfo>> generators;

		/// <summary>
		/// Just so we don't have to keep making these and overworking the garbage collector.
		/// </summary>
		protected SComparer comp;

		/// <summary>
		/// Compares elements of our registered items to keep them sorted.
		/// </summary>
		protected class SComparer : IComparer<Pair<S,MethodInfo>>
		{
			/// <summary>
			/// Compares the given objects.
			/// </summary>
			/// <param name="x">The first object.</param>
			/// <param name="y">The second object.</param>
			/// <returns>Returns negative if the name of the first command is less than the second, zero if equal, and positive if the first is greater than the second.</returns>
			public int Compare(Pair<S,MethodInfo> x, Pair<S,MethodInfo> y)
			{return x.val1.CompareTo(y.val1);}
		}
	}

	/// <summary>
	/// A generic factory of types.
	/// S is the type used to indentify which construction function to use.
	/// T is the lowest type that can be created. If the provided generators do not return a type that can be casted to this type then defaults will be returned instead of exceptions being thrown.
	/// </summary>
	public class Factory<S,T> where S : IComparable
	{
		/// <summary>
		/// Initializes the class data.
		/// </summary>
		public Factory()
		{
			generators = new List<Pair<S,MethodInfo>>();
			comp = new SComparer();
			
			return;
		}

		/// <summary>
		/// Registers a new command in the factory.
		/// </summary>
		/// <param name="p">The item to add. The first value is the identifier. The second value is the generator that will produce the command.</param>
		public void Register(Pair<S,MethodInfo> p)
		{
			p = new Pair<S,MethodInfo>(p.val1,p.val2);
			int index = generators.BinarySearch(p,comp);

			// We don't want duplicates
			if(index >= 0)
				return;

			generators.Insert(~index,p);
			return;
		}

		/// <summary>
		/// Remvoes an item from the factory.
		/// </summary>
		/// <param name="s">The item to remove.</param>
		public void Unregister(S s)
		{
			int index = generators.BinarySearch(new Pair<S,MethodInfo>(s,null),comp);

			if(index >= 0)
				generators.RemoveAt(index);

			return;
		}

		/// <summary>
		/// Unregisters all items.
		/// </summary>
		public void UnregisterAll()
		{
			generators.Clear();
			return;
		}

		/// <summary>
		/// Creates a new item from the given information.
		/// </summary>
		/// <param name="s">The item to create.</param>
		/// <param name="parameters">Any additional parameters the item will need to be created.</param>
		/// <returns>Returns an instance of the appropriate item or null if the item type was not registered.</returns>
		public T CreateItem(S s, object[] parameters)
		{
			int index = generators.BinarySearch(new Pair<S,MethodInfo>(s,null),comp);
			
			if(index >= 0)
				try
				{return (T)generators[index].val2.Invoke(null,parameters);}
				catch
				{}

			return default(T);
		}

		/// <summary>
		/// Creates a list of items using ToString on the identifiers.
		/// </summary>
		/// <returns>Returns a string listing all items.</returns>
		public string List()
		{
			string ret = "";

			for(int i = 0;i < generators.Count;i++)
				ret += generators[i].val1.ToString() + ", ";

			return ret.Substring(0,ret.Length - 2);
		}

		/// <summary>
		/// Creates a list of available items.
		/// </summary>
		/// <returns>Returns a list of identifiers listing all the items.</returns>
		public List<S> Items()
		{
			List<S> ret = new List<S>();

			for(int i = 0;i < generators.Count;i++)
				ret.Add(generators[i].val1);

			return ret;
		}

		/// <summary>
		/// All the item generators.
		/// </summary>
		protected List<Pair<S,MethodInfo>> generators;

		/// <summary>
		/// Just so we don't have to keep making these and overworking the garbage collector.
		/// </summary>
		protected SComparer comp;

		/// <summary>
		/// Compares elements of our registered items to keep them sorted.
		/// </summary>
		protected class SComparer : IComparer<Pair<S,MethodInfo>>
		{
			/// <summary>
			/// Compares the given objects.
			/// </summary>
			/// <param name="x">The first object.</param>
			/// <param name="y">The second object.</param>
			/// <returns>Returns negative if the name of the first command is less than the second, zero if equal, and positive if the first is greater than the second.</returns>
			public int Compare(Pair<S,MethodInfo> x, Pair<S,MethodInfo> y)
			{return x.val1.CompareTo(y.val1);}
		}
	}
}
