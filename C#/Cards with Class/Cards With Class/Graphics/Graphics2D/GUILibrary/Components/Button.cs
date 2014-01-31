using System.Collections.Generic;
using Graphics.Graphics2D.StandardLibrary;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

namespace Graphics.Graphics2D.GUILibrary.Components
{
	/// <summary>
	/// A GUI button.
	/// </summary>
	public class Button : Draw2DGroup, GUIComponent
	{
		/// <summary>
		/// Creates a new button.
		/// </summary>
		/// <param name="size">The size of the button.</param>
		/// <param name="pos">The position of the button.</param>
		/// <param name="font">The font to use for text.</param>
		/// <param name="txt">The text to display on the button.</param>
		/// <param name="ntex">The texture to use for the button in it's normal state.</param>
		/// <param name="htex">The texture to use for the button in it's hover state.</param>
		/// <param name="atex">The texture to use for the button in it's active state.</param>
		/// <param name="depth">The depth to draw the button at.</param>
		public Button(Vector2 size, Vector2 pos, string font, string txt, string ntex, string htex, string atex, float depth = 0.0f) : base(depth)
		{
			normal = new StaticImage(ntex);
			hover = new StaticImage(htex);
			active = new StaticImage(atex);
			text = new Label(font,txt);
			
			Position = pos;
			Scale = size;
			Source = new Rectangle(0,0,(int)size.X,(int)size.Y);

			hover.Visible = false;
			active.Visible = false;

			base.Add(normal);
			base.Add(hover);
			base.Add(active);
			base.Add(text);

			return;
		}

		/// <summary>
		/// Loads the object's content.
		/// </summary>
		/// <param name="Graphics">The graphics device in use by the game.</param>
		/// <param name="Content">The content manager to use to load data with.</param>
		/// <remarks>This function should not be called by Initialize.</remarks>
		public override void LoadContent(GraphicsDevice Graphics, ContentManager Content)
		{
			base.LoadContent(Graphics,Content);

			// Content is loaded so we can now do size logic
			normal.Scale = new Vector2(Scale.X / normal.texture.Width,Scale.Y / normal.texture.Height);
			hover.Scale = new Vector2(Scale.X / hover.texture.Width,Scale.Y / hover.texture.Height);
			active.Scale = new Vector2(Scale.X / active.texture.Width,Scale.Y / active.texture.Height);

			Rectangle font_size = text.Destination;
			text.Position = new Vector2(Scale.X / 2.0f,Scale.Y / 2.0f) - new Vector2(font_size.Width / 2.0f,font_size.Height / 2.0f);

			Scale = Vector2.One;
			return;
		}

		/// <summary>
		/// Adds a new object to this group.
		/// </summary>
		/// <param name="obj">The object to add.</param>
		/// <returns>Returns true if the object was added and false if not.</returns>
		public override bool Add(DrawableGameComponent2D obj)
		{return false;}

		/// <summary>
		/// Adds the provided objects to this group.
		/// </summary>
		/// <param name="objs">The objects to add.</param>
		/// <returns>Returns true if the objects was added and false if not.</returns>
		public override bool Add(IEnumerable<DrawableGameComponent2D> objs)
		{return false;}

		/// <summary>
		/// Removes the provided object from this group.
		/// </summary>
		/// <param name="obj">The object to remove.</param>
		/// <returns>Returns true if the object was removed and false otherwise.</returns>
		public override bool Remove(DrawableGameComponent2D obj)
		{return false;}

		/// <summary>
		/// Removes the provided objects from this group.
		/// </summary>
		/// <param name="objs">The objects to remove.</param>
		/// <returns>The number of objects actually removed.</returns>
		public override int Remove(IEnumerable<DrawableGameComponent2D> objs)
		{return 0;}

		/// <summary>
		/// Draws the object.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		public override void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode)
		{
			switch(mode)
			{
			case SpriteSortMode.Deferred: // You would expect the first three states it doesn't matter but it turns out it does
			case SpriteSortMode.Immediate:
			case SpriteSortMode.Texture:
			case SpriteSortMode.BackToFront:
				normal.Depth = 1.0f;
				hover.Depth = 1.0f;
				active.Depth = 1.0f;
				text.Depth = 0.0f;

				break;
			case SpriteSortMode.FrontToBack:
				normal.Depth = 0.0f;
				hover.Depth = 0.0f;
				active.Depth = 0.0f;
				text.Depth = 1.0f;

				break;
			}

			base.Draw2D(gameTime,spriteBatch,mode);
			return;
		}

