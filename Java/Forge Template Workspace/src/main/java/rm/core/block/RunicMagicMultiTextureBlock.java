package rm.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public abstract class RunicMagicMultiTextureBlock extends RunicMagicBlock
{
	protected RunicMagicMultiTextureBlock(Material mat)
	{
		super(mat);
		return;
	}
	
	public String texture()
	{return null;}
	
	public boolean IsMultiTextured()
	{return true;}
	
	/**
	 * The textures used for this block (bottom, top, north, south, west, east).
	 */
	public abstract IIcon[] textures();
	
	/**
	 * Loads the textures of this block.
	 */
	@SideOnly(Side.CLIENT) public abstract void registerBlockIcons(IIconRegister reg);
	
	/**
	 * Gets the texture of this block associated with a specific side and meta data.
	 */
	@SideOnly(Side.CLIENT) public IIcon getIcon(int side, int meta)
	{return textures()[side];}
}
