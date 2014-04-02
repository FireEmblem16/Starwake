package rm.core.achievement;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class RunicMagicAchievement extends Achievement
{
	/**
	 * Creates a new achievement.
	 * @param name The name of the achievement.
	 * @param desc The description of the achivement.
	 * @param x The x position in the page of this achivement.
	 * @param y The y position in the page of this achivement.
	 * @param img What the achivement is displayed as.
	 * @param prereq The prerequisite achivement. 
	 * @param special Determines if this achivement is special or not.
	 * @param event_handler The object which will allow the achivement to actually be achieved.
	 */
	public RunicMagicAchievement(String name, String desc, int x, int y, Block img, Achievement prereq, boolean special, Object event_handler)
	{
		super("achivement." + name,name,x,y,img,prereq);

		this.name = name;
		description = desc;
		
		EventHandler = event_handler;
		
		if(special)
			setSpecial();
		
		if(prereq == null)
			initIndependentStat();
		
		registerStat();
		return;
	}
	
	/**
	 * Creates a new achievement.
	 * @param name The name of the achievement.
	 * @param desc The description of the achivement.
	 * @param x The x position in the page of this achivement.
	 * @param y The y position in the page of this achivement.
	 * @param img What the achivement is displayed as.
	 * @param prereq The prerequisite achivement. 
	 * @param special Determines if this achivement is special or not.
	 * @param event_handler The object which will allow the achivement to actually be achieved.
	 */
	public RunicMagicAchievement(String name, String desc, int x, int y, Item img, Achievement prereq, boolean special, Object event_handler)
	{
		super("achivement." + name,name,x,y,img,prereq);
		
		this.name = name;
		description = desc;
		
		EventHandler = event_handler;
		
		if(special)
			setSpecial();

		if(prereq == null)
			initIndependentStat();
		
		registerStat();
		return;
	}
	
	/**
	 * Creates a new achievement.
	 * @param name The name of the achievement.
	 * @param desc The description of the achivement.
	 * @param x The x position in the page of this achivement.
	 * @param y The y position in the page of this achivement.
	 * @param img What the achivement is displayed as.
	 * @param prereq The prerequisite achivement.
	 * @param special Determines if this achivement is special or not.
	 * @param event_handler The object which will allow the achivement to actually be achieved.
	 */
	public RunicMagicAchievement(String name, String desc, int x, int y, ItemStack img, Achievement prereq, boolean special, Object event_handler)
	{
		super("achivement." + name,name,x,y,img,prereq);
		
		this.name = name;
		description = desc;
		
		EventHandler = event_handler;
		
		if(special)
			setSpecial();

		if(prereq == null)
			initIndependentStat();
		
		registerStat();
		return;
	}
	
	/**
	 * The name of this achievement.
	 */
	public final String name;
	
	/**
	 * The description used for this achievement.
	 */
	public final String description;
	
	/**
	 * The object which will handle events for this achievement.
	 */
	public Object EventHandler;
}
