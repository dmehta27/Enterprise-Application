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
		<h3 align="center">Graphical Analysis</h3>
		<hr />
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
		<hr />
		<br />
			<h:form>
				<h:outputText value="#{messageBean.errorMessage}" rendered="#{messageBean.renderErrorMessage}" />
				<h:outputText value="#{messageBean.successMessage}" rendered="#{messageBean.renderSuccessMessage}" />
					<h:panelGrid columns="6">
					<h:commandButton type="submit" value="Table List"
						action="#{dbActionBean.listTables}" />
					<h:commandButton id="get" value="Get Graphical Columns"
						action="#{dbActionBean.listColumnType}" />
					<h:commandButton type="submit" value="Generate Regression Analysis"
						action="#{actionBeanGraph.calculateRegressionVariables}" />
					
					<h:commandButton type="submit"
						value="GraphicalAnalysis"
						action="#{actionBeanGraph.generateGraph}" />
					
						<br />			
			</h:panelGrid>
			<h:panelGrid columns="150">
				<h:selectOneListbox value="#{dbActionBean.tableName}" rendered="#{dbActionBean.tableListRendered}">
						<f:selectItems value="#{dbActionBean.tableViewList}" />
					</h:selectOneListbox>

			
				
				<h:selectOneListbox id="predictor"
					value="#{actionBeanGraph.predictorValue}"
					rendered="#{dbActionBean.columnListRendered}" size="5">
					<f:selectItem itemValue="0" itemLabel="Select Predictor Value" />
					<f:selectItems value="#{dbActionBean.columnNames}" />
				</h:selectOneListbox>
				<h:selectOneListbox id="response"
					value="#{actionBeanGraph.responseValue}"
					rendered="#{dbActionBean.columnListRendered}" size="5">
					<f:selectItem itemValue="0" itemLabel="Select Response Value" />
					<f:selectItems value="#{dbActionBean.columnNames}" />
				</h:selectOneListbox>
								
				
				
			</h:panelGrid>
			<br />
			<hr />
		</h:form>	
			
			
		<h:form>
			<div
				style="background-attachment: scroll; overflow: auto; height: auto; background-repeat: repeat"
				align="center">
				<h:graphicImage value="#{actionBeanGraph.xYchartPath}"
					height="450" width="600" 
					rendered="#{actionBeanGraph.renderXYChart}" alt="XY Series" />

			</div>
		</h:form>
		
		<h:form> 
				
				
				
				
				<h:outputText value="Final Regression Equation: "
				    		rendered="#{actionBeanGraph.renderRegAnalysis}">
						</h:outputText> &#160;
						<h:outputText value="#{actionBeanGraph.rEquation}"
							rendered="#{actionBeanGraph.renderRegAnalysis}">
						</h:outputText> <br /><br />
						<h:outputText value="Regression Model" rendered="#{actionBeanGraph.renderRegAnalysis}"></h:outputText>
						<h:panelGrid columns="5" rendered="#{actionBeanGraph.renderRegAnalysis}" border="1" >
							<h:outputText value="Predictor"/>
							<h:outputText value="Co-efficient"/>
							<h:outputText value="Standard Error Co-efficient"/>
							<h:outputText value="T-Statistic"/>
							<h:outputText value="P-Value"/>
							<h:outputText value="Constant"/>
							<h:outputText value="#{actionBeanGraph.intercept}"/>
							<h:outputText value="#{actionBeanGraph.interceptStandardError}"/>
							<h:outputText value="#{actionBeanGraph.tStatistic }"/>
							<h:outputText value="#{actionBeanGraph.interceptPValue }"/>
							<h:outputText value="#{actionBeanGraph.predictorValue}"/>
							<h:outputText value="#{actionBeanGraph.slope}"/>
							<h:outputText value="#{actionBeanGraph.slopeStdError}"/>
							<h:outputText value="#{actionBeanGraph.tStatisticpredict }"/>
							<h:outputText value="#{actionBeanGraph.pValuePredictor }"/>
						</h:panelGrid> <br/> <br/>
						<h:panelGrid columns="2" rendered="#{actionBeanGraph.renderRegAnalysis}" border="1">
							<h:outputText value="Model Standard Error:"/>
							<h:outputText value="#{actionBeanGraph.standardErrorModel}"/>
							<h:outputText value="R Square(Co-efficient of Determination)"/>
							<h:outputText value="#{actionBeanGraph.rSqr}"/>
							<h:outputText value="Adjusted R Square(Co-efficient of Determination)"/>
							<h:outputText value="#{actionBeanGraph.rSquareAdjusted}"/>
						</h:panelGrid> <br/> <br/>
						<h:outputText value="Analysis of Variance" rendered="#{actionBeanGraph.renderRegAnalysis}"/> <br/>
						<h:panelGrid columns="6" rendered="#{actionBeanGraph.renderRegAnalysis}" border="1" >
							<h:outputText value="Source"/>
							<h:outputText value="Degrees of Freedom(DF)"/>
							<h:outputText value="Sum of Squares"/>
							<h:outputText value="Mean of Squares"/>
							<h:outputText value="F-Statistic"/>
							<h:outputText value="P-Value"/>
							<h:outputText value="Regression"/>
							<h:outputText value="#{actionBeanGraph.predictorDF}"/>
							<h:outputText value="#{actionBeanGraph.regressionSumSquares}"/>
							<h:outputText value="#{actionBeanGraph.meanSquare }"/>
							<h:outputText value="#{actionBeanGraph.fValue }"/>
							<h:outputText value="#{actionBeanGraph.pValue}"/>
							<h:outputText value="Residual Error"/>
							<h:outputText value="#{actionBeanGraph.residualErrorDF}"/>
							<h:outputText value="#{actionBeanGraph.sumSquaredErrors }"/>
							<h:outputText value="#{actionBeanGraph.meanSquareError }"/>
							<h:outputText value=""/>
							<h:outputText value=""/>
							<h:outputText value="Total"/>
							<h:outputText value="#{actionBeanGraph.totalDF}"/>
							<h:outputText value="#{actionBeanGraph.totalSumSquares}"/>
						</h:panelGrid>
				
				
				
			</h:form>
	

</f:view>
</body>
</html>