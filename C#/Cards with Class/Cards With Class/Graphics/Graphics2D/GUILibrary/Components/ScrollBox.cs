using System;
using System.Collections.Generic;
using Graphics.Graphics2D.StandardLibrary;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;

namespace Graphics.Graphics2D.GUILibrary.Components
{
	/// <summary>
	/// A drawing group with scrollbars if needed to see normally offscreen elements.
	/// It is possible to force the scrollbars to be drawn.
	/// </summary>
	public class ScrollBox : Draw2DGroup, GUIComponent
	{
		/// <summary>
		/// Creates a new scrollbox.
		/// </summary>
		/// <param name="size">The size of the scrollbox.</param>
		/// <param name="pos">The position of the scrollbox.</param>
		/// <param name="bntex">The texture to use for the bar in it's normal state.</param>
		/// <param name="bhtex">The texture to use for the bar in it's hover state.</param>
		/// <param name="batex">The texture to use for the bar in it's active state.</param>
		/// <param name="antex">The texture to use for arrows in their normal state. The up arrow is the unrotated state.</param>
		/// <param name="ahtext">The texture to use for arrows in their hover state. The up arrow is the unrotated state.</param>
		/// <param name="aatext">The teture to use for arrows in their active state. The up arrow is the unrotated state.</param>
		/// <param name="hbar_height">The height of the horizontal bar.</param>
		/// <param name="vbar_width">The width of the vertical bar.</param>
		/// <param name="depth">The depth to draw the scrollbox at.</param>
		public ScrollBox(Vector2 size, Vector2 pos, string bntex, string bhtex, string batex, string antex, string ahtext, string aatext, float hbar_height = 15.0f, float vbar_width = 15.0f, float depth = 0.0f) : base(depth)
		{
			hbar = new Button(new Vector2(hbar_height,size.X - 3.0f * hbar_height),new Vector2(hbar_height,size.Y),"Fonts\\TNR12","",bntex,bhtex,batex);
			vbar = new Button(new Vector2(vbar_width,size.Y - 3.0f * vbar_width),new Vector2(size.X - vbar_width,vbar_width),"Fonts\\TNR12","",bntex,bhtex,batex);
			
			hbar.Rotation = -MathHelper.PiOver2;

			left = new Button(Vector2.One * hbar_height,new Vector2(0.0f,size.Y),"Fonts\\TNR12","",antex,ahtext,aatext);
			right = new Button(Vector2.One * hbar_height,new Vector2(size.X - 2.0f * hbar_height,size.Y),"Fonts\\TNR12","",antex,ahtext,aatext);
			up = new Button(Vector2.One * vbar_width,new Vector2(size.X - vbar_width,0.0f),"Fonts\\TNR12","",antex,ahtext,aatext);
			down = new Button(Vector2.One * vbar_width,new Vector2(size.X - vbar_width,size.Y - 2.0f * hbar_height),"Fonts\\TNR12","",antex,ahtext,aatext);

			left.Rotation = -MathHelper.PiOver2;
			right.Rotation = -MathHelper.PiOver2;
			right.Effects = SpriteEffects.FlipHorizontally;
			down.Effects = SpriteEffects.FlipVertically;

			lower = new Line(new Vector2(0.0f,size.Y - hbar_height),new Vector2(size.X,size.Y - hbar_height),hbar_height);
			lower.Tint = BackgroundColor;
			side = new Line(new Vector2(size.X,0.0f),new Vector2(size.X,size.Y),vbar_width);
			side.Tint = BackgroundColor;

			inner_box = new Draw2DGroup();

			Position = pos;
			Scale = Vector2.One;
			Size = size;

			hp = 0;
			vp = 0;

			HorizontalBarHeight = hbar_height;
			VerticalBarWidth = vbar_width;

			fhhb = false;
			fhvb = false;
			fshb = false;
			fsvb = false;

			mouse_objects = new List<GUIComponent>();

			base.Add(hbar);
			base.Add(vbar);
			
			hbar.AddMouseListener(HorizontalBar);
			vbar.AddMouseListener(VerticalBar);

			hbar.Visible = false;
			vbar.Visible = false;

			base.Add(left);
			base.Add(right);
			base.Add(up);
			base.Add(down);

			left.AddMouseListener(LeftArrow);
			right.AddMouseListener(RightArrow);
			up.AddMouseListener(UpArrow);
			down.AddMouseListener(DownArrow);

			left.Visible = false;
			right.Visible = false;
			up.Visible = false;
			down.Visible = false;

			base.Add(lower);
			base.Add(side);

			base.Add(inner_box);
			return;
		}

