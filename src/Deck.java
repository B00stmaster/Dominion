
public class Deck extends Stack{
Player owner;
Decklist decklist; 



Deck(Player p) {
	super();
	owner=p;
	Shop s=p.theShop;
	decklist = new Decklist(p);
	for(int i=0;i<7;i++) add(s.getCard("Cuivre"));
	for(int i=0;i<3;i++) add(s.getCard("Domaine"));
	for (int i = 0; i<this.NCartes; i++) {
		decklist.add(cartes[i]);
	}
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



///ANR : action non renouvelante = ne donne pas d'autres actions
// le but c'est de quantifier la proba de pouvoir jouer une carte action donnée en fonction du nombre d'actions


///ATTENTION,COMPTE SEULEMENT DANS LE DECK (au sens bibliotheque),
// faut creer une liste des cartes du deck ! 


double goldDensity() {
	return decklist.goldDensity();
}

}
