package nodes;

import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import app.TreePanel;

public abstract class Node extends DefaultMutableTreeNode{
	
	public enum NodeType {
		TREE,TRUNK,BRANCHES,SIDE_BRANCHES,LEAVES,ROOTS;
		
		public String toString() {
			if(this == TREE)
				return "drzewo";
			if(this == TRUNK)
				return "pień";
			if(this == BRANCHES)
				return "gałęzie";
			if(this == SIDE_BRANCHES)
				return "gałęzie boczne";
			if(this == LEAVES)
				return "liście";
			if(this == ROOTS)
				return "korzenie";
			return "";
		}
	}
	
	protected JPanel panel; 
	public JPanel getPanel() {
		return panel;
	}
	
	public Node(Object object) {
		super(object);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
	}
	
	protected int getChildCount(NodeType type) {
		int count = 0;
		for(int i=0; i<getChildCount(); i++) {
			if(type == NodeType.TREE)
				if(getChildAt(i) instanceof TreeNode)
					count++;
			
			if(type == NodeType.TRUNK)
				if(getChildAt(i) instanceof TrunkNode)
					count++;
			
			if(type == NodeType.BRANCHES)
				if(getChildAt(i) instanceof BranchesNode)
					count++;
			
			if(type == NodeType.SIDE_BRANCHES)
				if(getChildAt(i) instanceof SideBranchesNode)
					count++;
			
			if(type == NodeType.LEAVES)
				if(getChildAt(i) instanceof LeavesNode)
					count++;
			
			if(type == NodeType.ROOTS)
				if(getChildAt(i) instanceof RootsNode)
					count++;
			
		}
		return count;
	}
	
	public Dimension getPanelSize() {
		
		int height=0;
		for(int i=0; i<panel.getComponentCount(); i++) {
			height += panel.getComponent(i).getPreferredSize().getHeight();
		}
		return new Dimension(190,height);
	}
	
	abstract public List<NodeType> getNodeTypesCanBeAdded() ;
	abstract public void save(FileWriter fileWriter) throws IOException;
	abstract public void load(Scanner scanner) throws IOException;
}
