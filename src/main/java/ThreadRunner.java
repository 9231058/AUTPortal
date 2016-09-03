/*
 * Decompiled with CFR 0_115.
 */
package entekhabvahed;

import entekhabvahed.myThread;
import java.util.ArrayList;

public class ThreadRunner {
    public ArrayList<String> lessons = new ArrayList();
    public ArrayList<String> group = new ArrayList();
    public ArrayList<String> courseid = new ArrayList();
    public ArrayList<String> groupno = new ArrayList();
    public ArrayList<String> ips = new ArrayList();
    public ArrayList<String> sessions = new ArrayList();

    public void add(String lesson_code, String group_code, String course_id, String group_no) {
        this.lessons.add(lesson_code);
        this.group.add(group_code);
        this.courseid.add(course_id);
        this.groupno.add(group_no);
    }

    public void addIP(String ip, String sessionID) {
        this.ips.add(ip);
        this.sessions.add(sessionID);
    }

    public void start(boolean read) {
        for (int i = 0; i < this.ips.size(); ++i) {
            for (int j = 0; j < this.lessons.size(); ++j) {
                new myThread(read, this.ips.get(i), this.sessions.get(i), this.lessons.get(j), this.group.get(j), this.courseid.get(j), this.groupno.get(j)).start();
            }
        }
    }
}

