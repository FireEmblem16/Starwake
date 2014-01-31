using System.Drawing;
using System.Windows.Forms;
using System;

namespace Engine.Game.Screens
{
	/// <summary>
	/// The abstract of a rules screen.
	/// </summary>
	public abstract partial class RuleScreen : Form
	{
#region Constructors
		/// <summary>
		/// Creates a new rules screen.
		/// </summary>
		/// <param name="title">The title of this window.</param>
		protected RuleScreen(string title)
		{
			InitializeComponent();
			Text = title;

			SizeChanged += new EventHandler(ResizeRules);
			return;
		}
#endregion

#region Helper Functions
		/// <summary>
		/// Appends the given string with the current font and color.
		/// </summary>
		/// <param name="str">The string to append.</param>
		/// <param name="style">The style of the current font to use.</param>
		protected void AppendString(string str)
		{
			richTextBox1.AppendText(str);
			return;
		}

		/// <summary>
		/// Appends the given string using the existing font with the given style and the current color.
		/// </summary>
		/// <param name="str">The string to append.</param>
		/// <param name="style">The style of the current font to use.</param>
		protected void AppendString(string str, FontStyle style)
		{
			richTextBox1.SelectionFont = new Font(richTextBox1.Font,style);
			AppendString(str);

			return;
		}

		/// <summary>
		/// Appends the given string using the given font and the current color.
		/// </summary>
		/// <param name="str">The string to append.</param>
		/// <param name="style">The font to use.</param>
		protected void AppendString(string str, Font font)
		{
			richTextBox1.SelectionFont = font;
			AppendString(str);

			return;
		}
		
		/// <summary>
		/// Appends the given string using the existing font with the given color and style.
		/// </summary>
		/// <param name="str">The string to append.</param>
		/// <param name="style">The style of the current font to use.</param>
		/// <param name="c">The color to use.</param>
		protected void AppendString(string str, FontStyle style, Color c)
		{
			richTextBox1.SelectionColor = c;
			AppendString(str,style);

			return;
		}

		/// <summary>
		/// Appends the given string using the given font and color.
		/// </summary>
		/// <param name="str">The string to append.</param>
		/// <param name="style">The font to use.</param>
		/// <param name="c">The color to use.</param>
		protected void AppendString(string str, Font font, Color c)
		{
			richTextBox1.SelectionColor = c;
			AppendString(str,font);

			return;
		}
#endregion

#region Windows Form Functions
		/// <summary>
		/// Called when the window loads.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void RuleScreen_Load(object sender, EventArgs e)
		{
			richTextBox1.ReadOnly = true;
			richTextBox1.BackColor = Color.White;
			richTextBox1.Text = "";

			Initialize();
			return;
		}
#endregion

#region Event Handlers
		/// <summary>
		/// Resizes the rules text.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void ResizeRules(object sender, EventArgs e)
		{
			richTextBox1.Location = new Point(0,0);
			richTextBox1.Size = new Size(Size.Width - 15,Size.Height - 33);

			return;
		}
#endregion

#region Abstract Functions
		/// <summary>
		/// Called when all other initialization logic is finished to provided extra initialization.
		/// </summary>
		protected abstract void Initialize();
#endregion

#region Delegates

#endregion

#region Properties
		/// <summary>
		/// The text of the rules.
		/// </summary>
		public string RulesText
		{
			get
			{return richTextBox1.Text;}
		}
#endregion

#region Member Variables

#endregion
	}
}
