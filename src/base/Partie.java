package base;

import java.util.Vector;

public class Partie {
public Player [] joueurs;
public Shop theShop;
public int iCurrentPlayer;
public boolean hasEnded = false;
public static final int NJOUEURS = 4;
public static final int NOMBRE_DE_PARTIES = 1000;
public static final int NOMBRE_DE_TOURS = 17;


Partie(){	
	Shop s = new Shop(NJOUEURS);
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(this);
	joueurs[0].number = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(this,joueurs[0].C, true);
	joueurs[i].number = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}

Partie(int players){	
	Shop s = new Shop(players);
	theShop = s;
	joueurs = new Player [players];
	for (int i = 0; i<players; i++) joueurs[i] = new Player(this);
	for (int i = 0; i<players; i++) joueurs[i].newHand();
}

Partie(String[] strats){	
	Shop s = new Shop(strats.length);
	theShop = s;
	joueurs = new Player[strats.length];
	for (int i = 0; i<joueurs.length; i++) joueurs[i] = new Player(this,strats[i]);
	for (int i = 0; i<joueurs.length; i++) joueurs[i].newHand();
}

Partie(Constantes Co){
	Shop s = new Shop(NJOUEURS);
	theShop = s;
	joueurs = new Player [NJOUEURS];
	joueurs[0] = new Player(this,Co, false);
	joueurs[0].number = 0;
	for (int i = 1; i<NJOUEURS; i++) {
	joueurs[i] = new Player(this,joueurs[0].C, true);
	joueurs[i].number = i;}
	for (int i = 0; i<NJOUEURS; i++) {
		joueurs[i].newHand();
	}
}

void joueUnTourComplet(boolean printDetails, int first) {
	for (int i = first; i<4+first; i++) {
		joueurs[i%4].tourDeJeu(printDetails);
	}
}

Player partie(boolean printDetails) {
	int first = (int) (Math.random()*this.joueurs.length);
	int turn=0;
	while(!hasEnded()) {
		joueurs[(turn+first)%this.joueurs.length].tourDeJeu(printDetails);
		turn++;
	}
	System.out.println("===================== PARTIE TERMINEE ===============================");
	for (int i = 0; i<this.joueurs.length; i++) {
		System.out.println("decklist de "+joueurs[i].name+":\n"+joueurs[i].decklist);
		int points = joueurs[i].countVictoryPoints();
		System.out.println(joueurs[i].name+" a " + points + " PV");
	}
	
	int maxVP = -1;
	Vector<Integer> maxPlayers = new Vector<Integer>();
	for (int i = 0; i<this.joueurs.length; i++) {
		int tempVP = joueurs[i].countVictoryPoints();
		if (tempVP>maxVP){
			maxVP = tempVP;
			maxPlayers.clear();
			maxPlayers.add(i);
			}
		else if(tempVP==maxVP)
			maxPlayers.add(i);
	}
	if(maxPlayers.size()==1)
		return joueurs[maxPlayers.get(0)];
	
	int leastTurn = -1;
	Vector<Integer> leastTurnPlayers = new Vector<Integer>();
	for (int j = 0; j<maxPlayers.size(); j++) {
		int tempTurn = (turn-((maxPlayers.get(j)-first)%joueurs.length))/joueurs.length;
		if (tempTurn<leastTurn){
			leastTurn = tempTurn;
			leastTurnPlayers.clear();
			leastTurnPlayers.add(maxPlayers.get(j));
			}
		else if(tempTurn==leastTurn)
			leastTurnPlayers.add(maxPlayers.get(j));
	}
	
	return joueurs[leastTurnPlayers.get((int)(Math.random()*leastTurnPlayers.size()))]; //also works if only one player is in vector (rounds to 0)
}

boolean hasEnded() {
	return theShop.nombrePilesVides()>=3 || (theShop.remainingCards("Province")==0);
}


Partie reinitialise() {
	Partie nouv = new Partie();
	for (int i = 0; i<this.joueurs.length; i++) {
		nouv.joueurs[i].C = joueurs[i].C;
	}
	return nouv;
}

public String toString() {
	String s = "appercu de la partie : "+ "\n";
	s+= "j0 : "+ "\n" +  joueurs[0].decklist.toString();
	s+= "j1 : "+ "\n" +  joueurs[1].decklist.toString();
	s+= "j2 : "+ "\n" +  joueurs[2].decklist.toString();
	s+= "j3 : "+ "\n" +  joueurs[3].decklist.toString();
	return s;
}

public static void main(String[] args) {
	Partie p = new Partie(new String[] {"BM","OptimizedBM","WitchBasicEngine"});
	p.partie(false);
}

}
