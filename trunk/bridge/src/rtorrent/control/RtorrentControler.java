package rtorrent.control;

import rtorrent.torrent.TorrentInfo;

import java.io.File;
import java.util.List;

/**
 * User: welvet
 * Date: 30.05.2010
 * Time: 22:52:19
 */
public interface RtorrentControler {
    /**
     * �������� ���� � ��������� ����������
     *
     * @return
     */
    public List<TorrentInfo> getList();

    /**
     * �������� �������
     *
     * @param torrentFile
     */
    public void addTorrent(File torrentFile);

    /**
     * ��������� �������
     *
     * @param hash
     */
    public void startTorrent(String hash);

    /**
     * ���������� �������
     *
     * @param hash
     */
    public void stopTorrent(String hash);

    /**
     * ������� �������
     *
     * @param hash
     */
    public void removeTorrent(String hash);

    /**
     * �������� ��������� ��������
     * ������, �������� isWatching
     *
     * @param hash
     */
    public void configureTorrent(String hash);
}
