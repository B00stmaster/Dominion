import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Decklist extends Vector<Card>{
	Player owner;
	//plein de methodes d'analyse d'une liste de cartes pour pouvoir les evaluer
	
	Decklist(Player p){
		owner = p;		
	}
	
	Decklist(Decklist cop){
		super(cop.size());
		for(int i=0;i<cop.size();i++) {
			this.add(Card.copie(cop.get(i).name));
		}
	}
	
	Decklist simulatedAdd(Card c) {
		Decklist temp = new Decklist(this);
		temp.add(c);
		return temp;
	}
	
	Decklist setupOnDeck(Deck d) {
		Stack temp = new Stack();
		while(!d.isEmpty()) {
			add(d.peek());
			temp.add(d.pop());
		}
		while(!temp.isEmpty()) {
			d.add(temp.pop());
		}
		return this;
	}
	
//	public int probaDePiocher2plusANR() {
//		
//		int n = nActionsNonRenouvelantes();
//		if (n<2)return 0;
//		return 1-probaDePiocher0ANR(n)-probaDePiocher1ANR(n);
//	}

	public double goldDensity() {
		//gold density + gold-adding actions weighted by the probability of playing it
		int TOTAL = 0;
		double prob = 
		for(int i = 0; i<size(); i++) {
			if (get(i).isATreasure()) {
			TOTAL += get(i).goldValue;}
			if (get(i).isAnAction()) {
				TOTAL += get(i).goldValue;
			}
		}
		return (double) TOTAL/size();
	}
	
	public double averageDrawnCards() {
		int TOTAL = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAnAction() ) {
				TOTAL += get(i).cartes;
			}
		}
		return (double) TOTAL/size();
	}
	
	
	public double givenActionDensity() {
		int TOTAL = 0; 
		for (int i = 0; i<size(); i++) {
			TOTAL += get(i).actions;
		}
		return (double) TOTAL/size();
	}
	
	public double givenAchatDensity() {
		int TOTAL = 0; 
		for (int i = 0; i<size(); i++) {

			TOTAL += get(i).achats;
		}
		return (double) TOTAL/size();
	}
	
	public double actionDensity() {
		int TOTAL = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAnAction()) {
				TOTAL ++;
			}
		}
		return (double)TOTAL/size();
	}
	
	public boolean pourraJouerSesActions(int pourcentage) {
		//PB AU DEBUT !! 
		return( actionDensity()/givenActionDensity()<= pourcentage/100);
	}

}
