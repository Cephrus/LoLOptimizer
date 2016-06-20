package github.cephrus.optimizer.lol.info;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("resource")
public class APIHelper
{
	public static final File dataDir;

	private static final String apiKey = "[REDACTED]";

	public static void injectItemData(Item i)
	{

	}

	public static void updateChampionInformation()
	{
		int index = 0;
		try
		{
			File champDataDir = new File(dataDir + File.separator + "champions");
			if(!champDataDir.exists()) champDataDir.mkdirs();

			for(Champion champ : Champion.champions())
			{
				File file = new File(champDataDir + File.separator + champ.name +".json");
				JSONObject base;
				if(!file.exists())
				{
					InputStream stream = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" + champ.id + "?champData=skins,stats,tags&api_key=" + apiKey).openStream();
					Scanner scn = new Scanner(stream).useDelimiter("\\A");
					base = new JSONObject(scn.next());

					try(FileWriter fw = new FileWriter(file))
					{
						fw.write(base.toString());
					}

					stream.close();
					scn.close();
				}
				else
				{
					InputStream stream = file.toURI().toURL().openStream();
					Scanner scn = new Scanner(stream).useDelimiter("\\A");
					base = new JSONObject(scn.next());

					stream.close();
					scn.close();
				}

				JSONObject obj = base.getJSONObject("stats");
				StatInfo info = new StatInfo(StatInfo.StatType.CHAMPION);

				Iterator<String> statKeys = obj.keys();
				while(statKeys.hasNext())
				{
					String key = statKeys.next();
					info.addStat(key, obj.get(key));
				}

				champ.displayName = base.getString("name");

				JSONArray skins = base.getJSONArray("skins");
				champ.maxSkins = skins.length();

				JSONArray tags = base.getJSONArray("tags");
				champ.tag = (String)tags.get(0);

				index++;
			}
		}
		catch(Exception e) {e.printStackTrace();}

		System.out.println("Finished " + index + " of " + Champion.champions().length);
	}

	@Deprecated
	public static StatInfo getChampionInformation(String champName)
	{
		try
		{
			Champion champ = Champion.fromName(champName);
			InputStream stream = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion/" + champ.id + "?champData=skins,stats&api_key=" + apiKey).openStream();
			Scanner scn = new Scanner(stream).useDelimiter("\\A");
			JSONObject base = new JSONObject(scn.next());
			JSONObject obj = base.getJSONObject("stats");

			StatInfo info = new StatInfo(StatInfo.StatType.CHAMPION);

			Iterator<String> statKeys = obj.keys();
			while(statKeys.hasNext())
			{
				String key = statKeys.next();
				info.addStat(key, obj.get(key));
			}

			JSONArray skins = base.getJSONArray("skins");
			champ.maxSkins = skins.length();

			stream.close();
			scn.close();

			return info;
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}

	static
	{
		dataDir = new File(System.getProperty("user.home") + File.separator + "LoLOptimizer");
		if(!dataDir.exists()) dataDir.mkdir();

		try
		{
			File file = new File(dataDir + File.separator + "championids.json");
			if(!file.exists())
			{
				InputStream stream = new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?champData=keys&api_key=" + apiKey).openStream();
				Scanner scn = new Scanner(stream).useDelimiter("\\A");
				JSONObject data = new JSONObject(scn.next()).getJSONObject("keys");
				try(FileWriter fw = new FileWriter(file))
				{
					fw.write(data.toString());
				}

				stream.close();
				scn.close();
			}

			InputStream stream = file.toURI().toURL().openStream();
			Scanner scn = new Scanner(stream).useDelimiter("\\A");
			JSONObject championData = new JSONObject(scn.next());

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

			scn.close();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
