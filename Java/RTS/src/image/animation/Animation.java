package image.animation;

import image.container.Pair;
import item.Item;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import java.util.Timer;
import engine.Engine;
import engine.RunnerTask;
import grid.Location;
import xml.Parser;
import xml.Tag;

/**
 * Holds information on how to perform an animation.
 * If an animation is not looped and finishes then nothing will be drawn afterwards. 
 */
public class Animation extends Observable implements Runnable
{
	/**
	 * Constructs and initializes this animation to run as specified by the file [config].
	 */
	public Animation(String config, Item animate)
	{
		// Initialize things
		next = 0;
		
		name = null;
		next_animation = null;
		
		play = false;
		loop = false;
		
		frames = new ArrayList<KeyFrame>();
		animating = animate;
		
		this.config = config;
		
		if(config == null)
			return;
		
		Scanner in = null;
		
		try
		{
			File f = new File(config);
			in = new Scanner(f);
		}
		catch(FileNotFoundException e)
		{return;}
		
		Parser parser = new Parser(config);
		boolean end = false;
		
		while(parser.HasNextTag() && !end)
		{
			Tag tag = parser.GetNext();
			
			if(tag.IsHeader())
			{
				if(tag.GetName().equals("keyframe"))
					frames.add(new KeyFrame(parser));
				else if(tag.GetName().equals("/animation"))
					end = true;
			}
			else if(tag.IsDescriptor())
			{
				if(tag.GetName().equals("name"))
					name = (String)tag.GetValue();
				else if(tag.GetName().equals("loop"))
					loop = (Boolean)tag.GetValue();
				else if(tag.GetName().equals("start-frame"))
					next = (Integer)tag.GetValue();
				else if(tag.GetName().equals("next-animation"))
					next_animation = (String)tag.GetValue();
			}
		}
		
		if(next < 0 || next >= frames.size())
			next = 0;
		
		return;
	}
	
	/**
	 * Creates a copy of the given animation.
	 */
	public Animation(Animation a)
	{
		if(a == null)
			return;
		
		loop = a.loop;
		play = a.play;
		next = 0;
		animating = a.animating;
		name = a.name;
		next_animation = a.next_animation;
		config = a.config;
		
		frames = new ArrayList<KeyFrame>();
		
		for(int i = 0;i < a.frames.size();i++)
			frames.add(new KeyFrame(a.frames.get(i)));
		
		return;
	}
	
	/**
	 * Returns the frame this animation is currently on.
	 * If this animation has not started yet it returns -1.
	 */
	public int GetFrame()
	{
		return next - 1;
	}
	
	/**
	 * Returns the number of frames in this animation.
	 */
	public int GetFrames()
	{
		return frames.size();
	}
	
	/**
	 * Sets the frame of this animation to [index] if possible. 
	 */
	public boolean SetFrame(int index)
	{
		if(index < 0 || index >= frames.size())
			return false;
		
		next = index;
		return true;
	}
	
	/**
	 * Sets the frame of this animation to the KeyFrame named [frame] if possible.
	 * If multiple frames have the same name the one indexed first will be chosen.
	 */
	public boolean SetFrame(String frame)
	{
		if(frame == null)
			return false;
		
		for(int i = 0;i < frames.size();i++)
			if(frame.equals(frames.get(i).GetPalletName()))
			{
				next = i;
				return true;
			}
		
		return false;
	}
	
	/**
	 * Starts or restarts this animation.
	 */
	public void Start()
	{
		if(!play)
		{
			play = true;
			run();
		}
		
		return;
	}
	
	/**
	 * Stops this animation and preserves the current frame.
	 */
	public void Pause()
	{
		play = false;
		return;
	}
	
	/**
	 * Stops this animation and resets it to the first frame.
	 */
	public void Stop()
	{
		play = false;
		next = 0;
		
		return;
	}
	
	/**
	 * Returns true if this animation is currently palying and false otherwise.
	 */
	public boolean Playing()
	{
		return play;
	}
	
	/**
	 * Sets this animation to loop if [l] is true and has the animation execute only once if [l] is false.
	 */
	public void Loop(boolean l)
	{
		loop = l;
		return;
	}
	
	/**
	 * Returns true if this animation will loop when played and false otherwise.
	 */
	public boolean IsLooping()
	{
		return loop;
	}
	
	/**
	 * Returns the name of this animation.
	 */
	public String GetName()
	{
		return name;
	}
	
	/**
	 * Returns the path to this animation's config file.
	 */
	public String GetConfigName()
	{
		return config;
	}
	
	/**
	 * Changes [animating]'s frame to the next available one or to nothing if none are available.
	 */
	public void run()
	{
		// If we are not playing this animation shouldn't do anything to [animating]
		if(!play)
			return;
		
		if(frames.size() == 0)
		{
			animating.SetPallet(null);
			animating.SetPalletName(null);
			
			if(next_animation != null)
				animating.ChangeAnimation(next_animation);
			
			Stop();
			return;
		}
		
		if(next >= frames.size())
		{
			if(loop)
				next = 0;
			else
			{
				animating.SetPallet(null);
				animating.SetPalletName(null);
				
				if(next_animation != null)
					animating.ChangeAnimation(next_animation);
				
				return;
			}
		}
		
		// We will need to give the old image to our observers so that we can properly redraw
		Pair<String,Item> old = new Pair<String,Item>(animating.GetPallet(),animating.GetPalletName(),animating);
		
		animating.SetPallet(frames.get(next).GetPallet());
		animating.SetPalletName(frames.get(next).GetPalletName());
		
		setChanged();
		notifyObservers(old);
		
		animation_sync.schedule(new RunnerTask(this),frames.get(next++).GetDuration());
		return;
	}
	
	static
	{
		animation_sync = new Timer(true);
	}
	
	/**
	 * Manages the execution of animations.
	 */
	protected static Timer animation_sync;
	
	/**
	 * This will be true if this animation should loop.
	 */
	protected boolean loop;
	
	/**
	 * This is true if this animation should execute.
	 */
	protected boolean play;
	
	/**
	 * This contains the index of the next frame in the animation.
	 */
	protected int next;
	
	/**
	 * This will contain all of the KeyFrame's of this animation.
	 */
	protected ArrayList<KeyFrame> frames;
	
	/**
	 * This will hold the item we want to animate.
	 */
	protected Item animating;
	
	/**
	 * This is the name that has been given to this animation.
	 */
	protected String name;
	
	/**
	 * This is the name of the next animation to execute, if any.
	 * This will be ignored if we are looping.
	 */
	protected String next_animation;
	
	/**
	 * Contains the path to this animation's config file.
	 */
	protected String config;
}
