package cards;

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
			AbstractCard reaction = pla.askToReact(this);
			if(reaction!=null) {
				switch (reaction.getName()) {
				case "Moat":
					break;
				default:
					if(p.decideSpy(pla,pla.reveal())) {
						pla.mill();
					}
					break;
				}
			}
			else {
				if(p.decideSpy(pla,pla.reveal())) {
					pla.mill();
				}
			}
		}
		return true;
	}
}
