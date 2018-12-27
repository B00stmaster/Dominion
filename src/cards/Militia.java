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
				AbstractCard reaction = pla.askToReact(this);
				if(reaction!=null) {
					switch (reaction.getName()) {
					case "Moat":
						break;
					default:
						pla.askToDiscard(pla.hand.size()-3);
						break;
					}
				}
				else {
					pla.askToDiscard(pla.hand.size()-3);
				}
			}
		}
		return true;
	}
}
