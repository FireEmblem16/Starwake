package rm.core.item.plant.harvest;

import net.minecraft.creativetab.CreativeTabs;
import rm.core.RunicMagic;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.RunicMagicItem;
import rm.core.item.plant.RunicMagicHarvest;
import rm.core.item.rune.BlankRune;

public class EthelWeed extends RunicMagicHarvest
{
	public EthelWeed()
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

	public static final String name = "Ethel Weed";
	public static final String texture = RunicMagic.MODID + ":" + name;
	public static final CreativeTabs tab = RunicMagicTab.instance;
	
	public static final EthelWeed instance = new EthelWeed();
}
