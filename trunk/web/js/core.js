$(document).ready(function() {
    $('#torrentTable').dataTable({
        bPaginate:false,
        bInfo:false,
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

        ]
        //        "bProcessing": true,
        //        "bServerSide": true,
        //        "sAjaxSource": "/?do=tbl"
    });
});