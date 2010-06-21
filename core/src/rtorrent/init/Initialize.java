package rtorrent.init;

import dialog.DialogParser;
import org.apache.log4j.Logger;
import rtorrent.action.ActionManager;
import rtorrent.action.ActionManagerImpl;
import rtorrent.config.ConfigManager;
import rtorrent.config.ConfigManagerImpl;
import rtorrent.control.RtorrentControler;
import rtorrent.control.RtorrentControlerImpl;
import rtorrent.dialog.DialogParserImpl;
import rtorrent.scheduler.SchedulerSingleton;
import rtorrent.service.RtorrentService;
import rtorrent.service.RtorrentServiceImpl;
import rtorrent.torrent.set.TorrentSetSingleton;
import rtorrent.utils.BindContext;
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
        try {
//            todo �������� addTorrent            
//            todo �������� ����������� �� �����
//            todo �������� ����������� �� ���
//            todo �������� standalone ������            
//            todo �������� ����
//            todo �������� �����
//            todo �������� ���� �� google
//            todo ��������� ������
            //������� ������� ����������
            File workDir = new File(System.getProperty("user.home") + "/" + ".rmanager");
            workDir.mkdirs();
            BindContext.bind("workdir", workDir);
            //�������������� �����
            LoggerSingleton.initialize(workDir);
            if (args[0] != null && args[0].equals("debug"))
                LoggerSingleton.debug();
            else {
                //��������� �������
                System.in.close();
                System.err.close();
                System.out.close();
            }
            //�������������� action
            ActionManager actionManager = new ActionManagerImpl();
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
