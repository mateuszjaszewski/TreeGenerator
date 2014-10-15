package nodes;

import graphics.Material;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import nodes.Node.NodeType;
import math.MyRandom;
import app.MinMaxValue;
import app.SimpleValue;
import app.TextureChooser;
import app.TreePanel;

public class TreeNode extends Node{
	
	private SimpleValue height, radius, textureScaleU, textureScaleV;
	private TextureChooser textureChooser;
	private Material barkMaterial;
	
	public SimpleValue getHeight() {
		return height;
	}
	public SimpleValue getRadius() {
		return radius;
	}
	public SimpleValue getTextureScaleU() {
		return textureScaleU;
	}
	public SimpleValue getTextureScaleV() {
		return textureScaleV;
	}
	public Material getBarkMaterial() {
		return barkMaterial;
	}
	
	public TreeNode(Object object) {
		super(object);
		newTree();
	}
	
	public List<NodeType> getNodeTypesCanBeAdded(){
		List<NodeType> list = new ArrayList<NodeType>();
		
		if(getChildCount(NodeType.TRUNK) == 0)
			list.add(NodeType.TRUNK);
		
		if(getChildCount(NodeType.ROOTS) == 0)
			list.add(NodeType.ROOTS);
		
		if(getChildCount(NodeType.LEAVES) == 0)
			list.add(NodeType.LEAVES);
			
		return list;
	}
	
	public void save(FileWriter fileWriter) throws IOException {
		fileWriter.write("tree\r\n");
		fileWriter.write(MyRandom.getGlobalSeed() + "\r\n");
		if(barkMaterial.getTexturePath().equals(""))
			fileWriter.write("0\r\n");
		else {
			fileWriter.write("1 ");
			fileWriter.write(barkMaterial.getTexturePath() + "\r\n");
		}
		fileWriter.write(height.getSliderValue() + "\r\n");
		fileWriter.write(radius.getSliderValue() + "\r\n");
		fileWriter.write(textureScaleU.getSliderValue() + "\r\n");
		fileWriter.write(textureScaleV.getSliderValue() + "\r\n");
		
		for(int i=0; i<getChildCount(); i++) {
			Node child = (Node)getChildAt(i);
			child.save(fileWriter);
		}
		
		fileWriter.write("treeEnd\r\n");
	}
	
	public void load(Scanner scanner) throws IOException {
		Node child;
		String str;
		int value;
		
		str = scanner.next();
		System.out.println(str);
		
		value = scanner.nextInt();
		MyRandom.setGlobalSeed(value);
		
		value = scanner.nextInt();
		if(value == 1) {
			str = scanner.next();
			barkMaterial.loadTexture(str);
			textureChooser.refresh(str);
		}
		
		value = scanner.nextInt();
		height.setSliderValue(value);
		
		value = scanner.nextInt();
		radius.setSliderValue(value);
		
		value = scanner.nextInt();
		textureScaleU.setSliderValue(value);
		value = scanner.nextInt();
		textureScaleV.setSliderValue(value);
		
		str = "";
		while(!str.equals("treeEnd")) {
			str = scanner.next();
			if(str.equals("trunk")) {
				child = new TrunkNode("pień");
				add(child);
				child.load(scanner);
			}
			
			if(str.equals("roots")) {
				child = new RootsNode("korzenie");
				add(child);
				child.load(scanner);
			}
			
			if(str.equals("leaves")) {
				child = new LeavesNode("liście");
				add(child);
				child.load(scanner);
			}
		}
	}
	
	public void newTree() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		
		barkMaterial = new Material();
		
		height = new SimpleValue("Wysokość", 30,1,100,1);
		radius = new SimpleValue("Promień", 0.7,0,5,0.1);
		textureScaleU = new SimpleValue("Powtarzanie tekstury U", 1, 0.1, 5, 0.1);
		textureScaleV = new SimpleValue("Powtarzanie tekstury V", 1, 0.1, 5, 0.1);
		textureChooser = new TextureChooser("Tekstura kory", barkMaterial);
		
		panel.add(height);
		panel.add(radius);
		panel.add(textureChooser);
		panel.add(textureScaleU);
		panel.add(textureScaleV);
		
		removeAllChildren();
	}
}
