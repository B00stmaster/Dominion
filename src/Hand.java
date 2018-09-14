
public class Hand extends Stack {
Player owner;

Hand(Player p){
	super();
	owner=p;
}

public String toString() {
	String s = "Contenu de la main :" + "\n";
	for (int i =0; i<NCartes;i++) {
		s += cartes[i].name + "  |  " ;
		if (i%7 == 0 && i !=0) {
			s+= "\n";
		}
	}
	return s;
}

}
