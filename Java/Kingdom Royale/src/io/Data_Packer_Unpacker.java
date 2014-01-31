package io;

import host_process.Game_Status;
import host_process.Player_Data;
import host_process.User;

public class Data_Packer_Unpacker
{
	public static byte[] PackMeetings(String Picker, String Picked)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Meetings".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = Picker.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		msg = Picked.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+200] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the person picking first and who was picked second.
	 */
	public static String[] UnpackMeetings(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Meetings"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		String[] ret = new String[2];
		ret[0] = new String(msg);
		ret[0] = ((String)ret[0]).substring(0,((String)ret[0]).indexOf(0));
		
		msg = null;
		
		for(int i = 200;i < 400;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 399;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+200];
		
		ret[1] = new String(msg);
		ret[1] = ((String)ret[1]).substring(0,((String)ret[1]).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackRevolutionize2()
	{
		byte[] ret = new byte[1024];
		for(int i = 0;i < 4;i++)
			ret[i] = "Double Dies".getBytes()[i];
		
		return ret;
	}
	
	public static String UnpackRevolutionize2(byte[] ping)
	{
		if(ping == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(ping[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = ping[i];
		
		String ret = new String(msg);
		
		if(ret.equals("Double Dies"))
			return ret;
		
		return null;
	}
	
	public static byte[] PackRevolutionize(String Name)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Revolutionize".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = Name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the name of the target as a String.
	 */
	public static String UnpackRevolutionize(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Revolutionize"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		String ret = new String(msg);
		ret = ((String)ret).substring(0,((String)ret).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackDeathBlow(String Name)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Knight Kill".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = Name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the name of the target as a String.
	 */
	public static String UnpackDeathBlow(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Knight Kill"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		String ret = new String(msg);
		ret = ((String)ret).substring(0,((String)ret).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackSorcery(String Name)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Sorcery".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = Name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the name of the target as a String.
	 */
	public static String UnpackSorcery(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Sorcery"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		String ret = new String(msg);
		ret = ((String)ret).substring(0,((String)ret).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackMurderRequest(String Name)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Murder".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = Name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the name of the target as a String.
	 */
	public static String UnpackMurderRequest(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Murder"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		String ret = new String(msg);
		ret = ((String)ret).substring(0,((String)ret).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackSubstitution()
	{
		byte[] ret = new byte[1024];
		for(int i = 0;i < 4;i++)
			ret[i] = "Substitution".getBytes()[i];
		
		return ret;
	}
	
	public static String UnpackSubstitution(byte[] ping)
	{
		if(ping == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(ping[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = ping[i];
		
		String ret = new String(msg);
		
		if(ret.equals("Substitution"))
			return ret;
		
		return null;
	}
	
	public static byte[] PackGiveFood(String Name, byte amount)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Give Food".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		ret[32] = amount;
		
		msg = Name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the name of the target as a String and the amount of food as a Byte.
	 */
	public static Object[] UnpackGiveFood(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Give Food"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		Object[] ret = new Object[2];
		ret[0] = new String(msg);
		ret[0] = ((String)ret[0]).substring(0,((String)ret[0]).indexOf(0));
		ret[1] = new Byte(request[32]);
		
		return ret;
	}
	
	public static byte[] PackDeath(String Class, String Name, byte how_died)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Death".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		ret[32] = how_died;
		
		msg = Class.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		msg = Name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+200] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the class of the dead first as a String, the name of the player who died second as a String, and the method of death third as a Byte.
	 */
	public static Object[] UnpackDeath(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Death"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 200;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 199;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		Object[] ret = new Object[3];
		ret[0] = new String(msg);
		ret[0] = ((String)ret[0]).substring(0,((String)ret[0]).indexOf(0));
		ret[2] = new Byte(request[32]);
		
		msg = null;
		
		for(int i = 200;i < 400;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 399;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+200];
		
		ret[1] = new String(msg);
		ret[1] = ((String)ret[1]).substring(0,((String)ret[1]).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackEndTurn(String Class)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "End Turn".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = Class.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Returns the class of the player ending their turn.
	 */
	public static String UnpackEndTurn(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("End Turn"))
			return null;
		
		msg = null;
		
		for(int i = 40;i < 100;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 99;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		String ret = new String(msg);
		ret = ((String)ret).substring(0,((String)ret).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackPing()
	{
		byte[] ret = new byte[1024];
		for(int i = 0;i < 4;i++)
			ret[i] = "ping".getBytes()[i];
		
		return ret;
	}
	
	public static String UnpackPing(byte[] ping)
	{
		if(ping == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(ping[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = ping[i];
		
		String ret = new String(msg);
		
		if(ret.equals("ping"))
			return ret;
		
		return null;
	}
	
	public static byte[] PackNameRace(long identifier, String name)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Name".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		long temp = identifier;
		
		for(int i = 32;i < 40;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		msg = name.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Contains the iD number (time of sending) of the sender as a Long.
	 * Next contains a String of what name the player wants.
	 */
	public static Object[] UnpackNameRace(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Name"))
			return null;
		
		Object[] ret = new Object[2];
		long LTemp = 0;
		
		for(long i = 32,m = 1;i < 40;i++,m *= 256)
			LTemp += (request[(int)i] < 0 ? request[(int)i] ^ 0xFFFFFFFFFFFFFF00L : request[(int)i]) * m;
		
		ret[0] = new Long(LTemp);
		
		msg = null;
		
		for(int i = 40;i < 100;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 99;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		ret[1] = new String(msg);
		ret[1] = ((String)ret[1]).substring(0,((String)ret[1]).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackClassRace(long identifier, String Class)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "Class".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		long temp = identifier;
		
		for(int i = 32;i < 40;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		msg = Class.getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i+40] = msg[i];
		
		return ret;
	}
	
	/**
	 * Contains the iD number (time of sending) of the sender as a Long.
	 * Next contains a String of what class the player wants.
	 */
	public static Object[] UnpackClassRace(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("Class"))
			return null;
		
		Object[] ret = new Object[2];
		long LTemp = 0;
		
		for(long i = 32,m = 1;i < 40;i++,m *= 256)
			LTemp += (request[(int)i] < 0 ? request[(int)i] ^ 0xFFFFFFFFFFFFFF00L: request[(int)i]) * m;
		
		ret[0] = new Long(LTemp);
		
		msg = null;
		
		for(int i = 40;i < 100;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 99;
			}
		
		if(msg == null)
			return null;
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i+40];
		
		ret[1] = new String(msg);
		ret[1] = ((String)ret[1]).substring(0,((String)ret[1]).indexOf(0));
		
		return ret;
	}
	
	public static byte[] PackPCRace(long identifier)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "PC".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		long temp = identifier;
		
		for(int i = 32;i < 40;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		return ret;
	}
	
	/**
	 * Contains the iD number (time of sending) of the sender.
	 */
	public static long[] UnpackPCRace(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("PC"))
			return null;
		
		long[] ret = new long[1];
		
		for(long i = 32,m = 1;i < 40;i++,m *= 256)
			ret[0] += (request[(int)i] < 0 ? request[(int)i] ^ 0xFFFFFFFFFFFFFF00L : request[(int)i]) * m;
		
		return ret;
	}
	
	public static byte[] PackJoinRequest(long identifier, int number_to_join)
	{
		byte[] ret = new byte[1024];
		byte[] msg = "join".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		ret[32] = (byte)number_to_join;
		
		long temp = identifier;
		
		for(int i = 33;i < 41;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		return ret;
	}
	
	/**
	 * First long is the number of players wanting to join.
	 * Second long is the identifier number of the sender.
	 */
	public static long[] UnpackJoinRequest(byte[] request)
	{
		if(request == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(request[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = request[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("join"))
			return null;
		
		long[] ret = new long[2];
		ret[0] = request[32];
		
		for(long i = 33,m = 1;i < 41;i++,m *= 256)
			ret[1] += (request[(int)i] < 0 ? request[(int)i] ^ 0xFFFFFFFFFFFFFF00L : request[(int)i]) * m;
		
		return ret;
	}
	
	public static byte[] PackJoinConfirm(long identifier)
	{
		byte[] ret = new byte[1024];
		
		byte[] msg = "join confirm".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		long temp = identifier;
		
		for(int i = 32;i < 40;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		return ret;
	}
	
	/**
	 * Array contains only the identifier number.
	 */
	public static long[] UnpackJoinConfirm(byte[] confirm)
	{
		if(confirm == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(confirm[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = confirm[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("join confirm"))
			return null;
		
		long[] ret = new long[1];
		
		for(long i = 32,m = 1;i < 40;i++,m *= 256)
			ret[0] += (confirm[(int)i] < 0 ? confirm[(int)i] ^ 0xFFFFFFFFFFFFFF00L : confirm[(int)i]) * m;
		
		return ret;
	}
	
	public static byte[] PackJoinDeny(long identifier)
	{
		byte[] ret = new byte[1024];
		
		byte[] msg = "join deny".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		long temp = identifier;
		
		for(int i = 32;i < 40;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		return ret;
	}
	
	/**
	 * Array contains only the identifier number.
	 */
	public static long[] UnpackJoinDeny(byte[] deny)
	{
		if(deny == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(deny[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = deny[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("join deny"))
			return null;
		
		long[] ret = new long[1];
		
		for(long i = 32,m = 1;i < 40;i++,m *= 256)
			ret[0] += deny[(int)i] < 0 ? deny[(int)i] ^ 0xFFFFFFFFFFFFFF00L : deny[(int)i] * m;
		
		return ret;
	}
	
	public static byte[] PackLeave(int players_leaving)
	{
		byte[] ret = new byte[1024];
		
		byte[] msg = "leaving".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		ret[32] = (byte)players_leaving;
		
		return ret;
	}
	
	/**
	 * Returns the number of players leaving.
	 */
	public static int[] UnpackLeave(byte[] leaving)
	{
		if(leaving == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(leaving[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = leaving[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("leaving"))
			return null;
		
		int[] ret = new int[1];
		ret[0] = leaving[32];
		
		return ret;
	}
	
	public static byte[] PackLeaveDuringGame(Player_Data[] players)
	{
		byte[] ret = new byte[1024];
		
		byte[] msg = "leave".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		ret[32] = (byte)players.length;
		
		for(int i = 0;i < players.length;i++)
		{
			msg = players[i].Name.getBytes();
			
			for(int j = 0;j < msg.length;j++)
				ret[j + 33 + 128 * i] = msg[j];
		}
		
		return ret;
	}
	
	/**
	 * Returns the names of the players leaving the game.
	 * If the player's name is null or of length zero it returns "".
	 */
	public static String[] UnpackLeaveDuringGame(byte[] leave)
	{
		if(leave == null)
			return null;
		
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(leave[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = leave[i];
		
		String ms = new String(msg);
		
		if(!ms.equals("leave"))
			return null;
		
		String[] ret = new String[(int)leave[32]];
		
		for(int j = 0;j < ret.length;j++)
		{
			msg = null;
		
			for(int i = 33 + 128 * j;i < 161 + 128 * j;i++)
				if(leave[i] == 0)
				{
					msg = new byte[i-33];
					i = 160;
				}
			
			if(msg == null)
				msg = new byte[128];
			
			for(int i = 0;i < msg.length;i++)
				msg[i] = leave[i + 33 + 128 * j];
			
			ret[j] = new String(msg);
			ret[j] = ((String)ret[j]).substring(0,((String)ret[j]).indexOf(0));
		}
		
		return ret;
	}
	
	public static byte[] PackPingResponse(Game_Status status, User player)
	{
		byte[] ret = new byte[1024];
		
		byte[] msg = "ping response".getBytes();
		
		for(int i = 0;i < msg.length;i++)
			ret[i] = msg[i];
		
		msg = status.name.getBytes();
		
		for(int i = 32;i < 32 + msg.length;i++)
			ret[i] = msg[i-32];
		
		ret[288] = (byte)status.players_left;
		ret[289] = status.status.equals("Ongoing") ? (byte)'o' : (status.status.equals("Waiting") ? (byte)'w' : (byte)'e');
		
		int temp = status.bet;
		
		for(int i = 290;i < 294;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		ret[294] = status.skip_empty_time ? (byte)1 : (byte)0;
		ret[295] = status.food ? (byte)1 : (byte)0;
		ret[607] = status.strict_time ? (byte)1 : (byte)0;
		
		msg = status.estimate_remaining_time.getBytes();
		
		for(int i = 296;i < 296 + msg.length;i++)
			ret[i] = msg[i-296];
		
		msg = status.address.getBytes();
		
		for(int i = 1013;i < 1013 + msg.length;i++)
			ret[i] = msg[i-1013];
		
		msg = (player.name == null ? new byte[0] : player.name.getBytes());
		
		for(int i = 311;i < 311 + msg.length;i++)
			ret[i] = msg[i-311];
		
		temp = player.wins;
		
		for(int i = 567;i < 571;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.losses;
		
		for(int i = 571;i < 575;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.money_games;
		
		for(int i = 575;i < 579;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.money_won;
		
		for(int i = 579;i < 583;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.money_lost;
		
		for(int i = 583;i < 587;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.money_bet;
		
		for(int i = 587;i < 591;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.pc_games;
		
		for(int i = 591;i < 595;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		temp = player.pc_wins;
		
		for(int i = 595;i < 599;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}

		long Ltemp = player.time;
		
		for(int i = 599;i < 607;i++)
		{
			ret[i] = (byte)(temp % 256);
			temp /= 256;
		}
		
		return ret;
	}
	
	/**
	 * Returns a String(Net Message Type), Game_Status, and User in that order.
	 */
	public static Object[] UnpackPingResponse(byte[] response)
	{
		if(response == null)
			return null;
		
		Object[] ret = new Object[3];
		byte[] msg = null;
		
		for(int i = 0;i < 32;i++)
			if(response[i] == 0)
			{
				msg = new byte[i];
				i = 31;
			}
		
		if(msg == null)
			msg = new byte[32];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = response[i];
		
		ret[0] = new String(msg);
		
		if(!ret[0].equals("ping response"))
			return null;
		
		Game_Status stats = new Game_Status();

		msg = null;
		
		for(int i = 32;i < 288;i++)
			if(response[i] == 0)
			{
				msg = new byte[i-32];
				i = 287;
			}
		
		if(msg == null)
			msg = new byte[256];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = response[i+32];
		
		stats.Players = null;
		stats.name = new String(msg);
		stats.players_left = (int)response[288];
		
		if(response[289] == 'w')
			stats.status = "Waiting";
		else if(response[289] == 'o')
			stats.status = "Ongoing";
		else
			stats.status = "Packet Error";
		
		msg = new byte[4];
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+290];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			stats.bet += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		stats.skip_empty_time = (response[294] == 0) ? false : true;
		stats.food = (response[295] == 0) ? false : true;
		stats.strict_time = (response[607] == 0) ? false : true;

		msg = null;
		
		for(int i = 296;i < 311;i++)
			if(response[i] == 0)
			{
				msg = new byte[i-296];
				i = 310;
			}
		
		if(msg == null)
			msg = new byte[15];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = response[i+296];
		
		stats.estimate_remaining_time = new String(msg);

		msg = null;
		
		for(int i = 1013;i < 1024;i++)
			if(response[i] == 0)
			{
				msg = new byte[i-1013];
				i = 1023;
			}
		
		if(msg == null)
			msg = new byte[11];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = response[i+1013];
		
		stats.address = new String(msg);
		
		ret[1] = stats;
		
		User player = new User();
		
		msg = null;
		
		for(int i = 311;i < 567;i++)
			if(response[i] == 0)
			{
				msg = new byte[i-311];
				i = 566;
			}
		
		if(msg == null)
			msg = new byte[256];
		
		for(int i = 0;i < msg.length;i++)
			msg[i] = response[i+311];
		
		player.name = new String(msg);
		
		msg = new byte[4];
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+567];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.wins += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+571];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.losses += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+575];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.money_games += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+579];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.money_won += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+583];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.money_lost += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+587];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.money_bet += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+591];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.pc_games += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+595];
		
		for(int i = 0, j = 1;i < 4;i++,j *= 256)
			player.pc_wins += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		msg = new byte[8];
		
		for(int i = 0;i < 4;i++)
			msg[i] = response[i+571];
		
		for(long i = 0, j = 1;i < 8;i++,j *= 256)
			player.time += (msg[(int)i] < 0 ? msg[(int)i] ^ 0xFFFFFF00 : msg[(int)i]) * j;
		
		ret[2] = player;
		
		return ret;
	}
	
	public static final byte ASSASINATION = 1;
	public static final byte DEATH_BLOW = 2;
	public static final byte ERROR = 3;
	public static final byte KNIFE = 4;
	public static final byte RULES_VIOLATION = 5;
	public static final byte SORCERY = 6;
	public static final byte STARVATION = 7;
}
