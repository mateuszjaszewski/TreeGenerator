package pl.mj.treegen.tree;

import pl.mj.treegen.graphics.Mesh;
import pl.mj.treegen.math.Matrix;
import pl.mj.treegen.math.MyRandom;
import pl.mj.treegen.math.SeedGenerator;
import pl.mj.treegen.math.Vector3;
import pl.mj.treegen.nodes.*;

import java.util.ArrayList;
import java.util.List;

import static pl.mj.treegen.graphics.Mesh.Vertex;
import static pl.mj.treegen.graphics.Mesh.Triangle;
import static pl.mj.treegen.graphics.Mesh.TextureCoord;

public class TreeElement { 
	
	private final float POINT_DISTANCE = 2f; 
	private final int MAX_NUMBER_OF_BENDS = 20;
	private List<TEPoint> points;
	private boolean hasLeaves;
	
	private int seed;
	
	public TreeElement() {
		points = new ArrayList<TEPoint>();
		this.seed = SeedGenerator.getSeed();
		hasLeaves = false;
	}
	
	public boolean getHasLeaves() {
		return hasLeaves;
	}
	
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	public int getPointsCount() {
		return points.size();
	}
	
	public TEPoint getPoint(int index) {
		return points.get(index);
	}
	
	public TEPoint getLastPoint() {
		return points.get(points.size()-1);
	}
	
	static public TEPoint getPoint(float x, List<TEPoint> points) {
		if(points.size() == 1)
			return points.get(0);
		TEPoint point = new TEPoint();
		
		int index1 = (int)(Math.floor(x * (points.size()-1))); 
		int index2 = index1 + 1;
		if(index2 >= points.size())
			index2 --;
		
		TEPoint p1 = points.get(index1);
		TEPoint p2 = points.get(index2);
		
		float x1 = (float)index1/(points.size()-1);
		float x2 = (float)index2/(points.size()-1);
		
		float d = x2 - x1;
		
		float d1 = (x - x1)/d;
		float d2 = (x2 - x)/d;
		
		point.setRadius(p1.getRadius()*d2 + p2.getRadius()*d1);
		point.setDirection(Vector3.add(
				Vector3.mult(p1.getDirection(),d2),
				Vector3.mult(p2.getDirection(),d1)));
		point.setPosition(Vector3.add(
				Vector3.mult(p1.getPosition(),d2),
				Vector3.mult(p2.getPosition(),d1)));
		for(int i=0; i<TEPoint.ARRAY_SIZE; i++) {
			point.setRadiusRandom(i,p1.getRadiusRandom(i)*d2 + p2.getRadiusRandom(i)*d1);
		}
		return point;
	}

	public float getRadius(float x) {
		TEPoint point = getPoint(x,points);
		return point.getRadius();
	}
	
	public Vector3 getDirection(float x) {
		TEPoint point = getPoint(x,points);
		return point.getDirection();
	}
	
	public Vector3 getPosition(float x) {
		TEPoint point = getPoint(x,points);
		return point.getPosition();
	}
	
	public float length() {
		float ret=0;
		Vector3 v;
		for(int i=0; i<points.size()-1; i++) {
			v = Vector3.sub(points.get(i+1).getPosition(), points.get(i).getPosition());
			ret += v.length();
		}
		return ret;
	}
	
	static public float distance(int start, int end, List<TEPoint> points) {
		float ret =0;
		Vector3 v;
		for(int i=start; i<end; i++) {
			v = Vector3.sub(points.get(i+1).getPosition(), points.get(i).getPosition());
			ret += v.length();
		}
		return ret;
	}
	
	private List<Vector3> generateBends() {
		List<Vector3> list = new ArrayList<Vector3>();
		for(int i=0; i<MAX_NUMBER_OF_BENDS; i++) {
			Vector3 direction = new Vector3(
					MyRandom.randomFloat(-1,1,SeedGenerator.getSeed()),
					MyRandom.randomFloat(-1,1,SeedGenerator.getSeed()),
					MyRandom.randomFloat(-1,1,SeedGenerator.getSeed()));
			direction.normalize();
			list.add(direction);
		}
		return list;
	}
	
