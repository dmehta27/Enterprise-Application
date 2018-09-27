package edu.uic.ids.model;

import java.sql.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;


public class DbmsUserBean 
{
	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMetaData;
	private String jdbcDriver;
	private String url;
	private DbAccessInfoBean dBAccessInfoBean;
	private MessageBean messageBean;
	private String query;
	private String message = "";
	private static final String MY_SQL = "MySQL";
	private static final String DB2 = "DB2";
	private static final String ORACLE = "Oracle";
	private static final String ACCESS_DENIED = "28000";
	private static final String INVALID_DB_SCHEMA = "42000";
	private static final String TIMEOUT = "08S01";
	private static final String INVALID_PORT = "08001";
	private int numOfCols = 0;
	private int numOfRows = 0;
	private String tableName;
	private String columnType;
	private List<String> columnNamesSelected;
	private List<String> columnTypeSelected;
	private List<String> tableList;
	private FacesContext context;
	private static final String[] TABLE_TYPES = { "TABLE", "VIEW" };
	
	
	public DbmsUserBean() {}
	
	
	@PostConstruct
	public void init()
	{
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessInfoBean = (DbAccessInfoBean) m.get("dBAccessInfoBean");
		messageBean = (MessageBean) m.get("messageBean");
		messageBean.reset();

	}
	
	public String connectDB() 
	{
		
		messageBean.setRenderErrorMessage(false);
		String dbms = dBAccessInfoBean.getDbms();
		switch (dbms) 
		{
			case MY_SQL:
				jdbcDriver = "com.mysql.jdbc.Driver";
				url = "jdbc:mysql://" + dBAccessInfoBean.getDbmsHost() + ":"+dBAccessInfoBean.getPortInfo()+"/" + dBAccessInfoBean.getDatabaseSchema()
						+ "?&useSSL=false";
				break;
			case DB2:
				jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
				url = "jdbc:db2://" + dBAccessInfoBean.getDbmsHost() + ":50000/" + dBAccessInfoBean.getDatabaseSchema();
				break;
			case ORACLE:
				jdbcDriver = "oracle.jdbc.driver.OracleDriver";
				url = "jdbc:oracle:thin:@" + dBAccessInfoBean.getDbmsHost() + ":1521:" + dBAccessInfoBean.getDatabaseSchema();
				break;
		}
		try 
		{
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, dBAccessInfoBean.getUserName(),
					dBAccessInfoBean.getPassword());
			statement = connection.createStatement();
			databaseMetaData = connection.getMetaData();
			String loginQuery="Select SUBSTRING_INDEX(host,':',1) as 'ip',sysdate() "
					+ "From information_schema.processlist "
					+ "WHERE ID=connection_id();";
			String ip="";
			String timestamp="";
			if(execute(loginQuery).equals("SUCCESS"))
			{
				while(resultSet.next())
				{
					ip=resultSet.getString(1);
					timestamp=resultSet.getString(2);
				}
				System.out.print("\nIP :"+ip+"\nTS :"+timestamp);
				String insertQuery="Insert into f17x321.f17g211_loginlogout values("+ "\""+ip+"\""+" , "+ "\""+timestamp+"\"" +" , "+ "\"Logged_In\")";
				System.out.print("\n"+insertQuery);
				execute(insertQuery);
			}
			return "SUCCESS";
		} 
		catch (ClassNotFoundException ce)
		{
			message = "Database: " + dBAccessInfoBean.getDbms() + " not supported.";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
				
		catch (SQLException se)
		{
			if (se.getSQLState().equals(ACCESS_DENIED))
			{
				message = "Invalid credentials!";
			}
			else if (se.getSQLState().equals(INVALID_DB_SCHEMA))
			{
				message = "Invalid database schema!";
			} 
			else if (se.getSQLState().equals(TIMEOUT)) 
			{
				message = "Check host & port!";
			}
			else if (se.getSQLState().equals(INVALID_PORT)) 
			{
				message = "Invalid port. It must contain only digits!";
			}
			else 
			{
				message = "SQL Exception occurred!\n" + "Error Code: " + se.getErrorCode() + "\n" + "SQL State: "
						+ se.getSQLState() + "\n" + "Message :" + se.getMessage() + "\n\n";
			}
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		} 
		
		
		catch (Exception e)
		{
			message = "Exception occurred: " + e.getMessage();
			close();
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			return "FAIL";

		}

	}	
	public void close() 
	{
		try 
		{
			if (resultSet != null) 
			{

				resultSet.close();
			}
			if (statement != null)
			{

				statement.close();
			}
			if (connection != null)
			{

				connection.close();

			}

		}
		catch (SQLException e) 
		{

			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			
		}
			
	}
	
