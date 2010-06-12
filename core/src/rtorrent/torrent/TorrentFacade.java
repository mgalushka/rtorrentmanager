package rtorrent.torrent;

import rtorrent.tracker.Tracker;

import java.io.File;

/**
 * User: welvet
 * Date: 12.06.2010
 * Time: 22:23:38
 */
public interface TorrentFacade {
    /**
     * @return ����������, ������ ������� ��� ����������
     */
    public Tracker getTracker();

    /**
     * @param file ���� � ����� ���������
     * @throws TorrentValidateException ������ ��� ��������� �����
     */
    public void setFile(File file) throws TorrentValidateException;

    /**
     * @param needUpdate �������� �������
     */
    public void setNeedUpdate(Boolean needUpdate);
}
