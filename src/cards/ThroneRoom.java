package cards;

import base.Player;

public class ThroneRoom extends AbstractAction {

	public ThroneRoom() {
		super();
		this.goldCost=4;
		this.name="Throne room";
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		AbstractCard playedTwice = p.chooseToPlay();
		if(playedTwice!=null) {
			p.hand.retire(playedTwice);
			playedTwice.onPlay(p);
			playedTwice.onPlay(p);
		}
		else
			System.out.println("...but doesn't play an action with it");
		return true;
	}

}
