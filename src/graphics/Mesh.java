package graphics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import math.Vector3;

/**
 * Klasa reprezentująca bryłę w przestrzeni, zawiera informacje o wierzchołkach i trójkątach.
 * @author Mateusz Jaszewski
 */
public class Mesh implements Renderable {

	/**
	 * Statyczna metoda służąca do generowania pojedyńczej bryły z listy brył.
	 * Służy do scalania wielu brył w jedną.
	 * @param list lista brył
	 * @return scalona bryła
	 */
	public static Mesh regroup(List<Mesh> list) {
		int index = 0;
		Mesh mesh = new Mesh();
		
		for(Mesh m : list) {
			for(Vertex v : m.vertices) {
				mesh.addVertex(v);
			}
			
			for(Triangle t : m.triangles) {
				Triangle nt = new Triangle(t.getVertexIndex(0) + index,
						t.getVertexIndex(1) + index,
						t.getVertexIndex(2) + index);
				nt.textureCoords = t.textureCoords;
				mesh.addTriangle(nt);
			}
			
			index += m.vertices.size();
		}
		return mesh;
	}
	
	public static void export(Mesh tree, Mesh leaves, FileWriter fileWriter) throws IOException {
		
		int vertexIndex = tree.vertices.size();
		int texCoordIndex = tree.triangles.size() * 3;
		
		for(Vertex v : tree.vertices)
			fileWriter.write("v " + v.getPosition() + "\r\n");
		for(Vertex v : leaves.vertices)
			fileWriter.write("v " + v.getPosition() + "\r\n");
		
		for(Vertex v : tree.vertices)
			fileWriter.write("vn " + v.getNormal() + "\r\n");
		for(Vertex v : leaves.vertices) {
			fileWriter.write("vn " + v.getNormal() + "\r\n");
		}
		
		for(Triangle t : tree.triangles) {
			for(int i=0; i<3; i++)
				fileWriter.write("vt " + t.getTextureCoord(i) + "\r\n");
		}
		for(Triangle t : leaves.triangles) {
			for(int i=0; i<3; i++)
				fileWriter.write("vt " + t.getTextureCoord(i) + "\r\n");
		}
		
		fileWriter.write("g tree\r\n");
		fileWriter.write("usemtl tree\r\n");
		for(int i=0; i<tree.triangles.size(); i++) {
			Triangle t = tree.triangles.get(i);
			fileWriter.write("f ");
			for(int j=0; j<3; j++) {
				fileWriter.write(t.indices[j] + 1 + "/");
				fileWriter.write(i*3 + j + 1 + "/");
				fileWriter.write(t.indices[j] + 1 + " ");
			}
			fileWriter.write("\r\n");
		}
		fileWriter.write("g leaves\r\n");
		fileWriter.write("usemtl leaves\r\n");
		for(int i=0; i<leaves.triangles.size(); i++) {
			Triangle t = leaves.triangles.get(i);
			fileWriter.write("f ");
			for(int j=0; j<3; j++) {
				fileWriter.write(vertexIndex + t.indices[j] + 1 + "/");
				fileWriter.write(texCoordIndex +i*3 + j + 1 + "/");
				fileWriter.write(vertexIndex + t.indices[j] + 1 + " ");
			}
			fileWriter.write("\r\n");
		}
	}
	
	private List<Vertex> vertices;
	private List<Triangle> triangles;

	/**
	 * Konstruktor tworzący pustą bryłę
	 */
	public Mesh() {
		vertices = new ArrayList<Vertex>();
		triangles = new ArrayList<Triangle>();
	}

	/**
	 * Metoda wywoływana podczas wyświetlania bryły
	 * @param gl kontekst OpenGL
	 */
	public void Render(GL2 gl) {
		Vertex v;
		
		gl.glColor3f(1f, 1f, 1f);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glBegin(GL2.GL_TRIANGLES);
		for (Triangle t : triangles) {
			for (int i = 0; i < 3; i++) {
				v = vertices.get(t.indices[i]);
				gl.glTexCoord2f(t.getTextureCoord(i).getU(),t.getTextureCoord(i).getV());
				gl.glNormal3f(v.getNormal().getX(), v.getNormal().getY(), v.getNormal().getZ());
				gl.glVertex3f(v.getPosition().getX(), v.getPosition().getY(), v.getPosition().getZ());
			}
		}
		gl.glEnd();
	}

