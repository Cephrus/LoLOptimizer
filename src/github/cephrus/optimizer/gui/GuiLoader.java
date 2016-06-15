package github.cephrus.optimizer.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class GuiLoader
{
	private static GuiLoader instance;

	public GuiLoader()
	{
		instance = this;
	}

	public static Object loadGui(String name)
	{
		try
		{
			return FXMLLoader.load(instance.getClass().getResource(name));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
