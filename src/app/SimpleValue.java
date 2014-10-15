package app;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tree.Tree;

/**
 * Klasa odpowiada za możliwość wyboru wartości za pomocą pojedyńczego suwaka.
 *
 */
public class SimpleValue extends JPanel implements ChangeListener{
	private String name;
	private Number min,max,value,step;
	private JSlider slider;
	private JLabel label;
	
	final int LABEL_WIDTH=40;
	final int LABEL_HEIGHT=20;
	final int SLIDER_WIDTH=125;
	final int SLIDER_HEIGHT=20;
	
	/**
	 * @param name nazwa
	 * @param value wartość początkowa
	 * @param min minimalna wartość
	 * @param max maksymalna wartość 
	 * @param step krok o jaki zmienia się wartość przesuwając suwak
	 */
	public SimpleValue(String name, Number value, Number min, Number max, Number step){
		this.name=name;
		this.value = value;
		this.step = step;
		this.min=min;
		this.max=max;
		
		if(value.floatValue() > max.floatValue())
			value = max;
		
		if(value.floatValue() < min.floatValue())
			value = min;
		
		setBorder(BorderFactory.createTitledBorder(name));	
		setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		
		label = new JLabel("  "+NumberFormat.format(value),SwingConstants.LEFT);
		label.setMinimumSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		label.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		label.setMaximumSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		
		slider=new JSlider(JSlider.HORIZONTAL, (int)(min.floatValue()/step.floatValue()),(int)(max.floatValue()/step.floatValue()),(int)(value.floatValue()/step.floatValue()));
		slider.setMaximumSize(new Dimension(SLIDER_WIDTH,SLIDER_HEIGHT));
		
		add(slider);
		add(label);
		
		slider.addChangeListener(this);
	}
	
	public int intValue(){
		return value.intValue();
	}
	
	public float floatValue(){
		return value.floatValue();
	}
	
	public int getSliderValue() {
		return slider.getValue();
	}
	
	public void setSliderValue(int value) {
		slider.setValue(value);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(step.floatValue()==(float)step.intValue())
			value = (int) (slider.getValue()*step.floatValue());
		else
			value = slider.getValue()*step.floatValue();
		label.setText("  "+NumberFormat.format(value));
		Tree.somethingChanged();
	}

}
