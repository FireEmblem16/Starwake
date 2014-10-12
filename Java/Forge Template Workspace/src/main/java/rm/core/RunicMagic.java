package rm.core;

import java.io.*;
import java.util.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.MinecraftForge;
import rm.core.achievement.RunicMagicAchievementPage;
import rm.core.block.RunicMagicBlock;
import rm.core.item.RunicMagicItem;
import rm.core.item.plant.RunicMagicSeed;
import rm.core.item.rune.BlankRune;
import rm.core.item.tool.RunicMagicToolMaterial;
import rm.core.proxy.CommonProxy;
import rm.core.worldgen.RunicMagicWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * The loader and logic runner for Runic Magic.
 */
@Mod(modid = RunicMagic.MODID,name = RunicMagic.MODNAME,version = RunicMagic.VERSION) public class RunicMagic
{
	/**
     * Handles preinitialization logic for Runic Magic such as recipe and block registration.
     */
	@EventHandler public void PreInit(FMLPreInitializationEvent e)
	{
		LoadConfigurations(e.getSuggestedConfigurationFile());
		
		RunicMagicBlock.RegisterBlocks();
		RunicMagicItem.RegisterItems();
		RunicMagicItem.RegisterTools();
		RunicMagicSeed.RegisterSeeds();
		RunicMagicWorldGenerator.RegisterGenerators();
		
		return;
	}
	
	/**
     * Handles initialization logic for Runic Magic.
     */
    @EventHandler public void Init(FMLInitializationEvent e)
    {
        proxy.registerRenderers();
        return;
    }
    
    /**
     * Handles postinitialization logic for Runic Magic.
     */
    @EventHandler public void PostInit(FMLPostInitializationEvent e)
    {
        
        return;
    }
    
    /**
     * Loads the submods of Runic Magic into Minecraft.
     * @return Returns a list of submods that were loaded or null if the config file is missing or if the core submod could not be loaded.
     */
    private ArrayList<String> LoadConfigurations(File config)
    {
    	Scanner fin;
    	
    	try
		{fin = new Scanner(config);}
		catch(FileNotFoundException e)
		{return null;}
    	
    	ArrayList<String> loaded = new ArrayList<String>();
    	ArrayList<String> needs_loading = new ArrayList<String>();
    	
    	while(fin.hasNext())
    	{
    		// Check which submod we want to load
    		String mod = fin.next();
    		
    		if(!fin.hasNext())
    			continue;
    		
    		fin.next(); // Skip =
    		
    		// Check if we should load this submod
    		if(!fin.hasNextBoolean() || !fin.nextBoolean())
    			if(mod.equals("Core") && !loaded.contains("Core")) // If we're not loading the core then we can't load anything
    			{
    				fin.close();
    				return null;
    			}
    			else
    				continue;
    		
    		Loader l = StringToLoader(mod);
    		
    		if(l == null)
    			continue; // We found a fake submod
    		
    		// If we've already loaded this submod don't do it again
    		if(loaded.contains(mod))
    			continue;
    		
    		if(!l.CheckDependencies(loaded))
    		{
    			// Prevent duplicate entries in this list
    			if(!needs_loading.contains(mod))
    				needs_loading.add(mod);
    			
    			continue;
    		}
    		
    		// Perform loading logic
    		File lang = new File("bin/assets/" + MODID + "/lang/en_US.lang"); // This is the language file for development settings 
    		
    		if(!lang.exists())
    			; // This is the language file for release settings
    		else // If we are working with development settings then make sure we keep the source lang file up to date for convenience
    			l.CheckLangEntries(new File("src/main/resources/assets/" + MODID + "/lang/en_US.lang"));
    		
    		// If we still don't have a file then make one
    		if(!lang.exists())
				try
				{lang.createNewFile();}
				catch(IOException e)
				{}
    		
    		// Make sure the fates are no conspiring against us
    		if(lang.exists())
    			l.CheckLangEntries(lang);
    		
    		// Load everything
    		l.FullLoad();
    		
    		// Add the submod to the list of loaded mods
    		loaded.add(mod);
    	}
    	
    	int size = -1;
    	
    	// Loop until we have nothing left to do or until we can not resolve dependencies any further
    	while(size != 0 && size != needs_loading.size())
    	{
    		size = needs_loading.size();
    		
    		for(int i = 0;i < needs_loading.size();i++)
    		{
    			String mod = needs_loading.get(i);
    			Loader l = StringToLoader(mod);
        		
        		if(l == null)
        			continue; // We found a fake submod
        		
        		// If we've already loaded this submod don't do it again (it might be possible that this occurs)
        		// Note that we will never have duplicate entries in the needs loading list (not that it matters)
        		if(loaded.contains(mod))
        		{
        			needs_loading.remove(i--);
        			continue;
        		}
        		
        		// If we still can't load the submod then skip it and hope for the best later
        		if(!l.CheckDependencies(loaded))
        			continue;
        		
        		// Load everything
        		l.FullLoad();
        		
        		// Add the submod to the list of loaded mods
        		loaded.add(mod);
        		
        		// Remove this mod from the needs loading list
        		needs_loading.remove(i--);
    		}
    	}
    	
    	// Load the fully formed achievement page
    	RunicMagicAchievementPage.RegisterPage();
    	
    	fin.close();
    	return loaded;
    }
    
    /**
     * Returns null on failure.
     */
    private Loader StringToLoader(String mod)
    {
    	if(mod.equals("Core"))
    		return new CoreLoader();
		
    	return null;
    }
    
    /**
     * The mod ID for Ritual Magic.
     */
    public static final String MODID = "runicmagic";
    
    /**
     * The mod name for Ritual Magic.
     */
    public static final String MODNAME = "Runic Magic";
    
    /**
     * The mod version for Ritual Magic.
     */
    public static final String VERSION = "0.1";
    
    /**
     * The instance of Ritual Magic running in Minecraft.
     */
    @Instance(value = RunicMagic.MODID) public static RunicMagic instance;
    
    /**
     * Performs operations specific to client or server side as appropriate.
     */
    @SidedProxy(modId = RunicMagic.MODID,clientSide = "rm.core.proxy.ClientProxy",serverSide = "rm.core.proxy.ServerProxy") public static CommonProxy proxy; 
}
