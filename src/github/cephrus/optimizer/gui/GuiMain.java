package github.cephrus.optimizer.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import github.cephrus.optimizer.LoLOptimizer;
import github.cephrus.optimizer.lol.info.APIHelper;
import github.cephrus.optimizer.lol.info.Ability;
import github.cephrus.optimizer.lol.info.Champion;
import github.cephrus.optimizer.lol.info.Item;
import github.cephrus.optimizer.lol.info.Item.Maps;
import github.cephrus.optimizer.lol.info.SpellHandler;
import github.cephrus.optimizer.lol.info.StatInfo;
import github.cephrus.optimizer.project.Project;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

@SuppressWarnings("all")
public class GuiMain implements Initializable
{
	@FXML
	private ListView<GuiPage> mListView;
	
	@FXML
	private ListView<Item> mIIBP1, mIIBP2, mIIBP3, mIIBP4;

	@FXML
	private Text mTitle;
	
	@FXML
	private AnchorPane anchor;
	
	@FXML
	private Text mTitle2;
	
	@FXML
	private Pane mPaneAbout, mBuildLoader, mRPNewProjectPane, mPaneUnavailable;

	@FXML
	private Pane mChampInfo, mItemInfoPane;
	
	@FXML
	private Pane mOptimizer;
	
	@FXML
	private Pane mBlurLayer;
	
	@FXML
	private Pane mSettings;
	
	@FXML
	private Button mReloadBtn, mRPNewProject,  mRPNPClose, mRPCPClose, mRPNPCreate, mRPNPSaveChoose, mRPOpenProject;
	
	@FXML
	private Pane mBlur2;
	
	@FXML
	private TilePane mChampInfoPane, mIIPane;
	
	@FXML
	private ScrollPane mChampInfoScroll, mIIScroller;
	
	@FXML
	private TextField mCISearchBox, mIISearchBox, mRPNPName, mRPNPSaveDir;
	
	@FXML
	private Pane mBorder, mBorder1;
	
	@FXML
	private Label mChampName, mChampName1, mRPCPOpen;
	
	@FXML
	private ComboBox mCILevel, mItemMapSelector, mRPNPChampion;
	
	@FXML
	private Label mCI1, mCI2, mCI3, mCI4, mCI5, mCI6, mCI7, mCI8, mCI9, mCI0, mCIRes, mCIResGen;
	
	@FXML
	private Label mAbilityName, mAbilityName1, mAbilityName2, mAbilityName3, mAbilityName4,
				mAbilityCooldown, mAbilityCooldown1, mAbilityCooldown2, mAbilityCooldown3,
				mAbilityCost, mAbilityCost1, mAbilityCost2, mAbilityCost3;
	
	@FXML
	private AnchorPane mBlurI, mBlurQ, mBlurW, mBlurE, mBlurR;
	
	@FXML
	private TextArea mWeb, mWeb1, mWeb2, mWeb3, mWeb4, mInfoBox;
	
	@FXML
	private Label mIIInfoName;
	
	@FXML
	private Pane mIIInfoIcon;
	
	@FXML
	private TextArea mIIInfoDescription;
	
	private Champion selected;
	private Item selectedItem;
	private Maps itemFilter = Maps.ALL;
	private Project currentProject;
	private boolean autoSaveFileName = true;
	
	int pnCounter = 0;
	public String projectName(String name)
	{
		name = name.replace(" ", "");
		File f = new File(APIHelper.dataDir + File.separator + name + ".aqua");
		if(f.exists())
			return projectName(name + ++pnCounter);
			
		pnCounter = 0;
		return name + (name.endsWith(".aqua") ? "" : ".aqua");
	}
	
