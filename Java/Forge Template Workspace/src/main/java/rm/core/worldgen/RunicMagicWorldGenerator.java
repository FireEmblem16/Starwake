package rm.core.worldgen;

import java.util.HashMap;
import java.util.Random;
import rm.core.block.RunicMagicBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class RunicMagicWorldGenerator implements IWorldGenerator
{
	/**
	 * The name of this world generator.
	 */
	public abstract String name();
	
	/**
	 * The generation weight of this generator.
	 * Lower values are run sooner in a chunk's generation.
	 */
	public abstract int weight();
	
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch(world.provider.dimensionId)
		{
		case 0:
			GenerateOverworld(rand,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
			break;
		case 1:
			GenerateTheEnd(rand,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
			break;
		case -1:
			GenerateNether(rand,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
			break;
		default:
			GenerateMisc(rand,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
			break;
		}
		
		return;
	}
	
	/**
	 * Called when generating in an overworld chunk.
	 */
	protected abstract void GenerateOverworld(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
	/**
	 * Called when generating in an end chunk.
	 */
	protected abstract void GenerateTheEnd(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
	/**
	 * Called when generating in a nether chunk.
	 */
	protected abstract void GenerateNether(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
	/**
	 * Called when generating in a faerie chunk.
	 */
	protected abstract void GenerateFaerie(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
	/**
	 * Called when generating in an unknown world chunk.
	 */
	protected abstract void GenerateMisc(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
	/**
	 * Registers Runic Magic world generators with Minecraft.
	 */
	public static void RegisterGenerators()
	{
		for(RunicMagicWorldGenerator generator : generators.values())
			GameRegistry.registerWorldGenerator(generator,generator.weight());
		
		return;
	}
	
	/**
	 * Registers the given world generator into the set of runic magic world generators.
	 * @return Returns the given world generator for cascading.
	 */
	public static RunicMagicWorldGenerator RegisterGenerator(RunicMagicWorldGenerator generator)
	{
		generators.put(generator.name(),generator);
		return generator;
	}
	
	/**
	 * Gets the world generator with the given name.
	 * @return Returns the world generator with the given name or null if no such world generator exists. 
	 */
	public static RunicMagicWorldGenerator GetGenerator(String name)
	{return generators.get(name);}
	
	private static HashMap<String,RunicMagicWorldGenerator> generators = new HashMap<String,RunicMagicWorldGenerator>();
}
