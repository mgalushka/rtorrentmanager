package rtorrent.tracker;

import java.io.Serializable;

/**
 * ����� � ������������ ��-����, ��� ������ ���� ��������������
 * User: welvet
 * Date: 18.05.2010
 * Time: 18:56:24
 */
public interface Tracker extends Serializable{

    /**
     * @return ����� ������, � ������������� ����������
     */
    public Tracker copy();
    public Trackers getTracker();
}
