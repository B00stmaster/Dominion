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
			if (get(i).isA(t)) total ++;
		}
		return total;
	}
	
	public double typeDensity(Card.Type t) {return (double)typeCount(t)/size();}
	
	public int cardCount(Card c) {
		return cardCount(c.name);
	}
	
	public int cardCount(String name) {
		int total = 0;
		for (int i = 0; i<size(); i++) {
			if (get(i).name.equals(name)) total ++;
		}
		return total;
	}
	
	public double cardDensity(Card c) {
		return cardDensity(c.name);
	}

	public double cardDensity(String name) {return (double)cardCount(name)/size();}
	
	public double noTerminalProb() {
		if(size()<=5) {
			if(typeCount(Card.Type.TERMINAL_ACTION)>0) return 0.;
			return 1.;
		}
		double res=1.;
		for(int i=0;i<5;i++) {
			res*=(1.-(((double)typeCount(Card.Type.TERMINAL_ACTION))/(size()-i)));
		}
		return res;
	}
	
	public double oneTerminalProb() {
		if(size()<=5) {
			if(typeCount(Card.Type.TERMINAL_ACTION)==1) return 1.;
			return 0.;
		}
		double tot=0.;
		for(int j=0;j<5;j++) {
			double temp=1.;
			for(int i=0;i<j;i++) {
				temp*=(1 - ((double) typeCount(Card.Type.TERMINAL_ACTION)) / (size()-i) );
			}
			temp*=( ((double) typeCount(Card.Type.TERMINAL_ACTION)) / (size()-j) );
			for(int i=j+1;i<5;i++) {
				temp*=(1 - ((double) typeCount(Card.Type.TERMINAL_ACTION) - 1.) / (size()-i) );
			}
			tot+=temp;
		}
		return tot;
	}
	
	//gives an exact result only if decklist has no village
	public double terminalCollisionProb() {
		return 1.-oneTerminalProb()-noTerminalProb();
	}
		
}