
public class Shop {
static final int SHOP_SIZE=100;
static Stack [] avalaible = new Stack[SHOP_SIZE];
static int nItems = 14; //pour pouvoir parcourir le shop sans avoir de null (?)

Shop(){ 
	avalaible[0]=new Stack("Cuivre", 50);
	avalaible[1]=new Stack("Argent", 50);
	avalaible[2]=new Stack("Or", 30);
	avalaible[3]=new Stack("Domaine", 30);
	avalaible[4]=new Stack("Duche", 30);
	avalaible[5]=new Stack("Province", 15);
	avalaible[6]=new Stack("Village", 11);
	avalaible[7]= new Stack("Forgeron", 11);
	avalaible[8]=new Stack("Marche", 11);
	avalaible[9] = new Stack("Bucheron", 11);
	avalaible[10] = new Stack("Laboratoire",11);
	avalaible[11] = new Stack("Festival", 11);
	avalaible[12] = new Stack("Sorciere", 11);
	avalaible[13] = new Stack("Malediction", 50);
}

private Stack findStack(String name) {
	for(int i=0;i<SHOP_SIZE;i++) {
		if(avalaible[i].peek().name.compareTo(name)==0)
			return avalaible[i];
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
	for (int i = 0; i<nItems; i++) {
		if (avalaible[i].size() <= 1) {
			reponse++;
		}
	}
	return reponse;
}

int remainingProvinces() {
	return findStack("Province").size()-1;
}

boolean provincesRemain() {	
	return remainingProvinces()>0 ;
}


public String toString() {
	String s = "contenu du Shop : " + "\n";
	for (int i = 0; i<nItems; i++) {
		if (avalaible[i].size() >0) {
		s +=( avalaible[i].size() -1)+ " - " + avalaible[i].peek().name + "\n";
		}
	}
	s+= "Piles vides : " + nombrePilesVides();
	s+= "Il reste des provinces : "+ provincesRemain();
	return s;
}

}
