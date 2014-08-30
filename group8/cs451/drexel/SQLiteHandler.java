/**
 * SQLHandler.java: handles all sql transactions
 * 
 * This file is part of FlashcardApp
 * 
 * Contributors:
 * Jon Hopkins
 * Jesse Kuehn
 * Rishir Patel
 * Sanjana Raj
 */

package group8.cs451.drexel;

import java.io.File;
import java.util.ArrayList;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteConstants;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class SQLiteHandler {
	private SQLiteConnection _db;
	private String _delimiter = "[,]";
	
	/**
	 * Creates a connection to the given file and return a handler object<br>
	 * If the database does not exist yet, it is created
	 * 
	 * @param inputFile The path to the SQLite database file, or null for an in-memory database
	 * @throws SQLiteException
	 */
	public SQLiteHandler(String inputFile) throws SQLiteException {
		if (null == inputFile || inputFile.trim().length() == 0) {
			_db = new SQLiteConnection(null);
		} else {
			_db = new SQLiteConnection(new File(inputFile));
		}
		_db.open(true);
	}
	
	/**
	 * Close the connection to the database
	 */
	public void close() {
		_db.dispose();
	}
	
	/**
	 * Select a single value from a table
	 * 
	 * 
	 * @param query The parameterized SELECT statement to execute
	 * @param args Comma-separated parameters for the select
	 * @return The value selected
	 * @throws SQLiteException
	 */
	public String selectSingle(String query, String args) throws SQLiteException {
		SQLiteStatement st = _db.prepare(query);
		String[] parameters = args.split(_delimiter);
		String value = null;
		
		try {
			for (int i = 0; i < parameters.length; i++) {
				st.bind(i + 1, parameters[i]);
			}
			int columnsCount = st.columnCount();
			if (columnsCount > 1) {
				System.err.println("Error: More than a single column!");
				return null;
			}
			
			if (st.step()) {
				int type = st.columnType(0);
				
				switch (type) {
				case SQLiteConstants.SQLITE_INTEGER:
					value = String.valueOf(st.columnInt(0));
					
				case SQLiteConstants.SQLITE_TEXT:
					value = st.columnString(0);
					
				default:
					break;
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			st.dispose();
		}
		
		return value;
	}
	
	/**
	 * Select values from a single column in a table
	 * <br><br>
	 * Example call:<br>
	 * sqliteHandler.selectColumn("Table1", "Column2", "Column1 = ? AND Column3 = ?", "Value,2");
	 * 
	 * @param table - The table to select from
	 * @param column - The column to select
	 * @param where - The parameterized WHERE clause of the select
	 * @param args - Comma-separated parameters of the WHERE clause
	 * @return An arraylist of selected values
	 * @throws SQLiteException
	 */
	public ArrayList<String> selectColumn(String table, String column, String where, String args) throws SQLiteException {
		String query = "SELECT " + column + " FROM " + table;
		if (where.trim().length() > 0) {
			query += " WHERE " + where;
		}
		
		SQLiteStatement st = _db.prepare(query);
		String[] parameters;
		
		if (null != args && !args.isEmpty()) {
			parameters = args.split(_delimiter);
			for (int i = 0; i < parameters.length; i++) {
				st.bind(i + 1, parameters[i]);
			}
		}
		
		ArrayList<String> list = new ArrayList<String>();
		try {
			int columnsCount = st.columnCount();
			if (columnsCount > 1) {
				System.err.println("Error: More than a single column!");
				return null;
			}
			while (st.step()) {
				int type = st.columnType(0);
				
				switch (type) {
				case SQLiteConstants.SQLITE_INTEGER:
					list.add(String.valueOf(st.columnInt(0)));
					break;
					
				case SQLiteConstants.SQLITE_TEXT:
					list.add(st.columnString(0));
					break;
					
				default:
					break;
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			st.dispose();
		}
		
		return list;
	}
	
	/**
	 * Selects any number of columns and rows from a table
	 * 
	 * @param table The table to select from
	 * @param columns The columns to select from, or * for all columns
	 * @param where The parameterized WHERE clause for the select
	 * @param args Comma-separated parameters of the WHERE clause
	 * @return An arraylist of arraylists representing the returned rows
	 * @throws SQLiteException
	 */
	public ArrayList<ArrayList<String>> select(String table, String columns, String where, String args) throws SQLiteException {
		String query = "SELECT " + columns + " FROM " + table;
		if (where.trim().length() > 0) {
			query += " WHERE " + where;
		}
		
		SQLiteStatement st = _db.prepare(query);
		String[] parameters;
		
		if (null != args && !args.isEmpty()) {
			parameters = args.split(_delimiter);
			for (int i = 0; i < parameters.length; i++) {
				st.bind(i + 1, parameters[i]);
			}
		}
		
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		try {
			int columnsCount = st.columnCount();
			while (st.step()) {
				ArrayList<String> row = new ArrayList<String>();
				for (int i = 0; i < columnsCount; i++) {
					int type = st.columnType(i);
					
					switch (type) {
					case SQLiteConstants.SQLITE_INTEGER:
						row.add(String.valueOf(st.columnInt(i)));
						break;
						
					case SQLiteConstants.SQLITE_TEXT:
						row.add(st.columnString(i));
						break;
						
					default:
						break;
					}
				}
				list.add(row);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			st.dispose();
		}
		
		return list;
	}
	
	/**
	 * Update values in a table
	 * <br><br>
	 * Example call:<br>
	 * sqliteHandler.update("Table1", "Column2", "Value_2", "Column2 = ?", "Value2");
	 * 
	 * @param table The table being updated
	 * @param columns Comma-separated list of columns being updated
	 * @param values Comma-separated list of values, matched up to the names in `columns` 
	 * @param where The parameterized WHERE clause of the update
	 * @param args Comma-separated parameters of the WHERE clause
	 * @return false if an exception occurs or if columns or values is empty, otherwise true
	 * @throws SQLiteException
	 */
	public boolean update(String table, String columns, String values, String where, String args) throws SQLiteException {
		String[] cols = columns.split(_delimiter);
		String[] vals = values.split(_delimiter);
		String[] parameters = args.split(_delimiter);
		
		if (cols.length == 0 || vals.length == 0) {
			return false;
		}
		
		String query = "UPDATE " + table + " SET " + cols[0] + " = ?";
		for (int i = 1; i < cols.length; i++) {
			query += ", " + cols[i] + " = ?";
		}
		if (where.trim().length() > 0) {
			query += " WHERE " + where;
		}
		
		SQLiteStatement st = _db.prepare(query);
		boolean success = true;
		
		try {
			for (int i = 0; i < vals.length; i++) {
				st.bind(i + 1, vals[i]);
			}
			for (int i = 0; i < parameters.length; i++) {
				st.bind(i + vals.length + 1, parameters[i]);
			}
			st.step();
		} catch (SQLiteException e) {
			System.err.println(e.getMessage());
			success = false;
		} finally {
			st.dispose();
		}
		
		return success;
	}
	
	/**
	 * Deletes row(s) from a table
	 * 
	 * @param table The table to delete from
	 * @param where The parameterized WHERE clause for the delete
	 * @param args Comma-separated parameters for the WHERE clause
	 * @return
	 * @throws SQLiteException
	 */
	public boolean delete(String table, String where, String args) throws SQLiteException {
		String[] parameters = args.split(_delimiter);
		
		String query = "DELETE FROM " + table;
		
		if (where.trim().length() > 0) {
			query += " WHERE " + where;
		}
		
		SQLiteStatement st = _db.prepare(query);
		boolean success = true;
		
		try {
			for (int i = 0; i < parameters.length; i++) {
				st.bind(i + 1, parameters[i]);
			}
			st.step();
		} catch (SQLiteException e) {
			System.err.println(e.getMessage());
			success = false;
		} finally {
			st.dispose();
		}
		
		return success;
	}
	
	/**
	 * Drops the given table if it exists
	 * 
	 * @param tableName The name of the table to drop
	 * @return false if an exception occurs, true otherwise
	 * @throws SQLiteException
	 */
	public boolean dropTable(String tableName) throws SQLiteException {
		String query = "DROP TABLE IF EXISTS " + tableName;
		SQLiteStatement st = _db.prepare(query);
		boolean success = true;
		
		try {
			st.step();
		} catch (SQLiteException e) {
			System.err.println(e.getMessage());
			success = false;
		} finally {
			st.dispose();
		}
		
		return success;
	}
	
	/**
	 * Creates the given table if it does not exist
	 * <br><br>
	 * Example call:<br>
	 * sqliteHandler.createTable("Table1", "Column1,Column2", "INTEGER PRIMARY KEY ASC,CHAR(25)");
	 * 
	 * @param tableName The name of the table to create
	 * @param columns Comma-separated column names
	 * @param constraints Comma-separated datatypes and constraints, matched up to the names in `columns`
	 * @return false if an exception occurs, true otherwise
	 * @throws SQLiteException
	 */
	public boolean createTable(String tableName, String columns, String constraints) throws SQLiteException {
		String[] cols = columns.split(_delimiter);
		String[] cons = constraints.split(_delimiter);
		
		if (cols.length == 0) {
			System.err.println("createTable: must have at least one column");
		}
		
		if (cols.length != cons.length) {
			System.err.println("createTable: number of constraints must match number of columns");
			return false;
		}
		
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + cols[0] + " " + cons[0];
		for (int i = 1; i < cols.length; i++) {
			query += ", " + cols[i] + " " + cons[i];
		}
		query += ")";
		SQLiteStatement st = _db.prepare(query);
		boolean success = true;
		
		try {
			st.step();
		} catch (SQLiteException e) {
			System.err.println(e.getMessage());
			success = false;
		} finally {
			st.dispose();
		}
		
		return success;
	}
	
	/**
	 * Insert values into a table
	 * <br><br>
	 * Example call:<br>
	 * sqliteHandler.insert("Table1", "Column1,Column3", "Value1,9");
	 * 
	 * @param tableName The table to insert into
	 * @param columns Comma-separated list of columns to receive values
	 * @param values Comma-separated list of values, matched up to the names in `columns`
	 * @return false if an exception occurs or if values is empty, true otherwise
	 * @throws SQLiteException
	 */
	public boolean insert(String tableName, String columns, String values) throws SQLiteException {
		String[] parameters = values.split(_delimiter);
		boolean success = true;
		
		if (parameters.length == 0) {
			return false;
		}
		
		String query = "INSERT INTO " + tableName + "(" + columns + ") VALUES (?";
		for (int i = 1; i < parameters.length; i++) {
			query += ", ?";
		}
		query += ")";
		
		SQLiteStatement st = _db.prepare(query);
		
		try {
			for (int i = 0; i < parameters.length; i++) {
				st.bind(i + 1, parameters[i]);
			}
			st.step();
		} catch (SQLiteException e) {
			System.err.println(e.getMessage());
			success = false;
		} finally {
			st.dispose();
		}
		
		return success;
	}
}