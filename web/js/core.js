//������������� ����������� ��������
var torrents = [];
var selectedTorrent;
var oTable;

//������������� ������� ���������
//todo ����� ��� �������� ����� ����������� ������
function initializeTable() {
    oTable = $('#torrentTable').dataTable({
        bPaginate:false,
        bInfo:false,
        bJQueryUI: true,
        aoColumns: [
            //������������� ����
            {bVisible: false},
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ],
        bLengthChange: false,
        fnRowCallback: rowCallback, //������ ������� ��� ������������ ����
        "bProcessing": true,
        "sAjaxSource": "mockSource.html"
    });
}

function initializeButtons() {
    //������� ������
    //������� ������ ������ ��� ���� ��������
    $(".button").each(function() {
        $(this).button();
    })
    //������� ������ ������ ��� ���� ��������
    $(".closeButton").each(function() {
        $(this).click(function() {
            $(this).parents(".dialog").dialog("close");
        })
    })
}

//������� ��� ��������� �����
function rowCallback(nRow, aData, iDisplayIndex) {
    //��������� ������� � ������
    torrents.push(nRow);
    $(nRow).contextMenu({
        menu: 'contextMenu'
    },
            function(action, el, pos) {
                //��� �������� ����� ���� �� �������. ������� ����� ���� doStart(hash);
                if (selectedTorrent != undefined) {
                    doAction(action, selectedTorrent);
                }
                else {
                    alert("�������� �������");
                }
            });
    //������������� �������� �� ��������� ����
    $(nRow).attr("hash", aData[0]);
    //������������� ���������
    $(nRow).mousedown(function() {
        for (var i = 0; i <= torrents.length; i++) {
            var sel = torrents[i];
            $(sel).removeClass("rowSelected");
        }
        $(this).addClass("rowSelected");
        selectedTorrent = $(this).attr("hash");
    });
    var img = $(nRow).children().get(1);
    //������������� ����������� ��� ������� ��������
    $(img).html("<img src=\"images/" + aData[2] + ".jpg\"/>");
    //��������������� ��������� ������� ����� ���������� todo � ������� �������� ������ � selectedTorrent
    if ((aData[0] == selectedTorrent) && (selectedTorrent != undefined))
        $(nRow).addClass("rowSelected");
    return nRow;
}

//������� ������ � �����������
function openSettingsDialog() {
    $("#settingsDialogBody").tabs();
    $("#settingsDialog").dialog({ modal: false, resizable: false,
        draggable: true, width: 800, height: 500 });
}

//������� ������ � ����������� ��������
function openTorrentSettingsDialog() {
    $("#torrentDialog").dialog({ modal: false, resizable: false,
        draggable: true, width: 500, height: 400 });
}

//������� ������ � ������ ������ todo �������� �� ����� ��������� � ��������� �������������� �� ������������

function reloadTable() {
    //todo ��������� ������� � ��������� ������ 10000 �������, �������� ����� ������� ��� �������� �������������
    oTable.everyTime(10000, "table", function() {
        //�������� ������ � ���������� � ��������� ��� � �������
        torrents = [];
        oTable.fnReloadAjax(oTable.fnSettings());
    });
}

//��� ������� ����� ���������� ��� �� ������������ ����, ��� � ����� "������ ����������"
function doAction(action, hash) {
    //        $.getJSON("/"+action+"/"+hash+"/", {}, function(json) { //����������� ������ http://localhost/action/hash/
    $.getJSON("mockJSON.html", {}, function(json) {
        if (json.needUserNotice == "true") { //������ ������ ��� ��������� � ""
            //��������� ������
            var dialog = openTorrentSettingsDialog();
            //���������� ���������
        }
    }); //
}

//public static void main(null)
$(document).ready(function() {
    initializeTable();
    initializeButtons();
    reloadTable();
});