	private Vector3 direction(List<Vector3> bends, float x, int numberOfBends, float bendingStrength, Vector3 startDirection, float gravity) {
		if(numberOfBends < 2)
			return startDirection;
		if(x>1)
			x=1;
		int i = (int)(x * (numberOfBends-1));
		float p = x-i*(float)(1f/(numberOfBends-1));
		p=p/(1f/(numberOfBends-1));
		Vector3 v =  Vector3.add(Vector3.mult(bends.get(i),1f-p),Vector3.mult(bends.get(i+1),p));
		v.normalize();
		
		float strength = x;
		strength= (float)Math.sqrt(strength);
		
		Vector3 ret = Vector3.add(Vector3.mult(v, bendingStrength*strength*0.7f),Vector3.mult(startDirection, 1f-bendingStrength*strength*0.5f));
		ret.normalize();
		
		ret = Vector3.add(new Vector3(0,-x*gravity*2,0),ret);
		ret.normalize();
		return ret;
	}
	
	public void generateTrunk(TreeNode treeNode, TrunkNode trunkNode) {
		points.clear();
		hasLeaves = true;
		
		float radius = treeNode.getRadius().floatValue();
		float height = treeNode.getHeight().floatValue();
		float shape = trunkNode.getShape().floatValue();
		float bendingStrength = trunkNode.getBendingStrength().floatValue(seed);
		int numberOfBends = trunkNode.getNumberOfBends().intValue(seed);
		float roughness = trunkNode.getRoughness().floatValue();
		
		float branchesStart = 1;
		for(int i=0; i<trunkNode.getChildCount(); i++) {
			Node child = (Node) trunkNode.getChildAt(i);
			if(child instanceof BranchesNode) {
				branchesStart = ((BranchesNode)child).getStart().floatValue();
				hasLeaves = false;
			}
		}
		
		List<Vector3> bends = generateBends();
		
		Vector3 up = new Vector3(0,1,0);
		Vector3 position = new Vector3(0,0,0);
		Vector3 direction = new Vector3(0,1,0);
		
		int numberOfSteps = (int)Math.floor((double)(height/POINT_DISTANCE));
		float step = height;
		if(numberOfSteps==0) {
			numberOfSteps = 1;
		} else {
			step = height/numberOfSteps;
		}
		
		List<TEPoint> tmpPoints = new ArrayList<TEPoint>();

		for(int i=0; i<=numberOfSteps+1; i++) {
			float currentRadius = (float) (1f - Math.pow(i*step/height,shape)) * radius;
			tmpPoints.add(new TEPoint(position, direction, currentRadius, roughness,points.size()));
		    direction = direction(bends,i*step/(height*branchesStart),numberOfBends,bendingStrength,up,0f);
			direction.mult(step);
			position = Vector3.add(position, direction);
		}
				
		for(int i=0; (float)i<branchesStart*(tmpPoints.size()-1); i++){
			points.add(tmpPoints.get(i));
		}
		if(points.size()==0)
			points.add(tmpPoints.get(0));
		if(branchesStart<0.999f)
			points.add(getPoint(branchesStart,tmpPoints));
	}
	
