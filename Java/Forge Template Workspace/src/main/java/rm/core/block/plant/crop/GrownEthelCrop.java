package rm.core.block.plant.crop;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rm.core.RunicMagic;
import rm.core.block.plant.BonemealHandler;
import rm.core.block.plant.RunicMagicCrop;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.plant.harvest.EthelWeed;
import rm.core.item.plant.seed.EthelSeed;

public class GrownEthelCrop extends RunicMagicCrop
{
	private GrownEthelCrop()
	{
		super(0.0f,0.0f,0.0f,1.0f,1.0f,1.0f);
		return;
	}

	public String StageTexture(int stage)
	{return RunicMagic.MODID + ":" + name;}

	public int MaxGrowthStage()
	{return 0;}
	
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		super.updateTick(world,x,y,z,rand);
		
		if(rand.nextInt(100) < 17)
			for(int i = -1;i < 2;i++)
				for(int j = -1;j < 2;j++)
					for(int k = -1;k < 2;k++)
					{
						int nx = x + i;
						int ny = y + j;
						int nz = z + k;
						
						// This will catch the (0,0,0) case
						if(!world.isAirBlock(nx,ny,nz))
							continue;
						
						Block b = world.getBlock(nx,ny + 1,nz);
						
						if(b.equals(Blocks.grass) || b.equals(Blocks.dirt) || b.equals(FaerieRingGrass.instance))
						{
							world.setBlock(nx,ny,nz,EthelCrop.instance,0,2);
							return;
						}
					}
			
		return;
	}

	public boolean WillGrow(World world, int x, int y, int z, Random rand, int stage)
	{return false;}
	
	public void UpdateAfterGrowth(World world, int x, int y, int z, int stage)
	{return;}
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		int payout = 1 + world.rand.nextInt(2 + fortune); // nextInt is excusive on the upper bound
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1 + payout);
		
		ret.add(new ItemStack(EthelWeed.instance));
		
		for(int i = 0;i < payout;i++)
			ret.add(new ItemStack(EthelSeed.instance,1)); // We do this to get the multiple drops look
		
		return ret;
	}
	
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		Block b = world.getBlock(x,y + 1,z);
		
		if(!(b.equals(Blocks.dirt) || b.equals(Blocks.grass) || b.equals(FaerieRingGrass.instance)))
			return false;
		
		return true;
	}

	protected BonemealHandler GetBonemealHandler()
	{return null;}
	
	public String name()
	{return name;}

	public CreativeTabs tab()
	{return RunicMagicTab.instance;}

	public float LightEmission()
	{return emission;}

	public SoundType StepSound()
	{return sound;}

	public float hardness()
	{return hardness;}

	public float resistance()
	{return resistance;}
	
	public static final String name = "Grown Ethel Crop";
	public static final float emission = 0.8f;
	public static final SoundType sound = soundTypeGrass;
	public static final float hardness = 0.0f;
	public static final float resistance = 0.0f;
	
	public static final GrownEthelCrop instance = new GrownEthelCrop();
}
