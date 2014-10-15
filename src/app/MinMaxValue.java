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
import math.MyRandom;
import app.NumberFormat;

/**
 * Klasa pozwala za pomocą dwuch suwaków określić przedział z jakiego będą losowane wartości.
 * 
 * @author Admin
 *
 */
public class MinMaxValue extends JPanel implements ChangeListener{

	private JSlider minSlider,maxSlider;
	private JLabel minLabel, maxLabel;
	private Number minValue,maxValue;
	private Number step;
	private JPanel minPanel,maxPanel;
	
	final int LABEL_WIDTH=40;
	final int LABEL_HEIGHT=20;
	final int SLIDER_WIDTH=125;
	final int SLIDER_HEIGHT=20;
	
	/**
	 * @param name nazwa parametru
	 * @param minValue minimalna wartość
	 * @param maxValue maksymalna wartość
	 * @param min minimalna wartość dolnego zakres
	 * @param max maksymalny wartość górnego zakres
	 * @param step krok o jaki minimalnie można przesunąć suwak
	 */
	public MinMaxValue(String name, Number minValue, Number maxValue, Number min, Number max, Number step) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
		
		if(maxValue.floatValue() > max.floatValue())
			maxValue = max;
		
		if(minValue.floatValue() < min.floatValue())
			minValue = min;
		
		if(minValue.floatValue() > maxValue.floatValue())
			maxValue=minValue;
		
		setBorder(BorderFactory.createTitledBorder(name));
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		minPanel = new JPanel();
		minPanel.setLayout(new BoxLayout(minPanel,BoxLayout.LINE_AXIS));
		
		minLabel = new JLabel("  "+NumberFormat.format(minValue),SwingConstants.LEFT);
		minLabel.setMinimumSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		minLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		minLabel.setMaximumSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		
		minSlider=new JSlider(JSlider.HORIZONTAL, (int)(min.floatValue()/step.floatValue()),(int)(max.floatValue()/step.floatValue()),(int)(minValue.floatValue()/step.floatValue()));
		minSlider.addChangeListener(this);
		minSlider.setMaximumSize(new Dimension(SLIDER_WIDTH,SLIDER_HEIGHT));
	   
	    minPanel.add(minSlider);
	    minPanel.add(minLabel);
		
		maxPanel = new JPanel();
		maxPanel.setLayout(new BoxLayout(maxPanel,BoxLayout.LINE_AXIS));
		
		maxLabel = new JLabel("  "+NumberFormat.format(maxValue),SwingConstants.LEFT);
		maxLabel.setMinimumSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		maxLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		maxLabel.setMaximumSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
		
		maxSlider=new JSlider(JSlider.HORIZONTAL, (int)(min.floatValue()/step.floatValue()),(int)(max.floatValue()/step.floatValue()),(int)(maxValue.floatValue()/step.floatValue()));
		maxSlider.addChangeListener(this);
	    maxSlider.setMaximumSize(new Dimension(SLIDER_WIDTH,SLIDER_HEIGHT));
	   
	    maxPanel.add(maxSlider);
	    maxPanel.add(maxLabel);
		
		add(minPanel);
		add(maxPanel);
		
	}
	
	/**
	 * Zwraca pseudolosową wartość z podanego zakresu w zależności od podanego ziarna.
	 * @param seed ziarno
	 * @return pseudolosowa wartość typu int
	 */
	public int intValue(int seed) {
		return MyRandom.randomInt(minValue.intValue(), maxValue.intValue(), seed);
	}
	
	/**
	 * Zwraca pseudolosową wartość z podanego zakresu w zależności od podanego ziarna.
	 * @param seed ziarno
	 * @return pseudolosowa wartość typu float
	 */
	public float floatValue(int seed){
		return MyRandom.randomFloat(minValue.floatValue(), maxValue.floatValue(), seed);
	}
	
	public void setMinSliderValue(int value) {
		minSlider.setValue(value);
	}
	
	public int getMinSliderValue() {
		return minSlider.getValue();
	}
	
	public void setMaxSliderValue(int value) {
		maxSlider.setValue(value);
	}
	
	public int getMaxSliderValue() {
		return maxSlider.getValue();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==minSlider) {
			if(minSlider.getValue()>maxSlider.getValue()){
				maxSlider.setValue(minSlider.getValue());
			}
		}
		
		if(e.getSource()==maxSlider) {
			if(maxSlider.getValue() < minSlider.getValue()){
				minSlider.setValue(maxSlider.getValue());
			}
		}
		
		if(step.floatValue()==(float)step.intValue())
			minValue = (int) (minSlider.getValue()*step.floatValue());
		else
			minValue = minSlider.getValue()*step.floatValue();
		minLabel.setText("  "+NumberFormat.format(minValue));
		
		if(step.floatValue()==(float)step.intValue())
			maxValue = (int) (maxSlider.getValue()*step.floatValue());
		else
			maxValue = maxSlider.getValue()*step.floatValue();
		maxLabel.setText("  "+NumberFormat.format(maxValue));
		Tree.somethingChanged();
	}
}
