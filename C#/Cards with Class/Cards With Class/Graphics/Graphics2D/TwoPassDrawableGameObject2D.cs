using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D
{
	/// <summary>
	/// A drawable game component that requires two passes to be drawn.
	/// The first pass is called outside of a spirtebatch begin/end and the second is called normally.
	/// </summary>
	public interface TwoPassDrawableGameObject2D : DrawableGameComponent2D
	{
		/// <summary>
		/// The first pass rendering of the object. This function must be called outside of a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <remarks>This function is not responsible for resetting the render state changes made by spritebatch calls.</remarks>
		void FirstPass(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode);

		/// <summary>
		/// The first pass rendering of the object. This function must be called outside of a spritebatch begin/end call.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		/// <param name="transPos">The distance to translate the object from its current position. This may have a different meaning than for Draw2D.</param>
		/// <param name="transRot">The amount in radians to rotate the object from its current rotation. This may have a different meaning than for Draw2D.</param>
		/// <remarks>This function is not responsible for resetting the render state changes made by spritebatch calls.</remarks>
		void FirstPass(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode, Vector2 transPos, float transRot);
	}
}
