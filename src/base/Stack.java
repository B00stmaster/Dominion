package base;
import java.util.ArrayList;
import java.util.List;

import cards.AbstractCard;

public class Stack {
public List<AbstractCard> data;

Stack() {
	data = new ArrayList<AbstractCard>();
}

Stack(AbstractCard c, int N) {
	this();
	for (int i = 0; i<N; i++) {
		data.add(c);
	}
}

Stack(Stack cop){
	this();
	for (AbstractCard c : cop.data) {
		data.add(c);
	}
}

public Stack add(AbstractCard c){
	data.add(c);
	return this;
}

public Stack simulatedAdd(AbstractCard c){
	return (new Stack(this)).add(c);
}

public AbstractCard pop(){
	return data.remove(data.size()-1);
}

public AbstractCard peek(){
	return data.get(data.size()-1);
}

public AbstractCard peek(int deepness){
	return data.get(data.size()-deepness);
}

public int size() {return data.size();}

public boolean isEmpty() {return data.isEmpty();}

public AbstractCard retire(AbstractCard c) {
	return data.remove(data.indexOf(c));
}

//in case it needs to enlarge, it repeats the card on top
public Stack resize(int newSize){
	while(size()>newSize)
		pop();
	while(size()<newSize)
		add(peek());
	return this;
}

public String toString() {
	String s = "Stack" + super.toString() + " content : " + "\n";
	for (int i = 0; i<data.size(); i++) {
		s += data.get(i).getName() + "\n";
	}
	return s;
}
}
