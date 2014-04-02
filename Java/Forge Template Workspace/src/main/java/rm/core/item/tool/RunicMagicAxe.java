package rm.core.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public abstract class RunicMagicAxe extends ItemAxe implements RunicMagicTool
{
	public RunicMagicAxe(RunicMagicToolMaterial mat, Block head)
	{
		super(mat.GetMaterial());
		
		material = mat;
		bhead = head;
		ihead = null;
		
		setUnlocalizedName(name());
		setTextureName(texture());
		setCreativeTab(tab());
		
		return;
	}
	
	public RunicMagicAxe(RunicMagicToolMaterial mat, Item head)
	{
		super(mat.GetMaterial());
		
		material = mat;
		bhead = null;
		ihead = head;
		
		setUnlocalizedName(name());
		setTextureName(texture());
		setCreativeTab(tab());
		
		return;
	}
	
	public RunicMagicToolMaterial material()
	{return material;}

	public void RegisterShapedCrafting()
	{
		GameRegistry.addShapedRecipe(new ItemStack(this,1),"hh ","hs "," s ",'s',Items.stick,'h',bhead == null ? ihead : bhead);
		return;
	}
	
	public void RegisterShapelessCrafting()
	{return;}

	public void RegisterSmelting()
	{return;}

	public void RegisterMisc()
	{return;}
	
	public final RunicMagicToolMaterial material;
	
	private final Block bhead;
	private final Item ihead;
}
