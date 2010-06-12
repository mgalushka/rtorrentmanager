package rtorrent.torrent.set.action;

import rtorrent.notice.TorrentNotice;
import rtorrent.torrent.ActionTorrent;
import rtorrent.torrent.TorrentValidateException;
import rtorrent.torrent.set.TorrentSetException;
import rtorrent.utils.LoggableException;

import java.util.Date;

/**
 * User: welvet
 * Date: 09.06.2010
 * Time: 0:48:36
 */
public class Update extends TorrentSetAction {
    public void action(ActionTorrent torrent) {
        if (torrent.isNeedUpdate()) {
            try {
                //��������� ���� �� ���� ��� ������� � ��������
                if (torrent.getTorrentFileHash() == null)
                    throw new TorrentSetException("�� ������ ������ ���� ��� " + torrent);
                //������� ������ �������
                rtorrentService.remove(torrent);
                if (rtorrentService.verify(torrent.getHash())) {
                    throw new TorrentSetException("�� ������� ������� ������ " + torrent);
                }
                //��������� �������
                torrents.update(torrent);
                //��������� ���
                torrent.setNeedStart(true);
                //��������� ����� �������
                rtorrentService.add(torrent);
                if (!rtorrentService.verify(torrent.getTorrentFileHash()))
                    throw new TorrentSetException("�� ������� �������� " + torrent);
                torrent.setLastUpdated(new Date());
                notice(torrent, TorrentNotice.UPDATE);
                log.info(torrent + " ��������");
            } catch (LoggableException e) {
                torrent.setNeedUpdate(true); //�������������� ������ � ������ ������
            } catch (TorrentValidateException e) {
                log.error(e);
                //������������� �������, ���� ��������� ������ ���������
                torrent.setNeedStop(true);
            }
        }
    }
}