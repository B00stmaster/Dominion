
public class Partie {
Player [] joueurs;
Shop theShop;
Player currentPlayer;

Partie(){
	Card.initialise();
	Shop s = new Shop();
	joueurs = new Player [4];
	joueurs[0] = new Player(s);
	joueurs[1] = new Player(s);
	joueurs[2] = new Player(s);
	joueurs[3] = new Player(s);
}


}
