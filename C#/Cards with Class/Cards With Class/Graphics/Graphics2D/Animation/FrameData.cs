using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.Animation
{
	/// <summary>
	/// Data resulting from a linear interpolation between two keyframes.
	/// </summary>
	public interface FrameData
	{
		/// <summary>
		/// The position from the resulting lerp.
		/// </summary>
		Vector2 Position
		{get;}

		/// <summary>
		/// The rotation from the resulting lerp.
		/// </summary>
		float Rotation
		{get;}

		/// <summary>
		/// The center of rotation from the resulting lerp.
		/// </summary>
		Vector2 Origin
		{get;}

		/// <summary>
		/// The scale from the resulting lerp.
		/// </summary>
		Vector2 Scale
		{get;}

		/// <summary>
		/// The texture of the first frame.
		/// </summary>
		Texture2D Texture1
		{get;}

		/// <summary>
		/// The texture of the second frame.
		/// </summary>
		Texture2D Texture2
		{get;}

		/// <summary>
		/// The source rectangle for the first texture.
		/// </summary>
		Nullable<Rectangle> TextureSource1
		{get;}

		/// <summary>
		/// The source rectangle for the second texture.
		/// </summary>
		Nullable<Rectangle> TextureSource2
		{get;}

		/// <summary>
		/// The tint of the frame.
		/// </summary>
		/// <remarks>The tint is relative to the original tint of what is being animated. Color modification is multiplicative by default.</remarks>
		Color Tint
		{get;}
	}
}