	public void newProjectWindow()
	{
		this.mRPNPSaveDir.textProperty().set(APIHelper.dataDir.getAbsolutePath() + File.separator + 
				(this.mRPNPName.textProperty().get().length() == 0 ? projectName("Untitled") : projectName(mRPNPName.textProperty().get())));
		
		this.mRPNPName.textProperty().addListener((obsrv, newVal, oldVal) ->
		{
			if(autoSaveFileName)
			{
				this.mRPNPSaveDir.textProperty().set(APIHelper.dataDir.getAbsolutePath() + File.separator + projectName(
						mRPNPName.textProperty().get().length() == 0 ? projectName("Untitled") :
							mRPNPName.textProperty().get()));
			}
		});
		
		this.mRPNPClose.setOnAction((e) ->
		{
			GuiMain.this.mRPNewProjectPane.setVisible(false);
		});
		
		if(this.currentProject == null)
		{
			this.mRPCPClose.setVisible(false);
			this.mRPCPOpen.setVisible(false);
		}
		
		this.mRPNPSaveChoose.setOnAction((e) ->
		{
			FileChooser choosir = new FileChooser();
			choosir.setTitle("Select Save Directory");
			choosir.getExtensionFilters().add(new ExtensionFilter("AQUA Optimizer Projects", "aqua", "test"));
			choosir.setInitialDirectory(APIHelper.dataDir);
			File chosen = choosir.showSaveDialog(LoLOptimizer.stage);
			
			this.mRPNPSaveDir.textProperty().set(chosen.getAbsolutePath());
			this.autoSaveFileName = false;
		});
	}
	
	public void setItemInfo(Item item)
	{
		Image img = new Image(APIHelper.getItemIcon(item));
		
		ImageView iv1 = new ImageView(img);
		iv1.setPreserveRatio(true);
		iv1.setFitHeight(200);
		
		mIIInfoIcon.getChildren().add(iv1);
		mIIInfoName.setText(item.name);
		
		String id = item.description;
		
		id = id.replace("/groupLimit", "").replace("groupLimit", "")
				.replace("/levelLimit", "").replace("levelLimit", "")
				.replace("/rules", "").replace("rules", "")
				.replace("/consumable", "").replace("consumable", "")
				.replace("/mana", "").replace("mana", "");
		
		StringBuilder builder = new StringBuilder();
		char[] a = id.toCharArray();
		int counter = 0;
		for(int i = 0; i < a.length; i++)
		{
			counter++;
			if(a[i] == '\n') counter = 0;
			if(counter >= 50 && a[i] == ' ')
			{
				builder.append("\n");
				counter = 0;
			}
			
			builder.append(a[i]);
		}
		
		mIIInfoDescription.setText(builder.toString());
		setBuildPathInfo(item);
	}

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
	
	public void setBuildPathInfo(Item item)
	{
		this.mIIBP1.setItems(FXCollections.observableArrayList());
		this.mIIBP1.getItems().add(item);
	}
	
