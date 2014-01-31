using System.Collections.Generic;
using Graphics.Graphics2D;
using Graphics.Graphics2D.GUILibrary;
using Graphics.Graphics2D.StandardLibrary;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Graphics.Graphics2D.GUILibrary.Components;

namespace ExampleGUI
{
	/// <summary>
	/// This is the main type for your game
	/// </summary>
	public class Game1 : Microsoft.Xna.Framework.Game
	{
		public Game1()
		{
			graphics = new GraphicsDeviceManager(this);
			IsMouseVisible = true;
            
			Content.RootDirectory = "ContentCompiler";
			return;
		}

		/// <summary>
		/// Allows the game to perform any initialization it needs to before starting to run.
		/// This is where it can query for any required services and load any non-graphic
		/// related content.  Calling base.Initialize will enumerate through any components
		/// and initialize them as well.
		/// </summary>
		protected override void Initialize()
		{
			painter = new Draw2DEngine(this);
			Components.Add(painter);

			GUIEngine.Initialize(this);
			
			Button b = new Button(new Vector2(80.0f,100.0f),new Vector2(50.0f,300.0f),"Fonts\\TNR12","Button","Decks\\Classic Cards\\Ace of Clubs","Decks\\Classic Cards\\Ace of Spades","Decks\\Classic Cards\\Ace of Hearts");
			GUIEngine.Engine.Add(b);
			painter.Add(b);
			
			ScrollBox sb = new ScrollBox(new Vector2(100.0f),new Vector2(50.0f,175.0f),"Decks\\Classic Cards\\Ace of Clubs","Decks\\Classic Cards\\Ace of Spades","Decks\\Classic Cards\\Ace of Hearts","Decks\\Classic Cards\\Ace of Clubs","Decks\\Classic Cards\\Ace of Spades","Decks\\Classic Cards\\Ace of Hearts");
			sb.BackgroundColor = Color.AntiqueWhite;
			sb.InnerBackgroundColor = Color.BlueViolet;
			sb.Add(new Line(new Vector2(0.0f),new Vector2(200.0f),10));
			sb.Add(new Button(new Vector2(20.0f,25.0f),new Vector2(150.0f,10.0f),"Fonts\\TNR12","","Decks\\Classic Cards\\Ace of Dimonds","Decks\\Classic Cards\\King of Clubs","Decks\\Classic Cards\\King of Spades"));
			sb.BoxPosition = new Vector2(50.0f,10.0f);
			GUIEngine.Engine.Add(sb);
			painter.Add(sb);
			
			Draw2DGroup group = new Draw2DGroup();
			group.Add(new StaticImage("Decks\\Classic Cards\\Ace of Clubs",new Vector2(100.0f,50.0f),-MathHelper.PiOver4,new Vector2(1.0f)));
			group.Add(new Label("Fonts\\TNR12","Hello World!",new Vector2(300.0f,50.0f),0.0f,new Vector2(1.0f)));
			painter.Add(group);
			
			group = new Draw2DGroup();
			group.Source = new Rectangle(0,0,200,200);
			group.Position = new Vector2(400.0f,100.0f);
			group.BackgroundColor = Color.Violet;
			List<Vector2> Points = new List<Vector2>();
			Points.Add(new Vector2(50.0f,50.0f));
			Points.Add(new Vector2(150.0f,50.0f));
			Points.Add(new Vector2(100.0f,100.0f));
			Polygon poly = new Polygon(Points);
			poly.Tint = Color.Goldenrod;
			poly.Rotation = MathHelper.PiOver2 / 3.0f;
			group.Add(poly);
			painter.Add(group);

			base.Initialize();
			return;
		}

		/// <summary>
		/// LoadContent will be called once per game and is the place to load
		/// all of your content.
		/// </summary>
		protected override void LoadContent()
		{
			spriteBatch = new SpriteBatch(GraphicsDevice);
			Services.AddService(typeof(SpriteBatch),spriteBatch);
			
			base.LoadContent();
			return;
		}

		/// <summary>
		/// UnloadContent will be called once per game and is the place to unload
		/// all content.
		/// </summary>
		protected override void UnloadContent()
		{
			base.UnloadContent();
			return;
		}

		/// <summary>
		/// Allows the game to run logic such as updating the world,
		/// checking for collisions, gathering input, and playing audio.
		/// </summary>
		/// <param name="gameTime">Provides a snapshot of timing values.</param>
		protected override void Update(GameTime gameTime)
		{
			// Allows the game to exit
			if(Keyboard.GetState().IsKeyDown(Keys.Escape))
				this.Exit();

			base.Update(gameTime);
			return;
		}

		/// <summary>
		/// This is called when the game should draw itself.
		/// </summary>
		/// <param name="gameTime">Provides a snapshot of timing values.</param>
		protected override void Draw(GameTime gameTime)
		{
			if(target == null || target.GraphicsDevice != GraphicsDevice || target.Width != GraphicsDevice.PresentationParameters.BackBufferWidth || target.Height != GraphicsDevice.PresentationParameters.BackBufferHeight)
			{
				if(target != null)
					target.Dispose();

				target = new RenderTarget2D(GraphicsDevice,GraphicsDevice.PresentationParameters.BackBufferWidth,GraphicsDevice.PresentationParameters.BackBufferHeight,false,SurfaceFormat.Color,DepthFormat.None,0,RenderTargetUsage.PreserveContents);
			}

			GraphicsDevice.SetRenderTarget(target);
			GraphicsDevice.Clear(Color.CornflowerBlue);
			base.Draw(gameTime);
			GraphicsDevice.SetRenderTarget(null);
			
			BlendState bs = GraphicsDevice.BlendState;
            DepthStencilState dss = GraphicsDevice.DepthStencilState;
            SamplerState ss = GraphicsDevice.SamplerStates[0];

			spriteBatch.Begin(SpriteSortMode.Immediate,BlendState.Opaque);
			spriteBatch.Draw(target,target.Bounds,Color.White);
			spriteBatch.End();

			GraphicsDevice.BlendState = bs;
            GraphicsDevice.DepthStencilState = dss;
            GraphicsDevice.SamplerStates[0] = ss;

			return;
		}

		/// <summary>
		/// Controls features about the display window.
		/// </summary>
		protected GraphicsDeviceManager graphics;

		/// <summary>
		/// The spritebatch used to draw textures as well as the final image.
		/// </summary>
		protected SpriteBatch spriteBatch;
		
		/// <summary>
		/// Allows the game to draw 2D objects.
		/// </summary>
		protected Draw2DEngine painter;

		/// <summary>
		/// The render target we draw to.
		/// </summary>
		protected RenderTarget2D target;
	}
}