		/// <summary>
		/// Adds a new object to this group.
		/// </summary>
		/// <param name="obj">The object to add.</param>
		/// <returns>Returns true if the object was added and false if not.</returns>
		public override bool Add(DrawableGameComponent2D obj)
		{
			if(obj is GUIComponent)
				mouse_objects.Add(obj as GUIComponent);

			bool ret = inner_box.Add(obj);
			UpdateScrollBars();

			return ret;
		}

		/// <summary>
		/// Adds the provided objects to this group.
		/// </summary>
		/// <param name="objs">The objects to add.</param>
		/// <returns>Returns true if the objects was added and false if not.</returns>
		public override bool Add(IEnumerable<DrawableGameComponent2D> objs)
		{
			foreach(DrawableGameComponent2D obj in objs)
				if(obj is GUIComponent)
					mouse_objects.Add(obj as GUIComponent);

			bool ret = inner_box.Add(objs);
			UpdateScrollBars();

			return ret;
		}

		/// <summary>
		/// Removes the provided object from this group.
		/// </summary>
		/// <param name="obj">The object to remove.</param>
		/// <returns>Returns true if the object was removed and false otherwise.</returns>
		public override bool Remove(DrawableGameComponent2D obj)
		{
			if(obj is GUIComponent)
				mouse_objects.Remove(obj as GUIComponent);

			bool ret = inner_box.Remove(obj);
			UpdateScrollBars();

			return ret;
		}

		/// <summary>
		/// Removes the provided objects from this group.
		/// </summary>
		/// <param name="objs">The objects to remove.</param>
		/// <returns>The number of objects actually removed.</returns>
		public override int Remove(IEnumerable<DrawableGameComponent2D> objs)
		{
			foreach(DrawableGameComponent2D obj in objs)
				if(obj is GUIComponent)
					mouse_objects.Remove(obj as GUIComponent);

			int ret = inner_box.Remove(objs);
			UpdateScrollBars();

			return ret;
		}

		/// <summary>
		/// Updates information in the scroll bars after components have been added or removed.
		/// </summary>
		protected void UpdateScrollBars()
		{
			Rectangle bounds = inner_box.Bounds;
			Vector2 scale = InnerSize / new Vector2(bounds.X + bounds.Width,bounds.Y + bounds.Height);
			
			if(scale.X > 1.0f)
				scale.X = 1.0f;

			if(scale.Y > 1.0f)
				scale.Y = 1.0f;

			hbar.Scale = new Vector2(hbar.Scale.X,scale.X / last_scale.X * hbar.Scale.Y);
			vbar.Scale = new Vector2(vbar.Scale.X,scale.Y / last_scale.Y * vbar.Scale.Y);

			if(epsilon(1.0f,scale.X))
			{
				if(!ForceShowHorizontalBar)
					ShowHorizontalBar(false);
			}
			else if(!ForceHideHorizontalBar)
				ShowHorizontalBar();

			if(epsilon(1.0f,scale.Y))
			{
				if(!ForceShowVerticalBar)
					ShowVerticalBar(false);
			}
			else if(!ForceHideVerticalBar)
				ShowVerticalBar();

			// We've already changed source value if necessary to we just need to position the scroll bars
			hbar.Position = new Vector2(HorizontalBarHeight + HorizontalPosition,InnerSize.Y + HorizontalBarHeight);
			vbar.Position = new Vector2(InnerSize.X,VerticalBarWidth + VerticalPosition);

			last_scale = scale;
			return;
		}

		/// <summary>
		/// Checks if |f - val| less than epsilon.
		/// </summary>
		/// <param name="f">The value we want.</param>
		/// <param name="val">The value we have.</param>
		/// <param name="epsilon">The epsilon value.</param>
		/// <returns>Returns true if val is within epsilon of f.</returns>
		protected bool epsilon(float f, float val, float epsilon = 0.001f)
		{return Math.Abs(f - val) < epsilon;}

