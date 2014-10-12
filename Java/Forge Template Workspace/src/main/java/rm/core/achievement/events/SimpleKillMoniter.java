package rm.core.achievement.events;

import rm.core.achievement.RunicMagicAchievement;
import rm.core.achievement.RunicMagicAchievementPage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Watches for kills.
 */
public class SimpleKillMoniter
{
	public SimpleKillMoniter(Class Target, int count, String achieve, boolean global)
	{
		target = Target;
		num = count;
		achievement = achieve;
		g = global;
		
		return;
	}
	
	@SubscribeEvent public void OnKill(LivingDeathEvent e)
	{
		if(!target.equals(e.entity.getClass()))
			return;
		
		if(g)
		{
			if(--num != 0)
				return;
			
			RunicMagicAchievement a = RunicMagicAchievementPage.GetAchievement(achievement);
			
			if(a != null)
				Minecraft.getMinecraft().thePlayer.addStat(a,1);
		}
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if(!e.source.getEntity().equals(player))
			return;
		
		if(--num != 0)
			return;
		
		RunicMagicAchievement a = RunicMagicAchievementPage.GetAchievement(achievement);
		
		if(a != null)
			player.addStat(a,1);
		
		return;
	}
	
	private final Class target;
	private int num;
	private final String achievement;
	private final boolean g;
}
