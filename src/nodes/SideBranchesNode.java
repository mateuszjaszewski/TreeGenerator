package nodes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import app.MinMaxValue;
import app.SimpleValue;

public class SideBranchesNode extends Node{
	
	private SimpleValue shape, start, end, startDirection, roughness, minLength;
	private MinMaxValue length, radius, number, bendingStrength, numberOfBends, gravity;
	
	public SimpleValue getShape() {
		return shape;
	}
	public SimpleValue getStart() {
		return start;
	}
	public SimpleValue getEnd() {
		return end;
	}
	public SimpleValue getStartDirection() {
		return startDirection;
	}
	public SimpleValue getRoughness() {
		return roughness;
	}
	public SimpleValue getMinLength() {
		return minLength;
	}
	public MinMaxValue getLength() {
		return length;
	}
	public MinMaxValue getRadius() {
		return radius;
	}
	public MinMaxValue getNumber() {
		return number;
	}
	public MinMaxValue getBendingStrength() {
		return bendingStrength;
	}
	public MinMaxValue getNumberOfBends() {
		return numberOfBends;
	}
	public MinMaxValue getGravity() {
		return gravity;
	}
	
	public SideBranchesNode(Object object) {
		super(object);
		
		shape = new SimpleValue("Kształt", 1,0.1,3,0.1);
		start = new SimpleValue("Początek", 0.5,0.01,1,0.01);
		end = new SimpleValue("Koniec", 1,0,1,0.01);
		minLength = new SimpleValue("Minimalna długość", 0,0,10,0.1);
		length = new MinMaxValue("Długość", 0.9,1.1,0,2,0.01);
		radius = new MinMaxValue("Promień", 0.7,0.9,0,1,0.01);
		
		number = new MinMaxValue("Liczba", 5,10,1,30,1);
		
		startDirection = new SimpleValue("Początkowy kierunek", 0,-90,90,1);
		
		bendingStrength = new MinMaxValue("Siła skrzywień", 0.1,0.2,0,1,0.01);
		numberOfBends = new MinMaxValue("Ilość skrzywień", 3,4,0,10,1);
		gravity = new MinMaxValue("Grawitacja", -0.1,0.1,-1,1,0.01);
		roughness = new SimpleValue("Chropowatość", 0.1,0,1,0.01);
		
		panel.add(shape);
		panel.add(start);
		panel.add(end);
		panel.add(minLength);
		panel.add(length);
		panel.add(radius);
		panel.add(number);
		panel.add(startDirection);
		panel.add(bendingStrength);
		panel.add(numberOfBends);
		panel.add(gravity);
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
		fileWriter.write("sideBranches\r\n");
		fileWriter.write(shape.getSliderValue() + "\r\n");
		fileWriter.write(start.getSliderValue() + "\r\n");
		fileWriter.write(end.getSliderValue() + "\r\n");
		fileWriter.write(startDirection.getSliderValue() + "\r\n");
		fileWriter.write(roughness.getSliderValue() + "\r\n");
		fileWriter.write(minLength.getSliderValue() + "\r\n");
		
		fileWriter.write(length.getMinSliderValue() + " ");
		fileWriter.write(length.getMaxSliderValue() + "\r\n");
		
		fileWriter.write(radius.getMinSliderValue() + " ");
		fileWriter.write(radius.getMaxSliderValue() + "\r\n");
		
		fileWriter.write(number.getMinSliderValue() + " ");
		fileWriter.write(number.getMaxSliderValue() + "\r\n");
		
		fileWriter.write(bendingStrength.getMinSliderValue() + " ");
		fileWriter.write(bendingStrength.getMaxSliderValue() + "\r\n");
		
		fileWriter.write(numberOfBends.getMinSliderValue() + " ");
		fileWriter.write(numberOfBends.getMaxSliderValue() + "\r\n");
		
		fileWriter.write(gravity.getMinSliderValue() + " ");
		fileWriter.write(gravity.getMaxSliderValue() + "\r\n");
		
		for(int i=0; i<getChildCount(); i++) {
			Node child = (Node)getChildAt(i);
			child.save(fileWriter);
		}
		
		fileWriter.write("sideBranchesEnd\r\n");
		
	}
	
	public void load(Scanner scanner) throws IOException {
		Node child;
		String str;
		int value;
		
		value = scanner.nextInt();
		shape.setSliderValue(value);
		
		value = scanner.nextInt();
		start.setSliderValue(value);
		
		value = scanner.nextInt();
		end.setSliderValue(value);
		
		value = scanner.nextInt();
		startDirection.setSliderValue(value);
		
		value = scanner.nextInt();
		roughness.setSliderValue(value);
		
		value = scanner.nextInt();
		minLength.setSliderValue(value);
		
		value = scanner.nextInt();
		length.setMinSliderValue(value);
		value = scanner.nextInt();
		length.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		radius.setMinSliderValue(value);
		value = scanner.nextInt();
		radius.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		number.setMinSliderValue(value);
		value = scanner.nextInt();
		number.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		bendingStrength.setMinSliderValue(value);
		value = scanner.nextInt();
		bendingStrength.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		numberOfBends.setMinSliderValue(value);
		value = scanner.nextInt();
		numberOfBends.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		gravity.setMinSliderValue(value);
		value = scanner.nextInt();
		gravity.setMaxSliderValue(value);
		
		str = "";
		while(!str.equals("sideBranchesEnd")) {
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
