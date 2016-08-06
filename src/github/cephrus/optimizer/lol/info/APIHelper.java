package github.cephrus.optimizer.lol.info;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import github.cephrus.optimizer.LoLOptimizer;
import github.cephrus.optimizer.lol.info.Item.Maps;

@SuppressWarnings("all")
public final class APIHelper
{
	public static final File dataDir;
	public static final String dataMod = File.separator + "data";
	public static final String imgDir = File.separator + "images";
	
	public static boolean queueUpdate = false;

	private static final String data = "https://ddragon.leagueoflegends.com/cdn/";
	private static String apiVer = "";
	
	private static Map<String, String> localizations = new HashMap<String, String>();
	
	public static void develInjectData(Item item)
	{
		JSONObject i = APIHelper.loadDataJSON("itemBeta").getJSONObject(item.id + "");
		//TODO: Download these fiels.
		item.name = i.getString("name");
		item.totalCost = i.getInt("totalcost");
		item.combineCost = i.getInt("combinecost");
		item.description = i.getString("description");
		
		if(i.has("from"))
		{
			JSONArray from = i.getJSONArray("from");
			Iterator iter = from.iterator();
			item.buildsFrom = new String[from.length()];
			item.hasDescendingItems = true;
			int counter = 0;
			while(iter.hasNext())
			{
				item.buildsFrom[counter] = (String)iter.next();
				counter++;
			}
		}

		if(!(item.id >= 1400 && item.id <= 1419) && item.id != 3706 && item.id != 3711 && item.id != 3715) 
		{
			item.maps.add(Maps.TWISTEDTREELINE);
			item.maps.add(Maps.HOWLINGABYSS);
		}
		item.maps.add(Maps.SUMMONERRIFT);
		
		if(item.id == 3008 || item.id == 3073 || item.id == 3007 || item.id == 3029) 
		{
			item.maps.clear();
			item.maps.add(Maps.CRYSTALSCAR);
		}
		
		if(item.id != 3004 && item.id != 3070 && item.id != 3003 && item.id != 3027)
			item.maps.add(Maps.CRYSTALSCAR);
		
		Iterator<Object> it1 = APIHelper.loadDataJSON("exclude").getJSONArray("3v3items").iterator();
		while(it1.hasNext())
		{
			if(item.id == ((Integer)it1.next()).intValue())
			{
				item.maps.clear();
				item.maps.add(Maps.TWISTEDTREELINE);
			}
		}
		
		item.maps.add(Maps.ALL);
		
		JSONArray ar = APIHelper.loadDataJSON("exclude").getJSONArray("exclude");
		Iterator<Object> it = ar.iterator();
		while(it.hasNext())
		{
			if(item.id == ((Integer)it.next()).intValue()) return;
		}
		
		Item.validItems.add(item);
	}
	
