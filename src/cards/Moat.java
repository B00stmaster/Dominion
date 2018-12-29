package cards;

import base.Player;

public class Moat extends AbstractAction {

	public Moat() {
		super();
		this.types.add(Type.REACTION);
		this.goldCost=2;
		this.plusCards=2;
	}
	
	public boolean onReactToAttack(Player p) {
		super.onReactToAttack(p);
		if(p.partie.visible)
			System.out.println(p.name+" is protected from the attack");
		return true;
	}
}