	public List<String> tableList() 
	{
		tableList = null;
		try 
		{
			if (databaseMetaData != null) 
			{
				resultSet = databaseMetaData.getTables(null, dBAccessInfoBean.getUserName(), null, TABLE_TYPES);
				resultSet.last();
				int numberOfRows = resultSet.getRow();
				tableList = new ArrayList<String>(numberOfRows);
				resultSet.beforeFirst();
				tableName = "";
				if (resultSet != null) 
				{
					while (resultSet.next()) 
					{
						tableName = resultSet.getString("TABLE_NAME");
						tableList.add(tableName);
					}
				}
			}
			System.out.println(tableName);
		} 
		catch (SQLException e)
		{
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
		}

		return tableList;
	}
	

	
	public String execute(String query)
	{
		try
		{
			if (connection != null && statement != null) 
			{
				if (query.toLowerCase().startsWith("select")) 
				{
					resultSet = statement.executeQuery(query);
					if (resultSet != null) 
					{
						resultSetMetaData = resultSet.getMetaData();
						numOfCols = resultSetMetaData.getColumnCount();
						resultSet.last();
						numOfRows = resultSet.getRow();
						resultSet.beforeFirst();
						columnNamesSelected = new ArrayList<String>(numOfCols);
						columnTypeSelected = new ArrayList<String>(numOfCols);
						for (int i = 0; i < numOfCols; i++) 
						{
							columnNamesSelected.add(resultSetMetaData.getColumnName(i + 1));
							columnTypeSelected.add(resultSetMetaData.getColumnTypeName(i+1));
						}

					}

				} 
				else
				{
					// UPDATE,INSERT,DELETE
					statement.executeUpdate(query);

				}

			}
			return "SUCCESS";
		} 
		catch (SQLException e)
		{
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
			e.printStackTrace();
			return "FAIL";
		}
	}

	public List<String> columnList(String tableName)
	{
		List<String> columnList = new ArrayList<String>();
		try 
		{
			if (databaseMetaData != null) 
			{
				resultSet = databaseMetaData.getColumns(null, dBAccessInfoBean.getDatabaseSchema(), tableName, null);

				String columnName = "";
				if (resultSet != null) 
				{
					while (resultSet.next()) 
					{
						columnName = resultSet.getString("COLUMN_NAME");
						columnList.add(columnName);
					}
				}
			}
		} 
		catch (SQLException e) 
		{
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
		}
		return columnList;
	}
	
	public List<String> columnTypeList(String tableName)
	{
		List<String> columnList = new ArrayList<String>();
		try 
		{
			if (databaseMetaData != null) 
			{
				resultSet = databaseMetaData.getColumns(null, dBAccessInfoBean.getDatabaseSchema(), tableName, null);
				if (resultSet != null) 
				{
					while (resultSet.next()) 
					{
						columnType = resultSet.getString("TYPE_NAME");
						if (("INT").equalsIgnoreCase(columnType)||("DOUBLE").equalsIgnoreCase(columnType) || ("DECIMAL").equalsIgnoreCase(columnType)|| ("FLOAT").equalsIgnoreCase(columnType))
							columnList.add(resultSet.getString("COLUMN_NAME"));
					}
				}
			}
		} 
		catch (SQLException e) 
		{
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			messageBean.setErrorMessage(message);
			messageBean.setRenderErrorMessage(true);
		}
		return columnList;
	}
		
	public void generateResult() 
	{
		if (resultSet != null) 
		{
			result = ResultSupport.toResult(resultSet);
		}
	}
	
	
	
	public ResultSetMetaData getResultSetMetaData() 
	{
		return resultSetMetaData;
	}

	
	public Connection getConnection() 
	{
		return connection;
	}
	

	public String getQuery()
	{
		
		return query;
	}
	
	public String getMessage() 
	{
		return message;
	}


	public DatabaseMetaData getDatabaseMetaData() 
	{
		return databaseMetaData;
	}


	
	private Result result;

	public Result getResult() 
	{
		return result;
	}

	public ResultSet getResultSet() 
	{
		return resultSet;
	}
	
	public int getNumOfCols() 
	{
		return numOfCols;
	}

	public int getNumOfRows() 
	{
		return numOfRows;
	}
	
	public List<String> getColumnNamesSelected() 
	{
		return columnNamesSelected;
	}
	
	public List<String> getColumnTypeSelected() 
	{
		return columnTypeSelected;
	}


	public String getColumnType() 
	{
		return columnType;
	}


	public void setColumnType(String columnType) 
	{
		this.columnType = columnType;
	}





	
	
}
