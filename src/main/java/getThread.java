/*
 * Decompiled with CFR 0_115.
 */
package entekhabvahed;

import entekhabvahed.EntekhabVahed;
import entekhabvahed.myThread;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

public class getThread
extends Thread {
    SSLSocket socket;
    Writer write;
    private String host;
    private String sessionId;
    private String page;
    ArrayList<info> infos = new ArrayList();

    public getThread(String host, String sessionId, String page) {
        this.host = host;
        this.sessionId = sessionId;
        this.page = page;
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
                this.socket = (SSLSocket)sc.getSocketFactory().createSocket(Inet4Address.getByName(this.host), 443);
                this.write = new OutputStreamWriter(this.socket.getOutputStream());
                String post = "GET /aportal/regadm/student.portal/student.portal.jsp?action=edit&st_info=register&st_sub_info=" + this.page + " HTTP/1.1" + "\r\n" + "Host: 192.168.1.51" + "\r\n" + "User-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0" + "\r\n" + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" + "\r\n" + "Accept-Language: en-US,en;q=0.5" + "\r\n" + "Accept-Encoding: gzip, deflate" + "\r\n" + "Referer: https://192.168.1.51/aportal/regadm/style/menu/menu.student.jsp" + "\r\n" + "Cookie: JSESSIONID=" + this.sessionId + "\r\n" + "Connection: close\r\n\r\n";
                this.write.write(post);
                this.write.flush();
                try {
                    Scanner reader = new Scanner(this.socket.getInputStream());
                    String str = "";
                    while (reader.hasNextLine()) {
                        str = str + reader.nextLine() + "\n";
                    }
                    String temp = "<input class=stdcheckbox type=checkbox id=st_reg_course name=st_reg_course value=";
                    while (str.contains(temp)) {
                        str = str.substring(str.indexOf(temp) + temp.length());
                        String lesson_code = str.substring(0, str.indexOf("_"));
                        str = str.substring(str.indexOf("_") + 1);
                        String group_code = str.substring(0, str.indexOf("_"));
                        str = str.substring(str.indexOf("_") + 1);
                        String course_id = str.substring(0, str.indexOf("_"));
                        str = str.substring(str.indexOf("_") + 1);
                        String group_no = str.substring(0, str.indexOf("></td>"));
                        System.out.println(lesson_code + "_" + group_code + "_" + course_id + "_" + group_no);
                        boolean isEqu = false;
                        for (int i = 0; i < this.infos.size(); ++i) {
                            info j = this.infos.get(i);
                            if (!j.lesson_code.equals(lesson_code) || !j.group_no.equals(group_no) || !j.group_code.equals(group_code) || !j.course_id.equals(course_id)) continue;
                            isEqu = true;
                            break;
                        }
                        if (isEqu) continue;
                        new myThread(false, this.host, this.sessionId, lesson_code, group_code, course_id, group_no).start();
                    }
                    this.write.close();
                    this.socket.close();
                }
                catch (IOException ex) {
                    Logger.getLogger(EntekhabVahed.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            catch (Exception ex) {
                Logger.getLogger(myThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(200);
                continue;
            }
            catch (InterruptedException ex) {
                Logger.getLogger(myThread.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            break;
        } while (true);
    }

    private class info {
        public String lesson_code;
        public String group_code;
        public String course_id;
        public String group_no;

        public info(String lesson_code, String group_code, String course_id, String group_no) {
            this.lesson_code = lesson_code;
            this.group_code = group_code;
            this.course_id = course_id;
            this.group_no = group_no;
        }
    }

}

