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

public String toString() {
	String s = "Stack" + super.toString() + "content : " + "\n";
	for (int i = 0; i<data.size(); i++) {
		s += data.get(i).name + "\n";
	}
	return s;
}
}
