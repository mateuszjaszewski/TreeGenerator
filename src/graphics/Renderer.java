package graphics;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Klasa zarządza wyświetlaniem obiektów implementujących interfejs renderable
 * 
 * @author Mateusz Jaszewski
 * 
 */
public class Renderer implements GLEventListener {
	private GLU glu;
	private GLProfile profile;
	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private List<Renderable> renderableList;
	private FPSAnimator animator; 
	private Camera camera;
	
	public Renderer(){
		glu = new GLU();
		profile = GLProfile.get(GLProfile.GL2);
		capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		renderableList = new ArrayList<Renderable>();
		animator = new FPSAnimator(60);
		animator.add(canvas);
		animator.start();
		camera = new Camera();
		camera.setCanvas(canvas);
	}
	
	/* (non-Javadoc)
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	public void init(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		gl.glClearColor(0.6f,0.7f,1f,0);
		gl.glClearDepth(1.0);
		
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		
		gl.glEnable(GL2.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL2.GL_GREATER, 0.5f);
		gl.glDisable(GL2.GL_BLEND);
	}

	public void display(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);		
		camera.set(glu);

		for(Renderable r:renderableList){
			r.Render(gl);
		}
		
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glColor3f(0.4f,0.4f,0.4f);
		gl.glBegin(GL2.GL_LINES);
			for(int i=-30; i<=30; i+=3)
			{
				gl.glVertex3f(i,0,30);
				gl.glVertex3f(i,0,-30);
				gl.glVertex3f(30,0,i);
				gl.glVertex3f(-30,0,i);
			}
		gl.glEnd();
        gl.glFlush();
		
	}

	public void reshape(GLAutoDrawable glDrawable, int x, int y, int w, int h) {
		final GL2 gl = glDrawable.getGL().getGL2();
		float aspect; 
		if(h==0)
			h=1;
		aspect = (float)w/h;
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60f, aspect, 0.1f, 100000f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	

	public void dispose(GLAutoDrawable glDrawable) {
		
	}
	
	/**
	 * Zwraca obiekt GLCanvas na którym odbywa się rysowanie
	 * @return 
	 */
	public GLCanvas getCanvas(){
		return canvas;
	}
	
	/**
	 * Dodaje podany obiekt do listy wyświetlanych obiektów. 
	 * @param renderable obiekt implementujący interfejs renderable
	 */
	public void add(Renderable renderable){
		this.renderableList.add(renderable);
	}
	
	/**
	 * Usuwa podany obiekt z listy wyświetlanych obiektów.
	 * @param renderable obiekt implementujący interfejs renderable
	 */
	public void remove(Renderable renderable){
		if(renderableList.indexOf(renderable)!=-1)
			renderableList.remove(renderableList.indexOf(renderable));
	}
	
	/**
	 * Czyści listę wyświetlanych obiektów.
	 */
	public void removeAll(){
		renderableList.clear();
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
}
