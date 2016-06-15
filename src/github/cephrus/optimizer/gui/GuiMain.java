package github.cephrus.optimizer.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

@SuppressWarnings("all")
public class GuiMain implements Initializable
{
	@FXML
	private ListView<?> mListView;
	
	@FXML
	private Pane mPaneAbout;
	
	@FXML
	private Pane mChampInfo;
	
	public void initialize(URL url, ResourceBundle bundl)
	{
		ObservableList list = FXCollections.observableArrayList();
		list.addAll("Start", "Champion Information", "Item Optimizer");
		mListView.setItems(list);
		
		mListView.getSelectionModel().select(0);
		
		mListView.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener()
		{
			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2)
			{
				if(arg1.equals("Start")) GuiMain.this.mPaneAbout.setVisible(false);
				else if(arg1.equals("Champion Information")) GuiMain.this.mChampInfo.setVisible(false);
				
				if(arg2.equals("Start")) GuiMain.this.mPaneAbout.setVisible(true);
				else if(arg2.equals("Champion Information")) GuiMain.this.mChampInfo.setVisible(true);
			}
		});
	}
}
