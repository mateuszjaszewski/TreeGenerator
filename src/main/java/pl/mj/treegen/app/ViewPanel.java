package pl.mj.treegen.app;

import pl.mj.treegen.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa służy do obsługi panelu na którym wyświetlane są parametry widoku: 
 *  - sposób rysowanie (tekstura, samo oświetleni, siatka modelu)
 *  - wyśiwetlany poziom szczegółowości.
 * 
 * @author Mateusz Jaszewski
 *
 */
public class ViewPanel extends JPanel implements ActionListener{
	
	private Tree tree;
	private JComboBox<String> lod;
	private JComboBox<String> view;
	private JCheckBox hideLeaves;
	
	/**
	 * @param tree drzewo dla którego zmianiany bédzie widok
	 */
	public ViewPanel(Tree tree) {
		super();
		
		this.tree = tree;
		
		setLayout(new GridLayout(3,1));
		setMinimumSize(new Dimension(220,120));
		setPreferredSize(new Dimension(220,120));
		setMaximumSize(new Dimension(220,120));
		setBorder(BorderFactory.createTitledBorder("Widok"));
		
		lod = new JComboBox<String>();
		for(int i=0; i<Tree.LODS_COUNT; i++) {
			String item = "Poziom uproszczenia " + i;
			lod.addItem(item);
		}
		lod.addActionListener(this);
		
		view = new JComboBox<String>();
		view.addItem("Teskturowanie");
		view.addItem("Oświetlenie");
		view.addItem("Siatka");
		view.addActionListener(this);
		
		hideLeaves = new JCheckBox("Ukryj liście");
		hideLeaves.addActionListener(this);
		
		add(lod);
		add(view);
		add(hideLeaves);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == lod)
			tree.setLOD(lod.getSelectedIndex());
		if(e.getSource() == view)
			tree.setView(view.getSelectedIndex());
		if(e.getSource() == hideLeaves)
			tree.setHideLeaves(hideLeaves.isSelected());
	}
}
