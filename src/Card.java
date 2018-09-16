
public class Card {
public String name;
public enum Type {TRESOR, VICTOIRE, ACTION};
public Type type;
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
	
}


public static Card getCardByName(String Name) {
	Card c = new Card();
	for (int i= 0; i<totalCards; i++) {
		Card c0 = cards[i];
		if (c0.name == Name) {
			return c; 
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


}
