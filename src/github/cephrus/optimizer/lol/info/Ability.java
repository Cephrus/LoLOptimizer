package github.cephrus.optimizer.lol.info;

public class Ability
{
	public int cooldown;
	public String name;
	public String description;
	
	/** Only if the ability is an innate Champion passive, not an ability passive. */
	public boolean isPassive;
	
	public Ability(String name, String description, int cooldown, boolean passive)
	{
		this(name, cooldown, passive);
		this.description = description;
	}
	
	public Ability(String name, int cooldown, boolean passive)
	{
		this.isPassive = passive;
		this.name = name;
		this.cooldown = cooldown;
	}
}
