package base;

import java.util.Vector;

import cards.AbstractCard;

public class AIPlayer extends Player {
	
	public String stratName;
	public AIPlayer() {
		super();
	}

	public AIPlayer(String strat) {
		super();
		stratName=strat;
	}

	public AbstractCard chooseToPlay() {
		//tries to play +actions and then most expensive actions, except if has throne room
			if(hand.cardCount("Throne room")>0)
				return hand.findA("Throne room");
			
			Vector<AbstractCard> playables = playables();
			for (AbstractCard c : playables) {
				if (c.getPlusActions(this)>0) {
					playSomething = true;
					return c;
				}
			}

			if (!playables.isEmpty()){
				playSomething = true;
				int maxCost=-1;
				AbstractCard maxCard=null;
				for (AbstractCard c : playables) {
					if(c.getGoldCost(this)>maxCost) {
						maxCost=c.getGoldCost(this);
						maxCard=c;
					}
				}
				return maxCard;
			}
			playSomething = false;
			return null;
		}
	
	//BUYING/RECEIVING CARDS
	public int valueCard(AbstractCard c) {
		return valueCard(c, stratName);
	}

	public int valueCard(AbstractCard c, String stratName) {
		return valueCard(c.getName(), stratName,this.partie.theShop);
	}

	private int valueCard(AbstractCard c, String stratName, Shop theShop) {
		if(c==null)
			return 0;
		return valueCard(c.getName(), stratName,theShop);
	}
	
