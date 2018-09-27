package edu.uic.ids.model;


public class DbAccessInfoBean {
	private String userName; //f17x321
	private String password; //f17x321TS
	private String dbms;
	private String dbmsHost;
	private String databaseSchema;
	private String portInfo;
	public DbAccessInfoBean()
	{
		this.userName = "f17x321";
		this.password = "f17x321TS";
		this.dbms = "MySQL";
		this.dbmsHost = "131.193.209.68";
		this.databaseSchema = "world";  //f17x321
		this.portInfo="3306";
	}
	//public DbAccessInfoBean() {}
	public DbAccessInfoBean(String userName, String password, String dbms,String dbmsHost, String databaseSchema, String portInfo)
	{
		
		this.userName=userName;
		this.password=password;
		this.dbms = dbms;
		this.dbmsHost = dbmsHost;
		this.databaseSchema = databaseSchema;
		this.portInfo = portInfo;
	}
	

	public String getUserName() 
	{
		return userName;
	}
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getDbms() 
	{
		return dbms;
	}
	public void setDbms(String dbms) 
	{
		this.dbms = dbms;
	}
	public String getDbmsHost()
	{
		return dbmsHost;
	}
	public void setDbmsHost(String dbmsHost) 
	{
		this.dbmsHost = dbmsHost;
	}
	public String getDatabaseSchema() 
	{
		return databaseSchema;
	}
	public void setDatabaseSchema(String databaseSchema) 
	{
		this.databaseSchema = databaseSchema;
	}
	public String getPortInfo() 
	{
		return portInfo;
	}
	public void setPortInfo(String portInfo) 
	{
		this.portInfo = portInfo;
		
	}
	//Getters and Settlers for the variables

}
