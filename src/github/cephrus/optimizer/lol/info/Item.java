package github.cephrus.optimizer.lol.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

public class Item
{
	protected static final Map<String, Item> itemByName = new HashMap<String, Item>();
	
	public static final Item[] items = new Item[4096];
	public final static List<Item> validItems = new ArrayList<Item>();
	
	public static enum Maps
	{
		ALL,
		SUMMONERRIFT,
		TWISTEDTREELINE,
		HOWLINGABYSS,
		CRYSTALSCAR
	}
	
	/*public static final Item
		bootsOfSpeed = new Item(1001),
		faerieCharm = new Item(1004),
		rejuvBead = new Item(1006),
		giantBelt = new Item(1011),
		agilityCloak = new Item(1018),
		blastingWand = new Item(1026),
		sapphireCrystal = new Item(1027),
		rubyCrystal = new Item(1028),
		clothArmor = new Item(1029),
		chainVest = new Item(1031),
		nullMantle = new Item(1033),
		longSword = new Item(1036),
		pickaxe = new Item(1037),
		bfSword = new Item(1038),
		jgTalisman = new Item(1039),
		jgMachete = new Item(1041),
		dagger = new Item(1042),
		recurveBow = new Item(1043),
		brawlrGlove = new Item(1051),
		ampTome = new Item(1052),
		vampScepter = new Item(1053),
		drnsShield = new Item(1054),
		drnsBlade = new Item(1055),
		drnsRing = new Item(1056),
		ngatronCloak = new Item(1057),
		largeRod = new Item(1058),
		darkSeal = new Item(1082),
		cull = new Item(1083),
		jgBlueWarrior = new Item(1400),
		jgBlueCinderhulk = new Item(1401),
		jgBlueEchoes = new Item(1402),
		jgGrenWarrior = new Item(1408),
		jgGrenCinderhulk = new Item(1409),
		jgGrenEchoes = new Item(1410),
		jgRedWarrior = new Item(1412),
		jgRedCinderhulk = new Item(1413),
		jgRedEchoes = new Item(1414),
		jgBlueRazor = new Item(1416),
		jgGrenRazor = new Item(1418),
		jgRedRazor = new Item(1419),
		healthPotion = new Item(2003),
		totalBiscuit = new Item(2009),
		biscuitRejuv = new Item(2010),
		kircheisShard = new Item(2015),
		refillPotion = new Item(2031),
		huntersPotion = new Item(2032),
		corruptingPotion = new Item(2033),
		visionWard = new Item(2043),
		rubySiteStone = new Item(2045),
		oraclesExtract = new Item(2047),
		sightStone = new Item(2049),
		explorersWard = new Item(2050),
		guardiansHorn = new Item(2051),
		poroSnax = new Item(2052),
		raptorCloak = new Item(2053),
		dietSnax = new Item(2054),
		ironElixir = new Item(2138),
		sorcElixir = new Item(2139),
		wrthElixir = new Item(2140),
		eyeOfWatchers = new Item(2301),
		eyeOfOasis = new Item(2302),
		eyeOfEquinox = new Item(2303),
		abyssalScepter = new Item(3001),
		archStaff = new Item(3003),
		manamune = new Item(3004),
		berserkersGreaves = new Item(3006),
		archStaffCS = new Item(3007),
		manamuneCS = new Item(3008),
		bootsSwiftness = new Item(3009),
		catalystAeons = new Item(3010),
		sorcShoes = new Item(3020),
		froznMallet = new Item(3022),
		glacialShroud = new Item(3024),
		icebornGauntlet = new Item(3025),
		guardianAngel = new Item(3026),
		rodOfAges = new Item(3027),
		chaliceHarmony = new Item(3028),
		rodOfAgesCS = new Item(3029),
		hextechGLP = new Item(3030),
		infinityEdge = new Item(3031),
		mortalReminder = new Item(3033),
		giantSlayer = new Item(3034),
		lastWhisper = new Item(3035),
		lordDominiks = new Item(3036),
		seraphsEmbrace = new Item(3040),
		soulstealer = new Item(3041),
		muramana = new Item(3042),
		muramana2 = new Item(3043),
		phage = new Item(3044),
		phantomDancar = new Item(3046),
		ninjaTabi = new Item(3047),
		seraphs2 = new Item(3048),
		zekesHarbinger = new Item(3050),
		jaurimsFist = new Item(3052),
		steraksGage = new Item(3053),
		ohmwrecker = new Item(3056),
		sheen = new Item(3057),
		banner = new Item(3060),
		visage = new Item(3065),
		kindlGem = new Item(3067),
		sunfireCape = new Item(3068),
		talisman = new Item(3069),
		tearGoddess = new Item(3070),
		blackCleaver = new Item(3071),
		bloodthirster = new Item(3072),
		tearGoddessCS = new Item(3073),
		ravenousHydra = new Item(3074),
		thornmail = new Item(3075),
		tiamat = new Item(3077),
		triForce = new Item(3078),
		wardensMail = new Item(3082),
		warmogs = new Item(3083),
		bloodmail = new Item(3084),
		runaans = new Item(3085),
		zeal = new Item(3086),
		statikkShock = new Item(3087),
		deathcap = new Item(3089),
		witchcap = new Item(3090),
		witsEnd = new Item(3091),
		frostQueens = new Item(3092),
		rapidFire = new Item(3094),
		nomadsMedal = new Item(3096),
		targonsBrace = new Item(3097),
		frostfang = new Item(3098),
		lichBane = new Item(3100),
		stinger = new Item(3101),
		bansheeVeil = new Item(3102),
		vanDammPillager = new Item(3104),
		aegisOfLegion = new Item(3105),
		fiendishCodex = new Item(3108),
		frozenHeart = new Item(3110),
		mercTreads = new Item(3111),
		guardianOrb = new Item(3112),
		aetherWisp = new Item(3113),
		forbiddenIdol = new Item(3114),
		nashorTooth = new Item(3115),
		crystalScepter = new Item(3116),
		mobilityBoots = new Item(3117),
		wickedHatchet = new Item(3122),
		executionCalling = new Item(3123),
		rageblade = new Item(3124),
		warhammer = new Item(3133),
		serratedDirk = new Item(3134),
		voidStaff = new Item(3135),
		hauntingGuise = new Item(3136),
		dervishBlade = new Item(3137),
		mercScimitar = new Item(3139),
		quicksilverSash = new Item(3140),
		youmuusGhostblade = new Item(3142),
		randuinsOmen = new Item(3143),
		bilgewaterCutlass = new Item(3144),
		hextechRevolver = new Item(3145),
		hextechGunblade = new Item(3146),
		duskblade = new Item(3147),
		liandrysTorment = new Item(3151),
		hextechProtobelt = new Item(3152),
		bladeOfRuinedKing = new Item(3153),
		hexdrinker = new Item(3155),
		mawOfMalmortius = new Item(3156),
		zhonyasHourglass = new Item(3157),
		lucidityBoots = new Item(3158),
		morellonomicon = new Item(3065),
		moonflair = new Item(3170),
		athenesGrail = new Item(3174),
		sanguineBlade = new Item(3181),
		guardianHammer = new Item(3184),
		lightBringer = new Item(3185),
		arcaneSweeper = new Item(3187),
		locketOfSolari = new Item(3190),
		seekersArmguard = new Item(3191),
		hexCoreMk1 = new Item(3196),
		hexCoreMk2 = new Item(3197),
		hexCoreMk3 = new Item(3198),
		hexCoreMk0 = new Item(3200),
		spectresCowl = new Item(3211),
		mikaelsCrucible = new Item(3222),
		ludensEcho = new Item(3285),
		ancientCoin = new Item(3301),
		relicShield = new Item(3302),
		spellthiefEdge = new Item(3303),
		wardingTotem = new Item(3340),
		sweepingLens = new Item(3341),
		soulAnchor = new Item(3345),
		arcaneSweeperTT = new Item(3348),
		greaterStealthTotem = new Item(3361),
		greaterVisionTotem = new Item(3362),
		farsightAlt = new Item(3363),
		oracleAlt = new Item(3364),
		faceOfTheMountain = new Item(3401),
		goldenTranscendence = new Item(3460),
		goldTranscDisabled = new Item(3461),
		seerStone = new Item(3462),
		ardentCenser = new Item(3504),
		essenceReaver = new Item(3508),
		zzrotPortal = new Item(3512),
		blackSpear = new Item(3599),
		jgStalkers = new Item(3706),
		jgTrackers = new Item(3711),
		jgSkirmishers = new Item(3715),
		deadMansPlate = new Item(3742),
		titanicHydra = new Item(3748),
		bamiCinder = new Item(3751),
		righteousGlory = new Item(3800),
		crystallineBracer = new Item(3801),
		lostChapter = new Item(3802),
		deathsDance = new Item(3812),
		fireAtWill = new Item(3901),
		deathsDaughter = new Item(3902),
		raiseMorale = new Item(3903);*/
	
