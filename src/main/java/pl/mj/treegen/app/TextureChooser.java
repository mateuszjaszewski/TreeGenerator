package pl.mj.treegen.app;

import pl.mj.treegen.graphics.Material;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasa pozwala na wybieranie pliku graficznego jako tekstury dla podanego
 * materiału. Dodatkowo wyświetla wczytany obrazek.
 * 
 * @author Mateusz Jaszewski
 *
 */
public class TextureChooser extends JPanel implements ActionListener {
	private BufferedImage image;
	private JButton button;
	private JLabel label;
	private String path;
	private JPanel imagePanel, bottomPanel;
	private Material material;

	private final int IMAGE_HEIGHT = 165;
	private final int IMAGE_WIDTH = 165;
	private final int BOTTOM_HEIGHT = 35;

	/**
	 * @param name
	 *            nazwa
	 * @param material
	 *            materiał którego tekstura będzie wczytywana
	 */
	public TextureChooser(String name, Material material) {
		this.material = material;

		path = "";

		setBorder(BorderFactory.createTitledBorder(name));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		label = new JLabel();

		button = new JButton("Otwórz");
		button.addActionListener(this);

		imagePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (image == null) {
					g.setColor(Color.RED);
					g.drawLine(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
					g.drawLine(0, IMAGE_HEIGHT, IMAGE_WIDTH, 0);
				} else {
					for (int i = 0; i < IMAGE_WIDTH; i += 5) {
						for (int j = 0; j < IMAGE_HEIGHT; j += 5) {
							if ((i + j) % 2 == 0)
								g.setColor(Color.WHITE);
							else
								g.setColor(Color.GRAY);
							g.fillRect(i, j, 5, 5);
						}
					}
					g.drawImage(image, 0, 0, IMAGE_WIDTH - 1, IMAGE_HEIGHT - 1,
							null);
				}
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, IMAGE_WIDTH - 1, IMAGE_HEIGHT - 1);
			}
		};

		imagePanel.setMinimumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
		imagePanel.setMaximumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

		bottomPanel = new JPanel();
		bottomPanel.setMinimumSize(new Dimension(IMAGE_WIDTH, BOTTOM_HEIGHT));
		bottomPanel.setPreferredSize(new Dimension(IMAGE_WIDTH, BOTTOM_HEIGHT));
		bottomPanel.setMaximumSize(new Dimension(IMAGE_WIDTH, BOTTOM_HEIGHT));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));

		bottomPanel.add(button);
		bottomPanel.add(label);

		add(imagePanel);
		add(bottomPanel);

	}

	public void actionPerformed(ActionEvent arg0) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Pliki graficzne", "jpg", "gif", "png", "bmp");
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						image = ImageIO.read(fileChooser.getSelectedFile());
						path = fileChooser.getSelectedFile().getPath();
						label.setText("  "
								+ fileChooser.getSelectedFile().getName());
						imagePanel.setToolTipText(path);
						imagePanel.repaint();
						material.loadTexture(path);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Błąd",
								"Błąd wczytywania pliku graficznego !",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}

		});
		t.start();
	}

	/**
	 * Wczytuje ponownie obrazek.
	 * 
	 * @param str
	 *            scieżka do obrazka
	 */
	public void refresh(final String str) {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					path = str;
					File img = new File(path);
					image = ImageIO.read(img);
					label.setText("  " + img.getName());
					imagePanel.setToolTipText(path);
					imagePanel.repaint();
					material.loadTexture(path);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Błąd",
							"Błąd wczytywania pliku graficznego !",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		t.start();
	}
}
