
//Constantes plutot pas mal : 
/*double k1 = 0.01; //coeff PdV
double k2 = 0.32;//importance gold
double k3 = 0.32; //importance cartes
double k4 = 0.2; //importance actions
double k5 = 0.15; //importance achats
double k6 = 0.9; // coeff esperance NA piochees
double k7 = 0.9; //seuil de proba
*/
public class Constantes {
public static final int NCOEFF = 7;
double k1 = 0.01; //coeff PdV
double k2 = 0.32;//importance gold
double k3 = 0.32; //importance cartes
double k4 = 0.2; //importance actions
double k5 = 0.15; //importance achats
double k6 = 0.9; // coeff esperance NA piochees
double k7 = 0.9; //seuil de proba
int N1= 5; //seuil provinces restantes avant transition to late Game
int N2 = 2; //seuil nombre piles vides avant transition to late Game
double q1 = 0.01; //coeff PdV
double q2 = 0.32;//importance gold
double q3 = 0.32; //importance cartes
double q4 = 0.2; //importance actions
double q5 = 0.15; //importance achats
double q6 = 0.9; // coeff esperance NA piochees
double q7 = 0.9; //seuil de proba
double epsilon = 0.1;

Constantes(){}

Constantes(double [] t){
	if (t.length < NCOEFF) {
		System.err.println("tableau trop petit");
	}
	else {
		k1 = t[0]; k2 = t[1]; k3 = t[2]; k4 = t[3]; k5 = t[4]; k6 = t[5]; k7 = t[6];
	}
}
double [] listeC() {
	double [] l = {k1,k2,k3,k4,k5,k6,k7};
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
	s += "seuil proba de piocher 2+ ANR : " + k7 + "\n";
	return s;
}






}
