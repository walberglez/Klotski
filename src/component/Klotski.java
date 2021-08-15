package component;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Klotski implements ActionListener {
	private static JFrame frame;
	private static Panel panel;
	private static Level l;
	private static String frameTitle = "Klotski";
	private static String PATH = "E:\\Walber\\Dropbox\\INFORMATICA\\workspace\\Klotski\\level\\";
	private static String lastGame = "lastGame.txt";
	private static String firstLevel = "nivel_0.txt";
	private static String currentLevelName;
	private static Integer moves = 0;
	
	public static void main(String[] args) throws IOException {
		if (args != null && args.length > 0) {
			frameTitle = args[0];
		}
		try {
			l = read(PATH + lastGame);
			int from = l.getName().indexOf("nivel_");
			int to = l.getName().indexOf(".txt");
			currentLevelName = l.getName().substring(from, to + 4);
			moves = Integer.parseInt(l.getName().substring(to + 4));
			l.setName(l.getName().substring(0, from));
		} catch (Exception e) {
			l = read(PATH + firstLevel);
			currentLevelName = firstLevel;
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		try {
			if ("New Game".equals(command)) {
				l = read(PATH + currentLevelName);
				frame.setContentPane(new Panel(l, 0));
				frame.setSize(frame.getContentPane().getSize());
			}
			else if ("Exit".equals(command)) {
				if (panel.getEndGame()) {
					l.setName(l.getName() + currentLevelName + panel.getMoves());
					write(PATH + lastGame, l);
					System.exit(0);
				}
			}
			else if ("Next Level".equals(command)) {
				actionNextLevel(event);
			}
			else if ("Back Level".equals(command)) {
				actionBackLevel(event);
			}
			else if ("Undo".equals(command)) {
				actionUndo(event);
			}
			else if ("View Help".equals(command)) {
				actionViewHelp(event);
			}
			else if ("About Klotski".equals(command)) {
				actionAboutKlotski(event);
			}
		} catch (Exception e) {
			System.out.println("ERROR: tratar accion");
			e.printStackTrace();
		}
	}
	private static void actionBackLevel(ActionEvent event) throws IOException {
		if (levelNumber() > 0) {
			currentLevelName = "nivel_" + (levelNumber()-1) + ".txt";
			l = read(PATH + currentLevelName);
			frame.setContentPane(new Panel(l, 0));
		}
		else {
			JLabel fin = new JLabel("First Level!!!");
			fin.setFont(new Font(null, 30, 20));
			frame.getContentPane().add("South",fin);
		}
		frame.setSize(frame.getContentPane().getSize());
	}
	private static void actionNextLevel(ActionEvent event) {
		currentLevelName = "nivel_" + (levelNumber()+1) + ".txt";
		try {
		l = read(PATH + currentLevelName);
		frame.setContentPane(new Panel(l, 0));
		} catch (Exception e) {
			currentLevelName = "nivel_" + (levelNumber()-1) + ".txt";
			JLabel fin = new JLabel("No More Levels!!!");
			fin.setFont(new Font(null, 30, 20));
			frame.getContentPane().add("South",fin);
		} finally {
			frame.setSize(frame.getContentPane().getSize());
		}
	}
	private static void actionUndo(ActionEvent event) {
		
	}
	private static void actionViewHelp(ActionEvent event) {
		JFrame f = new JFrame("Help");
		JTextArea output = new JTextArea(5, 30);
		output.append("Klotski:\nColocar la pieza principal" +
				" en la posicion objetivo.");
		output.setEditable(false);
		JPanel p = new JPanel(new BorderLayout());
		p.add(output);
		p.setOpaque(true);
		f.setContentPane(p);
		f.pack();
		f.setVisible(true);
	}
	private static void actionAboutKlotski(ActionEvent event) {
		JFrame f = new JFrame("About Klotski");
		JTextArea output = new JTextArea(5, 30);
		output.append("Autor: Walber Gonzalez\n");
		output.append("Universidad Politecnica de Madrid\n");
		output.append("Version: 1.0\n");
		output.append("Julio 2009");
		output.setEditable(false);
		JPanel p = new JPanel(new BorderLayout());
		p.add(output);
		p.setOpaque(true);
		f.setContentPane(p);
		f.pack();
		f.setVisible(true);
	}
	private static Integer levelNumber() {
		String nextLevelName = currentLevelName.substring
		(currentLevelName.indexOf('_')+1,currentLevelName.indexOf('.'));
		return Integer.parseInt(nextLevelName);
	}
	private static Level read(String path) throws IOException {
		FileReader file = new FileReader(path);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			try {
				return Level.read(reader);
			} finally {
				reader.close();
			}
		} finally {
			file.close();
		}
	}
	private static void write(String path, Level level) throws IOException {
		FileWriter file = new FileWriter(path);
		
		try {
			BufferedWriter writer = new BufferedWriter(file);
			
			try {
				level.write(writer);
			} finally {
				writer.close();
			}
		} finally {
			file.close();
		}
	}
	private static void createGUI() {
		frame = new JFrame(frameTitle);
		JMenuBar menu = new Menu();
		panel = new Panel(l, moves);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menu);
		frame.setContentPane(panel);
		frame.setSize(panel.getSize());
		frame.setVisible(true);
	}
/*
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.getContentPane().setLayout(null);
		f.getContentPane().add(new Piece(100, 100));
		f.getContentPane().add(new Piece(110, 110));
		f.getContentPane().add(new Piece(310, 310));
		f.setVisible( true );
	}
}
*/
}
