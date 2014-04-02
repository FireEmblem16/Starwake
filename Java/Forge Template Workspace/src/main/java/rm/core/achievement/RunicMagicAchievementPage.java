package rm.core.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import cpw.mods.fml.common.FMLCommonHandler;
import rm.core.RunicMagic;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public class RunicMagicAchievementPage extends AchievementPage
{
	private RunicMagicAchievementPage(Achievement... achievements)
	{
		super(RunicMagic.MODNAME,achievements);
		return;
	}
	
	/**
	 * Adds the provided achievement to the achievement page.
	 */
	public static void RegisterAchievement(RunicMagicAchievement achievement)
	{
		achievements.put(achievement.name,achievement);
		return;
	}
	
	/**
	 * Gets the achivement with the given name.
	 * @param name The name of the achievement.
	 * @return Returns the achievement with the provided name or null if no such achievement exists.
	 */
	public static RunicMagicAchievement GetAchievement(String name)
	{return achievements.get(name);}
	
	/**
	 * Call when all achivements are loaded and the page is ready to be added to Minecraft.
	 */
	public static void RegisterPage()
	{
		// If we've already done this don't do it again
		if(instance != null)
			return;
		
		// Create and register this page
		instance = new RunicMagicAchievementPage(achievements.values().toArray(new RunicMagicAchievement[achievements.size()]));
		AchievementPage.registerAchievementPage(instance);
		
		// Register all of the event handlers
		for(RunicMagicAchievement achievement : achievements.values())
			FMLCommonHandler.instance().bus().register(achievement.EventHandler);
		
		return;
	}
	
	private static HashMap<String,RunicMagicAchievement> achievements = new HashMap<String,RunicMagicAchievement>();
	
	/**
	 * The instance of this object.
	 * Not constructed until all submods are loaded.
	 * It would be ill-advised to change this but it can not be made final due to its contruction time. 
	 */
	public static RunicMagicAchievementPage instance = null;
}
