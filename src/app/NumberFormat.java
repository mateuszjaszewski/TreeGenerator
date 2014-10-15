package app;

/**
 * Klasa zawiera metodę pomocną przy wyświetlaniu liczb
 * 
 * @author Mateusz Jaszewski
 *
 */
public abstract class NumberFormat {
	/**
	 * Jeżeli liczba jest typu float zaokrągla ją do 2 miejsc po przecinku.
	 * @param n liczba typu Number
	 * @return tekstowa reprezentacja podanej liczby
	 */
	public static String format(Number n){
		if(n instanceof Integer)
			return String.format("%d",n.intValue());
		else
			return String.format("%.2f",n.floatValue());
	}
}
