package tree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.media.opengl.GL2;

import nodes.BranchesNode;
import nodes.LeavesNode;
import nodes.Node;
import nodes.RootsNode;
import nodes.SideBranchesNode;
import nodes.TreeNode;
import nodes.TrunkNode;
import app.InfoPanel;
import math.Matrix;
import math.MyRandom;
import math.SeedGenerator;
import math.Vector3;
import tree.TEPoint;
import graphics.DirectionalLight;
import graphics.Light;
import graphics.Mesh;
import graphics.Renderable;

public class Tree implements Renderable {
	private static boolean change;
	public static void somethingChanged() {
		change = true;
	}
	
	public static final int LODS_COUNT = 5;
	
	private static int[] lodVStep = {1,2,3,5,7}; 
	private static int[] lodHDivisions = {12,10,8,6,4}; 
	
	private List<TreeElement> elements;
	private List<Mesh> tmpMeshes;
	
	private Mesh leavesMesh;
	private Mesh[] meshes;
	
	private TreeNode treeNode;
	private TrunkNode trunkNode;
	private RootsNode rootsNode;
	private LeavesNode leavesNode;
	
	private int LOD = 0;
	private boolean hideLeaves = false;
	private int view = 0;
	
	private Light light;
	private InfoPanel infoPanel = null;
	
	public Tree() {
		light = new DirectionalLight(new Vector3(0.1f,0.4f,0.5f));
		meshes = new Mesh[LODS_COUNT];
		treeNode = new TreeNode("drzewo");
		elements = new ArrayList<TreeElement>();
		change = true;
	}
	
	public void setInfoPanel(InfoPanel infoPanel) {
		this.infoPanel = infoPanel;
	}
	
	public void setLOD(int LOD) {
		this.LOD = LOD;
	}
	
	public void setView(int view) {
		this.view = view;
	}
	
	public void setHideLeaves(boolean b) {
		hideLeaves = b;
	}
	
	public TreeNode getTreeNode() {
		return treeNode;
	}
	
	public void generateTree() {
		elements.clear();
		
		trunkNode = null;
		rootsNode = null;
		leavesNode = null;
		
		SeedGenerator.reset();
		
		Node child;
		for(int i=0; i<treeNode.getChildCount(); i++) {
			child = (Node) treeNode.getChildAt(i);
			if(child instanceof TrunkNode)
				trunkNode = (TrunkNode) child;
			if(child instanceof RootsNode)
				rootsNode = (RootsNode) child;
			if(child instanceof LeavesNode)
				leavesNode = (LeavesNode) child;
		}
			
		if(trunkNode != null) {
			TreeElement trunk = new TreeElement(); 
			trunk.generateTrunk(treeNode,trunkNode);
			elements.add(trunk);
			
			if(rootsNode != null) {
				TEPoint startPoint = trunk.getPoint(0).clone();
				generateRoots(rootsNode, startPoint); 
			}
			
			List<TreeElement> list = new ArrayList<TreeElement>();
			list.add(trunk);
			
			generateTreeElements(trunkNode,list);
			
			float scaleU = treeNode.getTextureScaleU().floatValue();
			float scaleV = treeNode.getTextureScaleV().floatValue();
			
			for(int i=0; i<LODS_COUNT; i++) {
				tmpMeshes = new ArrayList<Mesh>();
				for(TreeElement e : elements) {
					Mesh mesh = new Mesh();
					mesh = e.generateMesh(scaleU, scaleV, lodVStep[i], lodHDivisions[i]);
					tmpMeshes.add(mesh);
				}
				meshes[i] = Mesh.regroup(tmpMeshes);

			}
		}
		if(leavesNode != null) {
			List<TEPoint> ends = new ArrayList<TEPoint>();
			for(TreeElement e : elements)
				if(e.getHasLeaves() == true)
					ends.add(e.getLastPoint());
			leavesMesh = new Mesh();
			leavesMesh =  Leaves.generateLeaves(leavesNode, ends);
		}
		for(int i=0; i<LODS_COUNT; i++)
			if(infoPanel != null) {
				int count = 0;
				if(meshes[i] != null)
					count += meshes[i].countTriangles();
				if(leavesNode !=null && leavesMesh != null)
					count += leavesMesh.countTriangles();
				infoPanel.setTrianglesCount(i, count);
			}
	}
	
