
public class Apprentissage {
	static final int NOMBRE_DE_PARTIES = 1000;
	static final int NOMBRE_DEVOLUTIONS = 100;
static boolean wannaPrint = false;
	
	Apprentissage(){
	}
	
	Player meilleurJoueur(Constantes C) {
		Partie p = new Partie(C);
		int j0 = 0;
		int j1 = 0;
		int j2 = 0; 
		int j3 = 0;
		Player gagnant = p.partie(Partie.NOMBRE_DE_TOURS);
		for (int k = 0; k<NOMBRE_DE_PARTIES;k++) {
				p = p.reinitialise();	
				Player g = p.partie(Partie.NOMBRE_DE_TOURS);
				
				if (g.id == 0) {j0++;}
				if (g.id == 1) {j1++;}
				if (g.id == 2) {j2++;}
				if (g.id == 3) {j3++;}
				}
		System.out.println("j0 : " + j0);
		System.out.println("j1 : " + j1);
		System.out.println("j2 : " + j2);
		System.out.println("j3 : " + j3);
		int max= j0;
		Player reponse = p.joueurs[0];
		if (j1>max) {max = j1; reponse = p.joueurs[1];}
		if (j2>max) {max = j2; reponse = p.joueurs[2];}
		if (j3>max) {max = j3; reponse = p.joueurs[3];}

		return reponse;
		}

	Constantes constantesUpdated(Constantes C) {
		 return meilleurJoueur(C).C;
		}
//	
//	void initialisationNouvellePartie() {
//		p.theShop = new Shop();
//		
//	}
//	
	
	public static void main(String[] args) {
		Card.initialise();
		

		Apprentissage a = new Apprentissage();
		Constantes Cgen = a.constantesUpdated(new Constantes());
		System.out.println(new Constantes());
		System.out.println(Cgen);
		for (int i = 0; i<NOMBRE_DEVOLUTIONS; i++) {
		Cgen = a.constantesUpdated(Cgen);
		System.out.println(Cgen);}
		
		Partie p = new Partie();
		p.joueurs[0].C = Cgen;
		p.joueurs[1].C = new Constantes();
		p.joueurs[2].C = new Constantes();
		p.joueurs[3].C = new Constantes();
		wannaPrint = true;
		int compteur = 0;
		for (int i = 0; i<100; i++) {
			int ID = p.partie(Partie.NOMBRE_DE_TOURS).id;
			if (ID == 0) {compteur++;}
		System.out.println(ID);
		p = p.reinitialise();
		}
		System.out.println("le joueur crée a gagné " + compteur +" % des parties" );
		
	}
}
