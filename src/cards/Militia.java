package cards;

import base.Player;

public class Militia extends AbstractAttack {

	public Militia() {
		super();
		this.goldCost=4;
		this.plusGold=2;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		for (Player pla: p.partie.joueurs) {
			if (!pla.equals(p)) {
				if(!pla.askToReact(this).name.equals("Moat"))
					pla.askToDiscard(pla.hand.size()-3);
			}
		}
		return true;
	}
}
