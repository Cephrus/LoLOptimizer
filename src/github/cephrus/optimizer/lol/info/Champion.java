package github.cephrus.optimizer.lol.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Champion
{
	private static final Map<String, Champion> champByName = new HashMap<String, Champion>();
	
	/** I'll fill in the rest later *//*
	public static final Champion championAatrox = new Champion("Aatrox");
	public static final Champion championAhri = new Champion("Ahri");
	public static final Champion championAkali = new Champion("Akali");
	public static final Champion championAlistar = new Champion("Alistar");
	public static final Champion championAmumu = new Champion("Amumu");
	public static final Champion championAnivia = new Champion("Anivia");
	public static final Champion championAshe = new Champion("Ashe");
	public static final Champion championAurel = new Champion("AurelionSol");
	public static final Champion championAzir = new Champion("Azir");
	public static final Champion championBard = new Champion("Bard");
	public static final Champion championBlitz = new Champion("Blitzcrank");
	public static final Champion championBrand = new Champion("Brand");
	public static final Champion championBraum = new Champion("Braum");
	public static final Champion championCaitlyn = new Champion("Caitlyn");
	public static final Champion championCassio = new Champion("Cassiopeia");
	public static final Champion championChoGath = new Champion("ChoGath");
	public static final Champion championCorki = new Champion("Corki");
	public static final Champion championDarius = new Champion("Darius");
	public static final Champion championDiana = new Champion("Diana");
	public static final Champion championMundo = new Champion("DrMundo");
	public static final Champion championDraven = new Champion("Draven");
	public static final Champion championEkko = new Champion("Ekko");
	public static final Champion championElise = new Champion("Elise");
	public static final Champion championEve = new Champion("Evelynn");
	public static final Champion championEzreal = new Champion("Ezreal");
	public static final Champion championFiddle = new Champion("Fiddlesticks");
	public static final Champion championFiora = new Champion("Fiora");
	public static final Champion championFizz = new Champion("Fizz");
	public static final Champion championGalio = new Champion("Galio");
	public static final Champion championGP = new Champion("Gangplank");
	public static final Champion championGaren = new Champion("Garen");
	public static final Champion championGnar = new Champion("Gnar");
	public static final Champion championGragas = new Champion("Gragas");
	public static final Champion championGraves = new Champion("Graves");
	public static final Champion championHecarim = new Champion("Hecarim");
	public static final Champion championHeimer = new Champion("Heimerdinger");
	public static final Champion championIllaoi = new Champion("Illaoi");
	public static final Champion championIrelia = new Champion("Irelia");
	public static final Champion championJanna = new Champion("Janna");*/
	
	public Champion(String name)
	{
		this.name = name;
		champByName.put(name, this);
	}
	
	public Champion(String name, int id)
	{
		this(name);
		this.id = id;
	}
	
	public String name;
	public int id;
	public StatInfo info;
	public int maxSkins;
	
	public static Champion random()
	{
		Random rand = new Random();
		return ((Champion)champByName.values().toArray()[rand.nextInt(champByName.values().toArray().length)]);
	}
	
	public int randomSkin()
	{
		Random rand = new Random();
		return rand.nextInt(maxSkins) + 1;
	}
	
	public Champion setInfo(StatInfo stats)
	{	
		this.info = stats;
		return this;
	}
	
	public static Champion fromName(String championName)
	{
		return champByName.get(championName);
	}
}
