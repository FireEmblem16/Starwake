using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.StandardLibrary
{
	/// <summary>
	/// Draws a polygon.
	/// </summary>
	/// <remarks>The number of points can not be changed after creation.</remarks>
	public class Polygon : DrawableGameComponent2D
	{
		/// <summary>
		/// Creates a new static image with default values.
		/// </summary>
		/// <param name="points">The points of the polygon.</param>
		/// <param name="width">The width of the lines of the polygon.</param>
		/// <param name="depth">The layer to draw this image in.</param>
		public Polygon(IList<Vector2> points, float width = 1.0f, float depth = 0.0f)
		{
			if(points.Count == 0)
				points.Add(Vector2.Zero);

			if(points.Count == 1)
				points.Add(points[0]);

			Lines = new List<Line>(points.Count);
			this.points = new List<Vector2>(points);

			for(int i = 0;i < points.Count;i++)
				if(i != points.Count - 1)
					Lines.Add(new Line(points[i],points[i + 1],width,depth));
				else
					Lines.Add(new Line(points[i],points[0],width,depth));

			Vector2 cen_gravity = Vector2.Zero;

			foreach(Vector2 v in points)
				cen_gravity += v;

			cen_gravity /= points.Count;

			texture = null;
			position = cen_gravity;
			rotation = 0.0f;
			scale = new Vector2(1.0f);
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
		{
			foreach(Line l in Lines)
				l.Initialize();

			return;
		}
        
		/// <summary>
		/// Loads the object's content.
		/// </summary>
		/// <param name="Graphics">The graphics device in use by the game.</param>
		/// <param name="Content">The content manager to use to load data with.</param>
		/// <remarks>This function should not be called by Initialize.</remarks>
		public void LoadContent(GraphicsDevice Graphics, ContentManager Content)
		{
			foreach(Line l in Lines)
				l.LoadContent(Graphics,Content);

			return;
		}

		/// <summary>
		/// Unloads any content this object has.
		/// </summary>
		/// <remarks>Only data not managed by a ContentManager should be destroyed immediately.</remarks>
		public void UnloadContent()
		{
			foreach(Line l in Lines)
				l.UnloadContent();

			return;
		}

		/// <summary>
		/// Draws the object.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		public void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode)
		{
			if(!Visible)
				return;

			foreach(Line l in Lines)
				l.Draw2D(gameTime,spriteBatch,mode);
			
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
		public void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot)
		{
			if(!Visible)
				return;
			
			if(transPos != Vector2.Zero)
				Position += transPos;

			if(transRot != 0.0f)
				Rotation += transRot;

			foreach(Line l in Lines)
				l.Draw2D(gameTime,spriteBatch,mode);

			if(transPos != Vector2.Zero)
				Position -= transPos;
			
			if(transRot != 0.0f)
				Rotation -= transRot;

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
		/// The position of the entire polygon.
		/// </summary>
		public Vector2 Position
		{
			get
			{return position;}
			set
			{
				List<Vector2> newpoints = new List<Vector2>(points.Count);
				
				foreach(Vector2 v in points)
					newpoints.Add(v + value - Position); // The translation matrix would just do this and this is faster

				Points = newpoints;
				position = value;
				return;
			}
		}

		/// <summary>
		/// The position of the polygon.
		/// </summary>
		protected Vector2 position;

		/// <summary>
		/// The origin of the object.
		/// This is the center of rotation for the entire polygon.
		/// </summary>
		public Vector2 Origin
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
		/// The rotation of the object.
		/// </summary>
		/// <remarks>Relative to the polygon's center.</remarks>
		public float Rotation
		{
			get
			{return rotation;}
			set
			{
				List<Vector2> newpoints = new List<Vector2>(points.Count);
				value = MathHelper.WrapAngle(value);

				foreach(Vector2 v in points)
					newpoints.Add(Vector2.Transform(v - Position,Matrix.CreateRotationZ(value - Rotation)) + Position);

				Points = newpoints;
				rotation = value;
				return;
			}
		}

		/// <summary>
		/// The rotation of the polygon.
		/// </summary>
		protected float rotation;

		/// <summary>
		/// The scaling factors of the object.
		/// </summary>
		/// <remarks>Relative to the polygon's center.</remarks>
		public Vector2 Scale
		{
			get
			{return scale;}
			set
			{
				if(value.X == 0.0f || value.Y == 0.0f)
					return;

				List<Vector2> newpoints = new List<Vector2>(points.Count);

				foreach(Vector2 v in points)
					newpoints.Add(Vector2.Transform(v - Position,Matrix.CreateScale(value.X / Scale.X,value.Y / Scale.Y,0.0f)) + Position);

				Points = newpoints;
				scale = value;
				return;
			}
		}

		/// <summary>
		/// The scale of the polygon.
		/// </summary>
		protected Vector2 scale;

		/// <summary>
		/// The source in the texture (in texels) to use for drawing.
		/// Use null to draw the entire texture.
		/// </summary>
		public Nullable<Rectangle> Source
		{
			get
			{return Lines[0].Source;}
			set
			{
				foreach(Line l in Lines)
					l.Source = value;

				return;
			}
		}

		/// <summary>
		/// The destination rectangle for this object.
		/// </summary>
		public Rectangle Destination
		{
			get
			{
				Tuple<float,float,float,float> bounds = Bounds;
				return new Rectangle((int)bounds.Item1,(int)bounds.Item3,(int)(bounds.Item2 - bounds.Item1),(int)(bounds.Item4 - bounds.Item3));
			}
		}

		/// <summary>
		/// The bounds of this polygon.
		/// Similar to Destination but more accurate.
		/// Contains the values (minX,maxX,minY,maxY).
		/// </summary>
		protected Tuple<float,float,float,float> Bounds
		{
			get
			{
				float minX = float.PositiveInfinity;
				float maxX = float.NegativeInfinity;
				float minY = float.PositiveInfinity;
				float maxY = float.NegativeInfinity;

				foreach(Vector2 v in points)
				{
					if(v.X < minX)
						minX = v.X;
					
					if(v.X > maxX)
						maxX = v.X;

					if(v.Y < minY)
						minY = v.Y;

					if(v.Y > maxY)
						maxY = v.Y;
				}

				return new Tuple<float,float,float,float>(minX,maxX,minY,maxY);
			}
		}

		/// <summary>
		/// The color to tint this object.
		/// Use white for full color with no tinting.
		/// </summary>
		public Color Tint
		{
			get
			{return Lines[0].Tint;}
			set
			{
				foreach(Line l in Lines)
					l.Tint = value;

				return;
			}
		}

		/// <summary>
		/// The effects to apply to this object.
		/// </summary>
		public SpriteEffects Effects
		{
			get
			{return Lines[0].Effects;}
			set
			{
				foreach(Line l in Lines)
					l.Effects = value;

				return;
			}
		}

		/// <summary>
		/// The depth to draw at on the screen.
		/// By default, when drawing graphics 0 represents the front layer and 1 represents the back layer.
		/// </summary>
		public float Depth
		{
			get
			{return Lines[0].Depth;}
			set
			{
				foreach(Line l in Lines)
					l.Depth = value;

				return;
			}
		}

		/// <summary>
		/// If true then the object is drawn and if false then the object is not drawn.
		/// </summary>
		public bool Visible
		{
			get
			{return Lines[0].Visible;}
			set
			{
				foreach(Line l in Lines)
					l.Visible = value;

				return;
			}
		}

		/// <summary>
		/// The points in this polygon.
		/// </summary>
		public IEnumerable<Vector2> Points
		{
			get
			{return points;}
			protected set
			{
				List<Vector2> newpoints = new List<Vector2>(value);

				if(newpoints.Count != points.Count)
					return;

				points = newpoints;

				for(int i = 0;i < Lines.Count;i++)
					if(i != Lines.Count - 1)
					{
						Lines[i].PointOne = points[i];
						Lines[i].PointTwo = points[i + 1];
					}
					else
					{
						Lines[i].PointOne = points[i];
						Lines[i].PointTwo = points[0];
					}

				return;
			}
		}

		/// <summary>
		/// The number of sides this polygon has.
		/// </summary>
		public int N
		{
			get
			{return Lines.Count;}
		}

		/// <summary>
		/// The width of the line.
		/// </summary>
		public float LineWidth
		{
			get
			{return Lines[0].LineWidth;}
			set
			{
				foreach(Line l in Lines)
					l.LineWidth = value;

				return;
			}
		}

		/// <summary>
		/// The points in this polygon.
		/// </summary>
		protected List<Vector2>points;

		/// <summary>
		/// The lines used to draw this polygon.
		/// </summary>
		protected List<Line> Lines;
	}
}
