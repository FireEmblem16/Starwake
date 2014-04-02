package rm.core.item.tool.spade;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import rm.core.RunicMagic;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.tool.RunicMagicSpade;
import rm.core.item.tool.material.FaerieToolMaterial;

public class FaerieSpade extends RunicMagicSpade
{
	private FaerieSpade()
	{
		super(FaerieToolMaterial.instance,Blocks.pumpkin);
		return;
	}

	public String name()
	{return name;}

	public String texture()
	{return texture;}

	public CreativeTabs tab()
	{return tab;}

	public static final String name = "Faerie Shovel";
	public static final String texture = RunicMagic.MODID + ":" + name;
	public static final CreativeTabs tab = RunicMagicTab.instance;
	
	public static final FaerieSpade instance = new FaerieSpade();
}
