using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.StandardLibrary
{
	/// <summary>
	/// Draws a line.
	/// </summary>
	public class Line : StaticImage
	{
		/// <summary>
		/// Creates a new static image with default values.
		/// </summary>
		/// <param name="A">The first point in the line.</param>
		/// <param name="B">The second point in the line.</param>
		/// <param name="width">The width of the line.</param>
		/// <param name="depth">The layer to draw this image in.</param>
		public Line(Vector2 A, Vector2 B, float width = 1.0f, float depth = 0.0f) : base("",depth)
		{
			PointOne = A;
			PointTwo = B;
			LineWidth = width;
			
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
			texture = new Texture2D(Graphics,1,1);
			Color[] data = new Color[1];
			data[0] = Color.White;
			
			texture.SetData<Color>(data);
			return;
		}

		/// <summary>
		/// Unloads any content this object has.
		/// </summary>
		/// <remarks>Only data not managed by a ContentManager should be destroyed immediately.</remarks>
		public override void UnloadContent()
		{
			texture.Dispose();
			texture = null;

			return;
		}

		/// <summary>
		/// Draws the object.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <remarks>Does not utilize origin.</remarks>
		public override void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode)
		{
			if(!Visible)
				return;
			
			spriteBatch.Draw(texture,Position,Source,Tint,BaseRotation + Rotation,Vector2.Zero,new Vector2(Vector2.Distance(PointOne,PointTwo),LineWidth),Effects,Depth);
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
		/// <remarks>Does not utilize origin.</remarks>
		public override void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot)
		{
			if(!Visible)
				return;

			spriteBatch.Draw(texture,Position + transPos,Source,Tint,BaseRotation + Rotation + transRot,Vector2.Zero,new Vector2(Vector2.Distance(PointOne,PointTwo),LineWidth),Effects,Depth);
			return;
		}

		/// <summary>
		/// The destination rectangle for this object.
		/// </summary>
		/// <remarks>To change this value, change the position and scale of this object.</remarks>
		public override Rectangle Destination
		{
			get
			{
				int minx = PointOne.X < PointTwo.X ? (int)PointOne.X : (int)PointTwo.X;
				int miny = PointOne.Y < PointTwo.X ? (int)PointOne.Y : (int)PointTwo.Y;
				int maxx = PointOne.X > PointTwo.Y ? (int)PointOne.X : (int)PointTwo.X;
				int maxy = PointOne.Y > PointTwo.Y ? (int)PointOne.Y : (int)PointTwo.Y;

				return new Rectangle(minx,miny,maxx - minx,maxy - miny);
			}
		}

		/// <summary>
		/// The first point of this line.
		/// </summary>
		/// <remarks>This is equivalent to the position of the object.</remarks>
		public Vector2 PointOne
		{
			get
			{return Position;}
			set
			{
				Position = value;
				return;
			}
		}

		/// <summary>
		/// The second point of this line.
		/// </summary>
		public Vector2 PointTwo
		{get; set;}

		/// <summary>
		/// The rotation needed to turn a horizontal line into the line with the given two points.
		/// </summary>
		public float BaseRotation
		{
			get
			{
				float angle = (float)Math.Acos(Vector2.Dot(Vector2.Normalize(PointOne - PointTwo),-Vector2.UnitX));
				
				if(PointOne.Y > PointTwo.Y)
					angle = 2.0f * (float)Math.PI - angle;
				
				return angle;
			}
		}

		/// <summary>
		/// The width of the line.
		/// </summary>
		public float LineWidth
		{
			get
			{return Scale.Y;}
			set
			{
				Scale = new Vector2(Scale.X,value);
				return;
			}
		}

		/// <summary>
		/// Gets the normal of this line.
		/// </summary>
		/// <remarks>The normal will be computed so that (PointOne - PointTwo) X N points in the positive z direction.</remarks>
		public Vector2 Normal
		{
			get
			{
				Vector2 v = PointOne - PointTwo;
				v = new Vector2(-v.Y,v.X);
				v.Normalize();

				return v;
			}
		}
	}
}
