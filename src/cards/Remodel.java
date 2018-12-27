package cards;

import java.util.Vector;

import base.Card;
import base.Player;

public class Remodel extends AbstractAction {

	boolean needExactCost;
	
	public Remodel() {
		this.goldCost=4;
		needExactCost=false;
	}
	
	private int newCost(int oldCost) {
		return oldCost + 2;
	}
	
	boolean canUpgrade(Player p, AbstractCard oldCard, AbstractCard newCard) {
		if(needExactCost)
			return newCard.getGoldCost(p)==newCost(oldCard.getGoldCost(p));
		return newCard.getGoldCost(p)<=newCost(oldCard.getGoldCost(p));
	}
	
	boolean canUpgrade(Player p, Card oldCard, Card newCard) {
		return newCard.cost<=newCost(oldCard.cost);
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
			//p.gain(newChoice);
		}
		return true;
	}

}
