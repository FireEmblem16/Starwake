package rm.core.block.portal;

import java.util.Random;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import rm.core.RunicMagic;
import rm.core.block.RunicMagicMultiTextureBlock;
import rm.core.creative.tab.RunicMagicTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureClock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A block that is part of a faerie ring.
 */
public class FaerieRingGrass extends RunicMagicMultiTextureBlock
{
	private FaerieRingGrass()
	{
		super(Material.grass);
		
		// Add random ticking and once in a while this block grows a fairy mushroom
		
		return;
	}
	
	public String name()
	{return name;}
	
	public Material material()
	{return material;}
	
	public CreativeTabs tab()
	{return tab;}
	
	public float LightEmission()
	{return emission;}
	
	public SoundType StepSound()
	{return stepsound;}
	
	public float hardness()
	{return hardness;}
	
	public float resistance()
	{return resistance;}
	
	public IIcon[] textures()
	{return textures;}
	
	@SideOnly(Side.CLIENT) public void registerBlockIcons(IIconRegister reg)
	{
		// DO NOT check if textures is null because then this fails spectacuarly
		
		toptexture = reg.registerIcon(RunicMagic.MODID + ":" + name + " - top");
		bottomtexture = reg.registerIcon(RunicMagic.MODID + ":" + name + " - bottom");
		sidetexture = reg.registerIcon(RunicMagic.MODID + ":" + name + " - side");
		
		textures = new IIcon[] {bottomtexture,toptexture,sidetexture,sidetexture,sidetexture,sidetexture};
		return;
	}
	
	protected void RegisterShapedCrafting()
	{
		// A temporary recipe
		GameRegistry.addShapedRecipe(new ItemStack(instance,1),"ddd","dDd","ddd",'d',Items.diamond,'D',Blocks.grass);
		return;
	}
	
	protected void RegisterShapelessCrafting()
	{return;}
	
	protected void RegisterSmelting()
	{return;}
	
	protected void RegisterMisc()
	{return;}
	
	/**
	 * The name of this block. 
	 */
	public static final String name = "Faerie Ring Grass";
	
	/**
	 * The material of this block.
	 */
	public static final Material material = Material.grass;
	
	/**
	 * The creative tab of this block.
	 */
	public static final CreativeTabs tab = RunicMagicTab.instance;
	
	/**
	 * The amount of light this block emits.
	 */
	public static final float emission = 0.56f;
	
	/**
	 * The stepsound of this block.
	 */
	public static final SoundType stepsound = soundTypeGrass;
	
	/**
	 * The hardness of this block.
	 */
	public static final float hardness = 1.0f;
	
	/**
	 * The resistance of this block (to explosions).
	 */
	public static final float resistance = 1.0f;
	
	/**
	 * The textures used for this block (bottom, top, north, south, west, east) if this is a multitexture block and null otherwise.
	 * Not initialized until the registerBlockIcons function is called on the instance of this block.
	 * Modifying this is ill-advised.
	 */
	public static IIcon[] textures = null;
	
	/**
	 * The top texture for this block.
	 */
	@SideOnly(Side.CLIENT) public IIcon toptexture;
	
	/**
	 * The bottom texture for this block.
	 */
	@SideOnly(Side.CLIENT) public IIcon bottomtexture;
	
	/**
	 * The side texture for this block.
	 */
	@SideOnly(Side.CLIENT) public IIcon sidetexture;
	
	/**
	 * The instance of this block.
	 */
	public static final FaerieRingGrass instance = new FaerieRingGrass();
}
