package rtorrent.config;

/**
 * User: welvet
 * Date: 05.06.2010
 * Time: 10:52:10
 */
public interface ConfigManager {
    /**
     * @param name ���������� ��� �������
     * @return ������
     */
    public Config getConfig(String name);

    /**
     * �������� ������������ ������
      * @param config
     */
    public void saveConfig(Config config);
}
