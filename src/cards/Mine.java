package cards;

import java.util.Vector;

import base.Player;

public class Mine extends AbstractRemodeler {

	public Mine() {
		super();
		this.goldCost=5;
		needExactCost=false;
		isMandatory=false;
	}
	protected int newCost(int oldCost) {return oldCost + 3;}
	
	boolean canUpgrade(Player p, AbstractCard oldCard, AbstractCard newCard) {
		return (oldCard.isA(Type.TREASURE) && newCard.isA(Type.TREASURE) && newCard.getGoldCost(p)<=newCost(oldCard.getGoldCost(p)));
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
	
		AbstractCard trashedCard=null;
		AbstractCard gainedCard=null;
		Vector<AbstractCard> availableToTrash = availableToTrash(p);
		if(availableToTrash!=null) {
			trashedCard=p.chooseToTrash(availableToTrash);
			Vector<AbstractCard> availableToGain = selectAvailable(p, trashedCard);
			gainedCard = p.chooseToGain(availableToGain);
			if(gainedCard!=null) {
				p.trash(trashedCard);
				p.gainToHand(p.partie.theShop.getCard(gainedCard));
			}
			return true;
		}
		return false;
	}
		
}
