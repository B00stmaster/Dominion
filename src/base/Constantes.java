package base;
public class Constantes {
public static final int NCOEFF = 6;
double k1 = 0.115; //coeff PdV
double k2 = 2.559;//importance gold
double k3 = 1.46; //importance cartes
double k4 = 0.277; //importance actions
double k5 = 1.02; //importance achats
double k6 = 0.46; // coeff esperance NA piochees
double Seuil1 = 0.2;
double Seuil2 = 0.2;

int N1= 5; //seuil provinces restantes avant transition to late Game
int N2 = 2; //seuil nombre piles vides avant transition to late Game
double epsilon = 0.01;

Constantes(){}

Constantes(double [] t){
	if (t.length < NCOEFF) {
		System.err.println("tableau trop petit");
	}
	else {
		k1 = t[0]; k2 = t[1]; k3 = t[2]; k4 = t[3]; k5 = t[4]; k6 = t[5];
	}
}
double [] listeC() {
	double [] l = {k1,k2,k3,k4,k5,k6};
	return l;
}



boolean plus() {
	return Math.random()>=0.5;
}

Constantes alter() {
	double [] l = listeC();
	double [] nouv = new double[l.length];
	for (int i = 0; i<l.length;i++) {
		if (plus()) {
			nouv[i] = l[i]*(1 + epsilon);
		}
		else {nouv[i] = l[i]*(1- epsilon);}
	}
	return new Constantes(nouv);
}


void rotateToLateGame() {
	
}


public String toString() {
	String s = "";
	s += "coeff PDV : " + k1 + "\n";
	s += "coeff gold : " + k2 + "\n";
	s += "coeff cartes : " + k3 + "\n";
	s += "coeff Actions : " + k4 + "\n";
	s += "coeff achats : " + k5 + "\n";
	s += "coeff comparaison esperances nombre d'action/ cartes actions " + k6 + "\n";
	return s;
}






}
