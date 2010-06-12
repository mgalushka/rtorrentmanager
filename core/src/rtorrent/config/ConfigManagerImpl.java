package rtorrent.config;

import org.apache.log4j.Logger;
import rtorrent.utils.BindContext;
import rtorrent.utils.InContext;
import rtorrent.utils.LoggerSingleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * User: welvet
 * Date: 05.06.2010
 * Time: 12:10:19
 */
public class ConfigManagerImpl implements ConfigManager, InContext {
    private Set<Config> configs = new HashSet<Config>();
    private File file;
    private static final String EXT = ".cfg";
    private static final String DIR = "configs";
    private Logger log = LoggerSingleton.getLogger();

    public ConfigManagerImpl(File dir) {
        //������� ���������� � ���������
        dir = new File(dir.getAbsolutePath() + "\\" + DIR);
        dir.mkdir();

        this.file = dir;
    }

    public void bindContext() {
        BindContext.bind("rconfig", this);
    }

    private void save(Config config) throws IOException {
        Properties properties = new Properties();
        properties.putAll(config.getFields());
        //��������� ������ �� ��� �����
        String path = file.getAbsolutePath() + "\\" + config.getName() + EXT;
        FileOutputStream stream = new FileOutputStream(path);
        properties.store(stream, null);
        log.debug("������ " + config.getName() + " ��������");
    }

    private Config load(String name) throws IOException {
        Properties properties = new Properties();
        //��������� ������ �� ����� �����
        String path = file.getAbsolutePath() + "\\" + name + EXT;
        FileInputStream stream = new FileInputStream(path);
        properties.load(stream);
        //��������� ����, �����������
        Map map = new HashMap();
        for (Object s : properties.keySet()) {
            map.put(s, properties.get(s));
        }
        Config config = new Config();
        config.setName(name);
        config.setFields(map);
        //��������� ������
        configs.add(config);
        log.debug("������ " + config.getName() + " ��������");
        return config;
    }


    public Config getConfig(String name) {
        for (Config config : configs)
            if (config.getName().equals(name))
                return config;
        //���� ������ ����� �� �������, ������� ����� ��� � ������
        try {
            return load(name);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    public void saveConfig(Config config) {
        configs.add(config);
        try {
            save(config);
        } catch (IOException e) {
            log.error(e);
        }
    }
}
