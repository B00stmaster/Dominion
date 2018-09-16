
public class Shop {
static final int SHOP_SIZE=100;
static Stack [] avalaible = new Stack[SHOP_SIZE];
static int nItems = 12; //pour pouvoir parcourir le shop sans avoir de null (?)

Shop(){
	Stack cuivres = new Stack("Cuivre", 50);
	avalaible[0]=cuivres;
	Stack argents = new Stack("Argent", 50);
	avalaible[1]=argents;
	Stack or = new Stack("Or", 30);
	avalaible[2]=or;
	Stack domaines = new Stack("Domaine", 30);
	avalaible[3]=domaines;
	Stack duches = new Stack("Duche", 30);
	avalaible[4]=duches;
	Stack provinces = new Stack("Province", 15);
	avalaible[5]=provinces;
	Stack villages = new Stack("Village", 10);
	avalaible[6]=villages;
	Stack forgerons = new Stack("Forgeron", 10);
	avalaible[7]= forgerons;
	Stack marches = new Stack("Marche", 10);
	avalaible[8]=marches;
	Stack bucherons = new Stack("Bucheron", 10);
	avalaible[9] = bucherons;
	Stack laboratoires = new Stack("Laboratoire",10);
	avalaible[10] = laboratoires;
	Stack festivals = new Stack("Festival", 10);
	avalaible[11] = festivals;

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
		if (avalaible[i].NCartes <= 1) {
			reponse++;
		}
	}
	return reponse;
}

int provincesRestantes() {
	return findStack("Province").NCartes-1;
}

boolean ilResteDesProvinces() {	
	return findStack("Province").NCartes >1 ;
}


public String toString() {
	String s = "contenu du Shop : " + "\n";
	for (int i = 0; i<nItems; i++) {
		if (avalaible[i].NCartes >0) {
		s +=( avalaible[i].NCartes -1)+ " - " + avalaible[i].peek().name + "\n";
		}
	}
	s+= "piles vides : " + nombrePilesVides();
	s+= "il reste des provinces : "+ ilResteDesProvinces();
	return s;
}

}