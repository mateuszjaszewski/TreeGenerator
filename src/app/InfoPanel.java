package app;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tree.Tree;

public class InfoPanel extends JPanel{
	private JLabel[] labels;
	public InfoPanel() {
		setBorder(BorderFactory.createTitledBorder("Liczba trójkątów"));
		setLayout(new GridLayout(Tree.LODS_COUNT,1));
		
		setMinimumSize(new Dimension(220,100));
		setPreferredSize(new Dimension(220,100));
		setMaximumSize(new Dimension(220,100));
		
		labels = new JLabel[Tree.LODS_COUNT];
		for(int i=0; i<Tree.LODS_COUNT; i++) {
			labels[i] = new JLabel();
			add(labels[i]);
		}
	}
	
	public void setTrianglesCount(int i, int count) {
		labels[i].setText("Poziom " + i + " : " + count);
	}
	
}
