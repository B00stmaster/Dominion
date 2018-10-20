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

	//bullshit methods FIND A WAY TO GET RID OF THEM
	private double playActionProbability(double playActionConstant) {return Math.min(playActionConstant*givenActionDensity()/typeDensity(Card.Type.ACTION),100);}
	
	private double playActionProbability() {return playActionProbability(0.7);}
	
	public double goldDensity() {
		//gold density + gold-adding actions weighted by the approximated probability of playing it
		int total = 0;
		for(int i = 0; i<size(); i++) {
			if (get(i).isA(Card.Type.TREASURE)) {
				total += get(i).plusGold;}
			else if (get(i).isA(Card.Type.ACTION)) {
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
	
	public int typeCount(Card.Type t) {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).isA(t)) {
				total ++;
			}
		}
		return total;
	}
	
	public double typeDensity(Card.Type t) {return (double)typeCount(t)/size();}
		
}
