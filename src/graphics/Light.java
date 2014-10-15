package graphics;

import javax.media.opengl.GL2;

/**
 * 	
 * Klasa abstrakcyjna reprezentująca źródło światła.
 * Zawiera informacje o kolorze światła.
 * 
 * @author Mateusz Jaszewski
 */
public abstract class Light {
	protected Color ambient;
	protected Color diffuse;
	protected Color specular;
	
	public Light() {
		ambient = new Color(0.2f, 0.2f, 0.2f);
		diffuse = new Color(0.8f, 0.8f, 0.8f);
		specular = new Color(1f, 1f, 1f);
	}
	
	/**
	 * Ustawia kolor światła otaczającego
	 * @param ambient kolor
	 */
	public void setAmbient(Color ambient) {
		this.ambient = ambient;
	}
	
	/**
	 * Ustawia kolor światła odbitego rozproszonego
	 * @param diffuse kolor
	 */
	public void setDiffuse(Color diffuse) {
		this.diffuse = diffuse;
	}
	
	/**
	 * Ustawia kolor światła odbitego kierunkowego
	 * @param specular kolor
	 */
	public void setSpecular(Color specular) {
		this.specular = specular;
	}
	
	/**
	 * Włącza podane źródło światła
	 * @param gl kontekst OpenGL
	 * @param id numer światła z zakresu 0 - 7 
	 */
	public abstract void on(GL2 gl, int id);
}
