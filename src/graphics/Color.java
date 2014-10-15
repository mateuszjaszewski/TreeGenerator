package graphics;

/**
 * Klasa reprezentująca kolor o 4 składowych z zakresu 0 - 1:
 * r - czerwony
 * g - zielony
 * b - niebieski
 * a - przezroczystość
 * 
 * @author Mateusz Jaszewski
 *
 */
public class Color {
	private float r,g,b,a;
	/**
	 * Tworzy nowy kolor (nieprzezroczysty).
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(float r, float g, float b) {
		this.r=r;
		this.g=g;
		this.b=b;
		this.a=1;
	}
	
	/**
	 * Tworzy nowy kolor.
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Color(float r, float g, float b, float a) {
		this(r,g,b);
		this.a=a;
	}
	
	public void setR(float r){
		this.r=r;
	}
	
	public void setG(float g){
		this.g=r;
	}
	
	public void setB(float b){
		this.b=b;
	}
	
	public void setA(float a){
		this.a=a;
	}
	
	public void setRGB(float r, float b, float g){
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	public void setRGBA(float r, float g, float b, float a){
		setRGB(r,g,b);
		this.a=a;
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return g;
	}
	
	public float getB() {
		return b;
	}
	
	public float getA() {
		return a;
	}
	
	/**
	 * Zwraca tablicę zawierającą składowe koloru
	 * @return [r, g, b, a]
	 */
	public float[] getArray() {
		float[] array = new float[4];
		array[0] = r;
		array[1] = g;
		array[2] = b;
		array[3] = a;
		return array;
	}
}
