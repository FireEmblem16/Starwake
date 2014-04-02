package rm.core.block.plant;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealHandler
{
	/**
	 * Creates a bonemeal handler for growing crops.
	 * @param rmc The crop we want to work with.
	 * @param tries The number of times a single bone meal applies to the crop.
	 */
	public BonemealHandler(RunicMagicCrop rmc, int tries)
	{
		if(tries < 1)
			tries = 1;
		
		crop = rmc;
		t = tries;
		
		return;
	}
	
	@SubscribeEvent public void OnBonemeal(BonemealEvent e)
	{
		if(!crop.equals(e.block))
			return;
		
		RunicMagicCrop block = (RunicMagicCrop)e.block;
		int meta = e.world.getBlockMetadata(e.x,e.y,e.z);
		
		for(int i = 0;i < t && meta < block.MaxGrowthStage();i++)
			if(block.WillGrow(e.world,e.x,e.y,e.z,e.world.rand,meta))
			{
				e.world.setBlockMetadataWithNotify(e.x,e.y,e.z,++meta,3);
				block.UpdateAfterGrowth(e.world,e.x,e.y,e.z,meta);
			}
		
		return;
	}
	
	private final RunicMagicCrop crop;
	private final int t;
}
