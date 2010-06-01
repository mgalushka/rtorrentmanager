package rtorrent.torrent;

/**
 * User: welvet
 * Date: 01.06.2010
 * Time: 19:34:58
 */
public interface TorrentInfo {
    /**
     * ���������� ��� ������� �����
     *
     * @return
     */
    public String getName();

    /**
     * ���������� ��� ������� �����
     *
     * @return
     */
    public String getHash();

    /**
     * ���������� ������ ��������
     *
     * @return
     */
    public State getState();

    /**
     * ���������� ������� ���������� (�������� ��� �����������)
     *
     * @return
     */
    public Integer getPercentage();

    /**
     * ���������� �������� ������ (� ������)
     *
     * @return
     */
    public Long getCompliteSize();

    /**
     * ���������� ������ ������ (� ������)
     *
     * @return
     */
    public Long getFullSize();

    /**
     * ���������� �����
     * @return
     */
    public Float getRatio();

    /**
     * ���������� ����� ���������� �����
     *
     * @return
     */
    public Integer getPeersConnected();

    /**
     * ��������� ����� ���������� �����
     *
     * @return
     */
    public Integer getSids();

    /**
     * ���������� ������ � �������
     * @return
     */
    public Object[] toArray();
}
