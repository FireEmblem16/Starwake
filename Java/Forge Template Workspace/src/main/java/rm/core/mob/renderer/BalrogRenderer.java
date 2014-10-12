package rm.core.mob.renderer;

import rm.core.RunicMagic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class BalrogRenderer extends RenderLiving
{
	public BalrogRenderer(ModelBase model, float par2)
	{
		super(model,par2);
		return;
	}

	protected ResourceLocation getEntityTexture(Entity e)
	{return texture;}
	
	private static final ResourceLocation texture = new ResourceLocation(RunicMagic.MODID + ":" + "textures/mobs/Balrog");
}
