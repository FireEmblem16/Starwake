using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.StandardLibrary
{
	/// <summary>
	/// A group of 2D objects that are all drawn together.
	/// Other 2D objects can only interact with the resulting texture, not individual objects in the group.
	/// </summary>
	/// <remarks>Position, scale, rotation, etc data in this class does not change child 2D object data. It only affects the way the final texture results are drawn.</remarks>
	public class Draw2DGroup : TwoPassDrawableGameObject2D
	{
		/// <summary>
		/// Creates a group of 2D objects that are all drawn together.
		/// </summary>
		/// <param name="depth">The layer to draw this group in.</param>
		public Draw2DGroup(float depth = 0.0f)
		{
			objects = new List<DrawableGameComponent2D>();
			twopass_objects = new List<TwoPassDrawableGameObject2D>();

			Sorting = SpriteSortMode.BackToFront;
			Blending = BlendState.AlphaBlend;

			target = null;
			Position = Vector2.Zero;
			Origin = Vector2.Zero;
			Rotation = 0.0f;
			Scale = new Vector2(1.0f);
			Source = null;
			Tint = Color.White;
			Effects = SpriteEffects.None;
			Depth = depth;
			Visible = true;
			BackgroundColor = Color.Transparent;
			BufferSize = null;

			Initialized = false;
			Loaded = false;
			Unloaded = false;

			Graphics = null;
			Content = null;

			UpdateBounds();
			return;
		}

		/// <summary>
		/// Creates a group of 2D objects that are all drawn together.
		/// </summary>
		/// <param name="pos">The position of the group.</param>
		/// <param name="rotation">The rotation of the group.</param>
		/// <param name="scale">The scale of the group.</param>
		/// <param name="depth">The layer to draw this group in.</param>
		public Draw2DGroup(Vector2 pos, float rotation, Vector2 scale, float depth = 0.0f)
		{
			objects = new List<DrawableGameComponent2D>();
			twopass_objects = new List<TwoPassDrawableGameObject2D>();

			Sorting = SpriteSortMode.BackToFront;
			Blending = BlendState.AlphaBlend;

			target = null;
			Position = pos;
			Origin = Vector2.Zero;
			Rotation = rotation;
			Scale = scale;
			Source = null;
			Tint = Color.White;
			Effects = SpriteEffects.None;
			Depth = depth;
			Visible = true;
			BackgroundColor = Color.Transparent;
			BufferSize = null;

			Initialized = false;
			Loaded = false;
			Unloaded = false;

			Graphics = null;
			Content = null;

			UpdateBounds();
			return;
		}

		/// <summary>
		/// Creates a group of 2D objects that are all drawn together.
		/// </summary>
		/// <param name="pos">The position of the group.</param>
		/// <param name="rotation">The rotation of the group.</param>
		/// <param name="scale">The scale of the group.</param>
		/// <param name="origin">The origin of the image. Also the center of rotation.</param>
		/// <param name="depth">The layer to draw this group in.</param>
		public Draw2DGroup(Vector2 pos, float rotation, Vector2 scale, Vector2 origin, float depth = 0.0f)
		{
			objects = new List<DrawableGameComponent2D>();
			twopass_objects = new List<TwoPassDrawableGameObject2D>();

			Sorting = SpriteSortMode.BackToFront;
			Blending = BlendState.AlphaBlend;

			target = null;
			Position = pos;
			Origin = origin;
			Rotation = rotation;
			Scale = scale;
			Source = null;
			Tint = Color.White;
			Effects = SpriteEffects.None;
			Depth = depth;
			Visible = true;
			BackgroundColor = Color.Transparent;
			BufferSize = null;

			Initialized = false;
			Loaded = false;
			Unloaded = false;

			Graphics = null;
			Content = null;

			UpdateBounds();
			return;
		}

		/// <summary>
		/// Initializes the object.
		/// </summary>
		public virtual void Initialize()
		{
			Unloaded = false;

			foreach(DrawableGameComponent2D obj in objects)
				obj.Initialize();

			foreach(DrawableGameComponent2D obj in twopass_objects)
				obj.Initialize();

			Initialized = true;
			return;
		}
        
		/// <summary>
		/// Loads the object's content.
		/// </summary>
		/// <param name="Graphics">The graphics device in use by the game.</param>
		/// <param name="Content">The content manager to use to load data with.</param>
		/// <remarks>This function should not be called by Initialize.</remarks>
		public virtual void LoadContent(GraphicsDevice Graphics, ContentManager Content)
		{
			Unloaded = false;

			foreach(DrawableGameComponent2D obj in objects)
				obj.LoadContent(Graphics,Content);

			foreach(DrawableGameComponent2D obj in twopass_objects)
				obj.LoadContent(Graphics,Content);

			Loaded = true;
			this.Graphics = Graphics;
			this.Content = Content;

			return;
		}
		
		/// <summary>
		/// Unloads any content this object has.
		/// </summary>
		/// <remarks>Only data not managed by a ContentManager should be destroyed immediately.</remarks>
		public virtual void UnloadContent()
		{
			foreach(DrawableGameComponent2D obj in objects)
				obj.UnloadContent();

			foreach(DrawableGameComponent2D obj in twopass_objects)
				obj.UnloadContent();

			if(target != null)
			{
				target.Dispose();
				target = null;
			}

			Unloaded = true;
			Loaded = false;
			Initialized = false;

			return;
		}

		/// <summary>
		/// Adds a new object to this group.
		/// </summary>
		/// <param name="obj">The object to add.</param>
		/// <returns>Returns true if the object was added and false if not.</returns>
		public virtual bool Add(DrawableGameComponent2D obj)
		{
			if(Unloaded)
				return false;

			if(obj is TwoPassDrawableGameObject2D)
				twopass_objects.Add((TwoPassDrawableGameObject2D)obj);
			else
				objects.Add(obj);

			if(Initialized)
				obj.Initialize();
			
			if(Loaded)
				obj.LoadContent(Graphics,Content);

			UpdateBounds();
			return true;
		}

		/// <summary>
		/// Adds the provided objects to this group.
		/// </summary>
		/// <param name="objs">The objects to add.</param>
		/// <returns>Returns true if the objects was added and false if not.</returns>
		public virtual bool Add(IEnumerable<DrawableGameComponent2D> objs)
		{
			if(Unloaded)
				return false;

			foreach(DrawableGameComponent2D obj in objs)
				if(obj is TwoPassDrawableGameObject2D)
					twopass_objects.Add((TwoPassDrawableGameObject2D)obj);
				else
					objects.Add(obj);
			
			if(Initialized)
				foreach(DrawableGameComponent2D obj in objs)
					obj.Initialize();

			if(Loaded)
				foreach(DrawableGameComponent2D obj in objs)
					obj.LoadContent(Graphics,Content);

			UpdateBounds();
			return true;
		}

		/// <summary>
		/// Removes the provided object from this group.
		/// </summary>
		/// <param name="obj">The object to remove.</param>
		/// <returns>Returns true if the object was removed and false otherwise.</returns>
		public virtual bool Remove(DrawableGameComponent2D obj)
		{
			bool b = obj is TwoPassDrawableGameObject2D;
			int index = b ? twopass_objects.IndexOf((TwoPassDrawableGameObject2D)obj) : objects.IndexOf(obj);

			if(index < 0)
				return false;
			
			if(Loaded && !Unloaded)
				if(b)
					twopass_objects[index].UnloadContent();
				else
					objects[index].UnloadContent();
			
			objects.RemoveAt(index);
			UpdateBounds();

			return true;
		}

		/// <summary>
		/// Removes the provided objects from this group.
		/// </summary>
		/// <param name="objs">The objects to remove.</param>
		/// <returns>The number of objects actually removed.</returns>
		public virtual int Remove(IEnumerable<DrawableGameComponent2D> objs)
		{
			int ret = 0;

			foreach(DrawableGameComponent2D obj in objs)
				if(Remove(obj))
					ret++;

			return ret;
		}

		/// <summary>
		/// The first pass rendering of the object. This function must be called outside of a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <remarks>This function is not responsible for resetting the render state changes made by spritebatch calls.</remarks>
		public virtual void FirstPass(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode)
		{
			if(!Visible)
				return;

			foreach(TwoPassDrawableGameObject2D obj in twopass_objects)
                obj.FirstPass(gameTime,spriteBatch,mode);

			
			if(BufferSize.HasValue)
			{
				if(target == null || target.GraphicsDevice != spriteBatch.GraphicsDevice || target.Width != BufferSize.Value.Width || target.Height != BufferSize.Value.Height)
				{
					if(target != null)
						target.Dispose();

					target = new RenderTarget2D(spriteBatch.GraphicsDevice,BufferSize.Value.Width,BufferSize.Value.Height,false,SurfaceFormat.Color,DepthFormat.None,0,RenderTargetUsage.PreserveContents);
				}
			}
			else if(target == null || target.GraphicsDevice != spriteBatch.GraphicsDevice || target.Width != spriteBatch.GraphicsDevice.PresentationParameters.BackBufferWidth || target.Height != spriteBatch.GraphicsDevice.PresentationParameters.BackBufferHeight)
			{
				if(target != null)
					target.Dispose();

				target = new RenderTarget2D(spriteBatch.GraphicsDevice,spriteBatch.GraphicsDevice.PresentationParameters.BackBufferWidth,spriteBatch.GraphicsDevice.PresentationParameters.BackBufferHeight,false,SurfaceFormat.Color,DepthFormat.None,0,RenderTargetUsage.PreserveContents);
			}

			RenderTargetBinding[] tgs = spriteBatch.GraphicsDevice.GetRenderTargets();
			spriteBatch.GraphicsDevice.SetRenderTarget(target);
			spriteBatch.GraphicsDevice.Clear(BackgroundColor);
			
			spriteBatch.Begin(Sorting,Blending);
			
            foreach(DrawableGameComponent2D obj in objects)
                obj.Draw2D(gameTime,spriteBatch,mode);

			foreach(DrawableGameComponent2D obj in twopass_objects)
                obj.Draw2D(gameTime,spriteBatch,mode);

            spriteBatch.End();
			spriteBatch.GraphicsDevice.SetRenderTargets(tgs);

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
		public virtual void FirstPass(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot)
		{
			if(!Visible)
				return;

			foreach(TwoPassDrawableGameObject2D obj in twopass_objects)
                obj.FirstPass(gameTime,spriteBatch,mode,transPos,transRot);

			if(BufferSize.HasValue)
			{
				if(target == null || target.GraphicsDevice != spriteBatch.GraphicsDevice || target.Width != BufferSize.Value.Width || target.Height != BufferSize.Value.Height)
				{
					if(target != null)
						target.Dispose();

					target = new RenderTarget2D(spriteBatch.GraphicsDevice,BufferSize.Value.Width,BufferSize.Value.Height,false,SurfaceFormat.Color,DepthFormat.None,0,RenderTargetUsage.PreserveContents);
				}
			}
			else if(target == null || target.GraphicsDevice != spriteBatch.GraphicsDevice || target.Width != spriteBatch.GraphicsDevice.PresentationParameters.BackBufferWidth || target.Height != spriteBatch.GraphicsDevice.PresentationParameters.BackBufferHeight)
			{
				if(target != null)
					target.Dispose();

				target = new RenderTarget2D(spriteBatch.GraphicsDevice,spriteBatch.GraphicsDevice.PresentationParameters.BackBufferWidth,spriteBatch.GraphicsDevice.PresentationParameters.BackBufferHeight,false,SurfaceFormat.Color,DepthFormat.None,0,RenderTargetUsage.PreserveContents);
			}
			
			RenderTargetBinding[] tgs = spriteBatch.GraphicsDevice.GetRenderTargets();
			spriteBatch.GraphicsDevice.SetRenderTarget(target);
			spriteBatch.GraphicsDevice.Clear(BackgroundColor);

			spriteBatch.Begin(Sorting,Blending);
			
            foreach(DrawableGameComponent2D obj in objects)
                obj.Draw2D(gameTime,spriteBatch,mode);

			foreach(DrawableGameComponent2D obj in twopass_objects)
                obj.Draw2D(gameTime,spriteBatch,mode);

            spriteBatch.End();
			spriteBatch.GraphicsDevice.SetRenderTargets(tgs);

			return;
		}

		/// <summary>
		/// Draws the object.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		public virtual void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode)
		{
			if(!Visible || texture == null)
				return;

			spriteBatch.Draw(texture,Position,Source,Tint,Rotation,Origin,Scale,Effects,Depth);
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
		public virtual void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot)
		{
			if(!Visible || texture == null)
				return;

			spriteBatch.Draw(texture,Position + transPos,Source,Tint,Rotation + transRot,Origin,Scale,Effects,Depth);
			return;
		}

		/// <summary>
		/// Updates the object.
		/// </summary>
		/// <param name="gameTime">The time the object is updated at.</param>
		public virtual void Update(GameTime gameTime)
		{
			foreach(DrawableGameComponent2D obj in objects)
				obj.Update(gameTime);

			foreach(DrawableGameComponent2D obj in twopass_objects)
				obj.Update(gameTime);

			UpdateBounds();
			return;
		}

		/// <summary>
		/// Updates the boundaries of this group.
		/// </summary>
		protected void UpdateBounds()
		{
			if(objects.Count == 0 && twopass_objects.Count == 0)
			{
				Bounds = new Rectangle(0,0,0,0);
				return;
			}

			Rectangle ret = new Rectangle(int.MaxValue,int.MaxValue,int.MinValue,int.MinValue);

			foreach(DrawableGameComponent2D obj in objects)
			{
				Rectangle dest = obj.Destination;

				if(dest.X < ret.X)
					ret.X = dest.X;
					
				if(dest.X + dest.Width > ret.Width)
					ret.Width = dest.X + dest.Width;

				if(dest.Y < ret.Y)
					ret.Y = dest.Y;

				if(dest.Y + dest.Height > ret.Height)
					ret.Height = dest.Y + dest.Height;
			}

			foreach(TwoPassDrawableGameObject2D obj in twopass_objects)
			{
				Rectangle dest = obj.Destination;

				if(dest.X < ret.X)
					ret.X = dest.X;
					
				if(dest.X + dest.Width > ret.Width)
					ret.Width = dest.X + dest.Width;

				if(dest.Y < ret.Y)
					ret.Y = dest.Y;

				if(dest.Y + dest.Height > ret.Height)
					ret.Height = dest.Y + dest.Height;
			}

			ret.Width = ret.Width - ret.X;
			ret.Height = ret.Height - ret.Y;

			Bounds = ret;
			return;
		}

		/// <summary>
		/// The texture used for this object.
		/// </summary>
		public Texture2D texture
		{
			get
			{return target;}
		}

		/// <summary>
		/// The render texture to use for this group.
		/// </summary>
		protected RenderTarget2D target;

		/// <summary>
		/// The position of the object.
		/// </summary>
		public Vector2 Position
		{get; set;}

		/// <summary>
		/// The origin of the object.
		/// This is the center of rotation for the object.
		/// </summary>
		public Vector2 Origin
		{get; set;}

		/// <summary>
		/// The rotation of the object.
		/// </summary>
		public float Rotation
		{get; set;}

		/// <summary>
		/// The scaling factors of the object.
		/// </summary>
		public Vector2 Scale
		{get; set;}

		/// <summary>
		/// The source in the texture (in texels) to use for drawing.
		/// Use null to draw the entire texture.
		/// </summary>
		public virtual Nullable<Rectangle> Source
		{get; set;}

		/// <summary>
		/// The destination rectangle for this object.
		/// </summary>
		/// <remarks>To change this value, change the position and scale of this object.</remarks>
		public Rectangle Destination
		{
			get
			{
				if(Source.HasValue)
					return new Rectangle((int)Position.X,(int)Position.Y,(int)(Source.Value.Width * Scale.X),(int)(Source.Value.Height * Scale.Y));
				
				if(texture == null) // This can happen if Destination gets called before the first draw call
					return new Rectangle((int)Position.X,(int)Position.Y,0,0);

				return new Rectangle((int)Position.X,(int)Position.Y,(int)(texture.Width * Scale.X),(int)(texture.Height * Scale.Y));
			}
		}

		/// <summary>
		/// The color to tint this object.
		/// Use white for full color with no tinting.
		/// </summary>
		public Color Tint
		{get; set;}

		/// <summary>
		/// The effects to apply to this object.
		/// </summary>
		public SpriteEffects Effects
		{get; set;}

		/// <summary>
		/// The depth to draw at on the screen.
		/// By default, when drawing graphics 0 represents the front layer and 1 represents the back layer.
		/// </summary>
		public float Depth
		{get; set;}

		/// <summary>
		/// If true then the object is drawn and if false then the object is not drawn.
		/// </summary>
		public bool Visible
		{get; set;}

		/// <summary>
		/// The background color of this group.
		/// The default value is black with an alpha value of zero.
		/// </summary>
		public Color BackgroundColor
		{get; set;}

		/// <summary>
		/// The sorting, if any, applied to objects drawn with this group.
		/// By default, graphics will be drawn from back to front.
		/// </summary>
		public SpriteSortMode Sorting
		{get; set;}

		/// <summary>
		/// The way color blending is handled.
		/// By default, graphics will be drawn with alpha blending.
		/// </summary>
		public BlendState Blending
		{get; set;}

		/// <summary>
		/// If true then the engine has already been initialized.
		/// </summary>
		public bool Initialized
		{get; protected set;}

		/// <summary>
		/// If true then the engine has already had its content loaded.
		/// </summary>
		public bool Loaded
		{get; protected set;}

		/// <summary>
		/// If true then the engine has already has its content unloaded and can not accept any more data.
		/// </summary>
		public bool Unloaded
		{get; protected set;}

		/// <summary>
		/// The size of the texture buffer we want to draw to.
		/// If null then we will use the size of the graphic device's presentation parameters.
		/// </summary>
		public Nullable<Rectangle> BufferSize
		{get; set;}

		/// <summary>
		/// The bounding rectangle of elements inside this drawing group.
		/// </summary>
		public Rectangle Bounds
		{get; protected set;}

		/// <summary>
		/// The last used graphics device. For loading purposed.
		/// </summary>
		protected GraphicsDevice Graphics;

		/// <summary>
		/// The last used content manager. For loading purposed.
		/// </summary>
		protected ContentManager Content;

		/// <summary>
		/// The single pass objects in this group.
		/// </summary>
		protected List<DrawableGameComponent2D> objects;

		/// <summary>
		/// The two pass objects in this group.
		/// </summary>
		protected List<TwoPassDrawableGameObject2D> twopass_objects;
	}
}