		/// <summary>
		/// The first pass rendering of the object. This function must be called outside of a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <remarks>This function is not responsible for resetting the render state changes made by spritebatch calls.</remarks>
		public override void FirstPass(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode)
		{
			switch(mode)
			{
			case SpriteSortMode.Deferred: // You would expect the first three states it doesn't matter but it turns out it does
			case SpriteSortMode.Immediate:
			case SpriteSortMode.Texture:
			case SpriteSortMode.BackToFront:
				hbar.Depth = 0.5f;
				vbar.Depth = 0.5f;
				
				left.Depth = 0.0f;
				right.Depth = 0.0f;
				up.Depth = 0.0f;
				down.Depth = 0.0f;
				
				lower.Depth = 0.75f;
				side.Depth = 0.75f;

				inner_box.Depth = 1.0f;

				break;
			case SpriteSortMode.FrontToBack:
				hbar.Depth = 0.5f;
				vbar.Depth = 0.5f;
				
				left.Depth = 1.0f;
				right.Depth = 1.0f;
				up.Depth = 1.0f;
				down.Depth = 1.0f;

				lower.Depth = 0.25f;
				side.Depth = 0.25f;

				inner_box.Depth = 0.0f;

				break;
			}

			base.FirstPass(gameTime,spriteBatch,mode);
			return;
		}

		/// <summary>
		/// The first pass rendering of the object. This function must be called outside of a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <param name="transPos">The distance to translate the object from its current position. This may have a different meaning than for Draw2D.</param>
		/// <param name="transRot">The amount in radians to rotate the object from its current rotation. This may have a different meaning than for Draw2D.</param>
		/// <remarks>This function is not responsible for resetting the render state changes made by spritebatch calls.</remarks>
		public override void FirstPass(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot)
		{
			switch(mode)
			{
			case SpriteSortMode.Deferred: // You would expect the first three states it doesn't matter but it turns out it does
			case SpriteSortMode.Immediate:
			case SpriteSortMode.Texture:
			case SpriteSortMode.BackToFront:
				hbar.Depth = 0.5f;
				vbar.Depth = 0.5f;
				
				left.Depth = 0.0f;
				right.Depth = 0.0f;
				up.Depth = 0.0f;
				down.Depth = 0.0f;
				
				lower.Depth = 0.75f;
				side.Depth = 0.75f;

				inner_box.Depth = 1.0f;

				break;
			case SpriteSortMode.FrontToBack:
				hbar.Depth = 0.5f;
				vbar.Depth = 0.5f;
				
				left.Depth = 1.0f;
				right.Depth = 1.0f;
				up.Depth = 1.0f;
				down.Depth = 1.0f;

				lower.Depth = 0.25f;
				side.Depth = 0.25f;

				inner_box.Depth = 0.0f;

				break;
			}

			base.FirstPass(gameTime,spriteBatch,mode,transPos,transRot);
			return;
		}

		/// <summary>
		/// Called when the horizontal bar has an event.
		/// </summary>
		/// <param name="e">The event.</param>
		/// <param name="m">The state change of the mouse for the event.</param>
		/// <param name="gameTime">The time when the event occured.</param>
		protected void HorizontalBar(MouseEvent e, MouseDiff m, GameTime gameTime)
		{
			if((e & MouseEvent.MOUSE_ENTER) == MouseEvent.MOUSE_ENTER)
				in_arrow = true;
			else if((e & MouseEvent.MOUSE_LEAVE) == MouseEvent.MOUSE_LEAVE)
			{
				in_arrow = false;
				left_hold = false;
			}

			if((e & MouseEvent.LEFT_CLICK) == MouseEvent.LEFT_CLICK)
				left_hold = true;
			else if((e & MouseEvent.LEFT_RELEASE) == MouseEvent.LEFT_RELEASE)
				left_hold = false;

			if(in_arrow && left_hold)
				HorizontalPosition += m.DeltaX;

			return;
		}

