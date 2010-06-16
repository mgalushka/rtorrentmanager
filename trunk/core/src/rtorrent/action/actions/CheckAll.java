package rtorrent.action.actions;

import rtorrent.action.Action;
import rtorrent.scheduler.DownAndCheckTask;
import rtorrent.scheduler.UpdateTrackers;

/**
 * User: welvet
 * Date: 16.06.2010
 * Time: 20:42:02
 */
public class CheckAll implements Action{
    public void initialize() {
        
    }

    public Object run(Object param) {
        //��������� ��� �����, ��������� ��������� � ���
        new DownAndCheckTask().run();
        new UpdateTrackers().run();
        return null;
    }
}
