package rtorrent.torrent.set;

import rtorrent.torrent.ActionTorrent;
import rtorrent.torrent.TorrentValidateException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: welvet
 * Date: 19.05.2010
 * Time: 22:02:14
 */
public class TorrentHashtable extends ConcurrentHashMap<String, ActionTorrent> {
    /**
     * ��������� ������� � ���� �� ������ ���� �� �����
     * @param torrent
     * @throws rtorrent.torrent.TorrentValidateException
     */
    public void update(ActionTorrent torrent) throws TorrentValidateException {
        this.remove(torrent.getHash());
        torrent.updateHashFromFile();
        this.put(torrent.getHash(), torrent);
    }

}
