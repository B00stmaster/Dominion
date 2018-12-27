package cards;

import base.Player;

public class CouncilRoom extends AbstractAction {

	public CouncilRoom() {
		super();
		this.goldCost=5;
		this.plusCards=4;
		this.plusBuys=1;
		this.name="Council room";
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		for (Player pla: p.partie.joueurs) {
			if (!pla.equals(p))
				pla.draw();
		}
		return true;
	}
}
