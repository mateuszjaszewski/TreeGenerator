package math;

import java.util.Random;

/**
 * Klasa zawiera metody służące do generowania liczb pseudolosowych z podanego zakresu i dla ustawionego ziarna.
 *  @author Mateusz Jaszewski
 */
public class MyRandom {
	private static int globalSeed = 0;
	
	/**
	 * Ustawia globalne ziarno - wpływa na sposób losowania liczb
	 * @param seed
	 */
	static public void setGlobalSeed(int seed) {
		globalSeed = seed;
	}
	
	/**
	 * @return globalne ziarno
	 */
	static public int getGlobalSeed() {
		return globalSeed;
	}
	
	/**
	 * Generuje liczbę całkowitą (typu int) z podanego zakresu 
	 * @param min minimalna wartość
	 * @param max maksymalna wartość
	 * @param seed ziarno (razem z globalnym ziarnem wpływa na sposób losowania liczb)
	 * @return pseudolosowa liczba całkowita
	 */
	public static int randomInt(int min, int max, int seed) {
		Random r1 = new Random(globalSeed);
		Random r2 = new Random(seed);
		Random random = new Random(r1.nextInt() + r2.nextInt());
		max++;
		if(min == max) 
			return min;
		int rand = random.nextInt();
		if(rand < 0)
			rand = - rand;
		return (rand % (max - min)) + min;
		
	}
	
	/**
	 * Generuje liczbę zmiennoprzecinkową (typu float) z podanego zakresu
	 * @param min minimalna wartość
	 * @param max maksymalna wartość
	 * @param seed ziarno (razem z globalnym ziarnem wpływa na sposób losowania liczb)
	 * @return pseudolosowa liczba zmiennoprzecinkowa
	 */
	public static float randomFloat(float min, float max, int seed) {
		float rand = (float)randomInt(0,Integer.MAX_VALUE,seed)/Integer.MAX_VALUE;
		return (rand*(max - min) + min);
	}
	
	
}
