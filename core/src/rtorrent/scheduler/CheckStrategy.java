package rtorrent.scheduler;

import rtorrent.config.Config;
import rtorrent.config.ConfigManager;
import rtorrent.control.RtorrentControler;
import rtorrent.utils.ContextUtils;

/**
 * ��������� ����� �������� �� ������ � ����������� �� ���������
 * �� ������� ������������ ���� ����� ���-�� ���
 * User: welvet
 * Date: 14.06.2010
 * Time: 20:11:23
 */
class CheckStrategy {
    private static Boolean run = true;
    private static Boolean needCheck = true;

    /**
     * @return ������� �� ��������
     */
    static Boolean getRun() {
        ConfigManager manager = (ConfigManager) ContextUtils.lookup("rconfig");
        Config config = manager.getConfig("scheduler");
        //���� ��������� DownAndCheck ���������, �� ������ false ������  
        return (Boolean) config.getFieldValue("downAndCheckNeed") && run;
    }

    /**
     * @param needCheck ����� �� ��������� ��������
     */
    static void setNeedCheck(Boolean needCheck) {
        CheckStrategy.needCheck = needCheck;
    }

    static void check() {
        if (needCheck) {
            RtorrentControler controler = (RtorrentControler) ContextUtils.lookup("rcontroler");
            run = controler.checkAlive();
        }
    }
}
