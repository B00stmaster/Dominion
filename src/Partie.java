
public class Partie {
Player [] joueurs;
Shop theShop;
int iCurrentPlayer;

Partie(){
	Card.initialise();
	Shop s = new Shop();
	theShop = s;
	joueurs = new Player [4];
	joueurs[0] = new Player(s);
	joueurs[1] = new Player(s);
	joueurs[2] = new Player(s);
	joueurs[3] = new Player(s);
	for (int i = 0; i<4; i++) {
		joueurs[i].newHand();
	}
}

void joueUnTourComplet() {
	for (int i = 0; i<4; i++) {
		System.out.println("tour du joueur " + i);
		joueurs[i].tourDeJeu();
		System.out.println(joueurs[i]);
		System.out.println(theShop);
	}
}



public static void main(String[] args) {
	Partie p = new Partie();
	p.joueUnTourComplet();
	
}

}
