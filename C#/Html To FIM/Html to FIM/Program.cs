using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;

namespace Html_to_FIM
{
	public class Program
	{
		public static void Main(string[] args)
		{
			string file = Console.In.ReadLine();

			if(!File.Exists(file))
				return;
			
			string fin = File.ReadAllText(file);
			Scanner tkns = new Scanner(fin,new string[] {"<",">","\r\n","\n\r","\n","\r"});
			
			Stack<Format> fmt = new Stack<Format>();
			fmt.Push(new Format()); // This way the stack will never be empty

			string fout = Format.GenerateDifference(null,fmt.Peek());
			bool in_p = false;

			while(tkns.HasNext<string>())
			{
				string str = tkns.Next<string>();
				
				if(str.Contains("hoofwritten"))
					str = str;

				if(in_p)
				{
					if(str.StartsWith("span"))
					{
						Format f = new Format();

						if(str.Contains("font-style:italic;"))
							f.italics = true;

						if(str.Contains("font-weight:600;"))
							f.bold = true;

						if(str.Contains("text-decoration: underline;"))
							f.underline = true;

						fout += Format.GenerateDifference(fmt.Peek(),f);
						fmt.Push(f);
					}
					else if(str == "/span")
					{
						Format f = fmt.Pop();
						fout += Format.GenerateDifference(f,fmt.Peek());
					}
					else if(str == "br /")
					{
						fout += "\r\n";

						tkns.Next<string>();
						in_p = false;
					}
					else if(str == "/p")
					{
						fout += "\r\n\r\n";
						in_p = false;
					}
					else
						fout += str;
				}
				else if(str.StartsWith("p style=\"") || str.StartsWith("p align=\""))
				{
					if(str.Contains("center"))
					{
						fout += "[hr]\r\n\r\n";

						tkns.Next<string>();
						tkns.Next<string>();
					}
					else
						in_p = true;
				}
			}

			File.WriteAllText(output,fout);
			return;
		}

		public static string output = "out.txt";
	}

	public class Format
	{
		public Format()
		{return;}

		public Format(Format format)
		{
			Copy(format);
			return;
		}

		public Format(Format format, bool italics)
		{
			Copy(format);
			this.italics = italics;

			return;
		}

		public Format(Format format, bool italics, bool bold)
		{
			Copy(format);

			this.italics = italics;
			this.bold = bold;

			return;
		}

		protected void Copy(Format format)
		{
			italics = format.italics;
			bold = format.bold;
			underline = format.underline;
			strikethrough = format.strikethrough;
			align = format.align;

			return;
		}

		/// <summary>
		/// Generates the tags necessary to go from then to now.
		/// </summary>
		/// <param name="then">The old format. Can be null.</param>
		/// <param name="now">The new format. Can be null.</param>
		/// <returns>Returns the tags needed to go from then to now.</returns>
		public static string GenerateDifference(Format then, Format now)
		{
			string ret = "";

			if(then == now == null)
				return ret;
			else if(then == null)
			{
				if(now.italics)
					ret += "[i]";
					
				if(now.bold)
					ret += "[b]";

				if(now.underline)
					ret += "[u]";

				if(now.strikethrough)
					ret += "[s]";

				switch(now.align)
				{
				case ALIGNMENT.LEFT:
					break;
				case ALIGNMENT.CENTER:
					ret += "[center]";
					break;
				case ALIGNMENT.RIGHT:
					ret += "[right]";
					break;
				}
			}
			else if(now == null)
			{
				// Undo in reverse order of application
				switch(then.align)
				{
				case ALIGNMENT.LEFT:
					break;
				case ALIGNMENT.CENTER:
					ret += "[/center]";
					break;
				case ALIGNMENT.RIGHT:
					ret += "[/right]";
					break;
				}

				if(then.strikethrough)
					ret += "[/s]";

				if(then.underline)
					ret += "[/u]";

				if(then.bold)
					ret += "[/b]";
				
				if(then.italics)
					ret += "[/i]";
			}
			else
				ret += GenerateDifference(then,null) + GenerateDifference(null,now); // Undo formatting and then add it back in

			return ret;
		}

		public bool italics = false;
		public bool bold = false;
		public bool underline = false;
		public bool strikethrough = false;

		public ALIGNMENT align = ALIGNMENT.LEFT;
	}

	public enum ALIGNMENT
	{
		LEFT = 0,
		CENTER = 1,
		RIGHT = 2
	}
}
