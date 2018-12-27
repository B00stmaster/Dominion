package cards;

import base.Player;
import base.Shop;

public class Witch extends AbstractAttack {

	public Witch() {
		super();
		this.goldCost=5;
		this.plusCards=2;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Shop s = p.partie.theShop;
		for (Player pla: p.partie.joueurs) {
			if (!pla.equals(p)) {
				AbstractCard reaction = pla.askToReact(this);
				if(reaction!=null) {
					switch (reaction.getName()) {
					case "Moat":
						break;
					default:
						if(s.remainingCards("Curse")>0)
							pla.gainToDiscard(s.getCard("Curse"));
						break;
					}
				}
				else {
					if(s.remainingCards("Curse")>0)
						pla.gainToDiscard(s.getCard("Curse"));
				}
			}
		}
		return true;
	}
}
