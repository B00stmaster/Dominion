package base;
import java.util.Vector;

import cards.*;
import cards.AbstractCard.Type;

public class Shop {
public Vector<Stack> avalaible;

Shop(int players){ 
	avalaible = new Vector<Stack>();
	avalaible.add(new Stack(new Copper(), 61));
	avalaible.add(new Stack(new Silver(), 51));
	avalaible.add(new Stack(new Gold(), 31));
	avalaible.add(new Stack(new Estate(), 31));
	avalaible.add(new Stack(new Duchy(), 31));
	avalaible.add(new Stack(new Province(), 13));
	avalaible.add(new Stack(new Village(), 11));
	avalaible.add(new Stack(new Smithy(), 11));
	avalaible.add(new Stack(new Market(), 11));
	avalaible.add(new Stack(new Woodcutter(), 11));
	avalaible.add(new Stack(new Laboratory(),11));
	avalaible.add(new Stack(new Festival(), 11));
	avalaible.add(new Stack(new Witch(), 11));
	avalaible.add(new Stack(new Militia(), 11));
	avalaible.add(new Stack(new Garden(), 11));
	avalaible.add(new Stack(new Mine(), 11));
	avalaible.add(new Stack(new Cellar(), 11));
	avalaible.add(new Stack(new Chancellor(), 11));
	avalaible.add(new Stack(new Chapel(), 11));
	avalaible.add(new Stack(new CouncilRoom(), 11));
	avalaible.add(new Stack(new Library(), 11));
	avalaible.add(new Stack(new Moat(), 11));
	avalaible.add(new Stack(new MoneyLender(), 11));
	avalaible.add(new Stack(new Peddler(), 11));
	avalaible.add(new Stack(new Remodel(), 11));
	avalaible.add(new Stack(new Spy(), 11));
	avalaible.add(new Stack(new ThroneRoom(), 11));
	avalaible.add(new Stack(new Woodcutter(), 11));
	avalaible.add(new Stack(new Workshop(), 11));
	avalaible.add(new Stack(new Curse(), 10*(players-1)+1));
	System.out.println("Contenu du Shop:");
	for(Stack st : avalaible) {
		st.peek().generateAdditionalTypes();
		if(st.peek().isA(Type.VICTORY)) {
			if(players<=2)
				st.resize(9);
			else
				st.resize(13);
		}
		System.out.println("- "+st.peek()+" : "+remainingCards(st.peek()));
	}
}

private Stack findStack(String name) {
	for(int i=0;i<avalaible.size();i++) {
		if(avalaible.get(i).peek().getName().equals(name))
			return avalaible.get(i);
	}
	return null;
}

public Vector<AbstractCard> buyables() {
	Vector<AbstractCard> result = new Vector<AbstractCard>(10);
	for (int i = 0; i< avalaible.size(); i++) {
		if (avalaible.get(i).size()>1) { //TO ADD: check isBuyable
			result.add(avalaible.get(i).peek());
		}
	}
	return result;
}

public Vector<AbstractCard> buyables(Player p, int maxPrice) {
	Vector<AbstractCard> result = new Vector<AbstractCard>(10);
	for (int i = 0; i< avalaible.size(); i++) {
		if (avalaible.get(i).size()>1 && avalaible.get(i).peek().getGoldCost(p)<=maxPrice) { //TO ADD: check isBuyable
			result.add(avalaible.get(i).peek());
		}
	}
	return result;
}

public AbstractCard getCard(String name) {
	Stack s = findStack(name);
	if(s != null && s.size()>1) {
		return s.pop();
	}
	return null;
}

public AbstractCard getCard(AbstractCard c) {return getCard(c.getName());}

int nombrePilesVides() {
	int reponse = 0;
	for (int i = 0; i<avalaible.size(); i++) {
		if (avalaible.get(i).size() <= 1) {
			reponse++;
		}
	}
	return reponse;
}

public int remainingCards(AbstractCard c) {
	return remainingCards(c.getName());
}
public int remainingCards(String cardName) {
	Stack res=findStack(cardName);
	if(res!=null)
		return res.size()-1;
	return 0;
}

public String toString() {
	String s = "contenu du Shop : " + "\n";
	for (int i = 0; i<avalaible.size(); i++) {
		if (avalaible.get(i).size()>0) {
		s +=( avalaible.get(i).size() -1)+ " - " + avalaible.get(i).peek().getName() + "\n";
		}
	}
	s+= "Piles vides : " + nombrePilesVides();
	return s;
}

}
