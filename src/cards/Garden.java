package cards;

import base.Player;

public class Garden extends Estate {

	public Garden() {
		super();
		this.goldCost=4;
	}
	
	public int getVP(Player p) {
		return (int) Math.floor(p.decklist.size()/10.0);
	}

}
