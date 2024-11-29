/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel_AP;


import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AP_Jadwal extends javax.swing.JPanel {
    Connection conn;
    DefaultTableModel tableModel;
    

    public AP_Jadwal() {
        initComponents();
        
        txt_daftar.setIcon(new FlatSVGIcon("Resource_AP_Home/icon-daftar.svg", 30, 30));
        
         
        
        initDatabase();
        loadComboData(); 
        loadTableData("");
        setListeners();
    }
    
     // Memuat data combobox
    private void loadComboData() {
        try (Statement stmt = conn.createStatement()) {
            // Load Pasien
            String sqlPasien = "SELECT id_pasien, nama FROM pasien";
            ResultSet rsPasien = stmt.executeQuery(sqlPasien);
            Pasien.removeAllItems();
            Pasien.addItem("- < Pasien > -");
            while (rsPasien.next()) {
                Pasien.addItem(rsPasien.getString("nama"));
            }

            // Load Dokter
            String sqlDokter = "SELECT id_dokter, nama FROM dokter";
            ResultSet rsDokter = stmt.executeQuery(sqlDokter);
            Dokter.removeAllItems();
            Dokter.addItem("- < Dokter > -");
            while (rsDokter.next()) {
                Dokter.addItem(rsDokter.getString("nama"));
            }

            // Load Perawatan
            String sqlPerawatan = "SELECT id_perawatan, nama FROM perawatan";
            ResultSet rsPerawatan = stmt.executeQuery(sqlPerawatan);
            Perawatan.removeAllItems();
            Perawatan.addItem("- < Perawatan > -");
            while (rsPerawatan.next()) {
                Perawatan.addItem(rsPerawatan.getString("nama"));
            }

            // Load Status
            String[] statusOptions = {"- < Status > -", "Terjadwal", "Selesai", "Dibatalkan"};
            Status.setModel(new DefaultComboBoxModel<>(statusOptions));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data combo box: " + ex.getMessage());
        }
    }

    private void initDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/medica", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal: " + ex.getMessage());
        }
    }

    private void loadTableData(String query) {
        String sql = "SELECT jp.id_jadwal, p.nama AS pasien, jp.tanggal, d.nama AS dokter, pr.nama AS perawatan, jp.status " +
                     "FROM jadwal_perawatan jp " +
                     "JOIN pasien p ON jp.id_pasien = p.id_pasien " +
                     "JOIN dokter d ON jp.id_dokter = d.id_dokter " +
                     "JOIN perawatan pr ON jp.id_perawatan = pr.id_perawatan " +
                     query;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            tableModel = new DefaultTableModel(new Object[]{"Nama", "Tanggal", "Dokter", "Perawatan", "Status"}, 0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("pasien"),
                    rs.getDate("tanggal"),
                    rs.getString("dokter"),
                    rs.getString("perawatan"),
                    rs.getString("status")
                });
            }
            Daftar_Jadwal.setModel(tableModel);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage());
        }
    }

    private void updateStatus(String status) {
        int row = Daftar_Jadwal.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu.");
            return;
        }
        String namaPasien = tableModel.getValueAt(row, 0).toString();
        String sql = "UPDATE jadwal_perawatan jp " +
                     "JOIN pasien p ON jp.id_pasien = p.id_pasien " +
                     "SET jp.status = ? WHERE p.nama = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, namaPasien);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Status berhasil diperbarui.");
            loadTableData("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui status: " + ex.getMessage());
        }
    }

    private void setListeners() {
        Ascending.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadTableData("ORDER BY p.nama ASC");
            }
        });

        Descanding.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadTableData("ORDER BY p.nama DESC");
            }
        });

        Tanggal_Terbaru.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadTableData("ORDER BY jp.tanggal DESC");
            }
        });

        bt_cari.addActionListener(e -> {
            String keyword = txt_cari.getText().trim();
            loadTableData("WHERE p.nama LIKE '%" + keyword + "%' OR d.nama LIKE '%" + keyword + "%'");
        });

        bt_selesai.addActionListener(e -> updateStatus("Selesai"));
        bt_terjadwal.addActionListener(e -> updateStatus("Terjadwal"));
        bt_dibatalkan.addActionListener(e -> updateStatus("Dibatalkan"));

        Tambah.addActionListener(e -> addJadwalPerawatan());
        
        Hapus.addActionListener(e -> deleteJadwalPerawatan());
    }

    private void addJadwalPerawatan() {
        String pasien = (String) Pasien.getSelectedItem();
        String dokter = (String) Dokter.getSelectedItem();
        String perawatan = (String) Perawatan.getSelectedItem();
        Date tanggal = new Date(Tanggal.getDate().getTime());

        if ("- < Pasien > -".equals(pasien) || "- < Dokter > -".equals(dokter) || "- < Perawatan > -".equals(perawatan) || tanggal == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus dipilih.");
            return;
        }

        try {
            // Menambahkan data jadwal perawatan
            String sql = "INSERT INTO jadwal_perawatan (id_pasien, id_dokter, id_perawatan, tanggal, status) VALUES (" +
                         "(SELECT id_pasien FROM pasien WHERE nama = ?), " +
                         "(SELECT id_dokter FROM dokter WHERE nama = ?), " +
                         "(SELECT id_perawatan FROM perawatan WHERE nama = ?), ?, 'Terjadwal')";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pasien);
                ps.setString(2, dokter);
                ps.setString(3, perawatan);
                ps.setDate(4, tanggal);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Jadwal perawatan berhasil ditambahkan.");
                loadTableData("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan jadwal perawatan: " + ex.getMessage());
        }
    }


    private void deleteJadwalPerawatan() {
        int row = Daftar_Jadwal.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        String pasien = tableModel.getValueAt(row, 0).toString();
        try {
            String sql = "DELETE FROM jadwal_perawatan WHERE id_jadwal = (SELECT id_jadwal FROM jadwal_perawatan jp " +
                         "JOIN pasien p ON jp.id_pasien = p.id_pasien WHERE p.nama = ? LIMIT 1)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pasien);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Jadwal perawatan berhasil dihapus.");
                loadTableData("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus jadwal perawatan: " + ex.getMessage());
        }
    }




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new Costum.PanelRound();
        Pasien = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Daftar_Jadwal = new javax.swing.JTable();
        txt_daftar = new javax.swing.JLabel();
        txt_cari = new javax.swing.JTextField();
        bt_cari = new javax.swing.JButton();
        Perawatan = new javax.swing.JComboBox<>();
        Dokter = new javax.swing.JComboBox<>();
        Tanggal = new com.toedter.calendar.JDateChooser();
        Tambah = new javax.swing.JButton();
        Hapus = new javax.swing.JButton();
        Status = new javax.swing.JComboBox<>();
        refresh = new javax.swing.JButton();
        panelRound3 = new Costum.PanelRound();
        bt_selesai = new javax.swing.JButton();
        bt_terjadwal = new javax.swing.JButton();
        bt_dibatalkan = new javax.swing.JButton();
        pnl_bar = new Costum.PanelRound();
        txt_pengaturan = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Tanggal_Terbaru = new javax.swing.JLabel();
        Ascending = new javax.swing.JLabel();
        Descanding = new javax.swing.JLabel();

        setBackground(new java.awt.Color(239, 242, 251));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound1.setBackground(new java.awt.Color(255, 255, 255));
        panelRound1.setRoundBottomLeft(50);
        panelRound1.setRoundBottomRight(50);
        panelRound1.setRoundTopLeft(50);
        panelRound1.setRoundTopRight(50);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Pasien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- < Pasien > -" }));
        panelRound1.add(Pasien, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, 170, 30));

        Daftar_Jadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nama", "Tanggal", "Doker ", "Perawatan", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Daftar_Jadwal);

        panelRound1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 49, 490, 300));

        txt_daftar.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        txt_daftar.setForeground(new java.awt.Color(0, 102, 102));
        txt_daftar.setText("Daftar Jadwal Perawatan");
        panelRound1.add(txt_daftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 29));
        panelRound1.add(txt_cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 362, 220, 30));

        bt_cari.setText("Cari");
        panelRound1.add(bt_cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 362, -1, 30));

        Perawatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- < Perawatan > -" }));
        Perawatan.setToolTipText("Perawatan");
        panelRound1.add(Perawatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 170, 30));

        Dokter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- < Dokter > -" }));
        panelRound1.add(Dokter, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 170, 30));
        panelRound1.add(Tanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 260, 170, 20));

        Tambah.setBackground(new java.awt.Color(0, 153, 153));
        Tambah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Tambah.setForeground(new java.awt.Color(255, 255, 255));
        Tambah.setText(" TAMBAH");
        panelRound1.add(Tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 290, 170, 30));

        Hapus.setBackground(new java.awt.Color(255, 102, 102));
        Hapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Hapus.setForeground(new java.awt.Color(255, 255, 255));
        Hapus.setText("HAPUS");
        panelRound1.add(Hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 323, 170, 30));

        Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "- < Status > -" }));
        Status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusActionPerformed(evt);
            }
        });
        panelRound1.add(Status, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, 170, 30));

        refresh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });
        panelRound1.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 170, 30));

        add(panelRound1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 770, 410));

        panelRound3.setBackground(new java.awt.Color(255, 255, 255));
        panelRound3.setRoundBottomLeft(50);
        panelRound3.setRoundBottomRight(50);
        panelRound3.setRoundTopLeft(50);
        panelRound3.setRoundTopRight(50);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bt_selesai.setBackground(new java.awt.Color(0, 153, 153));
        bt_selesai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_selesai.setForeground(new java.awt.Color(255, 255, 255));
        bt_selesai.setText("Selesai");
        panelRound3.add(bt_selesai, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 110, 30));

        bt_terjadwal.setBackground(new java.awt.Color(153, 153, 0));
        bt_terjadwal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_terjadwal.setForeground(new java.awt.Color(255, 255, 255));
        bt_terjadwal.setText("Terjadwal");
        bt_terjadwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_terjadwalActionPerformed(evt);
            }
        });
        panelRound3.add(bt_terjadwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 110, 30));

        bt_dibatalkan.setBackground(new java.awt.Color(255, 102, 102));
        bt_dibatalkan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_dibatalkan.setForeground(new java.awt.Color(255, 255, 255));
        bt_dibatalkan.setText("Dibatalkan");
        panelRound3.add(bt_dibatalkan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 110, 30));

        add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 150, 130));

        pnl_bar.setBackground(new java.awt.Color(17, 137, 163));
        pnl_bar.setRoundBottomLeft(50);
        pnl_bar.setRoundBottomRight(50);
        pnl_bar.setRoundTopLeft(50);
        pnl_bar.setRoundTopRight(50);
        pnl_bar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_pengaturan.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        txt_pengaturan.setForeground(new java.awt.Color(255, 255, 255));
        txt_pengaturan.setText("Filter");
        pnl_bar.add(txt_pengaturan, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 7, -1, 29));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 2));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        pnl_bar.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 150, 3));

        Tanggal_Terbaru.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Tanggal_Terbaru.setForeground(new java.awt.Color(255, 255, 255));
        Tanggal_Terbaru.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Tanggal_Terbaru.setText("Tanggal Terbaru");
        pnl_bar.add(Tanggal_Terbaru, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 130, 30));

        Ascending.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Ascending.setForeground(new java.awt.Color(255, 255, 255));
        Ascending.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ascending.setText("A-Z");
        pnl_bar.add(Ascending, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 60, 30));

        Descanding.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Descanding.setForeground(new java.awt.Color(255, 255, 255));
        Descanding.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Descanding.setText("Z-A");
        pnl_bar.add(Descanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 60, 30));

        add(pnl_bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, 270));
    }// </editor-fold>//GEN-END:initComponents

    private void bt_terjadwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_terjadwalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bt_terjadwalActionPerformed

    private void StatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        // TODO add your handling code here:
        loadComboData();
        loadTableData("");
    }//GEN-LAST:event_refreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Ascending;
    private javax.swing.JTable Daftar_Jadwal;
    private javax.swing.JLabel Descanding;
    private javax.swing.JComboBox<String> Dokter;
    private javax.swing.JButton Hapus;
    private javax.swing.JComboBox<String> Pasien;
    private javax.swing.JComboBox<String> Perawatan;
    private javax.swing.JComboBox<String> Status;
    private javax.swing.JButton Tambah;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JLabel Tanggal_Terbaru;
    private javax.swing.JButton bt_cari;
    private javax.swing.JButton bt_dibatalkan;
    private javax.swing.JButton bt_selesai;
    private javax.swing.JButton bt_terjadwal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private Costum.PanelRound panelRound1;
    private Costum.PanelRound panelRound3;
    private Costum.PanelRound pnl_bar;
    private javax.swing.JButton refresh;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JLabel txt_daftar;
    private javax.swing.JLabel txt_pengaturan;
    // End of variables declaration//GEN-END:variables

    private void dispose(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
