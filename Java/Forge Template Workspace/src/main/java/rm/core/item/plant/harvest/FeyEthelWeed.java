package rm.core.item.plant.harvest;

import net.minecraft.creativetab.CreativeTabs;
import rm.core.RunicMagic;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.plant.RunicMagicHarvest;

public class FeyEthelWeed extends RunicMagicHarvest
{
	public FeyEthelWeed()
	{
		super();
		return;
	}
	
	public String name()
	{return name;}

	public String texture()
	{return texture;}

	public CreativeTabs tab()
	{return tab;}

	protected void RegisterShapedCrafting()
	{return;}

	protected void RegisterShapelessCrafting()
	{return;}

	protected void RegisterSmelting()
	{return;}

	protected void RegisterMisc()
	{return;}

	public static final String name = "Fey Ethel Weed";
	public static final String texture = RunicMagic.MODID + ":" + name;
	public static final CreativeTabs tab = RunicMagicTab.instance;
	
	public static final FeyEthelWeed instance = new FeyEthelWeed();
}
