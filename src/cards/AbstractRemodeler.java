package cards;

import java.util.Vector;

import base.Player;

public abstract class AbstractRemodeler extends AbstractAction {

	boolean needExactCost;
	boolean isMandatory;
	
	public AbstractRemodeler() {
		super();
		types.add(Type.REMODELER);
	}
	
	protected abstract int newCost(int oldCost);
	
	
	boolean canUpgrade(Player p, AbstractCard oldCard, AbstractCard newCard) {
		if(needExactCost)
			return newCard.getGoldCost(p)==newCost(oldCard.getGoldCost(p));
		return newCard.getGoldCost(p)<=newCost(oldCard.getGoldCost(p));
	}
	
	protected Vector<AbstractCard> selectAvailable(Player p, AbstractCard trashedCard){
		Vector<AbstractCard> shopAvailable = p.partie.theShop.buyables();
		Vector<AbstractCard> available = new Vector<AbstractCard>();
		for(AbstractCard a : shopAvailable) {
			if(canUpgrade(p, trashedCard, a))
				available.add(a);
			}
		return available;
	}
	
	protected Vector<AbstractCard> availableToTrash(Player p){
		Vector<AbstractCard> res = new Vector<AbstractCard>();
		for(AbstractCard c : p.hand) {
			if(!selectAvailable(p, c).isEmpty())
				res.add(c);
		}
		if(isMandatory) {
			if(res.isEmpty()) {
				System.out.println("ERROR: nothing in your hand is suitable for trashing. Canceling action");
				return null;
			}
		}
		else
			res.addElement(null);
		return res;
	}
	
	public boolean onPlay(Player p) {return super.onPlay(p);}

}
