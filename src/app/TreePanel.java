package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nodes.BranchesNode;
import nodes.LeavesNode;
import nodes.Node;
import nodes.RootsNode;
import nodes.SideBranchesNode;
import nodes.TreeNode;
import nodes.TrunkNode;
import nodes.Node.NodeType;
import tree.Tree;

/**
 * Klasa do obsługi panelu na którym umieszczony jest edytor elemntów drzewa.
 * Pozwala na dodawanie i usuwanie elementów drzewa.
 * 
 * @author Mateusz Jaszewski
 *
 */
public class TreePanel extends JPanel implements TreeSelectionListener {
	private JTree jTree;
	private TreeNode treeNode;
	private ImageIcon treeIcon, trunkIcon, branchesIcon, sideBranchesIcon,rootsIcon, leavesIcon;
	private Color backgroundColor;
	private JPanel buttonPanel,treePanel;
	private JScrollPane scrollPane;
	private Node selectedNode;
	private JButton buttonAdd, buttonDelete; 
	private JPanel rightPanel;
	
	/**
	 * @param rightPanel panel do którego będą dodawane panele z parametrami poszczególnych węzłów drzewa
	 * @param tree drzewo które będzie edytowane
	 */
	public TreePanel(JPanel rightPanel, Tree tree) {
		super();
		
		this.rightPanel = rightPanel;
		
		buttonAdd = new JButton("Dodaj");
		buttonDelete = new JButton("Usuń");
		
		buttonAdd.setEnabled(false);
		buttonDelete.setEnabled(false);
		
		backgroundColor  = new Color(214,217,223);
		
		treeIcon = new ImageIcon(Tree.class.getClass().getResource("/resources/icons/tree.png"));
		trunkIcon = new ImageIcon(Tree.class.getClass().getResource("/resources/icons/trunk.png"));
		branchesIcon = new ImageIcon(Tree.class.getClass().getResource("/resources/icons/branches.png"));
		sideBranchesIcon = new ImageIcon(Tree.class.getClass().getResource("/resources/icons/sideBranches.png"));
		rootsIcon = new ImageIcon(Tree.class.getClass().getResource("/resources/icons/roots.png"));
		leavesIcon = new ImageIcon(Tree.class.getClass().getResource("/resources/icons/leaves.png"));
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		treeNode = tree.getTreeNode();
		addNodePanel(treeNode);
		setVisibleNodePanel(treeNode);
		selectedNode = treeNode;
		
		setVisibleNodePanel(treeNode);
		
		jTree = new JTree(treeNode);
		jTree.putClientProperty("JTree.lineStyle", "Horizontal");
		jTree.setBackground(backgroundColor);
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.addTreeSelectionListener((TreeSelectionListener) this);
		
		jTree.setCellRenderer(new DefaultTreeCellRenderer(){
		    public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {
		    	super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
		    	if(value instanceof TreeNode)
		    		setIcon(treeIcon);
		    	if(value instanceof TrunkNode)
		    		setIcon(trunkIcon);
		    	if(value instanceof BranchesNode)
		    		setIcon(branchesIcon);
		    	if(value instanceof SideBranchesNode)
		    		setIcon(sideBranchesIcon);
		    	if(value instanceof RootsNode)
		    		setIcon(rootsIcon);
		    	if(value instanceof LeavesNode)
		    		setIcon(leavesIcon);
		    	setBackgroundNonSelectionColor(backgroundColor);
		    	return this;
		    }
		});
		
		treePanel = new JPanel();
		treePanel.setLayout(new GridLayout(1,1));
		treePanel.add(jTree);
		
		scrollPane = new JScrollPane(treePanel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Edytor"));
		
		buttonPanel = new JPanel();
		buttonPanel.setMinimumSize(new Dimension(220,25));
		buttonPanel.setMaximumSize(new Dimension(220,25));
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(buttonAdd);
		buttonPanel.add(buttonDelete);
		
		add(scrollPane);
		add(buttonPanel);
		
		buttonAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = selectedNode.getNodeTypesCanBeAdded().toArray();
				NodeType type = (NodeType)JOptionPane.showInputDialog(
				                    null,
				                    "Wybierz element do dodania:",
				                    "Dodaj",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    options,
				                    options[0]);
				
				if(type != null && selectedNode != null){
					Node newNode = null;
					if(type == NodeType.BRANCHES)
						newNode = new BranchesNode("gałęzie");
					if(type == NodeType.SIDE_BRANCHES)
						newNode = new SideBranchesNode("gałęzie boczne");
					if(type == NodeType.LEAVES)
						newNode = new LeavesNode("liście");
					if(type == NodeType.ROOTS)
						newNode = new RootsNode("korzenie");
					if(type == NodeType.TRUNK)
						newNode = new TrunkNode("pień");
					if(newNode != null) {
						selectedNode.add(newNode);
						DefaultTreeModel model;
						model = (DefaultTreeModel) jTree.getModel();
						model.reload(); 
						jTree.makeVisible(new TreePath(newNode.getPath()));
						jTree.setSelectionPath(new TreePath(newNode.getPath()));
						addNodePanel(selectedNode);
						
						Tree.somethingChanged();
					}
				}	
			}
		});
		
		buttonDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int choice = JOptionPane.showConfirmDialog(
						null,
						"Czy napewno chcesz usunąć wybrany element?",
						"Usuwanie", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				
				if(selectedNode != null && choice == 0){
					Node parent = (Node) selectedNode.getParent();
					
					DefaultTreeModel model;
					model = (DefaultTreeModel) jTree.getModel();
					model.removeNodeFromParent(selectedNode);
					removeNodePanel(selectedNode);
					selectedNode = null;

					
					jTree.setSelectionPath(new TreePath(parent.getPath()));
					
					Tree.somethingChanged();
				}
			}
			
		});
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		selectedNode = (Node) e.getPath().getLastPathComponent();
		if(selectedNode != null) {
			if(selectedNode ==  treeNode) {
				buttonAdd.setEnabled(true);
				buttonDelete.setEnabled(false);
			}
			else {
				buttonAdd.setEnabled(true);
				buttonDelete.setEnabled(true);
			}
			
			if(selectedNode.getNodeTypesCanBeAdded().isEmpty()) {
				buttonAdd.setEnabled(false);
			}
			setVisibleNodePanel(selectedNode);
		}
	}
	
	/**
	 * Dodaje do panelu po prawej panel z parametrami danego węzła
	 * @param node węzeł reprezentujący element drzewa
	 */
	private void addNodePanel(Node node){
		rightPanel.add(node.getPanel());
	}
	
	/**
	 * Odświeża panel z parmatrami rozpoczynając od węzła początkowego (treeNode).
	 * Przydatna po wczytaniu lub stworzeniu nowego drzewa.
	 */
	public void refreshRightPanel() {
		rightPanel.removeAll();
		doRefreshRightPanel(treeNode);
	}
	
	/**
	 * Wykonuje rekurencyjne odświerzanie panelu z parametrami dla wszystkich węzłów w drzewie.
	 * @param node węzeł
	 */
	private void doRefreshRightPanel(Node node) {
		Node child;
		addNodePanel(node);
		for(int i=0; i<node.getChildCount(); i++) {
			child = (Node) node.getChildAt(i);
			doRefreshRightPanel(child);
		}
		setVisibleNodePanel(treeNode);
	}
	
	public void refresh() {
		jTree.setSelectionPath(new TreePath(treeNode.getPath()));
		DefaultTreeModel model;
		model = (DefaultTreeModel) jTree.getModel();
		model.reload(); 
		refreshRightPanel();
	}

	/**
	 * Usuwa panel węzła z panelu zawierającego parametry wszystkich węzłów.
	 * @param node węzeł
	 */
	private void removeNodePanel(Node node) {
		setVisibleNodePanel(null);
		rightPanel.remove(node.getPanel());
	}
	
	/**
	 * Ustawia parametry węzła na widoczne na panelu z parametrami.
	 * @param node węzeł którego parametry mają być widoczne.
	 */
	private void setVisibleNodePanel(Node node) {
		Component component;
		for(int i=0; i<rightPanel.getComponentCount(); i++) {
			component = rightPanel.getComponent(i);
			if(node != null && component == node.getPanel())
				component.setVisible(true);
			else
				component.setVisible(false);
		}
		if(node != null)
			rightPanel.setPreferredSize(node.getPanelSize());
		rightPanel.validate();
	}
}
