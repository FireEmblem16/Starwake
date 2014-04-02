package rm.core.item.tool.material;

import rm.core.item.tool.RunicMagicToolMaterial;

public class FaerieToolMaterial extends RunicMagicToolMaterial
{
	public FaerieToolMaterial()
	{
		super("Faerie",1,512,2.0f,1.5f,30);
		return;
	}
	
	public static final FaerieToolMaterial instance = new FaerieToolMaterial();
}
