<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
<f:view>
		<h3 align="center">Upload File</h3>
		<hr>
		<br />
		
		<center>
			<h:form>
			
				<h:commandButton type="submit" value="Logout"
					action="#{dbActionBean.logout}" />&nbsp;&nbsp;&nbsp;
					<h:commandButton type="submit" value="Back"
					action="#{dbActionBean.back}" />
			</h:form>
		</center>
		<br />
		<hr>
		<br />
		
		<h:outputText value="#{messageBean.errorMessage}" rendered="#{messageBean.renderErrorMessage}" />
		<h:outputText value="#{messageBean.successMessage}" rendered="#{messageBean.renderSuccessMessage}" />
		<br />
		
	<h:form id="uploadForm" enctype="multipart/form-data">
		<h:panelGrid columns="2">
			<h:outputText value="Select File to Upload:" />
				<t:inputFileUpload id="fileUpload" label="File to upload" storage="default" value="#{actionBeanFile.uploadedFile}" size="60" />
			<h:outputText value="File Label:" />
				<h:inputText id="fileLabel" value="#{actionBeanFile.fileLabel}" size="60" />
			<h:outputText value=" "/>
			<h:commandButton id="upload" action= "#{actionBeanFile.processFileUpload}" value="Submit"/>
			<h:commandButton id="showfiles" action= "#{actionBeanFile.updateFileList}" value="Show Files"/>
			
			
			
			<h:outputText value=" "/>
			<h:commandButton type="submit" value="Process SQL Query" action="#{actionBeanFile.runQuery}" />
		</h:panelGrid>
		
		<h:panelGrid columns="2">
			<h:outputText value="Select file to process:" 	style="font-weight:bold"  />
			<h:selectOneListbox value="#{actionBeanFile.fileLabel1}">
						<f:selectItems value="#{actionBeanFile.fileList}" />
					</h:selectOneListbox>
		</h:panelGrid>
		
		
		<hr />
				<br />

				<h:panelGrid columns="2"
					rendered="#{dbActionBean.queryRendered}">
					<h:outputText value="SQL Query:" />
					<h:outputText id="sqlQuery" value="#{dbActionBean.sqlQuery}" />
					<h:outputText value="No of Columns:" />
					<h:outputText id="noOfCol" value="#{dbActionBean.noOfCols}" />
					<h:outputText value="No of Rows:" />
					<h:outputText id="noOfRows" value="#{dbActionBean.noOfRows}" />
				</h:panelGrid>
				<br />
				<hr />
				<br />
				
			
					<h:commandButton type="submit" value="Export Data"
						action="#{actionBeanFile.processFileDownload}" />
				
			
			<br />
				<hr />
				<br />		
				
				</h:form>
				<div style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
					<t:dataTable value="#{dbActionBean.result}" var="row"
						rendered="#{dbActionBean.queryRendered}" border="1"
						cellspacing="0" cellpadding="1"
						columnClasses="columnClass1 border" headerClass="headerClass"
						footerClass="footerClass" rowClasses="rowClass2"
						styleClass="dataTableEx" width="700">
						<t:columns var="col"
							value="#{dbActionBean.columnNamesSelected}">
							<f:facet name="header">
								<t:outputText styleClass="outputHeader" value="#{col}" />
							</f:facet>
							<t:outputText styleClass="outputText" value="#{row[col]}" />
						</t:columns>
					</t:dataTable>
		
		</div>
		<hr />
		<br />
		
			<br />
		
			
</f:view>
</body>
</html>