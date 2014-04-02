package rm.core.item.rune;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import rm.core.RunicMagic;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.RunicMagicRune;
import rm.core.item.plant.harvest.FeyEthelWeed;
import cpw.mods.fml.common.registry.GameRegistry;

public class FeyBlankRune extends RunicMagicRune
{
	private FeyBlankRune()
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
	{
		// The stone should be changed to fey stone
		GameRegistry.addShapedRecipe(new ItemStack(instance,2)," ss","ses","ss ",'s',Blocks.stone,'e',FeyEthelWeed.instance);
		return;
	}

	protected void RegisterShapelessCrafting()
	{return;}

	protected void RegisterSmelting()
	{return;}
	
	protected void RegisterMisc()
	{return;}
	
	/**
	 * The name of this item. 
	 */
	public static final String name = "Fey Blank Rune";
	
	/**
	 * The texture of this item.
	 */
	public static final String texture = RunicMagic.MODID + ":" + name;
	
	/**
	 * The creative tab of this item.
	 */
	public static final CreativeTabs tab = RunicMagicTab.instance;
	
	/**
	 * The instance of this item.
	 */
	public static final FeyBlankRune instance = new FeyBlankRune();
}
