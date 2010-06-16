package rtorrent.service;

import ntorrent.io.rtorrent.*;
import ntorrent.io.rtorrent.System;
import ntorrent.io.xmlrpc.XmlRpcConnection;
import org.apache.log4j.Logger;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import rtorrent.config.Config;
import rtorrent.config.ConfigManager;
import rtorrent.torrent.ActionTorrent;
import rtorrent.torrent.Torrent;
import rtorrent.utils.ContextUtils;
import rtorrent.utils.LoggerSingleton;

import java.io.FileInputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: welvet
 * Date: 18.05.2010
 * Time: 17:22:44
 */
public class RtorrentServiceImpl implements RtorrentService {
    private XmlRpcConnection xmlRpcConnection;
    private Download download;
    private File file;
    private Global global;
    private PeerConnection peerConnection;
    private System system;
    private Tracker tracker;
    private XmlRpcClient client;

    private Logger log = LoggerSingleton.getLogger();

    /**
     * ������������� � ��������� ������.
     */
    private static String[] download_variable = new String[]{
            "main",
            "d.get_name=",
            "d.get_hash=",
            "d.get_directory_base=",
            "d.get_custom1=",
            "d.is_active=",
            "d.is_open=",
            "d.is_hash_checking=",
            "d.is_hash_checked=",
            "d.get_state=",
            "d.get_left_bytes=",
            "d.get_bytes_done=",
            "d.get_size_bytes=",
            "d.get_up_total=",
            "d.get_ratio=",
            "d.get_peers_connected=",
            "d.get_peers_not_connected=",
            "d.get_hashing=",
    };
    private static List<String> list = Arrays.asList(download_variable);

    @Deprecated
    public RtorrentServiceImpl(String host, Integer port) {
        this.xmlRpcConnection = new XmlRpcConnection(host, port);
        initialize();
    }

    public RtorrentServiceImpl() {
        ConfigManager configManager = (ConfigManager) ContextUtils.lookup("rconfig");
        Config config = configManager.getConfig("rtorrent");
        String host = (String) config.getFieldValue("host");
        String port = (String) config.getFieldValue("port");
        this.xmlRpcConnection = new XmlRpcConnection(host, new Integer(port));
        initialize();
    }

    private void initialize() {
        client = xmlRpcConnection.getClient();
        download = xmlRpcConnection.getDownloadClient();
        file = xmlRpcConnection.getFileClient();
        global = xmlRpcConnection.getGlobalClient();
        peerConnection = xmlRpcConnection.getPeerConnectionClient();
        system = xmlRpcConnection.getSystemClient();
        tracker = xmlRpcConnection.getTrackerClient();
        log.info("RtorrentService ���������������");
    }

    /**
     * ��������� ������� ��������������� � ��������. ���� � ��� ��������� ������ ������ ���������� �� �����������.
     *
     * @param torrent ������� ��� ������ �������� ������� ������.
     */
    public synchronized void add(ActionTorrent torrent) throws RtorrentServiceException {
        try {
            FileInputStream fileInputStream = new FileInputStream(torrent.getFile().getFile());
            byte[] b = new byte[(int) torrent.getFile().getFile().length()];
            fileInputStream.read(b, 0, (int) torrent.getFile().getFile().length());
            global.load_raw(b);
            log.debug(torrent + " ������� ��������");
        } catch (Exception e) {
            throw new RtorrentServiceException(e);
        }
    }


    public synchronized void start(Torrent torrent) throws RtorrentServiceException {
        try {
            download.start(torrent.getHash());
            log.debug(torrent + " ������� �������");
        } catch (Exception e) {
            throw new RtorrentServiceException(e);
        }
    }

    public synchronized void stop(Torrent torrent) throws RtorrentServiceException {
        try {
            download.stop(torrent.getHash());
            log.debug(torrent + " ������� ����������");
        } catch (Exception e) {
            throw new RtorrentServiceException(e);
        }
    }

    public synchronized void remove(Torrent torrent) throws RtorrentServiceException {
        try {
            download.erase(torrent.getHash());
            log.debug(torrent + " ������� ������");
        } catch (Exception e) {
            throw new RtorrentServiceException(e);
        }
    }

    public synchronized Boolean verify(String hash) throws RtorrentServiceException {
        try {
            String rtorrentHash = download.get_hash(hash);
            log.debug("������(" + hash + ") ������");
            return hash.equals(rtorrentHash);
        } catch (UndeclaredThrowableException e) {
            log.debug("������(" + hash + ") �� ������");
            return false;
        } catch (Exception e) {
            throw new RtorrentServiceException(e);
        }
    }

    /**
     * �������� ������ ��������� � ���������, ��������� ������ ������ ������� ��� ���������� ������.
     * ����������� ��������������� ����� ����� ������ �� �����������, ����� �� ����������� ������ ��� �������� ��� ��� (����) ����������
     *
     * @return
     * @throws Exception
     */
    public synchronized Set<ActionTorrent> getSet() throws RtorrentServiceException {
        XmlRpcArray xmlRpcArray = null;
        try {
            xmlRpcArray = (XmlRpcArray) client.invoke("d.multicall", list);
        } catch (Exception e) {
            throw new RtorrentServiceException(e);
        }
        Set<ActionTorrent> torrentSet = new HashSet<ActionTorrent>();
        for (Object rowObject : xmlRpcArray) {
            XmlRpcArray rowArray = (XmlRpcArray) rowObject;
            Torrent torrent = new ActionTorrent();
            torrent.setName((String) rowArray.get(0));
            torrent.setHash((String) rowArray.get(1));
            torrent.setDir((String) rowArray.get(2));
            torrent.setLabel((String) rowArray.get(3));
            torrent.setActive((Long) rowArray.get(4));
            torrent.setOpen((Long) rowArray.get(5));
            torrent.setHashChecking((Long) rowArray.get(6));
            torrent.setHashChecked((Long) rowArray.get(7));
            torrent.setStart((Long) rowArray.get(8));
            torrent.setLeftBytes((Long) rowArray.get(9));
            torrent.setBytesDone((Long) rowArray.get(10));
            torrent.setSizeBytes((Long) rowArray.get(11));
            torrent.setUpTotal((Long) rowArray.get(12));
            torrent.setRatio((Long) rowArray.get(13));
            torrent.setPeersConnected((Long) rowArray.get(14));
            torrent.setPeersComplite((Long) rowArray.get(15));
            torrent.setHashing((Long) rowArray.get(16));
            torrentSet.add((ActionTorrent) torrent);
        }
        log.debug("������� ���� ������� ��������");
        return torrentSet;
    }

    /**
     * ���������� �������������
     *
     * @return
     */
    public synchronized Boolean isAlive() {
        return xmlRpcConnection.isConnected();
    }

    public void launch(List<Torrent> list) throws RtorrentServiceException {
        for (Torrent torrent : list) {
            try {
                start(torrent);
            } catch (Exception e) {
                log.debug("�� ����� ������� " + torrent + " ��������� ������: " + e.getMessage());
            }
        }
    }

    public void shutdown(List<Torrent> list) throws RtorrentServiceException {
        for (Torrent torrent : list) {
            try {
                stop(torrent);
            } catch (Exception e) {
                log.debug("�� ����� ��������� " + torrent + " ��������� ������: " + e.getMessage());
            }
        }
    }
}
