package graphics;

import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Klasa do obsługi materiału używanego do wyświetlania obiektów.
 * 
 * @author Mateusz Jaszewski
 */

public class Material {
	private Texture texture; 
	private String texturePath;
	private Color ambient, diffuse, specular;
	private float shininess;
	
	/**
	 * Tworzy nowy materiał o domyślnych parametrach
	 */
	public Material() {
		texturePath="";
		ambient = new Color(1f,1f,1f);
		diffuse = new Color(1f,1f,1f);
		specular = new Color(0.2f,0.2f,0.2f);
		shininess = 100;
	}
	
	public String getTexturePath() {
		return texturePath;
	}
	
	public void setAmbient(Color ambient) {
		this.ambient = ambient;
	}
	
	public void setDiffuse(Color diffuse) {
		this.diffuse = diffuse;
	}
	
	public void setSpecular(Color specular) {
		this.specular = specular;
	}
	
	public void setShininess(float shininess) {
		this.shininess = shininess;
	}
	
	/**
	 * Wczytuje teksturę
	 * @param path ścieżka do pliku zawierającego teksturę
	 */
	public void loadTexture(String path) {
		texture = null;
		texturePath = path;	
	}
	
	/**
	 * Włącza dany materiał
	 * @param gl kontekst OpenGL
	 */
	public void use(GL2 gl) {
		if(texture == null && !texturePath.equals("")) {
			try {
				File file = new File(texturePath);
				texture = TextureIO.newTexture(file,true);
				gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
				gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			} catch (IOException e)
			{
				e.printStackTrace();
			}	
		}
		gl.glEnable( GL2.GL_COLOR_MATERIAL );
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient.getArray(),0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse.getArray(),0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular.getArray(),0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
		if(texture!=null) {
			gl.glEnable(GL2.GL_TEXTURE_2D);
			texture.enable(gl);
			texture.bind(gl);
		} else 
			gl.glDisable(GL2.GL_TEXTURE_2D);
			
	}
	
}
