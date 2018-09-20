
public class Decklist extends Stack{
	Player owner;
	//plein de methodes d'analyse d'une liste de cartes pour pouvoir les evaluer
	
	Decklist(Player p){
		super();
		owner = p;		
	}
	Decklist(Stack s, Card c, Player p){
		super(s,c);
		owner = p;
	}
	public int nActionsNonRenouvelantes() {
		//compte les actions qui n'en donnent pas d'autres
		int reponse = 0;
		for (int i =0; i<NCartes; i++) {
			if (cartes[i].isAnAction() && cartes[i].actions == 0) {
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

//	public int probaDePiocher2plusANR() {
//		
//		int n = nActionsNonRenouvelantes();
//		if (n<2)return 0;
//		return 1-probaDePiocher0ANR(n)-probaDePiocher1ANR(n);
//	}

	public double goldDensity() {
		//densité en or + celle des cartes actions si la probabilité de pouvoir la jouer est >= .8
		int TOTAL = 0;
		for(int i = 0; i<NCartes; i++) {
			if (cartes[i].isATreasure() || cartes[i].isAVictory()) {
			TOTAL += cartes[i].value;}
			if (cartes[i].isAnAction()) {
				TOTAL += cartes[i].value;
			}
		}
		return (double) TOTAL/NCartes;
	}
	
	public double averageDrawnCards() {
		int TOTAL = 0;
		for (int i = 0; i<NCartes; i++) {
			if (cartes[i].isAnAction() ) {
				TOTAL += cartes[i].cartes;
			}
		}
		
		return (double) TOTAL/NCartes;
	}
	
	
	public double givenActionDensity() {
		int TOTAL = 0; 
		for (int i = 0; i<NCartes; i++) {
			TOTAL += cartes[i].actions;
		}
		return (double) TOTAL/NCartes;
	}
	
	
	public double givenAchatDensity() {
		int TOTAL = 0; 
		for (int i = 0; i<NCartes; i++) {

			TOTAL += cartes[i].achats;
		}
		return (double) TOTAL/NCartes;
	}
	
	public double actionDensity() {
		int TOTAL = 0;
		for (int i = 0; i<NCartes; i++) {
			if (cartes[i].isAnAction()) {
				TOTAL ++;
			}
		}
		return (double)TOTAL/NCartes;
	}
	
	public boolean pourraJouerSesActions(int pourcentage) {
		//PB AU DEBUT !! 
		return( actionDensity()/givenActionDensity()<= pourcentage/100);
	}

	public String toString() {
		String s = "contenu du deck : " + "\n";
		for (int i = 0; i<NCartes; i++) {
			s += cartes[i].name + "\n";
		}
		return s;
	}
	

}
