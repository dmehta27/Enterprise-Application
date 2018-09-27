<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<f:view>
		<h3 align="center">Database Login</h3>
		<hr>
		<br />
		<center>
			
		</center>
		<br />
		<hr>
		<br />
		<center>
			<h:form>
			<h:outputText value="#{messageBean.errorMessage}" rendered="#{messageBean.renderErrorMessage}"  />
				<h:panelGrid columns="2">
				
				
				<h:outputText value="UserName:" />
					<h:inputText id="userName" value="#{dBAccessInfoBean.userName}" />
					<h:outputText value="Password:" />
					<h:inputSecret id="password" value="#{dBAccessInfoBean.password}" />
					<h:outputText value="DB Host:" />
					<h:selectOneMenu value="#{dBAccessInfoBean.dbmsHost}">
						<f:selectItem itemValue="131.193.209.54" itemLabel="131.193.209.54"></f:selectItem>
						<f:selectItem itemValue="131.193.209.57" itemLabel="131.193.209.57"></f:selectItem>
						<f:selectItem itemValue="131.193.209.68" itemLabel="131.193.209.68"></f:selectItem>
						<f:selectItem itemValue="131.193.209.69" itemLabel="131.193.209.69"></f:selectItem>
						<f:selectItem itemValue="localhost" itemLabel="localost"></f:selectItem>						
					</h:selectOneMenu>
					
					<h:outputText value="DB Schema:" />
					<h:selectOneMenu value="#{dBAccessInfoBean.databaseSchema}">
						<f:selectItem itemValue="world" itemLabel="world"></f:selectItem>
						<f:selectItem itemValue="f17x321" itemLabel="f17x321"></f:selectItem>						
					</h:selectOneMenu>
					<h:outputText value="Port Info:" />
					<h:inputText id="portInfo" value="#{dBAccessInfoBean.portInfo}" />
					<h:outputText value="DBMS:" />
					<h:selectOneMenu value="#{dBAccessInfoBean.dbms}">
						<f:selectItem itemValue="MySQL" itemLabel="MySQL" />
						<f:selectItem itemValue="DB2" itemLabel="DB2" />
						<f:selectItem itemValue="Oracle" itemLabel="Oracle" />
					</h:selectOneMenu>
					
					<h:outputText value="" />
					<h:commandButton type="submit" value="Login" action="#{dbmsUserBean.connectDB}" />
				</h:panelGrid>
			</h:form>
		</center>
	</f:view>
</body>
</html>