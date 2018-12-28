package base;

import java.util.Scanner;
import java.util.Vector;

import cards.AbstractCard;
import cards.AbstractCard.Type;

public class HumanPlayer extends Player {
	
	Scanner input;
	
	public HumanPlayer() {
		input = new Scanner(System.in);
	}
	
	public HumanPlayer(String name) {
		this();
		this.name=name;
	}

	private AbstractCard genericChooser(Vector<AbstractCard> available, String msg) {
		if(available.isEmpty())
			return null;
		System.out.println(msg);
		for(int i=0;i<available.size();i++) {
			if(available.get(i)==null)
				System.out.println(Integer.toString(i)+" - None");
			else
				System.out.println(Integer.toString(i)+" - "+available.get(i));
		}
		int answer = -1;
		do {
			System.out.print("--> ");
			answer = input.nextInt();
		}while(answer<0 || answer>=available.size());
		return available.get(answer);
	}
	
	private boolean binaryChooser(String question) {
		System.out.println(question+" (y/n)");
		String answer;
		do {
			System.out.print("--> ");
			answer = input.next();
		}while((!answer.equals("y")) && (!answer.equals("n")));
		return (answer.equals("y"));
	}
	
	public AbstractCard chooseToPlay() {
		Vector<AbstractCard> playables = playables();
		if(playables.isEmpty())
			return null;
		playables.add(null);
		return genericChooser(playables, "Choose an action to play:");
	}

	public AbstractCard chooseToGain(Vector<AbstractCard> available) {return genericChooser(available, "Choose a card to gain:");}

	public AbstractCard chooseToDiscard(Vector<AbstractCard> available)  {return genericChooser(available, "Choose a card to discard:");}

	public AbstractCard decideToDiscard(Vector<AbstractCard> availablen, String benefit) {
		Vector<AbstractCard> available = new Vector<AbstractCard>(availablen);
		available.add(null);
		return genericChooser(available, "You can discard a card. If you do, "+benefit);
	}

	public boolean decideToDiscard(AbstractCard c, String benefit) {
		Vector<AbstractCard> available = new Vector<AbstractCard>(2);
		available.add(c);
		return decideToDiscard(available, benefit)!=null;
	}

	public AbstractCard chooseToTrash(Vector<AbstractCard> available) {return genericChooser(available, "Choose a card to trash:");}

	public AbstractCard decideToTrash(Vector<AbstractCard> availablen,String benefit) {
		Vector<AbstractCard> available = new Vector<AbstractCard>(availablen);
		available.add(null);
		return genericChooser(available, "You can discard a card. If you do, "+benefit);
	}

	public boolean decideToTrash(AbstractCard c, String benefit) {
		Vector<AbstractCard> available = new Vector<AbstractCard>(2);
		available.add(c);
		return decideToTrash(available, benefit)!=null;
	}

	public AbstractCard askToReact(AbstractCard att) {
		Vector<AbstractCard> available = new Vector<AbstractCard>(5);
		for(AbstractCard c : hand) {
			if(c.isA(Type.REACTION))
				available.add(c);
		}
		if(available.isEmpty())
			return null;
		available.add(null);
		return genericChooser(available, "You can react to "+att+" with the following cards:");
	}

	public boolean decideToReshuffle() {return binaryChooser("Do you want to put you deck in you discard pile?");}

	public boolean decideSpy(Player pla, AbstractCard shown) { return binaryChooser("Do you want "+pla.name+" to discard "+shown+" from deck?");}
	
	public String toString() {return "Human"+super.toString();}
}
