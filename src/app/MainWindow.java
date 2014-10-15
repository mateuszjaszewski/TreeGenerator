package app;

import graphics.Renderer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import tree.Tree;
import math.MyRandom;

/**
 * Klasa reprezentująca główne okno aplikacji
 * 
 * @author Mateusz Jaszewki
 *
 */
public class MainWindow extends JFrame implements ActionListener {

	private Random random;
	private JPanel rightPanel, leftPanel;
	private TreePanel treePanel;
	private ViewPanel viewPanel;
	private InfoPanel infoPanel;
	private JScrollPane scrollPane;
	private Renderer renderer;
	private Tree tree;

	private JMenu fileMenu, treeMenu, infoMenu;
	private JMenuItem newMI, saveMI, saveAsMI, openMI;
	private JMenuItem exportMI, randomMI;
	private JMenuBar menuBar;

	private String path, name;

	public MainWindow() {
		super("Generator Drzew");

		path = "";
		random = new Random();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 600);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		scrollPane = new JScrollPane(rightPanel);

		scrollPane.setMinimumSize(new Dimension(240, 0));
		scrollPane.setPreferredSize(new Dimension(240, 2000));
		scrollPane.setMaximumSize(new Dimension(240, 2000));
		scrollPane.setBorder(BorderFactory.createTitledBorder("Parametry"));

		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

		renderer = new Renderer();
		renderer.getCanvas().setSize(500, 500);

		infoPanel = new InfoPanel();

		tree = new Tree();
		tree.setInfoPanel(infoPanel);
		treePanel = new TreePanel(rightPanel, tree);
		renderer.add(tree);

		viewPanel = new ViewPanel(tree);

		leftPanel.add(viewPanel);
		leftPanel.add(treePanel);
		leftPanel.add(infoPanel);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;

		gbc.fill = GridBagConstraints.VERTICAL;
		add(leftPanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(renderer.getCanvas(), gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;

		gbc.fill = GridBagConstraints.VERTICAL;
		add(scrollPane, gbc);

		createMenuBar();

		setVisible(true);
	}

	/**
	 * Metoda tworzy pasek meni dostępny w głównym oknie
	 */
	private void createMenuBar() {

		menuBar = new JMenuBar();

		fileMenu = new JMenu("Plik");
		treeMenu = new JMenu("Drzewo");
		infoMenu = new JMenu("O Programie");
		infoMenu.addActionListener((ActionListener) this);

		newMI = new JMenuItem("Nowy");
		newMI.addActionListener((ActionListener) this);

		saveMI = new JMenuItem("Zapisz");
		saveMI.addActionListener((ActionListener) this);
		saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		saveAsMI = new JMenuItem("Zapisz jako");
		saveAsMI.addActionListener((ActionListener) this);

		openMI = new JMenuItem("Otwórz");
		openMI.addActionListener((ActionListener) this);

		exportMI = new JMenuItem("Eksportuj");
		exportMI.addActionListener((ActionListener) this);

		randomMI = new JMenuItem("Losuj");
		randomMI.addActionListener((ActionListener) this);
		randomMI.setAccelerator(KeyStroke.getKeyStroke("F5"));

		menuBar.add(fileMenu);
		menuBar.add(treeMenu);
		menuBar.add(infoMenu);

		fileMenu.add(newMI);
		fileMenu.addSeparator();
		fileMenu.add(openMI);
		fileMenu.addSeparator();
		fileMenu.add(saveMI);
		fileMenu.add(saveAsMI);

		treeMenu.add(exportMI);
		treeMenu.addSeparator();
		treeMenu.add(randomMI);

		setJMenuBar(menuBar);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openMI)
			open();
		if (e.getSource() == saveMI)
			save();
		if (e.getSource() == saveAsMI)
			saveAs();
		if (e.getSource() == newMI)
			newTree();
		if (e.getSource() == exportMI)
			export();
		if (e.getSource() == randomMI)
			newRandomSeed();
	}

	/**
	 * Metoda odpowiada za otwieranie pliku zawierającego zapisane drzewo. W
	 * przypadku błędu wyświetla odpowiedni komunikat.
	 */
	private void open() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Pliki generatora drzew", "gd");
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						path = fileChooser.getSelectedFile().getPath();
						tree.load(path);
						treePanel.refresh();
						name = fileChooser.getSelectedFile().getName();
						setTitle("Generator Drzew - " + name);
					} catch (NoSuchElementException e) {
						JOptionPane.showMessageDialog(null, "Błąd",
								"Nie poprawny plik !",
								JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Błąd",
								"Nie poprawny plik !",
								JOptionPane.ERROR_MESSAGE);
						newTree();
					}
				}
			}
		});
		t.start();
	}

	/**
	 * Metoda wykonuje operacje "Zapisz jako" - wyświetla okno wyboru pliku,
	 * następnie uruchamia zapis drzewa we wskazanym miejscu.
	 */
	private void saveAs() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Pliki generatora drzew", "gd");
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						path = fileChooser.getSelectedFile().getPath();
						if (path.length() < 3
								|| !path.substring(path.length() - 3).equals(
										".gd"))
							path = path + ".gd";
						tree.save(path);
						name = fileChooser.getSelectedFile().getName();
						if (name.length() < 3
								|| !name.substring(name.length() - 3).equals(
										".gd"))
							name = name + ".gd";
						setTitle("Generator Drzew - " + name);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Błąd",
								"Nie poprawny plik !",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		t.start();
	}

	/**
	 * Metoda wykonuje operację "Zapisz" - zapisuje drzewo pod ustaloną już
	 * wcześniej ścieżką, jeżeli nie ma jeszcze scieżki uruchamia metodę
	 * saveAs().
	 */
	private void save() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (!path.equals("")) {
					try {
						tree.save(path);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Błąd",
								"Błąd podczas zapisu !",
								JOptionPane.ERROR_MESSAGE);
					}
				} else
					saveAs();
			}
		});
		t.start();
	}

	/**
	 * Przywraca stan początkowy drzewa.
	 */
	private void newTree() {
		path = "";
		tree.newTree();
		treePanel.refresh();
		setTitle("Generator Drzew");
	}

	/**
	 * Losuje wartość ziarna dla drzewa.
	 */
	private void newRandomSeed() {
		MyRandom.setGlobalSeed(random.nextInt());
		Tree.somethingChanged();
	}

	private void export() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						tree.export(fileChooser.getSelectedFile().getPath(),
								fileChooser.getSelectedFile().getName());
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Błąd",
								"Błąd podczas eksportowania !",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		t.start();
	}

	public static void main(String args[]) {

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new MainWindow();
	}
}