	private void generateRoots(RootsNode rootsNode, TEPoint startPoint) {
		int number = rootsNode.getNumber().intValue();
		float startDirection = rootsNode.getStartDirection().floatValue();
		float endDirection = rootsNode.getEndDirection().floatValue();
		
		Matrix mat = new Matrix();
		Vector3 startVec = null, endVec = null;
		
		TreeElement root;
		
		for(int i=0; i<number; i++) {
			
			mat.rotate((float)Math.toRadians(startDirection),new Vector3(0,0,1));
			startVec = Matrix.mult(new Vector3(1,0,0), mat);
			
			float angle = (float)i/number * 360;
			mat.rotate((float)Math.toRadians(angle), new Vector3(0,1,0));
			startVec = Matrix.mult(startVec, mat);
			
			startVec.normalize();
			
			mat.rotate((float)Math.toRadians(90-endDirection),new Vector3(0,0,1));
			endVec = Matrix.mult(new Vector3(1,0,0), mat);
			
			angle = (float)i/number * 360;
			mat.rotate((float)Math.toRadians(angle), new Vector3(0,1,0));
			endVec = Matrix.mult(endVec, mat);
			
			endVec.normalize();
			
			root = new TreeElement();
			root.generateRoot(rootsNode, startPoint, startVec, endVec);
			elements.add(root);
			
		}
	}
	
	private List<TreeElement> generateBranches(BranchesNode branchesNode, List<TreeElement> list, int nodeNumber) {
		List<TreeElement> addedTreeElements = new ArrayList<TreeElement>();
		for(int i=0; i<list.size(); i++) {
			
			int numberOfBranches = branchesNode.getNumber().intValue(SeedGenerator.getSeed());
			float spread = branchesNode.getSpread().floatValue(SeedGenerator.getSeed());
			
			TEPoint startPoint = list.get(i).getLastPoint();
			
			List<Vector3> startDirections = new ArrayList<Vector3>();
			
			Vector3 normal = Vector3.cross(startPoint.getDirection(),new Vector3(1,0,0));
			normal.normalize();
			Vector3 startDirection = Vector3.add(Vector3.mult(normal, spread), Vector3.mult(startPoint.getDirection(), 1f-spread));
			startDirection.normalize();
			
			float startAngle = MyRandom.randomFloat(0, 180, SeedGenerator.getSeed());
			for(int k=0; k<numberOfBranches; k++) {

				Vector3 nextStartDirection;
				Matrix rotation = new Matrix();
				rotation.rotate((float)Math.toRadians(startAngle + (float)k/numberOfBranches * 360f), startPoint.getDirection());
				nextStartDirection = Matrix.mult(startDirection, rotation);
				startDirections.add(nextStartDirection);
			}
			
			for(int j=0; j<numberOfBranches; j++) {
				TreeElement branch = new TreeElement();
				branch.generateBranche((BranchesNode)branchesNode,startPoint, startDirections.get(j), list.get(i));
				elements.add(branch);
				addedTreeElements.add(branch);
			}
		}
		return addedTreeElements;
	}
	
