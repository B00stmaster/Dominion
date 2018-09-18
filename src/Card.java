
public class Card {
public String name;
public enum Type {TRESOR, VICTOIRE, ACTION};
public Type type;
public enum Effet {AUCUN, SORCIERE, CHAMBRE_DU_CONSEIL, PUITS_AUX_SOUHAITS, ESPION};
public Effet effet;
public int cost;
public int VP;
public int value;
public int actions;
public int achats;
public int cartes;
public static Card[] cards = new Card[100];
public static int totalCards;

Card(String n, Type t, int c, int p, int v, int a, int ach, int ca){
	System.out.println("- "+ n);
	name = n; type = t; 
	cost = c; 
	VP = p; 
	value = v; 
	actions = a; achats = ach; cartes = ca; 
}

Card(Card c){
	name = c.name; type = c.type; cost = c.cost; VP = c.VP; value = c.value; actions = c.actions; achats = c.achats; cartes = c.cartes;
}

public String toString() {
	return name;
}

public Card() {

}

static void add(Card c){
	cards[totalCards] = c;
	totalCards++;
}

static void initialise() {
	System.out.println("Creation des Cartes : ");
	System.out.println("Tresors");
	Card cuivre = new Card("Cuivre", Type.TRESOR, 0, 0, 1, 0,0,0);
	Card argent = new Card("Argent",Type.TRESOR , 3, 0, 2, 0,0,0);
	Card or = new Card("Or", Type.TRESOR, 6, 0, 3, 0,0,0);
	add(cuivre); add(argent); add(or);
	System.out.println("Victoire");
	Card domaine = new Card("Domaine", Type.VICTOIRE, 2, 1, 0, 0,0,0);
	Card duche = new Card("Duche", Type.VICTOIRE, 5, 3, 0, 0,0,0);
	Card province = new Card("Province", Type.VICTOIRE, 8, 6, 0, 0,0,0);
	add(domaine); add(duche); add(province);
	Card village = new Card("Village", Type.ACTION, 3,0,0,2,0,1);
	add(village);
	Card forgeron = new Card("Forgeron", Type.ACTION, 4,0,0,0,0,3 );
	add(forgeron);
	Card marche = new Card("Marche", Type.ACTION, 5,0,1,1,1,1);
	add(marche);
	Card bucheron = new Card("Bucheron", Type.ACTION, 3,0 ,2,0,1,0);
	add(bucheron);
	Card laboratoire = new Card("Laboratoire", Type.ACTION, 5, 0,0,1,0,2);
	add(laboratoire);
	Card festival = new Card("Festival", Type.ACTION, 5, 0,2,2,1,0);
	add(festival);
	Card sorciere = new Card("Sorciere", Type.ACTION, 5,0,0,0,0,2);
	sorciere.effet = Effet.SORCIERE;
	add(sorciere);
	Card malediction = new Card("Malediction", Type.VICTOIRE, 0, -1, 0, 0,0,0);
	add(malediction);
	Card chambreDuConseil = new Card("Chambre du Conseil", Type.ACTION, 5,0,0,0,1,4);
	chambreDuConseil.effet = Effet.CHAMBRE_DU_CONSEIL;
	add(chambreDuConseil);
	Card puitsAuxSouhaits = new Card("Puits aux Souhaits",Type.ACTION, 3,0,0,1,0,1);
	puitsAuxSouhaits.effet = Effet.PUITS_AUX_SOUHAITS;
	add(puitsAuxSouhaits);
	Card espion = new Card("Espion",Type.ACTION, 4,0,0,1,0,1);
	espion.effet = Effet.ESPION;
	add(espion);
}


public static Card getCardByName(String Name) {
	Card c = new Card();
	for (int i= 0; i<totalCards; i++) {
		Card c0 = cards[i];		
		if (c0.name == Name) {
			return c0; 
		}
	}
	return null;
}

public static Card copie(String Name) {
	Card c = new Card();
	for (int i= 0; i<totalCards; i++) {
		Card c0 = cards[i];
		if (c0.name == Name) {
			c = new Card(c0);
		}
	}
	return c;
}

public boolean equals(Object o) {
	if (o == null) return false;
    if (!(o instanceof Card))return false;
	return ((Card) o).name.compareTo(this.name)==0;
}

public static void sorciere(Partie p, Player j) {
	for (int i = 0; i< Partie.NJOUEURS; i++) {
		if (p.joueurs[i] != j) {
			p.joueurs[i].defausse.add(p.theShop.getCard("Malediction"));
			p.joueurs[i].deck.decklist.add(getCardByName("Malediction"));
		}
	}
}

public static void chambreDuConseil(Partie p, Player j) {
	for (int i = 0; i< Partie.NJOUEURS; i++) {
		if (p.joueurs[i] != j) {
			p.joueurs[i].draw();
		}
	}
}
public static void puitsAuxSouhaits(Card c, Player j) {
	Card c0 = j.deck.peek();
	System.out.println("scry 1 : " + c0.name);
	System.out.println("j'avais parié sur : " + c.name);
	if (c0.name == c.name) {
		j.draw();
	}
}

public static void espion(Partie p, boolean [] b) {
	if (b.length != Partie.NJOUEURS) {System.err.println("tableau b pas de la bonne taille");}
	for (int i = 0; i<Partie.NJOUEURS; i++) {
		if (b[i]) {p.joueurs[i].mill();}
	}
}


public Card [] choosables(Partie p, Player j) {
	if (effet == Effet.PUITS_AUX_SOUHAITS) {
		Card [] liste = new Card[totalCards];
		for (int i = 0; i<totalCards; i++) {
			liste[i] = cards[i];
		}
		return liste;
	}
	return null;
}

}
