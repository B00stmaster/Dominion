package cards;

import base.Card;
import base.Player;

public class Peddler extends AbstractAction {

	public Peddler() {
		super();
		this.goldCost=8;
		this.plusActions=1;
		this.plusCards=1;
	}
	
	public int getGoldCost(Player p) {
		return Math.max(goldCost-2*p.board.typeCount(Card.Type.ACTION)-p.board.cardCount("Pont"),0);
	}

}
