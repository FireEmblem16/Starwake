package rm.core.item.plant;

import java.util.HashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import rm.core.item.RunicMagicItem;

public abstract class RunicMagicHarvest extends RunicMagicItem
{
	protected RunicMagicHarvest()
	{
		super();
		return;
	}
	
	/**
	 * Registers the given harvest into the set of runic magic harvests.
	 * Note that this is a shell to registering with RunicMagicItem.
	 * @return Returns the given harvest for cascading.
	 */
	public static RunicMagicHarvest RegisterHarvest(RunicMagicHarvest harvest)
	{return (RunicMagicHarvest)RunicMagicItem.RegisterItem(harvest);}
	
	/**
	 * Gets the harvest with the given name.
	 * Note that this is a shell to registering with RunicMagicItem.
	 * @return Returns the harvest with the given name or null if no such harvest exists. 
	 */
	public static RunicMagicHarvest GetHarvest(String name)
	{return (RunicMagicHarvest)RunicMagicItem.GetItem(name);}
}
