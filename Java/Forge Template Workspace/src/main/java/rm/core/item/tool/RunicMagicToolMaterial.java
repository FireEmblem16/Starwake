package rm.core.item.tool;

import java.util.HashMap;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import rm.core.item.RunicMagicItem;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class RunicMagicToolMaterial
{
	public RunicMagicToolMaterial(String name, int level, int uses, float efficiency, float damage, int enchantability)
	{
		this.name = name;
		this.level = level;
		this.uses = uses;
		this.efficiency = efficiency;
		this.damage = damage;
		this.enchantability = enchantability;
		
		// We have to do this right away or else we can't construct tools the normal way
		material = EnumHelper.addToolMaterial(name,level,uses,efficiency,damage,enchantability);
		
		return;
	}
	
	/**
	 * The name of this tool material.
	 */
	public final String name;
	
	/**
	 * The harvest level of this tool material.
	 */
	public final int level;
	
	/**
	 * The maximum number of uses this tool material gives.
	 */
	public final int uses;
	
	/**
	 * The harvesting efficientcy of this tool material.
	 */
	public final float efficiency;
	
	/**
	 * The damage of this tool material.
	 */
	public final float damage;
	
	/**
	 * The enchantability of this tool material.
	 */
	public final int enchantability;
	
	/**
	 * The actual material of this tool material.
	 */
	public ToolMaterial GetMaterial()
	{return material;}
	
	private ToolMaterial material;
	
	/**
	 * Registers the given tool material into the set of runic magic tool materials.
	 * @return Returns the given tool material for cascading.
	 */
	public static RunicMagicToolMaterial RegisterToolMaterial(RunicMagicToolMaterial mat)
	{
		mats.put(mat.name,mat);
		return mat;
	}
	
	/**
	 * Gets the tool material with the given name.
	 * @return Returns the tool material with the given name or null if no such tool material exists. 
	 */
	public static RunicMagicToolMaterial GetToolMaterial(String name)
	{return mats.get(name);}
	
	private static HashMap<String,RunicMagicToolMaterial> mats = new HashMap<String,RunicMagicToolMaterial>(); 
}
