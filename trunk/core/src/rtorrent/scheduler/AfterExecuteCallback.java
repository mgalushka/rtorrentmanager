package rtorrent.scheduler;

import rtorrent.torrent.set.TorrentSetSingleton;
import rtorrent.utils.LoggerSingleton;

/**
 * User: welvet
 * Date: 14.06.2010
 * Time: 20:40:14
 */
public class AfterExecuteCallback {
    private static Boolean use = false;

    static void setUse(Boolean use) {
        AfterExecuteCallback.use = use;
    }

    public static void afterExecute() {
        if (use) {
            //��������� ��������
            TorrentSetSingleton.getInstance().launch();
            //�������������� ��������
            CheckStrategy.setNeedCheck(true);
            //��������� ����
            use = false;
            LoggerSingleton.getLogger().debug("AfterExecuteCallback ��������");
        }

    }
}
