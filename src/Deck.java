
public class Deck extends Stack{
Player owner;

Deck(Player p) {
	super();
	owner=p;
	Shop s=p.theShop;
	for(int i=0;i<7;i++) add(s.getCard("Cuivre"));
	for(int i=0;i<3;i++) add(s.getCard("Domaine"));
}

public String toString() {
	String s = "Contenu du deck :" + "\n";
	for (int i =0; i<NCartes;i++) {
		s += cartes[i].name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	return s;
}

}
