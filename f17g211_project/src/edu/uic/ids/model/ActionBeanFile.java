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
import org.apache.myfaces.custom.fileupload.*;

public class ActionBeanFile
{
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String uploadedFileContents;
	private long fileSize;
	private String fileName;
	private String fileContentType;
	private MessageBean messageBean;
	private DbmsUserBean dbmsUserBean;
	private DbActionBean dbActionBean;
	private String contextPath;
	private FacesContext context;
	private ExternalContext externalContext;
	private Boolean fileImportError;
	private Boolean fileImport;
	private Result result;
	private int noOfCols = 0;
	private int noOfRows = 0;
	private List<String> fileList;
	private String fileLabel1;
	
	
	
	@PostConstruct
	public void init() 
	{
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbmsUserBean = (DbmsUserBean) m.get("dbmsUserBean");
		messageBean = (MessageBean) m.get("messageBean");
		dbActionBean = (DbActionBean) m.get("dbActionBean");
		contextPath = context.getExternalContext().getRealPath("/");
		messageBean.reset();
		dbActionBean.setQueryRendered(false);
	}
	
	public String processFileUpload() 
	{
		messageBean.reset();
		String status="SUCCESS";
		uploadedFileContents = null;
		String path = contextPath + "temp";
		File dir = new File(path);
		if(!dir.exists())
			new File(path).mkdirs();
		File tempFile = null;
		FileOutputStream fos = null;
		fileImport = false;
		fileImportError = true;
		try 
		{
			fileName = uploadedFile.getName();
			fileName = fileName.substring((fileName.lastIndexOf("\\") + 1), fileName.length());
			fileSize = uploadedFile.getSize();
			fileContentType = uploadedFile.getContentType();
			uploadedFileContents = new 	String(uploadedFile.getBytes());
			tempFile = new File(path + "/" + fileLabel);
			System.out.println("\nFileName :"+ fileLabel);
			fos = new FileOutputStream(tempFile);
			fos.write(uploadedFile.getBytes());
			fos.flush();
			fos.close();
			//updateFileList();
			fileImport = true;
			fileImportError = false;
			messageBean.setRenderSuccessMessage(true);
			messageBean.setSuccessMessage("File Uploaded Successfully.");
			
		} 
		catch (FileNotFoundException e) 
		{

			e.printStackTrace();
			fileImportError = true;
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		} 
		
		catch(NullPointerException e)
		{
			e.printStackTrace();
			fileImportError = true;
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		
		catch (IOException e) 
		{

			e.printStackTrace();
			fileImportError = true;
			messageBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return status;
	}
	
	public void runQuery()
	{
		messageBean.reset();
		FacesContext fc = FacesContext.getCurrentInstance();
		String path = fc.getExternalContext().getRealPath("/temp");
		String fileName = path + "/" + fileLabel1;
		System.out.print("sad"+ fileLabel1);
		File f = new File(fileName); 
		Scanner s;
		try 
		{
	
			s = new Scanner(f);
			String query="";
			while(s.hasNext())
			{
				query = s.nextLine();
			}
			System.out.print(query);
			dbActionBean.setSqlQuery(query);
			dbActionBean.processQuery();
		}
		catch(Exception e)
		{
			messageBean.setErrorMessage("Couldnt parse the file. File upload failed");
			e.printStackTrace();
		}
		
	}
	
	public void updateFileList() 
	{
		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRealPath("/temp");
		
		File folder = new File(path);
		fileList = new ArrayList<String>();
		
		for (File fileEntry : folder.listFiles()) 
		{
	        if (!fileEntry.isDirectory()) 
	        {
	        	if(!fileEntry.getName().contains(".csv"))
	        		fileList.add(fileEntry.getName());
	            System.out.println(fileEntry.getName());
	        }
	    }
	}
	

	/*public String processFileDownload() 
	{
		messageBean.reset();
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		FileOutputStream fos = null;
		String path = fc.getExternalContext().getRealPath("/temp");
		//File dir = new File(path);
		
		File f = new File(fileName);
		if (!f.exists()) {
			new File(path).mkdirs();
		}

		
		/*if(!dir.exists())
			new File(path).mkdirs();
		ec.setResponseCharacterEncoding("UTF-8");
		
		String fileName1 = path + "/" + fileLabel1;
		//File f = new File(fileName); 
		File f1 = new File(fileName1); 
		String status="SUCCESS";
		Scanner s;
		try 
		{
			s = new Scanner(f1);
			String query="";
			while(s.hasNext())
			{
				query = s.nextLine();
			}
			System.out.println("\nQ"+ query);
			dbmsUserBean.execute(query);
			dbmsUserBean.generateResult();
			result = dbmsUserBean.getResult();
			s.close();
		   	Object[][] sData = result.getRowsByIndex();
		   	System.out.println(sData.length);
			StringBuffer sb = new StringBuffer();
			fos = new FileOutputStream(fileName);
			//fos = new FileOutputStream(path + "/" + fileLabel1+".csv");
			String fileNameBase = dbActionBean.getTableName()+ ".csv";
			String fileName = path + "/" + "_" + fileNameBase;
			for(int i=0; i<dbmsUserBean.getColumnNamesSelected().size(); i++)
			{
			
				sb.append(dbmsUserBean.getColumnNamesSelected().get(i) + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
			fos.write(sb.toString().getBytes());
			
			for(int i = 0; i < sData.length; i++)
			{
				
				sb= new StringBuffer();
				for(int j=0; j<sData[0].length; j++) 
				{
					StringBuffer sb1 = new StringBuffer();
					sb1.append(sData[i][j]);
					String temp=sb1.toString();
					temp=temp.replace(",", "");
					sb.append(temp+",");	
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("\n");
				fos.write(sb.toString().getBytes());
			}
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
		}
		String mimeType = ec.getMimeType(fileName);
		FileInputStream in = null;
		byte b;
		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition","attachment; filename=\"" +	fileName + "\"");
		try 
		{
			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			while(true)
			{
				b = (byte) in.read();
				if(b < 0)
					break;
				output.write(b);
			}
		}
		catch (Exception e) 
		{
			messageBean.setErrorMessage("Couldnt parse the file. File download failed");
			messageBean.setRenderErrorMessage(true);
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				in.close();
			}
			catch (Exception e) 
			{
				messageBean.setErrorMessage("Couldnt parse the file. File download failed");
				messageBean.setRenderErrorMessage(true);
				e.printStackTrace();
			}
		 }
		fc.responseComplete();
		return status;
		
	}*/
	
	/*
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
			
			String fileName1 = path + "/" + fileLabel1;
			File f = new File(fileName);
			File f1 = new File(fileName1);
			//rs = dbmsUserBean.getResultSet();
			//result = ResultSupport.toResult(rs);
			Scanner s;
			
				s = new Scanner(f1);
				String query="";
				while(s.hasNext())
				{
					query = s.nextLine();
				}
			
			dbmsUserBean.execute(query);
			dbmsUserBean.generateResult();
			s.close();
			result = dbmsUserBean.getResult();
			Object [][] sData = result.getRowsByIndex();
			StringBuffer stringBuff = new StringBuffer();
			
			try 
			{
			fileOutputStream = new FileOutputStream(fileName);
			for(int i=0; i<dbmsUserBean.getColumnNamesSelected().size(); i++) 
			{
				stringBuff.append(dbmsUserBean.getColumnNamesSelected().get(i) + ",");
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
			String fileNameBase = dbActionBean.getTableName()+ ".csv";
			String fileName = path + "/" + "_" + fileNameBase;
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

	*/
	
	
	public String processFileDownload() 
	{
		messageBean.reset();
		String status;
		if (dbActionBean.isQueryRendered() == true) 
		{

			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			FileOutputStream fos = null;
			String path = fc.getExternalContext().getRealPath("/temp");
			// File dir = new File(path);
			String fileName = path + "/" + fileLabel;
			File f = new File(fileName);
			if (!f.exists()) {
				new File(path).mkdirs();
			}

			status = "SUCCESS";
			Scanner s;
			try {
				s = new Scanner(f);
				String query = "";
				while (s.hasNext()) {
					query = s.nextLine();
				}
				System.out.println("\nQ" + query);
				dbActionBean.setSqlQuery(query);
				dbActionBean.processQuery();
				result = dbActionBean.getResult();
				s.close();
				Object[][] sData = result.getRowsByIndex();
				//System.out.println("SDatalenght" + databaseAccess.isQueryResultRendered());
				StringBuffer sb = new StringBuffer();
				fos = new FileOutputStream(path + "/" + "UploadQueryResult.csv");

				sb.append(" ");
				for (int i = 0; i < dbActionBean.getColumnNamesSelected().size(); i++) {

					sb.append(dbActionBean.getColumnNamesSelected().get(i) + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("\n");
				fos.write(sb.toString().getBytes());

				for (int i = 0; i < sData.length; i++) {

					sb = new StringBuffer();
					for (int j = 0; j < sData[0].length; j++) {
						StringBuffer sb1 = new StringBuffer();
						sb1.append(sData[i][j]);
						String temp = sb1.toString();
						temp = temp.replace(",", "");
						sb.append(temp + ",");
					}
					sb.deleteCharAt(sb.length() - 1);
					sb.append("\n");
					fos.write(sb.toString().getBytes());
				}
				fos.flush();
				fos.close();
				
				 messageBean.setRenderSuccessMessage(true);
				 messageBean.setSuccessMessage("File Exported Successfully.");
			}
			catch (NullPointerException e) 
			{
				 messageBean.setErrorMessage("Null Pointer Exception");
				status = "FAIL";
			} 
			catch (Exception e) 
			{
				messageBean.setErrorMessage("Couldnt parse the file. File upload failed");
				messageBean.setRenderErrorMessage(true);
				
			}
			FacesContext fc1 = FacesContext.getCurrentInstance();
			ExternalContext ec1 = fc1.getExternalContext();
			String mimeType = ec1.getMimeType(path + "/" + "UploadQueryResult.csv");
			File f1 = new File(path + "/" + "UploadQueryResult.csv");
			FileInputStream in = null;
			byte b;
			ec1.responseReset();
			ec1.setResponseContentType(mimeType);
			ec1.setResponseContentLength((int) f1.length());
			String fileNameBase ="UploadQueryResult.csv";
			//ec.setResponseHeader("Content-Disposition", "attachment; filename=\"UploadQueryResult.csv"+);
			ec1.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileNameBase + "\"");
			try {
				// use previously generated file as input
				in = new FileInputStream(f1);
				OutputStream output = ec.getResponseOutputStream();

				b = (byte) in.read();
				do {
					b = (byte) in.read();
					if (b < 0)
						break;
					output.write(b);
				} while (true);
			} catch (Exception e) {
				e.printStackTrace();
				status = "FAIL";

			} finally {
				try {
					in.close();
				} catch (NullPointerException e) 
				{
					messageBean.setErrorMessage("Null Pointer Exception");
					status = "FAIL";
				} catch (Exception e)
				{
					messageBean.setErrorMessage("Couldnt parse the file. File upload failed");
					messageBean.setRenderErrorMessage(true);

				}
			}
			fc1.responseComplete();
		} 
		else 
		{
			status = "FAIL";
			messageBean.setErrorMessage("No Result to Export");
		}

		return status;
	}
	
	public UploadedFile getUploadedFile() 
	{
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) 
	{
		this.uploadedFile = uploadedFile;
	}
	
	public boolean isFileImport() 
	{
		return fileImport;
	}

	public void setFileImport(boolean fileImport) 
	{
		this.fileImport = fileImport;
	}

	public boolean isFileImportError() 
	{
		return fileImportError;
	}

	public void setFileImportError(boolean fileImportError) 
	{
		this.fileImportError = fileImportError;
	}

	public String getFileName() 
	{
		return fileName;
	}

	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}
	

	public DbmsUserBean getDbaseBean() 
	{
		return dbmsUserBean;
	}

	public void setDbaseBean(DbmsUserBean dbmsUserBean) 
	{
		this.dbmsUserBean = dbmsUserBean;
	}

	
	public MessageBean getMessageBean() 
	{
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) 
	{
		this.messageBean = messageBean;
	}

	
	public FacesContext getContext() 
	{
		return context;
	}

	public void setContext(FacesContext context) 
	{
		this.context = context;
	}
	
	public ExternalContext getExternalContext() 
	{
		return externalContext;
	}

	public void setExternalContext(ExternalContext externalContext) 
	{
		this.externalContext = externalContext;
	}

	public String getContextPath() 
	{
		return contextPath;
	}

	public void setContextPath(String contextPath) 
	{
		this.contextPath = contextPath;
	}

	public long getFileSize() 
	{
		return fileSize;
	}

	public void setFileSize(long fileSize) 
	{
		this.fileSize = fileSize;
	}
	
	public String getUploadedFileContents()
	{
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) 
	{
		this.uploadedFileContents = uploadedFileContents;
	}
	
	public String getFileLabel() 
	{
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) 
	{
		this.fileLabel = fileLabel;
	}

	public String getFileContentType() 
	{
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) 
	{
		this.fileContentType = fileContentType;
	}

	public int getNoOfCols() 
	{
		return noOfCols;
	}

	public int getNoOfRows() 
	{
		return noOfRows;
	}

	public List<String> getFileList() 
	{
		return fileList;
	}

	public void setFileList(List<String> fileList) 
	{
		this.fileList = fileList;
	}

	public String getFileLabel1() {
		return fileLabel1;
	}

	public void setFileLabel1(String fileLabel1) {
		this.fileLabel1 = fileLabel1;
	}
	
	

}