package pl.mj.treegen.graphics;

import com.jogamp.opengl.glu.GLU;
import pl.mj.treegen.math.Matrix;
import pl.mj.treegen.math.Vector3;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Klasa do obsługi kamery.
 * 
 * @author Mateusz Jaszewski
 *
 */
public class Camera {
	private Vector3 point,position;
	private Vector3 up;
	private float zoom;
	
	private int mouseButton;
	private int lastMouseX,lastMouseY;
	private float angleX,angleY;
	
	/**
	 * Tworzy nową kamerę.
	 */
	public Camera(){
		point = new Vector3(0,0,1);
		up = new Vector3(0,1,0); 
		position = new Vector3(0,0,0);
		angleX=0;
		angleY=0;
		zoom = 100;
	}
	
	/**
	 * Ustawia kamerze Canvas dla którego będą przechwytywane zdarzenia myszy sterujące kamerą.
	 * @param canvas
	 */
	public void setCanvas(Canvas canvas){
		canvas.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent e) {
				if(mouseButton==MouseEvent.BUTTON1) {
					rotate((e.getX()-lastMouseX),
						   (e.getY()-lastMouseY));
				}
				if(mouseButton==MouseEvent.BUTTON3){
					Vector3 dir=point.negative();
					dir.normalize();
					
					Vector3 up,right;
					
					right=Vector3.cross(dir,new Vector3(0,1,0));
					up=Vector3.cross(dir, right);
					
					right.normalize();
					up.normalize();
					
					right.mult(-(e.getX()-lastMouseX)*0.1f);
					up.mult(-(e.getY()-lastMouseY)*0.1f);
					move(Vector3.add(up, right));
				}
				lastMouseX=e.getX();
				lastMouseY=e.getY();
			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mouseButton=e.getButton();
				lastMouseX=e.getX();
				lastMouseY=e.getY();
			}
		});
		canvas.addMouseWheelListener(new MouseAdapter(){
			public void mouseWheelMoved(MouseWheelEvent e) {
				zoom((float)e.getWheelRotation()*0.1f);
			}
		});
	}
	
	/**
	 * Obraca kamerę o podane kąty (w stopniach).
	 * @param x - kąt w płaszczyźnie YZ
	 * @param y - kąt w płaszczyźnie XZ
	 */
	public void rotate(float x, float y) {
		angleX-=x;
		angleY+=y;
		
		if(angleY>=89.9f)
			angleY=89.9f;
		if(angleY<=-89.9f)
			angleY=-89.9f;
		
		Matrix rotX = new Matrix();
		rotX.rotateX((float)Math.toRadians(angleY));
		Matrix rotY = new Matrix();
		rotY.rotateY((float)Math.toRadians(angleX));
		Matrix m = Matrix.mult(rotX,rotY);
		
		point=Matrix.mult(new Vector3(0,0,1),m);
	}
	
	/**
	 * Przesuwa kamerę o podany wektor.
	 * @param v
	 */
	public void move(Vector3 v) {
		position.add(v);
	}
	
	/**
	 * Przybliża bądź oddala kamerę w zależności od parametru.
	 * @param f parametr określający zmianę przybliżenia.
	 */
	public void zoom(float f) {
		zoom+=f*zoom;
		if(zoom<0)
			zoom=0;
	}
	
	/**
	 * Ustawia widok z kamery dla której wywołana jest ta funkcja.
	 * @param glu
	 */
	public void set(GLU glu) {
		Vector3 p = Vector3.mult(point,zoom);
		glu.gluLookAt(position.getX()+p.getX(),position.getY()+p.getY(),position.getZ()+p.getZ(),
					  position.getX(),position.getY(),position.getZ(),
					  up.getX(),up.getY(),up.getZ());
	}
}
