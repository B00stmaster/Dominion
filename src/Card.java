import java.util.Arrays;
import java.util.Vector;

public class Card {
public String name;
public enum Type {TREASURE, VICTORY, ACTION, REACTION, CURSE, ATTACK, VILLAGE, CANTRIP, PEDDLER, NON_TERMINAL, TERMINAL, TERMINAL_SILVER, TRASHER, GAINER, DRAWER};
private Vector<Type> types;
public enum Effet {AUCUN, SORCIERE, CHAMBRE_DU_CONSEIL, PUITS_AUX_SOUHAITS, ESPION};
public Effet effet;
public int cost;
public int VP;
public int plusGold;
public int plusActions;
public int plusBuys;
public int plusCards;
public static Vector<Card> cards = new Vector<Card>();

public Card() {

}

public Card(String n, Type ty, int c, int p, int v, int a, int ach, int ca){
	this(n,new Vector<Type>(Arrays.asList(ty)),c,p,v,a,ach,ca);
}

public Card(String n, Vector<Type> ty, int c, int p, int v, int a, int ach, int ca){
	System.out.println("- "+ n);
	name = n; types = ty; 
	cost = c; 
	VP = p; 
	plusGold = v; plusActions = a; plusBuys = ach; plusCards = ca;
	
	//a gainer is a card that gives you a card without using buy or trashing card
	
	//a terminal card is a card that costs you an action
	if(plusActions==0) types.add(Type.TERMINAL);
	//a non-terminal card is a card that replace its action
	if(plusActions>0) types.add(Type.NON_TERMINAL);
	//a village is a card that increase your action count
	if(plusActions>1) types.add(Type.VILLAGE);
	//a drawer is a card that increases your hand size
	if(plusCards>1) types.add(Type.DRAWER);
	//a cantrip is a card that can be played "free" i.e. it replaces itself and its action
	if((plusActions>0)&&(plusCards>0)) types.add(Type.CANTRIP);
	
	if(isA(Type.CANTRIP)&&(plusGold>0)) types.add(Type.PEDDLER);

	//a terminal silver is a card that increase gives 2 gold but costs you an action
	if((plusActions==0)&&(plusGold==2)) types.add(Type.TERMINAL_SILVER);
}

public Card(Card c){
	name = c.name; types = (Vector<Type>) c.types.clone(); cost = c.cost; VP = c.VP; plusGold = c.plusGold; plusActions = c.plusActions; plusBuys = c.plusBuys; plusCards = c.plusCards;
}

public boolean isA(Card.Type t) {return (types.contains(t));} 

public String toString() {return name;}

static void initialise() {
	System.out.println("Creation des Cartes : ");
	System.out.println("Tresors");
	Card cuivre = new Card("Cuivre", Type.TREASURE, 0, 0, 1, 0,0,0);
	Card argent = new Card("Argent",Type.TREASURE , 3, 0, 2, 0,0,0);
	Card or = new Card("Or", Type.TREASURE, 6, 0, 3, 0,0,0);
	cards.add(cuivre); cards.add(argent); cards.add(or);
	System.out.println("Victoire");
	Card domaine = new Card("Domaine", Type.VICTORY, 2, 1, 0, 0,0,0);
	Card duche = new Card("Duche", Type.VICTORY, 5, 3, 0, 0,0,0);
	Card province = new Card("Province", Type.VICTORY, 8, 6, 0, 0,0,0);
	cards.add(domaine); cards.add(duche); cards.add(province);
	Card village = new Card("Village", Type.ACTION, 3,0,0,2,0,1);
	cards.add(village);
	Card forgeron = new Card("Forgeron", Type.ACTION, 4,0,0,0,0,3 );
	cards.add(forgeron);
	Card marche = new Card("Marche", Type.ACTION, 5,0,1,1,1,1);
	cards.add(marche);
	Card bucheron = new Card("Bucheron", Type.ACTION, 3,0 ,2,0,1,0);
	cards.add(bucheron);
	Card laboratoire = new Card("Laboratoire", Type.ACTION, 5, 0,0,1,0,2);
	cards.add(laboratoire);
	Card festival = new Card("Festival", Type.ACTION, 5, 0,2,2,1,0);
	cards.add(festival);
	Card sorciere = new Card("Sorciere", Type.ACTION, 5,0,0,0,0,2);
	sorciere.effet = Effet.SORCIERE;
	cards.add(sorciere);
	Card malediction = new Card("Malediction", Type.CURSE, 0, -1, 0, 0,0,0);
	cards.add(malediction);
	Card chambreDuConseil = new Card("Chambre du Conseil", Type.ACTION, 5,0,0,0,1,4);
	chambreDuConseil.effet = Effet.CHAMBRE_DU_CONSEIL;
	cards.add(chambreDuConseil);
	Card puitsAuxSouhaits = new Card("Puits aux Souhaits",Type.ACTION, 3,0,0,1,0,1);
	puitsAuxSouhaits.effet = Effet.PUITS_AUX_SOUHAITS;
	cards.add(puitsAuxSouhaits);
	Card espion = new Card("Espion",Type.ACTION, 4,0,0,1,0,1);
	espion.effet = Effet.ESPION;
	cards.add(espion);
}

//pointe vers la liste de TOUTES LES CARTES i.e. ne pas modifier!
public static Card getCardByName(String name) {
	for (int i= 0; i<cards.size(); i++) {
		Card c0 = cards.get(i);		
		if (c0.name == name) {
			return c0; 
		}
	}
	return null;
}

public static Card copie(String Name) {
	for (int i= 0; i<cards.size(); i++) {
		Card c0 = cards.get(i);
		if (c0.name == Name) {
			return new Card(c0);
		}
	}
	return null;
}

public boolean equals(Object o) {
	if (o == null) return false;
    if (!(o instanceof Card))return false;
	return ((Card) o).name.compareTo(this.name)==0;
}

public void playedBy(Player p) {
	p.board.add(p.hand.retire(this));
	if(this.isA(Type.ACTION)) p.leftActions--;
	p.leftActions+=this.plusActions;
	p.leftBuys+=this.plusBuys;
	p.leftGold+=this.plusGold;
	for(int i=0;i<this.plusCards;i++) {
		p.draw();
	}
	switch (name) {
	case "Sorciere":
		sorciere(p);
		break;
	case "Chambre du Conseil":
		chambreDuConseil(p);
		break;
	case "Puits aux Souhaits":
		Card.puitsAuxSouhaits(p);
		break;
	case "Espion":
		Card.espion(p);	
		break;
	default:
		break;
	}
	System.out.println(p.name+" plays "+this+ " | actions left: "+p.leftActions+" | buys left: "+p.leftBuys+" | gold left: "+p.leftGold);
}

public static void sorciere(Player p) {
	Partie gam=p.partie;
	for (int i = 0; i< Partie.NJOUEURS; i++) {
		if (gam.joueurs[i] != p) {
			gam.joueurs[i].defausse.add(gam.theShop.getCard("Malediction"));
			gam.joueurs[i].decklist.add(getCardByName("Malediction"));
		}
	}
}

public static void chambreDuConseil(Player p) {
	Partie gam=p.partie;
	for (int i = 0; i< Partie.NJOUEURS; i++) {
		if (gam.joueurs[i] != p) {
			gam.joueurs[i].draw();
		}
	}
}

public static void puitsAuxSouhaits(Player p) {
	Card [] choosables = choosables(p); //fonction unique qui marche pour toutes les cartes qui font choisir parmi des cartes
	Card c = choosables[0]; //CHOIX DU JOUEUR !!! après on aura une fonction non débile pou choisir
	Card c0 = p.deck.peek();
	System.out.println("scry 1 : " + c0.name);
	System.out.println("j'avais parié sur : " + c.name);
	if (c0.name == c.name) {
		p.draw();
	}
}

public static void espion(Player p) {
	boolean[] discard = p.decideEspion();
	for (int i = 0; i<Partie.NJOUEURS; i++) {
		if (discard[i]) p.partie.joueurs[i].mill();
	}
}

static public Card [] choosables(Player p) {
	Card [] liste = new Card[cards.size()];
	for (int i = 0; i<cards.size(); i++) {
		liste[i] = cards.get(i);
	}
	return liste;
}

}