	/**
	 * Dodaje wierzchołek do bryły
	 * @param vertex wierzchołek
	 */
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}

	/**
	 * Zwraca wierzchołek 
	 * @param index indesk wierzchołka 
	 * @return wierzchołek o podanym indeksie
	 */
	public Vertex getVertex(int index) {
		return vertices.get(index);
	}

	/**
	 * Zwraca liczbę wierzchołków należących do bryły
	 * @return liczba wierzchołków
	 */
	public int countVertices() {
		return vertices.size();
	}

	/**
	 * Dodaje trójkąt do bryły
	 * @param triangle trójkąt
	 */
	public void addTriangle(Triangle triangle) {
		triangles.add(triangle);
	}

	/**
	 * Zwraca trójkąt
	 * @param index indeks trójkąta
	 * @return trójkąt o podanym indeksie
	 */
	public Triangle getTriangle(int index) {
		return triangles.get(index);
	}

	/**
	 * Zwraca liczbę trójkątów należących do bryły
	 * @return liczba trójkątów
	 */
	public int countTriangles() {
		return triangles.size();
	}
	
	/**
	 * Klasa do obsługi trójkąta który jest częścią bryły
	 *
	 */
	public static class Triangle {
		private int[] indices;
		private TextureCoord[] textureCoords;
		
		/**
		 * Konstruktor tworzący trójkąt składający się z wierzchołków 
		 * @param v1 indeks 1 wierzchołka
		 * @param v2 indeks 2 wierzchołka
		 * @param v3 indeks 3 wierzchołka
		 */
		public Triangle(int v1, int v2, int v3) {
			indices = new int[3];
			indices[0] = v1;
			indices[1] = v2;
			indices[2] = v3;
			textureCoords = new TextureCoord[3];
		}
		
		public void setTextureCoord(int index, TextureCoord tc) {
			textureCoords[index] = tc;
		}
		
		public TextureCoord getTextureCoord(int index) {
			return textureCoords[index];
		}

		public String toString() {
			return indices[0] + " " + indices[1] + " " + indices[2];
		}

		/**
		 * @param index indeks wierzchołka w trójkącie 
		 * @return wierzchołek o podanym indeksie 
		 */
		public int getVertexIndex(int index) {
			if (index >= 0 && index < 3) {
				return indices[index];
			} else
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Klasa do obsługi wierzchołków należących do bryły.
	 * Zawiera informacje o pozycji, wektorze normalnym i koordynacie textury
	 * 
	 * @author Mateusz Jaszewski
	 */
	public static class Vertex{
		private Vector3 position, normal;

		/**
		 * Konstruktor tworzący wierzchołek o podanych parametrach
		 * @param position  pozycja
		 * @param normal  wektor normalny 
		 * @param texCoordU  składowa U koordynatu textury
		 * @param texCoordV składowa V koordynatu textury
		 */
		public Vertex(Vector3 position, Vector3 normal) {
			this.position = position;
			this.normal = normal;
		}
		
		public Vector3 getPosition() {
			return position;
		}

		public void setPosition(Vector3 position) {
			this.position = position;
		}

		public Vector3 getNormal() {
			return normal;
		}

		public void setNormal(Vector3 normal) {
			this.normal = normal;
		}

		public String toString() {
			return position.toString() + " " + normal.toString();
		}
	    
	}
	
	/**
	 * Klasa do obsługi koordynatów tekstur.
	 * 
	 * @author Mateusz Jaszewski
	 *
	 */
	public static class TextureCoord {
		private float u,v;
		public TextureCoord(float u, float v) {
			this.u=u;
			this.v=v;
		}
		
		public float getU() {
			return u;
		}
		
		public float getV() {
			return v;
		}
		
		public void setU(float u) {
			this.u=u;
		}
		
		public void setV(float v) {
			this.v=v;
		}
		
		public String toString() {
			return u + " " + v;
		}
	}
}
