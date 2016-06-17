package github.cephrus.optimizer.gui;

import java.net.URL;
import java.util.ResourceBundle;

import github.cephrus.optimizer.LoLOptimizer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

@SuppressWarnings("all")
public class GuiMain implements Initializable
{
	@FXML
	private ListView<GuiPage> mListView;

	@FXML
	private Pane mPaneAbout;

	@FXML
	private Pane mChampInfo;
	
	@FXML
	private Pane mOptimizer;
	
	@FXML
	private Pane mBlurLayer;

	public void initialize(URL url, ResourceBundle bundl)
	{
		GuiPage pageAbout = new GuiPage("About", mPaneAbout);
		GuiPage pageInfo = new GuiPage("Champion Information", mChampInfo);
		GuiPage pageOptimizer = new GuiPage("Item Optimizer", mOptimizer);
		
		mBlurLayer.setBackground(new Background(LoLOptimizer.splash));
		mBlurLayer.setEffect(new GaussianBlur());
		
		ObservableList list = FXCollections.observableArrayList();
		list.addAll(GuiPage.mainPages.toArray());
	//	list.addAll("About", "Champion Information", "Item Optimizer");
		mListView.setItems(list);
		mListView.getSelectionModel().select(0);

		mListView.setCellFactory(new Callback<ListView<GuiPage>, ListCell<GuiPage>>()
		{
			@Override
			public ListCell<GuiPage> call(ListView<GuiPage> arg0)
			{
				return new ListCell<GuiPage>()
				{
					@Override
					public void updateItem(GuiPage item, boolean bool)
					{
						super.updateItem(item, bool);
						if(item != null)
						{
							Text name = new Text(item.name);
							name.setFill(Color.WHITE);
							VBox vb = new VBox(name);
							HBox hb = new HBox(vb);
							hb.setSpacing(10);
							setGraphic(hb);
						}
					}
				};
			}
			
		});
		
		mListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2)
			{
				if(arg1 instanceof GuiPage && arg2 instanceof GuiPage)
				{
					((GuiPage)arg1).panel.setVisible(false);
					((GuiPage)arg2).panel.setVisible(true);
				}
				
				/*if(GuiPage.forName((String)arg1) != null && GuiPage.forName((String) arg2) != null)
				{
					GuiPage.forName((String)arg1).panel.setVisible(false);
					GuiPage.forName((String)arg2).panel.setVisible(true);
				}*/
			}
		});
	}
}
