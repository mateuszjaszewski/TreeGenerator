package nodes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import app.MinMaxValue;
import app.SimpleValue;
import app.TreePanel;

public class RootsNode extends Node{
	private SimpleValue shape,number,startDirection,endDirection,roughness;
	private MinMaxValue length, radius, bendingStrength, numberOfBends;
	
	public SimpleValue getShape() {
		return shape;
	}
	public SimpleValue getNumber() {
		return number;
	}
	public SimpleValue getEndDirection() {
		return endDirection;
	}
	public SimpleValue getStartDirection() {
		return startDirection;
	}
	public SimpleValue getRoughness() {
		return roughness;
	}
	public MinMaxValue getLength() {
		return length;
	}
	public MinMaxValue getBendingStrength() {
		return bendingStrength;
	}
	public MinMaxValue getNumberOfBends() {
		return numberOfBends;
	}
	public MinMaxValue getRadius() {
		return radius;
	}
	public RootsNode(Object object) {
		super(object);
		
		shape = new SimpleValue("Kształt", 1,0.1,3,0.1);
		length = new MinMaxValue("Długość", 10,14,0,30,0.01);
		radius = new MinMaxValue("Promień", 0.5,0.7,0,1,0.01);
		number = new SimpleValue("Liczba", 5,1,15,1);
		startDirection = new SimpleValue("Kierunek początkowy", 10,0,90,1);
		endDirection = new SimpleValue("Kierunek końcowy", 10,0,90,1);
		bendingStrength = new MinMaxValue("Siła skrzywień", 0.1,0.2,0,1,0.01);
		numberOfBends = new MinMaxValue("Ilość skrzywień", 3,4,0,10,1);
		roughness = new SimpleValue("Chropowatość", 0.1,0,1,0.01);
		
		panel.add(shape);
		panel.add(length);
		panel.add(radius);
		panel.add(number);
		panel.add(startDirection);
		panel.add(endDirection);
		panel.add(bendingStrength);
		panel.add(numberOfBends);
		panel.add(roughness);
	}

	public List<NodeType> getNodeTypesCanBeAdded() {
		ArrayList<NodeType> list = new ArrayList<NodeType>();
		return list;
	}

	public void save(FileWriter fileWriter) throws IOException {
		fileWriter.write("roots\r\n");
		fileWriter.write(shape.getSliderValue() + "\r\n");
		fileWriter.write(number.getSliderValue() + "\r\n");
		fileWriter.write(startDirection.getSliderValue() + "\r\n");
		fileWriter.write(endDirection.getSliderValue() + "\r\n");

		fileWriter.write(roughness.getSliderValue() + "\r\n");
		
		fileWriter.write(length.getMinSliderValue() + " ");
		fileWriter.write(length.getMaxSliderValue() + "\r\n");
		fileWriter.write(radius.getMinSliderValue() + " ");
		fileWriter.write(radius.getMaxSliderValue() + "\r\n");

		fileWriter.write(bendingStrength.getMinSliderValue() + " ");
		fileWriter.write(bendingStrength.getMaxSliderValue() + "\r\n");
		
		fileWriter.write(numberOfBends.getMinSliderValue() + " ");
		fileWriter.write(numberOfBends.getMaxSliderValue() + "\r\n");
		
		for(int i=0; i<getChildCount(); i++) {
			Node child = (Node)getChildAt(i);
			child.save(fileWriter);
		}
		
		fileWriter.write("rootsEnd\r\n");
		
	}
	
	public void load(Scanner scanner) {
		String str;
		int value;
		
		value = scanner.nextInt();
		shape.setSliderValue(value);
		
		value = scanner.nextInt();
		number.setSliderValue(value);
		
		value = scanner.nextInt();
		startDirection.setSliderValue(value);
		
		value = scanner.nextInt();
		endDirection.setSliderValue(value);
		
		value = scanner.nextInt();
		roughness.setSliderValue(value);
		
		value = scanner.nextInt();
		length.setMinSliderValue(value);
		value = scanner.nextInt();
		length.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		radius.setMinSliderValue(value);
		value = scanner.nextInt();
		radius.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		bendingStrength.setMinSliderValue(value);
		value = scanner.nextInt();
		bendingStrength.setMaxSliderValue(value);
		
		value = scanner.nextInt();
		numberOfBends.setMinSliderValue(value);
		value = scanner.nextInt();
		numberOfBends.setMaxSliderValue(value);
		
		str = scanner.next(); // rootsEnd;
	}
}
