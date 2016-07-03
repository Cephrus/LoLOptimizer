package github.cephrus.optimizer.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import github.cephrus.optimizer.LoLOptimizer;
import github.cephrus.optimizer.lol.info.APIHelper;
import github.cephrus.optimizer.lol.info.Ability;
import github.cephrus.optimizer.lol.info.Champion;
import github.cephrus.optimizer.lol.info.SpellHandler;
import github.cephrus.optimizer.lol.info.StatInfo;
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
import javafx.scene.control.TextArea;
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
	private Pane mChampInfo, mItemInfoPane;
	
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
	private Pane mBorder, mBorder1;
	
	@FXML
	private Label mChampName, mChampName1;
	
	@FXML
	private ComboBox mCILevel;
	
	@FXML
	private Label mCI1, mCI2, mCI3, mCI4, mCI5, mCI6, mCI7, mCI8, mCI9, mCI0, mCIRes, mCIResGen;
	
	@FXML
	private Label mAbilityName, mAbilityName1, mAbilityName2, mAbilityName3, mAbilityName4,
				mAbilityCooldown, mAbilityCooldown1, mAbilityCooldown2, mAbilityCooldown3,
				mAbilityCost, mAbilityCost1, mAbilityCost2, mAbilityCost3;
	
	@FXML
	private AnchorPane mBlurI, mBlurQ, mBlurW, mBlurE, mBlurR;
	
	@FXML
	private TextArea mWeb, mWeb1, mWeb2, mWeb3, mWeb4;
	
	private Champion selected;

	public void setChampInfo(Champion c)
	{
		this.mChampName.setText(c.displayName);
		this.mChampName1.setText(c.displayName);
		
		Image imag = new Image(APIHelper.getSplash(c, 0));
		
		ImageView img = new ImageView(imag);
		img.setPreserveRatio(true);
		img.setFitHeight(240);
		img.setFitWidth(120);
		this.mBorder.getChildren().add(img);
		
		ImageView img1 = new ImageView(imag);
		img1.setPreserveRatio(true);
		img1.setFitHeight(240);
		img1.setFitWidth(120);
		this.mBorder1.getChildren().add(img1);
		
		StatInfo stats = c.info;
		String lvl = (String)this.mCILevel.getSelectionModel().getSelectedItem();
		
		this.mCI1.setText(c.getHealth(lvl));
		this.mCI2.setText(c.getRegen(lvl));
		this.mCIRes.setText(APIHelper.getLocalization(c.resource));
		this.mCIResGen.setText(c.resource.equalsIgnoreCase("MP") ? "Mana Regen" : 
			c.resource.equalsIgnoreCase("energy") ? "Energy Regen" : "");
		this.mCI3.setText(c.getResource(lvl));
		this.mCI4.setText(c.getResourceGen(lvl));
		this.mCI5.setText(c.getRange());
		this.mCI6.setText(c.getBaseAttackDamage(lvl));
		this.mCI7.setText(c.getAttackSpeed(lvl));
		this.mCI8.setText(c.getArmor(lvl));
		this.mCI9.setText(c.getMagicResist(lvl));
		this.mCI0.setText(c.getMS());
		
		mWeb.setText(formatAbility(c, -1));
		mWeb1.setText(formatAbility(c, 0));
		mWeb2.setText(formatAbility(c, 1));
		mWeb3.setText(formatAbility(c, 2));
		mWeb4.setText(formatAbility(c, 3));
		
		mAbilityName4.setText(SpellHandler.getPassive(c).name);
		mAbilityName.setText(SpellHandler.getAbility(c, 0).name);
		mAbilityName1.setText(SpellHandler.getAbility(c, 1).name);
		mAbilityName2.setText(SpellHandler.getAbility(c, 2).name);
		mAbilityName3.setText(SpellHandler.getAbility(c, 3).name);
		
		mAbilityCooldown.setText(SpellHandler.getAbility(c, 0).cooldown);
		mAbilityCooldown1.setText(SpellHandler.getAbility(c, 1).cooldown);
		mAbilityCooldown2.setText(SpellHandler.getAbility(c, 2).cooldown);
		mAbilityCooldown3.setText(SpellHandler.getAbility(c, 3).cooldown);
		
		mAbilityCost.setText(SpellHandler.getAbility(c, 0).cost);
		mAbilityCost1.setText(SpellHandler.getAbility(c, 1).cost);
		mAbilityCost2.setText(SpellHandler.getAbility(c, 2).cost);
		mAbilityCost3.setText(SpellHandler.getAbility(c, 3).cost);
		
		this.selected = c;
	}
	
	public String formatAbility(Champion champ, int index)
	{
		Ability ability = index == -1 ? SpellHandler.getPassive(champ) : 
			SpellHandler.getAbility(champ, index);
		StringBuilder builder = new StringBuilder();
		String description = ability.description;
		
		JSONObject obj = APIHelper.getJSONFor(champ).getJSONObject("data").getJSONObject(champ.name);
		JSONObject spell = index == -1 ? null : obj.getJSONArray("spells").getJSONObject(index);
		if(spell != null)
		{
			JSONArray bases = spell.getJSONArray("effectBurn");
			for(int i = 0; i < bases.length(); i++)
			{
				Object base = bases.get(i);
				if(base instanceof String && base != null) 
					description = description.replace("{{ e" + i + " }}", (String)base);
				
			}
			
			if(spell.has("maxammo"))
				description = description.replace("{{ maxammo }}", "" + spell.getInt("maxammo"));
			
			JSONArray scalings = spell.getJSONArray("vars");
			for(int i = 0; i < scalings.length(); i++)
			{
				JSONObject scaling = (JSONObject)scalings.get(i);
				if(scaling == null) continue;
				
				double scl = scaling.getDouble("coeff");
				String key = scaling.getString("key");
				String typ = scaling.getString("link");
				
				description = description.replace("{{ " + key + " }}", Math.round(scl * 100) + "% " + APIHelper.getLocalization(typ));
			}
		}
		
		String[] s = description.split("<|\\>");
		for(String str : s)
		{
			if(str.startsWith("span") || str.startsWith("/span")) continue;
			else if(str.equals("i") || str.equals("/i")) continue;
			else if(str.equals("br")) builder.append("\n");
			else
			{
				char[] a = str.toCharArray();
				int counter = 0;
				for(int i = 0; i < a.length; i++)
				{
					counter++;
					if(counter >= 50 && a[i] == ' ')
					{
						builder.append("\n");
						counter = 0;
					}
					
					builder.append(a[i]);
				}
			}
		}
		
		return builder.toString();
	}
	
	public void initialize(URL url, ResourceBundle bundl)
	{
		GuiPage pageAbout = new GuiPage("About", mPaneAbout);
		GuiPage pageInfo = new GuiPage("Champion Information", mChampInfo);
		/** Commented for release, uncomment if you need to work on them */
	//	GuiPage pageItem = new GuiPage("Item Information", mItemInfoPane);
	//	GuiPage pageOptimizer = new GuiPage("Build Optimizer", mOptimizer);
		GuiPage pageSettings = new GuiPage("Settings", mSettings);
		
		pageAbout.panel.setVisible(true);
		
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
				new File(APIHelper.dataDir + File.separator + "apiver").delete();
				APIHelper.queueUpdate = true;
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
        	Image i = new Image(APIHelper.getChampIcon(c));
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
			}
		});
		
		mCILevel.setStyle("-fx-text-fill : white ;");
		mCILevel.getSelectionModel().select("1-18");
		mCILevel.setCellFactory(new Callback<ListView<String>, ListCell<String>>()
		{
			@Override
			public ListCell<String> call(ListView<String> arg0)
			{
				return new ListCell<String>()
				{
					@Override
					public void updateItem(String item, boolean empty)
					{
						super.updateItem(item, empty);
						this.setText(item);
						this.setTextFill(Color.WHITE);
					}
				};
			}		
		});
		mCILevel.setItems(FXCollections.observableArrayList("1-18", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"));
		mCILevel.valueProperty().addListener((obsrv, oldV, newV) ->
		{
			if(newV instanceof String)
			{
				// I'm lazy
				Champion c = selected;
				String lvl = (String)newV;
				
				if(c == null) return;
				
				this.mCI1.setText(c.getHealth(lvl));
				this.mCI2.setText(c.getRegen(lvl));
				this.mCIRes.setText(APIHelper.getLocalization(c.resource));
				this.mCIResGen.setText(c.resource.equalsIgnoreCase("MP") ? "Mana Regen" : 
					c.resource.equalsIgnoreCase("energy") ? "Energy Regen" : "");
				this.mCI3.setText(c.getResource(lvl));
				this.mCI4.setText(c.getResourceGen(lvl));
				this.mCI5.setText(c.getRange());
				this.mCI6.setText(c.getBaseAttackDamage(lvl));
				this.mCI7.setText(c.getAttackSpeed(lvl));
				this.mCI8.setText(c.getArmor(lvl));
				this.mCI9.setText(c.getMagicResist(lvl));
				this.mCI0.setText(c.getMS());
			}
		});
	}
}
