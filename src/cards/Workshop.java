package cards;

import java.util.Vector;

import base.Player;

public class Workshop extends AbstractAction {

	public Workshop() {
		super();
		this.goldCost=3;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Vector<AbstractCard> choices = p.partie.theShop.buyables(p,4);
		AbstractCard choice = p.chooseToGain(choices);
		if(choice!=null) {
			p.gainToDiscard(p.partie.theShop.getCard(choice));	
		}
		return true;
	}

}
