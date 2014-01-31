//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////// Omacron Games ///////////////////////////////
//////////////////////////////////////////////////////////////////////////
///////////////////// This code is not open source ///////////////////////
/////////// If you are reading it you better have permission /////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
package Reader;

import java.io.*;
import java.net.*;
import java.util.*;

public class Shutdown extends Thread
{
	/**
	 * Shutdown is the shutdown hook
	 */
	public Shutdown(Object obj)
	{
		this.obj = obj;
		
		return;
	}
	
	/**
	 * Provides final management for the object.
	 * @return void
	 */
	public void finalize()
	{
		return;
	}
	
	/**
	 * The overridden function run of Thread.
	 * This is essentially the main function of the new thread.
	 * @return void
	 */
	public void run()
	{
		((Reader)obj).finalize();
		
		return;
	}
	
	protected Object obj;
}