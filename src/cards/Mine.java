package cards;

import java.util.Vector;
import base.Card;
import base.Player;

public class Mine extends Remodel {

	public Mine() {
		super();
		this.goldCost=5;
	}
	private int newCost(int oldCost) {
		return oldCost + 3;
	}
	
	boolean canUpgrade(Player p, AbstractCard oldCard, AbstractCard newCard) {
		return (oldCard.isA(Type.TREASURE) && newCard.isA(Type.TREASURE) && newCard.getGoldCost(p)<=newCost(oldCard.getGoldCost(p)));
	}
	
	boolean canUpgrade(Player p, Card oldCard, Card newCard) {
		return (oldCard.isA(Card.Type.TREASURE) && newCard.isA(Card.Type.TREASURE) && newCard.cost<=newCost(oldCard.cost));
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Card oldChoice = null;
		Card newChoice = null;
		Vector<Card> shopAvailable = p.partie.theShop.buyables();
		int upgradeValue= -100000;
		for(Card c : p.hand) {
			for(Card a : shopAvailable) {
				if(canUpgrade(p, c, a)) {
					int score = p.valueToTrash(c) + p.valueCard(a);
					if(score>upgradeValue) {
						upgradeValue=score;
						oldChoice=c;
						newChoice=a; 
					}
				}
			}
		}
		if(upgradeValue>0) {
			p.trash(oldChoice);
			//p.gainToHand(newChoice);
		}
		return true;
	}

}
