<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Rtorrent manager</title>
    <script type="text/javascript" language="javascript" src="js/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" language="javascript" src="js/jquery-ui-1.8.1.custom.min.js"></script>
    <script type="text/javascript" language="javascript" src="js/jquery.timers-1.2.js"></script>
    <script type="text/javascript" language="javascript" src="js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" language="javascript" src="js/jquery.contextMenu.js"></script>
    <script type="text/javascript" language="javascript" src="js/core.js"></script>
    <style type="text/css">
        @import "css/jquery.contextMenu.css";
        @import "css/jquery-ui-1.8.1.custom.css";
        @import "css/core.css";

    </style>
</head>
<body>
<!--блок с таблицей-->
<div id="tableConatainer">
    <table id="torrentTable">
        <thead>
        <tr>
            <th>hash</th>
            <th id="titleTd">Имя</th>
            <th>Статус</th>
            <th>&nbsp;</th>
            <th>Скачано</th>
            <th>Размер</th>
            <th>Ратио</th>
            <th>Пиры</th>
            <th>Сиды</th>
        </tr>
        </thead>
    </table>
</div>
<!--блок контекстного меню-->
<ul id="contextMenu">
    <li><a href="#start">Запустить</a></li>
    <li><a href="#stop">Остановить</a></li>
    <li><a href="#remove">Удалить</a></li>
    <li><a href="#properties">Свойства</a></li>
</ul>
<!--блок диалога с настройками торрента-->
<div id="torrentDialog" title="Настройки торрнта">
    <!--todo необходимо разработать логику вебсервера перед реализацией данного диалога-->
</div>
<!--блок диалога с настройками-->
<!--дебаг-->
<button onclick="openSettingsDialog();">Remove me</button>
<!--/дебаг-->
<div id="settingsDialog" class="dialog" title="Настройки">
    <form class="settingsForm" action="http://serv:8080/">
        <div id="settingsDialogBody">
            <!--блок с заголовками-->
            <ul>
                <li><a href="#tabs-1">Общие настройки</a></li>
            </ul>
            <!--блок с настройками-->
            <div id="tabs-1">
                <label>Адрес: <input type="text" name="ip"/></label><br/>
                <label>Порт: <input type="text" name="port"/></label>
            </div>
            <div id="buttons">
                <button class="button submit">ok</button>
                <button class="button closeButton">отмена</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>