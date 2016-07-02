package github.cephrus.optimizer.lol.info;

import org.json.JSONArray;
import org.json.JSONObject;

public final class SpellHandler
{
	public static Ability getAbility(Champion champ, int index)
	{
		JSONObject champion = APIHelper.getJSONFor(champ).getJSONObject("data").getJSONObject(champ.name);
		JSONArray spells = champion.getJSONArray("spells");
		
		JSONObject spell = spells.getJSONObject(index);
	//	System.out.println(spell.get("tooltip"));
		
		return new Ability(null, spell.getString("tooltip"), 0, false);
	}
	
	public static Ability getPassive(Champion champ)
	{
		return null;
	}
}