		/// <summary>
		/// Draws the object.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <param name="transPos">The distance to translate the object from its current position.</param>
		/// <param name="transRot">The amount in radians to rotate the object from its current rotation.</param>
		public override void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot)
		{
			switch(mode)
			{
			case SpriteSortMode.Deferred: // You would expect the first three states it doesn't matter but it turns out it does
			case SpriteSortMode.Immediate:
			case SpriteSortMode.Texture:
			case SpriteSortMode.BackToFront:
				normal.Depth = 1.0f;
				hover.Depth = 1.0f;
				active.Depth = 1.0f;
				text.Depth = 0.0f;

				break;
			case SpriteSortMode.FrontToBack:
				normal.Depth = 0.0f;
				hover.Depth = 0.0f;
				active.Depth = 0.0f;
				text.Depth = 1.0f;

				break;
			}

			base.Draw2D(gameTime,spriteBatch,mode,transPos,transRot);
			return;
		}

		/// <summary>
		/// Called if the mouse is enters the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		public void MouseEnter(MouseDiff m, GameTime gameTime)
		{
			normal.Visible = false;
			hover.Visible = true;
			active.Visible = false;

			if(observers != null)
				observers(MouseEvent.MOUSE_ENTER,m,gameTime);
			
			return;
		}

		/// <summary>
		/// Called if the mouse leaves the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		public void MouseLeave(MouseDiff m, GameTime gameTime)
		{
			normal.Visible = true;
			hover.Visible = false;
			active.Visible = false;
			
			if(observers != null)
				observers(MouseEvent.MOUSE_LEAVE,m,gameTime);

			return;
		}

		/// <summary>
		/// Called if the mouse hovers over the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		public void MouseHover(MouseDiff m, GameTime gameTime)
		{
			if(normal.Visible)
			{
				normal.Visible = false;
				hover.Visible = true;
			}

			if(observers != null)
				observers(MouseEvent.MOUSE_HOVER,m,gameTime);

			return;
		}

		/// <summary>
		/// Called if the left mouse button clicks inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		public void MouseLeftClick(MouseDiff m, GameTime gameTime)
		{
			normal.Visible = false;
			hover.Visible = false;
			active.Visible = true;

			if(observers != null)
				observers(MouseEvent.LEFT_CLICK,m,gameTime);

			return;
		}

		/// <summary>
		/// Called if the left mouse button is released inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in. There is also no garuntee that the click function was called.</remarks>
		public void MouseLeftRelease(MouseDiff m, GameTime gameTime)
		{
			MouseEvent e = active.Visible ? MouseEvent.LEFT_CLICK_AND_RELEASE : MouseEvent.NO_EVENT;

			normal.Visible = true;
			hover.Visible = false;
			active.Visible = false;
			
			if(observers != null)
				observers(e | MouseEvent.LEFT_RELEASE,m,gameTime);

			return;
		}

		/// <summary>
		/// Called if a non-left mouse button clicks inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in.</remarks>
		public void MouseOtherClick(MouseDiff m, GameTime gameTime)
		{
			MouseEvent whatup = MouseEvent.NO_EVENT;
			
			if(m.RightButtonChanged && m.Now.RightButton == ButtonState.Pressed)
				whatup |= MouseEvent.RIGHT_CLICK;

			if(m.RightButtonChanged && m.Now.MiddleButton == ButtonState.Pressed)
				whatup |= MouseEvent.MIDDLE_CLICK;

			if(m.X1ButtonChanged && m.Now.XButton1 == ButtonState.Pressed)
				whatup |= MouseEvent.X1_CLICK;

			if(m.X2ButtonChanged && m.Now.XButton2 == ButtonState.Pressed)
				whatup |= MouseEvent.X2_CLICK;

			// Buttons don't react to annoying mouse clicks

			if(observers != null)
				observers(whatup,m,gameTime);

			return;
		}

		/// <summary>
		/// Called if a non-left mouse button is released inside of the GUI component's destination box.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		/// <remarks>There is no garuntee which order the mouse functions are called in. There is also no garuntee that the click function was called.</remarks>
		public void MouseOtherRelease(MouseDiff m, GameTime gameTime)
		{
			MouseEvent whatup = MouseEvent.NO_EVENT;

			if(m.RightButtonChanged && m.Now.RightButton == ButtonState.Released)
				whatup |= MouseEvent.RIGHT_RELEASE;

			if(m.RightButtonChanged && m.Now.MiddleButton == ButtonState.Released)
				whatup |= MouseEvent.MIDDLE_RELEASE;

			if(m.X1ButtonChanged && m.Now.XButton1 == ButtonState.Released)
				whatup |= MouseEvent.X1_RELEASE;

			if(m.X2ButtonChanged && m.Now.XButton2 == ButtonState.Released)
				whatup |= MouseEvent.X2_RELEASE;

			// Buttons don't react to annoying mouse clicks
			
			if(observers != null)
				observers(whatup,m,gameTime);

			return;
		}

