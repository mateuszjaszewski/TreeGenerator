package pl.mj.treegen.math;

public class SeedGenerator {
	private static int counter = 0;
	
	public static void reset() {
		counter = 0;
	}
	public static int getSeed() {
		int seed = counter;
		counter++;
		return seed;
	}
}
