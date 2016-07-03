package github.cephrus.optimizer.lol.info;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import github.cephrus.optimizer.LoLOptimizer;

@SuppressWarnings("resource")
public final class APIHelper
{
	public static final File dataDir;
	public static final String dataMod = File.separator + "data";
	public static final String imgDir = File.separator + "images";
	
	public static boolean queueUpdate = false;

	private static final String data = "https://ddragon.leagueoflegends.com/cdn/";
	private static String apiVer = "";
	
	private static Map<String, String> localizations = new HashMap<String, String>();
	
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

	public static void injectItemData(Item i)
	{

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
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return "failed:uhe";
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
			return "failed:uhe";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return url;
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
		String rtn = "http://ddragon.leagueoflegends.com/cdn/6.12.1/img/champion/" + img;
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
		
		if(checkNewVersion()) localization.delete();
		
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

		try
		{
			setVersion();
			loadLanguage();
			
			if(queueUpdate)
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
}
