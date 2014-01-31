using System;
using System.Collections.Generic;

namespace TheGame.Parsers
{
	/// <summary>
	/// Takes a string and turns it into tokens.
	/// </summary>
	public class StringTokenizer
	{
		/// <summary>
		/// Creates a new set of tokens from the provided string.
		/// </summary>
		/// <param name="str">The source string.</param>
		/// <param name="delimiter">The series of characters used to seperate tokens.</param>
		public StringTokenizer(string str, string delimiter = " ")
		{
			tokens = new List<string>();
			delim = delimiter;

			while(str != "")
				tokens.Add(Tokenize(ref str));

			return;
		}

		/// <summary>
		/// Obtains the next available token and removes it from this StringTokenizer.
		/// </summary>
		/// <returns>Returns the next token.</returns>
		/// <exception cref="InvalidOperationException"></exception>
		public string NextToken()
		{
			if(TokensLeft < 1)
				throw new InvalidOperationException("No tokens remain in the StringTokenizer.");
			
			string ret = tokens[0];
			tokens.RemoveAt(0);

			return ret;
		}

		/// <summary>
		/// Obtains the next available token but does not remove it from the list of available tokens.
		/// </summary>
		/// <returns>Returns the next token.</returns>
		/// <exception cref="InvalidOperationException"></exception>
		public string PeekToken()
		{
			if(TokensLeft < 1)
				throw new InvalidOperationException("No tokens remain in the StringTokenizer.");

			return tokens[0];
		}

		/// <summary>
		/// Returns the remaining tokens as a string.
		/// </summary>
		/// <returns></returns>
		public override string ToString()
		{
			string ret = "";

			for(int i = 0;i < tokens.Count;i++)
				ret += tokens[i] + delim;

			return ret;
		}

		/// <summary>
		/// Returns the next token from the provided string and removes it from the string.
		/// </summary>
		/// <param name="str">The string to fetch the next token from.</param>
		protected string Tokenize(ref string str)
		{
			int index = str.IndexOf(delim);
			string ret;

			if(index < 0)
			{
				ret = str;
				str = "";
				
				return ret;
			}

			ret = str.Substring(0,index);
			str = str.Substring(index + delim.Length);

			return ret;
		}

		/// <summary>
		/// The number of tokens remaining.
		/// </summary>
		public int TokensLeft
		{
			get
			{return tokens.Count;}
		}

		/// <summary>
		/// Contains the remaining tokens.
		/// </summary>
		protected List<string> tokens;

		/// <summary>
		/// The delimiter used to seperate tokens.
		/// </summary>
		protected string delim;
	}
}
