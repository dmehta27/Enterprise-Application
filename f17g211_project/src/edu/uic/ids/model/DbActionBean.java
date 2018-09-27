package edu.uic.ids.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;

public class DbActionBean 
{
	private FacesContext context;
	private DbmsUserBean dbmsUserBean;
	private MessageBean messageBean;
	private List<String> tableViewList;
	private List<String> columnNamesSelected;
	private List<String> columnNames;
	private List<String> columnType;
	private boolean tableListRendered;
	private boolean columnListRendered = false;
	private boolean columnTypeListRendered = false;
	private boolean queryRendered;
	private String tableName;
	private String tableinList;
	private String sqlQuery;
	private int noOfCols = 0;
	private int noOfRows = 0;
	private Result result;
	
	List<Double> values;
	private String columnName;
	public DbActionBean() 
	{
		
	}
	
	@PostConstruct
	public void init() 
	{
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbmsUserBean = (DbmsUserBean) m.get("dbmsUserBean");
		messageBean = (MessageBean) m.get("messageBean");
		
		//listTables();

	}
	
	public String listTables() 
	{
		try
		{
			messageBean.reset();
			columnNamesSelected=null;
			columnNames=null;
			columnListRendered=false;
			tableViewList = dbmsUserBean.tableList();
			if (null != tableViewList) 
			{
				tableListRendered = true;
			}
			return "SUCCESS";
		} 
		catch (Exception e) 
		{
			tableListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}
	
	public String listColumns() 
	{

		try 
		{
			messageBean.reset();
			if (null != tableName && !"".equals(tableName)) 
			{
				columnNames = dbmsUserBean.columnList(tableName);
				tableinList = tableName;
				queryRendered = false;
				sqlQuery = "";
				if (null != columnNames) 
				{
					columnListRendered = true;
					columnTypeListRendered= true;
				}
			} 
			else 
			{
				messageBean.setErrorMessage("Please select Table Name from the list");
				messageBean.setRenderErrorMessage(true);
				return "FAIL";

			}
		} 
		catch (Exception e) 
		{
			columnListRendered = false;
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}

		return "SUCCESS";

	}
	
	public String listColumnType() 
	{

		try 
		{
			messageBean.reset();
			if (null != tableName && !"".equals(tableName)) 
			{
				columnNames = dbmsUserBean.columnTypeList(tableName);
				tableinList = tableName;
				queryRendered = false;
				sqlQuery = "";
				if (null != columnNames) 
				{
					columnListRendered = true;
					columnTypeListRendered= true;
				}
			} 
			else 
			{
				messageBean.setErrorMessage("Please select Table Name from the list");
				messageBean.setRenderErrorMessage(true);
				return "FAIL";

			}
		} 
		catch (Exception e) 
		{
			columnListRendered = false;
			messageBean.setErrorMessage("");
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}

		return "SUCCESS";

	}
	
	
	
	
	public String selectAllColumn() 
	{
		messageBean.reset();
		listColumns();
		if (null != tableinList && !"".equals(tableinList)) 
		{
			columnNamesSelected=null;
			sqlQuery = "select * from " + tableinList + " ;";
			dbmsUserBean.execute(sqlQuery);
			noOfCols = dbmsUserBean.getNumOfCols();
			noOfRows = dbmsUserBean.getNumOfRows();
			dbmsUserBean.generateResult();
			result = dbmsUserBean.getResult();
			columnNamesSelected = dbmsUserBean.columnList(tableinList);
			queryRendered = true;
			return "SUCCESS";
		} 
		else 
		{
			messageBean.setErrorMessage("Please select Table Name from list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}
	
	
	
	
	//private ArrayList<Double> values = new ArrayList<Double>();
	/*public String getColStat()
	{
		messageBean.reset();
		for(int j=0;j<columnNamesSelected.size();j++)
		{
			try 
			{
				System.out.println("zz");
				result = null;
				queryRendered = true;
				messageBean.reset();
				sqlQuery = "select " + columnNamesSelected.get(j) + " from  " + tableName;
				System.out.print("\nQuery"+sqlQuery);
				
				dbmsUserBean.execute(sqlQuery);
				rs = dbmsUserBean.getResultSet();
				rsmd = (ResultSetMetaData) rs.getMetaData();
				int columnCount = rsmd.getColumnCount();

				
				for (int i = 1; i < columnCount + 1; i++)
				{
					values = new ArrayList<Double>();
					columnName = rsmd.getColumnName(i);
					String columnType = rsmd.getColumnTypeName(i);
					
					while (rs.next())
					{
						switch (columnType.toLowerCase()) 
						{
						case "int":
							values.add((double) rs.getInt(columnName));
							break;
						case "smallint":
							values.add((double) rs.getInt(columnName));
							break;
						case "float":
							values.add((double) rs.getFloat(columnName));
							break;
						case "double":
							values.add((double) rs.getDouble(columnName));
							break;
						case "long":
							values.add((double) rs.getLong(columnName));
							break;
						default:
							values.add((double) rs.getDouble(columnName));
							break;
						}
					}
				}
			}
			catch (Exception e) 
			{
				messageBean.setErrorMessage("Query not exececuted due to General Exception. Error Message = " + e.getMessage());  
				queryRendered = false;
				return "FAIL";

			}
		}	
			return "SUCCESS";
		}
	
	
	
	*/

	public String selectColumns()
	{
		messageBean.reset();
		if(null!= columnNamesSelected && !columnNamesSelected.isEmpty())
		{
			sqlQuery = "select "+ (columnNamesSelected.toString().replace("[","")).replace("]", "")+" from "+tableinList+" ;";
			dbmsUserBean.execute(sqlQuery);
			noOfCols = dbmsUserBean.getNumOfCols();
			noOfRows = dbmsUserBean.getNumOfRows();
			dbmsUserBean.generateResult();
			result = dbmsUserBean.getResult();
			columnNamesSelected =  dbmsUserBean.getColumnNamesSelected();
			queryRendered = true;
			return "SUCCESS";
		}
		else 
		{
			messageBean.setErrorMessage("Please select Column Name from list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}
	
	public String export(){
		try 
		{
		
			FacesContext facesCont = FacesContext.getCurrentInstance();
			ExternalContext externalCont = facesCont.getExternalContext();
			FileOutputStream fileOutputStream = null;
			String path = facesCont.getExternalContext().getRealPath("/temp");
			System.out.println(path);
			File dir = new File(path);
			
			if(!dir.exists())
				new File(path).mkdirs();
			externalCont.setResponseCharacterEncoding("UTF-8");
			String fileNameBase = tableName+ ".csv";
			String fileName = path + "/" + "_" + fileNameBase;
			File f = new File(fileName);
			//rs = dbmsUserBean.getResultSet();
			//result = ResultSupport.toResult(rs);
			dbmsUserBean.execute(sqlQuery);
			dbmsUserBean.generateResult();
			result = dbmsUserBean.getResult();
			Object [][] sData = result.getRowsByIndex();
			StringBuffer stringBuff = new StringBuffer();
			
			try 
			{
			fileOutputStream = new FileOutputStream(fileName);
			for(int i=0; i<columnNamesSelected.size(); i++) 
			{
				stringBuff.append(columnNamesSelected.get(i) + ",");
			}
			stringBuff.deleteCharAt(stringBuff.length() - 1);
			stringBuff.append("\n");
			fileOutputStream.write(stringBuff.toString().getBytes());
			for(int i = 0; i < sData.length; i++)
			{
				
				stringBuff= new StringBuffer();
				for(int j=0; j<sData[0].length; j++) 
				{
					StringBuffer sb1 = new StringBuffer();
					sb1.append(sData[i][j]);
					String temp=sb1.toString();
					temp=temp.replace(",", "");
					stringBuff.append(temp+",");	
				}
				stringBuff.deleteCharAt(stringBuff.length() - 1);
				stringBuff.append("\n");
				fileOutputStream.write(stringBuff.toString().getBytes());
			}
			
			fileOutputStream.flush();
			fileOutputStream.close();
			} 
			
			catch (FileNotFoundException error) 
			{
				messageBean.setErrorMessage(error.getMessage());
				messageBean.setRenderErrorMessage(true);
			} 
			
			catch (IOException io) 
			{
				messageBean.setErrorMessage(io.getMessage());
				messageBean.setRenderErrorMessage(true);
			}
			String mimeType = externalCont.getMimeType(fileName);
			FileInputStream input = null;
			byte b;
			externalCont.responseReset();
			externalCont.setResponseContentType(mimeType);
			externalCont.setResponseContentLength((int) f.length());
			externalCont.setResponseHeader("Content-Disposition",
			"attachment; filename=\"" + fileNameBase + "\"");
			
			try 
			{
				input = new FileInputStream(f);
				OutputStream output = externalCont.getResponseOutputStream();
			
			while(true) 
			{
			b = (byte) input.read();
			if(b < 0)
			break;
			output.write(b);
		
			}
			} 
			
			catch (IOException error) 
			{
				messageBean.setErrorMessage(error.getMessage());
				messageBean.setRenderErrorMessage(true);
			}
			
			finally
			{

			
			try 
			{ 
			input.close(); 
			} 
			
			catch (IOException error) 
			{
				messageBean.setErrorMessage(error.getMessage());
				messageBean.setRenderErrorMessage(true);
			}
			}
			facesCont.responseComplete();
			
			
			
			
		
			return "SUCCESS";
			} 
			
			catch (Exception error) 
			{
				messageBean.setErrorMessage(error.getMessage());
				messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		}
	
	public String processQuery() {
		messageBean.reset();
		try 
		{
			if (dbmsUserBean.execute(sqlQuery).equals("SUCCESS")) 
			{
				if (sqlQuery.toLowerCase().startsWith("select")) 
				{
					noOfCols = dbmsUserBean.getNumOfCols();
					noOfRows = dbmsUserBean.getNumOfRows();
					dbmsUserBean.generateResult();
					result = dbmsUserBean.getResult();
					columnNamesSelected = dbmsUserBean.getColumnNamesSelected();

					queryRendered = true;
				} 
				else 
				{
					messageBean.setRenderSuccessMessage(true);
					messageBean.setSuccessMessage("SQL Query Successfully excecuted");
					queryRendered = false;
				}
			} 
			else 
			{
				queryRendered = false;
				return "FAIL";
			}
			return "SUCCESS";
		} 
		catch (Exception e) 
		{
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}
	
	public String back()
	{
		messageBean.reset();
		columnNamesSelected=null;
		columnListRendered=false;
		queryRendered = false;
		tableListRendered=false;
		sqlQuery="";
		return "BACK";
	}
	
	public String logout() 
	{
		messageBean.reset();
		String logoutQuery="Select SUBSTRING_INDEX(host,':',1) as 'ip',sysdate() "
				+ "From information_schema.processlist "
				+ "WHERE ID=connection_id();";
		String ip="";
		String timestamp="";
		try
		{
			if(dbmsUserBean.execute(logoutQuery).equals("SUCCESS"))
			{
				while(dbmsUserBean.getResultSet().next())
				{
					ip=dbmsUserBean.getResultSet().getString(1);
					timestamp=dbmsUserBean.getResultSet().getString(2);
				}
				System.out.print("\nIP :"+ip+"\nTS :"+timestamp);
				String insertQuery="Insert into f17x321.f17g211_loginlogout values("+ "\""+ip+"\""+" , "+ "\""+timestamp+"\"" +" , "+ "\"Logged_Out\")";
				System.out.print("\n"+insertQuery);
				dbmsUserBean.execute(insertQuery);
			}
		}
		catch(Exception e)
		{
			messageBean.setErrorMessage("Exception Occured"+e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "LOGOUT";
	}
	
	public boolean isTableListRendered() 
	{
		return tableListRendered;
	}
	
	public String getTableName() 
	{
		return tableName;
	}
	
	public String getSqlQuery() 
	{
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) 
	{
		this.sqlQuery = sqlQuery;
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public void setTableName(String tableName) 
	{
		this.tableName = tableName;
	}
	
	public List<String> getColumnNames()
	{
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) 
	{
		this.columnNames = columnNames;
	}
	
	public List<String> getColumnType()
	{
		return columnType;
	}

	public void setColumnType(List<String> columnType) 
	{
		this.columnType = columnType;
	}
	
	public List<String> getColumnNamesSelected() 
	{
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) 
	{
		this.columnNamesSelected = columnNamesSelected;
	}
	
	
	public List<String> getTableViewList() 
	{
		return tableViewList;
	}
	
	public boolean isQueryRendered() 
	{
		return queryRendered;
	}
	
	public boolean isColumnListRendered() 
	{
		return columnListRendered;
	}
	
	public boolean isColumnTypeListRendered() 
	{
		return columnTypeListRendered;
	}
	
	public int getNoOfCols() 
	{
		return noOfCols;
	}

	public int getNoOfRows() 
	{
		return noOfRows;
	}
	
	public Result getResult() 
	{
		return result;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setQueryRendered(boolean queryRendered) {
		this.queryRendered = queryRendered;
	}

	public void setColumnListRendered(boolean columnListRendered) {
		this.columnListRendered = columnListRendered;
	}
	
	
}
