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
				
				/*switch(name)
				{
				case "basead": baseAd = (int)stat; break;
				case "totalad": totalAd = (int)stat; break;
				case "totalap": totalAp = (int)stat; break;
				case "health": health = (int)stat; break;
				case "regen": regen = (int)stat; break;
				case "armor": armor = (int)stat; break;
				case "armpen": armPen = (int)stat; break;
				case "magres": magRes = (int)stat; break;
				case "mrpen": mrPen = (int)stat; break;
				case "lifesteal": lifeStealOver100 = (int)stat; break;
				case "cdr": cdrOver100 = (int)stat; break;
				case "attkspeed": attackSpeed = (double)stat; break;
				case "critical": critChanceOver100 = (int)stat; break;
				case "resource": resourceName = (String)stat; break;
				case "resourceVal": resource = (int)stat; break;
				case "manaRegen": manaRegen = (int)stat; break;
				case "spellvamp": spellVampOver100 = (int)stat; break;
				}*/
			}
		}
	}

	public StatType type;
	
	/*public int baseAd;
	public int totalAd;
	public int critChanceOver100;
	public double attackSpeed;
	public int armPen;

	public int totalAp;
	public int cdrOver100;
	public int mrPen;
	public String resourceName;
	public int resource;
	public int manaRegen;
	public int spellVampOver100;
	
	public int health;
	public int regen;
	public int armor;
	public int magRes;
	
	public int lifeStealOver100;*/
	
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
