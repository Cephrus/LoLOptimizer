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
	
	@FXML
	private Pane mOptimizer;

	public void initialize(URL url, ResourceBundle bundl)
	{
		GuiPage pageAbout = new GuiPage("About", mPaneAbout);
		GuiPage pageInfo = new GuiPage("Champion Information", mChampInfo);
		GuiPage pageOptimizer = new GuiPage("Item Optimizer", mOptimizer);
		
		ObservableList list = FXCollections.observableArrayList();
		list.addAll(GuiPage.getNames());
	//	list.addAll("About", "Champion Information", "Item Optimizer");
		mListView.setItems(list);

		mListView.getSelectionModel().select(0);

		mListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2)
			{
				if(GuiPage.forName((String)arg1) != null && GuiPage.forName((String) arg2) != null)
				{
					GuiPage.forName((String)arg1).panel.setVisible(false);
					GuiPage.forName((String)arg2).panel.setVisible(true);
				}
			}
		});
	}
}