	private List<TreeElement> generateSideBranches(SideBranchesNode sideBranchesNode, List<TreeElement> list,int nodeNumber) {
		List<TreeElement> addedTreeElements = new ArrayList<TreeElement>();
		for(int i=0; i<list.size(); i++) {
			int numberOfBranches = sideBranchesNode.getNumber().intValue(SeedGenerator.getSeed()); 
			float start = sideBranchesNode.getStart().floatValue();
			float end = sideBranchesNode.getEnd().floatValue();
			
			for(int j=0; j<numberOfBranches; j++) {
				float position = (end - start) * ((float)j/numberOfBranches) + start;
				float rotation = j * 137.5f;
				
				TreeElement sideBranche = new TreeElement();
				sideBranche.generateSideBranche((SideBranchesNode)sideBranchesNode, position, rotation, list.get(i));
				
				elements.add(sideBranche);
				addedTreeElements.add(sideBranche);
			}
		}
		return addedTreeElements;
	}
	
	private void generateTreeElements(Node node, List<TreeElement> list) {
		Node child;
		for(int i=0; i<node.getChildCount(); i++) {
			
			child = (Node) node.getChildAt(i);
			List<TreeElement> addedTreeElements = null;
			if(child instanceof BranchesNode) {
				addedTreeElements = generateBranches((BranchesNode)child, list, i);
			}
			if(child instanceof SideBranchesNode) {
				addedTreeElements = generateSideBranches((SideBranchesNode)child, list, i);
			}
			generateTreeElements(child,addedTreeElements);
		}
	}
	
	public void Render(GL2 gl) {
		if(change == true) {
			generateTree();
			change = false;
		}
		
		if(view == 1 || view == 2)
			gl.glDisable(GL2.GL_TEXTURE);
		else
			gl.glEnable(GL2.GL_TEXTURE);
		
		if(view == 2) {
			gl.glDisable(GL2.GL_LIGHTING);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		} else {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			gl.glEnable(GL2.GL_LIGHTING);
			light.on(gl, 0);
		}
		
		gl.glColor3f(1, 1, 1);
		
		if(trunkNode != null) {
			if(view == 0)
				treeNode.getBarkMaterial().use(gl);
			meshes[LOD].Render(gl);
		}

		if(leavesNode != null && hideLeaves == false) {
			if(view == 0)
				leavesNode.getMaterial().use(gl);
			leavesMesh.Render(gl);
		}
		
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}
	
	public void load(String path) throws IOException {
		treeNode.newTree();
		Scanner scanner = new Scanner(new File(path));
		treeNode.load(scanner);
		change = true;
	}

	public void save(String path) throws IOException {
		FileWriter fileWriter = new FileWriter(path); 
		treeNode.save(fileWriter);
		fileWriter.close();
	}
	
	public void newTree() {
		treeNode.newTree();
		change = true;
	}
	
	public void export(String path,String name) throws IOException {
		String mtlPath = path + ".mtl";
		String mtlName = name + ".mtl";
		String objPath;
		
		FileWriter fileWriter = new FileWriter(mtlPath);
		fileWriter.write("newmtl leaves\r\n");
		fileWriter.write("Ka 1 1 1\r\n");
		fileWriter.write("Kd 1 1 1\r\n");
		fileWriter.write("Ks 1 1 1\r\n");
		if(!leavesNode.getMaterial().getTexturePath().equals(""))
				fileWriter.write("map_Kd " + leavesNode.getMaterial().getTexturePath());
		
		fileWriter.write("\r\n");
		fileWriter.write("newmtl tree\r\n");
		fileWriter.write("Ka 1 1 1\r\n");
		fileWriter.write("Kd 1 1 1\r\n");
		fileWriter.write("Ks 1 1 1\r\n");
		if(!treeNode.getBarkMaterial().getTexturePath().equals(""))
				fileWriter.write("map_Kd " + treeNode.getBarkMaterial().getTexturePath());
		fileWriter.close();
		
		for(int i=0; i<LODS_COUNT; i++) {
			objPath = path + "LOD_" + i + ".obj";
			fileWriter = new FileWriter(objPath);
				fileWriter.write("mtllib " + mtlName + "\r\n");
				Mesh.export(meshes[i], leavesMesh, fileWriter);
			fileWriter.close();
		}
		
	}
	
}
