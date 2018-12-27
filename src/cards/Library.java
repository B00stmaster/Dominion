package cards;

import base.Player;

public class Library extends AbstractAction {

	public Library() {
		super();
		this.goldCost=5;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		while((!p.deck.isEmpty()) && p.hand.size()<7){
			AbstractCard drawn = p.deck.pop();
			if(drawn.isA(AbstractCard.Type.ACTION)) {
				if(p.decideToDiscard(drawn,"+ 1 card")) {
					System.out.println(name+" sets "+drawn+" aside");
					p.defausse.add(drawn);
				}
				else {
					System.out.println(name+" draws "+drawn+" and decides to keep it");
					p.hand.add(drawn);
				}
			}
			else {
				System.out.println(name+" draws "+drawn);
				p.hand.add(drawn);
			}
		}
		return true;
	}
}
