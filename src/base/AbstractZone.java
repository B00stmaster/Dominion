package base;
import java.util.Vector;

import cards.AbstractCard;

public abstract class AbstractZone  extends Vector<AbstractCard>{
	Player owner;

	AbstractZone(Player p){
		super();
		owner=p;
	}

	public String toString() {
		String s = "";
		for (int i =0; i<size();i++) {
			s += get(i).getName() + "  |  " ;
			if (i%7 == 0 && i !=0) {
				s+= "\n";
			}
		}
		return s;
	}

	//you CAN count cards that doesn't exist...
	public int cardCount(String name) {
		int total = 0;
		for (AbstractCard c: this) {
			if (c.getName().equals(name)) total ++;
		}
		return total;
	}

	public int typeCount(AbstractCard.Type t) {
		int total = 0;
		for (AbstractCard c: this) {
			if (c.isA(t)) total ++;
		}
		return total;
	}
	
	public AbstractCard findA(String name) {
		for (AbstractCard c : this) {
			if (c.getName().equals(name)) return c;
		}
		return null;
	}
	
	public AbstractCard retire(AbstractCard c) {
		//CARE c must be precisely one of the cards (i.e. must not be a copy)
		return remove(indexOf(c));
	}
}
