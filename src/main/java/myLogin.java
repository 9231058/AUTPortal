/*
 * Decompiled with CFR 0_115.
 */
package entekhabvahed;

import entekhabvahed.myThread;
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

public class myLogin {
    SSLSocket socket;
    Writer write;
    private boolean read;
    private String host;
    private String username;
    private String sessionId = "";
    private String password;
    private String captcha = "";
    private String captchaURL = "";

    public myLogin(boolean read, String host, String username, String password) {
        this.read = read;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String getCaptchaURL() {
        return this.captchaURL;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getLastSession() {
        return this.sessionId;
    }

    public String getSession() {
        this.sessionId = "";
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
            this.socket = (SSLSocket)sc.getSocketFactory().createSocket();
            this.socket.connect(new InetSocketAddress(Inet4Address.getByName(this.host), 443), 30000);
            this.write = new OutputStreamWriter(this.socket.getOutputStream());
            String post = "GET /aportal/ HTTP/1.1\r\nHost: " + this.host + "\r\n" + "User-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0\r\n" + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + "Accept-Language: en-US,en;q=0.5\r\n" + "Accept-Encoding: gzip, deflate\r\n" + "Referer: http://portal.aut.ac.ir/\r\n" + "Cookie: JSESSIONID=00000000000000000000000000000000\r\n" + "Connection: close\r\n\r\n";
            this.write.write(post);
            this.write.flush();
            Scanner reader = new Scanner(this.socket.getInputStream());
            String str = "";
            while (!str.contains("</body>") && reader.hasNextLine()) {
                String temp = reader.nextLine();
                str = str + temp + '\n';
            }
            if (str.contains("JSESSIONID=")) {
                this.sessionId = str.substring(str.indexOf("JSESSIONID=") + "JSESSIONID=".length());
                this.sessionId = this.sessionId.substring(0, 32);
            }
            if (str.contains("<img src=")) {
                this.captchaURL = str.substring(str.indexOf("<img src=") + "<img src=".length());
                this.captchaURL = this.captchaURL.substring(1, 94);
                this.captchaURL = "https://" + this.host + "/aportal/" + this.captchaURL;
            }
            this.write.close();
            this.socket.close();
        }
        catch (Exception ex) {
            Logger.getLogger(myThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.sessionId;
    }

    public boolean login() {
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
            this.socket = (SSLSocket)sc.getSocketFactory().createSocket();
            this.socket.connect(new InetSocketAddress(Inet4Address.getByName(this.host), 443), 30000);
            this.write = new OutputStreamWriter(this.socket.getOutputStream());
            String qq = "username=" + this.username + "&password=" + this.password + "&passline=" + this.captcha + "&login=%D9%88%D8%B1%D9%88%D8%AF+%D8%A8%D9%87+%D9%BE%D9%88%D8%B1%D8%AA%D8%A7%D9%84";
            String post = "POST /aportal/login.jsp HTTP/1.1\r\nHost: " + this.host + "\r\n" + "User-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0\r\n" + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" + "Accept-Language: en-US,en;q=0.5\r\n" + "Accept-Encoding: gzip, deflate\r\n" + "Referer: https://portal.aut.ac.ir/aportal/\r\n" + "Cookie: JSESSIONID=" + this.sessionId + "\r\n" + "Connection: close\r\n" + "Content-Length: " + qq.length() + "\r\n" + "Content-Type: application/x-www-form-urlencoded\r\n" + "\r\n" + qq;
            this.write.write(post);
            this.write.flush();
            Scanner reader = new Scanner(this.socket.getInputStream());
            String str = "";
            while (reader.hasNextLine()) {
                if (!(str = str + reader.nextLine() + '\n').contains("<img src=\"PassImageServlet")) continue;
                reader.close();
                this.write.close();
                this.socket.close();
                return false;
            }
            this.write.close();
            this.socket.close();
            if (str.contains("<img src=\"PassImageServlet")) {
                return false;
            }
        }
        catch (Exception ex) {
            Logger.getLogger(myThread.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

}

