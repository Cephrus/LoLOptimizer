package github.cephrus.optimizer.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;

public class GuiPage
{
	public static final List<GuiPage> mainPages = new ArrayList<GuiPage>();
	
	public String name;
	public Pane panel;
	
	public static GuiPage forName(String name)
	{
		for(GuiPage page : mainPages)
		{
			if(page.name.equals(name))
				return page;
		}
		
		return null;
	}
	
	public static List<String> getNames()
	{
		List<String> list = new ArrayList<String>();
		
		for(GuiPage page : mainPages)
		{
			list.add(page.name);
		}
		
		return list;
	}
	
	public GuiPage(String pageName, Pane page)
	{
		this.name = pageName;
		this.panel = page;
		mainPages.add(this);
	}
}
