package rm.core.block;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class RunicMagicBlock extends Block
{
	protected RunicMagicBlock(Material mat)
	{
		super(mat);
		
		setCreativeTab(tab());
		setBlockName(name());
		setLightLevel(LightEmission());
		setStepSound(StepSound());
		setHardness(hardness());
		setResistance(resistance());
		
		if(!IsMultiTextured())
			setBlockTextureName(texture());
		
		return;
	}
	
	/**
	 * The name of this block. 
	 */
	public abstract String name();
	
	/**
	 * The material of this block.
	 */
	public abstract Material material();
	
	/**
	 * The creative tab of this block.
	 */
	public abstract CreativeTabs tab();
	
	/**
	 * The amount of light this block emits.
	 */
	public abstract float LightEmission();
	
	/**
	 * The stepsound of this block.
	 */
	public abstract SoundType StepSound();
	
	/**
	 * The hardness of this block.
	 */
	public abstract float hardness();
	
	/**
	 * The resistance of this block (to explosions).
	 */
	public abstract float resistance();
	
	/**
	 * If true then this is a multi texture block.
	 */
	public abstract boolean IsMultiTextured();
	
	/**
	 * The texture of this block if this is a single texture block and null otherwise.
	 */
	public abstract String texture();
	
	/**
	 * Registers all shaped recipies that can craft this block.
	 */
	protected abstract void RegisterShapedCrafting();
	
	/**
	 * Registers all shaped recipies that can craft this block.
	 */
	protected abstract void RegisterShapelessCrafting();
	
	/**
	 * Registers all recipies that can smelt this block.
	 */
	protected abstract void RegisterSmelting();
	
	/**
	 * Performs any extra registration needed by this block.
	 */
	protected abstract void RegisterMisc();
	
	/**
	 * Registers Runic Magic blocks with Minecraft.
	 */
	public static void RegisterBlocks()
	{
		for(RunicMagicBlock block : blocks.values())
		{
			GameRegistry.registerBlock(block,block.name());
			
			block.RegisterShapedCrafting();
			block.RegisterShapelessCrafting();
			block.RegisterSmelting();
			block.RegisterMisc();
		}
		
		return;
	}
	
	/**
	 * Registers the given block into the set of runic magic blocks.
	 * @return Returns the given block for cascading.
	 */
	public static RunicMagicBlock RegisterBlock(RunicMagicBlock block)
	{
		blocks.put(block.name(),block);
		return block;
	}
	
	/**
	 * Gets the block with the given name.
	 * @return Returns the block with the given name or null if no such block exists. 
	 */
	public static RunicMagicBlock GetBlock(String name)
	{return blocks.get(name);}
	
	private static HashMap<String,RunicMagicBlock> blocks = new HashMap<String,RunicMagicBlock>();
}
