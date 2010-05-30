package rtorrent.control;

import java.io.File;
import java.util.Set;

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
    public Set getList();

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