	public void generateBranche(BranchesNode branchesNode, TEPoint startPoint, Vector3 startDirection, TreeElement parentElement) {
		points.clear();
		points.add(startPoint);
		
		hasLeaves = true;
		
		float nextBranchesStart = 1;
		Node child;
		for(int i=0; i<branchesNode.getChildCount(); i++) {
			child = (Node) branchesNode.getChildAt(i);
			if(child instanceof BranchesNode) {
				nextBranchesStart = ((BranchesNode)child).getStart().floatValue();
				hasLeaves = false;
			} 
				
		}
		
		float radius = startPoint.getRadius();
		float start = branchesNode.getStart().floatValue();
		
		float parentLength = parentElement.length() / start; 
		
		float length = branchesNode.getLength().floatValue(seed) * (1f - start) * parentLength;
		float shape = branchesNode.getShape().floatValue();
		float bendingStrength = branchesNode.getBendingStrength().floatValue(seed);
		float gravity = branchesNode.getGravity().floatValue(seed);
		int numberOfBends = branchesNode.getNumberOfBends().intValue(seed);
		float roughness = branchesNode.getRoughness().floatValue();
		
		List<Vector3> bends = generateBends();
		
		Vector3 position = startPoint.getPosition();
		Vector3 direction = new Vector3();
		
		int numberOfSteps = (int)Math.floor((double)length/POINT_DISTANCE);
		float step = length;
		if(numberOfSteps==0) {
			numberOfSteps = 1;
		} else {
			step = length/numberOfSteps;
		}
		
		List<TEPoint> tmpPoints = new ArrayList<TEPoint>();
		for(int i=0; i<=numberOfSteps; i++) {
			float x = i*step/length; 
			float currentRadius = (float) (1f - Math.pow( i*step/length,shape)) * radius;
			direction = direction(bends, x, numberOfBends,bendingStrength,startDirection,gravity);
			direction.mult(step);
			position = Vector3.add(position,direction);
			tmpPoints.add(new TEPoint(position,direction,currentRadius,roughness,tmpPoints.size()));
		}
		tmpPoints.add(new TEPoint(position,direction,0,roughness,tmpPoints.size()));
		for(int i=0; (float)i<nextBranchesStart*(tmpPoints.size()-1); i++){
			points.add(tmpPoints.get(i));
		}
		if(points.size()==0)
			points.add(tmpPoints.get(0));
		else if(nextBranchesStart<0.999f)
			points.add(getPoint(nextBranchesStart,tmpPoints));
		
	}
	
	public void generateSideBranche(SideBranchesNode sideBranchesNode, float start, float rotation, TreeElement parentElement) {
		points.clear();
		
		hasLeaves = true;
		
		float nextBranchesStart = 1;
		Node child;
		for(int i=0; i<sideBranchesNode.getChildCount(); i++) {
			child = (Node) sideBranchesNode.getChildAt(i);
			if(child instanceof BranchesNode) {
				nextBranchesStart = ((BranchesNode)child).getStart().floatValue();
				hasLeaves = false;
			}
		}
		

		Vector3 startPosition = parentElement.getPosition(start);
		Vector3 parentDirection = parentElement.getDirection(start);
		parentDirection.normalize();
		float parentRadius = parentElement.getRadius(start);
		
		float radius = sideBranchesNode.getRadius().floatValue(seed) * parentRadius;
		float startDir = sideBranchesNode.getStartDirection().floatValue();
		float shape = sideBranchesNode.getShape().floatValue();
		float length = sideBranchesNode.getLength().floatValue(seed) * parentElement.length() * (1f - start);
		
		length += sideBranchesNode.getMinLength().floatValue();
		
		float bendingStrength = sideBranchesNode.getBendingStrength().floatValue(seed);
		float gravity = sideBranchesNode.getGravity().floatValue(seed);
		int numberOfBends = sideBranchesNode.getNumberOfBends().intValue(seed);
		float roughness = sideBranchesNode.getRoughness().floatValue();
		
		int numberOfSteps = (int)Math.floor((double)length/POINT_DISTANCE);
		float step = length;
		if(numberOfSteps==0) {
			numberOfSteps = 1;
		} else {
			step = length/numberOfSteps;
		}
		
		
		Vector3 startDirection = new Vector3(), normal;
		normal = parentDirection.anyNormal();
		normal.normalize();
		
		normal.normalize();
		Matrix rotMatrix = new Matrix();
		rotMatrix.rotate((float)Math.toRadians(-startDir + 90), normal);
		startDirection = Matrix.mult(parentDirection, rotMatrix);
		
		parentDirection.normalize();
		rotMatrix.rotate((float)Math.toRadians(rotation), parentDirection);
		startDirection = Matrix.mult(startDirection, rotMatrix);
		
		startDirection.normalize();
		
		List<Vector3> bends = generateBends();  
		List<TEPoint>tmpPoints = new ArrayList<TEPoint>(); 
		
		Vector3 direction = null;
		Vector3 position = startPosition;
		
		points.add(new TEPoint(position,startDirection,radius,roughness,points.size()));
		
		for(int i=0; i<=numberOfSteps; i++) {
			float x = i*step/length; 
			float currentRadius = (float) (1f - Math.pow(x,shape)) * radius;
			direction = direction(bends, x, numberOfBends,bendingStrength,startDirection,gravity);
			direction.mult(step);
			
			position = Vector3.add(position,direction);
			tmpPoints.add(new TEPoint(position,direction,currentRadius,roughness,points.size()));
			
		}
		
		tmpPoints.add(new TEPoint(position,direction,0,roughness,tmpPoints.size()));
		for(int i=0; (float)i<nextBranchesStart*(tmpPoints.size()-1); i++){
			points.add(tmpPoints.get(i));
		}
		if(points.size()==0)
			points.add(tmpPoints.get(0));
		else if(nextBranchesStart<0.999f)
			points.add(getPoint(nextBranchesStart,tmpPoints));
		
	}
	
