using System;
using System.Threading;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

// The IGraphicsDeviceService interface requires a DeviceCreated event, but we
// always just create the device inside our constructor, so we have no place to
// raise that event. The C# compiler warns us that the event is never used, but
// we don't care so we just disable this warning.
#pragma warning disable 67

namespace Graphics.Graphics2D.XNAForms
{
    /// <summary>
    /// Helper class responsible for creating and managing the GraphicsDevice.
    /// All GraphicsDeviceControl instances share the same GraphicsDeviceService,
    /// so even though there can be many controls, there will only ever be a single
    /// underlying GraphicsDevice. This implements the standard IGraphicsDeviceService
    /// interface, which provides notification events for when the device is reset
    /// or disposed.
    /// </summary>
   public class GraphicsDeviceService : IGraphicsDeviceService
    {
        #region Fields

        // Singleton device service instance.
        protected static GraphicsDeviceService singletonInstance;
        protected static ContentManager content;

        // Keep track of how many controls are sharing the singletonInstance.
        static int referenceCount;

        #endregion


        /// <summary>
        /// Constructor is private, because this is a singleton class:
        /// client controls should use the public AddRef method instead.
        /// </summary>
        protected GraphicsDeviceService(IntPtr windowHandle, int width, int height)
        {
            parameters = new PresentationParameters();

            parameters.BackBufferWidth = Math.Max(width,1);
            parameters.BackBufferHeight = Math.Max(height,1);
            parameters.BackBufferFormat = SurfaceFormat.Color;
            parameters.DepthStencilFormat = DepthFormat.Depth24;
            parameters.DeviceWindowHandle = windowHandle;
            parameters.PresentationInterval = PresentInterval.Immediate;
            parameters.IsFullScreen = false;
            
            graphicsDevice = new GraphicsDevice(GraphicsAdapter.DefaultAdapter,GraphicsProfile.Reach,parameters);
        }

        /// <summary>
        /// Gets a reference to the singleton instance.
        /// </summary>
        public static GraphicsDeviceService AddRef(IntPtr windowHandle, int width, int height)
        {
            // Increment the "how many controls sharing the device" reference count.
            if(Interlocked.Increment(ref referenceCount) == 1)
            {
                // If this is the first control to start using the
                // device, we must create the singleton instance.
                singletonInstance = new GraphicsDeviceService(windowHandle,width,height);
                content = new ContentManager(null,"");
            }

            return singletonInstance;
        }

        /// <summary>
        /// Sets the root directory of the content manager.
        /// </summary>
        /// <param name="root">The new root directory of the content manager.</param>
        /// <returns>Returns true if the root directory could be changed and false otherwise.</returns>
        public static bool SetRootDirectory(string root)
        {
            if(content == null)
                return false;

            content.RootDirectory = root;
            return true;
        }

        /// <summary>
        /// Releases a reference to the singleton instance.
        /// </summary>
        public void Release(bool disposing)
        {
            // Decrement the "how many controls sharing the device" reference count.
            if(Interlocked.Decrement(ref referenceCount) == 0)
            {
                // If this is the last control to finish using the
                // device, we should dispose the singleton instance.
                if(disposing)
                {
                    if(DeviceDisposing != null)
                        DeviceDisposing(this,EventArgs.Empty);

                    graphicsDevice.Dispose();
                    content.Dispose();
                }

                graphicsDevice = null;
                content = null;
            }
        }


        /// <summary>
        /// Resets the graphics device to whichever is bigger out of the specified
        /// resolution or its current size. This behavior means the device will
        /// demand-grow to the largest of all its GraphicsDeviceControl clients.
        /// </summary>
        public void ResetDevice(int width, int height)
        {
            if(DeviceResetting != null)
                DeviceResetting(this,EventArgs.Empty);

            parameters.BackBufferWidth = Math.Max(parameters.BackBufferWidth,width);
            parameters.BackBufferHeight = Math.Max(parameters.BackBufferHeight,height);

            graphicsDevice.Reset(parameters);

            if(DeviceReset != null)
                DeviceReset(this,EventArgs.Empty);
        }


        /// <summary>
        /// Gets the current graphics device.
        /// </summary>
        public GraphicsDevice GraphicsDevice
        {
            get
            {return graphicsDevice;}
        }

        /// <summary>
        /// Gets the content manager.
        /// </summary>
        public ContentManager Content
        {
            get
            {return content;}
        }

        protected GraphicsDevice graphicsDevice;

        // Store the current device settings.
        protected PresentationParameters parameters;

        // IGraphicsDeviceService events.
        public event EventHandler<EventArgs> DeviceCreated;
        public event EventHandler<EventArgs> DeviceDisposing;
        public event EventHandler<EventArgs> DeviceReset;
        public event EventHandler<EventArgs> DeviceResetting;
    }
}
