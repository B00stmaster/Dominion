package cards;

public class Silver extends AbstractCard {
	public Silver() {
		super();
		this.goldCost=3;
		this.plusGold=2;
		this.types.add(Type.TREASURE);
	}

}
