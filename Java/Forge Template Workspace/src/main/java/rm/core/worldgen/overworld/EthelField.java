package rm.core.worldgen.overworld;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import rm.core.CONSTANTS;
import rm.core.block.plant.crop.GrownEthelCrop;
import rm.core.block.portal.FaerieRingGrass;
import rm.core.worldgen.RunicMagicWorldGenerator;

public class EthelField extends RunicMagicWorldGenerator
{
	private EthelField()
	{return;}
	
	protected void GenerateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if(random.nextInt(100) > 27)
			return;
		
		for(int i = 0;i < 4;i++)
		{
			int X = chunkX * 16 + random.nextInt(16);
			int Y = -1;
			int Z = chunkZ * 16 + random.nextInt(16);
			int maxY = Math.min(world.getTopSolidOrLiquidBlock(X,Z),CONSTANTS.SEA_LEVEL) - 1; // Ethel does not spawn above sea level
			
			for(int j = maxY;j > 5;j--)
				if(world.isAirBlock(X,j,Z))
				{
					Y = j;
					break;
				}
			
			if(Y < 0)
				continue;
			
			if(!GrownEthelCrop.instance.canBlockStay(world,X,Y + 1,Z))
				world.setBlock(X,Y + 1,Z,Blocks.dirt);
			
			// Place the ethel weed
			world.setBlock(X,Y,Z,GrownEthelCrop.instance,0,2);
			
			// Give the ethel weed room to grow
			for(int j = 0;j < 8;j++)
				ExpandDirt(j,7,world,X,Y,Z);
		}
		
		return;
	}

	private void ExpandDirt(int direction, int distance, World world, int X, int Y, int Z)
	{
		if(distance < 1)
			return; // We can go no further
		
		switch(direction)
		{
		case NORTH:
			--Z;
			break;
		case SOUTH:
			++Z;
			break;
		case EAST:
			++X;
			break;
		case WEST:
			--X;
			break;
		case NE:
			--Z;++X;
			break;
		case NW:
			--Z;--X;
			break;
		case SE:
			++Z;++X;
			break;
		case SW:
			++Z;--X;
			break;
		}
		
		// Brackets left in for clarity because this is a mess
		if(world.isAirBlock(X,Y,Z)) // Check if the current block is air
		{
			if(world.isAirBlock(X,Y + 1,Z))
			{
				if(!world.isAirBlock(X,Y + 2,Z))
				{
					if(!GrownEthelCrop.instance.canBlockStay(world,X,Y + 2,Z)) // We have a block so it needs to support ethel
						world.setBlock(X,Y + 2,Z,Blocks.dirt,0,2);
					
					++Y; // We moved the potential ethel weed up one
				}
				else
					return; // We can not go any further
			}
			else if(!GrownEthelCrop.instance.canBlockStay(world,X,Y + 1,Z)) // We have open space and the block above now needs to support ethel
				world.setBlock(X,Y + 1,Z,Blocks.dirt); // We stay the course so we do not need to modify Y
			else
				return; // We can not go any further
		}
		else if(world.isAirBlock(X,Y - 1,Z)) // The current block is not air so if the one below it is we should make this block ethel friendly
		{
			if(!GrownEthelCrop.instance.canBlockStay(world,X,Y,Z))
				world.setBlock(X,Y,Z,Blocks.dirt,0,2); // Ethel could not be placed on this block so we should turn it into dirt
			
			--Y; // We moved the potential ethel weed down one
		}
		else
			return; // We can not go any further
		
		--distance;
		
		switch(direction)
		{
		case NORTH:
		case SOUTH:
		case EAST:
		case WEST:
			ExpandDirt(direction,distance,world,X,Y,Z);
			break;
		case NE:
			ExpandDirt(NORTH,distance,world,X,Y,Z);
			ExpandDirt(direction,distance,world,X,Y,Z);
			ExpandDirt(EAST,distance,world,X,Y,Z);
			
			break;
		case NW:
			ExpandDirt(NORTH,distance,world,X,Y,Z);
			ExpandDirt(direction,distance,world,X,Y,Z);
			ExpandDirt(WEST,distance,world,X,Y,Z);
			
			break;
		case SE:
			ExpandDirt(SOUTH,distance,world,X,Y,Z);
			ExpandDirt(direction,distance,world,X,Y,Z);
			ExpandDirt(EAST,distance,world,X,Y,Z);
			
			break;
		case SW:
			ExpandDirt(SOUTH,distance,world,X,Y,Z);
			ExpandDirt(direction,distance,world,X,Y,Z);
			ExpandDirt(WEST,distance,world,X,Y,Z);
			
			break;
		}
		
		return;
	}
	
	private static final int NORTH = 0;
	private static final int SOUTH = 1;
	private static final int EAST = 2;
	private static final int WEST = 3;
	private static final int NE = 4;
	private static final int NW = 5;
	private static final int SE = 6;
	private static final int SW = 7;
	
	protected void GenerateTheEnd(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	protected void GenerateNether(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	protected void GenerateFaerie(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	protected void GenerateMisc(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{return;}

	public String name()
	{return "Ethel Field";}

	public int weight()
	{return 100;}
	
	public static final EthelField instance = new EthelField();
}
