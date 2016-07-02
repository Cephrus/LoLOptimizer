package github.cephrus.optimizer.lol.info;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StatInfo
{
	public static enum DamageType
	{
		PHYSICAL,
		MAGIC,
		TRUE
	}
	
	public static enum StatType
	{
		CHAMPION,
		ITEM,
		OTHER
	}
	
	public Map<String, Object> stats = new HashMap<String, Object>();
	
	public StatInfo(StatType type, Object... stats)
	{
		this.type = type;
		
		Iterator<Object> iterator = Arrays.asList(stats).iterator();
		while(iterator.hasNext())
		{
			Object o = iterator.next();
			if(o instanceof String && iterator.hasNext())
			{
				String name = ((String)o).toLowerCase();
				Object stat = iterator.next();
				
				this.stats.put(name, stat);
			}
		}
	}

	public StatType type;
	
	public StatInfo addStat(String name, Object stat)
	{
		this.stats.put(name, stat);
		return this;
	}
	
	public Object getStat(String name)
	{
		return this.stats.get(name);
	}
}
