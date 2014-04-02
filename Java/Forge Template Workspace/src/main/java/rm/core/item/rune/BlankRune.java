package rm.core.item.rune;

import cpw.mods.fml.common.registry.GameRegistry;
import rm.core.RunicMagic;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.RunicMagicItem;
import rm.core.item.RunicMagicRune;
import rm.core.item.plant.harvest.EthelWeed;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * The basic rune with no symbol etched onto it yet.
 */
public class BlankRune extends RunicMagicRune
{
	private BlankRune()
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
		GameRegistry.addShapedRecipe(new ItemStack(instance,2)," ss","ses","ss ",'s',Blocks.stone,'e',EthelWeed.instance);
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
	public static final String name = "Blank Rune";
	
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
	public static final BlankRune instance = new BlankRune();
}
