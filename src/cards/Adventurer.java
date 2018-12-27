package cards;

import java.util.Vector;

import base.Player;

public class Adventurer extends AbstractAction {

	public Adventurer() {
		super();
		this.goldCost=6;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Vector<AbstractCard> found = new Vector<AbstractCard>();
		while(found.size()<2 && !p.deck.isEmpty()) {
			AbstractCard c = p.deck.pop();
			if(c.isA(AbstractCard.Type.TREASURE)) {
				found.add(c);
			}
			else {
				p.board.add(c);
			}
		}
		
		for(AbstractCard c : found) {
			p.hand.add(c);
		}
		return true;
	}
}
