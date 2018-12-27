package cards;

import base.Card;
import base.Player;

public class Cellar extends AbstractAction {

	public Cellar() {
		super();
		this.goldCost=2;
		this.plusActions=1;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		int number=0;
		boolean wantToDiscard=true;
		do {
			wantToDiscard=false;
			Card c = p.decideToDiscard("+1 card");
			if(c!=null) {
				wantToDiscard=true;
				p.discard(c);
				number++;
			}
		}while((!p.hand.isEmpty()) && wantToDiscard);
		
		p.draw(number);
		
		return true;
	}

}
