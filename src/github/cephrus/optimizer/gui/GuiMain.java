package github.cephrus.optimizer.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import github.cephrus.optimizer.LoLOptimizer;
import github.cephrus.optimizer.lol.info.APIHelper;
import github.cephrus.optimizer.lol.info.Champion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
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
	private Text mTitle;
	
	@FXML
	private AnchorPane anchor;
	
	@FXML
	private Text mTitle2;
	
	@FXML
	private Pane mPaneAbout;

	@FXML
	private Pane mChampInfo;
	
	@FXML
	private Pane mOptimizer;
	
	@FXML
	private Pane mBlurLayer;
	
	@FXML
	private Pane mSettings;
	
	@FXML
	private Button mReloadBtn;
	
	@FXML
	private Pane mBlur2;
	
	@FXML
	private TilePane mChampInfoPane;
	
	@FXML
	private ScrollPane mChampInfoScroll;
	
	@FXML
	private TextField mCISearchBox;
	
	@FXML
	private Pane mBorder;
	
	@FXML
	private Label mChampName;
	
	@FXML
	private ComboBox mCILevel;

	public void setChampInfo(Champion c)
	{
		this.mChampName.setText(c.displayName);
		ImageView img = new ImageView(new Image("http://ddragon.leagueoflegends.com/cdn/img/champion/loading/" + c.name + "_0.jpg"));
		img.setPreserveRatio(true);
		img.setFitHeight(240);
		img.setFitWidth(120);
		this.mBorder.getChildren().add(img);
	}
	
	public void initialize(URL url, ResourceBundle bundl)
	{
		GuiPage pageAbout = new GuiPage("About", mPaneAbout);
		GuiPage pageInfo = new GuiPage("Champion Information", mChampInfo);
		GuiPage pageOptimizer = new GuiPage("Item Optimizer", mOptimizer);
		GuiPage pageSettings = new GuiPage("Settings", mSettings);
		
		DropShadow shad = new DropShadow();
		shad.setOffsetX(1.8);
		shad.setOffsetY(1.8);
		mTitle.setEffect(shad);
		mTitle2.setEffect(shad);
		
		mReloadBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				new File(APIHelper.dataDir + File.separator + "championids.json").delete();
				for(File f : new File(APIHelper.dataDir + File.separator + "champions").listFiles())
				{
					f.delete();
				}
				new APIHelper();
				APIHelper.updateChampionInformation();
			}
		});
		
		mBlurLayer.setBackground(new Background(LoLOptimizer.splash));
		mBlurLayer.setEffect(new GaussianBlur());
		
		mChampInfoScroll.setFitToWidth(true);
		mChampInfoScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		List<Button> btns = new ArrayList<Button>();
		Map<Champion, Button> champButtons = new HashMap<Champion, Button>();
		Map<Champion, Image> art = new HashMap<Champion, Image>();
		mCISearchBox.textProperty().addListener((obsrv, oldVal, newVal) ->
		{
			GuiMain.this.mChampInfoPane.getChildren().clear();
			btns.clear();
			
			for(Champion c : Champion.byAlpha())
			{
				if(Champion.tags.contains(newVal.toLowerCase()) && !c.tag.equalsIgnoreCase(newVal)) continue;
				else if(!c.tag.equalsIgnoreCase(newVal) && !c.displayName.toLowerCase().contains(newVal.toLowerCase()) && !newVal.isEmpty()) continue;
				
				ImageView iv = new ImageView(art.get(c));
	        	iv.setPreserveRatio(true);
	        	iv.setFitHeight(50);
	        	iv.setFitWidth(50);
	        	VBox v = new VBox(champButtons.get(c));
	        	HBox b = new HBox(v);
	        	mChampInfoPane.getChildren().add(b);
	        	btns.add(champButtons.get(c));
			}
		});
		
        for(Champion c : Champion.byAlpha()) 
        {
        	Image i = new Image("http://ddragon.leagueoflegends.com/cdn/6.12.1/img/champion/" + c.name + ".png");
        	art.put(c, i);
        	ImageView iv = new ImageView(i);
        	iv.setPreserveRatio(true);
        	iv.setFitHeight(50);
        	iv.setFitWidth(50);
        	
        	Button btn = new Button();
        	btn.setTooltip(new Tooltip(c.displayName));
        	btn.setGraphic(iv);
        	btn.setOnAction(new EventHandler<ActionEvent>()
        	{
        		@Override
        		public void handle(ActionEvent e)
        		{
        			GuiMain.this.setChampInfo(c);
        		}
        	});
        	
        	VBox v = new VBox(btn);
        	HBox b = new HBox(v);
        	mChampInfoPane.getChildren().add(b);
        	btns.add(btn);
        	champButtons.put(c, btn);
        }
		
		ObservableList list = FXCollections.observableArrayList();
		list.addAll(GuiPage.mainPages.toArray());
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
		
		mCILevel.setEditable(true);
		mCILevel.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18));
	}
}
