package pl.mj.treegen.nodes;

import pl.mj.treegen.app.LeavesTypeChooser;
import pl.mj.treegen.app.MinMaxValue;
import pl.mj.treegen.app.TextureChooser;
import pl.mj.treegen.graphics.Material;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static pl.mj.treegen.tree.Leaves.LeavesType;

public class LeavesNode extends Node{

	private MinMaxValue size;
	private TextureChooser textureChooser;
	private Material leavesMaterial;
	private LeavesTypeChooser leavesTypeChooser;
	
	public MinMaxValue getSize() { 
		return size;
	}
	
	public Material getMaterial() {
		return leavesMaterial;
	}
	
	public LeavesType getLeavesType() {
		return leavesTypeChooser.getSelectedType();
	}
	
	public LeavesNode(Object object) {
		super(object);
		
		leavesMaterial = new Material();
		
		size = new MinMaxValue("Rozmiar", 0.5,1,0,10,0.1);
		textureChooser = new TextureChooser("Tekstura liści",leavesMaterial);
		leavesTypeChooser = new LeavesTypeChooser("Typ liści");
		
		panel.add(size);
		panel.add(textureChooser);
		panel.add(leavesTypeChooser);
	}

	public List<NodeType> getNodeTypesCanBeAdded() {
		ArrayList<NodeType> list = new ArrayList<NodeType>();
		return list;
	}
	
	public void save(FileWriter fileWriter) throws IOException {
		fileWriter.write("leaves\r\n");
		if(leavesMaterial.getTexturePath().equals(""))
			fileWriter.write("0\r\n");
		else {
			fileWriter.write("1 ");
			fileWriter.write(leavesMaterial.getTexturePath() + "\r\n");
		}
		fileWriter.write(size.getMinSliderValue() + " ");
		fileWriter.write(size.getMaxSliderValue() + "\r\n");
		fileWriter.write(leavesTypeChooser.getSelectedIndex() + "\r\n");
		fileWriter.write("leavesEnd\r\n");
	}

	public void load(Scanner scanner) throws IOException{
		String str;
		int value;
		
		value = scanner.nextInt();
		if(value == 1) {
			str = scanner.next();
			leavesMaterial.loadTexture(str);
			textureChooser.refresh(str);
		}
		
		value = scanner.nextInt();
		size.setMinSliderValue(value);
		value = scanner.nextInt();
		size.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		leavesTypeChooser.setSelectedIndex(value);
		
		str = scanner.next(); // leavesEnd
	}
}
