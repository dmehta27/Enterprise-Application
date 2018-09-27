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
		

			<div align="center">

			<h:form>
				<h:commandButton type="submit" value="Logout"
					action="#{dbActionBean.logout}" />&nbsp;&nbsp;&nbsp;
					<h:commandButton type="submit" value="Back"
					action="#{dbActionBean.back}" />
			</h:form>
			<br /> <br />
		</div>
			<br />
			<br />
			<br />

	<h:form>
		<h:outputText value="#{messageBean.errorMessage}" rendered="#{messageBean.renderErrorMessage}" />
				<h:outputText value="#{messageBean.successMessage}" rendered="#{messageBean.renderSuccessMessage}" />
		
			<h:panelGrid columns="2">
				<h:commandButton type="submit" value="Table List"
						action="#{dbActionBean.listTables}" />
				
				<h:commandButton id="get" value="Get Descriptive Columns"
					action="#{dbActionBean.listColumnType}" />

				<h:selectOneListbox value="#{dbActionBean.tableName}" rendered="#{dbActionBean.tableListRendered}">
						<f:selectItems value="#{dbActionBean.tableViewList}" />
					</h:selectOneListbox>


				<h:selectManyListbox size="10" styleClass="selectManyListbox"
					value="#{dbActionBean.columnNamesSelected}"
					rendered="#{dbActionBean.columnListRendered}">
					<f:selectItems value="#{dbActionBean.columnNames}" />
				</h:selectManyListbox>
				
		

			</h:panelGrid>

			<br />
			<h:commandButton id="process" value="Process"
				action="#{actionBeanMath.processRequest}" />
			
		</h:form>
		<br />
		<h:outputLabel value="Column analyzed: " style="font-weight:bold" />
		<h:outputText value="#{dbActionBean.columnNamesSelected}" />

		<f:verbatim>
			<br />
		</f:verbatim>
		<hr />
		<br />
		<h:outputLabel value="Descriptive Statistics" style="font-weight:bold" />
		
			
			
			<div style="background-attachment: scroll; overflow: auto; background-repeat: repeat"
				align="center">
			<t:dataTable value="#{actionBeanMath.statsList}"
				var="rowNumber" rendered="#{actionBeanMath.render}"
				border="1" cellspacing="0" cellpadding="1"
				headerClass="headerWidth">
							
			<h:column>
				<f:facet name="header">
					<h:outputText value="Column Selected" />
				</f:facet>
				<h:outputText value="#{rowNumber.columnName}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Minimum Value" />
				</f:facet>
				<h:outputText value="#{rowNumber.minValue}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Maximum Value" />
				</f:facet>
				<h:outputText value="#{rowNumber.maxValue}" />
			</h:column>
			<h:column>
			<f:facet name="header">
			<h:outputText value="Mean" />
			</f:facet>
			<h:outputText value="#{rowNumber.mean}" />
			</h:column>
			<h:column>
			<f:facet name="header">
			<h:outputText value="Variance" />
			</f:facet>
			<h:outputText value="#{rowNumber.variance}" />
			</h:column>
			<h:column>
			<f:facet name="header">
			<h:outputText value="Standard Deviation" />
			</f:facet>
			<h:outputText value="#{rowNumber.std}" />
			</h:column>
			<h:column>
			<f:facet name="header">
			<h:outputText value="quartileOne" />
			</f:facet>
			<h:outputText value="#{rowNumber.q1}" />
			</h:column>
			
			<h:column>
			<f:facet name="header">
			<h:outputText value="Median" />
			</f:facet>
			<h:outputText value="#{rowNumber.median}" />
			</h:column>
			
			<h:column>
			<f:facet name="header">
			<h:outputText value="quartileThree" />
			</f:facet>
			<h:outputText value="#{rowNumber.q3}" />
			</h:column>
			
			<h:column>
			<f:facet name="header">
			<h:outputText value="Range" />
			</f:facet>
			<h:outputText value="#{rowNumber.range}" />
			</h:column>
			<h:column>
			<f:facet name="header">
			<h:outputText value="InterquartileRange" />
			</f:facet>
			<h:outputText value="#{rowNumber.interquartileRange}" />
			</h:column>
			
			</t:dataTable>
			
			</div>
			

		<hr />
		<br />
		<br />
		<h:form>
			<h:commandButton id="process" value="Export Data"
				action="#{actionBeanMath.export}" />
		</h:form>
			
	</f:view>
	

</body>
</html>