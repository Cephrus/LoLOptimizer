package github.cephrus.optimizer;

import github.cephrus.optimizer.gui.GuiLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoLOptimizer extends Application
{
	public static void main(String[] args)
	{
		launch(args);
		/*
		 * try { UIManager.put("nimbusBase", Color.DARK_GRAY);
		 * 
		 * for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		 * if("Nimbus".equals(info.getName())) {
		 * UIManager.setLookAndFeel(info.getClassName()); break; } } }
		 * catch(Exception e) {e.printStackTrace();}
		 * 
		 * JFrame frame = new JFrame("Nimbus");
		 * frame.setDefaultCloseOperation(3); frame.setPreferredSize(new
		 * Dimension(600, 800)); frame.setVisible(true);
		 * 
		 * frame.pack();
		 */
	}

	@Override
	public void start(Stage primaryStage)
	{
		new GuiLoader();
		try
		{
			Parent pane = (Parent) GuiLoader.loadGui("Main.fxml");
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
