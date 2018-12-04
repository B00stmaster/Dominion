package base;
public class Hand extends AbstractZone{

	Hand(Player p){
		super(p);
	}

	public String toString() {
		String s = "Contenu de la main "+ getClass().getName() + "@" + Integer.toHexString(hashCode()) + ":\n";
		return s+=super.toString();
	}
}
