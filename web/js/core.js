$(document).ready(function() {
    //������������� ������� ���������
    //todo ����� ��� �������� ����� ����������� ������
    function initializeTable() {
        $('#torrentTable').dataTable({
            bPaginate:false,
            bInfo:false,
            bJQueryUI: true,
            bLengthChange: false,
            aoColumns: [
                null,//            <th>���</th>
                {bVisible:false},//            <th>hash</th>
                null,//            <th>������</th>
                null,//            <th>&nbsp;</th>
                null,//            <th>�������</th>
                null,//            <th>������</th>
                null,//            <th>�����</th>
                null,//            <th>����</th>
                null//            <th>����</th>

            ],
            "bProcessing": true,
            "sAjaxSource": "mockSource.html"
        });
    }

    //public static void main(null)
    initializeTable();
    $("#dialogBody").tabs();
    $("#dialog").dialog({ modal: false, resizable: false,
        draggable: true, width: 800, height: 500 });
    $.widget("#leftMenu");
});