	public int id;
	public String name;
	public StatInfo info;
	
	public int totalCost;
	public int combineCost;
	public String description;
	public String[] buildsFrom;
	public boolean hasDescendingItems = false;
	
	public List<Maps> maps;
	
	protected static boolean initialized = false;
	
	public static boolean initialize()
	{
		if(initialized) return false;
		
		/*JSONObject itemJson = APIHelper.loadDataJSON("items");
		Iterator<String> it = itemJson.keys();
		while(it.hasNext())
		{
			String key = it.next();
			int id = Integer.parseInt(key);
			
			new Item(id);
		}*/
		
		JSONObject itemJson = APIHelper.loadDataJSON("itemBeta");
		for(String key : itemJson.keySet())
		{
			int id = Integer.parseInt(key);
			new Item(id);
		}
		
		initialized = true;
		return true;
	}
	
	public static Item[] byCost()
	{
		Item[] item = new Item[validItems.size()];
		List<Item> li = (List<Item>)Arrays.asList(validItems.toArray(item));
		
		Collections.sort(li,  new Comparator<Item>()
		{
			@Override
			public int compare(Item arg0, Item arg1)
			{
				if(arg0.totalCost > arg1.totalCost)
					return 1;
				else if(arg0.totalCost < arg1.totalCost)
					return -1;
				else return 0;
			}		
		});
		
		return li.toArray(item);
	}
	
	public static Item[] byAlpha()
	{
		String[] names = new String[validItems.size()];
		for(int i = 0; i < names.length; i++)
		{
			names[i] = validItems.get(i).name;
		}
		
		Arrays.sort(names);
		Item[] rtn = new Item[names.length];
		for(int i = 0; i < names.length; i++)
		{
			rtn[i] = fromName(names[i]);
		}
		
		return rtn;
	}
	
	public Item(int id)
	{
		if(items[id] != null) throw new IllegalArgumentException("Item ID " + id + " already occupied by " + items[id].name);
		
		items[id] = this;
		this.id = id;
		this.maps = new ArrayList<Maps>();
	//	APIHelper.injectItemData(this);
		APIHelper.develInjectData(this);
		
		if(this.name != null) itemByName.put(name, this);
	}
	
	public static Item random()
	{
		Random rand = new Random();
		return validItems.get(rand.nextInt(validItems.size()));
	}
	
	public static Item fromName(String name)
	{
		return itemByName.get(name);
	}
}
