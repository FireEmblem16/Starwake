using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D
{
	/// <summary>
	/// The requirements for a class to be a two dimensional drawable component.
	/// </summary>
	public interface DrawableGameComponent2D
	{
		/// <summary>
		/// Initializes the object.
		/// </summary>
		void Initialize();
        
		/// <summary>
		/// Loads the object's content.
		/// </summary>
		/// <param name="Graphics">The graphics device in use by the game.</param>
		/// <param name="Content">The content manager to use to load data with.</param>
		/// <remarks>This function should not be called by Initialize.</remarks>
		void LoadContent(GraphicsDevice Graphics, ContentManager Content);
		
		/// <summary>
		/// Unloads any content this object has.
		/// </summary>
		/// <remarks>Only data not managed by a ContentManager should be destroyed immediately.</remarks>
		void UnloadContent();

		/// <summary>
		/// Draws the object. Must be called in a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode);

		/// <summary>
		/// Draws the object. Must be called in a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <param name="transPos">The distance to translate the object from its current position.</param>
		/// <param name="transRot">The amount in radians to rotate the object from its current rotation.</param>
		void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot);

		/// <summary>
		/// Updates the object.
		/// </summary>
		/// <param name="gameTime">The time the object is updated at.</param>
		void Update(GameTime gameTime);

		/// <summary>
		/// The texture used for this object.
		/// </summary>
		Texture2D texture
		{get;}

		/// <summary>
		/// The position of the object.
		/// </summary>
		Vector2 Position
		{get; set;}

		/// <summary>
		/// The origin of the object.
		/// This is the center of rotation for the object.
		/// </summary>
		Vector2 Origin
		{get; set;}

		/// <summary>
		/// The rotation of the object.
		/// </summary>
		float Rotation
		{get; set;}

		/// <summary>
		/// The scaling factors of the object.
		/// </summary>
		Vector2 Scale
		{get; set;}

		/// <summary>
		/// The source in the texture (in texels) to use for drawing.
		/// Use null to draw the entire texture.
		/// </summary>
		Nullable<Rectangle> Source
		{get; set;}

		/// <summary>
		/// The destination rectangle for this object.
		/// </summary>
		/// <remarks>To change this value, change the position and scale of this object.</remarks>
		Rectangle Destination
		{get;}

		/// <summary>
		/// The color to tint this object.
		/// Use white for full color with no tinting.
		/// </summary>
		Color Tint
		{get; set;}

		/// <summary>
		/// The effects to apply to this object.
		/// </summary>
		SpriteEffects Effects
		{get; set;}

		/// <summary>
		/// The depth to draw at on the screen.
		/// By default, when drawing graphics 0 represents the front layer and 1 represents the back layer.
		/// </summary>
		float Depth
		{get; set;}

		/// <summary>
		/// If true then the object is drawn and if false then the object is not drawn.
		/// </summary>
		bool Visible
		{get; set;}
	}
}
