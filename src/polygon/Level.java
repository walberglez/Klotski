package polygon;

import java.awt.Dimension;
import java.awt.Point;
import java.io.*;
import java.util.*;

public class Level {
	private String name;
	private Character[][] board;
	private Dimension boardDimension;
	private Hashtable<Character, ArrayList<Point>> elements;

	private Level() {
		elements = new Hashtable<Character, ArrayList<Point>>(40);
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}
	public Character[][] getBoard() {
		return board;
	}
	public void setBoard(Character[][] board) {
		this.board = board;
	}
	public Dimension getBoardDimension() {
		return boardDimension;
	}
	private void setBoardDimension(Dimension boardDimension) {
		this.boardDimension = boardDimension;
		board = new Character[boardDimension.height][boardDimension.width];
	}
	public Hashtable<Character, ArrayList<Point>> getElements() {
		return elements;
	}
	private void readBoard(BufferedReader reader) throws IOException {
		String line;
		for ( int row = 0; row < boardDimension.height && (line=reader.readLine()) != null; ++row) {
			processLine(line, row);
		}
	}
	private void processLine(String line, int row) {
		for ( int i = 0; i < line.length(); ++i ) {
			board[row][i] = line.charAt( i );
			updateElement(row, i, board[row][i]);
		}
	}
	private void updateElement(int row, int col, Character c) {
		if (elements.containsKey(c) == false) {
			elements.put(c, new ArrayList<Point>());
		}
		elements.get(c).add(new Point(col,row));
	}
	public void write(BufferedWriter writer) throws IOException {
		writeLine(writer, name);
		writeDimension(writer);
		writeBoard(writer);
	}
	public static Level read(BufferedReader reader) throws IOException {
		Level result = new Level();
		result.name = readName( reader );

		if (result.name != null) {
			Dimension dimension = readDimension( reader );
			if (dimension != null) {
				result.setBoardDimension(dimension);
				result.readBoard(reader);
			}
		}
		return result;
	}
	private static String readName(BufferedReader reader) throws IOException {
		return reader.readLine();
	}
	private static Dimension readDimension( BufferedReader reader ) throws IOException {
		String line  = reader.readLine();
		if ( line != null ) {
			String[] tokens = line.split( " " );
			if (tokens != null && tokens.length == 2) {
				return getDimension(tokens[0], tokens[1]);
			}
		}
		return null;
	}
	private static Dimension getDimension(String width, String heigh) {
		return new Dimension(toInteger(width.trim()), toInteger(heigh.trim()));
	}
	private static int toInteger(String value) {
		return Integer.parseInt( value );
	}
	private static void writeLine(BufferedWriter writer, String value) throws IOException {
		writer.write(value);
		writer.write("\n");
	}
	private void writeDimension(BufferedWriter writer) throws IOException {
		writer.write(toString(boardDimension.width));
		writer.write(" ");
		writeLine(writer, toString(boardDimension.height));
	}
	private void writeBoard(BufferedWriter writer) throws IOException {
		for (int i = 0; i < boardDimension.height; ++i) {
			for (int j = 0; j < boardDimension.width && board[i][j] != null; ++j) {
				writer.write(board[i][j]);
			}
			if (i+1 < boardDimension.height) {
				writeLine(writer, "");
			}
		}
	}
	private static String toString(int value) {
		return String.valueOf(value);
	}
}
