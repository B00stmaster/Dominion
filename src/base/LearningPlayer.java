package base;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import cards.AbstractCard;
import cards.AbstractCard.Type;

public class LearningPlayer extends AIPlayer implements Serializable {

	double[][] constants;
	
	public LearningPlayer() {
		super("BM*");
		constants = new double[20][6];
		for(int i=0;i<constants.length;i++) {
			for(int j=0;j<constants[i].length;j++) {
				constants[i][j]=100*(Math.random()+Math.random()-1);
			}
		}
	}
	
	public LearningPlayer(double[][] cons) {
		super("BM*");
		constants=cons;
		}

	public LearningPlayer copy() {
		double[][] cons = new double[constants.length][];
		for(int i=0;i<constants.length;i++)
			cons[i]=constants[i].clone();
		return(new LearningPlayer(cons));
	}
	
	protected double alter(double d) {
		return d*(0.80+(Math.random()+Math.random())/5.0)+(Math.random()-0.5);
	}
	
	public void alter() {
		for(int i=0;i<constants.length;i++) {
			for(int j=0;j<constants[i].length;j++) {
				constants[i][j]=alter(constants[i][j]);
			}
		}
	}
	
    public void save(String name) {
        try {
        	String path = "D:\\Come\\Documents\\GitHub\\Dominion\\saved_AI\\"+name;
        	FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(constants);
            objectOut.close();
            System.out.println("The player constants were succesfully saved to "+path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static LearningPlayer recover(String name) {
        try {
        	String path = "D:\\Come\\Documents\\GitHub\\Dominion\\saved_AI\\"+name;
	    	FileInputStream fi = new FileInputStream(new File(path));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			// Read objects
			double[][] cons = (double[][]) oi.readObject();
	
			oi.close();
			fi.close();
			
			return (new LearningPlayer(cons));
	
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return null;
    }
    
	public void print() {
		System.out.println("Learning player "+name+" : ");
		System.out.println(Arrays.deepToString(constants).replace("], ", "]\n"));
	}
	
	protected int cardNumber(AbstractCard c) {
		if(constants.length!=partie.theShop.avalaible.size())
			System.out.println("ERROR in the number of constants!");
		for(int i=0;i<partie.theShop.avalaible.size();i++) {
			if(partie.theShop.avalaible.get(i).peek().getName().equals(c.getName()))
				return i;
		}
		return -1;
	}
	
	protected int valueCard(String cardName, String stratName, Shop theShop) {return valueCard(partie.theShop.findCard(cardName));}
	
	protected int valueCard(AbstractCard c) {
		
		double opponentAttackRatio=0;
		for(Player p : partie.joueurs)
			opponentAttackRatio+=p.decklist.typeDensity(Type.ATTACK);
		opponentAttackRatio/=partie.joueurs.length;
		double[] values = new double[] {gameProgress(),decklist.goldDensity(),decklist.typeDensity(Type.TERMINAL_ACTION),decklist.typeDensity(Type.REACTION), opponentAttackRatio, decklist.typeDensity(Type.TRASHER)};
		
		int cardNb = cardNumber(c);
		int res=(int) constants[cardNb][0];
		for(int i=0;i<values.length;i++)
			res+=(int)(constants[cardNb][i+1]*values[i]);
		return res;
	}
	
	//aggregated indicator that range from 0 at the beginning of the game to 1 at the end
	protected double gameProgress() {
		Shop s = partie.theShop;
		double provinceRatio = ((double) s.remainingCards("Province"))/s.getStartingNumber("Province");
		
		Vector<Double> pileRatios = new Vector<Double>(s.avalaible.size());
		for(Stack st : s.avalaible) {
			AbstractCard c = st.peek();
			pileRatios.add(((double) s.remainingCards(c))/s.getStartingNumber(c));
		}
		double[] smallerPiles = new double[3];
		for(int i=0;i<3;i++) {
			double minValue = Collections.min(pileRatios);
			smallerPiles[i]=minValue;
			pileRatios.remove(minValue);
		}
		double pileRatio = (smallerPiles[0]+smallerPiles[1]+smallerPiles[2])/3.0;
		return 1-Math.min(provinceRatio, pileRatio);
	}

	
	public static void main(String [] args) {
		LearningPlayer toTest = recover("epoch_6_best_fitness_89");
		toTest.print();

	}
}
	
