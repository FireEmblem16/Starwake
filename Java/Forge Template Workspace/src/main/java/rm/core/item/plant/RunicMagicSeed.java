package rm.core.item.plant;

import java.util.HashMap;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.item.RunicMagicItem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.MinecraftForge;

public abstract class RunicMagicSeed extends ItemSeeds
{
	/**
	 * Creates the base implementation of a seed.
	 * @param freq The frequency of drops from tall grass relative to wheat at 10.
	 * @param rate The number of drops from one drop event.
	 * @param type A rather unimportant parameter but a hint for where this seed can be planted.
	 */
	protected RunicMagicSeed(int freq, int rate, EnumPlantType type)
	{
		super(null,null);
		
		setUnlocalizedName(name());
		setTextureName(texture());
		setCreativeTab(tab());
		
		frequency = freq;
		spawnrate = rate;
		this.type = type;
		
		return;
	}
	
	/**
	 * Gets the type of plant this is.
	 * Rather unimportant as it has little to do with how the plant actually works.
	 */
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{return type;}
	
	/**
	 * Gets the block that this seed grows into.
	 */
	public abstract Block getPlant(IBlockAccess world, int x, int y, int z);
	
	/**
	 * Gets the initial value plant meta data is set to.
	 */
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{return 0;}
	
	/**
	 * Attempts to plant this seed.
	 * @param use_from The item stack being used to plant with.
	 * @param player The player planting.
	 * @param world The world.
	 * @param x The x position of the block we are trying to plant on.
	 * @param y The y position of the block we are trying to plant on.
	 * @param z The z position of the block we are trying to plant on.
	 * @param side The side we are trying to plant on.
	 * @param hx The x hit location of the side we are trying to plant on of the block we are trying to plant on.
	 * @param hy The y hit location of the side we are trying to plant on of the block we are trying to plant on.
	 * @param hz The z hit location of the side we are trying to plant on of the block we are trying to plant on.
	 * @return Returns true if this seed is planted and false otherwise.
	 */
	public boolean onItemUse(ItemStack use_from, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz)
	{
		if(!Valid(use_from,player,world,x,y,z,side,hx,hy,hz))
			return false;
		
		if(!world.setBlock(x,y - 1,z,getPlant(world,x,y,z),0,2))
			return false;
		
		use_from.splitStack(1);
		return true;
	}
	
	/**
	 * Determines if this seed can be placed as given.
	 * @param use_from The item stack being used to plant with.
	 * @param player The player planting.
	 * @param world The world.
	 * @param x The x position of the block we are trying to plant on.
	 * @param y The y position of the block we are trying to plant on.
	 * @param z The z position of the block we are trying to plant on.
	 * @param side The side we are trying to plant on.
	 * @param hx The x hit location of the side we are trying to plant on of the block we are trying to plant on.
	 * @param hy The y hit location of the side we are trying to plant on of the block we are trying to plant on.
	 * @param hz The z hit location of the side we are trying to plant on of the block we are trying to plant on.
	 * @return Returns true if this seed can be planted and false otherwise.
	 */
	protected abstract boolean Valid(ItemStack use_from, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz);
	
	/**
	 * The name of this seed. 
	 */
	public abstract String name();
	
	/**
	 * The texture of this seed.
	 */
	public abstract String texture();
	
	/**
	 * The creative tab of this seed.
	 */
	public abstract CreativeTabs tab();
	
	/**
	 * Registers all shaped recipies that can craft this seed.
	 */
	protected abstract void RegisterShapedCrafting();
	
	/**
	 * Registers all shaped recipies that can craft this seed.
	 */
	protected abstract void RegisterShapelessCrafting();
	
	/**
	 * Registers all recipies that can smelt this seed.
	 */
	protected abstract void RegisterSmelting();
	
	/**
	 * Performs any extra registration needed by this seed.
	 */
	protected abstract void RegisterMisc();
	
	/**
	 * How often this seed is dropped from tall grass (regular seeds have a value of 10).
	 */
	public final int frequency;
	
	/**
	 * How many seeds are dropped from one tall grass.
	 */
	public final int spawnrate;
	
	/**
	 * The type of plant this seed produces.
	 */
	public final EnumPlantType type;
	
	/**
	 * Registers Runic Magic seeds with Minecraft.
	 */
	public static void RegisterSeeds()
	{
		for(RunicMagicSeed seed : seeds.values())
		{
			GameRegistry.registerItem(seed,seed.name());
			MinecraftForge.addGrassSeed(new ItemStack(seed,seed.spawnrate),seed.frequency);
			
			seed.RegisterShapedCrafting();
			seed.RegisterShapelessCrafting();
			seed.RegisterSmelting();
			seed.RegisterMisc();
		}
		
		return;
	}
	
	/**
	 * Registers the given seed into the set of runic magic seeds.
	 * @return Returns the given seed for cascading.
	 */
	public static RunicMagicSeed RegisterSeed(RunicMagicSeed seed)
	{
		seeds.put(seed.name(),seed);
		return seed;
	}
	
	/**
	 * Gets the seed with the given name.
	 * @return Returns the seed with the given name or null if no such seed exists. 
	 */
	public static RunicMagicSeed GetSeed(String name)
	{return seeds.get(name);}
	
	private static HashMap<String,RunicMagicSeed> seeds = new HashMap<String,RunicMagicSeed>();
}
