/*
 * Decompiled with CFR 0_115.
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class frameMain
        extends JFrame {
    JTextField txtUsername = new JTextField("9331032");
    JTextField txtPassword = new JTextField("");
    JTextField txtIP1 = new JTextField("192.168.1.50");
    JTextField txtIP2 = new JTextField("192.168.1.51");
    JTextField txtIP3 = new JTextField("portal2.aut.ac.ir");
    JTextField txtIP4 = new JTextField("portal.aut.ac.ir");
    JTextField txtCaptcha1 = new JTextField();
    JTextField txtCaptcha2 = new JTextField();
    JTextField txtCaptcha3 = new JTextField();
    JTextField txtCaptcha4 = new JTextField();
    JLabel lblStatus1 = new JLabel("waiting for IP");
    JLabel lblStatus2 = new JLabel("waiting for IP");
    JLabel lblStatus3 = new JLabel("waiting for IP");
    JLabel lblStatus4 = new JLabel("waiting for IP");
    myPanel img1;
    myPanel img2;
    myPanel img3;
    myPanel img4;
    ThreadRunner tr;
    myLogin login1;
    myLogin login2;
    myLogin login3;
    myLogin login4;

    public frameMain() {
        this.img1 = new myPanel();
        this.img2 = new myPanel();
        this.img3 = new myPanel();
        this.img4 = new myPanel();
        this.tr = new ThreadRunner();
        try {
            System.out.println("Loading courses...");
            Scanner sc = new Scanner(new File("course.txt"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Scanner sc2 = new Scanner(line);
                String lcode = sc2.next();
                String lgroup = sc2.next();
                String tcode = "%7BAssistCourseId%7D";
                String tgroup = "%7BAssistGroupNo%7D";
                if (sc2.hasNext()) {
                    tcode = sc2.next();
                    tgroup = sc2.next();
                }
                System.out.println(lcode + "_" + lgroup + "_" + tcode + "_" + tgroup);
                this.tr.add(lcode, lgroup, tcode, tgroup);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.tr.start(true);
        this.setLayout(null);
        this.setSize(700, 300);
        this.txtUsername.setBounds(10, 10, 200, 20);
        this.txtUsername.setEditable(false);
        this.txtPassword.setBounds(10, 30, 200, 20);
        this.txtIP1.setBounds(10, 70, 200, 20);
        this.txtIP2.setBounds(10, 110, 200, 20);
        this.txtIP3.setBounds(10, 150, 200, 20);
        this.txtIP4.setBounds(10, 190, 200, 20);
        this.img1.setBounds(210, 70, 150, 40);
        this.img2.setBounds(210, 110, 150, 40);
        this.img3.setBounds(210, 150, 150, 40);
        this.img4.setBounds(210, 190, 150, 40);
        this.txtCaptcha1.setBounds(370, 70, 200, 20);
        this.txtCaptcha2.setBounds(370, 110, 200, 20);
        this.txtCaptcha3.setBounds(370, 150, 200, 20);
        this.txtCaptcha4.setBounds(370, 190, 200, 20);
        this.lblStatus1.setBounds(580, 70, 150, 20);
        this.lblStatus2.setBounds(580, 110, 150, 20);
        this.lblStatus3.setBounds(580, 150, 150, 20);
        this.lblStatus4.setBounds(580, 190, 150, 20);
        this.add(this.txtUsername);
        this.add(this.txtPassword);
        this.add(this.txtIP1);
        this.add(this.txtIP2);
        this.add(this.txtIP3);
        this.add(this.txtIP4);
        this.add(this.txtCaptcha1);
        this.add(this.txtCaptcha2);
        this.add(this.txtCaptcha3);
        this.add(this.txtCaptcha4);
        this.add(this.img1);
        this.add(this.img2);
        this.add(this.img3);
        this.add(this.img4);
        this.add(this.lblStatus1);
        this.add(this.lblStatus2);
        this.add(this.lblStatus3);
        this.add(this.lblStatus4);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
        this.txtPassword.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    frameMain.this.loadingUser();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        KeyListener key = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    if (e.getSource() == frameMain.this.txtCaptcha1) {
                        new Thread() {

                            @Override
                            public void run() {
                                frameMain.this.lblStatus1.setText("wait");
                                frameMain.this.login1.setCaptcha(frameMain.this.txtCaptcha1.getText());
                                if (frameMain.this.login1.login()) {
                                    for (int j = 0; j < frameMain.this.tr.lessons.size(); ++j) {
                                        new myThread(false, frameMain.this.txtIP1.getText(), frameMain.this.login1.getLastSession(), frameMain.this.tr.lessons.get(j), frameMain.this.tr.group.get(j), frameMain.this.tr.courseid.get(j), frameMain.this.tr.groupno.get(j)).start();
                                        frameMain.this.lblStatus1.setText("Successful! ( " + (j + 1) + " Thread run )");
                                    }
                                } else {
                                    frameMain.this.txtCaptcha1.setText("");
                                    frameMain.this.lblStatus1.setText("FAILED!! wait for session");
                                    frameMain.this.login1.getSession();
                                    frameMain.this.lblStatus1.setText("loading captcha");
                                    String capt = frameMain.this.login1.getCaptchaURL();
                                    try {
                                        frameMain.this.img1.setImage(ImageIO.read(new URL(capt)));
                                    } catch (MalformedURLException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    frameMain.this.lblStatus1.setText("ready!");
                                }
                            }
                        }.start();
                    } else if (e.getSource() == frameMain.this.txtCaptcha2) {
                        new Thread() {

                            @Override
                            public void run() {
                                frameMain.this.lblStatus2.setText("wait");
                                frameMain.this.login2.setCaptcha(frameMain.this.txtCaptcha2.getText());
                                if (frameMain.this.login2.login()) {
                                    for (int j = 0; j < frameMain.this.tr.lessons.size(); ++j) {
                                        new myThread(false, frameMain.this.txtIP2.getText(), frameMain.this.login2.getLastSession(), frameMain.this.tr.lessons.get(j), frameMain.this.tr.group.get(j), frameMain.this.tr.courseid.get(j), frameMain.this.tr.groupno.get(j)).start();
                                        frameMain.this.lblStatus2.setText("Successful! ( " + (j + 1) + " Thread run )");
                                    }
                                } else {
                                    frameMain.this.txtCaptcha2.setText("");
                                    frameMain.this.lblStatus2.setText("FAILED!! wait for session");
                                    frameMain.this.login2.getSession();
                                    frameMain.this.lblStatus2.setText("loading captcha");
                                    String capt = frameMain.this.login2.getCaptchaURL();
                                    try {
                                        frameMain.this.img2.setImage(ImageIO.read(new URL(capt)));
                                    } catch (MalformedURLException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    frameMain.this.lblStatus2.setText("ready!");
                                }
                            }
                        }.start();
                    } else if (e.getSource() == frameMain.this.txtCaptcha3) {
                        new Thread() {

                            @Override
                            public void run() {
                                frameMain.this.lblStatus3.setText("wait");
                                frameMain.this.login3.setCaptcha(frameMain.this.txtCaptcha3.getText());
                                if (frameMain.this.login3.login()) {
                                    for (int j = 0; j < frameMain.this.tr.lessons.size(); ++j) {
                                        new myThread(false, frameMain.this.txtIP3.getText(), frameMain.this.login3.getLastSession(), frameMain.this.tr.lessons.get(j), frameMain.this.tr.group.get(j), frameMain.this.tr.courseid.get(j), frameMain.this.tr.groupno.get(j)).start();
                                        frameMain.this.lblStatus3.setText("Successful! ( " + (j + 1) + " Thread run )");
                                    }
                                } else {
                                    frameMain.this.txtCaptcha3.setText("");
                                    frameMain.this.lblStatus3.setText("FAILED!! wait for session");
                                    frameMain.this.login3.getSession();
                                    frameMain.this.lblStatus3.setText("loading captcha");
                                    String capt = frameMain.this.login3.getCaptchaURL();
                                    try {
                                        frameMain.this.img3.setImage(ImageIO.read(new URL(capt)));
                                    } catch (MalformedURLException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    frameMain.this.lblStatus3.setText("ready!");
                                }
                            }
                        }.start();
                    } else if (e.getSource() == frameMain.this.txtCaptcha4) {
                        new Thread() {

                            @Override
                            public void run() {
                                frameMain.this.lblStatus4.setText("wait");
                                frameMain.this.login4.setCaptcha(frameMain.this.txtCaptcha4.getText());
                                if (frameMain.this.login4.login()) {
                                    for (int j = 0; j < frameMain.this.tr.lessons.size(); ++j) {
                                        new myThread(false, frameMain.this.txtIP4.getText(), frameMain.this.login4.getLastSession(), frameMain.this.tr.lessons.get(j), frameMain.this.tr.group.get(j), frameMain.this.tr.courseid.get(j), frameMain.this.tr.groupno.get(j)).start();
                                        frameMain.this.lblStatus4.setText("Successful! ( " + (j + 1) + " Thread run )");
                                    }
                                } else {
                                    frameMain.this.txtCaptcha4.setText("");
                                    frameMain.this.lblStatus4.setText("FAILED!! wait for session");
                                    frameMain.this.login4.getSession();
                                    frameMain.this.lblStatus4.setText("loading captcha");
                                    String capt = frameMain.this.login4.getCaptchaURL();
                                    try {
                                        frameMain.this.img4.setImage(ImageIO.read(new URL(capt)));
                                    } catch (MalformedURLException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    frameMain.this.lblStatus4.setText("ready!");
                                }
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ("F2".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
                    frameMain.this.loadingUser();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        };
        this.txtCaptcha1.addKeyListener(key);
        this.txtCaptcha2.addKeyListener(key);
        this.txtCaptcha3.addKeyListener(key);
        this.txtCaptcha4.addKeyListener(key);
    }

    public void loadingUser() {
        System.out.println("getCaptcha");
        this.txtUsername.setEditable(false);
        this.txtPassword.setEditable(false);
        String username = this.txtUsername.getText().trim();
        String password = this.txtUsername.getText().trim();
        this.txtIP1.setEditable(false);
        this.txtIP2.setEditable(false);
        this.txtIP3.setEditable(false);
        this.txtIP4.setEditable(false);
        this.txtIP1.setFocusable(false);
        this.txtIP2.setFocusable(false);
        this.txtIP3.setFocusable(false);
        this.txtIP4.setFocusable(false);
        this.txtUsername.setFocusable(false);
        this.txtPassword.setFocusable(false);
        if (this.txtIP1.getText().trim().length() > 0) {
            new Thread() {

                @Override
                public void run() {
                    String ip = frameMain.this.txtIP1.getText().trim();
                    frameMain.this.login1 = new myLogin(true, ip, frameMain.this.txtUsername.getText(), frameMain.this.txtPassword.getText());
                    frameMain.this.lblStatus1.setText("wait for session");
                    frameMain.this.login1.getSession();
                    frameMain.this.lblStatus1.setText("loading captcha");
                    String capt = frameMain.this.login1.getCaptchaURL();
                    try {
                        frameMain.this.img1.setImage(ImageIO.read(new URL(capt)));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frameMain.this.lblStatus1.setText("ready!");
                }
            }.start();
        }
        if (this.txtIP2.getText().trim().length() > 0) {
            new Thread() {

                @Override
                public void run() {
                    String ip = frameMain.this.txtIP2.getText().trim();
                    frameMain.this.login2 = new myLogin(true, ip, frameMain.this.txtUsername.getText(), frameMain.this.txtPassword.getText());
                    frameMain.this.lblStatus2.setText("wait for session");
                    frameMain.this.login2.getSession();
                    frameMain.this.lblStatus2.setText("loading captcha");
                    String capt = frameMain.this.login2.getCaptchaURL();
                    try {
                        frameMain.this.img2.setImage(ImageIO.read(new URL(capt)));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frameMain.this.lblStatus2.setText("ready!");
                }
            }.start();
        }
        if (this.txtIP3.getText().trim().length() > 0) {
            new Thread() {

                @Override
                public void run() {
                    String ip = frameMain.this.txtIP3.getText().trim();
                    frameMain.this.login3 = new myLogin(true, ip, frameMain.this.txtUsername.getText(), frameMain.this.txtPassword.getText());
                    frameMain.this.lblStatus3.setText("wait for session");
                    frameMain.this.login3.getSession();
                    frameMain.this.lblStatus3.setText("loading captcha");
                    String capt = frameMain.this.login3.getCaptchaURL();
                    try {
                        frameMain.this.img3.setImage(ImageIO.read(new URL(capt)));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frameMain.this.lblStatus3.setText("ready!");
                }
            }.start();
        }
        if (this.txtIP4.getText().trim().length() > 0) {
            new Thread() {

                @Override
                public void run() {
                    String ip = frameMain.this.txtIP4.getText().trim();
                    frameMain.this.login4 = new myLogin(true, ip, frameMain.this.txtUsername.getText(), frameMain.this.txtPassword.getText());
                    frameMain.this.lblStatus4.setText("wait for session");
                    frameMain.this.login4.getSession();
                    frameMain.this.lblStatus4.setText("loading captcha");
                    String capt = frameMain.this.login4.getCaptchaURL();
                    try {
                        frameMain.this.img4.setImage(ImageIO.read(new URL(capt)));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    frameMain.this.lblStatus4.setText("ready!");
                }
            }.start();
        }
    }

    public class myPanel
            extends JPanel {
        BufferedImage i;

        public myPanel() {
            this.i = null;
            new Thread() {

                @Override
                public void run() {
                    do {
                        myPanel.this.repaint();
                        try {
                            Thread.sleep(100);
                            continue;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(frameMain.class.getName()).log(Level.SEVERE, null, ex);
                            continue;
                        }
                    } while (true);
                }
            }.start();
        }

        public void setImage(BufferedImage iq) {
            this.i = iq;
        }

        @Override
        public void paint(Graphics gg) {
            super.paint(gg);
            Graphics2D g = (Graphics2D) gg;
            if (this.i != null) {
                g.drawImage(this.i, null, 0, 0);
            } else {
                g.fillRect(0, 0, 150, 40);
            }
        }

    }

}

