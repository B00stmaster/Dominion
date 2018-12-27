package base;
import java.util.Arrays;
import java.util.Vector;

public class Card {
public String name;
public enum Type {TREASURE, VICTORY, ACTION, REACTION, CURSE, ATTACK, VILLAGE, CANTRIP, PEDDLER, NON_TERMINAL_ACTION, TERMINAL_ACTION, TERMINAL_SILVER, TRASHER, GAINER, DRAWER};
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

public Card(String name, Type ty, int cost, int vpoints, int pG, int pA, int pAch, int pC){
	this(name,new Vector<Type>(Arrays.asList(ty)),cost,vpoints,pG,pA,pAch,pC);
}

public Card(String n, Vector<Type> ty, int cost, int vpoints, int pG, int pA, int pAch, int pC){
	System.out.println("- "+ n);
	name = n; types = ty; 
	this.cost = cost; 
	VP = vpoints; 
	plusGold = pG; plusActions = pA; plusBuys = pAch; plusCards = pC;
	
	//a gainer is a card that gives you a card without using buy or trashing card
	
	//a terminal action is an action that costs you an action
	if(plusActions==0 && ty.contains(Type.ACTION)) types.add(Type.TERMINAL_ACTION);
	//a non-terminal action is an action that replace its action
	if(plusActions>0 && ty.contains(Type.ACTION)) types.add(Type.NON_TERMINAL_ACTION);
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
	cards.clear();
	System.out.println("Creation des Cartes : ");
	System.out.println("Tresors:");
	cards.add(new Card("Cuivre", Type.TREASURE, 0, 0, 1, 0,0,0));
	cards.add(new Card("Argent",Type.TREASURE , 3, 0, 2, 0,0,0));
	cards.add(new Card("Or", Type.TREASURE, 6, 0, 3, 0,0,0));
	System.out.println("Victoire:");
	cards.add(new Card("Domaine", Type.VICTORY, 2, 1, 0, 0,0,0));
	cards.add(new Card("Duche", Type.VICTORY, 5, 3, 0, 0,0,0));
	cards.add(new Card("Province", Type.VICTORY, 8, 6, 0, 0,0,0));
	System.out.println("Royaume:");
	cards.add(new Card("Village", Type.ACTION, 3,0,0,2,0,1));
	cards.add(new Card("Forgeron", Type.ACTION, 4,0,0,0,0,3 ));
	cards.add(new Card("Marche", Type.ACTION, 5,0,1,1,1,1));
	cards.add(new Card("Bucheron", Type.ACTION, 3,0 ,2,0,1,0));
	cards.add(new Card("Laboratoire", Type.ACTION, 5, 0,0,1,0,2));
	cards.add(new Card("Festival", Type.ACTION, 5, 0,2,2,1,0));
	cards.add(new Card("Sorciere", new Vector<Type>(Arrays.asList(new Type[]{Type.ACTION,Type.ATTACK})), 5,0,0,0,0,2));
	cards.add(new Card("Malediction", Type.CURSE, 0, -1, 0, 0,0,0));
	cards.add(new Card("Milice", new Vector<Type>(Arrays.asList(new Type[]{Type.ACTION,Type.ATTACK})), 4,0,2,0,0,0));
	cards.add(new Card("Chambre du Conseil", Type.ACTION, 5,0,0,0,1,4));
	cards.add(new Card("Puits aux Souhaits",Type.ACTION, 3,0,0,1,0,1));
	cards.add(new Card("Espion",new Vector<Type>(Arrays.asList(new Type[]{Type.ACTION,Type.ATTACK})), 4,0,0,1,0,1));
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
	p.leftActions+=this.plusActions;
	p.leftBuys+=this.plusBuys;
	p.leftGold+=this.plusGold;
	p.draw(this.plusCards);
	System.out.println(p.name+" plays "+this+ " | actions left: "+p.leftActions+" | buys left: "+p.leftBuys+" | gold left: "+p.leftGold);
	switch (name) {
	case "Sorciere":
		sorciere(p);
		break;
	case "Chambre du Conseil":
		chambreDuConseil(p);
		break;
	case "Puits aux Souhaits":
		puitsAuxSouhaits(p);
		break;
	case "Espion":
		espion(p);	
		break;
	case "Milice":
		milice(p);	
		break;
	default:
		break;
	}
}

public static void sorciere(Player p) {
	Partie gam=p.partie;
	for (int i = 0; i<p.partie.joueurs.length; i++) {
		if (gam.joueurs[i] != p && gam.theShop.remainingCards("Malediction")>0) {
			gam.joueurs[i].defausse.add(gam.theShop.getCard("Malediction"));
			//oui la ligne du dessous est inutilement compliquee mais servira en cas de traduction du jeu
			System.out.println(gam.joueurs[i].name+" reçoit "+Card.getCardByName("Malediction").name);
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
	Card [] choosables = choosables(); //fonction unique qui marche pour toutes les cartes qui font choisir parmi des cartes
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
	for (int i = 0; i<p.partie.joueurs.length; i++) {
		if (discard[i]) p.partie.joueurs[i].mill();
	}
}

public static void milice(Player p) {
	for (int i = 0; i<p.partie.joueurs.length; i++) {
		if (!p.partie.joueurs[i].equals(p)) {
			p.partie.joueurs[i].askToDiscard(p.partie.joueurs[i].hand.size()-3);
		}
	}
}

static public Card[] choosables() {
	Card [] liste = new Card[cards.size()];
	for (int i = 0; i<cards.size(); i++) {
		liste[i] = cards.get(i);
	}
	return liste;
}

}
