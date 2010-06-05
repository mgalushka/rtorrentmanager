package rtorrent.test;

import rtorrent.torrent.ActionTorrent;

import java.util.Date;

/**
 * User: welvet
 * Date: 19.05.2010
 * Time: 21:54:34
 */
public class TorrentSetTest extends RtorrentTestCase {
    private String hash;
    private static final int WAIT_TIME = 3000;

    /**
     * �������� �������� ��������
     *
     * @throws Exception
     */
    public void testSaveLoad() throws Exception {
        ActionTorrent torrent = new ActionTorrent(torrentFile);
        try {
            torrentSet.updateSet();

            torrentSet.addOrUpdate(torrent);

            torrentSet.updateRtorrent();
            torrentSet.updateSet();

            Thread.sleep(WAIT_TIME);

            assertNotNull(torrentSet.getByHash(torrent.getHash()));
        } finally {
            torrent.setNeedDelete(true);
            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();
        }
    }

    /**
     * ��������� update
     *
     * @throws Exception
     */
    public void testUpdate() throws Exception {
        ActionTorrent torrent = new ActionTorrent(torrentFile);
        try {

            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();
            torrentSet.updateSet();

            Thread.sleep(WAIT_TIME / 2);

            torrent = torrentSet.getByHash(torrent.getHash());

            torrent.setFile(torrent2File);
            torrent.setNeedUpdate(true);
            torrent.setLastUpdated(new Date(System.currentTimeMillis()));
            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();
            torrentSet.updateSet();

            Thread.sleep(WAIT_TIME / 2);

            torrent = torrentSet.getByHash(torrent.getTorrentFileHash());
        } finally {
            torrent.setNeedDelete(true);
            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();
            torrentSet.updateSet();
        }
    }

    public void testStartStop() throws Exception {
        ActionTorrent torrent = new ActionTorrent(torrentFile);
        try {
            hash = torrent.getHash();
            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();

            //���� ���� �������� ��� �������
            Thread.sleep(WAIT_TIME);
            torrentSet.updateRtorrent();
            Thread.sleep(WAIT_TIME / 2);
            torrentSet.updateSet();
            Thread.sleep(WAIT_TIME / 2);

            torrent = torrentSet.getByHash(hash);
            assertTrue(torrent.isStart());

            torrent.setNeedStop(true);
            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();

            Thread.sleep(WAIT_TIME);
            torrentSet.updateSet();
            Thread.sleep(WAIT_TIME / 2);

            torrent = torrentSet.getByHash(hash);
            assertFalse(torrent.isStart());
            torrent.setNeedStart(true);

            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();

            Thread.sleep(WAIT_TIME);
            torrentSet.updateSet();

            Thread.sleep(WAIT_TIME / 2);
            torrent = torrentSet.getByHash(hash);
            assertTrue(torrent.isStart());
        } finally {
            torrent.setNeedDelete(true);
            torrentSet.addOrUpdate(torrent);
            torrentSet.updateRtorrent();
        }
    }
}
