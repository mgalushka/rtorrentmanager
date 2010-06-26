package rtorrent.notice;

/**
 * User: welvet
 * Date: 12.06.2010
 * Time: 21:31:31
 */
public enum TorrentNotice {
    UPDATE("update"), //��������
    FINISH("done"); //������

    private String name;

    TorrentNotice(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
