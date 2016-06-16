package github.cephrus.optimizer.lol.info;

import java.util.HashMap;
import java.util.Map;

public class Item
{
	protected static final Map<String, Item> itemByName = new HashMap<String, Item>();
	
	public int id;
	public String name;
	public StatInfo info;
	
	public Item(String name)
	{
		
	}
	
	public static Item fromName(String name)
	{
		return itemByName.get(name);
	}
}