		/// <summary>
		/// Called when the vertical bar has an event.
		/// </summary>
		/// <param name="e">The event.</param>
		/// <param name="m">The state change of the mouse for the event.</param>
		/// <param name="gameTime">The time when the event occured.</param>
		protected void VerticalBar(MouseEvent e, MouseDiff m, GameTime gameTime)
		{
			if((e & MouseEvent.MOUSE_ENTER) == MouseEvent.MOUSE_ENTER)
				in_arrow = true;
			else if((e & MouseEvent.MOUSE_LEAVE) == MouseEvent.MOUSE_LEAVE)
			{
				in_arrow = false;
				left_hold = false;
			}

			if((e & MouseEvent.LEFT_CLICK) == MouseEvent.LEFT_CLICK)
				left_hold = true;
			else if((e & MouseEvent.LEFT_RELEASE) == MouseEvent.LEFT_RELEASE)
				left_hold = false;

			if(in_arrow && left_hold)
				VerticalPosition += m.DeltaY;

			return;
		}

		/// <summary>
		/// Called when the up arrow event occurs.
		/// </summary>
		/// <param name="e">The event.</param>
		/// <param name="m">The state change of the mouse for the event.</param>
		/// <param name="gameTime">The time when the event occured.</param>
		protected void UpArrow(MouseEvent e, MouseDiff m, GameTime gameTime)
		{
			if((e & MouseEvent.MOUSE_ENTER) == MouseEvent.MOUSE_ENTER)
				in_arrow = true;
			else if((e & MouseEvent.MOUSE_LEAVE) == MouseEvent.MOUSE_LEAVE)
			{
				in_arrow = false;
				left_hold = false;
			}

			if((e & MouseEvent.LEFT_CLICK) == MouseEvent.LEFT_CLICK && !left_hold)
			{
				left_hold = true;
				VerticalPosition--;

				time_stamp = gameTime.TotalGameTime.TotalSeconds;
			}
			else if((e & MouseEvent.LEFT_RELEASE) == MouseEvent.LEFT_RELEASE)
				left_hold = false;

			if(in_arrow && left_hold && gameTime.TotalGameTime.TotalSeconds - time_stamp > lag)
				VerticalPosition--;
			
			return;
		}

		/// <summary>
		/// Called when the down arrow event occurs.
		/// </summary>
		/// <param name="e">The event.</param>
		/// <param name="m">The state change of the mouse for the event.</param>
		/// <param name="gameTime">The time when the event occured.</param>
		protected void DownArrow(MouseEvent e, MouseDiff m, GameTime gameTime)
		{
			if((e & MouseEvent.MOUSE_ENTER) == MouseEvent.MOUSE_ENTER)
				in_arrow = true;
			else if((e & MouseEvent.MOUSE_LEAVE) == MouseEvent.MOUSE_LEAVE)
			{
				in_arrow = false;
				left_hold = false;
			}

			if((e & MouseEvent.LEFT_CLICK) == MouseEvent.LEFT_CLICK && !left_hold)
			{
				left_hold = true;
				VerticalPosition++;

				time_stamp = gameTime.TotalGameTime.TotalSeconds;
			}
			else if((e & MouseEvent.LEFT_RELEASE) == MouseEvent.LEFT_RELEASE)
				left_hold = false;

			if(in_arrow && left_hold && gameTime.TotalGameTime.TotalSeconds - time_stamp > lag)
				VerticalPosition++;

			return;
		}

		/// <summary>
		/// Called when the left arrow event occurs.
		/// </summary>
		/// <param name="e">The event.</param>
		/// <param name="m">The state change of the mouse for the event.</param>
		/// <param name="gameTime">The time when the event occured.</param>
		protected void LeftArrow(MouseEvent e, MouseDiff m, GameTime gameTime)
		{
			if((e & MouseEvent.MOUSE_ENTER) == MouseEvent.MOUSE_ENTER)
				in_arrow = true;
			else if((e & MouseEvent.MOUSE_LEAVE) == MouseEvent.MOUSE_LEAVE)
			{
				in_arrow = false;
				left_hold = false;
			}

			if((e & MouseEvent.LEFT_CLICK) == MouseEvent.LEFT_CLICK && !left_hold)
			{
				left_hold = true;
				HorizontalPosition--;

				time_stamp = gameTime.TotalGameTime.TotalSeconds;
			}
			else if((e & MouseEvent.LEFT_RELEASE) == MouseEvent.LEFT_RELEASE)
				left_hold = false;

			if(in_arrow && left_hold && gameTime.TotalGameTime.TotalSeconds - time_stamp > lag)
				HorizontalPosition--;

			return;
		}

