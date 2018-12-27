package base;
import java.util.Vector;

public abstract class AbstractZone  extends Vector<Card>{
	Player owner;

	AbstractZone(Player p){
		super();
		owner=p;
	}

	public String toString() {
		String s = "";
		for (int i =0; i<size();i++) {
			s += get(i).name + "  |  " ;
			if (i%7 == 0 && i !=0) {
				s+= "\n";
			}
		}
		return s;
	}

	//you CAN count cards that doesn't exist...
	public int cardCount(String name) {
		int total = 0;
		for (Card c:this) {
			if (c.name.equals(name)) total ++;
		}
		return total;
	}

	public int typeCount(Card.Type t) {
		int total = 0;
		for (Card c: this) {
			if (c.isA(t)) total ++;
		}
		return total;
	}
	
	public Card findA(String name) {
		for (Card c:this) {
			if (c.name.equals(name)) return c;
		}
		return null;
	}
	
	public Card retire(Card c) {
		//CARE c must be precisely one of the cards (i.e. must not be a copy)
		return remove(indexOf(c));
	}
}
