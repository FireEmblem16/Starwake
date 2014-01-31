package app;

import java.awt.event.KeyEvent;
import java.util.Hashtable;

/**
 * Maps strings to key codes. 
 */
public class KeyBindings
{
	/**
	 * Initializes this key binding object to have no bindings.
	 */
	public KeyBindings()
	{
		binds = new Hashtable<String,Integer>();
		codes = new Hashtable<Integer,String>();
		
		return;
	}
	
	/**
	 * Returns the name of the lastest binding on the given key code.
	 */
	public String GetLatestNameOnCode(Integer code)
	{
		return codes.get(code);
	}
	
	/**
	 * Adds a key binding if there is not a key bound to [bind] yet.
	 */
	public boolean AddBinding(String bind, Integer code)
	{
		Integer i = binds.get(bind);
		
		if(i != null)
			return false;
		
		binds.put(bind,code);
		
		codes.remove(code);
		codes.put(code,bind);
		
		return true;
	}
	
	/**
	 * Removes the bind known by [bind] if it exists.
	 */
	public void RemoveBinding(String bind)
	{
		codes.remove(binds.get(bind));
		binds.remove(bind);
		
		return;
	}
	
	/**
	 * Changes a key binding to [bind] or just adds it if there isn't a binding for [bind] yet.
	 */
	public boolean ChangeBinding(String bind, Integer code)
	{
		codes.put(code,bind);
		binds.put(bind,code);
		
		return true;
	}
	
	/**
	 * Returns the key code bound to [bind] and null if there isn't one.
	 */
	public Integer GetBinding(String bind)
	{
		return binds.get(bind);
	}
	
	/***
	 * Contains every binding that this object needs be concerned with.
	 */
	protected Hashtable<String,Integer> binds;
	
	/***
	 * This will allow us to look up key bindings by keycode.
	 */
	protected Hashtable<Integer,String> codes;
}