		/// <summary>
		/// Called if this component has exclusive focus in the GUI engine.
		/// No other mouse functions will be called.
		/// </summary>
		/// <param name="m">The state change of the mouse.</param>
		/// <param name="gameTime">The time the event occurs.</param>
		public void MouseExclusive(MouseDiff m, GameTime gameTime)
		{
			// First figure out what's going on
			MouseEvent whatup = MouseEvent.MOUSE_EXCLUSIVE | (active.Visible && (m.LeftButtonChanged && m.Now.LeftButton == ButtonState.Released) ? MouseEvent.LEFT_CLICK_AND_RELEASE : MouseEvent.NO_EVENT);

			if(m.LeftButtonChanged && m.Now.LeftButton == ButtonState.Pressed)
			{
				whatup |= MouseEvent.LEFT_CLICK;
				MouseLeftClick(m,gameTime);
			}

			bool btemp = false;

			if(m.RightButtonChanged && m.Now.RightButton == ButtonState.Pressed)
			{
				whatup |= MouseEvent.RIGHT_CLICK;
				btemp = true;
			}

			if(m.RightButtonChanged && m.Now.MiddleButton == ButtonState.Pressed)
			{
				whatup |= MouseEvent.MIDDLE_CLICK;
				btemp = true;
			}

			if(m.X1ButtonChanged && m.Now.XButton1 == ButtonState.Pressed)
			{
				whatup |= MouseEvent.X1_CLICK;
				btemp = true;
			}

			if(m.X2ButtonChanged && m.Now.XButton2 == ButtonState.Pressed)
			{
				whatup |= MouseEvent.X2_CLICK;
				btemp = true;
			}

			if(btemp)
			{
				MouseOtherClick(m,gameTime);
				btemp = false;
			}

			if(m.LeftButtonChanged && m.Now.LeftButton == ButtonState.Released)
			{
				whatup |= MouseEvent.LEFT_RELEASE;
				MouseLeftRelease(m,gameTime);
			}

			if(m.RightButtonChanged && m.Now.RightButton == ButtonState.Released)
			{
				whatup |= MouseEvent.RIGHT_RELEASE;
				btemp = true;
			}

			if(m.RightButtonChanged && m.Now.MiddleButton == ButtonState.Released)
			{
				whatup |= MouseEvent.MIDDLE_RELEASE;
				btemp = true;
			}

			if(m.X1ButtonChanged && m.Now.XButton1 == ButtonState.Released)
			{
				whatup |= MouseEvent.X1_RELEASE;
				btemp = true;
			}

			if(m.X2ButtonChanged && m.Now.XButton2 == ButtonState.Released)
			{
				whatup |= MouseEvent.X2_RELEASE;
				btemp = true;
			}

			if(btemp)
				MouseOtherRelease(m,gameTime);

			bool is_in = Destination.Contains(m.Now.X,m.Now.Y);
			bool was_in = Destination.Contains(m.Old.X,m.Old.Y);

			if(is_in && was_in)
			{
				whatup |= MouseEvent.MOUSE_HOVER;
				MouseHover(m,gameTime);
			}
			else if(is_in && !was_in)
			{
				whatup |= MouseEvent.MOUSE_ENTER;
				MouseEnter(m,gameTime);
			}
			else if(was_in)
			{
				whatup |= MouseEvent.MOUSE_LEAVE;
				MouseLeave(m,gameTime);
			}

			if(observers != null)
				observers(whatup,m,gameTime);

			return;
		}

		/// <summary>
		/// Adds a mouse listener to this GUI component.
		/// </summary>
		/// <param name="listen">The listener to remove.</param>
		public void AddMouseListener(MouseListener listen)
		{
			observers += listen;
			return;
		}

		/// <summary>
		/// Removes a mouse listener from this GUI component.
		/// </summary>
		/// <param name="listen">The listener to remove.</param>
		public void RemoveMouseListener(MouseListener listen)
		{
			observers -= listen;
			return;
		}

		/// <summary>
		/// The button text.
		/// </summary>
		public string Text
		{
			get
			{return text.Text;}
			set
			{
				text.Text = value;
				return;
			}
		}

		/// <summary>
		/// The color of the text of this button.
		/// </summary>
		public Color TextColor
		{
			get
			{return text.Tint;}
			set
			{
				text.Tint = value;
				return;
			}
		}

		/// <summary>
		/// Everything listening to this button.
		/// </summary>
		protected MouseListener observers;

		/// <summary>
		/// What the button looks like when it's not being hovered over.
		/// </summary>
		protected StaticImage normal;

		/// <summary>
		/// What the button looks like when it's being hovered over.
		/// </summary>
		protected StaticImage hover;

		/// <summary>
		/// What the button looks like when it's being clicked.
		/// </summary>
		protected StaticImage active;

		/// <summary>
		/// The label on the button.
		/// </summary>
		protected Label text;
	}
}
