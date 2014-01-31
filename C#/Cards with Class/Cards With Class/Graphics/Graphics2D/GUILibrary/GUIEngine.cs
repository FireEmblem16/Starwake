using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input;

namespace Graphics.Graphics2D.GUILibrary
{
	/// <summary>
	/// The engine to handle GUI events such as mouse clicking.
	/// </summary>
	public class GUIEngine : GameComponent
	{
		/// <summary>
		/// The GUI events manager for the game.
		/// </summary>
		/// <remarks>The engine must first be initialized by the game utilizing it.</remarks>
		public static GUIEngine Engine
		{get; protected set;}

		/// <summary>
		/// Initializes the GUI engine.
		/// </summary>
		/// <param name="game">The game that will be utilizing the engine.</param>
		/// <remarks>The GUI engine will automatically be added to game components.</remarks>
		public static void Initialize(Game game)
		{
			if(Engine == null)
				Engine = new GUIEngine(game);
			
			return;
		}

		/// <summary>
		/// Creates a new engine for GUI events.
		/// </summary>
		protected GUIEngine(Game game) : base(game)
		{
			components = new List<GUIComponent>();
			Game.Components.Add(this);

			return;
		}

		/// <summary>
		/// Adds the given element to the list of components managed by the GUI engine.
		/// </summary>
		/// <param name="e">The component to add.</param>
		/// <remarks>To draw GUI components you will need to place them in a Draw2DEngine.</remarks>
		public void Add(GUIComponent e)
		{
			components.Add(e);
			return;
		}

		/// <summary>
		/// Adds the given elements to the list of components managed by the GUI engine.
		/// </summary>
		/// <param name="e">The components to add.</param>
		/// <remarks>To draw GUI components you will need to place them in a Draw2DEngine.</remarks>
		public void Add(IEnumerable<GUIComponent> e)
		{
			components.AddRange(e);
			return;
		}

		/// <summary>
		/// Removes the given component from the list managed by the GUI engine.
		/// </summary>
		/// <param name="e">The component to remove.</param>
		/// <returns>Returns true if the component was removed and false otherwise.</returns>
		public bool Remove(GUIComponent e)
		{return components.Remove(e);}

		/// <summary>
		/// Removes all the given components from the list managed by the GUI engine.
		/// </summary>
		/// <param name="e">The components to remove.</param>
		/// <returns>Returns the number of components removed.</returns>
		public int Remove(IEnumerable<GUIComponent> e)
		{
			int ret = 0;

			foreach(GUIComponent ge in e)
				if(Remove(ge))
					ret++;

			return ret;
		}

		/// <summary>
		/// Gives exclusive focus for events to the given component if no other component has already requested exclusive focus.
		/// </summary>
		/// <param name="c">The component to give focus to.</param>
		/// <returns>Returns true if the request was accepted and false otherwise.</returns>
		/// <remarks>If an update is already in progress the rest of the update must finish before changes will occur.</remarks>
		public bool RequestExclusiveFocus(GUIComponent c)
		{
			if(exclusive != null)
				return false;
			
			exclusive = c;
			return true;
		}

		/// <summary>
		/// Releases exclusive focus taken by the given component.
		/// </summary>
		/// <param name="c">The component that has focus. This acts as a key to ensure nothing else removes its focus.</param>
		/// <returns>Returns true if focus was release and false otherwise.</returns>
		/// <remarks>If an update is already in progress the rest of the update must finish before changes will occur.</remarks>
		public bool ReleaseFocus(GUIComponent c)
		{
			if(exclusive != c)
				return false;

			exclusive = null;
			return true;
		}