	public void generateRoot(RootsNode rootsNode, TEPoint startPoint, Vector3 startDirection, Vector3 endDirection) {
		points.clear();
		
		float radius = startPoint.getRadius()*rootsNode.getRadius().floatValue(seed);
		float shape = rootsNode.getShape().floatValue();
		float length = rootsNode.getLength().floatValue(seed);
		
		float bendingStrength = rootsNode.getBendingStrength().floatValue(seed);
		int numberOfBends = rootsNode.getNumberOfBends().intValue(seed);
		float roughness = rootsNode.getRoughness().floatValue();
		
		int numberOfSteps = (int)Math.floor((double)length/POINT_DISTANCE);
		float step = length;
		if(numberOfSteps==0) {
			numberOfSteps = 1;
		} else {
			step = length/numberOfSteps;
		}
		
		List<Vector3> bends = generateBends(); 
		
		points.add(startPoint);
		
		Vector3 position, direction, basicDirection;
		position = startPoint.getPosition();
		for(int i=0; i<=numberOfSteps; i++) {
			float x = i*step/length;
			float currentRadius = (float)(1f - Math.pow(x, shape)) * radius;
			
			basicDirection = Vector3.add(
					Vector3.mult(startDirection, 1-x),
					Vector3.mult(endDirection,x));
			direction = direction(bends, x, numberOfBends,bendingStrength, basicDirection, 0);
			direction.mult(step);
			
			position = Vector3.add(position,direction);
			points.add(new TEPoint(position,direction.negative(),currentRadius,roughness,points.size()));
			
		}
	}
	
