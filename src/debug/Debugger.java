package debug;

import javax.swing.*;

public class Debugger extends JFrame {
	private static final long serialVersionUID = 3603880021224975833L;
	private JTextArea box;
	
	public Debugger() {
		box = new JTextArea(10,100);
		this.add(new JScrollPane(box));
	}
	public void add(String message) {
		box.append(message);
		box.append("\n");
	}
}