		/// <summary>
		/// Called when the right arrow event occurs.
		/// </summary>
		/// <param name="e">The event.</param>
		/// <param name="m">The state change of the mouse for the event.</param>
		/// <param name="gameTime">The time when the event occured.</param>
		protected void RightArrow(MouseEvent e, MouseDiff m, GameTime gameTime)
		{
			if((e & MouseEvent.MOUSE_ENTER) == MouseEvent.MOUSE_ENTER)
				in_arrow = true;
			else if((e & MouseEvent.MOUSE_LEAVE) == MouseEvent.MOUSE_LEAVE)
			{
				in_arrow = false;
				left_hold = false;
			}

			if((e & MouseEvent.LEFT_CLICK) == MouseEvent.LEFT_CLICK && !left_hold)
			{
				left_hold = true;
				HorizontalPosition++;

				time_stamp = gameTime.TotalGameTime.TotalSeconds;
			}
			else if((e & MouseEvent.LEFT_RELEASE) == MouseEvent.LEFT_RELEASE)
				left_hold = false;

			if(in_arrow && left_hold && gameTime.TotalGameTime.TotalSeconds - time_stamp > lag)
				HorizontalPosition++;

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
			MouseExclusive(m,gameTime);
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
			MouseExclusive(m,gameTime);
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
			MouseExclusive(m,gameTime);
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
			MouseExclusive(m,gameTime);
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
			MouseExclusive(m,gameTime);
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
			MouseExclusive(m,gameTime);
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
			MouseExclusive(m,gameTime);
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
			// Recreate mouse differences since the mouse position in the scroll box is different from where it is in the window
			MouseState old = new MouseState(m.Old.X - (int)last_position.X + (int)last_box_position.X,m.Old.Y - (int)last_position.Y + (int)last_box_position.Y,m.Old.ScrollWheelValue,m.Old.LeftButton,m.Old.MiddleButton,m.Old.RightButton,m.Old.XButton1,m.Old.XButton2);
			MouseState now = new MouseState(m.Now.X - (int)Position.X + (int)BoxPosition.X,m.Now.Y - (int)Position.Y + (int)BoxPosition.Y,m.Now.ScrollWheelValue,m.Now.LeftButton,m.Now.MiddleButton,m.Now.RightButton,m.Now.XButton1,m.Now.XButton2);
			MouseDiff changes = new MouseDiff(now,old);

			List<GUIComponent> was_in = GetComponentsAt(changes.Old.X,changes.Old.Y);
			List<GUIComponent> is_in = GetComponentsAt(changes.Now.X,changes.Now.Y);
			
			// Manually add the six buttons to the two sets
			if(hbar.Visible)
			{
				// Handle the scroll bar now since we are in the scroll box
				if(m.DeltaS != 0)
					VerticalPosition -= m.DeltaS / 60; // Apparently the unit of scroll is 60

				// Man, rotations suck (as a side note it is generally inappropriate to rotate GUI components)
				Rectangle hbdest = new Rectangle((int)HorizontalBarHeight + HorizontalPosition,(int)(Size.Y - HorizontalBarHeight),(int)((Size.X - 3.0f * HorizontalBarHeight) * last_scale.X),(int)HorizontalBarHeight);
				Rectangle ldest = new Rectangle(0,(int)(Size.Y - HorizontalBarHeight),(int)HorizontalBarHeight,(int)HorizontalBarHeight);
				Rectangle rdest = new Rectangle((int)(Size.X - 2.0f * HorizontalBarHeight),(int)(Size.Y - HorizontalBarHeight),(int)HorizontalBarHeight,(int)HorizontalBarHeight);

				if(hbdest.Contains(m.Old.X - (int)last_position.X,m.Old.Y - (int)last_position.Y))
					was_in.Add(hbar);

				if(ldest.Contains(m.Old.X - (int)last_position.X,m.Old.Y - (int)last_position.Y))
					was_in.Add(this.left); // Something weird is up with left and we have to use this.left

				if(rdest.Contains(m.Old.X - (int)last_position.X,m.Old.Y - (int)last_position.Y))
					was_in.Add(right);

				if(hbdest.Contains(m.Now.X - (int)Position.X,m.Now.Y - (int)Position.Y))
					is_in.Add(hbar);

				if(ldest.Contains(m.Now.X - (int)Position.X,m.Now.Y - (int)Position.Y))
					is_in.Add(this.left); // Something weird is up with left and we have to use this.left

				if(rdest.Contains(m.Now.X - (int)Position.X,m.Now.Y - (int)Position.Y))
					is_in.Add(right);
			}

			if(vbar.Visible)
			{
				/*Rectangle vbdest = vbar.Destination;
				vbdest.Height = (int)(vbdest.Height * last_scale.Y);*/
                Rectangle vbdest = new Rectangle((int)(Size.X - VerticalBarWidth),(int)VerticalBarWidth + VerticalPosition,(int)VerticalBarWidth,(int)((Size.Y - 3.0f * VerticalBarWidth) * last_scale.Y));

				if(vbdest.Contains(m.Old.X - (int)last_position.X,m.Old.Y - (int)last_position.Y))
					was_in.Add(vbar);

				if(up.Destination.Contains(m.Old.X - (int)last_position.X,m.Old.Y - (int)last_position.Y))
					was_in.Add(up);

				if(down.Destination.Contains(m.Old.X - (int)last_position.X,m.Old.Y - (int)last_position.Y))
					was_in.Add(down);

				if(vbdest.Contains(m.Now.X - (int)Position.X,m.Now.Y - (int)Position.Y))
					is_in.Add(vbar);

				if(up.Destination.Contains(m.Now.X - (int)Position.X,m.Now.Y - (int)Position.Y))
					is_in.Add(up);

				if(down.Destination.Contains(m.Now.X - (int)Position.X,m.Now.Y - (int)Position.Y))
					is_in.Add(down);
			}

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

			return;
		}
		
