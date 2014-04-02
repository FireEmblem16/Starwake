package rm.core.item;

import java.util.HashMap;
import rm.core.item.tool.RunicMagicTool;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class RunicMagicItem extends Item
{
	protected RunicMagicItem()
	{
		super();
		
		setUnlocalizedName(name());
		setTextureName(texture());
		setCreativeTab(tab());
		
		return;
	}
	
	/**
	 * The name of this item. 
	 */
	public abstract String name();
	
	/**
	 * The texture of this item.
	 */
	public abstract String texture();
	
	/**
	 * The creative tab of this item.
	 */
	public abstract CreativeTabs tab();
	
	/**
	 * Registers all shaped recipies that can craft this item.
	 */
	protected abstract void RegisterShapedCrafting();
	
	/**
	 * Registers all shaped recipies that can craft this item.
	 */
	protected abstract void RegisterShapelessCrafting();
	
	/**
	 * Registers all recipies that can smelt this item.
	 */
	protected abstract void RegisterSmelting();
	
	/**
	 * Performs any extra registration needed by this item.
	 */
	protected abstract void RegisterMisc();
	
	/**
	 * Registers Runic Magic items with Minecraft.
	 */
	public static void RegisterItems()
	{
		for(RunicMagicItem item : items.values())
		{
			GameRegistry.registerItem(item,item.name());
			
			item.RegisterShapedCrafting();
			item.RegisterShapelessCrafting();
			item.RegisterSmelting();
			item.RegisterMisc();
		}
		
		return;
	}
	
	/**
	 * Registers Runic Magic tools with Minecraft.
	 */
	public static void RegisterTools()
	{
		for(RunicMagicTool tool : tools.values())
		{
			GameRegistry.registerItem((Item)tool,tool.name());
			
			tool.RegisterShapedCrafting();
			tool.RegisterShapelessCrafting();
			tool.RegisterSmelting();
			tool.RegisterMisc();
		}
		
		return;
	}
	
	/**
	 * Registers the given item into the set of runic magic items.
	 * @return Returns the given item for cascading.
	 */
	public static RunicMagicItem RegisterItem(RunicMagicItem item)
	{
		items.put(item.name(),item);
		return item;
	}
	
	/**
	 * Registers the given tool into the set of runic magic items.
	 * @return Returns the given tool for cascading or null if the tool could not be added because it is not an Item.
	 */
	public static RunicMagicTool RegisterTool(RunicMagicTool tool)
	{
		if(!(tool instanceof Item))
			return null;
		
		tools.put(tool.name(),tool);
		return tool;
	}
	
	/**
	 * Gets the item with the given name.
	 * @return Returns the item with the given name or null if no such item exists. 
	 */
	public static RunicMagicItem GetItem(String name)
	{return items.get(name);}
	
	private static HashMap<String,RunicMagicItem> items = new HashMap<String,RunicMagicItem>();
	private static HashMap<String,RunicMagicTool> tools = new HashMap<String,RunicMagicTool>();
}
