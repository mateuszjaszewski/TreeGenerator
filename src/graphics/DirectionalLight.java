package graphics;

import javax.media.opengl.GL2;
import math.Vector3;

/**
 * Klas reprezentująca światło kierunkowe, dziedziczy po klasie Light.
 *
 * @author Mateusz Jaszewski
 *
 */
public class DirectionalLight extends Light{
	private Vector3 direction; 
	
	/**
	 * Tworzy nowe kierunkowe źródło światła.
	 * @param direction kierunek padania światła.
	 */
	public DirectionalLight(Vector3 direction) {
		super();
		setDirection(direction);
	}
	
	public void setDirection(Vector3 direction) {
		this.direction = direction;
		this.direction.normalize();
	}

	/**
	 * Włącza wybrane światło. 
	 * @param gl kontekst OpenGL
	 * @param id numer światła OpenGL
	 */
	public void on(GL2 gl, int id) {
		float[] dir = new float[4];
		dir[0] = direction.getX();
		dir[1] = direction.getY();
		dir[2] = direction.getZ();
		dir[3] = 0;
		gl.glEnable(GL2.GL_LIGHT0 + id);
		gl.glLightfv(GL2.GL_LIGHT0 + id, GL2.GL_POSITION,dir,0);
		gl.glLightfv(GL2.GL_LIGHT0 + id, GL2.GL_AMBIENT, ambient.getArray(), 0);
		gl.glLightfv(GL2.GL_LIGHT0 + id, GL2.GL_DIFFUSE, diffuse.getArray(), 0);
		gl.glLightfv(GL2.GL_LIGHT0 + id, GL2.GL_SPECULAR, specular.getArray(), 0);
	}

}
