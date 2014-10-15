package math;

/**
 * Klasa do obsługi wektorów w przestrzeni 3D 
 * @author Mateusz Jaszewski
 */
public class Vector3 {
	private float x, y, z;

	/**
	 * Tworzy wektor o składowych (0,0,0)
	 */
	public Vector3() {
		x = 0f;
		y = 0f;
		z = 0f;
	}
	
	/**
	 * Tworzy wektor o składowych (x,y,z)
	 * @param x składowa x
	 * @param y składowa y
	 * @param z składowa z
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Dodaje wektory 
	 * @param a wektor a 
	 * @param b wektor b
	 * @return suma wektorów a+b
	 */
	public static Vector3 add(Vector3 a, Vector3 b) {
		return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	/**
	 * Odejmuje wektory 
	 * @param a wektor a
	 * @param b wektor b
	 * @return różnica wektorów a-b
	 */
	public static Vector3 sub(Vector3 a, Vector3 b) {
		return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	/**
	 * Liczy iloczyn wektorowy podanych wektorów
	 * @param a wektor a
	 * @param b wektor b
	 * @return iloczyn wektorowy a i b 
	 */
	public static Vector3 cross(Vector3 a, Vector3 b){
		return new Vector3(a.y*b.z-a.z*b.y,
						   a.z*b.x-a.x*b.z,
						   a.x*b.y-a.y*b.x);	
	}
	
	/**
	 * Mnoży wektor przez skalar
	 * @param v wektor
	 * @param f skalar
	 * @return wektor pomnożony przez skalar
	 */
	public static Vector3 mult(Vector3 v, float f){
		return new Vector3(v.x*f, v.y*f, v.z*f);
	}
	
	/**
	 * Oblicza wektor odwrotny 
	 * @return zwraca wektor odwrotny - o składowych (-x,-y,-z)
	 */
	public Vector3 negative() {
		Vector3 n = new Vector3(-x,-y,-z);
		return n;
	}
	
	/**
	 * Zwraca wektor prostopadły do podanego.
	 * @return wektor prostopadły
	 */
	public Vector3 anyNormal(){
		Vector3 v = new Vector3(y,z,x);
		return Vector3.cross(this, v);
	}

	/**
	 * Liczy długość wektora
	 * @return długość wektora
	 */
	public float length(){
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	
	/**
	 * Normalizuje wektor
	 */
	public void normalize(){
		float l=length();
		x=x/l;
		y=y/l;
		z=z/l;
	}
	
	/**
	 * Mnoży wektor przez skalar
	 * @param f skalar
	 */
	public void mult(float f){
		x*=f;
		y*=f;
		z*=f;
	}
	
	/**
	 * Dodaje do wektor
	 * @param v wektor
	 */
	public void add(Vector3 v) {
		x+=v.x;
		y+=v.y;
		z+=v.z;
	}
	
	/**
	 * Odejmuje wektor
	 * @param v wektor
	 */
	public void sub(Vector3 v) {
		x-=v.x;
		y-=v.y;
		z-=v.z;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public String toString(){
		return x+" "+y+" "+z;
	}
	
	public Vector3 clone() {
		Vector3 v = new Vector3();
		v.x = x;
		v.y = y;
		v.z = z;
		return v;
	}
}
