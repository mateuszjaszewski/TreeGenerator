package tree;

import math.MyRandom;
import math.Vector3;

public class TEPoint implements Cloneable{
	public final static int ARRAY_SIZE = 120;
	private Vector3 position;
	private Vector3 direction;
	private float radius;
	private float[] radiusRandom;
	
	public TEPoint() {
		radiusRandom = new float[ARRAY_SIZE]; 
	}
	
	public TEPoint(Vector3 position, Vector3 direction, float radius, float roughness, int id) {
		this.radius = radius;
		this.direction = direction;
		this.direction.normalize();
		this.position = position;
		radiusRandom = new float[ARRAY_SIZE]; 
		
		for(int i=0; i<ARRAY_SIZE; i++) {
			radiusRandom[i]= radius + radius * roughness * (0.5f - MyRandom.randomFloat(0f,1f,i + id*id))*2 * 0.3f;
		}
	}
	
	public Vector3 getPosition() {
		return position;
	}
	
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public Vector3 getDirection() {
		return direction;
	}
	
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public float getRadiusRandom(int index) {
		return radiusRandom[index];
	}
	
	public void setRadiusRandom(int index, float random) {
		radiusRandom[index] = random;
	}
	
	public TEPoint clone() {
		TEPoint point = new TEPoint();
		for(int i=0; i<ARRAY_SIZE; i++) {
			point.radiusRandom[i] = radiusRandom[i];
		}
		point.direction = direction.clone();
		point.position = position.clone();
		point.radius = radius;
		return point;
	}
}
