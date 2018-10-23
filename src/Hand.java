import java.util.Vector;

public class Hand extends Vector<Card>{
Player owner;

Hand(Player p){
	super();
	owner=p;
}

public String toString() {
	String s = owner.name + " hand content:" + "\n";
	for (int i =0; i<size();i++) {
		s += get(i).name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	return s;
}

public Card retire(Card c) {
	//CARE c must be precisely one of the cards in hand (i.e. must not be a copy)
	return remove(indexOf(c));
}

}
