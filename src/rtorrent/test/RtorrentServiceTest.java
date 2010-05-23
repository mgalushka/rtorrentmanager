package rtorrent.test;

import junit.framework.TestCase;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import org.eclipse.ecf.protocol.bittorrent.TorrentFile;
import rtorrent.service.RtorrentService;
import rtorrent.service.RtorrentServiceException;
import rtorrent.service.RtorrentServiceImpl;
import rtorrent.torrent.ActionTorrent;
import rtorrent.utils.UtilException;

import java.io.File;
import java.util.Set;

/**
 * User: welvet
 * Date: 18.05.2010
 * Time: 20:51:22
 */
public class RtorrentServiceTest extends TestCase {
    File file;
    XmlRpcConnection connection;
    RtorrentService rtorrentService;
    private Boolean aBoolean;
    private String rawData;

    @Override
    protected void setUp() throws Exception {
        file = new File(RtorrentServiceTest.class.getResource("resource/").getPath() + "test.torrent");
        connection = new XmlRpcConnection("serv", 5000);
        rtorrentService = new RtorrentServiceImpl(connection);
//        rtorrentService = new MockRtorrentService();
        if (!(rtorrentService.isAlive()))
            throw new Exception("Rtorrent not run");
    }

    /**
     * ������� ���� �� ����������� ����� getTorrentByHash() �� �� ����� ������������� ������ ����������
     * ������� ����� �������� ����
     *
     * @throws Exception
     */
    public void testService() throws Exception, UtilException {
        ActionTorrent torrent = new ActionTorrent(file);
        TorrentFile torrentFile = new TorrentFile(file);
        //������� �������� �������
        rtorrentService.add(torrent);
        torrent = getOnceTorrent();

        //��������� �������
        rtorrentService.start(torrent);
        torrent = getOnceTorrent();
        assertTrue(torrent.isStart());

        //�������������
        rtorrentService.stop(torrent);
        torrent = getOnceTorrent();
        assertFalse(torrent.isStart());

        //�������� ��� �� ������� �������
        assertTrue(rtorrentService.verify(torrent.getHash()));
        torrent = getOnceTorrent();

        //��������� ����
        assertTrue(torrentFile.getHexHash().toUpperCase().equals(torrent.getHash()));

        //������� � ���������
        rtorrentService.remove(torrent);
        assertFalse(rtorrentService.verify(torrent.getHash()));
    }

    private ActionTorrent getOnceTorrent() throws RtorrentServiceException {
        ActionTorrent torrent;
        Set<ActionTorrent> set = rtorrentService.getSet();
        torrent = set.iterator().next();
        return torrent;
    }
}
