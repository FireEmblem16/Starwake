package rm.core.worldgen.overworld;

import java.util.ArrayList;
import java.util.Random;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.worldgen.RunicMagicWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class BalrogMine extends RunicMagicWorldGenerator
{
	private BalrogMine()
	{return;}
	
	protected void GenerateOverworld(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		// If we're currently generating Moria then do nothing
		// Note that only one Moria can be generating at a time so fully generate one to get another
		// This is thematically appropriate because we have previous few balrogs to go around
		if(generating)
		{
			// Check if we are within Moria
			if(chunkX < minchunkX || chunkX > maxchunkX || chunkZ < minchunkZ || chunkZ > maxchunkZ)
				return;
			
			// Generate this Moria chunk
			GenerateMoriaChunk(rand,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
			--left;
			
			// Handle the balrog case
			if(chunkX == (maxchunkX - minchunkX) / 2 && chunkZ == (maxchunkZ - minchunkZ) / 2)
				; // Spawn balrog
			
			if(left == 0)
				generating = false;
			
			return;
		}
		else if(rand.nextInt(1000) > 0) // Moria rarely generates
			return;
		
		// Initialize generation data
		generating = true;
		left = 399;
		minchunkX = chunkX;
		minchunkZ = chunkZ;
		maxchunkX = chunkX + 19;
		maxchunkZ = chunkZ + 19;
		
		// Generate the first Moria chunk
		GenerateMoriaChunk(rand,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
		
		// Don't worry about every chunk already generated
		// Kind of a janky fix but its the only way to make a coherent Moria without generating it all at once
		
		return;
	}
	
	private boolean generating = false;
	private int left = 0;
	private int minchunkX = 0;
	private int minchunkZ = 0;
	private int maxchunkX = 0;
	private int maxchunkZ = 0;
	
	private static void GenerateMoriaChunk(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		int X = chunkX * 16;
		int Z = chunkZ * 16;
		
		for(int j = 0;j < 16;j++)
			for(int k = 0;k < 16;k++)
			{
				world.setBlock(X + j,4,Z + k,Blocks.stonebrick,0,0);
				world.setBlock(X + j,5,Z + k,Blocks.stonebrick,0,0);
				world.setBlock(X + j,55,Z + k,Blocks.stonebrick,0,0);
				world.setBlock(X + j,56,Z + k,Blocks.stonebrick,0,0);
				world.setBlock(X + j,57,Z + k,Blocks.air,0,0); // Prevent ethel spawns in Moria
				world.setBlock(X + j,58,Z + k,Blocks.stonebrick,0,0);
			}
		
		for(int i = 6;i < 55;i++)
			for(int j = 0;j < 16;j++)
				for(int k = 0;k < 16;k++)
					if(!world.getBlock(X + j,i,Z + k).equals(Blocks.stonebrick))
						world.setBlock(X + j,i,Z + k,Blocks.air,0,0);
		
		ArrayList<Loc> blocks = null;
		
		switch(Math.abs(chunkX % 2) + 2 * Math.abs(chunkZ % 2))
		{
		case 0: // bottom-left
			blocks = GetBL();
			break;
		case 1: // bottom-right
			blocks = GetBR();
			break;
		case 2: // top-left
			blocks = GetTL();
			break;
		case 3: // top-right
			blocks = GetTR();
			break;
		}
		
		if(blocks == null)
			return;
		
		for(Loc loc : blocks)
			world.setBlock(X + loc.x,6 + loc.y,Z + loc.z,Blocks.stonebrick,0,0);
		
		return;
	}
	
	private static ArrayList<Loc> GetBL()
	{
		ArrayList<Loc> ret = new ArrayList<Loc>(1500);
		
		ret.add(new Loc(0,0,5));
		ret.add(new Loc(1,0,5));
		ret.add(new Loc(2,0,4));
		ret.add(new Loc(3,0,4));
		ret.add(new Loc(3,0,3));
		ret.add(new Loc(4,0,2));
		ret.add(new Loc(4,0,1));
		ret.add(new Loc(4,0,0));
		
		ret.add(new Loc(0,1,5));
		ret.add(new Loc(1,1,5));
		ret.add(new Loc(2,1,4));
		ret.add(new Loc(3,1,3));
		ret.add(new Loc(4,1,2));
		ret.add(new Loc(4,1,1));
		ret.add(new Loc(4,1,0));
		
		ret.add(new Loc(0,2,5));
		ret.add(new Loc(1,2,4));
		ret.add(new Loc(2,2,3));
		ret.add(new Loc(3,2,2));
		ret.add(new Loc(4,2,1));
		ret.add(new Loc(4,2,0));
		
		ret.add(new Loc(0,3,4));
		ret.add(new Loc(1,3,4));
		ret.add(new Loc(2,3,3));
		ret.add(new Loc(3,3,2));
		ret.add(new Loc(3,3,1));
		ret.add(new Loc(3,3,0));
		
		ret.add(new Loc(0,4,4));
		ret.add(new Loc(1,4,4));
		ret.add(new Loc(2,4,3));
		ret.add(new Loc(3,4,2));
		ret.add(new Loc(3,4,1));
		ret.add(new Loc(3,4,0));
		
		ret.add(new Loc(0,5,4));
		ret.add(new Loc(1,5,4));
		ret.add(new Loc(2,5,3));
		ret.add(new Loc(3,5,2));
		ret.add(new Loc(3,5,1));
		ret.add(new Loc(3,5,0));
		
		ret.add(new Loc(0,6,4));
		ret.add(new Loc(1,6,3));
		ret.add(new Loc(2,6,3));
		ret.add(new Loc(2,6,2));
		ret.add(new Loc(3,6,1));
		ret.add(new Loc(3,6,0));
		
		ret.add(new Loc(0,7,3));
		ret.add(new Loc(1,7,3));
		ret.add(new Loc(2,7,2));
		ret.add(new Loc(2,7,1));
		ret.add(new Loc(3,7,0));
		
		ret.add(new Loc(0,8,3));
		ret.add(new Loc(1,8,3));
		ret.add(new Loc(2,8,2));
		ret.add(new Loc(2,8,1));
		ret.add(new Loc(2,8,0));
		
		ret.add(new Loc(0,9,4));
		ret.add(new Loc(1,9,4));
		ret.add(new Loc(2,9,3));
		ret.add(new Loc(3,9,2));
		ret.add(new Loc(3,9,1));
		ret.add(new Loc(3,9,0));
		
		ret.add(new Loc(0,10,3));
		ret.add(new Loc(1,10,3));
		ret.add(new Loc(2,10,2));
		ret.add(new Loc(2,10,1));
		ret.add(new Loc(2,10,0));
		
		ret.add(new Loc(0,11,3));
		ret.add(new Loc(1,11,3));
		ret.add(new Loc(2,11,2));
		ret.add(new Loc(2,11,1));
		ret.add(new Loc(2,11,0));
		
		ret.add(new Loc(0,12,3));
		ret.add(new Loc(1,12,3));
		ret.add(new Loc(2,12,2));
		ret.add(new Loc(2,12,1));
		ret.add(new Loc(2,12,0));
		
		ret.add(new Loc(0,13,3));
		ret.add(new Loc(1,13,3));
		ret.add(new Loc(2,13,2));
		ret.add(new Loc(2,13,1));
		ret.add(new Loc(2,13,0));
		
		ret.add(new Loc(0,14,3));
		ret.add(new Loc(1,14,2));
		ret.add(new Loc(2,14,1));
		ret.add(new Loc(2,14,0));
		
		ret.add(new Loc(0,15,2));
		ret.add(new Loc(1,15,1));
		ret.add(new Loc(2,15,0));
		
		ret.add(new Loc(0,16,2));
		ret.add(new Loc(1,16,1));
		ret.add(new Loc(1,16,0));
		
		ret.add(new Loc(0,17,2));
		ret.add(new Loc(1,17,2));
		ret.add(new Loc(1,17,1));
		ret.add(new Loc(1,17,0));
		
		ret.add(new Loc(0,18,2));
		ret.add(new Loc(1,18,2));
		ret.add(new Loc(1,18,1));
		ret.add(new Loc(1,18,0));
		
		ret.add(new Loc(0,19,2));
		ret.add(new Loc(1,19,2));
		ret.add(new Loc(1,19,1));
		ret.add(new Loc(1,19,0));
		
		ret.add(new Loc(0,20,2));
		ret.add(new Loc(1,20,1));
		ret.add(new Loc(1,20,0));
		
		ret.add(new Loc(0,21,2));
		ret.add(new Loc(1,21,1));
		ret.add(new Loc(1,21,0));
		
		ret.add(new Loc(0,22,2));
		ret.add(new Loc(1,22,1));
		ret.add(new Loc(1,22,0));
		
		ret.add(new Loc(0,23,2));
		ret.add(new Loc(1,23,1));
		ret.add(new Loc(1,23,0));
		
		ret.add(new Loc(0,24,2));
		ret.add(new Loc(1,24,2));
		ret.add(new Loc(1,24,1));
		ret.add(new Loc(1,24,0));
		
		ret.add(new Loc(0,25,2));
		ret.add(new Loc(1,25,2));
		ret.add(new Loc(1,25,1));
		ret.add(new Loc(2,25,0));
		
		ret.add(new Loc(0,26,3));
		ret.add(new Loc(1,26,2));
		ret.add(new Loc(2,26,1));
		ret.add(new Loc(2,26,0));
		
		ret.add(new Loc(0,27,3));
		ret.add(new Loc(1,27,2));
		ret.add(new Loc(2,27,1));
		ret.add(new Loc(2,27,0));
		
		ret.add(new Loc(0,28,3));
		ret.add(new Loc(1,28,2));
		ret.add(new Loc(2,28,1));
		ret.add(new Loc(2,28,0));
		
		ret.add(new Loc(0,29,5));
		ret.add(new Loc(1,29,5));
		ret.add(new Loc(2,29,4));
		ret.add(new Loc(3,29,3));
		ret.add(new Loc(4,29,2));
		ret.add(new Loc(4,29,1));
		ret.add(new Loc(4,29,0));
		
		ret.add(new Loc(0,29,4));
		ret.add(new Loc(1,29,4));
		ret.add(new Loc(1,29,3));
		ret.add(new Loc(2,29,3));
		ret.add(new Loc(2,29,2));
		ret.add(new Loc(3,29,2));
		ret.add(new Loc(3,29,1));
		ret.add(new Loc(3,29,0));
		
		ret.add(new Loc(0,30,5));
		ret.add(new Loc(1,30,5));
		ret.add(new Loc(2,30,4));
		ret.add(new Loc(3,30,3));
		ret.add(new Loc(4,30,2));
		ret.add(new Loc(4,30,1));
		ret.add(new Loc(4,30,0));
		
		ret.add(new Loc(0,31,4));
		ret.add(new Loc(1,31,4));
		ret.add(new Loc(2,31,3));
		ret.add(new Loc(3,31,2));
		ret.add(new Loc(3,31,1));
		ret.add(new Loc(3,31,0));
		
		ret.add(new Loc(0,32,4));
		ret.add(new Loc(1,32,4));
		ret.add(new Loc(2,32,3));
		ret.add(new Loc(3,32,2));
		ret.add(new Loc(3,32,1));
		ret.add(new Loc(3,32,0));
		
		ret.add(new Loc(0,33,5));
		ret.add(new Loc(1,33,5));
		ret.add(new Loc(2,33,4));
		ret.add(new Loc(3,33,3));
		ret.add(new Loc(4,33,2));
		ret.add(new Loc(4,33,1));
		ret.add(new Loc(4,33,0));
		
		ret.add(new Loc(0,34,5));
		ret.add(new Loc(1,34,5));
		ret.add(new Loc(2,34,4));
		ret.add(new Loc(3,34,3));
		ret.add(new Loc(4,34,2));
		ret.add(new Loc(4,34,1));
		ret.add(new Loc(4,34,0));
		
		ret.add(new Loc(0,35,6));
		ret.add(new Loc(1,35,6));
		ret.add(new Loc(2,35,5));
		ret.add(new Loc(3,35,4));
		ret.add(new Loc(4,35,3));
		ret.add(new Loc(5,35,2));
		ret.add(new Loc(5,35,1));
		ret.add(new Loc(5,35,0));
		
		ret.add(new Loc(0,36,6));
		ret.add(new Loc(1,36,6));
		ret.add(new Loc(2,36,5));
		ret.add(new Loc(3,36,4));
		ret.add(new Loc(4,36,3));
		ret.add(new Loc(5,36,2));
		ret.add(new Loc(5,36,1));
		ret.add(new Loc(5,36,0));
		
		ret.add(new Loc(0,37,7));
		ret.add(new Loc(1,37,7));
		ret.add(new Loc(2,37,6));
		ret.add(new Loc(3,37,6));
		ret.add(new Loc(3,37,5));
		ret.add(new Loc(4,37,5));
		ret.add(new Loc(4,37,4));
		ret.add(new Loc(5,37,4));
		ret.add(new Loc(5,37,3));
		ret.add(new Loc(6,37,2));
		ret.add(new Loc(6,37,1));
		ret.add(new Loc(6,37,0));
		
		ret.add(new Loc(0,38,7));
		ret.add(new Loc(1,38,7));
		ret.add(new Loc(2,38,6));
		ret.add(new Loc(3,38,6));
		ret.add(new Loc(3,38,5));
		ret.add(new Loc(4,38,5));
		ret.add(new Loc(4,38,4));
		ret.add(new Loc(5,38,4));
		ret.add(new Loc(5,38,3));
		ret.add(new Loc(6,38,2));
		ret.add(new Loc(6,38,1));
		ret.add(new Loc(6,38,0));
		
		ret.add(new Loc(0,39,8));
		ret.add(new Loc(1,39,8));
		ret.add(new Loc(2,39,7));
		ret.add(new Loc(3,39,6));
		ret.add(new Loc(4,39,6));
		ret.add(new Loc(5,39,5));
		ret.add(new Loc(5,39,4));
		ret.add(new Loc(6,39,3));
		ret.add(new Loc(7,39,2));
		ret.add(new Loc(7,39,1));
		ret.add(new Loc(7,39,0));
		
		ret.add(new Loc(0,40,8));
		ret.add(new Loc(1,40,8));
		ret.add(new Loc(2,40,7));
		ret.add(new Loc(3,40,6));
		ret.add(new Loc(4,40,6));
		ret.add(new Loc(5,40,6));
		ret.add(new Loc(5,40,5));
		ret.add(new Loc(5,40,4));
		ret.add(new Loc(6,40,3));
		ret.add(new Loc(7,40,2));
		ret.add(new Loc(7,40,1));
		ret.add(new Loc(7,40,0));
		
		ret.add(new Loc(0,41,9));
		ret.add(new Loc(1,41,9));
		ret.add(new Loc(2,41,8));
		ret.add(new Loc(3,41,7));
		ret.add(new Loc(4,41,7));
		ret.add(new Loc(5,41,6));
		ret.add(new Loc(6,41,5));
		ret.add(new Loc(6,41,4));
		ret.add(new Loc(7,41,3));
		ret.add(new Loc(8,41,2));
		ret.add(new Loc(8,41,1));
		ret.add(new Loc(8,41,0));
		
		ret.add(new Loc(0,42,9));
		ret.add(new Loc(1,42,9));
		ret.add(new Loc(2,42,8));
		ret.add(new Loc(3,42,7));
		ret.add(new Loc(4,42,7));
		ret.add(new Loc(5,42,6));
		ret.add(new Loc(6,42,5));
		ret.add(new Loc(6,42,4));
		ret.add(new Loc(7,42,3));
		ret.add(new Loc(8,42,2));
		ret.add(new Loc(8,42,1));
		ret.add(new Loc(8,42,0));
		
		ret.add(new Loc(0,43,10));
		ret.add(new Loc(1,43,10));
		ret.add(new Loc(2,43,9));
		ret.add(new Loc(3,43,8));
		ret.add(new Loc(4,43,7));
		ret.add(new Loc(5,43,7));
		ret.add(new Loc(6,43,6));
		ret.add(new Loc(6,43,5));
		ret.add(new Loc(7,43,4));
		ret.add(new Loc(8,43,3));
		ret.add(new Loc(9,43,2));
		ret.add(new Loc(9,43,1));
		ret.add(new Loc(9,43,0));
		
		ret.add(new Loc(0,44,10));
		ret.add(new Loc(1,44,10));
		ret.add(new Loc(2,44,9));
		ret.add(new Loc(3,44,9));
		ret.add(new Loc(4,44,8));
		ret.add(new Loc(5,44,7));
		ret.add(new Loc(6,44,7));
		ret.add(new Loc(6,44,6));
		ret.add(new Loc(7,44,5));
		ret.add(new Loc(8,44,4));
		ret.add(new Loc(8,44,3));
		ret.add(new Loc(9,44,2));
		ret.add(new Loc(9,44,1));
		ret.add(new Loc(9,44,0));
		
		ret.add(new Loc(2,45,10));
		ret.add(new Loc(3,45,10));
		ret.add(new Loc(4,45,9));
		ret.add(new Loc(5,45,8));
		ret.add(new Loc(6,45,7));
		ret.add(new Loc(7,45,6));
		ret.add(new Loc(8,45,5));
		ret.add(new Loc(9,45,4));
		ret.add(new Loc(9,45,3));
		
		ret.add(new Loc(2,46,11));
		ret.add(new Loc(3,46,10));
		ret.add(new Loc(4,46,10));
		ret.add(new Loc(5,46,9));
		ret.add(new Loc(6,46,8));
		ret.add(new Loc(7,46,7));
		ret.add(new Loc(8,46,6));
		ret.add(new Loc(9,46,5));
		ret.add(new Loc(9,46,4));
		ret.add(new Loc(10,46,3));
		
		ret.add(new Loc(2,47,12));
		ret.add(new Loc(3,47,11));
		ret.add(new Loc(4,47,10));
		ret.add(new Loc(5,47,9));
		ret.add(new Loc(6,47,8));
		ret.add(new Loc(7,47,7));
		ret.add(new Loc(8,47,6));
		ret.add(new Loc(9,47,5));
		ret.add(new Loc(10,47,4));
		ret.add(new Loc(11,47,3));
		
		ret.add(new Loc(2,48,12));
		ret.add(new Loc(3,48,11));
		ret.add(new Loc(4,48,10));
		ret.add(new Loc(5,48,9));
		ret.add(new Loc(6,48,8));
		ret.add(new Loc(7,48,7));
		ret.add(new Loc(8,48,6));
		ret.add(new Loc(9,48,5));
		ret.add(new Loc(10,48,4));
		ret.add(new Loc(11,48,3));
		
		for(int i = 0;i < 2;i++)
			for(int j = 45;j < 49;j++)
				for(int k = 11;k < 16;k++)
					ret.add(new Loc(i,j,k));
		
		for(int i = 0;i < 3;i++)
			for(int j = 45;j < 49;j++)
				for(int k = 10;k < 16;k++)
					ret.add(new Loc(k,j,i));
		
		return ret;
	}
	
	private static ArrayList<Loc> GetBR()
	{
		ArrayList<Loc> ret = new ArrayList<Loc>(BL.size());
		
		for(Loc loc : BL)
			ret.add(new Loc(15 - loc.z,loc.y,loc.x + 1));
		
		return ret;
	}
	
	private static ArrayList<Loc> GetTL()
	{
		ArrayList<Loc> ret = new ArrayList<Loc>(BL.size());
		
		for(Loc loc : BL)
			ret.add(new Loc(loc.z - 1,loc.y,15 - loc.x));
		
		return ret;
	}
	
	private static ArrayList<Loc> GetTR()
	{
		ArrayList<Loc> ret = new ArrayList<Loc>(BL.size());
		
		for(Loc loc : BL)
			ret.add(new Loc(14 - loc.x,loc.y,16 - loc.z));
		
		return ret;
	}
	
	private static final ArrayList<Loc> BL = GetBL();
	private static final ArrayList<Loc> BR = GetBR();
	private static final ArrayList<Loc> TL = GetTL();
	private static final ArrayList<Loc> TR = GetTR();
	
	protected void GenerateTheEnd(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	protected void GenerateNether(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	protected void GenerateFaerie(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	protected void GenerateMisc(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	public String name()
	{return name;}

	public int weight()
	{return 50;}
	
	public static final String name = "Balrog Mine";
	public static final BalrogMine instance = new BalrogMine();
	
	private static class Loc
	{
		public Loc(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			
			return;
		}
		
		public int x;
		public int y;
		public int z;
	}
}
