package cards;

public class Estate extends AbstractCard {
	public Estate() {
		super();
		this.goldCost=2;
		this.VP=1;
		this.types.add(Type.VICTORY);
	}
}
