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

	private double playActionProbability(double playActionConstant) {return Math.min(playActionConstant*givenActionDensity()/actionDensity(),100);}
	
	private double playActionProbability() {return playActionProbability(0.7);}
	
	public double goldDensity() {
		//gold density + gold-adding actions weighted by the approximated probability of playing it
		int total = 0;
		double prob = playActionProbability();
		for(int i = 0; i<size(); i++) {
			if (get(i).isATreasure()) {
				total += get(i).goldValue;}
			if (get(i).isAnAction()) {
				total += get(i).goldValue*prob;
			}
		}
		return (double) total/size();
	}
	
	public double averageDrawnCards() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAnAction() ) {
				total += get(i).cartes;
			}
		}
		return (double) total/size();
	}
	
	
	public double givenActionDensity() {
		int total = 0; 
		for (int i = 0; i<size(); i++) {
			total += get(i).actions;
		}
		return (double) total/size();
	}
	
	public double givenAchatDensity() {
		int total = 0; 
		for (int i = 0; i<size(); i++) {

			total += get(i).achats;
		}
		return (double) total/size();
	}
	
	public double actionDensity() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAnAction()) {
				total ++;
			}
		}
		return (double)total/size();
	}
	
	public boolean pourraJouerSesActions(int pourcentage) {
		//PB AU DEBUT !! 
		return( actionDensity()/givenActionDensity()<= pourcentage/100);
	}

}
