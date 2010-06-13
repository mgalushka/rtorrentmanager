package rtorrent.torrent.set;

import rtorrent.service.RtorrentService;
import rtorrent.torrent.ActionTorrent;

import java.util.Set;

/**
 * ����� ������������� � ���� (�������������� ^_^) ������ � �������� �������� � �������� ������
 * <p/>
 * ����� ����� �������� �������� ���������� �-����
 * private Boolean needStart = false; - ����� ������� ��� �������� ����� � ����������
 * private Boolean needStop = false; - ����� ���������� ��� ��������� ����� � ����������
 * (����� ������� ��� ������������ ������� �� ����� ���������� ��� ������ ������ getWatchedSet(),
 * ������� ������ ����� ����� ������������ ������ �� ��������)
 * private Boolean needAdd = true; - ������� ����� �������� ��� ��������� �����. (NB! ������� ������ ����� ���� �� �����)
 * (�� ��������� ����������� ������� ����� ������������� �����������)
 * private Boolean needUpdate = false; - ������� ������ ����� ����� ���� �� �����.
 * private Boolean needDelete = false; - ������� ����� ������ �� ���������. ����� ������� �� ����� ������������ ��� ������ getWatchedSet()
 * ������ ����� � ������ ����� �����, ��� ����������� ������ ��������
 * User: welvet
 * Date: 18.05.2010
 * Time: 17:29:03
 */
public interface TorrentSet {
    /**
     * ��������� ���������� � ���������, � �������� ��� ��������� ��������
     */
    @Deprecated
    public void updateSet();

    /**
     * �������� (��������, ����������, �������) ��� �������� �� ���������
     */
    @Deprecated
    public void updateRtorrent();

    /**
     * ��������
     */
    public void update();

    /**
     * �������� ������ ���� ���������
     *
     * @return
     */
    public Set<ActionTorrent> getSet();

    /**
     * �������� ������ ����������� ���������
     *
     * @return
     */
    public Set<ActionTorrent> getWatchedSet();

    /**
     * �������� ������� ���� �� ����
     *
     * @param hash UPPER ���
     * @return �������� �������
     */
    public ActionTorrent getByHash(String hash);

    /**
     * �������� ��������� �������� � ��������� ��� � �������
     *
     * @param torrent ������� ����, ��� ����������
     */
    public void addOrUpdate(ActionTorrent torrent);

    void setService(RtorrentService service);
}
