package cards;
public class Curse extends AbstractCard {
	public Curse() {
		super();
		this.VP=-1;
		this.goldCost=0;
	}
	
	public int getStartingNumber(int players) {return 10*(players-1);}
}
