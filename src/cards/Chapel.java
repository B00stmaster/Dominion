package cards;

import base.Player;

public class Chapel extends AbstractAction {

	public Chapel() {
		super();
		this.goldCost=2;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		int number=0;
		boolean wantToTrash=true;
		do {
			wantToTrash=false;
			AbstractCard c = p.decideToTrash("None");
			if(c!=null) {
				wantToTrash=true;
				p.trash(c);
				number++;
			}
		}while((!p.hand.isEmpty()) && wantToTrash && number<4);
		return true;
	}

}
