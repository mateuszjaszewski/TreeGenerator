package graphics;

import javax.media.opengl.GL2;

/**
 * Interfejs który implementują klasy które mogą być wyświetlane przez obiekt klasy Renderer.
 * @author Mateusz Jaszewski
 */
public interface Renderable {
	/**
	 * Metoda wywoływana podczas rysowania.
	 * @param gl kontekst OpenGL
	 */
	public void Render(GL2 gl);
}
