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
		<h3 align="center">Display Tables</h3>
		<hr>
		<br />
		
		<center>
			<h:form>
				<h:commandButton type="submit" value="Logout"
					action="#{dbActionBean.logout}" />&nbsp;&nbsp;&nbsp;
					<h:commandButton type="submit" value="Back"
					action="#{dbActionBean.back}" />
			</h:form>
			<br />
			<br />
			<br />
			<h:form>
					<h:commandButton type="submit" value="Upload File"
					action="UploadScript" /> &nbsp;&nbsp;&nbsp;
					<h:commandButton type="submit" value="Graphical Analysis"
						action="#{actionBeanGraph.toGraph}" /> &nbsp;&nbsp;&nbsp;
					<h:commandButton type="submit" value="Numerical Analysis"
						action="#{actionBeanMath.toMath}" />	
			</h:form>
		</center>
		<br />
		<hr>
		<br />
		<div align="left">
			<h:form enctype="multipart/form-data">
				<h:outputText value="#{messageBean.errorMessage}" rendered="#{messageBean.renderErrorMessage}" />
				<h:outputText value="#{messageBean.successMessage}" rendered="#{messageBean.renderSuccessMessage}" />
					<h:panelGrid columns="6">
					<h:commandButton type="submit" value="Table List"
						action="#{dbActionBean.listTables}" />
					<h:commandButton type="submit" value="Column List"
						action="#{dbActionBean.listColumns}" />
					<h:commandButton type="submit" value="Selected Columns"
						action="#{dbActionBean.selectColumns}" />
					<h:commandButton type="submit" value="Display Table"
						action="#{dbActionBean.selectAllColumn}" />	
					<h:commandButton type="submit" value="Process SQL Query"
						action="#{dbActionBean.processQuery}" />
					
				</h:panelGrid>

				
				<h:panelGrid columns="80">
					<h:selectOneListbox value="#{dbActionBean.tableName}" rendered="#{dbActionBean.tableListRendered}">
						<f:selectItems value="#{dbActionBean.tableViewList}" />
					</h:selectOneListbox>
					
					<h:selectManyListbox size="10" styleClass="selectManyListbox"
						value="#{dbActionBean.columnNamesSelected}"
						rendered="#{dbActionBean.columnListRendered}">
						<f:selectItems value="#{dbActionBean.columnNames}" />
					</h:selectManyListbox>
					<h:inputTextarea cols="70" rows="10"
						value="#{dbActionBean.sqlQuery}" />
				</h:panelGrid>
				
				<br />
				<hr />
				<br />
			<h:commandButton type="submit" value="Export Data"
						action="#{dbActionBean.export}" />
			<br />
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
				<hr />
				<br />
				<br />
				
				
				
				
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
				
			</h:form>
		</div>			
	</f:view>
</body>
</html>