package rtorrent.tracker;

import rtorrent.torrent.ActionTorrent;

/**
 * User: welvet
 * Date: 19.05.2010
 * Time: 21:45:31
 */
public interface TrackerService {
    /**
     * ��������� ��� �� ������
     *
     * @return true ���� ���
     * @throws TrackerException
     */
    public Boolean isAlive() throws TrackerException;

    /**
     * ��������� �� �����������, � ��������������, ���� ��� �����
     * TODO �������� ����� ���������� ��� ������
     *
     * @return true ���� ������ ��������������
     * @throws TrackerException
     */
    public Boolean isAuth() throws TrackerException;

    /**
     * ���������, ����� �� �������� ������
     *
     * @param torrent - ������� � �������� �������� ������ �������� needTorrentFileUpdate
     * @return ���������� flase ���� �������� ����������� ��������
     * @throws TrackerException
     */
    public Boolean chechUpdate(ActionTorrent torrent) throws TrackerException;

    /**
     * ��������� �-�� File, needUpdate � ������� �����. ���� ���������� �� ����������, �� ��������� needTorrentFileUpdate � Date �� �����
     * TODO ���������� ������ � ��� ���������, � ����� ���������� �� ������������ ����� ��� �����.
     *
     * @param torrent ����������� �������
     * @return
     */
    public Boolean update(ActionTorrent torrent) throws TrackerException;
}
