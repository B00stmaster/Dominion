package cards;

public class Platinum extends Silver {

	public Platinum() {
		super();
		this.goldCost=9;
		this.plusGold=5;
	}
	
	public int getStartingNumber(int players) {return 12;}

}
