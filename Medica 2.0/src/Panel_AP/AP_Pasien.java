/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel_AP;

/**
 *
 * @author ACER
 */

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class AP_Pasien extends javax.swing.JPanel {

    Connection conn; // Koneksi database
    DefaultTableModel model; // Model untuk JTable
// Model untuk JTable
   private Timer timer;


    public AP_Pasien() {
        initComponents();
        connectDatabase();
        loadTable();
        setButtonListeners();
        
        txt_daftar.setIcon(new FlatSVGIcon("Resource_AP_Home/icon-daftar.svg", 30, 30));
        
        
        timer = new Timer(15000, e -> {
            
            loadTable();
           
        });
        timer.start(); // Memulai timer
        
    }
    

    // Koneksi ke database
    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/medica", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal: " + e.getMessage());
        }
    }

    // Muat data dari database ke JTable
    private void loadTable() {
        String[] kolom = {"Nama", "Jenis Kelamin", "No Telepon", "Alamat"};
        model = new DefaultTableModel(null, kolom);
        String query = "SELECT nama, jenis_kelamin, no_telepon, alamat FROM pasien";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String nama = rs.getString("nama");
                String jk = rs.getString("jenis_kelamin").equals("L") ? "Laki-laki" : "Perempuan";
                String telp = rs.getString("no_telepon");
                String alamat = rs.getString("alamat");
                model.addRow(new Object[]{nama, jk, telp, alamat});
            }
            Pasien.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    // Tambahkan aksi ke tombol
    private void setButtonListeners() {
        Tambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    tambahData(); // Jika valid, panggil method tambahData
                } else {
                    JOptionPane.showMessageDialog(AP_Pasien.this, "Semua field harus diisi!");
                }
            }
        });
        
      

        Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editData();
            }
        });

        Hapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        Clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(); // Panggil method untuk menghapus isi text field dan text area
            }
        });

        Pasien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiDataKeForm();
            }
        });
    }

    // Tambah data
    private void tambahData() {
        String nama = Nama.getText();
        String telp = No_Telepon.getText();
        String alamat = Alamat.getText();
        String jk = Jenis_Kelamin.getSelectedItem().toString().equals("Laki-laki") ? "L" : "P";

        String query = "INSERT INTO pasien (nama, no_telepon, alamat, jenis_kelamin) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nama);
            ps.setString(2, telp);
            ps.setString(3, alamat);
            ps.setString(4, jk);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            loadTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data: " + e.getMessage());
        }
    }

    // Edit data
   // Edit data
        private void editData() {
            int selectedRow = Pasien.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
                return;
            }

            // Konfirmasi edit
            int option = JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin mengedit data dokter ini?", 
                    "Konfirmasi Edit", 
                    JOptionPane.YES_NO_OPTION);

            // Jika pengguna memilih Yes, lanjutkan pengeditan
            if (option == JOptionPane.YES_OPTION) {
                String nama = Nama.getText();
                String telp = No_Telepon.getText();
                String alamat = Alamat.getText();
                String jk = Jenis_Kelamin.getSelectedItem().toString().equals("Laki-laki") ? "L" : "P";
                String namaLama = model.getValueAt(selectedRow, 0).toString();

                String query = "UPDATE pasien SET nama=?, no_telepon=?, alamat=?, jenis_kelamin=? WHERE nama=?";

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, nama);
                    ps.setString(2, telp);
                    ps.setString(3, alamat);
                    ps.setString(4, jk);
                    ps.setString(5, namaLama);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                    loadTable(); // Reload tabel setelah perubahan
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + e.getMessage());
                }
            } else {
                // Jika pengguna memilih No, tidak ada yang terjadi
                JOptionPane.showMessageDialog(this, "Data tidak diubah.");
            }
        }

    

    // Hapus data
    private void hapusData() {
        int selectedRow = Pasien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
            return;
        }

        String nama = model.getValueAt(selectedRow, 0).toString();
        String query = "DELETE FROM pasien WHERE nama=?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nama);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            loadTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
        }
    }

    // Isi data dari tabel ke form
    private void isiDataKeForm() {
        int selectedRow = Pasien.getSelectedRow();
        if (selectedRow == -1) return;

        Nama.setText(model.getValueAt(selectedRow, 0).toString());
        Jenis_Kelamin.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
        No_Telepon.setText(model.getValueAt(selectedRow, 2).toString());
        Alamat.setText(model.getValueAt(selectedRow, 3).toString());
    }

    // Method untuk menghapus isi text field dan text area
    private void clearFields() {
        Nama.setText("");
        No_Telepon.setText("");
        Alamat.setText("");
        Jenis_Kelamin.setSelectedIndex(0); // Pilih default "Laki-laki"
    }

    // Method untuk validasi input field
    private boolean validateFields() {
        String nama = Nama.getText().trim();
        String telp = No_Telepon.getText().trim();
        String alamat = Alamat.getText().trim();
        
        return !nama.isEmpty() && !telp.isEmpty() && !alamat.isEmpty();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound2 = new Costum.PanelRound();
        Tambah = new javax.swing.JButton();
        Hapus = new javax.swing.JButton();
        Clear = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        panelRound3 = new Costum.PanelRound();
        Nama = new javax.swing.JTextField();
        Jenis_Kelamin = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Alamat = new javax.swing.JTextArea();
        No_Telepon = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panelRound4 = new Costum.PanelRound();
        jScrollPane3 = new javax.swing.JScrollPane();
        Pasien = new javax.swing.JTable();
        txt_daftar = new javax.swing.JLabel();

        setBackground(new java.awt.Color(239, 242, 251));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound2.setBackground(new java.awt.Color(255, 255, 255));
        panelRound2.setRoundBottomLeft(50);
        panelRound2.setRoundBottomRight(50);
        panelRound2.setRoundTopLeft(50);
        panelRound2.setRoundTopRight(50);
        panelRound2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tambah.setBackground(new java.awt.Color(0, 153, 153));
        Tambah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Tambah.setForeground(new java.awt.Color(255, 255, 255));
        Tambah.setText(" TAMBAH");
        panelRound2.add(Tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 230, 30));

        Hapus.setBackground(new java.awt.Color(255, 102, 102));
        Hapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Hapus.setForeground(new java.awt.Color(255, 255, 255));
        Hapus.setText("HAPUS");
        panelRound2.add(Hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        Clear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Clear.setText("CLEAR");
        panelRound2.add(Clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));

        Edit.setBackground(new java.awt.Color(204, 204, 0));
        Edit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Edit.setForeground(new java.awt.Color(255, 255, 255));
        Edit.setText("EDIT");
        panelRound2.add(Edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, -1, -1));

        add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 360, 270, 100));

        panelRound3.setBackground(new java.awt.Color(17, 137, 163));
        panelRound3.setRoundBottomLeft(50);
        panelRound3.setRoundBottomRight(50);
        panelRound3.setRoundTopLeft(50);
        panelRound3.setRoundTopRight(50);
        panelRound3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelRound3.add(Nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 230, -1));

        Jenis_Kelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-laki", "Perempuan" }));
        panelRound3.add(Jenis_Kelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 230, -1));

        Alamat.setColumns(20);
        Alamat.setRows(5);
        jScrollPane1.setViewportView(Alamat);

        panelRound3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 230, 60));
        panelRound3.add(No_Telepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 230, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("No. HP:");
        panelRound3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nama Lengkap:");
        panelRound3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Jenis Kelamin:");
        panelRound3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Alamat:");
        panelRound3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 20));

        add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, 270, 300));

        panelRound4.setBackground(new java.awt.Color(255, 255, 255));
        panelRound4.setRoundBottomLeft(50);
        panelRound4.setRoundBottomRight(50);
        panelRound4.setRoundTopLeft(50);
        panelRound4.setRoundTopRight(50);
        panelRound4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Pasien.setFont(new java.awt.Font("Futura Bk BT", 0, 12)); // NOI18N
        Pasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama", "Jenis Kelamin", "No Telepon", "Alamat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Pasien.setGridColor(new java.awt.Color(0, 102, 102));
        jScrollPane3.setViewportView(Pasien);

        panelRound4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 600, 340));

        txt_daftar.setFont(new java.awt.Font("Century Gothic", 1, 20)); // NOI18N
        txt_daftar.setForeground(new java.awt.Color(0, 102, 102));
        txt_daftar.setText("Daftar Pasien");
        panelRound4.add(txt_daftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 29));

        add(panelRound4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 640, 410));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Alamat;
    private javax.swing.JButton Clear;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Hapus;
    private javax.swing.JComboBox<String> Jenis_Kelamin;
    private javax.swing.JTextField Nama;
    private javax.swing.JTextField No_Telepon;
    private javax.swing.JTable Pasien;
    private javax.swing.JButton Tambah;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private Costum.PanelRound panelRound2;
    private Costum.PanelRound panelRound3;
    private Costum.PanelRound panelRound4;
    private javax.swing.JLabel txt_daftar;
    // End of variables declaration//GEN-END:variables
}
