#pragma warning disable 0162

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace Twilight
{
	public class Program
	{
		public static void Main(string[] args)
		{
			new Program();
			return;
		}

		private Program()
		{
			t = new Thread(Feedback);
			t.IsBackground = true;

			t.Start();
			Twilight();

			return;
		}

#region Feedback Functions
		public void Feedback()
		{
			while(true)
			{
				ConsoleKeyInfo key = Console.ReadKey(true);
				InputType type = DetermineInputType(key);

				if(type == InputType.COMMAND && key.Key == ConsoleKey.C || type == InputType.ESCAPE)
				{
					run = false;
					break;
				}
				else if(type == InputType.AUTO_COMPLETE)
					IncUtility();

				// Eh, nothing to do here yet
			}

			return;
		}

#region Input Helper Functions
		/// <summary>
		/// Determines the input type of the provided key stroke.
		/// </summary>
		/// <param name="key">The key stroke to consider.</param>
		/// <returns>Returns the type of input that the key stroke is.</returns>
		protected InputType DetermineInputType(ConsoleKeyInfo key)
		{
			if((key.Modifiers & (ConsoleModifiers.Control | ConsoleModifiers.Alt)) != 0)
				return InputType.COMMAND;

			if(key.Key == ConsoleKey.UpArrow || key.Key == ConsoleKey.DownArrow || key.Key == ConsoleKey.LeftArrow || key.Key == ConsoleKey.RightArrow)
				return InputType.ARROW;

			if(key.Key == ConsoleKey.Home)
				return InputType.HOME;

			if(key.Key == ConsoleKey.End)
				return InputType.END;

			if(key.KeyChar == '\u0000')
				return InputType.OTHER;

			if(key.Key == ConsoleKey.Tab)
				return InputType.AUTO_COMPLETE;

			if(key.Key == ConsoleKey.Backspace)
				return InputType.BACKSPACE;

			if(key.Key == ConsoleKey.Enter)
				return InputType.TERMINATE_LINE;

			if(key.Key == ConsoleKey.Escape)
				return InputType.ESCAPE;

			return InputType.TEXT;
		}

		/// <summary>
		/// Represents the type of key that is a character represents.
		/// </summary>
		protected enum InputType
		{
			COMMAND = 0,
			TEXT = 1,
			BACKSPACE = 2,
			AUTO_COMPLETE = 3,
			TERMINATE_LINE = 4,
			ARROW = 5,
			ESCAPE = 6,
			HOME = 7,
			END = 8,
			OTHER = 9
		}
#endregion
#endregion

#region Twilight Functions
		public void Twilight()
		{
			while(run)
			{
				
			}

			return;
		}

		public void IncUtility(int amount = 1)
		{
			// Eh, nothing to do here yet

			utility += amount;
			return;
		}

#region Twilight Data
		protected Normal nrand = new Normal();

		protected Thread t;
		protected bool run = true;
		protected int utility = 0;
#endregion
#endregion
	}
}
