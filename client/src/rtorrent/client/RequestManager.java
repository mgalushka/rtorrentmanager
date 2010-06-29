package rtorrent.client;

import java.io.Serializable;

/**
 * User: welvet
 * Date: 27.06.2010
 * Time: 22:08:48
 */
public class RequestManager {
    private Serializable doRequest(Serializable serializable) {
        try {
            Connector connector = new Connector();
            return connector.request(serializable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @return true ���� ����������
     */
    public Boolean checkTorrent() {
        RequestAction action = new RequestAction();
        action.setName("checkRtorrent");
        return (Boolean) doRequest(action);
    }

    public void switchTorrent() {
        RequestAction action = new RequestAction();
        action.setName("shitchTorrent");
        doRequest(action);
    }

    public String[] getTorrents() {
        RequestAction action = new RequestAction();
        action.setName("torrentArray");
        return (String[]) doRequest(action);
    }

}
