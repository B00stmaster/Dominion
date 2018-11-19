import java.util.Vector;

public class Shop {
Vector<Stack> avalaible;

<<<<<<< HEAD
Shop(){ 
	avalaible[0]=new Stack("Cuivre", 50);
	avalaible[1]=new Stack("Argent", 50);
	avalaible[2]=new Stack("Or", 30);
	avalaible[3]=new Stack("Domaine", 30);
	avalaible[4]=new Stack("Duche", 30);
	avalaible[5]=new Stack("Province", 15);
	avalaible[6]=new Stack("Village", 11);
	avalaible[7]=new Stack("Forgeron", 11);
	avalaible[8]=new Stack("Marche", 11);
	avalaible[9] = new Stack("Bucheron", 11);
	avalaible[10] = new Stack("Laboratoire",11);
	avalaible[11] = new Stack("Festival", 11);
	avalaible[12] = new Stack("Sorciere", 11);
	avalaible[13] = new Stack("Malediction", 50);
=======
Shop(int players){ 
	avalaible = new Vector<Stack>();
	avalaible.add(new Stack("Cuivre", 50));
	avalaible.add(new Stack("Argent", 50));
	avalaible.add(new Stack("Or", 30));
	avalaible.add(new Stack("Domaine", 30));
	avalaible.add(new Stack("Duche", 30));
	avalaible.add(new Stack("Province", 4*players+1));
	avalaible.add(new Stack("Village", 11));
	avalaible.add(new Stack("Forgeron", 11));
	avalaible.add(new Stack("Marche", 11));
	avalaible.add(new Stack("Bucheron", 11));
	avalaible.add(new Stack("Laboratoire",11));
	avalaible.add(new Stack("Festival", 11));
	avalaible.add(new Stack("Sorciere", 11));
	avalaible.add(new Stack("Milice", 11));
	avalaible.add(new Stack("Malediction", 10*(players-1)));
>>>>>>> 2b3299fa6d00be8271138fa2db7dd12f949e5fb2
}

private Stack findStack(String name) {
	for(int i=0;i<avalaible.size();i++) {
		if(avalaible.get(i).peek().name.compareTo(name)==0)
			return avalaible.get(i);
	}
	return null;
}

public Card getCard(String name) {
	Stack s = findStack(name);
	if(s != null && s.size()>1) {
		return s.pop();
	}
	return null;
}

int nombrePilesVides() {
	int reponse = 0;
	for (int i = 0; i<avalaible.size(); i++) {
		if (avalaible.get(i).size() <= 1) {
			reponse++;
		}
	}
	return reponse;
}

public int remainingCards(Card c) {
	return remainingCards(c.name);
}
public int remainingCards(String cardName) {
	return findStack(cardName).size()-1;
}

int remainingProvinces() {
	return findStack("Province").size()-1;
}

boolean provincesRemain() {	
	return remainingProvinces()>0 ;
}


public String toString() {
	String s = "contenu du Shop : " + "\n";
	for (int i = 0; i<avalaible.size(); i++) {
		if (avalaible.get(i).size()>0) {
		s +=( avalaible.get(i).size() -1)+ " - " + avalaible.get(i).peek().name + "\n";
		}
	}
	s+= "Piles vides : " + nombrePilesVides();
	s+= "Il reste des provinces : "+ provincesRemain();
	return s;
}

}
