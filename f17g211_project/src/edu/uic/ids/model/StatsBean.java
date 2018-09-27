package edu.uic.ids.model;

import java.io.Serializable;

public class StatsBean implements Serializable,Cloneable
{
	

	private static final long serialVersionUID = 1L;
	private double  q1;
	private double q3;
	private double interquartileRange;
	private double range;
	private double minValue;
	private double maxValue;
	private double mean;
	private double variance;
	private double std;
	private double median;
	private String columnName;
	
	
	
	public StatsBean(double q1, double q3, double interquartileRange, double range, String columnName, double minValue,
			double maxValue, double mean, double variance, double std, double median) 
	{
		this.q1=q1;
		this.q3=q3;
		this.interquartileRange=interquartileRange;
		this.range=range;
		this.columnName=columnName;
		this.minValue=minValue;
		this.maxValue=maxValue;
		this.mean=mean;
		this.std=std;
		this.variance=variance;
		this.median=median;
	}




	public double getQ1() {
		return q1;
	}




	public void setQ1(double q1) {
		this.q1 = q1;
	}




	public double getQ3() {
		return q3;
	}




	public void setQ3(double q3) {
		this.q3 = q3;
	}




	public double getInterquartileRange() {
		return interquartileRange;
	}




	public void setInterquratileRange(double interquratileRange) {
		this.interquartileRange = interquratileRange;
	}




	public double getRange() {
		return range;
	}




	public void setRange(double range) {
		this.range = range;
	}




	public double getMinValue() {
		return minValue;
	}




	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}




	public double getMaxValue() {
		return maxValue;
	}




	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}




	public double getMean() {
		return mean;
	}




	public void setMean(double mean) {
		this.mean = mean;
	}




	public double getVariance() {
		return variance;
	}




	public void setVariance(double variance) {
		this.variance = variance;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	public double getStd() {
		return std;
	}




	public void setStd(double std) {
		this.std = std;
	}




	public double getMedian() {
		return median;
	}




	public void setMedian(double median) {
		this.median = median;
	}




	public String getColumnName() {
		return columnName;
	}




	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}




	public void setVariables(double q1, double q3, double median) 
	{
		this.q1=q1;
		this.q3=q3;
		this.median=median;
	}

	

	
}



