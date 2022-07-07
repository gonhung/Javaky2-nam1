package controller;

import java.awt.EventQueue;
import view.DangNhapGUI;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DangNhapGUI view = new DangNhapGUI();
                    view.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

