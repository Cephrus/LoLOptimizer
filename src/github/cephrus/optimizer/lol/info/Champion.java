package github.cephrus.optimizer.lol.info;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Champion
{
	private static final Map<String, Champion> champByName = new HashMap<String, Champion>();
	
	public static final List<String> tags = Arrays.asList("assassin", "fighter", "tank", "mage", "marksman", "support");
	
	public Champion(String name)
	{
		this.name = name;
		champByName.put(name, this);
		frm = new DecimalFormat("##.00");
	}
	
	public Champion(String name, int id)
	{
		this(name);
		this.id = id;
	}
	
	public String name;
	public String displayName;
	public String tag;
	public int id;
	public StatInfo info;
	public int maxSkins;
	public String resource;
	
	private DecimalFormat frm;
	
	public static Champion[] byAlpha()
	{
		Champion[] champs = champions();
		String[] names = new String[champs.length];
		for(int i = 0; i < champs.length; i++)
		{
			names[i] = champs[i].name;
		}
		Arrays.sort(names);
		Champion[] retn = new Champion[champs.length];
		
		for(int i = 0; i < champs.length; i++)
		{
			retn[i] = Champion.fromName(names[i]);
		}
		
		return retn;
	}
	
	public static Champion[] champions()
	{
		return (Champion[])champByName.values().toArray(new Champion[champByName.size()]);
	}
	
	public static Champion random()
	{
		Random rand = new Random();
		if(champByName.values().toArray().length < 1) return null;
		return ((Champion)champByName.values().toArray()[rand.nextInt(champByName.values().toArray().length)]);
	}
	
	public int randomSkin()
	{
		Random rand = new Random();
		return rand.nextInt(maxSkins);
	}
	
	public Champion setInfo(StatInfo stats)
	{	
		this.info = stats;
		return this;
	}
	
	public static Champion fromName(String championName)
	{
		return champByName.get(championName);
	}
	
	@Deprecated
	public int getIntFromObject(Object o)
	{
		if(o instanceof Double) return ((Double)o).intValue();
		else return ((Integer)o).intValue();
	}
	
	public double getDoubleFromObject(Object o)
	{
		if(o instanceof Double) return ((Double)o).doubleValue();
		else return ((Integer)o).doubleValue();
	}
	/**
	 * http://na.leagueoflegends.com/en/news/game-updates/patch/patch-420-notes#patch-stats-gained-per-level
	 * http://boards.na.leagueoflegends.com/en/c/gameplay-balance/i1tQmvMX-the-true-growth-stat-formula-you-cant-seem-to-find-anywhere-else
	 */
	public String calculateStat(Object statName, Object growthName, String lvl, boolean round)
	{
		double stat = getDoubleFromObject(statName), growth = getDoubleFromObject(growthName);
		
		if(!lvl.equals("1-18"))
		{
			int level = Integer.parseInt(lvl);
			double statAtL = stat + .65 * growth * (level - 1) + .035 * growth * (1.5 + ((0.5 * level) - 0.5)) * (level-1);
			return "" + (round ? (int)Math.round(statAtL) : frm.format(statAtL));
		}
		else
		{
			double statAt18 = stat + .65 * growth * (17) + .035 * growth * (1.5 + ((0.5 * 18) - 0.5)) * (17);
			if(round) statAt18 = Math.round(statAt18);
			if(round) stat = Math.round(stat);
			
			if(!round) return stat == statAt18 ? frm.format(stat) : frm.format(stat) + "-" + frm.format(statAt18);
			else return stat == statAt18 ? "" + (int)stat : (int)stat + "-" + (int)statAt18;
		}
	}
	
	public String getBaseAttackDamage(String lvl)
	{
		return this.calculateStat(info.getStat("attackdamage"), info.getStat("attackdamageperlevel"), lvl, true);
	}
	
	public String getRange()
	{
		return "" + (int)Math.ceil(getDoubleFromObject(info.getStat("attackrange")));
	}
	
	public String getRegen(String lvl)
	{
		return this.calculateStat(info.getStat("hpregen"), info.getStat("hpregenperlevel"), lvl, false);
	}
	
	public String getHealth(String lvl)
	{
		return this.calculateStat(info.getStat("hp"), info.getStat("hpperlevel"), lvl, true);
	}

	public String getResource(String lvl)
	{	
		double resource;
		Object rsc = info.getStat("mp");
	
		resource = getDoubleFromObject(rsc);
	
		if(resource == 0.0) return "";
		return this.calculateStat(info.getStat("mp"), info.getStat("mpperlevel"), lvl, true);
	}

	public String getResourceGen(String lvl)
	{
		double resource;
		Object rsc = info.getStat("mpregen");
		
		resource = getDoubleFromObject(rsc);
		
		if(resource == 0.0) return "";
		return this.calculateStat(info.getStat("mpregen"), info.getStat("mpregenperlevel"), lvl, false);
	}
	
	public String getAttackSpeed(String lvl)
	{
		double base, perlev;
		Object as = info.getStat("attackspeedoffset"), pl = info.getStat("attackspeedperlevel");
		base = 0.625 / (1 + getDoubleFromObject(as));
		perlev = getDoubleFromObject(pl);
		
		int level = lvl.equals("1-18") ? 18 : Integer.parseInt(lvl) ;
		double bonusAtLevel = .65 * perlev * (level - 1) + .035 * perlev * (1.5 + ((0.5 * level) - 0.5)) * (level-1);
		return new DecimalFormat("0.000").format(base) + " " + (!lvl.equals("1-18") ? "(+" + Math.round(bonusAtLevel) + "%)" 
				: "(+0%-" + Math.round(bonusAtLevel) + "%)");
	}
	
	public String getArmor(String lvl)
	{
		return this.calculateStat(info.getStat("armor"), info.getStat("armorperlevel"), lvl, true);
	}
	
	public String getMS()
	{
		return (int)getDoubleFromObject(info.getStat("movespeed")) + "";
	}
	
	public String getMagicResist(String lvl)
	{
		return this.calculateStat(info.getStat("spellblock"), info.getStat("spellblockperlevel"), lvl, true);
	}
}
