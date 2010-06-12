package rtorrent.tracker;

import rtorrent.thread.ThreadQueueSingleton;
import rtorrent.torrent.ActionTorrent;
import rtorrent.torrent.set.TorrentSet;
import rtorrent.torrent.set.TorrentSetSingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * User: welvet
 * Date: 12.06.2010
 * Time: 22:28:38
 */
public class TorrentWorkersObserver implements Runnable {
    public static Map<Trackers, TrackerWorker> workers = new HashMap<Trackers, TrackerWorker>();
    private TorrentSet torrentSet = TorrentSetSingleton.getInstance();

    protected TorrentWorkersObserver() {
    }

    /**
     * @param worker �������������� ������
     */
    public void registerWorker(TrackerWorker worker) {
        workers.put(worker.whoIs(), worker);
    }

    public void clearWorkers() {
        workers.clear();
    }

    public void run() {
        Map<Trackers, TorrentJob> jobMap = new HashMap<Trackers, TorrentJob>();

        for (ActionTorrent torrent : torrentSet.getWatchedSet()) {
            //�������� ������
            Trackers trackers = torrent.getTracker().getTracker();
            TrackerWorker worker = workers.get(trackers);
            //���� ������ ����, �� �������� ��� ������������
            if (worker != null) {
                TorrentJob job = jobMap.get(trackers);
                if (job == null) {
                    //������� ������ � ������������� �� ������ �� �������
                    job = new TorrentJob();
                    job.setWorker(worker);
                    jobMap.put(trackers, job);
                }
                //�������� ������� ��� ���� ������
                job.addTorrent(torrent);
            }
        }

        //��������� ���������� � �������
        for (TorrentJob job : jobMap.values())
            ThreadQueueSingleton.add(job);
    }
}
