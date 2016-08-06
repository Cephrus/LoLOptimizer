package github.cephrus.optimizer.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import github.cephrus.optimizer.lol.info.Champion;

public class Project
{
	public Champion champion;
	public String name;
	public File saveDir;
	
	public void save()
	{
		if(saveDir == null) throw new IllegalArgumentException("Attempt to save project with null savedir");
		
		try(FileWriter fw = new FileWriter(saveDir))
		{
			JSONObject obj = new JSONObject();
			obj.put("name", name);
			obj.put("champion", champion.name);
			
			//TODO
			
			fw.write(obj.toString());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Project read(JSONObject obj)
	{
		Project project = new Project();
		project.name = obj.getString("name");
		project.champion = Champion.fromName(obj.getString("champion"));
		
		return project;
	}
}
