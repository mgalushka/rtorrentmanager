package rtorrent.config;

import dialog.Dialog;
import dialog.DialogParser;
import dialog.Input;
import org.apache.log4j.Logger;
import rtorrent.utils.BindContext;
import rtorrent.utils.ContextUtils;
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
    private static final String BOOLEAN = "boolean_";

    public ConfigManagerImpl(File dir) {
        //������� ���������� � ���������
        dir = new File(dir.getAbsolutePath() + "/" + DIR);
        dir.mkdir();

        this.file = dir;
        bindContext();
    }

    public void bindContext() {
        BindContext.bind("rconfig", this);
    }

    private void save(Config config) throws IOException {
        Properties properties = new Properties();
        properties.putAll(config.getFields());
        //��������� ������ �� ��� �����
        String path = file.getAbsolutePath() + "/" + config.getName() + EXT;
        FileOutputStream stream = new FileOutputStream(path);
        properties.store(stream, null);
        log.debug("������ " + config.getName() + " ��������");
    }

    private Config load(String name) throws IOException {
        Properties properties = new Properties();
        //��������� ������ �� ����� �����
        String path = file.getAbsolutePath() + "/" + name + EXT;
        FileInputStream stream = new FileInputStream(path);
        properties.load(stream);
        //��������� ����, �����������
        Map map = new HashMap();
        for (Object s : properties.keySet()) {
            String property = (String) properties.get(s);
            if (property.equals(BOOLEAN + "true")) {
                map.put(s, true);
                continue;
            }
            if (property.equals(BOOLEAN + "false")) {
                map.put(s, false);
                continue;
            }
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
        //���� � ��� �� ������� - ������� ��� �� �������
        DialogParser parser = (DialogParser) ContextUtils.lookup("rdialog");

        Config config = new Config();
        Dialog dialog = parser.parse(name);

        config.setName(name);

        for (Input input : dialog.getInputs()) {
            config.setFieldValue(input.getFieldName(), input.getFieldValue());
        }

        saveConfig(config);

        log.info(config + "�������� � ����������� �� ���������");

        return getConfig(name);
    }

    public void saveConfig(Config config) {
        configs.add(config);
        try {
            Config configToSave = prepareToFileConfig(config);
            save(configToSave);
        } catch (IOException e) {
            log.error(e);
        }
    }

    private Config prepareToFileConfig(Config config) {
        Config newConfig = new Config();
        //�������� ��-��
        newConfig.setName(config.getName());
        Map<String, Object> map = config.getFields();
        Map<String, Object> newMap = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            Object val = map.get(key);
            if (val instanceof Boolean) {
                newMap.put(key, ((Boolean) val ? BOOLEAN + "true" : BOOLEAN + "false"));
            } else {
                newMap.put(key, val);
            }
        }
        newConfig.setFields(newMap);
        return newConfig;
    }
}