	public void initBuildPath()
	{
		this.mIIBP1.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>()
		{
			@Override
			public ListCell<Item> call(ListView<Item> arg0)
			{
				return new ListCell<Item>()
				{
					@Override
					public void updateItem(Item item, boolean bool)
					{
						super.updateItem(item, bool);
						if(item == null || bool)
						{
							setGraphic(null);
						}
						else
						{
							Text title = new Text(item.name);
							Text cost = new Text("Cost: " + item.totalCost + (item.totalCost == item.combineCost ? "" : 
								" (" + item.combineCost + ")"));
							cost.setFill(Color.WHITE);
							title.setFill(Color.WHITE);
							VBox v = new VBox(title, cost);
							ImageView img = new ImageView(new Image(APIHelper.getItemIcon(item)));
							img.setPreserveRatio(true);
							img.setFitHeight(40);
							HBox hb = new HBox(img, v);
							hb.setSpacing(10);
							setGraphic(hb);
						}
					}
				};
			}
			
		});
		this.mIIBP2.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>()
		{
			@Override
			public ListCell<Item> call(ListView<Item> arg0)
			{
				return new ListCell<Item>()
				{
					@Override
					public void updateItem(Item item, boolean bool)
					{
						super.updateItem(item, bool);
						if(item == null || bool)
						{
							setGraphic(null);
						}
						else
						{
							Text title = new Text(item.name);
							Text cost = new Text("Cost: " + item.totalCost + (item.totalCost == item.combineCost ? "" : 
								" (" + item.combineCost + ")"));
							cost.setFill(Color.WHITE);
							title.setFill(Color.WHITE);
							VBox v = new VBox(title, cost);
							ImageView img = new ImageView(new Image(APIHelper.getItemIcon(item)));
							img.setPreserveRatio(true);
							img.setFitHeight(40);
							HBox hb = new HBox(img, v);
							hb.setSpacing(10);
							setGraphic(hb);
						}
					}
				};
			}
			
		});
		this.mIIBP3.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>()
		{
			@Override
			public ListCell<Item> call(ListView<Item> arg0)
			{
				return new ListCell<Item>()
				{
					@Override
					public void updateItem(Item item, boolean bool)
					{
						super.updateItem(item, bool);
						if(item == null || bool)
						{
							setGraphic(null);
						}
						else
						{
							Text title = new Text(item.name);
							Text cost = new Text("Cost: " + item.totalCost + (item.totalCost == item.combineCost ? "" : 
								" (" + item.combineCost + ")"));
							cost.setFill(Color.WHITE);
							title.setFill(Color.WHITE);
							VBox v = new VBox(title, cost);
							ImageView img = new ImageView(new Image(APIHelper.getItemIcon(item)));
							img.setPreserveRatio(true);
							img.setFitHeight(40);
							HBox hb = new HBox(img, v);
							hb.setSpacing(10);
							setGraphic(hb);
						}
					}
				};
			}
			
		});
		this.mIIBP4.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>()
		{
			@Override
			public ListCell<Item> call(ListView<Item> arg0)
			{
				return new ListCell<Item>()
				{
					@Override
					public void updateItem(Item item, boolean bool)
					{
						super.updateItem(item, bool);
						if(item == null || bool)
						{
							setGraphic(null);
						}
						else
						{
							Text title = new Text(item.name);
							Text cost = new Text("Cost: " + item.totalCost + (item.totalCost == item.combineCost ? "" : 
								" (" + item.combineCost + ")"));
							cost.setFill(Color.WHITE);
							title.setFill(Color.WHITE);
							VBox v = new VBox(title, cost);
							ImageView img = new ImageView(new Image(APIHelper.getItemIcon(item)));
							img.setPreserveRatio(true);
							img.setFitHeight(40);
							HBox hb = new HBox(img, v);
							hb.setSpacing(10);
							setGraphic(hb);
						}
					}
				};
			}
			
		});
		
		this.mIIBP2.setItems(FXCollections.observableArrayList());
		this.mIIBP3.setItems(FXCollections.observableArrayList());
		this.mIIBP4.setItems(FXCollections.observableArrayList());
		
		this.mIIBP1.getSelectionModel().selectedItemProperty().addListener((obsrv, oldV, newV) ->
		{
			this.mIIBP2.getItems().clear();
			this.mIIBP2.setItems(FXCollections.observableArrayList());
			if(!newV.hasDescendingItems) return;
			for(String id : newV.buildsFrom)
			{
				int i = Integer.parseInt(id);
				for(Item items : Item.byAlpha())
				{
					if(items.id == i) mIIBP2.getItems().add(items);
				}
			}
		});
		
		this.mIIBP2.getSelectionModel().selectedItemProperty().addListener((obsrv, oldV, newV) ->
		{
			this.mIIBP3.getItems().clear();
			this.mIIBP3.setItems(FXCollections.observableArrayList());
			if(!newV.hasDescendingItems) return;
			for(String id : newV.buildsFrom)
			{
				int i = Integer.parseInt(id);
				for(Item items : Item.byAlpha())
				{
					if(items.id == i) mIIBP3.getItems().add(items);
				}
			}
		});
		
		this.mIIBP3.getSelectionModel().selectedItemProperty().addListener((obsrv, oldV, newV) ->
		{
			this.mIIBP4.getItems().clear();
			this.mIIBP4.setItems(FXCollections.observableArrayList());
			if(!newV.hasDescendingItems) return;
			for(String id : newV.buildsFrom)
			{
				int i = Integer.parseInt(id);
				for(Item items : Item.byAlpha())
				{
					if(items.id == i) mIIBP4.getItems().add(items);
				}
			}
		});
	}
	
	public void initialize(URL url, ResourceBundle bundl)
	{
		Tooltip uvTip = new Tooltip("There must be an active project to use this page.");
		GuiPage pageAbout = new GuiPage("About", mPaneAbout);
		GuiPage pageInfo = new GuiPage("Champion Information", mChampInfo, new Image(APIHelper.getChampIcon(Champion.fromName("Nunu"))));
		/** Comment for release, uncomment if you need to work on them */
		GuiPage pageItem = new GuiPage("Item Information", mItemInfoPane, new Image(APIHelper.getItemIcon(Item.random())));
		GuiPage buildLoader = new GuiPage("Build Loader", mBuildLoader, new Image(APIHelper.getChampIcon(Champion.fromName("Ekko"))));
		GuiPage pageOptimizer = new GuiPage("Build Optimizer", mOptimizer, new Image(
				APIHelper.getChampIcon(Champion.fromName("Velkoz"))), uvTip);
		GuiPage pageSettings = new GuiPage("Settings", mSettings, new Image(APIHelper.getChampIcon(Champion.fromName("Heimerdinger"))));
		
		pageAbout.panel.setVisible(true);
		GuiMain.this.mPaneUnavailable.setVisible(false);
	//	pageOptimizer.visible = false;
		mInfoBox.setText("Build Optimizer isn't endorsed by Riot Games and doesn't \n"
						+ " reflect the views or opinions of Riot Games or anyone \n"
						+ " officially involved in producing or managing League of Legends. \n"
						+ " League of Legends and Riot Games are trademarks or registered \n"
						+ " trademarks of Riot Games, Inc. League of Legends © Riot Games, Inc.\n"
						+ "Program written by Cephrus. Special thanks to Aqua Dragon.");
		
		DropShadow shad = new DropShadow();
		shad.setOffsetX(1.8);
		shad.setOffsetY(1.8);
		mTitle.setEffect(shad);
		mTitle2.setEffect(shad);
		
		ObservableList list = FXCollections.observableArrayList();
		list.addAll(GuiPage.mainPages.toArray());
		list.remove(pageOptimizer);
		
		newProjectWindow();
		initBuildPath();
		this.mRPNPCreate.setOnAction((e) ->
		{
			if(currentProject != null) currentProject.save();
			
			Project project = new Project();
			project.name = this.mRPNPName.textProperty().get();
			project.champion = Champion.fromName((String)this.mRPNPChampion.valueProperty().get());
			project.saveDir = new File(mRPNPSaveDir.textProperty().get());
			project.save();
			this.currentProject = project;
			
		//	pageOptimizer.visible = true;
			list.add(4, pageOptimizer);
			this.mRPCPClose.setVisible(true);
			this.mRPCPOpen.setVisible(true);
			this.mRPCPOpen.setText("Current Project: " + project.name);
		});
		
		this.mRPCPClose.setOnAction((e) ->
		{
			this.currentProject = null;
		//	pageOptimizer.visible = false;
			list.remove(pageOptimizer);
			this.mRPCPClose.setVisible(false);
			this.mRPCPOpen.setVisible(false);
			//TODO
		});
		
		this.mRPOpenProject.setOnAction((e) ->
		{
			if(currentProject != null)
			{
				currentProject.save();
				currentProject = null;
				this.mRPCPClose.setVisible(false);
				this.mRPCPOpen.setVisible(false);
			//	pageOptimizer.visible = false;
				list.remove(pageOptimizer);
				//TODO
			}
			
			try
			{	
				FileChooser choosir = new FileChooser();
				choosir.setTitle("Open Project");
				choosir.getExtensionFilters().add(new ExtensionFilter("AQUA Optimized Projects", "*.aqua", "*.test"));
				choosir.setInitialDirectory(APIHelper.dataDir);
				File chosen = choosir.showOpenDialog(LoLOptimizer.stage);
				if(chosen == null) return;
				
				Scanner scn = new Scanner(chosen.toURI().toURL().openStream());
				JSONObject obj = new JSONObject(scn.next());
				
				Project project = Project.read(obj);
				project.saveDir = chosen;
				this.currentProject = project;
				
			//	pageOptimizer.visible = true;
				list.add(4, pageOptimizer);
				this.mRPCPClose.setVisible(true);
				this.mRPCPOpen.setVisible(true);
				this.mRPCPOpen.setText("Current Project: " + project.name);
			}
			catch(Exception ex) {ex.printStackTrace();}
		});
		
		String[] names = new String[Champion.champions().length];
		for(int i = 0; i < Champion.champions().length; i++)
		{
			names[i] = Champion.byAlpha()[i].displayName;
		}
		
		mRPNPChampion.getItems().addAll(names);
		mRPNewProject.setOnAction((action) ->
		{
			GuiMain.this.mRPNewProjectPane.setVisible(true);
		});
		
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
		
		mIIScroller.setFitToWidth(true);
		mIIScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		List<Button> itemBtns = new ArrayList<Button>();
		Map<Item, Button> itemButtons = new HashMap<Item, Button>();
		Map<Item, Image> itemArt = new HashMap<Item, Image>();
		mIISearchBox.textProperty().addListener((obsrv, oldVal, newVal) ->
		{
			GuiMain.this.mIIPane.getChildren().clear();
			itemBtns.clear();
			
			for(Item i : Item.byCost())
			{
				if(!i.name.equalsIgnoreCase(newVal) && !i.name.toLowerCase().contains(newVal.toLowerCase()) && !newVal.isEmpty()) continue;
				if(i.maps.indexOf(itemFilter) == -1 && itemFilter != Maps.ALL) continue;
				
				ImageView iv = new ImageView(itemArt.get(i));
				iv.setPreserveRatio(true);
				iv.setFitHeight(50);
	        	iv.setFitWidth(50);
	        	VBox v = new VBox(itemButtons.get(i));
	        	HBox b = new HBox(v);
	        	mIIPane.getChildren().add(b);
	        	itemBtns.add(itemButtons.get(i));
			}
		});
		
		for(Item item : Item.byCost())
		{
			Image i = new Image(APIHelper.getItemIcon(item));
			itemArt.put(item, i);
			ImageView iv = new ImageView(i);
        	iv.setPreserveRatio(true);
        	iv.setFitHeight(50);
        	iv.setFitWidth(50);
        	
        	Button btn = new Button();
        	btn.setTooltip(new Tooltip(item.name));
        	btn.setGraphic(iv);
        	btn.setOnAction(new EventHandler<ActionEvent>()
        	{
        		@Override
        		public void handle(ActionEvent e)
        		{
        			GuiMain.this.setItemInfo(item);
        		}
        	});
        	
        	VBox v = new VBox(btn);
        	HBox b = new HBox(v);
        	mIIPane.getChildren().add(b);
        	itemBtns.add(btn);
        	itemButtons.put(item, btn);
		}

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
						
						/*if(item != null)
						{
							Text name = new Text(item.name);
							name.setFill(Color.WHITE);
							VBox vb = new VBox(name);
							HBox hb = new HBox(vb);
							hb.setSpacing(10);
							setGraphic(hb);
						}*/
						/*if(item != null) // Circular Icons
						{
							Text name = new Text(item.name);
							name.setFill(Color.WHITE);
							VBox vb = new VBox(name);
							Circle circ = new Circle(20, 20, 20);
							ImageView img = new ImageView(item.image);
							img.setPreserveRatio(true);
							img.setFitHeight(40);
							img.setClip(circ);
							HBox hb = new HBox(img, vb);
							hb.setSpacing(10);
							setGraphic(hb);
						}*/
						if(item == null || bool)
						{
							setGraphic(null);
						}
						else // Rounded Icons
						{
							Text name = new Text(item.name);
							name.setFill(Color.WHITE);
							VBox vb = new VBox(name);
							Circle circ = new Circle(0, 0, 40);
							ImageView img = new ImageView(item.image);
							img.setPreserveRatio(true);
							img.setFitHeight(40);
							img.setClip(circ);
							HBox hb = new HBox(img, vb);
							hb.setSpacing(10);
							if(!item.visible && item.tooltip != null) this.setTooltip(item.tooltip);
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
					GuiMain.this.mPaneUnavailable.setVisible(false);
					((GuiPage)arg1).panel.setVisible(false);
					((GuiPage)arg2).panel.setVisible(true);
				/*	if(((GuiPage)arg2).visible) */
				//	else GuiMain.this.mPaneUnavailable.setVisible(true);
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
		
		mItemMapSelector.setItems(FXCollections.observableArrayList(
				"All", "Summoner's Rift", "Twisted Treeline", "Howling Abyss", "Crystal Scar"));
		mItemMapSelector.getSelectionModel().select(0);
		mItemMapSelector.valueProperty().addListener((obsrv, oldV, newVal) ->
		{
			if(newVal instanceof String)
			{
				int index = mItemMapSelector.getSelectionModel().getSelectedIndex();
				GuiMain.this.itemFilter = Maps.values()[index];
				
				GuiMain.this.mIIPane.getChildren().clear();
				itemBtns.clear();
				
				for(Item i : Item.byCost())
				{
					if(i.maps.indexOf(itemFilter) == -1 && itemFilter != Maps.ALL) continue;
					
					ImageView iv = new ImageView(itemArt.get(i));
					iv.setPreserveRatio(true);
					iv.setFitHeight(50);
		        	iv.setFitWidth(50);
		        	VBox v = new VBox(itemButtons.get(i));
		        	HBox b = new HBox(v);
		        	mIIPane.getChildren().add(b);
		        	itemBtns.add(itemButtons.get(i));
				}
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
		
		mIIInfoDescription.setEditable(false);
	}
}
