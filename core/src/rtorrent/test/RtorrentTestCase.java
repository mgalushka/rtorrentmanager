package rtorrent.test;

import junit.framework.TestCase;
import rtorrent.config.ConfigManagerImpl;
import rtorrent.control.RtorrentControlerImpl;
import rtorrent.dialog.DialogParserImpl;
import rtorrent.service.RtorrentService;
import rtorrent.torrent.set.TorrentSet;
import rtorrent.torrent.set.TorrentSetSingleton;
import rtorrent.web.WebServerBuilder;

import javax.naming.InitialContext;
import java.io.File;

/**
 * User: welvet
 * Date: 05.06.2010
 * Time: 13:59:21
 */
public abstract class RtorrentTestCase extends TestCase {
    File dir;
    String warPath;
    File datFile;
    File torrentFile;
    File torrent2File;
    RtorrentService rtorrentService;
    TorrentSet torrentSet;
    RtorrentControlerImpl controler;
    ConfigManagerImpl configManager;
    WebServerBuilder builder;
    String port = "8081";
    String host;
    private Boolean loaded = false;

    @Override
    protected void setUp() throws Exception {
        //��� ���������� ������ ���������� ��������� ������ prepre-test
        datFile = new File(RtorrentTestCase.class.getResource("resource/").getPath() + "torrents.dat");
        torrentFile = new File(RtorrentTestCase.class.getResource("resource/").getPath() + "test.torrent");
        torrent2File = new File(RtorrentTestCase.class.getResource("resource/").getPath() + "test2.torrent");
        //������ �� ���� ����������
        dir = new File(System.getProperty("java.io.tmpdir"));
        //����� �� �������� ������
        //todo ���������� �� ���� ���
        warPath = "C:\\rtorrentmanager\\out\\rtorrentmanager\\web.war";
        //��������� �������
        port = "8081";
        host = "127.0.0.1";
        //������� �������� ������
        rtorrentService = new MockRtorrentService();
        //������� ��������
        TorrentSetSingleton.initialze(rtorrentService, datFile);
        torrentSet = TorrentSetSingleton.getInstance();
        //������� ���������
        controler = new RtorrentControlerImpl();
        controler.bindContext();
        //������� �������
        configManager = new ConfigManagerImpl(dir);
        configManager.bindContext();
        //��������� ������
        DialogParserImpl dialogParser = new DialogParserImpl();
        dialogParser.bindContext();
        //������� ��� ������
        builder = new WebServerBuilder();
        builder.setPort(port);
        builder.setHost(host);
        builder.setWar(warPath);
    }

    @Override
    protected void tearDown() throws Exception {
        //������� ��������
        InitialContext context = new InitialContext();
        context.unbind("rcontroler");
        context.unbind("rconfig");
    }
}
