
public class Stack {
static final int MAX_SIZE=1000;
public Card[] cartes;
protected int NCartes;


Stack() {
	NCartes=0;
	cartes = new Card[MAX_SIZE];
}

Stack(String s, int N) {
	cartes = new Card[MAX_SIZE];
	for (int i = 0; i<N; i++) {
		cartes[i]= Card.copie(s);
	}
	NCartes = N;
}


Stack add(Card c){
	if(NCartes>=MAX_SIZE)
		System.err.println("Tentative de add une file pleine!");
	cartes[NCartes] = c;
	NCartes++;
	return this;
}

Card pop(){
	if(isEmpty())
		System.err.println("ERREUR - Tentative de pop une file vide!"); 
	NCartes--;
	return cartes[NCartes];
}

Card peek(){
	if(isEmpty())
		System.err.println("ERREUR - Tentative de peek une file vide!");
	return cartes[NCartes-1];
}

int size() {return NCartes;}


boolean isEmpty() {return NCartes<=0;}

public Card retire(Card c) {
	for (int i = 0; i<NCartes;i++) {
		if (c == cartes[i]) {
			Card temp=cartes[i];
			NCartes --;
			cartes[i] = cartes[NCartes];
			return temp;
		}
	}
	System.out.println("Erreur carte absente");
	return null;
}

void permutation(int i, int j) {
	Card temp = cartes[i];
	cartes[i] = cartes[j];
	cartes[j] = temp;
	
}

void shuffle() {
	System.out.println("shuffle");
	int n = 100;
	for (int i = 0; i<n; i++) {
		int a = (int) (Math.random()*NCartes);
		int b = (int) (Math.random()*NCartes);
		
		permutation(a,b);
	}
}

}
