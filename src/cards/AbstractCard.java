package cards;

import java.util.EnumSet;

import base.Player;

public class AbstractCard {
	protected String name;
	public enum Type {TREASURE, VICTORY, ACTION, REACTION, CURSE, ATTACK, VILLAGE, CANTRIP, PEDDLER, NON_TERMINAL_ACTION, TERMINAL_ACTION, TERMINAL_SILVER, TRASHER, GAINER, DRAWER};
	protected EnumSet<Type> types;
	protected int goldCost;
	protected int potionCost;
	protected int VP;
	protected int plusGold;
	protected int plusActions;
	protected int plusBuys;
	protected int plusCards;
	
	public AbstractCard() {
		super();
		this.name="";
		this.types = EnumSet.noneOf(Type.class);
		this.goldCost = 0;
		this.potionCost = 0;
		this.VP = 0;
		this.plusGold = 0;
		this.plusActions = 0;
		this.plusBuys = 0;
		this.plusCards = 0;
	}
	
	public boolean generateAdditionalTypes() {
		int previousTypesNb = types.size();
		//a gainer is a card that gives you a card without using buy or trashing card
		
		//a terminal action is an action that costs you an action
		if(plusActions==0 && isA(Type.ACTION)) types.add(Type.TERMINAL_ACTION);
		//a non-terminal action is an action that replace its action
		if(plusActions>0 && isA(Type.ACTION)) types.add(Type.NON_TERMINAL_ACTION);
		//a village is a card that increase your action count
		if(plusActions>1) types.add(Type.VILLAGE);
		//a drawer is a card that increases your hand size
		if(plusCards>1) types.add(Type.DRAWER);
		//a cantrip is a card that can be played "free" i.e. it replaces itself and its action
		if((plusActions>0)&&(plusCards>0)) types.add(Type.CANTRIP);
		
		if(isA(Type.CANTRIP)&&(plusGold>0)) types.add(Type.PEDDLER);

		//a terminal silver is a card that increase gives 2 gold but costs you an action
		if((plusActions==0)&&(plusGold==2)) types.add(Type.TERMINAL_SILVER);
		
		return (types.size()>previousTypesNb);
	}
	
	public boolean isA(AbstractCard.Type t) {return (types.contains(t));}
	
	public String toString() {return getName();}

	public String getName() {
		if(!name.equals(""))
			return name;
		return this.getClass().getName().split("\\.")[1];
	}
	
	public int getGoldCost(Player p) {
		return Math.max(goldCost-p.board.cardCount("Bridge"),0);
	}
	
	public int[] getCost(Player p) {
		return (new int[] {getGoldCost(p),potionCost});
	}
	
	public int getPlusGold(Player p) {
		return plusGold;
	}
	
	public int getPlusActions(Player p) {
		return plusActions;
	}
	
	public int getPlusBuys(Player p) {
		return plusBuys;
	}
	
	public int getPlusCards(Player p) {
		return plusCards;
	}
	
	public int getPlusPotion(Player p) {
		return 0;
	}
	
	public int getVP(Player p) {
		return VP;
	}
	
	public boolean canBeBought(Player p) {
		return true;
	}
	
	public boolean onStartOfGame(Player p) {
		return false;
	}
	
	public boolean onBuy(Player p) {
		return false;
	}
	
	public boolean onGain(Player p) {
		return false;
	}
	
	public boolean onPlay(Player p) {
		p.board.add(p.hand.retire(this));
		p.leftActions+=getPlusActions(p);
		p.leftGold+=getPlusGold(p);
		p.leftBuys+=getPlusBuys(p);
		p.leftPotions+=getPlusPotion(p);
		p.draw(getPlusCards(p));
		System.out.println(p.name+" plays "+this+ " | actions left: "+p.leftActions+" | buys left: "+p.leftBuys+" | gold left: "+p.leftGold);
		return true;
	}
	
	public boolean onTrash(Player p) {
		return false;
	}
	
	public boolean onDiscard(Player p) {
		return false;
	}
	
	public boolean onCleanup(Player p) {
		return false;
	}
	
	public boolean onDuration(Player p) {
		return false;
	}
	
	public boolean onShuffle(Player p) {
		return false;
	}
	
	public boolean onReactToAttack(Player p) {
		System.out.println(p+" shows "+this);
		return false;
	}
}
