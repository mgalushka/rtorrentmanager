package rtorrent.tracker.rutracker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import rtorrent.tracker.HttpHelper;
import rtorrent.tracker.TrackerException;
import rtorrent.tracker.WorkSaver;
import rtorrent.utils.XpathUtils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * User: welvet
 * Date: 14.06.2010
 * Time: 0:45:20
 */
public class RuTrackerHelper extends HttpHelper {
    private static final String RUTRACKER_ORG = "rutracker.org";
    private static final String loginUrl = "http://login." + RUTRACKER_ORG + "/forum/login.php";
    private static final String HOST = "http://" + RUTRACKER_ORG + "/";
    private static final String THEME = "http://" + RUTRACKER_ORG + "/forum/viewtopic.php?t=";
    private static final String DOWNLOAD = "http://dl." + RUTRACKER_ORG + "/forum/dl.php?t=";
    private WorkSaver saver;

    public RuTrackerHelper(String login, String password, File workDir) {
        super(login, password, workDir);
        //���������� ������

        //��������� ����������� ���� � ���������� ������
        saver = new WorkSaver(new File(workDir.getAbsolutePath() + "/rutracker.dat"));
        torrentsMap = saver.load();
    }

    public void auth() throws TrackerException {
        try {
            //������� ���� ����� ������������
            httpClient.getCookieStore().clear();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(loginUrl));
            //���������� ��������� ��� �����������
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("login_username", login));
            formparams.add(new BasicNameValuePair("login_password", password));
            formparams.add(new BasicNameValuePair("autologin", "on"));
            //������ ����� �� ��� ������, �� ���������� "????" (����)
            formparams.add(new BasicNameValuePair("login", "%C2%F5%EE%E4"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "windows-1251");
            //���������� �����
            httpPost.setEntity(entity);
            httpPost.addHeader("Referer", loginUrl);

            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            //��������� ����� � null
            while (true) {
                String ln = reader.readLine();
                if (ln == null)
                    break;
            }
            log.debug("������� ����������� �� rutracker ������ �������");
        } catch (Exception e) {
            throw new TrackerException(e);
        }
    }

    /**
     * @param cookie ������ � ����� bb_data. ����� ��������� ����������� ��������� ���� ��������, ����� �� ������� ����������� � ��������
     *               todo �������� ���������� ������ �� ����������� ����� �� ��, �� ������ ���� �������� ������� ���� ����� ������������
     */
    public void setRemoteCookie(String cookie) {
        BasicClientCookie netscapeCookie = new BasicClientCookie("bb_data", cookie);
        netscapeCookie.setVersion(0);
        netscapeCookie.setDomain("." + RUTRACKER_ORG);
        netscapeCookie.setPath("/");
        httpClient.getCookieStore().addCookie(netscapeCookie);
    }

    /**
     * @return ���� ��� ����������� � ��������
     */
    public String getCookie() {
        for (Cookie cookie : httpClient.getCookieStore().getCookies()) {
            if (cookie.getName().equals("bb_data"))
                return cookie.getValue();
        }
        return null;
    }

    public Boolean checkAuth() throws TrackerException {
        XpathUtils utils = null;
        try {
            HttpGet httpGet = new HttpGet(new URI(HOST + "forum/index.php"));
            BasicHttpResponse response = (BasicHttpResponse) httpClient.execute(httpGet);
            //������� ���������
            utils = new XpathUtils(response.getEntity().getContent());

            return !login.isEmpty() && login.equals(utils.doXPath("//DIV[@class=\"topmenu\"]//B[@class=\"med\"]/text()"));
        } catch (Exception e) {
            log.warn("�� ������� ���������������� �� rutracker.org. ����������� � ��� �����");
            if (utils != null) {
                try {
                    log.info(utils.getFile());
                } catch (Exception e1) {
                    throw new TrackerException(e);
                }
            }
            throw new TrackerException(e);
        }
    }

    //true ���� ����� ������

    public Boolean checkTorrent(String url) throws TrackerException {
        try {
            HttpGet httpGet = new HttpGet(new URI(THEME + url));
            BasicHttpResponse response = (BasicHttpResponse) httpClient.execute(httpGet);
            XpathUtils utils = new XpathUtils(response.getEntity().getContent());

            String string = torrentsMap.get(url);
            String date = utils.doXPath("//DIV[@id=\"tor-reged\"]/TABLE/TBODY/TR[2]/TD[2]/SPAN/text()");

            if (date == null) {
                log.warn("�� ������� ��������� ������� � url:" + url + ". ����������� � ��� �����");
                log.info(utils.getFile());
                throw new TrackerException("������ �������� �����");
            }

            torrentsMap.put(url, date);
            saver.save(torrentsMap);

            if (string != null) {
                log.debug("������� � url " + url + " ��������");
                //���� ������ �� ���������, �� ����� ���������
                return !string.equals(date);
            }

            log.info("������� " + url + " �� ������ � ������");
            return true;
        } catch (Exception e) {
            throw new TrackerException(e);
        }
    }

    /**
     * @param url ��������� ����� � ��� ���� ��������
     * @return ������� ����, ���� null ���� �� ����� ������� ���� ������
     *         todo ���������� ��������� ��� �����, ����� ��� ��� �������� ��� rtorrent
     */
    public File downloadFile(String url) throws TrackerException {
        try {
            //�������� ��������
            BasicClientCookie netscapeCookie = new BasicClientCookie("bb_dl", url);
            netscapeCookie.setVersion(0);
            netscapeCookie.setDomain("." + RUTRACKER_ORG);
            netscapeCookie.setPath("/");
            httpClient.getCookieStore().addCookie(netscapeCookie);

            HttpGet httpGet = new HttpGet(new URI(DOWNLOAD + url));
            BasicHttpResponse response = (BasicHttpResponse) httpClient.execute(httpGet);

            File file = new File(workDir.getAbsolutePath() + "/" + System.currentTimeMillis() + url + ".torrent");
            OutputStream oStream = new FileOutputStream(file);
            response.getEntity().writeTo(oStream);
            oStream.close();

            return file;
        } catch (Exception e) {
            torrentsMap.remove(url);
            saver.save(torrentsMap);
            throw new TrackerException(e);
        }
    }
}