		/// <summary>
		/// Determines what events have occured.
		/// </summary>
		/// <param name="gameTime">The time the engine is updated at.</param>
		public override void Update(GameTime gameTime)
		{
			MouseDiff changes = new MouseDiff(Mouse.GetState(),last_state);

			if(exclusive != null)
			{
				exclusive.MouseExclusive(changes,gameTime);

				last_state = changes.Now;
				base.Update(gameTime);
				return;
			}

			List<GUIComponent> was_in = GetComponentsAt(changes.Old.X,changes.Old.Y);
			List<GUIComponent> is_in = GetComponentsAt(changes.Now.X,changes.Now.Y);
			
			List<GUIComponent> hovered = GetIntersection(is_in,was_in);
			List<GUIComponent> entered = GetEntered(is_in,was_in);
			List<GUIComponent> left = GetLeft(is_in,was_in);

			if(changes.ButtonsChanged)
			{
				if(changes.ClickLeftOccured)
					foreach(GUIComponent c in is_in)
						c.MouseLeftClick(changes,gameTime);
				else if(changes.ReleaseLeftOccured)
					foreach(GUIComponent c in is_in)
						c.MouseLeftRelease(changes,gameTime);

				if(changes.RightButtonChanged || changes.MiddleButtonChanged || changes.X1ButtonChanged || changes.X2ButtonChanged)
				{
					bool click = changes.ClickOtherOccured;
					bool release = changes.ReleaseOtherOccured;

					foreach(GUIComponent c in is_in)
					{
						if(click)
							c.MouseOtherClick(changes,gameTime);

						if(release)
							c.MouseOtherRelease(changes,gameTime);
					}
				}
			}
			
			foreach(GUIComponent c in hovered)
				c.MouseHover(changes,gameTime);

			foreach(GUIComponent c in entered)
				c.MouseEnter(changes,gameTime);

			foreach(GUIComponent c in left)
				c.MouseLeave(changes,gameTime);

			last_state = changes.Now;
			base.Update(gameTime);
			return;
		}

		/// <summary>
		/// Gets all GUI components in this GUI engine with source at the given point (x,y).
		/// </summary>
		/// <param name="x">The x coordinate of the point to check.</param>
		/// <param name="y">The y coordinate of the point to check.</param>
		/// <returns>Returns a list of components with destination values that contain p.</returns>
		/// <remarks>Never returns something that is not visible.</remarks>
		protected List<GUIComponent> GetComponentsAt(int x, int y)
		{
			List<GUIComponent> ret = new List<GUIComponent>();

			foreach(GUIComponent c in components)
				if(c.Visible && c.Destination.Contains(x,y))
					ret.Add(c);

			return ret;
		}

		/// <summary>
		/// Gets the list of components that are in both then and now.
		/// </summary>
		/// <param name="l1">A list of components.</param>
		/// <param name="l2">A list of components.</param>
		/// <returns>Returns all components in both then and now.</returns>
		protected List<GUIComponent> GetIntersection(List<GUIComponent> l1, List<GUIComponent> l2)
		{
			List<GUIComponent> ret = new List<GUIComponent>();

			// We could optimize this to a linear check but the code gets messy and there should never be very many GUI components so O(n^2) is also fine
			foreach(GUIComponent c in l1)
				if(l2.Contains(c))
					ret.Add(c);

			return ret;
		}

		/// <summary>
		/// Gets the list of components that are in then but not in now.
		/// </summary>
		/// <param name="now">A list of components.</param>
		/// <param name="then">A list of components.</param>
		/// <returns>Returns all components in then but not in now.</returns>
		protected List<GUIComponent> GetLeft(List<GUIComponent> now, List<GUIComponent> then)
		{
			List<GUIComponent> ret = new List<GUIComponent>();

			// We could optimize this to a linear check but the code gets messy and there should never be very many GUI components so O(n^2) is also fine
			foreach(GUIComponent c in then)
				if(!now.Contains(c))
					ret.Add(c);

			return ret;
		}

		/// <summary>
		/// Gets the list of components that are in now but not in then.
		/// </summary>
		/// <param name="now">A list of components.</param>
		/// <param name="then">A list of components.</param>
		/// <returns>Returns all components in now but not in then.</returns>
		protected List<GUIComponent> GetEntered(List<GUIComponent> now, List<GUIComponent> then)
		{
			List<GUIComponent> ret = new List<GUIComponent>();

			// We could optimize this to a linear check but the code gets messy and there should never be very many GUI components so O(n^2) is also fine
			foreach(GUIComponent c in now)
				if(!then.Contains(c))
					ret.Add(c);

			return ret;
		}

		/// <summary>
		/// The components managed by this GUI engine.
		/// </summary>
		protected List<GUIComponent> components;

		/// <summary>
		/// If a component has exclusive focus it will be put here.
		/// </summary>
		protected GUIComponent exclusive;

		/// <summary>
		/// The last state of the mouse.
		/// </summary>
		protected MouseState last_state;
	}

