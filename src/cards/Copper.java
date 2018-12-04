package cards;

import base.Player;

public class Copper extends Silver {
	public Copper() {
		super();
		this.goldCost=0;
		this.plusGold=1;
	}
	
	public int getPlusGold(Player p) {
		return plusGold+p.board.cardCount("Coppersmith");
	}
}