	public static void develCreateBaseItemJson()
	{
		try
		{
			File json = new File(dataDir + dataMod + File.separator + "itemBeta.json");
			JSONObject dataJson = new JSONObject();
			
			if(json.exists()) return;
			
			LoLOptimizer.logger.info("Caching Item Data");
			
			InputStream is = new URL("https://ddragon.leagueoflegends.com/cdn/" + apiVer + "/data/en_US/item.json").openStream();
			Scanner scn = new Scanner(is).useDelimiter("\\A");
			JSONObject items = new JSONObject(scn.next()).getJSONObject("data");
			
			for(String key : items.keySet())
			{
				JSONObject item = items.getJSONObject(key);
				JSONObject iw = new JSONObject();
				
				iw.put("name", item.getString("name"));
				iw.put("totalcost", item.getJSONObject("gold").get("total"));
				iw.put("combinecost", item.getJSONObject("gold").get("base"));
				iw.put("sell", item.getJSONObject("gold").get("sell"));
				iw.put("tags", item.getJSONArray("tags"));
				
				String dsc[] = item.getString("description").replace("<br>", "\n").split("<|\\>");
				String description = "";
				for(String s : dsc)
				{
					if(s.equals("unique") || s.equals("stats") || s.equals("active") || s.equals("passive")
							|| s.equals("/unique") || s.equals("/stats") || s.equals("/active") || s.equals("/passive")
							|| s.startsWith("font color=") || s.equals("/font")) 
								continue;
					
					description += s;
				}
				
				iw.put("description", description);
				
				JSONObject stats = item.getJSONObject("stats");
				JSONArray effect = new JSONArray();
				for(String s : stats.keySet())
				{
					JSONObject j = new JSONObject();
					j.put("link", s);
					j.put("var", stats.get(s));
					
					effect.put(j);
				}
				
				iw.put("stats", effect);
				iw.put("maps", item.getJSONObject("maps"));
				
				if(item.has("from")) iw.put("from", item.getJSONArray("from"));
				
				/*String[] uniquePassives = item.getString("description").replace("<br>", "<br>").split("<unique|\\<br>");
				JSONArray passives = new JSONArray();
				for(String s : uniquePassives)
				{	
					if(s.startsWith(">"))
					{
						//TODO
					}
				}*/
				
				dataJson.put(key, iw);
			}
			
			try(FileWriter fw = new FileWriter(json))
			{
				json.delete();
				fw.write(dataJson.toString());
			}
			
			scn.close();
			is.close();
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public static void injectItemData(Item item)
	{
		try
		{
			
			//TODO: Download items.json
			InputStream is = new File(dataDir + dataMod + File.separator + "items.json").toURI().toURL().openStream();
			Scanner scn = new Scanner(is).useDelimiter("\\A");
			
			//NOTE: item health stat marked as "health", not to be confused with base health from champions.
			JSONObject i = new JSONObject(scn.next()).getJSONObject("" + item.id);
			item.name = i.getString("name");
			item.totalCost = i.getInt("totalcost");
			item.combineCost = i.getInt("combinecost");
			item.description = i.getString("description");
			
			Item.validItems.add(item);
		}
		catch(JSONException e)
		{
		//	IllegalArgumentException ex = new IllegalArgumentException("Item " + item.id + " does not have an entry in Items.json.\n"
		//			+ "Please update data to refresh the file.");
		//	throw ex;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads json in the data folder
	 * @param jsonName
	 * @return
	 */
	public static JSONObject loadDataJSON(String jsonName)
	{
		try
		{
			InputStream is = new File(dataDir + dataMod + File.separator + jsonName + ".json").toURI().toURL().openStream();
			Scanner scn = new Scanner(is).useDelimiter("\\A");
			
			return new JSONObject(scn.next());
		}
		catch(Exception e) {;}
		
		return null;
	}
	
	public static String getLocalization(String key)
	{
		String rtn = localizations.get(key);
		if(rtn != null) return rtn;
		
		return key;
	}
	
	public static JSONObject getJSONFor(Champion champ)
	{
		try
		{
			InputStream stream = new File(dataDir + dataMod + File.separator +  "champions" + 
					File.separator + champ.name + ".json").toURI().toURL().openStream();
			Scanner scn = new Scanner(stream).useDelimiter("\\A");
			JSONObject obj = new JSONObject(scn.next());
			
			scn.close();
			stream.close();
			
			if(obj != null) return obj;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void registerLocalization(String key, String localization)
	{
		localizations.put(key, localization);
	}
	
	private static String downloadImage(String url, File out)
	{
		try(InputStream i = new URL(url).openStream(); ByteArrayOutputStream o = new ByteArrayOutputStream())
		{
			byte[] buffer = new byte[4096];
			int bit = 0;
			while((bit = i.read(buffer)) != -1)
			{
				o.write(buffer, 0, bit);
			}
			
			FileOutputStream stm = new FileOutputStream(out);
			stm.write(o.toByteArray());
			return "file:" + out.getAbsolutePath();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "failed:uhe";
		}
	}
	
	public static String getFullSplash(Champion champ, int skin)
	{
		String img = champ.name + "_" + skin + ".jpg";
		String rtn = "http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + img;
		File image = new File(dataDir + imgDir + File.separator + "splash" + File.separator + img);
		
		if(!image.exists())
		{
			rtn = downloadImage(rtn, image);
			if(rtn.equals("failed:uhe"))
			{
				File imgdir = new File(dataDir + imgDir + File.separator + "splash");
				int rand = new Random().nextInt(imgdir.listFiles().length);
				
				return "file:" + imgdir.listFiles()[rand].getAbsolutePath();
			}
		}
		
		rtn = "file:" + image.getAbsolutePath();
		return rtn;
	}
	
	public static String getChampIcon(Champion champ)
	{
		String img = champ.name + ".png";
		String rtn = "http://ddragon.leagueoflegends.com/cdn/" + apiVer + "/img/champion/" + img;
		File image = new File(dataDir + imgDir + File.separator + "portrait" + File.separator + img);
		
		if(!image.exists())
		{
			downloadImage(rtn, image);
		}

		rtn = "file:" + image.getAbsolutePath();
		return rtn;
	}
	
	public static String getSplash(Champion champ, int skin)
	{
		String img = champ.name + "_" + skin + ".jpg";
		String rtn = "http://ddragon.leagueoflegends.com/cdn/img/champion/loading/" + img;
		File image = new File(dataDir + imgDir + File.separator + "border" + File.separator + img);
		
		if(!image.exists())
		{
			downloadImage(rtn, image);
		}
		
		rtn = "file:" + image.getAbsolutePath();
		return rtn;
	}

	public static String getItemIcon(Item item)
	{
		String img = item.id + ".png";
		String rtn = "http://ddragon.leagueoflegends.com/cdn/" + apiVer + "/img/item/" + img;
		File image = new File(dataDir + imgDir + File.separator + "item" + File.separator + img);
		
		if(!image.exists())
			downloadImage(rtn, image);
		
		rtn = "file:" + image.getAbsolutePath();
		return rtn;
	}
	
	public static void updateChampionInformation()
	{
		int index = 0;
		try
		{
			File champDataDir = new File(dataDir + dataMod + File.separator + "champions");
			if(!champDataDir.exists()) champDataDir.mkdirs();

			for(Champion champ : Champion.champions())
			{
				File file = new File(champDataDir + File.separator + champ.name +".json");
				JSONObject base;
				if(!file.exists())
				{
					InputStream stream = new URL(data + apiVer + "/data/en_US/champion/" + champ.name + ".json").openStream();
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

				
				JSONObject obj = base.getJSONObject("data").getJSONObject(champ.name).getJSONObject("stats");
				StatInfo info = new StatInfo(StatInfo.StatType.CHAMPION);

				Iterator<String> statKeys = obj.keys();
				while(statKeys.hasNext())
				{
					String key = statKeys.next();
					info.addStat(key, obj.get(key));
				}
				
				champ.info = info;

				JSONObject ch = base.getJSONObject("data").getJSONObject(champ.name);
				champ.displayName = ch.getString("name");
				
				champ.resource = ch.getString("partype");
				
				/** Exception for Vlad thanks to spaghetti code. */
				if(champ.name.equalsIgnoreCase("vladimir")) champ.resource = "man.vladfury";

				JSONArray skins = ch.getJSONArray("skins");
				champ.maxSkins = skins.length();

				JSONArray tags = ch.getJSONArray("tags");
				champ.tag = (String)tags.get(0);
				
				getSplash(champ, 0);
				getChampIcon(champ);

				index++;
			}
		}
		catch(Exception e) {e.printStackTrace();}

		LoLOptimizer.logger.info("Finished " + index + " of " + Champion.champions().length);
	}

	public static void setVersion()
	{
		File versionFile = new File(dataDir + File.separator + "apiver");
		
		try
		{
			InputStream ver = new URL("https://ddragon.leagueoflegends.com/api/versions.json").openStream();
			Scanner scan = new Scanner(ver).useDelimiter("\\A");
			JSONArray versions = new JSONArray(scan.next());
			String version = versions.getString(0);
			
			if(versionFile.exists())
			{
				Scanner fileScn = new Scanner(versionFile.toURI().toURL().openStream()).useDelimiter("\\A");
				String fileVer = fileScn.next();
			
				if(!fileVer.equals(version))
				{
					APIHelper.queueUpdate = true;
					versionFile.delete();
					try(FileWriter fw = new FileWriter(versionFile))
					{
						fw.write(version);
					}
				}
					
				APIHelper.apiVer = fileVer;
			}
			else
			{
				APIHelper.queueUpdate = true;
				try(FileWriter fw = new FileWriter(versionFile))
				{
					fw.write(version);
				}
					
				APIHelper.apiVer = version;
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public static boolean checkNewVersion() throws IOException
	{
		InputStream stream = new URL("https://cephrus.github.io/optimizerdata/ver").openStream();
		Scanner scn = new Scanner(stream).useDelimiter("\\A");
		String ver = scn.next().trim();
		
		if(!ver.equals(LoLOptimizer.version))
			LoLOptimizer.logger.info("There is a new version available. It is a good idea to get it.");
		
		return !ver.equals(LoLOptimizer.version);
	}

	public static void loadLanguage() throws MalformedURLException, IOException
	{
		File localization = new File(dataDir + dataMod + File.separator + "lang.json");
		
		if(checkNewVersion() && !LoLOptimizer.debug) localization.delete();
		
		if(!localization.exists())
		{
			InputStream streem = new URL("https://cephrus.github.io/optimizerdata/lang.json").openStream();
			Scanner scn = new Scanner(streem).useDelimiter("\\A");
			JSONObject o = new JSONObject(scn.next());
			try(FileWriter f = new FileWriter(localization))
			{
				f.write(o.toString());
			}
		}
		
		InputStream s = localization.toURI().toURL().openStream();
		Scanner scn = new Scanner(s).useDelimiter("\\A");
		JSONObject obj = new JSONObject(scn.next());
		
		Iterator<String> it = obj.keys();
		while(it.hasNext())
		{
			String k = it.next();
			localizations.put(k, obj.getString(k));
		}
		
		s.close();
		scn.close();
	}
	
	static
	{
		dataDir = new File(System.getProperty("user.home") + File.separator + "LoLOptimizer");
		if(!dataDir.exists()) dataDir.mkdir();
		
		File f = new File(dataDir + dataMod);
		if(!f.exists()) f.mkdirs();
		
		File i1 = new File(dataDir + imgDir);
		if(!i1.exists()) i1.mkdirs();
		new File(i1 + File.separator + "splash").mkdirs();
		new File(i1 + File.separator + "portrait").mkdirs();
		new File(i1 + File.separator + "border").mkdirs();
		new File(i1 + File.separator + "item").mkdirs();
		
		try
		{
			setVersion();
			loadLanguage();
		}
		catch(Exception e) {e.printStackTrace();}

		try
		{
			if(queueUpdate && !LoLOptimizer.debug)
			{
				LoLOptimizer.logger.info("Updating champion data.");
				new File(dataDir + dataMod + File.separator + "championids.json").delete();
				for(File champFile : new File(dataDir + dataMod + File.separator + "champions").listFiles())
				{
					champFile.delete();
				}
				
				queueUpdate = false;
			}
			
			File file = new File(dataDir + dataMod + File.separator + "championids.json");
			if(!file.exists())
			{
				InputStream stream = new URL(data + apiVer + "/data/en_US/champion.json").openStream();
				Scanner scn = new Scanner(stream).useDelimiter("\\A");
				JSONObject data = new JSONObject(scn.next()).getJSONObject("data");
				JSONObject write = new JSONObject();
				
				Iterator<String> it = data.keys();
				while(it.hasNext())
				{
					String key = it.next();
					String id = data.getJSONObject(key).getString("key");
					
					write.put(id, key);
				}
				
				try(FileWriter fw = new FileWriter(file))
				{
					fw.write(write.toString());
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

	public static String getOldChampIcon(Champion champ, String apiVersion)
	{
		String img = champ.name + ".png";
		String rtn = "http://ddragon.leagueoflegends.com/cdn/" + apiVersion + "/img/champion/" + img;
		File image = new File(dataDir + imgDir + File.separator + "portrait" + File.separator + champ.name + "_" + apiVersion + ".png");
		
		if(!image.exists())
		{
			downloadImage(rtn, image);
		}

		rtn = "file:" + image.getAbsolutePath();
		return rtn;
	}
}
