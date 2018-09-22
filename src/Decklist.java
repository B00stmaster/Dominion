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
	
	public double proportion(Card c){
		int total = 0; 
		for (int i =0; i< size() ;i++){
			if (get(i).name == c.name){total++;} 

		} 
		return (double) total/size(); 
	} 

	private double playActionProbability(double playActionConstant) {return Math.min(playActionConstant*givenActionDensity()/actionDensity(),100);}
	
	private double playActionProbability() {return playActionProbability(0.7);}
	
	public double goldDensity() {
		//gold density + gold-adding actions weighted by the approximated probability of playing it
		int total = 0;
		for(int i = 0; i<size(); i++) {
			if (get(i).isATreasure()) {
				total += get(i).plusGold;}
			else if (get(i).isAnAction()) {
				total += get(i).plusGold*playActionProbability();}
		}
		return (double) total/size();
	}
	
	public double givenCardsDensity() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			total += get(i).plusCards;
		}
		return (double) total/size();
	}
		
	public double givenActionDensity() {
		int total = 0; 
		for (int i = 0; i<size(); i++) {
			total += get(i).plusActions;
		}
		return (double) total/size();
	}
	
	public double givenAchatDensity() {
		int total = 0; 
		for (int i = 0; i<size(); i++) {

			total += get(i).plusBuys;
		}
		return (double) total/size();
	}
	
	public int actionCount() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAnAction()) {
				total ++;
			}
		}
		return total;
	}
	
	public double actionDensity() {return (double)actionCount()/size();}
	
	public int treasureCount() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isATreasure()) {
				total ++;
			}
		}
		return total;
	}
	
	public double treasureDensity() {return (double)treasureCount()/size();}
	
	public int victoryCount() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAVictory()) {
				total ++;
			}
		}
		return total;
	}
	
	public double victoryDensity() {return (double)victoryCount()/size();}
	
	public int reactionCount() {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isAReaction()) {
				total ++;
			}
		}
		return total;
	}
	
	public double reactionDensity() {return (double)reactionCount()/size();}
	

}
