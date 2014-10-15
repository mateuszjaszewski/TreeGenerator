package app;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import tree.Leaves;
import tree.Tree;
import tree.Leaves.LeavesType;

/**
 * Klasa dziedzicząca po JPanel, pozwala na wybieranie typu liści z rozwijanej listy.
 * 
 * @author Mateusz Jaszewski
 */
public class LeavesTypeChooser extends JPanel{
	private final int COMBO_BOX_HEIGHT = 30;
	private final int COMBO_BOX_WIDTH = 170;
	private JComboBox<Leaves.LeavesType> comboBox;
	
	public LeavesTypeChooser(String name) {
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createTitledBorder(name));
		
		comboBox = new JComboBox<Leaves.LeavesType>();
		comboBox.setMinimumSize(new Dimension(COMBO_BOX_WIDTH,COMBO_BOX_HEIGHT));
		comboBox.setPreferredSize(new Dimension(COMBO_BOX_WIDTH,COMBO_BOX_HEIGHT));
		comboBox.setMaximumSize(new Dimension(COMBO_BOX_WIDTH,COMBO_BOX_HEIGHT));
		
		comboBox.addItem(Leaves.LeavesType.SINGLE);
		comboBox.addItem(Leaves.LeavesType.DOUBLE);
		comboBox.addItem(Leaves.LeavesType.TRIPLE);
		
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				Tree.somethingChanged();
			}
		});
		
		add(comboBox);
	}
	
	/**
	 * @return Wybrany rodzaj liści
	 */
	public Leaves.LeavesType getSelectedType() {
		return (LeavesType) comboBox.getSelectedItem();
	}
	
	/**
	 * @return Wybrany indeks
	 */
	public int getSelectedIndex(){
		return comboBox.getSelectedIndex();
	}
	
	/**
	 * Ustawia indeks na podany
	 * @param index indeks
	 */
	public void setSelectedIndex(int index) {
		comboBox.setSelectedIndex(index);
	}
}
