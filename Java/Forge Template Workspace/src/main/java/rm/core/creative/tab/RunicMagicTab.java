package rm.core.creative.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class RunicMagicTab extends CreativeTabs
{
	protected RunicMagicTab(String label)
	{
		super(label);
		return;
	}
	
	protected RunicMagicTab(int val, String label)
	{
		super(val,label);
		return;
	}
	
	public Item getTabIconItem()
	{return Items.enchanted_book;}
	
	/**
	 * The instance of this tab.
	 */
	public static final RunicMagicTab instance = new RunicMagicTab("Runic Magic");
}
