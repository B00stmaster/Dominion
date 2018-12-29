package base;

import java.util.Vector;

import cards.AbstractCard.Type;

public class Training {
	static final int POPULATION_NUMBER = 10;
	static final int GAME_NUMBER = 100;
	static final int EPOCH_NUMBER = 50;
								//gameProgress,goldDensity,TERMINAL_ACTION,REACTION,opponentAttackRatio,TRASHER
	static final double[][] BM_START = {{ -2000 , 0 , 0 , 0 , 0 , 0 , 0 }, //Copper
										{ 300 , -100 , -100 , 0 , 0 , 0 , 0 }, //Silver
										{ 600 , -50 , -200 , 0 , 0 , 0 , 0 }, //Gold
										{ -50 , 200 , 0 , 0 , 0 , 0 , 0 }, //Estate
										{ 50 , 400 , 0 , 0 , 0 , 0 , 0 }, //Duchy
										{ 300 , 600 , 0 , 0 , 0 , 0 , 0 }, //Province
										{ -2000 , 0 , 0 , 0 , 0 , 0 , 0 }, //Village
										{  250 , -100 , 0 , -250 , 0 , 0 , 0 }, //Smithy
										{ -2000 , 0 , 0 , 0 , 0 , 0 , 0 }, //Market
										{ -2000 , 0 , 0 , 0 , 0 , 0 , 0 }, //Laboratory
										{ -2000 , 0 , 0 , 0 , 0 , 0 , 0 }, //Festival
										{ 300 , -75 , 0 , -250 , 0 , 0 , 0 }, //Witch
										{ 275 , -125 , 0 , -250 , 0 , 0 , 0 }, //Militia
										{ 350 , -200 , 0 , -250 , 0 , 0 , 0 }, //Mine
										{ -2000 , -250 , 0 , 0 , 0 , 500 , -500 }, //Chapel
										{ 275 , -50 , 0 , -250 , 0 , 0 , 0 }, //CouncilRoom
										{ 150 , 0 , -100 , 0 , -500 , 1000 , 0 }, //Moat
										{ 325 , -300 , 0 , -250 , 0 , 100 , -350 }, //Remodel
										{ -2000 , 0 , 0 , -250 , 0 , 0 , 400 }, //Workshop
										{ -2000 , 0 , 0 , 0 , 0 , 0 , 0 }}; //Curse
	
	public Training() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void main(String [] args) {
		Vector<LearningPlayer> currentGen = new Vector<LearningPlayer>(POPULATION_NUMBER);
		Vector<int[]> currentGenScores = new Vector<int[]>(POPULATION_NUMBER);
		Vector<Integer> currentGenTotalWins = new Vector<Integer>(POPULATION_NUMBER);
		String[] strategiesToTrain = new String[] {"OptimizedBM","SmithyBM","MilitiaBasicEngine","WitchBasicEngine"};
		for(int i=0;i<POPULATION_NUMBER;i++) {
			LearningPlayer pl = LearningPlayer.recover("epoch_3_best_fitness_89");
			pl.alter();
			currentGen.add(pl);
			int[] res = new int[strategiesToTrain.length];
			for(int j=0;j<res.length;j++)
				res[j]=0;
			currentGenScores.add(res);
			currentGenTotalWins.add(0);
		}

		for(int gen=0;gen<EPOCH_NUMBER;gen++) {
			System.out.println("===================== EPOCH "+gen+" ===============================");
			for(int i=0;i<POPULATION_NUMBER;i++) {
				int totalWins=0;
				System.out.println("Learning player #"+i);
				for(int strat=0;strat<strategiesToTrain.length;strat++) {
					System.out.println("...begins to fight with "+strategiesToTrain[strat]);
					for(int gam=0;gam<GAME_NUMBER;gam++) {
						AIPlayer opp = new AIPlayer(strategiesToTrain[strat]);
						LearningPlayer recruit = currentGen.get(i);
						
						Partie p = new Partie(new Player[] {opp,recruit},false);
						if(p.partie().equals(recruit)) {
							currentGenScores.get(i)[strat]+=1;
							totalWins++;
						}
					}
				}
				currentGenTotalWins.set(i, totalWins);
			}
			
			int maxWins=-1;
			int bestPlayer=-1;
			int minWins=strategiesToTrain.length*GAME_NUMBER+1;
			int worstPlayer=-1;
			int totalWins=0;
			for(int i=0;i<POPULATION_NUMBER;i++) {
				totalWins+=currentGenTotalWins.get(i);
				if(currentGenTotalWins.get(i)>maxWins) {
					maxWins=currentGenTotalWins.get(i);
					bestPlayer=i;
				}
				if(currentGenTotalWins.get(i)<minWins) {
					minWins=currentGenTotalWins.get(i);
					worstPlayer=i;
				}
			}
			
			System.out.println("Epoch champion is Learning player #"+bestPlayer+" :");
			for(int strat=0;strat<strategiesToTrain.length;strat++) {
				System.out.println("Wins against "+strategiesToTrain[strat]+" : "+currentGenScores.get(bestPlayer)[strat]+" ("+((int)(100.0*currentGenScores.get(bestPlayer)[strat]/GAME_NUMBER))+"%)");
			}
			int fitness = ((int)(100.0*currentGenTotalWins.get(bestPlayer)/(GAME_NUMBER*strategiesToTrain.length)));
			System.out.println("Total wins : "+currentGenTotalWins.get(bestPlayer)+", fitness : "+fitness);
			
			System.out.println("Epoch worst is Learning player #"+worstPlayer+" :");
			for(int strat=0;strat<strategiesToTrain.length;strat++) {
				System.out.println("Wins against "+strategiesToTrain[strat]+" : "+currentGenScores.get(worstPlayer)[strat]+" ("+((int)(100.0*currentGenScores.get(worstPlayer)[strat]/GAME_NUMBER))+"%)");
			}
			int fitness2 = ((int)(100.0*currentGenTotalWins.get(worstPlayer)/(GAME_NUMBER*strategiesToTrain.length)));
			System.out.println("Total wins : "+currentGenTotalWins.get(worstPlayer)+", fitness : "+fitness2);
			
			currentGen.get(bestPlayer).save("epoch_"+Integer.toString(gen)+"_best_fitness_"+Integer.toString(fitness));
			
			Vector<LearningPlayer> nextGen = new Vector<LearningPlayer>(POPULATION_NUMBER);
			for(int i=0;i<POPULATION_NUMBER;i++) {
				int rep = ((int) (POPULATION_NUMBER*((double)(currentGenTotalWins.get(i))/totalWins)));
				for(int k=0;k<rep;k++) {
					LearningPlayer next = currentGen.get(i).copy();
					next.alter();
					nextGen.add(next);
				}
			}
			int missing = POPULATION_NUMBER-nextGen.size();
			System.out.println(missing+" players are missing for next generation. Using best player");
			for(int i=0;i<missing;i++) {
				LearningPlayer next = currentGen.get(bestPlayer).copy();
				next.alter();
				nextGen.add(next);
			}
			
			for(int i=0;i<POPULATION_NUMBER;i++) {
				for(int j=0;j<currentGenScores.get(i).length;j++)
					currentGenScores.get(i)[j]=0;
				currentGenTotalWins.set(i, 0);
			}
			
			
		}
	}

}
