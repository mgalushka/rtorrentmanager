<%@ page import="com.google.gson.Gson" %>
<%@ page language="java" %>
<%@ page contentType="text/html;UTF-8" pageEncoding="UTF-8" %>

<%--отдаем json текст--%>
<div id="dialogBody">
    <form id="dialogForm" action="/settings/" method="POST" class="settingsForm">
        <jsp:useBean id="dialog" scope="request" class="java.lang.String"/>
        <jsp:include page="<%="dialogs/"+dialog%>" flush="true"/>
        <jsp:useBean id="dialogPath" scope="request" class="java.lang.String"/>
        <input name="path" type="hidden" value="<%=dialogPath%>"/>
        <div id="buttons">
            <button class="button submit">ok</button>
            <button class="button closeButton">отмена</button>
        </div>
    </form>
</div>
<jsp:useBean id="dialogTitle" scope="request" class="java.lang.String"/>
<input id="title" type="hidden" value="<%=dialogTitle%>"/>
<input id="needUserNotice" type="hidden" value="true"/>