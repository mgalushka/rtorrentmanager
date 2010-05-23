package rtorrent.torrent.set;

import org.apache.log4j.Logger;
import rtorrent.service.RtorrentService;
import rtorrent.service.RtorrentServiceException;
import rtorrent.torrent.ActionTorrent;
import rtorrent.torrent.TorrentHashtable;
import rtorrent.utils.LoggableException;

import java.util.Date;

class TorrentSetHelper {
    private TorrentHashtable torrents;
    private RtorrentService rtorrentService;
    private Logger log;

    public TorrentSetHelper(TorrentSetImpl torrentSetImpl) {
        torrents = torrentSetImpl.getTorrents();
        rtorrentService = torrentSetImpl.getRtorrentService();
        log = torrentSetImpl.getLog();
    }

    void work() {
        updateStart();
        updateStop();
        updateAdd();
        updateUpdate();
        updateDelete();
    }

    /**
     * ���� � ��������� ������, �� ����� ��������.
     */
    void updateStart() {
        for (ActionTorrent torrent : torrents.values()) {
            if (torrent.isNeedStart()) {
                try {
                    rtorrentService.start(torrent);
                    torrent.setNeedStart(false);
                    log.debug(torrent + " �������");
                } catch (RtorrentServiceException e) {
                    return;    //��� ����� ������������� ����������, ��������� ��-�� ������������ ����� � ��������.
                    // ������������ ������ ���������� � ������������ ����������
                }
            }
        }
    }

    void updateStop() {
        for (ActionTorrent torrent : torrents.values()) {
            if (torrent.isNeedStop()) {
                try {
                    rtorrentService.stop(torrent);
                    torrent.setNeedStop(false);
                    log.debug(torrent + " ����������");
                } catch (RtorrentServiceException e) {
                    return;
                }
            }
        }
    }

    void updateAdd() {
        for (ActionTorrent torrent : torrents.values()) {
            if (torrent.isNeedAdd()) {
                try {
                    rtorrentService.add(torrent);
                    if (!rtorrentService.verify(torrent.getHash()))
                        throw new TorrentSetException("�� ������� �������� " + torrent);
                    log.debug(torrent + " ��������");
                    //������ ���������
                    torrent.setNeedAdd(false);
                    torrent.setNeedStart(true);
                } catch (LoggableException e) {
                    return;
                }
            }
        }
    }

    void updateUpdate() {
        for (ActionTorrent torrent : torrents.values()) {
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
                    //������� ������
                    torrent.setNeedUpdate(false);
                    //��������� �������
                    torrents.update(torrent);
                    //��������� ���
                    torrent.setNeedStart(true);
                    //��������� ����� �������
                    rtorrentService.add(torrent);
                    if (!rtorrentService.verify(torrent.getTorrentFileHash()))
                        throw new TorrentSetException("�� ������� �������� " + torrent);
                    torrent.setLastUpdated(new Date());
                    log.debug(torrent + " ��������");
                } catch (LoggableException e) {
                    torrent.setNeedUpdate(true); //�������������� ������ � ������ ������
                    return;
                }
            }
        }
    }

    void updateDelete() {
        for (ActionTorrent torrent : torrents.values()) {
            if (torrent.isNeedDelete()) {
                try {
                    rtorrentService.remove(torrent);
                    if (rtorrentService.verify(torrent.getHash()))
                        throw new TorrentSetException("�� ������� ������� " + torrent);
                    torrents.remove(torrent.getHash());
                    log.debug(torrent + " ������");
                } catch (LoggableException e) {
                    return;
                }
            }
        }
    }
}