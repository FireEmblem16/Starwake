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
			if(!File.Exists(file))
				return;
			
			string fin = File.ReadAllText(file);
			Scanner tkns = new Scanner(fin,new string[] {"<",">","\r\n","\n\r","\n","\r"});
			
			Stack<Format> fmt = new Stack<Format>();
			fmt.Push(new Format()); // This way the stack will never be empty

			string fout = Format.GenerateDifference(null,fmt.Peek());
			bool in_p = false;
			bool start_sub = false;
			
			while(tkns.HasNext<string>())
			{
				string str = tkns.Next<string>();

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
					else if(str == "br /" || str == "[br]")
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
					else if(str == "[d]")
					{
						fout += "-----------------------------------------------";
						start_sub = true;
					}
					else if(str == "[/d]")
					{
						fout = fout.Substring(0,fout.Length - 2);
						fout += "-----------------------------------------------";
					}
					else if(fmt.Peek().bold)
						fout += str.Replace("&quot;","\"").Replace("—","–").Replace("^0","⁰").Replace("^1","¹").Replace("^2","²").Replace("^3","³").Replace("^4","⁴").Replace("^5","⁵").Replace("^6","⁶").Replace("^7","⁷").Replace("^8","⁸").Replace("^9","⁹");
					else
						fout += str.Replace("&quot;","\"").Replace("—","–").Replace("^0","[b]⁰[/b]").Replace("^1","[b]¹[/b]").Replace("^2","[b]²[/b]").Replace("^3","[b]³[/b]").Replace("^4","[b]⁴[/b]").Replace("^5","[b]⁵[/b]").Replace("^6","[b]⁶[/b]").Replace("^7","[b]⁷[/b]").Replace("^8","[b]⁸[/b]").Replace("^9","[b]⁹[/b]");
				}
				else if(str.StartsWith("p style=\"") || str.StartsWith("p align=\""))
				{
					if(start_sub)
					{
						fout = fout.Substring(0,fout.Length - 2);
						start_sub = false;
					}

					if(str.Contains("center"))
					{
						if(fout != "") // I'm tired of having a bunch of [hr]s at the beginning of every output
							fout += "[hr]\r\n\r\n";

						tkns.Next<string>();
						tkns.Next<string>();
					}
					else
						in_p = true;
				}
			}

			// Pull off any extra padding at the end of the text
			bool done = false;

			while(true)
			{
				char c = fout[fout.Length - 1];

				switch(c)
				{
				case '\n':
				case ' ':
				case '\t':
				case '\r':
					fout = fout.Substring(0,fout.Length - 1);
					break;
				case ']':
					if(fout.EndsWith("[hr]"))
						fout = fout.Substring(0,fout.Length - 4);
					else
						done = true;

					break;
				default:
					done = true;
					break;
				}

				if(done)
					break;
			}

			File.Delete(file);
			File.WriteAllText(output,fout,System.Text.Encoding.Unicode);
			return;
		}

		public static string file = "t.html";
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
