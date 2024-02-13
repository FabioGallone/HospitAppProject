package ui;

import domain.HospitApp;

public class HospitAppFrame {
    private HospitApp hospitapp;
    public HospitAppFrame() {

        hospitapp= HospitApp.getInstance();
        initComponents();


    }

    private void initComponents(){
        new LoginGUI(hospitapp);
    }
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HospitAppFrame();
            }
        });
    }
}
