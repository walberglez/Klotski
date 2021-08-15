package component;

import java.awt.*;

import javax.swing.JComponent;

public class Piece extends JComponent {

	private static final long serialVersionUID = 1157893729646114957L;
	/**
	 * Default constructor
	 */
	public Piece(int x, int y) {
		setLocation(x, y);
		setSize(100, 100);
		setBackground(Color.RED);
	}
	/**
	 * @see JComponent.contains
	 */
	@Override
	public boolean contains(int x, int y) {
		return super.contains(x, y);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
}
