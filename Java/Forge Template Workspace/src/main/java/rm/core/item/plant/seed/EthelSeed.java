package rm.core.item.plant.seed;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import rm.core.RunicMagic;
import rm.core.SIDE;
import rm.core.block.plant.crop.EthelCrop;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.creative.tab.RunicMagicTab;
import rm.core.item.plant.RunicMagicSeed;
import cpw.mods.fml.common.registry.GameRegistry;

public class EthelSeed extends RunicMagicSeed
{
	private EthelSeed()
	{
		super(1,1,EnumPlantType.Plains);
		return;
	}
	
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{return EthelCrop.instance;}
	
	protected boolean Valid(ItemStack use_from, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz)
	{
		if(side != SIDE.BOTTOM)
			return false;
		
		if(y == 0)
			return false;
		
		if(!world.isAirBlock(x,y - 1,z))
			return false;
		
		Block b = world.getBlock(x,y,z);
		
		if(!(b.equals(Blocks.dirt) || b.equals(Blocks.grass) || b.equals(FaerieRingGrass.instance)))
			return false;
		
		return true;
	}
	
	public String name()
	{return name;}

	public String texture()
	{return texture;}

	public CreativeTabs tab()
	{return tab;}

	public void RegisterShapedCrafting()
	{return;}

	public void RegisterShapelessCrafting()
	{return;}

	public void RegisterSmelting()
	{return;}

	public void RegisterMisc()
	{return;}
	
	public static final String name = "Ethel Seed";
	public static final String texture = RunicMagic.MODID + ":" + name;
	public static final CreativeTabs tab = RunicMagicTab.instance;
	
	public static final EthelSeed instance = new EthelSeed();
}
