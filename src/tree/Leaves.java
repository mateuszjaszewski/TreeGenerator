package tree;

import graphics.Mesh;
import graphics.Mesh.TextureCoord;
import graphics.Mesh.Triangle;
import graphics.Mesh.Vertex;

import java.util.List;

import nodes.LeavesNode;
import math.Matrix;
import math.MyRandom;
import math.Vector3;

public abstract class Leaves {
	
	public enum LeavesType {
		SINGLE, DOUBLE, TRIPLE;
		public String toString(){
			if(this == SINGLE)
				return "Pojedyńcze";
			if(this == DOUBLE)
				return "Podwójne";
			if(this == TRIPLE)
				return "Potrójne";
			return "";
		}
	}
	
	public static Mesh generateLeaves(LeavesNode leavesNode,List<TEPoint> points) {
		
		Mesh mesh = new Mesh();
		
		float size, angle;
		Vector3 up, right, forward, normal, position;
		Vector3 x[] = new Vector3[3];
		Vector3 y[] = new Vector3[3];
		Triangle triangle;
		Matrix rotate = new Matrix();
		
		LeavesType type = leavesNode.getLeavesType();
		
		for(int i=0; i<points.size(); i++) {
			TEPoint p = points.get(i);
			size = leavesNode.getSize().floatValue(i);
			
			up = p.getDirection();
			up.normalize();
			right = Vector3.cross(up, new Vector3(0,1,0));
			right.normalize();
			
			angle = MyRandom.randomFloat(0, 360, i*i + 100);
			rotate.rotate((float)Math.toRadians(angle), up);
			
			right = Matrix.mult(right,rotate);
			right.normalize();
			
			forward = Vector3.cross(up, right);
			forward.normalize();
			
			y[0] = up;
			x[0] = forward;
			
			y[1] = up;
			x[1] = right;
			
			y[2] = right;
			x[2] = forward;
			
			int number = 0;
			if(type == LeavesType.SINGLE)
				number = 1;
			if(type == LeavesType.DOUBLE)
				number = 2;
			if(type == LeavesType.TRIPLE)
				number = 3;
			
			for(int j=0; j<number; j++) {
			
				position = Vector3.add(p.getPosition(), Vector3.mult(y[j],size/2));
				position = Vector3.add(position, Vector3.mult(x[j],size/2));
				normal = Vector3.sub(position, p.getPosition());
				mesh.addVertex(new Vertex(position,normal));
			
				position = Vector3.add(p.getPosition(), Vector3.mult(y[j].negative(),size/2));
				position = Vector3.add(position, Vector3.mult(x[j],size/2));
				normal = Vector3.sub(position, p.getPosition());
				mesh.addVertex(new Vertex(position,normal));
			
				position = Vector3.add(p.getPosition(), Vector3.mult(y[j].negative(),size/2));
				position = Vector3.add(position, Vector3.mult(x[j].negative(),size/2));
				normal = Vector3.sub(position, p.getPosition());
				mesh.addVertex(new Vertex(position,normal));
			
				position = Vector3.add(p.getPosition(), Vector3.mult(y[j],size/2));
				position = Vector3.add(position, Vector3.mult(x[j].negative(),size/2));
				normal = Vector3.sub(position, p.getPosition());
				mesh.addVertex(new Vertex(position,normal));
			
				triangle = new Triangle(mesh.countVertices() - 2, mesh.countVertices() - 4, mesh.countVertices() - 1);
				triangle.setTextureCoord(0, new TextureCoord(0,0));
				triangle.setTextureCoord(1, new TextureCoord(1,1));
				triangle.setTextureCoord(2, new TextureCoord(0,1));
			
				mesh.addTriangle(triangle);
			
				triangle = new Triangle(mesh.countVertices() - 2, mesh.countVertices() - 3, mesh.countVertices() - 4);
				triangle.setTextureCoord(0, new TextureCoord(0,0));
				triangle.setTextureCoord(1, new TextureCoord(1,0));
				triangle.setTextureCoord(2, new TextureCoord(1,1));
			
				mesh.addTriangle(triangle);
			}
		}
		
		return mesh;
	}
}
