package rm.core;

import java.io.File;
import java.util.ArrayList;

public abstract class Loader
{
	/**
	 * Checks if this submod can be loaded given the submods already loaded.
	 * @param submods_loaded The names of the submods already loaded.
	 * @returns Returns true if this submod can be loaded and false otherwise (including if it has already been loaded).
	 */
	public abstract boolean CheckDependencies(ArrayList<String> submods_loaded);
	
	/**
	 * Checks that all necessary language entries for this submod are present.
	 * @param lang The file containing our language specifications.
	 */
	public abstract void CheckLangEntries(File lang);
	
	/**
	 * Loads everything.
	 */
	public abstract void FullLoad();
	
	/**
	 * Loads all the blocks of this submod into the RitualMagicBlock registry.
	 */
	public abstract void LoadBlocks();
	
	/**
	 * Loads all the items of this submod into the RitualMagicItem registry.
	 */
	public abstract void LoadItems();
	
	/**
	 * Loads all the seeds of this submod into the RitualMagicSeed registry.
	 */
	public abstract void LoadSeeds();
}
