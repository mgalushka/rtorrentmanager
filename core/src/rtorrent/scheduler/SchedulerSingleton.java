package rtorrent.scheduler;

import org.apache.log4j.Logger;
import rtorrent.config.Config;
import rtorrent.config.ConfigManager;
import rtorrent.utils.ContextUtils;
import rtorrent.utils.LoggerSingleton;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: welvet
 * Date: 13.06.2010
 * Time: 0:33:58
 */
public class SchedulerSingleton {
    private static final Integer PER_SEC = 1000 * 60;
    private static HashMap<String, Timer> timerHashMap = new HashMap<String, Timer>();
    private static Logger logger = LoggerSingleton.getLogger();

    public static void startDefaultTask() {
        ConfigManager manager = (ConfigManager) ContextUtils.lookup("rconfig");
        Config config = manager.getConfig("scheduler");

        addTask(UpdateNoticeTask.class, new Integer((String) config.getFieldValue("updateNotice")));
        addTask(UpdateSetTask.class, new Integer((String) config.getFieldValue("updateSet")));
        addTask(UpdateTrackers.class, new Integer((String) config.getFieldValue("updateTrackers")));
        addTask(UpdateStrategyTask.class, new Integer((String) config.getFieldValue("updateSet")));
        addTask(DownAndCheckTask.class, new Integer((String) config.getFieldValue("downAndCheck")));
        if ((Boolean) config.getFieldValue("restartWhenClientStop"))
            addTask(RestartWhenClientStop.class, 1);
    }

    public static void addTask(Class<? extends TimerTask> clazz, Integer min) {
        if (timerHashMap.containsKey(clazz.getName())) {
            logger.warn("������ " + clazz.getName() + " ��� ���������");
            return;
        }
        Timer timer = new Timer();
        try {
            timer.scheduleAtFixedRate(clazz.newInstance(), min * PER_SEC, min * PER_SEC);
            timerHashMap.put(clazz.getName(), timer);
            logger.debug("������ " + clazz.getName() + " ���������");
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