	public Mesh generateMesh(float scaleU, float scaleV, int verticalStep, int horitzontalDivisions) {
		Mesh mesh = new Mesh();
		
		if(points.size() < 2)
			return mesh;
		
		float horitzontalStep = (float)TEPoint.ARRAY_SIZE / horitzontalDivisions;
		float verticalDivisions = (float)(points.size()-1)/verticalStep;
		List<TEPoint> usedPoints = new ArrayList<TEPoint>();
		float step = (float)1/verticalDivisions;

		float x=0;

		TEPoint p;
		while(x < 1) {
			p = getPoint(x,points);
			usedPoints.add(p);
			x+=step;
		}

		usedPoints.add(points.get(points.size()-1));
		
		Vector3 position, normal, direction;
		float r;
		
		int[][] indices = new int[horitzontalDivisions][usedPoints.size()];
		float textureCoordV[] = new float[usedPoints.size()];
		float currentTexCoordV = 0;
		Matrix matrix = new Matrix();
		
		for(int i=0; i<usedPoints.size(); i++) {
			
			if(i>0) {
				float distance = distance(i-1, i, usedPoints);
				float radius = 1 / usedPoints.get(i-1).getRadius() / 10;
				currentTexCoordV += radius * distance;
			}else
				currentTexCoordV += 0;
			textureCoordV[i] = currentTexCoordV;
			for(int j=0; j<horitzontalDivisions; j++) {
				
				r = usedPoints.get(i).getRadiusRandom((int)(j * horitzontalStep));
				position = usedPoints.get(i).getPosition();
				direction = usedPoints.get(i).getDirection();
				
				matrix.rotate((float)Math.toRadians((float)j/horitzontalDivisions * 360), direction);
				//System.out.println(j + " " + horitzontalStep  + " " + horitzontalDivisions + " " +(float)j * horitzontalStep);
				
				normal = usedPoints.get(i).getDirection().anyNormal();
				normal = Matrix.mult(normal, matrix);
				normal.normalize();
				normal.mult(r);
				position = Vector3.add(position, normal);
				
				indices[j][i] = mesh.countVertices();
				mesh.addVertex(new Vertex(position,normal));
			}
		}
		
		int v1, v2, v3;
	
		Triangle triangle;
		for(int i=0; i<usedPoints.size()-1; i++) {
			for(int j=0; j<horitzontalDivisions; j++) {
				if(j == horitzontalDivisions - 1) {
					v1 = indices[j][i];
					v2 = indices[0][i];
					v3 = indices[0][i + 1];
					triangle = new Triangle(v1,v2,v3);
					
					triangle.setTextureCoord(0, new TextureCoord((float)j/horitzontalDivisions * scaleU, textureCoordV[i] * scaleV));
					triangle.setTextureCoord(1, new TextureCoord(scaleU, textureCoordV[i]* scaleV));
					triangle.setTextureCoord(2, new TextureCoord(scaleU, textureCoordV[i + 1] * scaleV));
		
					mesh.addTriangle(triangle);
				
					v1 = indices[0][i + 1];
					v2 = indices[j][i + 1];
					v3 = indices[j][i];

					triangle = new Triangle(v1,v2,v3);
					
					triangle.setTextureCoord(0, new TextureCoord(scaleU, textureCoordV[i + 1] * scaleV));
					triangle.setTextureCoord(1, new TextureCoord((float)j/horitzontalDivisions *  scaleU, textureCoordV[i + 1] * scaleV));
					triangle.setTextureCoord(2, new TextureCoord((float)j/horitzontalDivisions *  scaleU, textureCoordV[i] * scaleV));
					mesh.addTriangle(triangle);
				}
				else
				{
					v1 = indices[j][i];
					v2 = indices[j + 1][i];
					v3 = indices[j + 1][i + 1];
					triangle = new Triangle(v1,v2,v3);
			
					triangle.setTextureCoord(0, new TextureCoord((float)j/horitzontalDivisions *  scaleU, textureCoordV[i] * scaleV));
					triangle.setTextureCoord(1, new TextureCoord((float)(j+1)/horitzontalDivisions *  scaleU, textureCoordV[i]* scaleV));
					triangle.setTextureCoord(2, new TextureCoord((float)(j+1)/horitzontalDivisions *  scaleU, textureCoordV[i + 1] * scaleV));
					mesh.addTriangle(triangle);
				
					v1 = indices[j + 1][i + 1];
					v2 = indices[j][i + 1];
					v3 = indices[j][i];

					triangle = new Triangle(v1,v2,v3);
					triangle.setTextureCoord(0, new TextureCoord((float)(j+1)/horitzontalDivisions * scaleU, textureCoordV[i + 1]* scaleV));
					triangle.setTextureCoord(1, new TextureCoord((float)j/horitzontalDivisions * scaleU, textureCoordV[i + 1] * scaleV));
					triangle.setTextureCoord(2, new TextureCoord((float)j/horitzontalDivisions *  scaleU, textureCoordV[i] * scaleV));
			
					mesh.addTriangle(triangle);
				}
			}
		}
		
		return mesh;
	}
}
