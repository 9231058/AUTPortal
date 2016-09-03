/*
 * Decompiled with CFR 0_115.
 */
package entekhabvahed;

import entekhabvahed.EntekhabVahed;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class myThread
extends Thread {
    SSLSocket socket;
    Writer write;
    private boolean read;
    private String host;
    private String sessionId;
    private String lesson_code;
    private String group_code;
    private String course_id;
    private String group_no;

    public myThread(boolean read, String host, String sessionId, String lesson_code, String group_code, String course_id, String group_no) {
        this.read = read;
        this.host = host;
        this.sessionId = sessionId;
        this.lesson_code = lesson_code;
        this.group_code = group_code;
        this.course_id = course_id;
        this.group_no = group_no;
    }

    @Override
    public void run() {
        SSLContext sc = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier(){

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception trustAllCerts) {
            // empty catch block
        }
        do {
            try {
                this.socket = (SSLSocket)sc.getSocketFactory().createSocket();
                this.socket.connect(new InetSocketAddress(Inet4Address.getByName(this.host), 443), 30000);
                this.write = new OutputStreamWriter(this.socket.getOutputStream());
                String qq = "st_reg_course=" + this.lesson_code + "_" + this.group_code + "_" + this.course_id + "_" + this.group_no + "&st_course_add=%D8%AF%D8%B1%D8%B3+%D8%B1%D8%A7+%D8%A7%D8%B6%D8%A7%D9%81%D9%87+%DA%A9%D9%86";
                String post = "POST /aportal/regadm/student.portal/student.portal.jsp?action=apply_reg&st_info=add HTTP/1.1\r\nHost: portal.aut.ac.ir\r\nUser-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\nAccept-Language: en-US,en;q=0.5\r\nAccept-Encoding: gzip, deflate\r\nReferer: https://" + this.host + "/aportal/regadm/student.portal/student.portal.jsp?action=edit&st_info=register&st_sub_info=u_mine_all\r\n" + "Cookie: JSESSIONID=" + this.sessionId + "\r\n" + "Connection: close\r\n" + "Content-Length: " + qq.length() + "\r\n" + "Content-Type: application/x-www-form-urlencoded\r\n" + "\r\n" + qq;
                this.write.write(post);
                this.write.flush();
                if (this.read) {
                    new Thread(){

                        @Override
                        public void run() {
                            try {
                                Scanner reader = new Scanner(myThread.this.socket.getInputStream());
                                while (reader.hasNextLine()) {
                                }
                                myThread.this.write.close();
                                myThread.this.socket.close();
                            }
                            catch (IOException ex) {
                                Logger.getLogger(EntekhabVahed.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }.start();
                } else {
                    this.write.close();
                    this.socket.close();
                }
            }
            catch (Exception qq) {
                // empty catch block
            }
            try {
                Thread.sleep(75);
                continue;
            }
            catch (InterruptedException ex) {
                Logger.getLogger(myThread.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            break;
        } while (true);
    }

}

