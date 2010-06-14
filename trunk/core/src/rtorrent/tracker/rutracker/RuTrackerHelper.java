package rtorrent.tracker.rutracker;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import rtorrent.utils.LoggerSingleton;
import rtorrent.utils.XpathUtils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: welvet
 * Date: 14.06.2010
 * Time: 0:45:20
 */
public class RuTrackerHelper {
    private String login;
    private String password;
    private Logger log = LoggerSingleton.getLogger();
    private DefaultHttpClient httpClient = new DefaultHttpClient();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.9.0.4) Gecko/2008102920 AdCentriaIM/1.7 Firefox/3.0.4\"";
    private static final String RUTRACKER_ORG = "rutracker.org";
    private static final String loginUrl = "http://login." + RUTRACKER_ORG + "/forum/login.php";
    private static final String HOST = "http://" + RUTRACKER_ORG + "/";
    private static final String THEME = "http://" + RUTRACKER_ORG + "/forum/viewtopic.php?t=";
    private static final String DOWNLOAD = "http://dl." + RUTRACKER_ORG + "/forum/dl.php?t=";
    private static final int TIMEOUT = 20000;
    private Map<String, String> torrentsMap;
    private RuTrackerSaver saver;
    private File workDir;

    public RuTrackerHelper(String login, String password, File workDir) {
        this.login = login;
        this.password = password;
        this.workDir = workDir;
        //���������� ������
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                httpRequest.setHeader(HTTP.USER_AGENT, USER_AGENT);
            }
        });
        //���������� �������
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
        httpClient.setParams(params);
        //��������� ����������� ���� � ���������� ������
        saver = new RuTrackerSaver(new File(workDir.getAbsolutePath() + "/rutracker.dat"));
        torrentsMap = saver.load();
    }

    public void auth() throws RuTrackerException {
        try {
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
            throw new RuTrackerException(e);
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

    public Boolean checkAuth() throws RuTrackerException {
        try {
            HttpGet httpGet = new HttpGet(new URI(HOST + "forum/index.php"));
            BasicHttpResponse response = (BasicHttpResponse) httpClient.execute(httpGet);
            //������� ���������
            XpathUtils utils = new XpathUtils(response.getEntity().getContent());

            if (login.equals(utils.doXPath("//DIV[@class=\"topmenu\"]//B[@class=\"med\"]/text()")))
                return true;
            else {
                log.warn("�� ������� ���������������� �� rutracker.org. ����������� � ��� �����");
                log.info(utils.getFile());
                throw new Exception("������ �����������");
            }
        } catch (Exception e) {
            throw new RuTrackerException(e);
        }
    }

    public Boolean checkTorrent(String url) throws RuTrackerException {
        try {
            HttpGet httpGet = new HttpGet(new URI(THEME + url));
            BasicHttpResponse response = (BasicHttpResponse) httpClient.execute(httpGet);
            XpathUtils utils = new XpathUtils(response.getEntity().getContent());

            String string = torrentsMap.get(url);
            String date = utils.doXPath("//DIV[@id=\"tor-reged\"]//SPAN[@title=\"���������������\"]/text()");

            if (date == null) {
                log.warn("�� ������� ��������� ������� � url:" + url + ". ����������� � ��� �����");
                log.info(utils.getFile());
                throw new RuTrackerException("������ �������� �����");
            }

            if (string != null) {
                log.debug("������� � url " + url + " ��������");
                return string.equals(date);
            }

            torrentsMap.put(url, date);
            saver.save(torrentsMap);
            log.info("������� " + url + " �� ������ � ������");
            return false;
        } catch (Exception e) {
            throw new RuTrackerException(e);
        }
    }

    /**
     * @param url ��������� ����� � ��� ���� ��������
     * @return ������� ����, ���� null ���� �� ����� ������� ���� ������
     *         todo ���������� ��������� ��� �����, ����� ��� ��� �������� ��� rtorrent
     */
    public File downloadFile(String url) throws RuTrackerException {
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
            throw new RuTrackerException(e);
        }
    }
}
