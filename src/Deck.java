
public class Deck extends Stack{
Player owner;
Stack decklist; //pas encore crée... 

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

///ANR : action non renouvelante = ne donne pas d'autres actions
// le but c'est de quantifier la proba de pouvoir jouer une carte action donnée en fonction du nombre d'actions


///ATTENTION,COMPTE SEULEMENT DANS LE DECK (au sens bibliotheque),
// faut creer une liste des cartes du deck ! 
public int nActionsNonRenouvelantes() {
	//compte les actions qui n'en donnent pas d'autres
	int reponse = 0;
	for (int i =0; i<NCartes; i++) {
		if (cartes[i].type == Card.Type.ACTION && cartes[i].actions == 0) {
			reponse++;
		}
	}
	return reponse;
}

public int probaDePiocher0ANR(int nANR) {
	
	return ((NCartes-nANR)*(NCartes-nANR-1)*(NCartes-nANR-2)*(NCartes-nANR-3)*(NCartes-nANR-4))/(NCartes*(NCartes-1)*(NCartes-2)*(NCartes-3)*(NCartes-4));
}

public int probaDePiocher1ANR(int nANR) {
	return 5*(nANR*(NCartes-nANR)*(NCartes-nANR-1)*(NCartes-nANR-2)*(NCartes-nANR-3)/(NCartes*(NCartes-1)*(NCartes-2)*(NCartes-3)*(NCartes-4)));
}

public int probaDePiocher2plusANR() {
	int n = nActionsNonRenouvelantes();
	return 1-probaDePiocher0ANR(n)-probaDePiocher1ANR(n);
}

public double goldDensity() {
	//densité en or + celle des cartes actions si la probabilité de pouvoir la jouer est >= .8
	int TOTAL = 0;
	double p = probaDePiocher2plusANR();
	for(int i = 0; i<NCartes; i++) {
		if (cartes[i].type == Card.Type.TRESOR || cartes[i].type == Card.Type.VICTOIRE) {
		TOTAL += cartes[i].value;}
		if (cartes[i].type == Card.Type.ACTION && p < Constante.k1) {
			TOTAL += cartes[i].value;
		}
	}
	return (double) TOTAL/NCartes;
}

}
