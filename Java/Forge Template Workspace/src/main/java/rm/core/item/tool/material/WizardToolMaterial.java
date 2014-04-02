package rm.core.item.tool.material;

import rm.core.item.tool.RunicMagicToolMaterial;

public class WizardToolMaterial extends RunicMagicToolMaterial
{
	public WizardToolMaterial()
	{
		super("Wizard",3,2048,5.0f,6.0f,100);
		return;
	}
	
	public static final WizardToolMaterial instance = new WizardToolMaterial();
}
