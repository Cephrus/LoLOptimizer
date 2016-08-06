package github.cephrus.optimizer.gui;

import java.util.ArrayList;
import java.util.List;

import github.cephrus.optimizer.lol.info.APIHelper;
import github.cephrus.optimizer.lol.info.Champion;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class GuiPage
{
	public static final List<GuiPage> mainPages = new ArrayList<GuiPage>();
	
	public String name;
	public Pane panel;
	public Image image;
	public boolean visible = true;
	public Tooltip tooltip;
	
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
	
	public Image getIcon()
	{
		return image;
	}
	
	public GuiPage(String pageName, Pane page, Image icon)
	{
		this(pageName, page);
		this.image = icon;
	}
	
	public GuiPage(String pageName, Pane page)
	{
		this.name = pageName;
		this.panel = page;
		this.image = new Image(APIHelper.getChampIcon(Champion.random()));
		
		panel.setVisible(false);
		mainPages.add(this);
	}
	
	public GuiPage(String pageName, Pane page, Image icon, Tooltip tip)
	{
		this(pageName, page, icon);
		this.tooltip = tip;
	}
}
