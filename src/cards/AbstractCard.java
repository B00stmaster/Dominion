package cards;

import java.util.EnumSet;
import java.util.Set;

import base.*;
import base.Card.Type;

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
	
	public boolean isA(AbstractCard.Type t) {return (types.contains(t));} 
	public String toString() {return name;}

	public String getName() {
		if(!name.equals(""))
			return name;
		return this.getClass().getName();
	}
	public int getGoldCost(Player p) {
		return Math.max(goldCost-p.board.cardCount("Pont"),0);
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
		//p.board.add(p.hand.retire(this));
		if(this.isA(Type.ACTION)) p.leftActions--;
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
		return false;
	}
	
}
