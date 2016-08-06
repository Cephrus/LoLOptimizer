package github.cephrus.optimizer.lol.info;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public final class SpellHandler
{
	private static boolean initialized = false;
	
	public static boolean initialize()
	{
		if(initialized) return false;
		
		return true;
	}
	
	public static Ability getAbility(Champion champ, int index)
	{
		JSONObject champion = APIHelper.getJSONFor(champ).getJSONObject("data").getJSONObject(champ.name);
		JSONArray spells = champion.getJSONArray("spells");
		
		JSONObject spell = spells.getJSONObject(index);
		JSONArray cooldown = spell.getJSONArray("cooldown");
		
		StringBuilder cd = new StringBuilder();
		Iterator<Object> it = cooldown.iterator();
		while(it.hasNext())
		{
			Object i = it.next();
			
			if(i instanceof Double) cd.append(((Double)i).doubleValue());
			else cd.append(((Integer)i).doubleValue());
			
			if(it.hasNext()) cd.append("/");
		}
		
		return new Ability(spell.getString("name"), spell.getString("tooltip"), cd.toString(), spell.getString("costBurn"));
	}
	
	public static Ability getPassive(Champion champ)
	{
		JSONObject champion = APIHelper.getJSONFor(champ).getJSONObject("data").getJSONObject(champ.name);
		JSONObject passive = champion.getJSONObject("passive");
		
		return new Ability(passive.getString("name"), passive.getString("description"), true);
	}
}
