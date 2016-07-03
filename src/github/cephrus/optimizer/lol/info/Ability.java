package github.cephrus.optimizer.lol.info;

public class Ability
{
	public String cooldown;
	public String name;
	public String description;
	public String cost;
	
	/** Only if the ability is an innate Champion passive, not an ability passive. */
	public boolean isPassive;
	
	public Ability(String name, String description, String cooldown, String cost)
	{
		this(name, description, cooldown);
		this.cost = cost;
	}
	
	public Ability(String name, String description, String cooldown)
	{
		this(name, description, false);
		this.cooldown = cooldown;
	}
	
	public Ability(String name, String description, boolean passive)
	{
		this.isPassive = passive;
		this.name = name;
		this.description = description;
	}
}
