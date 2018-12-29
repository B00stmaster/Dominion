package cards;

import java.util.Vector;
import base.Player;

public class Remodel extends AbstractRemodeler {
	
	public Remodel() {
		this.goldCost=4;
		needExactCost=false;
		isMandatory=true;
	}
	
	protected int newCost(int oldCost) {return oldCost + 2;}

	public boolean onPlay(Player p) {
		super.onPlay(p);
	
		AbstractCard trashedCard=null;
		AbstractCard gainedCard=null;
		Vector<AbstractCard> availableToTrash = availableToTrash(p);
		if(!availableToTrash.isEmpty()) {
			trashedCard=p.chooseToTrash(availableToTrash);
			Vector<AbstractCard> availableToGain = selectAvailable(p, trashedCard);
			gainedCard = p.chooseToGain(availableToGain);
			if(gainedCard!=null) {
				p.trash(trashedCard);
				p.gainToDiscard(p.partie.theShop.getCard(gainedCard));
			}
			return true;
		}
		return false;
	}

}
