package base;
public class Hand extends AbstractZone{

	Hand(Player p){
		super(p);
	}

	public String toString() {
		String s = owner.name+"'s hand content :\n";
		return s+=super.toString();
	}
}
