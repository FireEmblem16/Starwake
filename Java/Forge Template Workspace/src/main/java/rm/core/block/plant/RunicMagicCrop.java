package rm.core.block.plant;

import java.util.ArrayList;
import java.util.Random;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import rm.core.block.RunicMagicBlock;

public abstract class RunicMagicCrop extends RunicMagicBlock
{
	protected RunicMagicCrop(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		super(Material.plants);
		setBlockBounds(minX,minY,minZ,maxX,maxY,maxZ);
		
		setTickRandomly(true);
		return;
	}
	
	public Material material()
	{return Material.plants;}

	public boolean IsMultiTextured()
	{return false;}

	/**
	 * The initial texture for this crop at its lowest growth level.
	 */
	public String texture()
	{return null;}
	
	/**
	 * Gets the texture of this crop at the given growth level.
	 */
	public abstract String StageTexture(int stage);
	
	@SideOnly(Side.CLIENT) public void registerBlockIcons(IIconRegister reg)
	{
		for(int i = 0;i <= MaxGrowthStage();i++)
			icons[i] = reg.registerIcon(StageTexture(i));
		
		return;
	}
	
	@SideOnly(Side.CLIENT) public IIcon getIcon(int side, int meta)
	{return icons[meta];}
	
	/**
	 * Gets the current level of growth of the block at the given position.
	 * Note that we assume that we are checking a revelvant block as no error will occur even if we are not.
	 */
	public int GrowthStage(IBlockAccess world, int x, int y, int z)
	{return world.getBlockMetadata(x,y,z);}
	
	/**
	 * Note that we assume that we are changing a revelvant block.
	 */
	private void ChangeStage(World world, int x, int y, int z, int meta)
	{
		world.setBlockMetadataWithNotify(x,y,z,meta,3);
		return;
	}
	
	/**
	 * Returns the number of growth stages (minus one for stage zero).
	 */
	public abstract int MaxGrowthStage();
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{return null;}
	
	public int getRenderType()
	{return 6;} // Apparently a magic number
	
	public boolean isOpaqueCube()
	{return false;}
	
	/**
	 * Updates this crop.
	 * @param world The world.
	 * @param x The x position of the crop in the world.
	 * @param y The y position of the crop in the world.
	 * @param z The z position of the crop in the world.
	 * @param rand The random function for the game.
	 */
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		int stage = GrowthStage(world,x,y,z);
		
		if(stage == MaxGrowthStage())
			return;
		
		if(!WillGrow(world,x,y,z,rand,stage))
			return;
		
		ChangeStage(world,x,y,z,++stage);
		UpdateAfterGrowth(world,x,y,z,stage);
		
		return;
	}
	
	/**
	 * Checks if this crop will grow on this tick.
	 * @param world The world.
	 * @param x The x position of the crop in the world.
	 * @param y The y position of the crop in the world.
	 * @param z The z position of the crop in the world.
	 * @param rand The random function for the game.
	 * @param stage The stage of growth the crop is in.
	 * @return Returns true if the plant should grow this tick and false otherwise. 
	 */
	public abstract boolean WillGrow(World world, int x, int y, int z, Random rand, int stage);
	
	/**
	 * Performs any extra updates after a growth period for the crop and related blocks.
	 */
	public abstract void UpdateAfterGrowth(World world, int x, int y, int z, int stage);
	
	public abstract ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune);
	
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor)
	{
		if(!canBlockStay(world,x,y,z))
		{
			for(ItemStack stack : getDrops(world,x,y,z,world.getBlockMetadata(x,y,z),0))
				dropBlockAsItem(world,x,y,z,stack);
			
			world.setBlockToAir(x,y,z);
		}
		
		return;
	}
	
	public abstract boolean canBlockStay(World world, int x, int y, int z);
	
	protected void RegisterShapedCrafting()
	{return;}

	protected void RegisterShapelessCrafting()
	{return;}

	protected void RegisterSmelting()
	{return;}

	protected void RegisterMisc()
	{
		BonemealHandler event = GetBonemealHandler();
		
		if(event != null)
			MinecraftForge.EVENT_BUS.register(event); // Apparently we use this event bus for bonemeal events
		
		return;
	}
	
	/**
	 * Gets a bonemeal handler for this crop.
	 */
	protected abstract BonemealHandler GetBonemealHandler();
	
	private final IIcon[] icons = new IIcon[MaxGrowthStage() + 1];
}
