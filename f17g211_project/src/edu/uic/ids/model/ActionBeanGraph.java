package edu.uic.ids.model;


import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class ActionBeanGraph 
{
	private MessageBean messageBean;
	private DbAccessInfoBean dbAcessInfoBean;
	private DbActionBean dbActionBean;
	private DbmsUserBean dbmsUserBean;
	private FacesContext context;
	private boolean renderXYChart = false;
	private boolean renderRegAnalysis = false;
	private String xYchartPath;
	private String status;
	private double intercept;
	private double slope;
	private double rSqr;
	private double interceptStdError;
	private double slopeStdError;
	private double rSignificance;
	private String rEquation;
	
	private XYSeries predictorSeries;
	private XYSeries responseSeries;
	private XYSeries xySeries; 
	private XYSeriesCollection xySeriesVariable;
	private XYSeriesCollection xyTimeSeriesCollection ;
	private double totalDF;
	private double interceptStandardError;
	private double tStatistic;
	private double predictorDF;
	private double residualErrorDF;
	private double rSquareAdjusted;
	private double interceptPValue;
	private double tStatisticpredict;
	private double pValuePredictor;
	private double standardErrorModel;
	private double regressionSumSquares;
	private double sumSquaredErrors;
	private double totalSumSquares;
	private double meanSquare;
	private double meanSquareError;
	private double fValue;
	private double pValue ;
	private String predictorValue;
	private String responseValue;
	
	@PostConstruct
	public void init() 
	{
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		messageBean = (MessageBean) m.get("messageBean");
		dbActionBean = (DbActionBean) m.get("dbActionBean");
		dbAcessInfoBean=  (DbAccessInfoBean) m.get("dbAcessInfoBean");
		dbmsUserBean=  (DbmsUserBean) m.get("dbmsUserBean");
		messageBean.reset();
		dbActionBean.setColumnListRendered(false);
		
	}
	

	
	public String generateGraph() 
	{
		
		renderRegAnalysis = false;
		messageBean.reset();
		renderXYChart = false;
		context = FacesContext.getCurrentInstance();
		JFreeChart chart;
		System.out.println("bc"+responseValue);
		System.out.println("bc"+predictorValue);
		if(null==dbActionBean.getTableName()&& "".equals(dbActionBean.getTableName()))
		{
			messageBean.setErrorMessage("Please select Table Name from the list");
			messageBean.setRenderErrorMessage(true);
			status= "FAIL";
		}
		else if(responseValue == null || predictorValue == null)
		{
			messageBean.setErrorMessage("Please select response and predictor values from the list");
			messageBean.setRenderErrorMessage(true);
			status=  "FAIL";
		}
		else if(responseValue.equals("") || predictorValue.equals(""))
		{
			messageBean.setErrorMessage("Please select response and predictor values from the list");
			messageBean.setRenderErrorMessage(true);
			status=  "FAIL";
		}
		
		else
		{
		try
		{
			predictorSeries= new XYSeries("Predictor");
 			responseSeries = new XYSeries("Response");
 			xySeries= new XYSeries("Random");
 			xySeriesVariable= new XYSeriesCollection();
 			xyTimeSeriesCollection = new XYSeriesCollection();
			responseSeries.clear();
			predictorSeries.clear();
			xySeries.clear();
			xySeriesVariable.removeAllSeries();
			xyTimeSeriesCollection.removeAllSeries();
		
		String sqlQuery = "select " + predictorValue + ", " + responseValue + " from " + dbActionBean.getTableName();
		System.out.print(sqlQuery);
		dbmsUserBean.execute(sqlQuery);
		ResultSet resultSet = dbmsUserBean.getResultSet();
		if (resultSet != null)
		{
		ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
		String predictorName = resultSetMetaData.getColumnTypeName(1);
		String responseName = resultSetMetaData.getColumnTypeName(2);
		System.out.print("\nPre:"+predictorName+"\nRes :"+responseName);
		List<Double> predictorList = new ArrayList<Double>();
		List<Double> responseList = new ArrayList<Double>();
		while (resultSet.next()) 
		{
		switch (predictorName.toLowerCase()) 
		{
		case "int":
		predictorList.add((double) resultSet.getInt(1));
		break;
		case "smallint":
		predictorList.add((double) resultSet.getInt(1));
		break;
		case "float":
		predictorList.add((double) resultSet.getFloat(1));
		break;
		case "double":
		predictorList.add((double) resultSet.getDouble(1));
		break;
		case "long":
		predictorList.add((double) resultSet.getLong(1));
		break;
		default:
		predictorList.add((double) resultSet.getDouble(1));
		break;
		}
		switch (responseName.toLowerCase())
		{
		case "int":
		responseList.add((double) resultSet.getInt(2));
		break;
		case "smallint":
		responseList.add((double) resultSet.getInt(2));
		break;
		case "float":
		responseList.add((double) resultSet.getFloat(2));
		break;
		case "double":
		responseList.add((double) resultSet.getDouble(2));
		break;
		case "long":
		responseList.add((double) resultSet.getLong(2));
		break;
		default:
		responseList.add((double) resultSet.getDouble(2));
		break;
		}
		}
		System.out.print("\nPredictor List: \n");
		for(int z=0;z<predictorList.size();z++)
		{
			System.out.print(predictorList.get(z)+" ");
		}
		System.out.print("\nResponse List: \n");
		for(int z=0;z<responseList.size();z++)
		{
			System.out.print(responseList.get(z)+" ");
		}
		double[] predictorArray = new double[predictorList.size()];
		for (int i = 0; i < predictorList.size(); i++)
		{
		predictorArray[i] = (double) predictorList.get(i);
		predictorSeries.add(i + 1, (double) predictorList.get(i));
		}
		double[] responseArray = new double[responseList.size()];
		for (int i = 0; i < responseList.size(); i++) 
		{
		responseArray[i] = (double) responseList.get(i);
		responseSeries.add(i + 1, (double) responseList.get(i));
		}
		xyTimeSeriesCollection.addSeries(predictorSeries);
		xyTimeSeriesCollection.addSeries(responseSeries);
		
		if (responseArray.length > predictorArray.length)
		{
		for (int i = 0; i < predictorArray.length; i++) 
		{
		
		xySeries.add(predictorArray[i], responseArray[i]);
		}
		}
		else 
		{
		for (int i = 0; i < responseArray.length; i++) 
		{
		
		xySeries.add(predictorArray[i], responseArray[i]);
		}
		}
		xySeriesVariable.addSeries(xySeries);
			System.out.println("Hello");
			FacesContext context = FacesContext.getCurrentInstance();
			 String path = context.getExternalContext().getRealPath("/ChartImages");
			 File dir = new File(path);
			 if(!dir.exists())
			 {
				 new File(path).mkdirs();
			 }
			//String path = context.getExternalContext().getRealPath("/ChartImages");
			System.out.print("\nPath "+path);
			File outChart;
			//generateDataset();
			outChart = new File(path + "/" +"_X-YSeriesGraph.png");
			chart = ChartFactory.createScatterPlot(
					 "ScatterPlot", // chart title
					 predictorValue, // x axis label
					 responseValue, // y axis label
					 xySeriesVariable, // data
					 PlotOrientation.VERTICAL,
					 true, // include legend
					 true, // tooltips
					 false // urls
					 );
			ChartUtilities.saveChartAsPNG(outChart, chart, 600, 450);
			xYchartPath = "/ChartImages/" + "_X-YSeriesGraph.png";
			renderXYChart = true;
		}
	}
		catch(NullPointerException e)
		{
			messageBean.setErrorMessage("Exception Occured :"+e.getMessage());
			messageBean.setRenderErrorMessage(true);
			e.printStackTrace();
			status="FAIL";
		}
		catch(Exception e)
		{
			messageBean.setErrorMessage("Exception Occured :"+e.getMessage());
			messageBean.setRenderErrorMessage(true);
			e.printStackTrace();
			status="FAIL";
		}
		}
		return status;	
	}
	
	

	public String toGraph() 
	{
		messageBean.reset();
		renderRegAnalysis = false;
		renderXYChart = false;
		predictorSeries=null;
		responseSeries=null;
		xySeries=null; 
		xySeriesVariable=null;
		xyTimeSeriesCollection=null;
		predictorValue="";
		responseValue="";
		return "GRAPH";
	}
	
/*	public String regressionAnalysis() 
	{
		
		messageBean.reset();
		renderRegAnalysis = false;
		if(null==dbActionBean.getTableName()&&"".equals(dbActionBean.getTableName()))
		{
			messageBean.setErrorMessage("Please select Table Name from the list");
			messageBean.setRenderErrorMessage(true);
			status= "FAIL";
		}
		else if(null== dbActionBean.getColumnNamesSelected())
		{
			messageBean.setErrorMessage("Please select Column Name from the list");
			messageBean.setRenderErrorMessage(true);
			status=  "FAIL";
		}
		else if(dbActionBean.getColumnNamesSelected().size()!=2)
		{
			messageBean.setErrorMessage("Please select 2 Column Names from the list");
			messageBean.setRenderErrorMessage(true);
			status=  "FAIL";
			
		}
		else
		{
		try 
		{
			status = dbActionBean.getColStat();
		
			
			SimpleRegression sr = new SimpleRegression();
			
			
			for(int i=0;i<dbActionBean.getValue_result().length;i++)
			{
				sr.addData(dbActionBean.getValue_result()[i],dbActionBean.getValue_result1()[i]);
			}	

			intercept = sr.getIntercept();
			slope = sr.getSlope();
			rSqr = sr.getRSquare();
			interceptStdError = sr.getInterceptStdErr();
			slopeStdError = sr.getSlopeStdErr();
			rSignificance = sr.getSignificance();
			renderRegAnalysis = true;
			xData= dbActionBean.getColumnNamesSelected().get(0);
			yData= dbActionBean.getColumnNamesSelected().get(1);
			rEquation = dbActionBean.getColumnNamesSelected().get(0) + " = " + round(intercept) + " + " + round(slope) + " * "
							+ dbActionBean.getColumnNamesSelected().get(1);
		 
		

		}
		catch (Exception e)
		{
			
		}
		}
		return "SUCCESS";
	}*/
	
	public double round(double d) 
	{
		if (10 < Math.abs(d)) 
		{
			d = Math.round(d * 100.0) / 100.0;
		} 
		else 
		{
			d = Math.round(d * 10000.0) / 10000.0;
		}
		return d;
	}
	

	
	public String calculateRegressionVariables()
	{	 	
		renderXYChart=false;
		messageBean.reset();
		renderRegAnalysis = false;
		if(null==dbActionBean.getTableName()&&"".equals(dbActionBean.getTableName()))
		{
			messageBean.setErrorMessage("Please select Table Name from the list");
			messageBean.setRenderErrorMessage(true);
			status= "FAIL";
		}
		else if(responseValue == null || predictorValue == null)
		{
			messageBean.setErrorMessage("Please select response and predictor values from the list");
			messageBean.setRenderErrorMessage(true);
			status=  "FAIL";
		}
		else if(totalDF==-1)
		{
			messageBean.setErrorMessage("Please select response and predictor values from the list");
			messageBean.setRenderErrorMessage(true);
			status=  "FAIL";
		}
		
		else
		{
		
			try 
			{
				predictorSeries= new XYSeries("Predictor");
	 			responseSeries = new XYSeries("Response");
	 			xySeries= new XYSeries("Random");
	 			xySeriesVariable= new XYSeriesCollection();
	 			xyTimeSeriesCollection = new XYSeriesCollection();
				responseSeries.clear();
				predictorSeries.clear();
				xySeries.clear();
				xySeriesVariable.removeAllSeries();
				xyTimeSeriesCollection.removeAllSeries();
			
			String sqlQuery = "select " + predictorValue + ", " + responseValue + " from " + dbActionBean.getTableName();
			System.out.print(sqlQuery);
			dbmsUserBean.execute(sqlQuery);
			ResultSet resultSet = dbmsUserBean.getResultSet();
			if (resultSet != null)
			{
			ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
			String predictorName = resultSetMetaData.getColumnTypeName(1);
			String responseName = resultSetMetaData.getColumnTypeName(2);
			System.out.print("\nPre:"+predictorName+"\nRes :"+responseName);
			List<Double> predictorList = new ArrayList<Double>();
			List<Double> responseList = new ArrayList<Double>();
			while (resultSet.next()) 
			{
			switch (predictorName.toLowerCase()) 
			{
			case "int":
			predictorList.add((double) resultSet.getInt(1));
			break;
			case "smallint":
			predictorList.add((double) resultSet.getInt(1));
			break;
			case "float":
			predictorList.add((double) resultSet.getFloat(1));
			break;
			case "double":
			predictorList.add((double) resultSet.getDouble(1));
			break;
			case "long":
			predictorList.add((double) resultSet.getLong(1));
			break;
			default:
			predictorList.add((double) resultSet.getDouble(1));
			break;
			}
			switch (responseName.toLowerCase())
			{
			case "int":
			responseList.add((double) resultSet.getInt(2));
			break;
			case "smallint":
			responseList.add((double) resultSet.getInt(2));
			break;
			case "float":
			responseList.add((double) resultSet.getFloat(2));
			break;
			case "double":
			responseList.add((double) resultSet.getDouble(2));
			break;
			case "long":
			responseList.add((double) resultSet.getLong(2));
			break;
			default:
			responseList.add((double) resultSet.getDouble(2));
			break;
			}
			}
			System.out.print("\nPredictor List: \n");
			for(int z=0;z<predictorList.size();z++)
			{
				System.out.print(predictorList.get(z)+" ");
			}
			System.out.print("\nResponse List: \n");
			for(int z=0;z<responseList.size();z++)
			{
				System.out.print(responseList.get(z)+" ");
			}
			double[] predictorArray = new double[predictorList.size()];
			for (int i = 0; i < predictorList.size(); i++)
			{
			predictorArray[i] = (double) predictorList.get(i);
			predictorSeries.add(i + 1, (double) predictorList.get(i));
			}
			double[] responseArray = new double[responseList.size()];
			for (int i = 0; i < responseList.size(); i++) 
			{
			responseArray[i] = (double) responseList.get(i);
			responseSeries.add(i + 1, (double) responseList.get(i));
			}
			xyTimeSeriesCollection.addSeries(predictorSeries);
			xyTimeSeriesCollection.addSeries(responseSeries);
			SimpleRegression sr = new SimpleRegression();
			if (responseArray.length > predictorArray.length)
			{
			for (int i = 0; i < predictorArray.length; i++) 
			{
			sr.addData(predictorArray[i], responseArray[i]);
			xySeries.add(predictorArray[i], responseArray[i]);
			}
			}
			else 
			{
			for (int i = 0; i < responseArray.length; i++) 
			{
			sr.addData(predictorArray[i], responseArray[i]);
			xySeries.add(predictorArray[i], responseArray[i]);
			}
			}
			xySeriesVariable.addSeries(xySeries);
			totalDF = responseArray.length - 1;
			System.out.println("\nTotalDf :"+totalDF);
				TDistribution tDistribution = new TDistribution(totalDF);
				intercept = sr.getIntercept();
				interceptStandardError = sr.getInterceptStdErr();
				tStatistic = 0;
				predictorDF = 1;
				residualErrorDF = totalDF - predictorDF;
				rSqr = sr.getRSquare();
				rSquareAdjusted = rSqr - (1 - rSqr)/(totalDF - predictorDF - 1);
				if(interceptStandardError!=0){
					tStatistic = (double)intercept/interceptStandardError;
				}
				interceptPValue = (double)2*tDistribution.cumulativeProbability(-Math.abs(tStatistic));
				slope = sr.getSlope();
				slopeStdError = sr.getSlopeStdErr();
				tStatisticpredict = 0;
				if(slopeStdError != 0) {
					tStatisticpredict = (double)slope/slopeStdError;
				}
				pValuePredictor = (double)2*tDistribution.cumulativeProbability(-Math.abs(tStatisticpredict));
				standardErrorModel = Math.sqrt(StatUtils.variance(responseArray))*(Math.sqrt(1-rSquareAdjusted));
				regressionSumSquares = sr.getRegressionSumSquares();
				sumSquaredErrors = sr.getSumSquaredErrors();
				totalSumSquares = sr.getTotalSumSquares();
				meanSquare = 0;
				if(predictorDF!=0) 
				{
					meanSquare = regressionSumSquares/predictorDF;
				}
				meanSquareError = 0;
				if(residualErrorDF != 0)
				{
					meanSquareError = (double)(sumSquaredErrors/residualErrorDF);
				}
				fValue = 0;
				if(meanSquareError != 0) 
				{
					fValue = meanSquare/meanSquareError;
				}
				rEquation = responseValue + " = " + round(intercept) + " + " + round(slope) + " * "
						+ predictorValue;
			
				FDistribution fDistribution = new FDistribution(predictorDF, residualErrorDF);
				pValue = (double)(1-fDistribution.cumulativeProbability(fValue));
				
				System.out.print("TotalDF NOW :"+totalDF);
				renderRegAnalysis = true;
				return "SUCCESS";
			
			} 
		}
	catch(Exception e) 
		{
			messageBean.setErrorMessage("Exception Occured :"+e.getMessage()); 
			e.printStackTrace();
			messageBean.setRenderErrorMessage(true);
			renderRegAnalysis = false;
			return "FAIL";
		}
	}
	return "SUCCESS";
	}

	
	
	
	public boolean isRenderXYChart() 
	{
		return renderXYChart;
	}
	public String getxYchartPath() 
	{
		return xYchartPath;
	}
	
	public String getrEquation() {
		return rEquation;
	}

	public double getIntercept() {
		return round(intercept);
	}

	public double getSlope() {
		return round(slope);
	}

	public double getrSqr() {
		return round(rSqr);
	}

	public double getInterceptStdError() {
		return round(interceptStdError);
	}

	public double getSlopeStdError() {
		return round(slopeStdError);
	}

	public double getrSignificance() {
		return round(rSignificance);
	}
	public boolean isRenderRegAnalysis() {
		return renderRegAnalysis;
	}


	public void setRenderRegAnalysis(boolean renderRegAnalysis) 
	{
		this.renderRegAnalysis = renderRegAnalysis;
	}

	public double getInterceptStandardError() {
		return interceptStandardError;
	}

	public double gettStatistic() {
		return tStatistic;
	}

	public double getPredictorDF() {
		return predictorDF;
	}

	public double getrSquareAdjusted() {
		return rSquareAdjusted;
	}

	public double getInterceptPValue() {
		return interceptPValue;
	}

	public double gettStatisticpredict() {
		return tStatisticpredict;
	}

	public double getpValuePredictor() {
		return pValuePredictor;
	}

	public double getStandardErrorModel() {
		return standardErrorModel;
	}

	public double getRegressionSumSquares() {
		return regressionSumSquares;
	}

	public double getMeanSquare() {
		return meanSquare;
	}

	public double getMeanSquareError() {
		return meanSquareError;
	}

	public double getfValue() {
		return fValue;
	}


	public double getResidualErrorDF() {
		return residualErrorDF;
	}

	public double getSumSquaredErrors() {
		return sumSquaredErrors;
	}


	public XYSeries getXySeries() {
		return xySeries;
	}

	public String getPredictorValue() {
		return predictorValue;
	}



	public void setPredictorValue(String predictorValue) {
		this.predictorValue = predictorValue;
	}



	public String getResponseValue() {
		return responseValue;
	}



	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}



	public XYSeriesCollection getXySeriesVariable() {
		return xySeriesVariable;
	}

	public double getTotalDF() {
		System.out.println("zz tdf :"+totalDF);
		return totalDF;
	}

	public double getTotalSumSquares() {
		return totalSumSquares;
	}

	public double getpValue() {
		return pValue;
	}


	
	
	
	


	
}
		

	