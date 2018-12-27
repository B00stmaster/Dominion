package cards;

import base.Card;
import base.Player;

public class ThroneRoom extends AbstractAction {

	public ThroneRoom() {
		super();
		this.goldCost=4;
		this.name="Throne room";
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Card playedTwice = p.chooseToPlay();
		//playedTwice.onPlay(p);
		//playedTwice.onPlay(p);
		return true;
	}

}
