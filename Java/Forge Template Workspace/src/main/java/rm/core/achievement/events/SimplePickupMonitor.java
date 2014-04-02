package rm.core.achievement.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import rm.core.achievement.RunicMagicAchievement;
import rm.core.achievement.RunicMagicAchievementPage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

/**
 * Mointers for a single item or a single block to be picked up and awards an achievement based when it occurs.
 */
public class SimplePickupMonitor
{
	/**
	 * Creates an event handler that moniters picked up blocks for when the given block is picked up and awards the given achievement.
	 * @param block The block that we want to pick up.
	 * @param achievement The name of the acievement we want to award.
	 */
	public SimplePickupMonitor(Block block, String achievement)
	{
		b_make = block;
		i_make = null;
		
		achieve = achievement;
		is_block = true;
		
		return;
	}
	
	/**
	 * Creates an event handler that moniters picked up items for when the given item is picked up and awards the given achievement.
	 * @param item The item that we want to pick up.
	 * @param achievement The name of the acievement we want to award.
	 */
	public SimplePickupMonitor(Item item, String achievement)
	{
		b_make = null;
		i_make = item;
		
		achieve = achievement;
		is_block = false;
		
		return;
	}
	
	/**
	 * Called when an item/block is picked up.
	 */
	@SubscribeEvent public void OnPickup(ItemPickupEvent e)
	{
		if(is_block)
		{
			if(Block.getBlockFromItem(e.pickedUp.getEntityItem().getItem()).equals(b_make))
			{
				RunicMagicAchievement a = RunicMagicAchievementPage.GetAchievement(achieve);
				
				if(a != null)
					e.player.addStat(a,1);
			}
		}
		else if(e.pickedUp.getEntityItem().getItem().equals(i_make))
		{
			RunicMagicAchievement a = RunicMagicAchievementPage.GetAchievement(achieve);
			
			if(a != null)
				e.player.addStat(a,1);
		}
		
		return;
	}
	
	private final Block b_make;
	private final Item i_make;
	private final String achieve;
	private final boolean is_block;
}
