package rtorrent.test;

import junit.framework.TestCase;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import rtorrent.action.ActionManagerImpl;
import rtorrent.config.ConfigManagerImpl;
import rtorrent.control.RtorrentControlerImpl;
import rtorrent.dialog.DialogParserImpl;
import rtorrent.notice.NoticeObserverSingleton;
import rtorrent.service.RtorrentService;
import rtorrent.service.RtorrentServiceImpl;
import rtorrent.service.UtorrentService;
import rtorrent.torrent.set.TorrentSet;
import rtorrent.torrent.set.TorrentSetSingleton;
import rtorrent.tracker.TorrentWorkersObserverSingleton;
import rtorrent.utils.BindContext;
import rtorrent.utils.LoggerSingleton;
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
    File datFile;
    File torrentFile;
    File torrent2File;
    RtorrentService rtorrentService;
    TorrentSet torrentSet;
    RtorrentControlerImpl controler;
    ConfigManagerImpl configManager;
    private static Boolean loaded = false;
    static final int WAIT_TIME = 3000;

    @Override
    protected void setUp() throws Exception {
        //��� ���������� ������ ���������� ��������� ������ prepre-test
        datFile = new File(RtorrentTestCase.class.getResource("resource/").getPath());
        torrentFile = new File(RtorrentTestCase.class.getResource("resource/").getPath() + "test.torrent");
        torrent2File = new File(RtorrentTestCase.class.getResource("resource/").getPath() + "test2.torrent");
        //������ �� ���� ����������
        dir = new File(System.getProperty("java.io.tmpdir") + "/rtorrent");
        dir.mkdirs();
        BindContext.bind("workdir", dir);
        //logger
        LoggerSingleton.initialize(dir);
        new ActionManagerImpl();
        //����� �� �������� ������
        //������� �������� ������
        rtorrentService = new MockRtorrentService();
        //������� ��������
        rtorrentService = new RtorrentServiceImpl("serv", 5000);
        rtorrentService = new UtorrentService("127.0.0.1", "8080", "admin", "", "gui");
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
        NoticeObserverSingleton.clearService();
        NoticeObserverSingleton.registerService(MockNoticeService.class);

        TorrentWorkersObserverSingleton.initialize();

        if (!loaded) {
            loaded = true;
            ConsoleAppender appender = new ConsoleAppender(new PatternLayout());
            appender.setThreshold(Priority.DEBUG);
            LoggerSingleton.getLogger().addAppender(appender);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        //������� ��������
        InitialContext context = new InitialContext();
        context.unbind("rcontroler");
        context.unbind("rconfig");
    }
}
