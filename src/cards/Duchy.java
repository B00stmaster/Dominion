package cards;

import base.Player;

public class Duchy extends Estate {

	public Duchy() {
		super();
		this.goldCost=5;
		this.VP=3;
	}
	
	public boolean onGain(Player p) {
		if(p.partie.theShop.remainingCards("Duchess")>0) {
			System.out.println("Player "+p+" can gain a Duchess!..");
			return true;
		}
		return false;
	}
}
