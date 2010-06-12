package rtorrent.init;

import dialog.DialogParser;
import org.apache.log4j.Logger;
import rtorrent.config.ConfigManager;
import rtorrent.config.ConfigManagerImpl;
import rtorrent.control.RtorrentControler;
import rtorrent.control.RtorrentControlerImpl;
import rtorrent.dialog.DialogParserImpl;
import rtorrent.notice.LogNoticeService;
import rtorrent.notice.NoticeObserverSingleton;
import rtorrent.notice.NoticeService;
import rtorrent.scheduler.SchedulerSingleton;
import rtorrent.service.RtorrentService;
import rtorrent.service.RtorrentServiceImpl;
import rtorrent.torrent.set.TorrentSetSingleton;
import rtorrent.utils.LoggerSingleton;
import rtorrent.web.WebServerBuilder;

import java.io.File;
import java.io.IOException;

/**
 * User: welvet
 * Date: 29.05.2010
 * Time: 19:31:36
 */
public class Initialize {
    private static Logger log = LoggerSingleton.getLogger();

    public static void main(String[] args) throws InterruptedException, IOException {
        //todo �� �������� ��������
        //todo ������� ������ ����������, ���� ��������� �������
        try {        //��������� �������
//        System.in.close();
//        System.out.close();
        //������� ������� ����������
        File workDir = new File(System.getProperty("user.home") + "/" + ".rmanager");
        workDir.mkdirs();
        //�������������� �������
        DialogParser parser = new DialogParserImpl();
        //�������������� �������
        ConfigManager manager = new ConfigManagerImpl(workDir);
        //�������������� �������� ������
        RtorrentService service = new RtorrentServiceImpl();
        //�������������� ��������
        TorrentSetSingleton.initialze(service, workDir);
        //�������������� ���������
        RtorrentControler controler = new RtorrentControlerImpl();
        //������������ ������
        NoticeService noticeService = new LogNoticeService();
        NoticeObserverSingleton.registerService(noticeService);
        //������������ �������
//        TorrentWorkersObserverSingleton.registerWorker();
        //��������� �������
        SchedulerSingleton.startDefaultTask();
        //��������� ��� ������
        WebServerBuilder builder = new WebServerBuilder();
        builder.build();
        //��������� �������� ���� � ����
        while (true) {
            Thread.sleep(1000);
        }
        } catch (Exception e) {
            log.error(e);
        }

    }
}
