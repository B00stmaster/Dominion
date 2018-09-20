import java.util.ArrayList;
import java.util.List;

public class Stack {
public List<Card> data;

Stack() {
	data = new ArrayList<Card>();
}

Stack(String s, int N) {
	this();
	for (int i = 0; i<N; i++) {
		data.add(Card.copie(s));
	}
}

Stack(Stack cop){
	this();
	for (int i = 0; i<cop.data.size()-1; i++) {
		data.add(Card.copie(cop.data.get(i).name));
	}
}

Stack add(Card c){
	data.add(c);
	return this;
}

Stack simulatedAdd(Card c){
	return (new Stack(this)).add(c);
}

Card pop(){
	return data.remove(data.size()-1);
}

Card peek(){
	return data.get(data.size()-1);
}

int size() {return data.size();}


boolean isEmpty() {return data.isEmpty();}

public Card retire(Card c) {
	return data.remove(data.indexOf(c));
}

//void permutation(int i, int j) {
//	Card temp = cartes[i];
//	cartes[i] = cartes[j];
//	cartes[j] = temp;
//}

//void shuffle() {
//	//System.out.println("shuffle");
//	int n = 500;
//	for (int i = 0; i<n; i++) {
//		int a = (int) (Math.random()*NCartes);
//		int b = (int) (Math.random()*NCartes);
//		
//		permutation(a,b);
//	}
//}

public String toString() {
	String s = "Stack" + super.toString() + "content : " + "\n";
	for (int i = 0; i<data.size(); i++) {
		s += data.get(i).name + "\n";
	}
	return s;
}
}
