package base;
public class DiscardPile extends Stack{
	Player owner;
	DiscardPile(Player p) {
		super();
		owner=p;
	}
	
	public String toString() {
		String s = owner.name+"'s discard pile content :\n";
		for(int i = 0; i < data.size();i++) {
			s += data.get(data.size()-i-1).getName() + "  <  " ;
			if (i%8 == 0 && i !=0) {
				s+= "\n";
			}
		}
		return s;
	}
}
