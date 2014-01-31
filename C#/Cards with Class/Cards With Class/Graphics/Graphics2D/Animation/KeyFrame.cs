using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.Animation
{
	/// <summary>
	/// The keyframe of an animation.
	/// </summary>
	public interface KeyFrame
	{
		/// <summary>
		/// Linearly interpolates between this keyframe and the second keyframe.
		/// </summary>
		/// <param name="next">The keyframe to interpolate with.</param>
		/// <param name="t">The time into this frame. Must be between 0 and Length inclusive.</param>
		/// <returns>Returns </returns>
		FrameData Lerp(KeyFrame next, float t);

		/// <summary>
		/// The name of this frame.
		/// </summary>
		string Name
		{get;}

		/// <summary>
		/// The length of this frame.
		/// </summary>
		float Length
		{get;}

		/// <summary>
		/// The position of the frame.
		/// </summary>
		/// <remarks>The position is relative to the original location of what is being animated.</remarks>
		Vector2 Position
		{get;}

		/// <summary>
		/// The rotation of the frame.
		/// </summary>
		/// <remarks>The rotation is relative to the original rotation of what is being animated.</remarks>
		float Rotation
		{get;}

		/// <summary>
		/// The center of rotation for this frame.
		/// </summary>
		Vector2 Origin
		{get;}

		/// <summary>
		/// The scale of the frame.
		/// </summary>
		/// <remarks>The scale is relative to the original scale of what is being animated.</remarks>
		Vector2 Scale
		{get;}

		/// <summary>
		/// The texture of the frame.
		/// </summary>
		Texture2D Texture
		{get;}

		/// <summary>
		/// The part of the texture this frame will use at the start.
		/// </summary>
		Nullable<Rectangle> InitialTextureSource
		{get;}

		/// <summary>
		/// The part of the texture this frame will use at the end.
		/// </summary>
		Nullable<Rectangle> FinalTextureSource
		{get;}

		/// <summary>
		/// The tint of the frame.
		/// </summary>
		/// <remarks>The tint is relative to the original tint of what is being animated. Color modification is multiplicative unless otherwise specified.</remarks>
		Color Tint
		{get;}
	}
}