		/// <summary>
		/// Updates the scroll box.
		/// </summary>
		/// <param name="gameTime">The time the update is occuring at.</param>
		public override void Update(GameTime gameTime)
		{
			last_position = Position;
			last_box_position = BoxPosition;
			
			lower.Tint = BackgroundColor;
			side.Tint = BackgroundColor;

			UpdateScrollBars();

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

			foreach(GUIComponent c in mouse_objects)
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
		/// This operation is unsupported in scrollboxes.
		/// </summary>
		/// <param name="listen">The listener to remove.</param>
		public void AddMouseListener(MouseListener listen)
		{throw new NotSupportedException();}

		/// <summary>
		/// This operation is unsupported in scrollboxes.
		/// </summary>
		/// <param name="listen">The listener to remove.</param>
		public void RemoveMouseListener(MouseListener listen)
		{throw new NotSupportedException();}

		/// <summary>
		/// Hides/Shows the horizontal scroll bar.
		/// </summary>
		/// <param name="show">If true then the horizontal scroll bar will be shown otherwise it will be hidden.</param>
		protected void ShowHorizontalBar(bool show = true)
		{
			hbar.Visible = show;
			left.Visible = show;
			right.Visible = show;
			lower.Visible = show;

			return;
		}

		/// <summary>
		/// Hides/Shows the vertical scroll bar.
		/// </summary>
		/// <param name="show">If true then the vertical scroll bar will be shown otherwise it will be hidden.</param>
		protected void ShowVerticalBar(bool show = true)
		{
			vbar.Visible = show;
			up.Visible = show;
			down.Visible = show;
			side.Visible = show;

			return;
		}

		/// <summary>
		/// The source in the texture (in texels) to use for drawing.
		/// Can not be null for scrollboxes.
		/// </summary>
		public override Nullable<Rectangle> Source
		{
			get
			{return src;}
			set
			{
				if(value != null)
				{
					Rectangle bounds = inner_box.Bounds;
					src = new Rectangle(0,0,value.Value.Width,value.Value.Height);
					
					inner_box.Source = new Rectangle(value.Value.X,value.Value.Y,value.Value.Width,value.Value.Height);
					inner_box.BufferSize = new Rectangle(0,0,value.Value.X + value.Value.Width,value.Value.Y + value.Value.Height);
					UpdateScrollBars();
				}

				return;
			}
		}

		/// <summary>
		/// Contains the value for Source.
		/// </summary>
		protected Nullable<Rectangle> src = new Rectangle(0,0,0,0);

		/// <summary>
		/// Forces the horizontal bar to be drawn.
		/// </summary>
		public bool ForceShowHorizontalBar
		{
			get
			{return fshb;}
			set
			{
				if(value)
				{
					ForceHideHorizontalBar = false;
					ShowHorizontalBar();
				}
				else if(MaxHorizontalPosition == 0)
					ShowHorizontalBar(false);

				fshb = value;
				return;
			}
		}

		/// <summary>
		/// Contains the value for ForceShowHorizontalBar.
		/// </summary>
		protected bool fshb;

		/// <summary>
		/// Forces the vertical bar to be drawn.
		/// </summary>
		public bool ForceShowVerticalBar
		{
			get
			{return fsvb;}
			set
			{
				if(value)
				{
					ForceHideVerticalBar = false;
					ShowVerticalBar();
				}
				else if(MaxVerticalPosition == 0)
					ShowVerticalBar(false);
				
				fsvb = value;
				return;
			}
		}

		/// <summary>
		/// Contains the value for ForceShowVerticalBar.
		/// </summary>
		protected bool fsvb;

		/// <summary>
		/// Forces the horizontal bar to never be drawn.
		/// </summary>
		public bool ForceHideHorizontalBar
		{
			get
			{return fhhb;}
			set
			{
				if(value)
				{
					ForceShowHorizontalBar = false;
					ShowHorizontalBar(false);
				}
				else if(MaxHorizontalPosition != 0)
					ShowHorizontalBar();

				fhhb = value;
				return;
			}
		}

		/// <summary>
		/// Contains the value for ForceHideVerticalBar.
		/// </summary>
		protected bool fhhb;

		/// <summary>
		/// Forces the vertical bar to never be drawn.
		/// </summary>
		public bool ForceHideVerticalBar
		{
			get
			{return fhvb;}
			set
			{
				if(value)
				{
					ForceShowVerticalBar = false;
					ShowVerticalBar(false);
				}
				else if(MaxVerticalPosition != 0)
					ShowVerticalBar();

				fhvb = value;
				return;
			}
		}

		/// <summary>
		/// Contains the value for ForceHideVerticalBar.
		/// </summary>
		protected bool fhvb;

		/// <summary>
		/// The height of the horizontal bar.
		/// </summary>
		public float HorizontalBarHeight
		{get; protected set;}

		/// <summary>
		/// The width of the vertical bar.
		/// </summary>
		public float VerticalBarWidth
		{get; protected set;}

		/// <summary>
		/// The position of the horizontal scroll bar.
		/// </summary>
		public int HorizontalPosition
		{
			get
			{return hp;}
			set
			{
				if(value > MaxHorizontalPosition)
					value = MaxHorizontalPosition;

				if(value < 0)
					value = 0;

				Rectangle bounds = inner_box.Bounds;
				BoxPosition = new Vector2(value * (bounds.X + bounds.Width - InnerSize.X) / MaxHorizontalPosition,BoxPosition.Y);

				return;
			}
		}

		/// <summary>
		/// The position of the horizontal scroll bar.
		/// </summary>
		protected int hp;

		/// <summary>
		/// The position of the vertical scroll bar.
		/// </summary>
		public int VerticalPosition
		{
			get
			{return vp;}
			set
			{
				if(value > MaxVerticalPosition)
					value = MaxVerticalPosition;

				if(value < 0)
					value = 0;

				Rectangle bounds = inner_box.Bounds;
				BoxPosition = new Vector2(BoxPosition.X,value * (bounds.Y + bounds.Height - InnerSize.Y) / MaxVerticalPosition);

				return;
			}
		}

		/// <summary>
		/// The position of the vertical scroll bar.
		/// </summary>
		protected int vp;

		/// <summary>
		/// The maximum position of the horizontal scroll bar.
		/// </summary>
		public int MaxHorizontalPosition
		{
			get
			{return (int)((1.0f - last_scale.X) * (Size.X - HorizontalBarHeight * 3.0f));}
		}
		
		/// <summary>
		/// The maximum position of the vertical scroll bar.
		/// </summary>
		public int MaxVerticalPosition
		{
			get
			{return (int)((1.0f - last_scale.Y) * (Size.Y - VerticalBarWidth * 3.0f));}
		}

		/// <summary>
		/// The color of the inner drawing group.
		/// </summary>
		public Color InnerBackgroundColor
		{
			get
			{return inner_box.BackgroundColor;}
			set
			{
				inner_box.BackgroundColor = value;
				return;
			}
		}

		/// <summary>
		/// The position of the inner box of the scrollbox.
		/// </summary>
		public Vector2 BoxPosition
		{
			get
			{return new Vector2(inner_box.Source.Value.X,inner_box.Source.Value.Y);}
			set
			{
				Rectangle bounds = inner_box.Bounds;

				hp = (int)Math.Round(value.X / ((bounds.X + bounds.Width - InnerSize.X) / MaxHorizontalPosition));

				if(hp > MaxHorizontalPosition)
				{
					hp = MaxHorizontalPosition;
					value.X = bounds.X + bounds.Width - InnerSize.X;
				}

				vp = (int)Math.Round(value.Y / ((bounds.Y + bounds.Height - InnerSize.Y) / MaxVerticalPosition));

				if(vp > MaxVerticalPosition)
				{
					vp = MaxVerticalPosition;
					value.Y = bounds.Y + bounds.Height - InnerSize.Y;
				}

				Source = new Rectangle((int)value.X,(int)value.Y,Source.Value.Width,Source.Value.Height);
				return;
			}
		}

		/// <summary>
		/// The size of the scrollbox.
		/// </summary>
		public Vector2 Size
		{
			get
			{return new Vector2(Source.Value.Width,Source.Value.Height);}
			set
			{
				Source = new Rectangle(inner_box.Source.HasValue ? inner_box.Source.Value.X : 0,inner_box.Source.HasValue ? inner_box.Source.Value.Y : 0,(int)value.X,(int)value.Y);
				return;
			}
		}

		/// <summary>
		/// The size of the scroll box excluding scrollbars if they are visible and not if they are not.
		/// </summary>
		public Vector2 InnerSize
		{
			get
			{return new Vector2(Size.X - (vbar.Visible ? VerticalBarWidth : 0),Size.Y - (hbar.Visible ? HorizontalBarHeight : 0));}
		}

		/// <summary>
		/// The button that is the horizontal bar.
		/// </summary>
		protected Button hbar;

		/// <summary>
		/// The button that is the vertical bar.
		/// </summary>
		protected Button vbar;

		/// <summary>
		/// The left scroll arrow.
		/// </summary>
		protected Button left;

		/// <summary>
		/// The right scroll arrow.
		/// </summary>
		protected Button right;

		/// <summary>
		/// The up scroll arrow.
		/// </summary>
		protected Button up;

		/// <summary>
		/// The down scroll arrow.
		/// </summary>
		protected Button down;

		/// <summary>
		/// The lower line.
		/// </summary>
		protected Line lower;

		/// <summary>
		/// The right line.
		/// </summary>
		protected Line side;

		/// <summary>
		/// The additional content drawn inside this scrollbox.
		/// </summary>
		protected Draw2DGroup inner_box;

		/// <summary>
		/// Objects in this scrollbox that need mouse handling.
		/// </summary>
		protected List<GUIComponent> mouse_objects;

		/// <summary>
		/// The last scales on scrollbar update.
		/// </summary>
		protected Vector2 last_scale = Vector2.One;

		/// <summary>
		/// The last known position of the scroll box.
		/// </summary>
		protected Vector2 last_position = Vector2.Zero;

		/// <summary>
		/// The last known position of the the scroll box's internal position.
		/// </summary>
		protected Vector2 last_box_position = Vector2.Zero;

		// A bunch of state variables.
		protected bool in_arrow = false;
		protected bool left_hold = false;
		protected double time_stamp = 0.0;
		protected double lag = 0.4;
	}
}
