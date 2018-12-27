package cards;

import java.util.Vector;

import base.Card;
import base.Player;

public class Workshop extends AbstractAction {

	public Workshop() {
		super();
		this.goldCost=3;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		Vector<Card> choices = p.partie.theShop.buyables(4);
		Card choice = p.chooseToGain(choices);
		if(choice!=null) {
			//p.gainToDiscard(choice);	
		}
		return true;
	}

}
