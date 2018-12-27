package cards;

import java.util.Vector;

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

	public boolean onPlay(Player p) {
		super.onPlay(p);
		AbstractCard oldChoice = null;
		AbstractCard newChoice = null;
		Vector<AbstractCard> shopAvailable = p.partie.theShop.buyables();
		int upgradeValue= -100000;
		for(AbstractCard c : p.hand) {
			for(AbstractCard a : shopAvailable) {
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
			p.gainToDiscard(newChoice);
		}
		return true;
	}

}
