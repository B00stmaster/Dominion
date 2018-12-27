package cards;

import base.Card;
import base.Player;

public class Spy extends AbstractAttack {

	public Spy() {
		super();
		this.goldCost=4;
		this.plusCards=1;
		this.plusActions=1;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		for (Player pla: p.partie.joueurs) {
			if(!pla.askToReact(this).name.equals("Moat")) {
				Card shown = pla.reveal();
				if(p.decideSpy(pla,shown)) {
					pla.mill();
				}
			}
		}
		return true;
	}
}
