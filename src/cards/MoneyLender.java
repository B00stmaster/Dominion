package cards;

import base.Player;

public class MoneyLender extends AbstractAction {

	public MoneyLender() {
		super();
		this.goldCost=4;
		this.name="Money lender";
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		if(p.hand.cardCount("Copper")>0) {
			AbstractCard cop = p.hand.findA("Copper");
			if(p.decideToTrash(cop, "+ 3 gold")) {
				p.trash(cop);
				p.leftGold+=3;
			}
		}
		return true;
	}
}
