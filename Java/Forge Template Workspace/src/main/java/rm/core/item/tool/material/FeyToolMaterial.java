package rm.core.item.tool.material;

import rm.core.item.tool.RunicMagicToolMaterial;

public class FeyToolMaterial extends RunicMagicToolMaterial
{
	public FeyToolMaterial()
	{
		super("Fey",2,1024,3.5f,3.0f,50);
		return;
	}
	
	public static final FeyToolMaterial instance = new FeyToolMaterial();
}