	//STRATEGIES CODED HERE. default is BM
	//Higher value means higher priority. Negative value means you prefer to buy nothing
	private int valueCard(String cardName, String stratName, Shop theShop) {
		switch (stratName) {
		case "BM":
			switch (cardName) {
			case "Province":
				return 1000;
			case "Gold":
				return 200;
			case "Silver":
				if(this.decklist.goldDensity()<2) return 100;
				else return -50;
			default:
				return -100;
			}
		case "OptimizedBM":
			switch (cardName) {
			case "Province":
				if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
				else return 1600;
			case "Gold":
				return 200;
			case "Duchy":
				if(theShop.remainingCards("Province")==5) return 150;
				if(theShop.remainingCards("Province")<=4) return 800;
				else return 0;
			case "Silver":
				if(this.decklist.goldDensity()<2) return 100;
				else return -50;
			case "Estate":
				if(theShop.remainingCards("Province")<=2) return 400;
				if(theShop.remainingCards("Province")==3) return 50;
				else return -50;
			default:
				return -100;
			}
		case "SmithyBM":
			switch (cardName) {
			case "Province":
				if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
				else return 1600;
			case "Gold":
				return 200;
			case "Smithy":
				if(this.decklist.cardCount("Smithy")==0 || (this.decklist.size()>=14 && this.decklist.cardCount("Smithy")==1)) return 150;
				else return -50;
			case "Duchy":
				if(theShop.remainingCards("Province")==5) return 150;
				if(theShop.remainingCards("Province")<=4) return 800;
				else return 0;
			case "Silver":
				if(this.decklist.goldDensity()<2) return 100;
				else return -50;
			case "Estate":
				if(theShop.remainingCards("Province")<=2) return 400;
				if(theShop.remainingCards("Province")==3) return 50;
				else return -50;
			default:
				return -100;
			}
		case "BasicEngine":
			switch (cardName) {
			case "Province":
				if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
				else return 1600;
			case "Gold":
				if(this.decklist.cardCount("Gold")==0) return 400;
				else return -50;
			case "Smithy":
				if(this.decklist.cardCount("Smithy")==0) return 300;
				if(this.decklist.cardCount("Village")>=this.decklist.cardCount("Smithy")) return 250;
				else return -50;
			case "Market":
				if(this.decklist.cardCount("Market")<=4) return 350;
				else return -50;
			case "Village":
				if(this.decklist.cardCount("Village")<this.decklist.cardCount("Smithy")) return 250;
				if((this.decklist.cardCount("Village")-this.decklist.cardCount("Smithy"))<=1) return 200;
				else return -50;
			case "Duchy":
				if(theShop.remainingCards("Province")==4) return 150;
				if(theShop.remainingCards("Province")<=3) return 600;
				else return 0;
			case "Silver":
				if(this.decklist.cardCount("Silver")==0) return 200;
				else return -50;
			case "Estate":
				if(theShop.remainingCards("Province")<=2) return 400;
				if(theShop.remainingCards("Province")==3) return 50;
				else return -50;
			default:
				return -100;
			}
		case "MilitiaBasicEngine":
			switch (cardName) {
			case "Province":
				if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
				else return 1600;
			case "Gold":
				if(this.decklist.cardCount("Gold")==0) return 500;
				else return -50;
			case "Militia":
				if(this.decklist.cardCount("Militia")==0) return 400;
				else return -50;
			case "Smithy":
				if(this.decklist.cardCount("Smithy")==0) return 300;
				if(this.decklist.cardCount("Village")>=this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
				else return -50;
			case "Market":
				if(this.decklist.cardCount("Market")<=4) return 350;
				else return -50;
			case "Village":
				if(this.decklist.cardCount("Village")<this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
				if((this.decklist.cardCount("Village")-this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION))<=1) return 200;
				else return -50;
			case "Duchy":
				if(theShop.remainingCards("Province")==4) return 150;
				if(theShop.remainingCards("Province")<=3) return 600;
				else return 0;
			case "Silver":
				if(this.decklist.cardCount("Silver")==0) return 200;
				else return -50;
			case "Estate":
				if(theShop.remainingCards("Province")<=1) return 400;
				if(theShop.remainingCards("Province")==2) return 50;
				else return -50;
			default:
				return -100;
			}
		case "WitchBasicEngine":
			switch (cardName) {
			case "Province":
				if(this.decklist.cardCount("Gold")==0 && this.decklist.cardCount("Silver")<5) return 150;
				else return 1600;
			case "Gold":
				if(this.decklist.cardCount("Gold")==0) return 500;
				else return -50;
			case "Militia":
				if(this.decklist.cardCount("Militia")==0) return 400;
				else return -50;
			case "Witch":
				if(this.decklist.cardCount("Witch")==0) return 450;
				else return -50;
			case "Smithy":
				if(this.decklist.cardCount("Smithy")==0) return 300;
				if(this.decklist.cardCount("Village")>=this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
				else return -50;
			case "Market":
				if(this.decklist.cardCount("Market")<=4) return 350;
				else return -50;
			case "Village":
				if(this.decklist.cardCount("Village")<this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION)) return 250;
				if((this.decklist.cardCount("Village")-this.decklist.typeCount(AbstractCard.Type.TERMINAL_ACTION))<=1) return 200;
				else return -50;
			case "Duchy":
				if(theShop.remainingCards("Province")==4) return 150;
				if(theShop.remainingCards("Province")<=3) return 600;
				else return 0;
			case "Silver":
				if(this.decklist.cardCount("Silver")==0) return 200;
				else return -50;
			case "Estate":
				if(theShop.remainingCards("Province")<=1) return 400;
				if(theShop.remainingCards("Province")==2) return 50;
				else return -50;
			default:
				return -100;
			}
		default:
			System.out.println("Unknown stategy. Defaulting to BM...");
			switch (cardName) {
			case "Province":
				return 1000;
			case "Gold":
				return 200;
			case "Silver":
				if(this.decklist.goldDensity()<2) return 100;
				else return -50;
			default:
				return -100;
			}
		}
	}

	public AbstractCard chooseToGain(Vector<AbstractCard> available) {
		return chooseToGain(available,this.stratName);
	}

	public AbstractCard chooseToGain(Vector<AbstractCard> available,String stratName) {
		return chooseToGain(available,stratName, partie.theShop);
	}

	public AbstractCard chooseToGain(Vector<AbstractCard> available,String stratName, Shop theShop) {
		double noteMax = 0;
		AbstractCard res = null;
		for (AbstractCard c: available) {
			int note = valueCard(c, stratName, theShop);
			//System.out.println("Considering "+c+" note: "+note);
			if (note > noteMax) {
				res = c;
				noteMax = note;
			}
		}
		return res;
	}
	
	//DISCARDING
	//STRATEGIES MATTERS HERE
	//Higher value means higher priority. Negative value means you prefer to discard nothing vs drawing a card
	public int valueToDiscard(AbstractCard c, String stratName) {
		if(c.isA(AbstractCard.Type.VICTORY) && !c.isA(AbstractCard.Type.TREASURE) && !c.isA(AbstractCard.Type.ACTION)) return 1600;
		if(c.getName().equals("Curse")) return 1200;
		if( ((this.hand.typeCount(AbstractCard.Type.TERMINAL_ACTION) - this.hand.typeCount(AbstractCard.Type.VILLAGE))>1) && c.isA(AbstractCard.Type.TERMINAL_ACTION)) return 1000-c.getGoldCost(this)*100;
		if(c.isA(AbstractCard.Type.TREASURE)) {
			//Engine decks prefer to have actions in their hands
			if(stratName.contains("Engine")) return (int) ((this.decklist.goldDensity()+0.8-c.getPlusGold(this))*100);
			else return (int) ((this.decklist.goldDensity()-c.getPlusGold(this))*100);
		}
		if(c.isA(AbstractCard.Type.ACTION)) {
			//Engine decks prefer to have actions in their hands
			if(stratName.contains("Engine")) return (int) (-200*(1-this.decklist.typeDensity(AbstractCard.Type.ACTION)));
			else return -80;
		}
		return -50;
	}

	public int valueToDiscard(AbstractCard c) {
		return valueToDiscard(c, stratName);
	}

	//return the preferred card to discard
	public AbstractCard chooseToDiscard(Vector<AbstractCard> available) {
		return chooseToDiscard(available,this.stratName);
	}

	public AbstractCard chooseToDiscard(Vector<AbstractCard> available, String stratName) {
		double noteMax = -1000000;
		AbstractCard res = null;
		for (int i = 0; i<available.size(); i++) {
			int note = valueToDiscard(available.get(i), stratName);
			if (note > noteMax) {
				res = available.get(i);
				noteMax = note;
			}
		}
		return res;
	}
	
	public AbstractCard decideToDiscard(Vector<AbstractCard> available, String benefit) {
		AbstractCard res=chooseToDiscard(available);
		if(decideToDiscard(res,benefit)) //not optimized
			return res;
		return null;
	}
	
	public boolean decideToDiscard(AbstractCard c,String benefit) {
		switch (benefit.toLowerCase().replaceAll("\\s","")) {
		case "+1card":
			return valueToDiscard(c)>0;
		case "+1gold":
			return valueToDiscard(c)>50;
		case "+2gold":
			return valueToDiscard(c)>0;
		default:
			return valueToDiscard(c)>0;
		}
	}
	
	//TRASHING
	//STRATEGIES MATTERS HERE
	//Higher value means higher priority. Negative value means you prefer to trash nothing without benefit
	public int valueToTrash(AbstractCard c, String stratName) {
		if(c.isA(AbstractCard.Type.CURSE)) return 1600;
		if(c.getName().equals("Estate")) return 400*(4-this.partie.theShop.remainingCards("Province"))-10;
		if(c.isA(AbstractCard.Type.TREASURE)) {
			//Engine decks prefer to have actions in their hands
			if(stratName.contains("Engine")) return (int) ((this.decklist.goldDensity()+0.6-c.getPlusGold(this))*100);
			else return (int) ((this.decklist.goldDensity()-c.getPlusGold(this))*100);
		}
		if(c.isA(AbstractCard.Type.ACTION)) {
			//Engine decks prefer to have actions in their hands
			if(stratName.contains("Engine")) return (int) (-400*(1-this.decklist.typeDensity(AbstractCard.Type.ACTION)));
			else return -80;
		}
		return -50;
	}

	public int valueToTrash(AbstractCard c) {
		return valueToTrash(c, stratName);
	}
	
	public AbstractCard chooseToTrash(Vector<AbstractCard> available) {
		return chooseToTrash(this.hand,this.stratName);
	}

	AbstractCard chooseToTrash(Vector<AbstractCard> available, String stratName) {
		double noteMax = -1000000;
		AbstractCard res = null;
		for (AbstractCard c : available) {
			int note = valueToTrash(c, stratName);
			if (note > noteMax) {
				res = c;
				noteMax = note;
			}
		}
		return res;
	}
	
	public AbstractCard decideToTrash(Vector<AbstractCard> available, String benefit) {
		AbstractCard res=chooseToTrash(available);
		if(decideToTrash(res,benefit)) //not optimized
			return res;
		return null;
	}
	
	public boolean decideToTrash(AbstractCard c,String benefit) {
		switch (benefit.toLowerCase().replaceAll("\\s","")) {
		case "+1card":
			return valueToTrash(c)>-25;
		case "+3gold":
			return valueToTrash(c)>-50;
		case "none":
			return valueToTrash(c)>0;
		default:
			return valueToTrash(c)>0;
		}
	}
	
	public boolean decideToReshuffle() { //TO DO: put a non-trivial decision-maker
		return true;
	}

	//OTHERS
	public AbstractCard askToReact(AbstractCard att) {
		AbstractCard res=null;
		for(AbstractCard c: hand) {
			if(c.isA(AbstractCard.Type.REACTION)) {
				switch (c.getName()) {
				case "Moat":
					res= c;
					break;
				default:
					res=c;
					break;
				}
			}
		}
		if(res!=null) {
			res.onReactToAttack(this);
		}
		return res;
	}
	
	public boolean decideSpy(Player pla,AbstractCard shown) {
		boolean res=true;
		if(shown.isA(AbstractCard.Type.VICTORY) || (shown.isA(AbstractCard.Type.TREASURE)&&(shown.getPlusGold(pla)<=pla.decklist.goldDensity()))) res=false;
		if(pla==this) res = !res;
		return res;
	}

	public String toString() {return "AI "+super.toString();}
	
	static private String pad(String s,int size) {
		if(s.length()>size) {
			System.out.println("ERROR: String too big for padding");
			return s;
		}
		else {
			int a = size - s.length();
			String res="";
			for(int i=0;i<a-a/2;i++) {
				res+=" ";
			}
			res+=s;
			for(int i=0;i<a/2;i++) {
				res+=" ";
			}
			return res;
		}
	}

	public String getStrategyTab(String stratName) {
		Shop fakeShop = new Shop(2);
		String res= "Tab for strategy "+stratName+":\n";
		int temp = res.length();
		res+="                  | ";
		for(int j=8;j>=0;j--) {
			res+=pad(Integer.toString(j)+" gold",12)+" | ";
		}
		res+="\n"+(new String(new char[res.length()-temp]).replace('\0', '-'))+"\n";
		int NProvinces = fakeShop.remainingCards("Province");
		for(int i=NProvinces;i>0;i--) {
			res+=pad(fakeShop.remainingCards("Province")+" provinces left | ",20);
			for(int j=8;j>=0;j--) {
				leftGold=j;
				leftBuys=1;
				AbstractCard c = chooseToGain(buyables(),stratName,fakeShop);
				if(c==null) res+= pad("None",12) +" | ";
				else res+= pad(c.getName(),12) +" | ";
			}
			res+="\n";
			fakeShop.getCard("Province");
		}
		return res;
	}

	public static void main(String [] args) {
		AIPlayer toTest = new AIPlayer("SmithyBM");
		Partie p = new Partie(new Player[] {toTest});
		p.joueurs[0].buy(p.theShop.getCard("Gold"));
		p.joueurs[0].buy(p.theShop.getCard("Smithy"));
		System.out.println(p.joueurs[0].decklist);
		System.out.println("No terminal prob: "+p.joueurs[0].decklist.noTerminalProb());
		System.out.println("One terminal prob: "+p.joueurs[0].decklist.oneTerminalProb());
		System.out.println("Terminal coll prob: "+p.joueurs[0].decklist.terminalCollisionProb());
		System.out.println(toTest.getStrategyTab("SmithyBM"));
	}

}
