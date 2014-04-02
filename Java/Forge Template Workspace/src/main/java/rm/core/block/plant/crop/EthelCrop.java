package rm.core.block.plant.crop;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rm.core.RunicMagic;
import rm.core.block.plant.BonemealHandler;
import rm.core.block.plant.RunicMagicCrop;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.plant.harvest.EthelWeed;
import rm.core.item.plant.seed.EthelSeed;

public class EthelCrop extends RunicMagicCrop
{
	private EthelCrop()
	{
		super(0.0f,0.5f,0.0f,1.0f,1.0f,1.0f);
		return;
	}

	public String StageTexture(int stage)
	{return RunicMagic.MODID + ":" + name + " - " + stage;}

	public int MaxGrowthStage()
	{return 7;}

	public boolean WillGrow(World world, int x, int y, int z, Random rand, int stage)
	{
		int max = 3 + stage + world.getBlockLightValue(x,y,z);//-1 + stage + world.getBlockLightValue(x,y,z);
		
		// It might be able to happen since we might experiment in negative light
		if(max < 1)
			return true;
		
		if(rand.nextInt(max) != 0)
			return false;
		
		return true;
	}
	
	public void UpdateAfterGrowth(World world, int x, int y, int z, int stage)
	{
		if(stage >= MaxGrowthStage())
			if(world.getBlock(x,y + 1,z).equals(FaerieRingGrass.instance)) // Fey Ethel grows under faerie rings
				world.setBlock(x,y,z,FeyEthelCrop.instance,0,2);
			else
				world.setBlock(x,y,z,GrownEthelCrop.instance,0,2);
		
		return;
	}
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>(1);
		ret.add(new ItemStack(EthelSeed.instance,world.rand.nextInt(10) > 3 ? 1 : 0)); // Sometimes we just don't know what went wrong
		
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
	{return new BonemealHandler(this,20);}
	
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
	
	public static final String name = "Ethel Crop";
	public static final float emission = 0.0f;
	public static final SoundType sound = soundTypeGrass;
	public static final float hardness = 0.0f;
	public static final float resistance = 0.0f;
	
	public static final EthelCrop instance = new EthelCrop();
}
