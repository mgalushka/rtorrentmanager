package rtorrent.notice;

import rtorrent.torrent.ActionTorrent;

/**
 * User: welvet
 * Date: 12.06.2010
 * Time: 21:48:30
 */
public interface NoticeService {
    /**
     * @param torrent �������, � ������� ��������� ���������
     * @param notice ��� ���������
     */
    public void notice(ActionTorrent torrent, TorrentNotice notice);
}
