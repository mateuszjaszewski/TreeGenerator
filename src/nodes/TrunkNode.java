package nodes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import app.MinMaxValue;
import app.SimpleValue;

public class TrunkNode extends Node {
	private SimpleValue shape, roughness;
	private MinMaxValue bendingStrength, numberOfBends;
	public SimpleValue getShape() {
		return shape;
	}
	public SimpleValue getRoughness() {
		return roughness;
	}
	public MinMaxValue getBendingStrength() {
		return bendingStrength;
	}
	public MinMaxValue getNumberOfBends() {
		return numberOfBends;
	}
	
	public TrunkNode(Object object) {
		super(object);
		
		shape = new SimpleValue("Kształt", 1,0.1,3,0.1);
		bendingStrength = new MinMaxValue("Siła skrzywień", 0.1,0.2,0,1,0.01);
		numberOfBends = new MinMaxValue("Ilość skrzywień", 3,4,0,20,1);
		roughness = new SimpleValue("Chropowatość", 0.1,0,1,0.01);
		
		panel.add(shape);
		panel.add(bendingStrength);
		panel.add(numberOfBends);
		panel.add(roughness);
	}

	public List<NodeType> getNodeTypesCanBeAdded() {
		List<NodeType> list = new ArrayList<NodeType>();
		if(getChildCount(NodeType.BRANCHES) == 0)
			list.add(NodeType.BRANCHES);
		
		list.add(NodeType.SIDE_BRANCHES);
		return list;
	}
	
	public void save(FileWriter fileWriter) throws IOException {
		fileWriter.write("trunk\r\n");
		fileWriter.write(shape.getSliderValue() + "\r\n");
		fileWriter.write(roughness.getSliderValue() + "\r\n");
		fileWriter.write(bendingStrength.getMinSliderValue() + " ");
		fileWriter.write(bendingStrength.getMaxSliderValue() + "\r\n");
		fileWriter.write(numberOfBends.getMinSliderValue() + " ");
		fileWriter.write(numberOfBends.getMaxSliderValue() + "\r\n");
		
		for(int i=0; i<getChildCount(); i++) {
			Node child = (Node)getChildAt(i);
			child.save(fileWriter);
		}
		
		fileWriter.write("trunkEnd\r\n");
	}
	public void load(Scanner scanner) throws IOException {
		Node child;
		String str;
		int value;
		
		value = scanner.nextInt();
		shape.setSliderValue(value);
		
		value = scanner.nextInt();
		roughness.setSliderValue(value);
		
		value = scanner.nextInt();
		bendingStrength.setMinSliderValue(value);
		value = scanner.nextInt();
		bendingStrength.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		numberOfBends.setMinSliderValue(value);
		value = scanner.nextInt();
		numberOfBends.setMaxSliderValue(value);
		
		str = "";
		while(!str.equals("trunkEnd")) {
			str = scanner.next();
			
			if(str.equals("branches")) {
				child = new BranchesNode("gałęzie");
				add(child);
				child.load(scanner);
			}
			
			if(str.equals("sideBranches")) {
				child = new SideBranchesNode("gałęzie boczne");
				add(child);
				child.load(scanner);
			}
		}
	}
}
