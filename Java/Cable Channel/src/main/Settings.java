package main;

public class Settings
{
	public Settings()
	{
		pre_load = 3;
		prep_days = 7;
		special_lag = 1000;
		lag = 1000;
		weight_op_1 = FILES_IN_FOLDER;
		weight_op_2 = PLAY_IN_ORDER;
		index = ".\\index.html";
		location = ".\\settings";
		mpc_loc = ".\\mplayerc.exe";
		root = "http://localhost";
		monitor = 1;
		
		return;
	}
	
	public String get_weight_op_1_name()
	{
		switch(weight_op_1)
		{
		case FILES_IN_FOLDER:
			return "Will select directories based on the number of files in them.";
		case FOLDERS_IN_FOLDER:
			return "Will select directories based on the number of directories in them.";
		case BOTH:
			return "Will select directories based on the sum of the number of files and directories in them.";
		case TOTAL_RANDOM:
			return "Will select directories at random.";
		}

		return null;
	}
	
	public String get_weight_op_2_name()
	{
		switch(weight_op_2)
		{
		case PLAY_MOST_OFTEN_PLAYED:
			return "Will play videos most often played.";
		case PLAY_LEAST_OFTEN_PLAYED:
			return "Will play videos least often played.";
		case PLAY_IN_ORDER:
			return "Will play videos in order.";
		case PLAY_RANDOM:
			return "Will play videos at random.";
		}
		
		return null;
	}
	
	public String toString()
	{
		String ret = pre_load + "\n";
		
		ret += prep_days + "\n";
		ret += special_lag + "\n";
		ret += weight_op_1 + "\n";
		ret += weight_op_2 + "\n";
		ret += monitor + "\n";
		ret += index + "\n";
		ret += location + "\n";
		ret += mpc_loc + "\n";
		ret += root;
		
		return ret;
	}
	
	public int lag;
	public int monitor;
	public int pre_load;
	public int prep_days;
	public int special_lag;
	public int weight_op_1;
	public int weight_op_2;
	public String index;
	public String location;
	public String mpc_loc;
	public String root;
	
	public static final int FILES_IN_FOLDER = 0;
	public static final int FOLDERS_IN_FOLDER = 1;
	public static final int BOTH = 2;
	public static final int TOTAL_RANDOM = 3;
	
	public static final int PLAY_MOST_OFTEN_PLAYED = 0;
	public static final int PLAY_LEAST_OFTEN_PLAYED = 1;
	public static final int PLAY_IN_ORDER = 2;
	public static final int PLAY_RANDOM = 3;
}
