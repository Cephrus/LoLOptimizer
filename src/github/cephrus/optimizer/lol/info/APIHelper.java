package github.cephrus.optimizer.lol.info;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

@SuppressWarnings("resource")
public class APIHelper
{	
	private static final String apiKey = "[REDACTED]";
	
	public static StatInfo getChampionInformation(String champName)
	{
		try
		{
			Champion champ = Champion.fromName(champName);
			InputStream stream = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" + champ.id + "?champData=stats&api_key=" + apiKey).openStream();
			Scanner scn = new Scanner(stream).useDelimiter("\\A");
			JSONObject obj = new JSONObject(scn.next()).getJSONObject("stats");
			
			StatInfo info = new StatInfo(StatInfo.StatType.CHAMPION);
			
			Iterator<String> statKeys = obj.keys();
			while(statKeys.hasNext())
			{
				String key = statKeys.next();
				info.addStat(key, obj.get(key));
				
				System.out.println(key + ":" + obj.get(key));
			}
			
			stream.close();
			scn.close();
			
			return info;
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}

	
	static
	{
		try
		{
			InputStream stream = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?champData=all&api_key=" + apiKey).openStream();

			Scanner scn = new Scanner(stream).useDelimiter("\\A");
			JSONObject championData = new JSONObject(scn.next()).getJSONObject("keys");
			
			int offset = championData.length();
			for(int i = 1; i < offset; i++)
			{
				if(!championData.has("" + i))
				{
					offset++;
					continue;
				}
				
				String s = (String) championData.get("" + i);
				new Champion(s, i);
			}
			
			stream.close();
			scn.close();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		APIHelper.getChampionInformation("Kalista");
	}
}
