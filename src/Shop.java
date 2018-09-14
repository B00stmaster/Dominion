
public class Shop {
static final int SHOP_SIZE=100;
static Stack [] avalaible = new Stack[SHOP_SIZE];
static int nItems = 4; //pour pouvoir parcourir le shop sans avoir de null (?)

Shop(){
	Stack cuivres = new Stack("Cuivre", 50);
	avalaible[0]=cuivres;
	Stack argents = new Stack("Argent", 50);
	avalaible[1]=argents;
	Stack or = new Stack("Or", 30);
	avalaible[2]=or;
	Stack domaines = new Stack("Domaine", 30);
	avalaible[3]=domaines;
}

private Stack findStack(String name) {
	for(int i=0;i<SHOP_SIZE;i++) {
		if(avalaible[i].peek().name.compareTo(name)==0)
			return avalaible[i];
	}
	return null;
}

//Stack findStack(Card c) {
//	for(int i=0;i<SHOP_SIZE;i++) {
//		System.out.println(avalaible[i].peek().name);
//		if(avalaible[i].peek().name.compareTo(c.name)==0)
//			return avalaible[i];
//	}
//	return null;
//}

public Card getCard(String name) {
	Stack s = findStack(name);
	if(s != null && s.size()>1) {
		return s.pop();
	}
	return null;
}


public String toString() {
	String s = "contenu du Shop : " + "\n";
	for (int i = 0; i<nItems; i++) {
		if (avalaible[i].NCartes >1) {
		s += avalaible[i].NCartes + " - " + avalaible[i].peek().name + "\n";
		}
	}
	return s;
}

}
