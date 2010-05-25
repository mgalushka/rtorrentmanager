$(document).ready(function() {
    //������������� ����������� ��������
    var selectedTorrents = [];
    //������������� ������� ���������
    //todo ����� ��� �������� ����� ����������� ������
    function initializeTable() {
        $('#torrentTable').dataTable({
            bPaginate:false,
            bInfo:false,
            bJQueryUI: true,
            aoColumns: [      //������������� ����
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
    //������� ��� ��������� �����
    function rowCallback(nRow, aData, iDisplayIndex) {
        //��������� ������� � ������
        selectedTorrents.push(nRow);
        $(nRow).contextMenu({
            menu: 'contextMenu'
        },
                function(action, el, pos) {
                    //��� �������� ����� ���� �� �������. ������� ����� ���� doStart(hash);
                    alert(action + "(" + $(el).attr("hash") + ")");
                });
        //������������� �������� �� ��������� ����
        $(nRow).attr("hash", aData[0]);
        //������������� ���������
        $(nRow).mousedown(function() {
            for (var i = 0; i <= selectedTorrents.length; i++) {
                var sel = selectedTorrents[i];
                $(sel).removeClass("rowSelected");
            }
            $(this).addClass("rowSelected");
        });
        var img = $(nRow).children().get(1);
        //������������� ����������� ��� ������� ��������
        $(img).html("<img src=\"images/"+aData[2]+".jpg\"/>");
        return nRow;
    }

    //������� ������ � �����������
    function openSettingsDialog() {
        $("#dialogBody").tabs();
        $("#dialog").dialog({ modal: false, resizable: false,
            draggable: true, width: 800, height: 500 });
        $.widget("#leftMenu");
    }


    //public static void main(null)
    initializeTable();
});