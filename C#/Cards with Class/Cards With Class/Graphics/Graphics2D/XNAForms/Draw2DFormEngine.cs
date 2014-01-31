using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Windows.Forms;
using Graphics.Graphics2D.GUILibrary;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Graphics.Graphics2D.XNAForms
{
    /// <summary>
    /// The engine that allows two dimensional graphics to be drawn with minimal graphics slowdown.
    /// </summary>
    public class Draw2DFormEngine : GraphicsDeviceControl
    {
        /// <summary>
        /// Creates a new engine to draw two dimensional graphics.
        /// </summary>
        public Draw2DFormEngine()
        {
            Sorting = SpriteSortMode.BackToFront;
            Blending = BlendState.AlphaBlend;

            objects = new List<DrawableGameComponent2D>();
            twopass_objects = new List<TwoPassDrawableGameObject2D>();
            
            Initialized = false;
            Loaded = false;
            Unloaded = false;

            timer = Stopwatch.StartNew();
            last = new TimeSpan(0,0,0,0,0);

            Application.Idle += delegate{Invalidate();}; // Force constant redraws to force constant updates
            return;
        }

        /// <summary>
        /// Initializes the graphics engine.
        /// </summary>
        protected override void Initialize()
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
        /// Loads the content of everything in the graphics engine.
        /// </summary>
        /// <remarks>This function should not be called by Initialize.</remarks>
        protected override void LoadContent()
        {
            Unloaded = false;
            
            foreach(DrawableGameComponent2D obj in objects)
                obj.LoadContent(GraphicsDevice,Content);
            
            foreach(DrawableGameComponent2D obj in twopass_objects)
                obj.LoadContent(GraphicsDevice,Content);

            Loaded = true;
            return;
        }

        /// <summary>
        /// Unloads any content objects in this graphics engine have.
        /// </summary>
        /// <param name="disposing">If true then disposing is happening.</param>
        protected override void Dispose(bool disposing)
        {
            foreach(DrawableGameComponent2D obj in objects)
                obj.UnloadContent();

            foreach(DrawableGameComponent2D obj in twopass_objects)
                obj.UnloadContent();

            base.Dispose(disposing); // Placed last just in case
            Unloaded = true;
            Loaded = false;
            Initialized = false;

            return;
        }

        /// <summary>
        /// Adds a new object to this graphics engine.
        /// </summary>
        /// <param name="obj">The object to add.</param>
        /// <returns>Returns true if the object was added and false if not.</returns>
        public bool Add(DrawableGameComponent2D obj)
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
                obj.LoadContent(GraphicsDevice,Content);

            return true;
        }

        /// <summary>
        /// Adds the provided objects to this graphics engine.
        /// </summary>
        /// <param name="objs">The objects to add.</param>
        /// <returns>Returns true if the objects was added and false if not.</returns>
        public bool Add(IEnumerable<DrawableGameComponent2D> objs)
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
                    obj.LoadContent(GraphicsDevice,Content);
            
            return true;
        }

        /// <summary>
        /// Removes the provided object from this graphics engine.
        /// </summary>
        /// <param name="obj">The object to remove.</param>
        /// <returns>Returns true if the object was removed and false otherwise.</returns>
        public bool Remove(DrawableGameComponent2D obj)
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
            return true;
        }

        /// <summary>
        /// Removes the provided objects from this graphics engine.
        /// </summary>
        /// <param name="objs">The objects to remove.</param>
        /// <returns>The number of objects actually removed.</returns>
        public int Remove(IEnumerable<DrawableGameComponent2D> objs)
        {
            int ret = 0;

            foreach(DrawableGameComponent2D obj in objs)
                if(Remove(obj))
                    ret++;

            return ret;
        }

        /// <summary>
        /// Updates everything in this graphics engine.
        /// </summary>
        /// <param name="gameTime">The time the objects are being updated at.</param>
        public void Update(GameTime gameTime)
        {
            foreach(DrawableGameComponent2D obj in objects)
                obj.Update(gameTime);

            foreach(DrawableGameComponent2D obj in twopass_objects)
                obj.Update(gameTime);

            return;
        }

        /// <summary>
        /// Draws everything managed by this graphics engine.
        /// </summary>
        /// <remarks>Requires a spritebatch in the game's services.</remarks>
        protected override void Draw()
        {
            GameTime gameTime = new GameTime(timer.Elapsed,timer.Elapsed.Subtract(last));
            last = timer.Elapsed;

            // Update here since we'll never get it anywhere else
            Update(gameTime);

            SpriteBatch spriteBatch = (SpriteBatch)Services.GetService(typeof(SpriteBatch));
            BlendState bs = GraphicsDevice.BlendState;
            DepthStencilState dss = GraphicsDevice.DepthStencilState;
            SamplerState ss = GraphicsDevice.SamplerStates[0];

            foreach(TwoPassDrawableGameObject2D obj in twopass_objects)
                obj.FirstPass(gameTime,spriteBatch,Sorting);

            spriteBatch.Begin(Sorting,Blending);

            foreach(DrawableGameComponent2D obj in objects)
                obj.Draw2D(gameTime,spriteBatch,Sorting);

            foreach(DrawableGameComponent2D obj in twopass_objects)
                obj.Draw2D(gameTime,spriteBatch,Sorting);

            spriteBatch.End();

            GraphicsDevice.BlendState = bs;
            GraphicsDevice.DepthStencilState = dss;
            GraphicsDevice.SamplerStates[0] = ss;

            return;
        }

        /// <summary>
        /// The sorting, if any, applied to objects drawn with this graphics engine.
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
        /// The objects to draw.
        /// </summary>
        protected List<DrawableGameComponent2D> objects;

        /// <summary>
        /// The objects to draw that need a second pass.
        /// </summary>
        protected List<TwoPassDrawableGameObject2D> twopass_objects;

        /// <summary>
        /// The time the game is at.
        /// </summary>
        protected Stopwatch timer;

        /// <summary>
        /// The last time we had an update at.
        /// </summary>
        protected TimeSpan last;
    }
}
