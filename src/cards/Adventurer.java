package cards;

import java.util.Vector;

import base.Card;
import base.Player;

public class Adventurer extends AbstractAction {

	public Adventurer() {
		super();
		this.goldCost=6;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Vector<Card> found = new Vector<Card>();
		while(found.size()<2 && !p.deck.isEmpty()) {
			Card c = p.deck.pop();
			if(c.isA(Card.Type.TREASURE)) {
				found.add(c);
			}
			else {
				p.board.add(c);
			}
		}
		
		for(Card c : found) {
			p.hand.add(c);
		}
		return true;
	}
}
