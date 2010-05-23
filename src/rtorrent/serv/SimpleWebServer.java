/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of Mini Wegb Server / SimpleWebServer.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: ServerSideScriptEngine.java,v 1.4 2004/02/01 13:37:35 pjm2 Exp $

*/

package rtorrent.serv;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Copyright Paul Mutton
 * http://www.jibble.org/
 *
 */
public class SimpleWebServer extends Thread {

    public static final String VERSION = "SimpleWebServer  http://www.jibble.org/";
    public static final Hashtable MIME_TYPES = new Hashtable();
    private RequestParser requestParser;

    static {
        String image = "image/";
        MIME_TYPES.put(".gif", image + "gif");
        MIME_TYPES.put(".jpg", image + "jpeg");
        MIME_TYPES.put(".jpeg", image + "jpeg");
        MIME_TYPES.put(".png", image + "png");
        String text = "text/";
        MIME_TYPES.put(".html", text + "html");
        MIME_TYPES.put(".htm", text + "html");
        MIME_TYPES.put(".txt", text + "plain");
    }

    protected SimpleWebServer(String host, int port, File rootDir, RequestParser requestParser) throws IOException {
        this.requestParser = requestParser;
        InetAddress in = InetAddress.getByName(host);
        _rootDir = rootDir.getCanonicalFile();
        if (!_rootDir.isDirectory()) {
            throw new IOException("Not a directory.");
        }
        _serverSocket = new ServerSocket(port, 0, in);
        start();
    }

    public void run() {
        while (_running) {
            try {
                Socket socket = _serverSocket.accept();
                RequestThread requestThread = new RequestThread(socket, _rootDir, requestParser);
                requestThread.start();
            }
            catch (IOException e) {
                System.exit(1);
            }
        }
    }

    // Work out the filename extension.  If there isn't one, we keep
    // it as the empty string ("").
    public static String getExtension(File file) {
        String extension = "";
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        if (dotPos >= 0) {
            extension = filename.substring(dotPos);
        }
        return extension.toLowerCase();
    }

    private File _rootDir;
    private ServerSocket _serverSocket;
    private boolean _running = true;

}