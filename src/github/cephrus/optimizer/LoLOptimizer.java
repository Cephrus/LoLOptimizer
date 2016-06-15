package github.cephrus.optimizer;

import github.cephrus.optimizer.gui.GuiLoader;
import github.cephrus.optimizer.gui.GuiMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoLOptimizer extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		new GuiLoader();
		try
		{
			AnchorPane pane = FXMLLoader.load(GuiMain.class.getResource("Main.fxml"));
			Scene scene = new Scene(pane);
			primaryStage.setTitle("LoL Item Optimizer");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
