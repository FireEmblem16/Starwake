#region File Description
//-----------------------------------------------------------------------------
// Game1.cs
//
// Microsoft XNA Community Game Platform
// Copyright (C) Microsoft Corporation. All rights reserved.
//-----------------------------------------------------------------------------
#endregion

using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Net;
using Microsoft.Xna.Framework.Storage;

namespace ThirdPersonCamera
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        Matrix view;
        Matrix proj;

        Model box;
        Texture2D boxTexture;
        Texture2D avatarTexture;

        // Set the avatar position and rotation variables.
        Vector3 avatarPosition = new Vector3(0, 0, -50);

        Vector3 avatarHeadOffset = new Vector3(0, 10, 0);

        float avatarYaw;

        // Set the direction the camera points without rotation.
        Vector3 cameraReference = new Vector3(0, 0, 10);

        Vector3 thirdPersonReference = new Vector3(0, 200, -200);

        // Set rates in world units per 1/60th second (the default fixed-step interval).
        float rotationSpeed = 1f / 60f;
        //float forwardSpeed = 500f / 60f;
        float forwardSpeed = 50f / 60f;

        // Set field of view of the camera in radians (pi/4 is 45 degrees).
        static float viewAngle = MathHelper.PiOver4;

        // Set distance from the camera of the near and far clipping planes.
        //static float nearClip = 5.0f;
        static float nearClip = 1.0f;
        static float farClip = 2000.0f;

        // Set the camera state, avatar's center, first-person, third-person.
        int cameraState = 2;
        bool cameraStateKeyDown;

        KeyboardState oldKeyState;

        SpriteBatch foregroundBatch;
        SpriteFont comicSansMS;
        Vector2 fontPos;
        float fontRotation;

        GraphicsDeviceManager graphics;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here

            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            box = Content.Load<Model>("box");
            boxTexture = Content.Load<Texture2D>("boxtexture");
            avatarTexture = Content.Load<Texture2D>("avatartexture");
            comicSansMS = Content.Load<SpriteFont>("ComicSansMS");
            foregroundBatch = new SpriteBatch(GraphicsDevice);

            oldKeyState = Keyboard.GetState();
            fontRotation = 0;
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            // Allows the game to exit
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
            {
                this.Exit();
            }

            GetCurrentCamera();
            UpdateAvatarPosition();

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            graphics.GraphicsDevice.Clear(Color.SteelBlue);

            switch (cameraState)
            {
                default:
                case 0:
                    UpdateCamera();
                    break;
                case 1:
                    UpdateCameraFirstPerson();
                    break;
                case 2:
                    UpdateCameraThirdPerson();
                    break;
            }

            DrawBoxes();
            Matrix World = Matrix.CreateRotationY(avatarYaw) * Matrix.CreateTranslation(avatarPosition);
            if (cameraState == 2)
            {
                DrawModel(box, World, avatarTexture);
            }
            DrawCameraState();

            base.Draw(gameTime);
        }

        /// <summary>
        /// Draws the current camera state (1st, 1st offset, or 3rd person)
        /// </summary>
        private void DrawCameraState()
        {
            foregroundBatch.Begin();

            string message = "-Person camera selected";

            switch (cameraState)
            {
                case 0:
                    message = "First" + message;
                    break;
                case 1:
                    message = "First" + message + " (offset)";
                    break;
                case 2:
                    message = "Third" + message;
                    break;
            };

            fontPos = new Vector2(20, 20);
            // Draw the string
            foregroundBatch.DrawString(comicSansMS, message, fontPos, Color.LightGreen,
                fontRotation, Vector2.Zero, 1.0f, SpriteEffects.None, 0.5f);

            // Draw instructions
            Vector2 guidepos = new Vector2(fontPos.X, fontPos.Y + comicSansMS.LineSpacing);
            string guide = "Press TAB or left shoulder button to change camera";
            foregroundBatch.DrawString(comicSansMS, guide, guidepos, Color.White);

            foregroundBatch.End();
            graphics.GraphicsDevice.RenderState.DepthBufferEnable = true;
        }

        /// <summary>
        /// Update the position and direction of the avatar.
        /// </summary>
        void UpdateAvatarPosition()
        {
            KeyboardState keyboardState = Keyboard.GetState();
            GamePadState currentState = GamePad.GetState(PlayerIndex.One);

            if (keyboardState.IsKeyDown(Keys.Left) || (currentState.DPad.Left == ButtonState.Pressed))
            {
                // Rotate left.
                avatarYaw += rotationSpeed;
            }

            if (keyboardState.IsKeyDown(Keys.Right) || (currentState.DPad.Right == ButtonState.Pressed))
            {
                // Rotate right.
                avatarYaw -= rotationSpeed;
            }

            if (keyboardState.IsKeyDown(Keys.Up) || (currentState.DPad.Up == ButtonState.Pressed))
            {
                Matrix forwardMovement = Matrix.CreateRotationY(avatarYaw);
                Vector3 v = new Vector3(0, 0, forwardSpeed);
                v = Vector3.Transform(v, forwardMovement);
                avatarPosition.Z += v.Z;
                avatarPosition.X += v.X;
            }

            if (keyboardState.IsKeyDown(Keys.Down) || (currentState.DPad.Down == ButtonState.Pressed))
            {
                Matrix forwardMovement = Matrix.CreateRotationY(avatarYaw);
                Vector3 v = new Vector3(0, 0, -forwardSpeed);
                v = Vector3.Transform(v, forwardMovement);
                avatarPosition.Z += v.Z;
                avatarPosition.X += v.X;
            }
        }

        /// <summary>
        /// Gets the current camera state.
        /// </summary>
        void GetCurrentCamera()
        {
            KeyboardState currentKeyState = Keyboard.GetState();
            GamePadState currentState = GamePad.GetState(PlayerIndex.One);

            // Toggle the state of the camera.
            if ((oldKeyState.IsKeyDown(Keys.Tab) && currentKeyState.IsKeyUp(Keys.Tab)) ||
                (currentState.Buttons.LeftShoulder == ButtonState.Pressed))
            {
                cameraStateKeyDown = true;
            }
            else if (cameraStateKeyDown == true)
            {
                cameraStateKeyDown = false;
                cameraState += 1;
                cameraState %= 3;
            }

            oldKeyState = currentKeyState;
        }

        /// <summary>
        /// Updates the camera when it's in the 1st person state.
        /// </summary>
        void UpdateCamera()
        {
            // Calculate the camera's current position.
            Vector3 cameraPosition = avatarPosition;

            Matrix rotationMatrix = Matrix.CreateRotationY(avatarYaw);

            // Create a vector pointing the direction the camera is facing.
            Vector3 transformedReference = Vector3.Transform(cameraReference, rotationMatrix);

            // Calculate the position the camera is looking at.
            Vector3 cameraLookat = cameraPosition + transformedReference;

            // Set up the view matrix and projection matrix.
            view = Matrix.CreateLookAt(cameraPosition, cameraLookat, new Vector3(0.0f, 1.0f, 0.0f));

            Viewport viewport = graphics.GraphicsDevice.Viewport;
            float aspectRatio = (float)viewport.Width / (float)viewport.Height;

            proj = Matrix.CreatePerspectiveFieldOfView(viewAngle, aspectRatio, nearClip, farClip);
        }

        /// <summary>
        /// Updates the camera when it's in the 1st person offset state.
        /// </summary>
        void UpdateCameraFirstPerson()
        {
            Matrix rotationMatrix = Matrix.CreateRotationY(avatarYaw);

            // Transform the head offset so the camera is positioned properly relative to the avatar.
            Vector3 headOffset = Vector3.Transform(avatarHeadOffset, rotationMatrix);

            // Calculate the camera's current position.
            Vector3 cameraPosition = avatarPosition + headOffset;

            // Create a vector pointing the direction the camera is facing.
            Vector3 transformedReference = Vector3.Transform(cameraReference, rotationMatrix);
            
            // Calculate the position the camera is looking at.
            Vector3 cameraLookat = transformedReference + cameraPosition;

            // Set up the view matrix and projection matrix.
            view = Matrix.CreateLookAt(cameraPosition, cameraLookat, new Vector3(0.0f, 1.0f, 0.0f));

            Viewport viewport = graphics.GraphicsDevice.Viewport;
            float aspectRatio = (float)viewport.Width / (float)viewport.Height;

            proj = Matrix.CreatePerspectiveFieldOfView(viewAngle, aspectRatio, nearClip, farClip);

        }

        /// <summary>
        /// Updates the camera when it's in the 3rd person state.
        /// </summary>
        void UpdateCameraThirdPerson()
        {
            Matrix rotationMatrix = Matrix.CreateRotationY(avatarYaw);

            // Create a vector pointing the direction the camera is facing.
            Vector3 transformedReference = Vector3.Transform(thirdPersonReference, rotationMatrix);

            // Calculate the position the camera is looking from.
            Vector3 cameraPosition = transformedReference + avatarPosition;

            // Set up the view matrix and projection matrix.
            view = Matrix.CreateLookAt(cameraPosition, avatarPosition, new Vector3(0.0f, 1.0f, 0.0f));

            Viewport viewport = graphics.GraphicsDevice.Viewport;
            float aspectRatio = (float)viewport.Width / (float)viewport.Height;

            proj = Matrix.CreatePerspectiveFieldOfView(viewAngle, aspectRatio, nearClip, farClip);
        }


        /// <summary>
        /// Draws a field of evenly-spaced 3D boxes.
        /// </summary>
        void DrawBoxes()
        {
            for (int z = 0; z < 9; z++)
            {
                for (int x = 0; x < 9; x++)
                {
                    DrawModel(box, Matrix.CreateTranslation(x * 60, 0, z * 60), boxTexture);
                }
            }
        }

        /// <summary>
        /// Draws the 3D specified model.
        /// </summary>
        /// <param name="model">The 3D model being drawn.</param>
        /// <param name="world">Transformation matrix for world coords.</param>
        /// <param name="texture">Texture used for the drawn 3D model.</param>
        void DrawModel(Model model, Matrix world, Texture2D texture)
        {
            foreach (ModelMesh mesh in model.Meshes)
            {
                foreach (BasicEffect be in mesh.Effects)
                {
                    be.Projection = proj;
                    be.View = view;
                    be.World = world;
                    be.Texture = texture;
                    be.TextureEnabled = true;
                }
                mesh.Draw();
            }
        }
    }
}
