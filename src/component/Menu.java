package component;

import javax.swing.*;

import polygon.Klotski;


public class Menu extends JMenuBar {

	private static final long serialVersionUID = 8387166164446599804L;
	private String menuName1 = "Game";
	private String menuName2 = "Help";
	private String[] subMenuNames1 = {"New Game","Next Level", "Back Level","Undo","Exit"};
	private String[] subMenuNames2 = {"View Help","About Klotski"};
	
	public Menu() {
		super();
		this.add(makeMenu(menuName1, subMenuNames1));
		this.add(makeMenu(menuName2, subMenuNames2));
	}
  	private JMenu makeMenu(String menuName, String[] subMenuNames) {
		JMenu menu = new JMenu(menuName);
		for (int i = 0;i < subMenuNames.length; i++) {
			menu.add(makeMenuItem(subMenuNames[i]));
		}
		return menu;
    }
  	private JMenuItem makeMenuItem(String menuItemName) {
		JMenuItem submenu = new JMenuItem(menuItemName);
		submenu.addActionListener(new Klotski());
		submenu.setActionCommand(menuItemName);	
		return submenu;
	}

}
