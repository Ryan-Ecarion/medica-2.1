package Main;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Panel_AP.AP_Dokter;
import Panel_AP.AP_Home;
import Panel_AP.AP_Jadwal;
import Panel_AP.AP_Pasien;
import Panel_AP.AP_Setting;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;


/**
 *
 * @author Ryan
 */
public class C_AdminPage extends javax.swing.JFrame {

    private JLabel activeLabel;
    
    public C_AdminPage() {
        initComponents();
        
        bt_home.setIcon(new FlatSVGIcon("Resource_AP/icon-home.svg", 30, 30));
        bt_dokter.setIcon(new FlatSVGIcon("Resource_AP/icon-staf.svg", 23, 23));
        bt_jadwal.setIcon(new FlatSVGIcon("Resource_AP/icon-jadwal.svg", 23, 23));
        bt_pasien.setIcon(new FlatSVGIcon("Resource_AP/icon-pasien.svg", 23, 23));
        bt_setting.setIcon(new FlatSVGIcon("Resource_AP/icon-setting.svg", 23, 23));
        
        
        
        setupPanels();
        setupActions();
        CardLayout cardLayout = (CardLayout) pnl_utama.getLayout();
    cardLayout.show(pnl_utama, "home");
    new AP_Home();
    }
    

    private void setupPanels() {
    pnl_utama.setLayout(new CardLayout());
    pnl_utama.add(new AP_Home(), "home");
    pnl_utama.add(new AP_Dokter(), "dokter");
    pnl_utama.add(new AP_Pasien(), "pasien");
    pnl_utama.add(new AP_Jadwal(), "jadwal");
    pnl_utama.add(new AP_Setting(), "setting");
}


    private void setupActions() {
        setupLabelAction(bt_home, new AP_Home(), true);
        setupLabelAction(bt_dokter, new AP_Dokter(), false);
        setupLabelAction(bt_pasien, new AP_Pasien(), false);
        setupLabelAction(bt_jadwal, new AP_Jadwal(), false);
        setupLabelAction(bt_setting, new AP_Setting(), false);
    }

    private void setupLabelAction(JLabel label, JPanel targetPanel, boolean isHome) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchPanel(label, targetPanel, isHome);
            }
        });
    }

    private void switchPanel(JLabel label, JPanel newPanel, boolean isHome) {
    if (label == activeLabel) return;

    // Update active label styling
    updateActiveLabel(label, isHome);

    // Gunakan CardLayout untuk mengganti panel
    CardLayout cardLayout = (CardLayout) pnl_utama.getLayout();
    if (newPanel instanceof AP_Home) {
        cardLayout.show(pnl_utama, "home");
    } else if (newPanel instanceof AP_Dokter) {
        cardLayout.show(pnl_utama, "dokter");
    } else if (newPanel instanceof AP_Pasien) {
        cardLayout.show(pnl_utama, "pasien");
    } else if (newPanel instanceof AP_Jadwal) {
        cardLayout.show(pnl_utama, "jadwal");
    } else if (newPanel instanceof AP_Setting) {
        cardLayout.show(pnl_utama, "setting");
    }
}


    private void updateActiveLabel(JLabel label, boolean isHome) {
        if (activeLabel != null) {
            activeLabel.setBorder(null); // Remove underline from previous active label
        }
        activeLabel = label;
        activeLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, isHome ? new Color(113, 179, 196) : Color.WHITE));
    }

    


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_home = new Costum.PanelRound();
        bt_home = new javax.swing.JLabel();
        pnl_bar = new Costum.PanelRound();
        bt_pasien = new javax.swing.JLabel();
        bt_dokter = new javax.swing.JLabel();
        bt_jadwal = new javax.swing.JLabel();
        bt_setting = new javax.swing.JLabel();
        pnl_bg = new javax.swing.JPanel();
        pnl_utama = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_home.setBackground(new java.awt.Color(255, 255, 255));
        pnl_home.setRoundBottomLeft(30);
        pnl_home.setRoundBottomRight(30);
        pnl_home.setRoundTopLeft(30);
        pnl_home.setRoundTopRight(30);
        pnl_home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bt_home.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bt_home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_homeMouseClicked(evt);
            }
        });
        pnl_home.add(bt_home, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 68, 38));

        getContentPane().add(pnl_home, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 507, 80, 50));

        pnl_bar.setBackground(new java.awt.Color(17, 137, 163));
        pnl_bar.setRoundTopLeft(50);
        pnl_bar.setRoundTopRight(50);
        pnl_bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bt_pasien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bt_pasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_pasienMouseClicked(evt);
            }
        });
        pnl_bar.add(bt_pasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 6, 80, 28));

        bt_dokter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bt_dokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_dokterMouseClicked(evt);
            }
        });
        pnl_bar.add(bt_dokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(51, 6, 80, 28));

        bt_jadwal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bt_jadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_jadwalMouseClicked(evt);
            }
        });
        pnl_bar.add(bt_jadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 6, 80, 28));

        bt_setting.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bt_setting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_settingMouseClicked(evt);
            }
        });
        pnl_bar.add(bt_setting, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 6, 80, 28));

        getContentPane().add(pnl_bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 526, 680, 40));

        pnl_bg.setBackground(new java.awt.Color(239, 242, 251));

        pnl_utama.setBackground(new java.awt.Color(239, 242, 251));
        pnl_utama.setPreferredSize(new java.awt.Dimension(1000, 490));

        javax.swing.GroupLayout pnl_utamaLayout = new javax.swing.GroupLayout(pnl_utama);
        pnl_utama.setLayout(pnl_utamaLayout);
        pnl_utamaLayout.setHorizontalGroup(
            pnl_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        pnl_utamaLayout.setVerticalGroup(
            pnl_utamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_bgLayout = new javax.swing.GroupLayout(pnl_bg);
        pnl_bg.setLayout(pnl_bgLayout);
        pnl_bgLayout.setHorizontalGroup(
            pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_bgLayout.createSequentialGroup()
                .addComponent(pnl_utama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_bgLayout.setVerticalGroup(
            pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_bgLayout.createSequentialGroup()
                .addComponent(pnl_utama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 73, Short.MAX_VALUE))
        );

        getContentPane().add(pnl_bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 563));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bt_dokterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_dokterMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_dokterMouseClicked

    private void bt_pasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_pasienMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_pasienMouseClicked

    private void bt_homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_homeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_homeMouseClicked

    private void bt_jadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_jadwalMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_jadwalMouseClicked

    private void bt_settingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_settingMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_settingMouseClicked

    public static void main(String args[]) {
        
         try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Pilih tema FlatLightLaf
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize FlatLaf: " + ex.getMessage());
        }
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new C_AdminPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bt_dokter;
    private javax.swing.JLabel bt_home;
    private javax.swing.JLabel bt_jadwal;
    private javax.swing.JLabel bt_pasien;
    private javax.swing.JLabel bt_setting;
    private Costum.PanelRound pnl_bar;
    private javax.swing.JPanel pnl_bg;
    private Costum.PanelRound pnl_home;
    private javax.swing.JPanel pnl_utama;
    // End of variables declaration//GEN-END:variables
}
