package edu.uic.ids.model;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.commons.math3.stat.StatUtils;
import java.io.OutputStream;
import com.mysql.jdbc.ResultSetMetaData;

public class ActionBeanMath
{
	private MessageBean messageBean;
	private DbActionBean dbActionBean;
	private DbmsUserBean dbmsUserBean;
	private FacesContext context;
	private String variableName;
	boolean render;
	private List <StatsBean> statsList = null;
	private String status;
	private String contextPath;
	private StatsBean statsBean;
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	private String columnName;
	List<Double> values;
	private Result result;
	public ActionBeanMath() 
	{
		render = false;
	}
	
	public String toMath() 
	{
		messageBean.reset();
		render=false;
		return "MATH";
	}
	
	@PostConstruct
	public void init()
	{
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		messageBean = (MessageBean) m.get("messageBean");
		dbActionBean = (DbActionBean) m.get("dbActionBean");
		dbmsUserBean = (DbmsUserBean) m.get("dbmsUserBean");
		statsBean = (StatsBean) m.get("statsBean");
		dbActionBean.listTables();
		dbActionBean.setColumnNamesSelected(null);;
		messageBean.reset();
		dbActionBean.setColumnListRendered(false);
		dbActionBean.setColumnNames(null);
		contextPath = context.getExternalContext().getRealPath("/");
	}

	
	StringBuffer exportBuffer;
	public String processRequest() 
	{
		exportBuffer = new StringBuffer();
		statsList = new ArrayList<StatsBean>();
		try
		{
			
			for(int j=0;j<dbActionBean.getColumnNamesSelected().size();j++)
			{
				dbActionBean.setQueryRendered(true); 
				messageBean.reset();
				String sqlQuery = "select " + dbActionBean.getColumnNamesSelected().get(j) + " from  " + dbActionBean.getTableName();
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
				double[] values1 = new double[values.size()];
				for (int i1 = 0; i1 < values.size(); i1++) 
				{
					values1[i1] = (double) values.get(i1);
				}
			
				double minValue = StatUtils.min(values1);
				double maxValue = StatUtils.max(values1);
				double mean = StatUtils.mean(values1);
				double variance = StatUtils.variance(values1, mean);
				double std = Math.sqrt(variance);
				
				double arr[] ={1.0,2.0,3.0};
				double m1= StatUtils.percentile(arr,50.0);
				double median = StatUtils.percentile(values1, 50.0);
				double q1 = StatUtils.percentile(values1, 25.0);
				double q3 = StatUtils.percentile(values1, 75.0);
				double interquartileRange = q3 - q1;
				double range = maxValue - minValue;
				
				System.out.println(median);
				System.out.println(m1);
				System.out.println(q1);
				System.out.println(q3);
				exportBuffer.append(dbActionBean.getColumnNamesSelected().get(j)+","+minValue+","+maxValue+","+mean+","+variance+","+std +","+q1 +","+median+","+q3+","+range+","+interquartileRange+",\n");
				statsList.add(new StatsBean(q1, q3, interquartileRange, range,
						dbActionBean.getColumnNamesSelected().get(j), minValue, maxValue, mean, variance, std, median));
					
				
			}
			render = true;
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			status="FAIL";
		}
		return status;
	
	}
	
	/*public String export()
	{
		String status="SUCCESS";
		FacesContext fc = FacesContext.getCurrentInstance();
		FileOutputStream fos = null;
		String path = fc.getExternalContext().getRealPath("/temp");
		try
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Statistic,");
			sb.append("Value");
			sb.append("\n");
			fos = new FileOutputStream(path + "/" + "StatsData"+".csv");
			for(int i=0;i<statsList.size();i++)
			{
				//sb.append(statsList.get(i).getName()+","+statsList.get(i).getValue()+",");
				sb.deleteCharAt(sb.length() - 1);
				sb.append("\n");
			}
			
			sb.append("\n");
			fos.write(sb.toString().getBytes());
			fos.flush();
			fos.close();
			messageBean.setRenderSuccessMessage(true);
			messageBean.setSuccessMessage("File Exported Successfully.");
		}
		catch(Exception e)
		{
			messageBean.setErrorMessage("Couldnt parse the file. File upload failed");
			messageBean.setRenderErrorMessage(true);
			e.printStackTrace();
			status="FAIL";
		}
		return status;
	}
*/
	

	public String export(){
	try 
	{
	
		FacesContext facesCont = FacesContext.getCurrentInstance();
		ExternalContext externalCont = facesCont.getExternalContext();
		FileOutputStream fileOutputStream = null;
		String path = facesCont.getExternalContext().getRealPath("/temp");
		File dir = new File(path);
		
		if(!dir.exists())
			new File(path).mkdirs();
		externalCont.setResponseCharacterEncoding("UTF-8");
		String fileNameBase = dbActionBean.getTableName() + ".csv";
		String fileName = path + "/" + "_" + fileNameBase;
		File f = new File(fileName);
		result = ResultSupport.toResult(rs);
		result.getRowsByIndex();
		String columnNames [] = new String[]{"Column Selected","Minimum Value","	Maximum Value","	Mean","	   Variance ","	Standard Deviation	","   quartileOne"," Median ","quartileThree ","	Range ","	interquartileRange"};
		StringBuffer stringBuff = new StringBuffer();
		
		try 
		{
		fileOutputStream = new FileOutputStream(fileName);
		for(int i=0; i<columnNames.length; i++) 
		{
		stringBuff.append(columnNames[i].toString() + ",");
		}
		stringBuff.append("\n");
		stringBuff.append(exportBuffer);
		fileOutputStream.write(stringBuff.toString().getBytes());
		
		
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
	
	public String getVariableName() 
	{
		return variableName;
	}
	public void setVariableName(String variableName) 
	{
		this.variableName = variableName;
	}
	public boolean isRender() 
	{
		return render;
	}
	public void setRender(boolean render) 
	{
		this.render = render;
	}
	public List<StatsBean> getStatsList() 
	{
		return statsList;
	}
	public void setStatsList(List<StatsBean> statsList) 
	{
		this.statsList = statsList;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public StatsBean getStatsBean() {
		return statsBean;
	}

	public void setStatsBean(StatsBean statsBean) {
		this.statsBean = statsBean;
	}
	
	
}