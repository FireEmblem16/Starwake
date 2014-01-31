using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.Animation
{
	/// <summary>
	/// The outline of what an animation is.
	/// </summary>
	public interface Animation
	{
		/// <summary>
		/// Plays the animation.
		/// </summary>
		/// <param name="start">Where to start in the animation.</param>
		void Play(float start = 0.0f);

		/// <summary>
		/// Plays the animation.
		/// </summary>
		/// <param name="frame">The frame to start playing from.</param>
		void Play(int frame);

		/// <summary>
		/// Pauses the animation.
		/// </summary>
		void Pause();

		/// <summary>
		/// Stops the animation and resets it to the beginning.
		/// </summary>
		void Stop();

		/// <summary>
		/// Skips to the specified time in the animation.
		/// </summary>
		/// <param name="time">The time to jump to.</param>
		void SkipTo(float time);

		/// <summary>
		/// Skips to the specified frame in the animation.
		/// </summary>
		/// <param name="frame">The frame number to skip to.</param>
		void SkipTo(int frame);

		/// <summary>
		/// Draws the object.
		/// </summary>
		/// <param name="gameTime">The time the object is being drawn at.</param>
		/// <param name="spriteBatch">The spritebatch to use for drawing the component.</param>
		/// <param name="mode">The sprite sorting mode in use.</param>
		void Draw2D(GameTime gameTime, SpriteBatch spriteBatch, SpriteSortMode mode);

		/// <summary>
		/// The name of the animation.
		/// </summary>
		string Name
		{get;}

		/// <summary>
		/// If true then this animation can be combined with other animations.
		/// </summary>
		/// <remarks>In other words, the changes in position, rotation, scale, etc... will all be expressed as deltas instead of fixed locations. For example, rotation += 5 instead of rotation = 170.</remarks>
		bool Additive
		{get;}

		/// <summary>
		/// The key frames in this animation.
		/// </summary>
		IEnumerable<KeyFrame> KeyFrames
		{get;}

		/// <summary>
		/// The number of keyframe in this animation.
		/// </summary>
		int KeyFrameCount
		{get;}

		/// <summary>
		/// The current key frame in the animation.
		/// </summary>
		KeyFrame CurrentFrame
		{get;}

		/// <summary>
		/// The index of the current keyframe.
		/// </summary>
		int CurrentFrameIndex
		{get;}

		/// <summary>
		/// The next key frame in the animation.
		/// </summary>
		KeyFrame NextFrame
		{get;}

		/// <summary>
		/// The index of the next keyframe or -1 if no such frame exists.
		/// </summary>
		int NextFrameIndex
		{get;}

		/// <summary>
		/// The previous key frame in the animation.
		/// </summary>
		KeyFrame PreviousFrame
		{get;}

		/// <summary>
		/// The index of the previous keyframe or -1 if no such frame exists.
		/// </summary>
		int PreviousFrameIndex
		{get;}

		/// <summary>
		/// The length of the animation.
		/// </summary>
		float Length
		{get;}

		/// <summary>
		/// The current time in the animation.
		/// Change this to jump forward or backwards.
		/// </summary>
		float CurrentTime
		{get; set;}

		/// <summary>
		/// If true then the animation is playing.
		/// </summary>
		bool Playing
		{get; set;}

		/// <summary>
		/// If true then the animation is paused.
		/// </summary>
		bool Paused
		{get; set;}

		/// <summary>
		/// If true then the animation is stopped.
		/// </summary>
		bool Stopped
		{get; set;}

		/// <summary>
		/// If true then the animation should loop when it finishes.
		/// </summary>
		bool Loop
		{get; set;}

		/// <summary>
		/// If true then if the animation is looping then any time the animation goes between the first and last frame it will interpolate between them.
		/// </summary>
		bool InterpolateFirstLast
		{get; set;}

		/// <summary>
		/// If true then the animation should play forward.
		/// If false then the animation will play in reverse.
		/// </summary>
		bool Forward
		{get; set;}
	}
}
