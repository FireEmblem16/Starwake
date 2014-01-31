using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;

namespace Graphics.Graphics2D.GUILibrary
{
	/// <summary>
	/// The requirements to be a GUI component.
	/// </summary>
	public interface GUIComponent : DrawableGameComponent2D
	{
		/// <summary>
		/// Called if the mouse is enters the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		void MouseEnter(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if the mouse leaves the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		void MouseLeave(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if the mouse hovers over the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		void MouseHover(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if the left mouse button clicks inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		void MouseLeftClick(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if the left mouse button is released inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in. There is also no garuntee that the click function was called.</remarks>
		void MouseLeftRelease(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if a non-left mouse button clicks inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		void MouseOtherClick(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if a non-left mouse button is released inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in. There is also no garuntee that the click function was called.</remarks>
		void MouseOtherRelease(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Called if this component has exclusive focus in the GUI engine.
		/// No other mouse functions will be called.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		void MouseExclusive(MouseDiff m, GameTime gameTime);

		/// <summary>
		/// Adds a mouse listener to this GUI component.
		/// </summary>
		/// <param name="listen">The listener to remove.</param>
		void AddMouseListener(MouseListener listen);

		/// <summary>
		/// Removes a mouse listener from this GUI component.
		/// </summary>
		/// <param name="listen">The listener to remove.</param>
		void RemoveMouseListener(MouseListener listen);
	}

	/// <summary>
	/// A mouse event listener.
	/// </summary>
	/// <param name="e">The event.</param>
	/// <param name="m">The state change of the mouse for the event.</param>
	/// <param name="gameTime">The time when the event occured.</param>
	/// <remarks>It is important to note that more than one event can occur on the same call.</remarks>
	public delegate void MouseListener(MouseEvent e, MouseDiff m, GameTime gameTime);

	/// <summary>
	/// The types of mouse events that can occur.
	/// </summary>
	[System.Flags]
	public enum MouseEvent
	{
		NO_EVENT = 0x0000,
		MOUSE_EXCLUSIVE = 0x0001,
		LEFT_CLICK = 0x0002,
		LEFT_RELEASE = 0x0004,
		LEFT_CLICK_AND_RELEASE = 0x0008,
		RIGHT_CLICK = 0x0010,
		RIGHT_RELEASE = 0x0020,
		MIDDLE_CLICK = 0x0040,
		MIDDLE_RELEASE = 0x0080,
		X1_CLICK = 0x0100,
		X1_RELEASE = 0x0200,
		X2_CLICK = 0x0400,
		X2_RELEASE = 0x0800,
		MOUSE_ENTER = 0x1000,
		MOUSE_LEAVE = 0x2000,
		MOUSE_HOVER = 0x4000
	}
}
