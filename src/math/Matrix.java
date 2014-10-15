package math;
/**
 * Klasa do obsługi macierzy o wymiarach 4x4.
 * Zawiera metody do genrowania macierzy odpowiedzialnych za przekształcenia (obroty, przesunięcie, skalowanie).
 * @author Mateusz Jaszewski
 */
public class Matrix {
	private float[][] tab;
	
	/**
	 * Konstruktor tworzący macierz jednostkową
	 */
	public Matrix(){
		tab=new float[4][4];
		identity();
	}
	
	/**
	 * Zwraca wartość z macierzy
	 * @param row wiersz
	 * @param colum kolumna
	 * @return wartość znajdującą się w podanym wierszu i kolumnie w macierzy
	 */
	public float get(int row, int colum){
		return tab[row][colum];
	}
	
	/**
	 * Wpisuje do macierzy podaną wartość
	 * @param row wiersz
	 * @param column kolumna
	 * @param value wartość
	 */
	public void set(int row, int column, float value){
		tab[row][column]=value;
	}
	
	/**
	 * Mnoży macierze
	 * @param a macierz A
	 * @param b macierz B
	 * @return Iloczyn macierzy A*B
	 */
	static public Matrix mult(Matrix a, Matrix b){
		Matrix m = new Matrix();
		float s;
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++){
				s=0f;
				for(int k=0; k<4; k++){
					s+=a.get(i,k)*b.get(k,j);
				}
				m.tab[i][j]=s;
			}
		}
		return m;
	}
	
	/**
	 * Mnoży wektor przez macierz
	 * @param matrix macierz
	 * @param vector wektor
	 * @return Iloczyn podanego wektora i macierzy (wektor traktowany jest jako macierz 1x4)
	 */
	static public Vector3 mult(Vector3 vector, Matrix matrix){
		float x,y,z;
		x=matrix.get(0, 0)* vector.getX() + matrix.get(1, 0) * vector.getY() + matrix.get(2, 0) * vector.getZ();
		y=matrix.get(0, 1)* vector.getX() + matrix.get(1, 1) * vector.getY() + matrix.get(2, 1) * vector.getZ();
		z=matrix.get(0, 2)* vector.getX() + matrix.get(1, 2) * vector.getY() + matrix.get(2, 2) * vector.getZ();
		return new Vector3(x,y,z);
	}
	
	/**
	 * Tworzy macierz jednostkową 
	 */
	public void identity(){
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				tab[i][j]=0f;
		for(int i=0; i<4; i++)
			tab[i][i]=1f;
	}
	
	/**
	 * Tworzy macierz obrotu wokół podanej osi
	 * @param angle oś obrotu
	 * @param axis kąt
	 */
	public void rotate(float angle, Vector3 axis){
		identity();
	    tab[0][0]=(float)Math.cos(angle)+axis.getX()*axis.getX()*(1f-(float)Math.cos(angle));
	    tab[0][1]=axis.getX()*axis.getY()*(1f-(float)Math.cos(angle))-axis.getZ()*(float)Math.sin(angle);
	    tab[0][2]=axis.getX()*axis.getZ()*(1f-(float)Math.cos(angle))+axis.getY()*(float)Math.sin(angle);

	    tab[1][0]=axis.getY()*axis.getX()*(1f-(float)Math.cos(angle))+axis.getZ()*(float)Math.sin(angle);
	    tab[1][1]=(float)Math.cos(angle)+axis.getY()*axis.getY()*(1-(float)Math.cos(angle));
	    tab[1][2]=axis.getY()*axis.getZ()*(1f-(float)Math.cos(angle))-axis.getX()*(float)Math.sin(angle);

	    tab[2][0]=axis.getZ()*axis.getX()*(1f-(float)Math.cos(angle))-axis.getY()*(float)Math.sin(angle);
	    tab[2][1]=axis.getZ()*axis.getY()*(1f-(float)Math.cos(angle))+axis.getX()*(float)Math.sin(angle);
	    tab[2][2]=(float)Math.cos(angle)+axis.getZ()*axis.getZ()*(1f-(float)Math.cos(angle));
	}
	
	/**
	 * Tworzy macierz obrotu wokół osi X
	 * @param angle kąt
	 */
	public void rotateX(float angle) {
		identity();
		tab[1][1]=(float)Math.cos(angle);
		tab[2][2]=(float)Math.cos(angle);
		tab[1][2]=(float)Math.sin(angle);
    	tab[2][1]=-(float)Math.sin(angle);
	}
	
	/**
	 * Tworzy macierz obrotu wokół osi Y
	 * @param angle kąt
	 */
	public void rotateY(float angle) {
		identity();
		 tab[0][0]=(float)Math.cos(angle);
		 tab[2][2]=(float)Math.cos(angle);
		 tab[0][2]=-(float)Math.sin(angle);
		 tab[2][0]=(float)Math.sin(angle);
	}
	
	/**
	 * Tworzy macierz obrotu wokół osi Z
	 * @param angle kąt
	 */
	public void rotateZ(float angle) {
		identity();
	    tab[0][0]=(float)Math.cos(angle);
	    tab[1][1]=(float)Math.cos(angle);
	    tab[0][1]=(float)Math.sin(angle);
	    tab[1][0]=-(float)Math.sin(angle);
	}

	
	/**
	 * Tworzy macierz translacji 
	 * @param vector wektor przesunięcia
	 */
	public void translate(Vector3 vector){
		identity();
	    tab[0][3]=vector.getX();
	    tab[1][3]=vector.getY();
	    tab[2][3]=vector.getZ();
	}
	
	/**
	 * Tworzy macierz skalowania
	 * @param vector wektor
	 */
	public void scale(Vector3 vector){
		identity();
	    tab[0][0]=vector.getX();
	    tab[1][1]=vector.getY();
	    tab[2][2]=vector.getZ();
	}
	
	public String toString(){
		String r="";
		for(int i=0; i<4; i++){
			r=r+"[ ";
			for(int j=0; j<4; j++)
				r=r+tab[i][j]+" ";
			r=r+"] ";
		}
		return r;
	}
	
}
