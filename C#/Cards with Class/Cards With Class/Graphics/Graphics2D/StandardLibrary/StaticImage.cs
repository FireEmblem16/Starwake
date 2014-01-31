using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.StandardLibrary
{
	/// <summary>
	/// A 2D image that has no features beyond being drawn such as animation.
	/// </summary>
	public class StaticImage : DrawableGameComponent2D
	{
		/// <summary>
		/// Creates a new static image with default values.
		/// </summary>
		/// <param name="tex">The path to the desired texture.</param>
		/// <param name="depth">The layer to draw this image in.</param>
		public StaticImage(string tex, float depth = 0.0f)
		{
			TexSource = tex;

			texture = null;
			Position = Vector2.Zero;
			Origin = Vector2.Zero;
			Rotation = 0.0f;
			Scale = new Vector2(1.0f);
			Source = null;
			Tint = Color.White;
			Effects = SpriteEffects.None;
			Depth = depth;
			Visible = true;

			return;
		}

		/// <summary>
		/// Creates a new static image with the provided position, rotation and scale.
		/// </summary>
		/// <param name="tex">The path to the desired texture.</param>
		/// <param name="pos">The position of the image.</param>
		/// <param name="rotation">The rotation of the image.</param>
		/// <param name="scale">The scale of the image.</param>
		/// <param name="depth">The layer to draw this image in.</param>
		public StaticImage(string tex, Vector2 pos, float rotation, Vector2 scale, float depth = 0.0f)
		{
			TexSource = tex;

			texture = null;
			Position = pos;
			Origin = Vector2.Zero;
			Rotation = rotation;
			Scale = scale;
			Source = null;
			Tint = Color.White;
			Effects = SpriteEffects.None;
			Depth = depth;
			Visible = true;

			return;
		}

		/// <summary>
		/// Creates a new static image with the provided position, rotation, scale and origin.
		/// </summary>
		/// <param name="tex">The path to the desired texture.</param>
		/// <param name="pos">The position of the image.</param>
		/// <param name="rotation">The rotation of the image.</param>
		/// <param name="scale">The scale of the image.</param>
		/// <param name="origin">The origin of the image. Also the center of rotation.</param>
		/// <param name="depth">The layer to draw the image in.</param>
		public StaticImage(string tex, Vector2 pos, float rotation, Vector2 scale, Vector2 origin, float depth = 0.0f)
		{
			TexSource = tex;

			texture = null;
			Position = pos;
			Origin = origin;
			Rotation = rotation;
			Scale = scale;
			Source = null;
			Tint = Color.White;
			Effects = SpriteEffects.None;
			Depth = depth;
			Visible = true;

			return;
		}

		/// <summary>
		/// Initializes the object.
		/// </summary>
		public void Initialize()
		{return;}
        
		/// <summary>
		/// Loads the object's content.
		/// </summary>
		/// <param name="Graphics">The graphics device in use by the game.</param>
		/// <param name="Content">The content manager to use to load data with.</param>
		/// <remarks>This function should not be called by Initialize.</remarks>
		public virtual void LoadContent(GraphicsDevice Graphics, ContentManager Content)
		{
			texture = Content.Load<Texture2D>(TexSource);
			return;
		}

		/// <summary>
		/// Unloads any content this object has.
		/// </summary>
		/// <remarks>Only data not managed by a ContentManager should be destroyed immediately.</remarks>
		public virtual void UnloadContent()
		{
			texture = null;
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
		public void Update(GameTime gameTime)
		{return;}

		/// <summary>
		/// The texture used for this object.
		/// </summary>
		public Texture2D texture
		{get; protected set;}

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
		public Nullable<Rectangle> Source
		{get; set;}

		/// <summary>
		/// The destination rectangle for this object.
		/// </summary>
		/// <remarks>To change this value, change the position and scale of this object.</remarks>
		public virtual Rectangle Destination
		{
			get
			{
				if(Source.HasValue)
					return new Rectangle((int)Position.X,(int)Position.Y,(int)(Source.Value.Width * Scale.X),(int)(Source.Value.Height * Scale.Y));
				
				if(texture == null)
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
		/// The source location of the texture.
		/// </summary>
		protected string TexSource;
	}
}