	/// <summary>
	/// The difference between two mouse states.
	/// </summary>
	public class MouseDiff
	{
		/// <summary>
		/// Creates a new set of differences between two mouse states.
		/// </summary>
		/// <param name="current">The current mouse state.</param>
		/// <param name="last">The old mouse state.</param>
		public MouseDiff(MouseState current, MouseState last)
		{
			Now = current;
			Old = last;

			DeltaX = Now.X - Old.X;
			DeltaY = Now.Y - Old.Y;
			DeltaS = Now.ScrollWheelValue - Old.ScrollWheelValue;
			
			LeftButtonChanged = Now.LeftButton != Old.LeftButton;
			RightButtonChanged = Now.RightButton != Old.RightButton;
			MiddleButtonChanged = Now.MiddleButton != Old.MiddleButton;
			X1ButtonChanged = Now.XButton1 != Old.XButton1;
			X2ButtonChanged = Now.XButton2 != Old.XButton2;

			return;
		}

		/// <summary>
		/// The change in the X position of the mouse.
		/// </summary>
		public int DeltaX
		{get; protected set;}

		/// <summary>
		/// The change in the Y position of the mouse.
		/// </summary>
		public int DeltaY
		{get; protected set;}

		/// <summary>
		/// The change in the position of the scroll wheel.
		/// </summary>
		public int DeltaS
		{get; protected set;}

		/// <summary>
		/// If true then the mouse has moved.
		/// </summary>
		public bool MouseMoved
		{
			get
			{return DeltaX != 0 && DeltaY != 0 && DeltaS != 0;}
		}

		/// <summary>
		/// True if the state of the left mouse button has changed.
		/// </summary>
		public bool LeftButtonChanged
		{get; protected set;}

		/// <summary>
		/// True if the state of the right mouse button has changed.
		/// </summary>
		public bool RightButtonChanged
		{get; protected set;}

		/// <summary>
		/// True if the state of the middle mouse button has changed.
		/// </summary>
		public bool MiddleButtonChanged
		{get; protected set;}

		/// <summary>
		/// True if the state of the first extra mouse button has changed.
		/// </summary>
		public bool X1ButtonChanged
		{get; protected set;}

		/// <summary>
		/// True if the state of the second extra mouse button has changed.
		/// </summary>
		public bool X2ButtonChanged
		{get; protected set;}

		/// <summary>
		/// True if a mouse button has changed state.
		/// </summary>
		public bool ButtonsChanged
		{
			get
			{return LeftButtonChanged || RightButtonChanged || MiddleButtonChanged || X1ButtonChanged || X2ButtonChanged;}
		}

		/// <summary>
		/// True if the left button clicked.
		/// </summary>
		public bool ClickLeftOccured
		{
			get
			{return LeftButtonChanged && Now.LeftButton == ButtonState.Pressed;}
		}

		/// <summary>
		/// True if a non-left button was clicked.
		/// </summary>
		public bool ClickOtherOccured
		{
			get
			{return ButtonsChanged && (RightButtonChanged && Now.RightButton == ButtonState.Pressed || MiddleButtonChanged && Now.MiddleButton == ButtonState.Pressed || X1ButtonChanged && Now.XButton1 == ButtonState.Pressed || X2ButtonChanged && Now.XButton2 == ButtonState.Pressed);}
		}

		/// <summary>
		/// True if the left button was released.
		/// </summary>
		public bool ReleaseLeftOccured
		{
			get
			{return LeftButtonChanged && Now.LeftButton == ButtonState.Released;}
		}

		/// <summary>
		/// True if a non-left button was released.
		/// </summary>
		public bool ReleaseOtherOccured
		{
			get
			{return ButtonsChanged && (RightButtonChanged && Now.RightButton == ButtonState.Released || MiddleButtonChanged && Now.MiddleButton == ButtonState.Released || X1ButtonChanged && Now.XButton1 == ButtonState.Released || X2ButtonChanged && Now.XButton2 == ButtonState.Released);}
		}

		/// <summary>
		/// The new mouse state.
		/// </summary>
		public MouseState Now
		{get; protected set;}

		/// <summary>
		/// The old mouse state.
		/// </summary>
		public MouseState Old
		{get; protected set;}
	}
}
