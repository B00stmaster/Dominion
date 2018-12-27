package cards;

import base.Player;

public class Chancellor extends AbstractAction {

	public Chancellor() {
		super();
		this.goldCost=3;
		this.plusGold=2;
	}
	
	public boolean onPlay(Player p) {
		super.onPlay(p);
		if(p.decideToReshuffle()) {
			System.out.println(p+" décide de placer son deck dans la défausse");
			while(!p.deck.isEmpty()) {
				p.defausse.add(p.deck.pop());
			}
		}
		else
			System.out.println(p+" décide de garder son deck");
		return true;
	}

}
