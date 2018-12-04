package base;
public class Board extends AbstractZone{
	
	Board(Player p){
		super(p);
	}

	public String toString() {
		String s = "Contenu de la zone de jeu "+ getClass().getName() + "@" + Integer.toHexString(hashCode()) + ":\n";
		return s+=super.toString();
	}
	
	//similar to .pop() in a stack
	public Card popACard() {
		return remove(this.size()-1);
	}
}
