package rm.core.item.tool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import rm.core.item.RunicMagicItem;

public interface RunicMagicTool
{
	/**
	 * The name of this tool. 
	 */
	String name();
	
	/**
	 * The texture of this tool.
	 */
	String texture();
	
	/**
	 * The creative tab of this tool.
	 */
	CreativeTabs tab();
	
	/**
	 * The material of this tool.
	 */
	RunicMagicToolMaterial material();
	
	/**
	 * Registers all shaped recipies that can craft this tool.
	 */
	void RegisterShapedCrafting();
	
	/**
	 * Registers all shaped recipies that can craft this tool.
	 */
	void RegisterShapelessCrafting();
	
	/**
	 * Registers all recipies that can smelt this tool.
	 */
	void RegisterSmelting();
	
	/**
	 * Performs any extra registration needed by this tool.
	 */
	void RegisterMisc();
}
