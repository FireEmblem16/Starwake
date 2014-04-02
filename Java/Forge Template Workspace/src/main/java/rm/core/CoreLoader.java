package rm.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.stats.Achievement;
import rm.core.achievement.*;
import rm.core.achievement.events.*;
import rm.core.block.*;
import rm.core.block.plant.crop.*;
import rm.core.block.portal.*;
import rm.core.creative.tab.*;
import rm.core.item.*;
import rm.core.item.plant.RunicMagicSeed;
import rm.core.item.plant.harvest.*;
import rm.core.item.plant.seed.*;
import rm.core.item.rune.*;
import rm.core.item.tool.RunicMagicTool;
import rm.core.item.tool.RunicMagicToolMaterial;
import rm.core.item.tool.material.*;
import rm.core.item.tool.spade.FaerieSpade;

public class CoreLoader extends Loader
{
	public boolean CheckDependencies(ArrayList<String> submods_loaded)
	{return !submods_loaded.contains("Core");}
	
	public void CheckLangEntries(File lang)
	{
		Scanner fin = null;
		
		try
		{fin = new Scanner(lang);}
		catch(IOException e)
		{return;}
		
		ArrayList<String> text = new ArrayList<String>();
		
		while(fin.hasNextLine())
		{
			String next = fin.nextLine();
			
			if(!next.equals(""))
				text.add(next);
		}
		
		String[] entries = text.toArray(new String[text.size()]);
		Arrays.sort(entries);
		
		// Java really sucks so we are going to just insert willy nilly into the arraylist rather than keeping it sorted
		// Why, oh why can't Java be C#
		boolean modified = false;
		
		for(RunicMagicBlock block : blocks)
			if(Arrays.binarySearch(entries,"tile." + block.name() + ".name=" + block.name()) < 0)
			{
				text.add("tile." + block.name() + ".name=" + block.name());
				modified = true;
			}
		
		for(RunicMagicItem item : items)
			if(Arrays.binarySearch(entries,"item." + item.name() + ".name=" + item.name()) < 0)
			{
				text.add("item." + item.name() + ".name=" + item.name());
				modified = true;
			}
		
		for(RunicMagicTool tool : tools)
			if(Arrays.binarySearch(entries,"item." + tool.name() + ".name=" + tool.name()) < 0)
			{
				text.add("item." + tool.name() + ".name=" + tool.name());
				modified = true;
			}
		
		for(CreativeTabs tab : tabs)
			if(Arrays.binarySearch(entries,"itemGroup." + tab.getTabLabel() + "=" + tab.getTabLabel()) < 0)
			{
				text.add("itemGroup." + tab.getTabLabel() + "=" + tab.getTabLabel());
				modified = true;
			}
		
		for(RunicMagicAchievement achievement : achievements.values())
		{
			RunicMagicAchievementPage.RegisterAchievement(achievement);
			
			if(Arrays.binarySearch(entries,"achievement." + achievement.name + "=" + achievement.name) < 0)
			{
				text.add("achievement." + achievement.name + "=" + achievement.name);
				modified = true;
			}
			
			if(Arrays.binarySearch(entries,"achievement." + achievement.name + ".desc=" + achievement.description) < 0)
			{
				text.add("achievement." + achievement.name + ".desc=" + achievement.description);
				modified = true;
			}
		}
		
		for(RunicMagicSeed seed : seeds)
			if(Arrays.binarySearch(entries,"item." + seed.name() + ".name=" + seed.name()) < 0)
			{
				text.add("item." + seed.name() + ".name=" + seed.name());
				modified = true;
			}
		
		fin.close();
		
		if(modified)
		{
			entries = text.toArray(new String[text.size()]);
			Arrays.sort(entries);
			
			try
			{
				FileWriter fout = new FileWriter(lang);
				
				for(String s : entries)
					fout.write(s + "\n");
				
				fout.close();
			}
			catch(IOException e)
			{return;}
		}
		
		return;
	}
	
	public void FullLoad()
	{
		LoadBlocks();
		LoadItems();
		LoadTools();
		LoadSeeds();
		
		return;
	}
	
	public void LoadBlocks()
	{
		for(RunicMagicBlock block : blocks)
			RunicMagicBlock.RegisterBlock(block);
		
		return;
	}
	
	public void LoadItems()
	{
		for(RunicMagicItem item : items)
			RunicMagicItem.RegisterItem(item);
		
		return;
	}
	
	public void LoadTools()
	{
		for(RunicMagicTool tool : tools)
			RunicMagicItem.RegisterTool(tool);
		
		return;
	}
	
	public void LoadSeeds()
	{
		for(RunicMagicSeed seed : seeds)
			RunicMagicSeed.RegisterSeed(seed);
		
		return;
	}
	
	private static ArrayList<RunicMagicBlock> blocks = new ArrayList<RunicMagicBlock>();
	private static ArrayList<RunicMagicItem> items = new ArrayList<RunicMagicItem>();
	private static ArrayList<CreativeTabs> tabs = new ArrayList<CreativeTabs>();
	private static HashMap<String,RunicMagicAchievement> achievements = new HashMap<String,RunicMagicAchievement>();
	private static ArrayList<RunicMagicSeed> seeds = new ArrayList<RunicMagicSeed>();
	private static ArrayList<RunicMagicToolMaterial> tool_mats = new ArrayList<RunicMagicToolMaterial>();
	private static ArrayList<RunicMagicTool> tools = new ArrayList<RunicMagicTool>();
	
	static
	{
		// Add blocks added by this submod
		blocks.add(FaerieRingGrass.instance);
		
		// Add items added by this submod
		
		
		// Add runes added by this submod
		items.add(BlankRune.instance);
		items.add(FeyBlankRune.instance);
		
		// Add creative tabs added by this submod
		tabs.add(RunicMagicTab.instance);
		
		// Add achivements added by this submod
		achievements.put("Essence",new RunicMagicAchievement("Essence","Gather Ethel Seeds.",0,0,EthelSeed.instance,null,false,new SimplePickupMonitor(EthelSeed.instance,"Essence")));
		achievements.put("Potential",new RunicMagicAchievement("Potential","Forge a blank rune.",2,0,BlankRune.instance,achievements.get("Essence"),false,new SimpleCraftingMonitor(BlankRune.instance,"Potential")));
		
		// This achievement should require an achievement for entering the faerie realm first
		achievements.put("Potent",new RunicMagicAchievement("Potent","Forge a fey blank rune.",2,2,FeyBlankRune.instance,achievements.get("Potential"),false,new SimpleCraftingMonitor(FeyBlankRune.instance,"Potent")));
		
		// Add all seeds added by this submod
		seeds.add(EthelSeed.instance);
		
		// Add all crops added by this submod
		blocks.add(EthelCrop.instance);
		blocks.add(GrownEthelCrop.instance);
		blocks.add(FeyEthelCrop.instance);
		
		// Add all harvests added by this submod
		items.add(EthelWeed.instance);
		items.add(FeyEthelWeed.instance);
		
		// Add tool materials added by this submod
		tool_mats.add(FaerieToolMaterial.instance);
		tool_mats.add(FeyToolMaterial.instance);
		tool_mats.add(WizardToolMaterial.instance);
		
		// Add tools added by this submod
		tools.add(FaerieSpade.instance);
	}
}
