package cards;
import base.Player;

public class Potion extends Silver {
	public Potion() {
		super();
		this.goldCost=4;
		this.plusGold=0;
	}

	public int getPlusPotion(Player p) {return 1;}
	
	public int getStartingNumber(int players) {return 16;}
}
