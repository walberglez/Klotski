package component;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 5649561839878135826L;
	private static Color backgroundColor = Color.WHITE;
	private static int width = 50;
	private static int height = 50;
	private Hashtable<Character, Polygon> polygons;
	private Character lastPieceMoved;
	private Point lastPosition;
	private Character pieceMoved;
//	private Stack<Point> undoMove;
	private JLabel levelNameCounter;
	private Point pressed;
	private Point delta;
	private Level l;
	private int moves;
	private boolean firstTime;
	private boolean endGame;
	
	public Panel(Level l, int moves){
		super();

		this.l = l;
		this.moves = moves;
//		this.undoMove = new Stack<Point>();
		this.polygons = new Hashtable<Character, Polygon>(40);
		levelNameCounter = new JLabel(" ");
		this.firstTime = true;
		endGame = false;
		
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);
		this.setSize(l.getBoardDimension().width*width + 100,
					l.getBoardDimension().height*height + 100);
		this.addBoard();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	public int getMoves() {
		return moves;
	}
	public boolean getEndGame () {
		return endGame;		
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (pressed == null) {
			pressed = toCoordIJ(e.getPoint());
			if ((pressed.x < l.getBoardDimension().width)
					&& (pressed.y < l.getBoardDimension().height)
					&& (l.getBoard()[pressed.y][pressed.x] != null)
					&& (l.getBoard()[pressed.y][pressed.x] != ' ')
					&& (l.getBoard()[pressed.y][pressed.x] != '#')
					&& (l.getBoard()[pressed.y][pressed.x] != '.')
					&& (l.getBoard()[pressed.y][pressed.x] != '-')) {
				pieceMoved = l.getBoard()[pressed.y][pressed.x];
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = null;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (pressed != null) {
			Point pos = toCoordIJ(e.getPoint());
			delta = new Point((pos.x - pressed.x) % 2, (pos.y - pressed.y) % 2);
			updateLocation();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Object[] key = polygons.keySet().toArray();

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			for (int i = 0; i < key.length; i++) {
				if ((!endGame) && (pieceMoved != null) && (pressed != null) && (delta != null) 
						&& (delta.x+delta.y != 0) && (key[i] == pieceMoved)) {
					// Clear last position
					g2.setColor(backgroundColor);
					g2.fillPolygon(polygons.get(key[i]));
					// Set new polygon position
					polygons.get(key[i]).translate(delta.x*width,delta.y*height);
					isEndGame();
					delta = null;
					pressed = null;
				}
				g2.setColor(fillColor((Character) key[i]));
				g2.fillPolygon(polygons.get(key[i]));
				g2.setColor(Color.BLACK);
				g2.drawPolygon(polygons.get(key[i]));
			}
			this.addLevelNameCounter();
		}
	}
	private void isEndGame() {
		if (l.getElements().get('.').containsAll(l.getElements().get('*'))) {
			endGame = true;
		}
	}
	private void addLevelNameCounter() {
		levelNameCounter.setFont(new Font(null, 30, 20));
		levelNameCounter.setBackground(Color.GRAY);
		this.levelNameCounter.setText("        " + l.getName() 
				+ "               Moves: " + moves);
		this.add("South",levelNameCounter);
	}
	private void addBoard() {
		Hashtable<Character, ArrayList<Point>> elements = l.getElements();
		Object[] key = elements.keySet().toArray();
				
		for (int i = 0; i < key.length; i++) {
			if ((Character) key[i] != ' ') {
				polygons.put((Character)key[i], addPiece(elements.get(key[i])));
			}
		}
	}
	private Color fillColor(Character c) {
		switch (c) {
		case '*':
			return Color.RED;
		case '#':
			return Color.DARK_GRAY;
		case '.':
			return Color.GREEN;
		case '-':
			return Color.YELLOW;	
		default:
			return Color.LIGHT_GRAY;
		}
	}
	private Polygon addPiece(ArrayList<Point> list) {
		Polygon rect = new Polygon();
		ArrayList<Point> sortList = sortPoints(selectPoints(list), allPoints(list));

		for (int i = 0; i < sortList.size(); i++) {
			rect.addPoint(sortList.get(i).x*width, sortList.get(i).y*height);
		}
		return rect;
	}
	private ArrayList<Point> selectPoints(ArrayList<Point> list) {
		ArrayList<Point> pointSelected = new ArrayList<Point>();
		ArrayList<Point> allPoints = allPoints(list);
		ArrayList<Point> aux;
		Point pAux;
		int count;

		while (!allPoints.isEmpty()) {
			pAux = allPoints.get(0);
			aux = new ArrayList<Point>();
			count = 0;
			for(int j = 0; j < allPoints.size(); j++) {
				if (allPoints.get(j).equals(pAux)) {
					count++;
				}
				else {
					aux.add(allPoints.get(j));
				}
			}
			allPoints = aux;
			if ((count % 2) != 0) {
				pointSelected.add(pAux);
			}
		}
		return pointSelected;
	}
	private ArrayList<Point> allPoints(ArrayList<Point> list) {
		ArrayList<Point> allPoints = new ArrayList<Point>();
		Point pAux;
		
		for (int i = 0; i < list.size(); i++) {
			pAux = list.get(i);
			allPoints.add(new Point(pAux.x, pAux.y));
			allPoints.add(new Point(pAux.x + 1, pAux.y));
			allPoints.add(new Point(pAux.x + 1, pAux.y + 1));
			allPoints.add(new Point(pAux.x, pAux.y + 1));
		}
		return allPoints;
	}
	private ArrayList<Point> sortPoints(ArrayList<Point> selected, ArrayList<Point> all) {
		ArrayList<Point> sorted = new ArrayList<Point>();
		ArrayList<Point> points;
		Point p = new Point();
		int state = 1; // 0 :: rigth, left; 1 :: down,top;
		Point currentPoint = selected.get(0);
		sorted.add(currentPoint);
		selected.remove(currentPoint);

		while (selected.size() > 1) {
			points = new ArrayList<Point>();
			switch (state) {
			case 0:	// RIGTH, LEFT
				state = 1;
				if (all.contains(new Point(currentPoint.x,currentPoint.y - 1))) {
					// go to TOP
					for (int i = 0; i < selected.size(); i++) {
						if ((selected.get(i).x == currentPoint.x) 
								&& (selected.get(i).y < currentPoint.y)) {
							points.add(selected.get(i));
						}
					}
					if (!points.isEmpty()) {
						p = points.get(0);
						for (int i = 0; i < points.size(); i++) {
							if (points.get(i).y > p.y) {
								p = points.get(i);
							}
						}
					}
				}
				if ((points.isEmpty()) && (all.contains(new Point(currentPoint.x,currentPoint.y + 1)))) {
					// go to DOWN
					for (int i = 0; i < selected.size(); i++) {
						if ((selected.get(i).x == currentPoint.x) 
								&& (selected.get(i).y > currentPoint.y)) {
							points.add(selected.get(i));
						}
					}
					if (!points.isEmpty()) {
						p = points.get(0);
						for (int i = 0; i < points.size(); i++) {
							if (points.get(i).y < p.y) {
								p = points.get(i);
							}
						}
					}
				}
				break;
			case 1:	// DOWN, TOP
				if (all.contains(new Point(currentPoint.x + 1,currentPoint.y))) {
					state = 0;// go to RIGTH
					for (int i = 0; i < selected.size(); i++) {
						if ((selected.get(i).y == currentPoint.y) 
								&& (selected.get(i).x > currentPoint.x)) {
							points.add(selected.get(i));
						}
					}
					if (!points.isEmpty()) {
						p = points.get(0);
						for (int i = 0; i < points.size(); i++) {
							if (points.get(i).x < p.x) {
								p = points.get(i);
							}
						}
					}
				}
				if ((points.isEmpty()) && (all.contains(new Point(currentPoint.x - 1,currentPoint.y)))) {
					state = 0;// go to LEFT
					for (int i = 0; i < selected.size(); i++) {
						if ((selected.get(i).y == currentPoint.y) 
								&& (selected.get(i).x < currentPoint.x)) {
							points.add(selected.get(i));
						}
					}
					if (!points.isEmpty()) {
						p = points.get(0);
						for (int i = 0; i < points.size(); i++) {
							if (points.get(i).x > p.x) {
								p = points.get(i);
							}
						}
					}
				}
				break;
			}
			currentPoint = p;
			sorted.add(currentPoint);
			selected.remove(currentPoint);
		}
		sorted.add(selected.get(0));
		return sorted;
	}
	private static Point toCoordIJ(Point p) {
		return new Point((p.x / width),(p.y / height));
	}
	
	private void updateLocation() {
		if (isEmptyDelta()) {
			if (firstTime || (!lastPieceMoved.equals(pieceMoved))) {
				lastPosition = pressed;
				lastPieceMoved = pieceMoved;
				moves++;
				firstTime = false;
			}
			else if (l.getElements().get(pieceMoved).contains(lastPosition)) {
				moves--;
				firstTime = true;
			}
			repaint();	
		}
	}
	private boolean isEmptyDelta() {
		if (delta == null || (delta.x+delta.y) == 0) {
			return false;
		}

		int col, row;
		Character next;
		for (Point point : l.getElements().get(pieceMoved)) {
			col = point.x + delta.x;
			row = point.y + delta.y;
			next = l.getBoard()[row][col];
			
			if (next != null && pieceMoved.equals(next) == false &&	next.equals(' ') == false) {
				if (pieceMoved.equals('*')) {
					if (next.equals('-') == false && next.equals('.') == false) {
						return false;
					}
				}
				else {
					return false;
				}
			}
		}
		updateBoard();
		return true;
	}
	
	private void updateBoard() {
		ArrayList<Point> newPoints = new ArrayList<Point>();

		for (Point point : l.getElements().get(pieceMoved)) {
			l.getBoard()[point.y + delta.y][point.x + delta.x] = pieceMoved;
			newPoints.add(new Point(point.x + delta.x, point.y + delta.y));
		}
		for (Point point : l.getElements().get(pieceMoved)) {
			if (newPoints.contains(point) == false) {
				l.getBoard()[point.y][point.x] = ' ';
			}
		}
		l.getElements().remove(pieceMoved);
		l.getElements().put(pieceMoved, newPoints);
	}
}