package github.cephrus.optimizer;

import github.cephrus.optimizer.gui.GuiMain;
import github.cephrus.optimizer.lol.info.APIHelper;
import github.cephrus.optimizer.lol.info.Champion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoLOptimizer extends Application
{
	public static BackgroundImage splash;
	public static AnchorPane base;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		new APIHelper();
		try
		{
			Champion splash = Champion.random();
			if(splash != null) APIHelper.getChampionInformation(splash.name);
			
			if(splash != null) LoLOptimizer.splash = new BackgroundImage(new Image("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + splash.name + "_" + splash.randomSkin() + ".jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			AnchorPane pane = FXMLLoader.load(GuiMain.class.getResource("Main.fxml"));
			
			if(splash != null)pane.setBackground(new Background(LoLOptimizer.splash));
			else pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
			
			base = pane;
			
			Scene scene = new Scene(pane);
			primaryStage.setTitle("LoL Item Optimizer");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(false);
			primaryStage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
