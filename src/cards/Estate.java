package cards;

public class Estate extends AbstractCard {
	public Estate() {
		super();
		this.types.add(Type.VICTORY);
		this.goldCost=2;
		this.VP=1;
	}
	
	public int getStartingNumber(int players) {
		if(players<=2)
			return 8;
		return 12;
	}
}
