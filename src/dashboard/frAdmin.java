/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package dashboard;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.Koneksi;
import main.LoginForm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.lowagie.text.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

// --- IMPORT UTAMA UNTUK KONEKSI DAN GUI ---
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//image tampil
import java.awt.Image; // untuk GUI
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
// jangan import com.lowagie.text.Image;

//statistik chart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
//end statistik


public class frAdmin extends javax.swing.JFrame {
    private CardLayout cardLayout;
    DefaultTableModel model;
    private File selectedFile;
    private String fotoPath;
    private String fotoPathFromDatabase;
    private Object PDFImage;
    
    public static frAdmin instance;
    
    public frAdmin() {
        initComponents();
        this.setSize(1080, 720);
        this.setLocationRelativeTo(null);
        cardLayout = (CardLayout) Manage.getLayout();
        Manage.add(Manajemen, "Manajemen");
        Manage.add(Pengaduan, "Pengaduan");
        Manage.add(Statistik, "Statistik");
        loadTable();
        instance = this; // simpan instansinya
        loadDataPengaduan();
        loadDataTampilkanDS();
        tampilkanSemuaChart();
        model = (DefaultTableModel) tblUser.getModel(); // pastikan nama tabel di GUI adalah tblUser
        
        
        
        
        //Start
        // Panel manajemen user
            btn_Manajemen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(Manage, "Manajemen");
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Manajemen.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Manajemen.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Manajemen.setBackground(Color.WHITE);
        }
    });
           
            // Panel data laporan
            btn_Laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(Manage, "Pengaduan");
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Laporan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Laporan.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Laporan.setBackground(Color.WHITE);
        }
    });
       
             // Panel Statistik
            btn_Analisis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(Manage, "Statistik");
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Analisis.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Analisis.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Analisis.setBackground(Color.WHITE);
        }
    });
            //end
          
            
    tblUser.addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent evt) {
        int baris = tblUser.getSelectedRow();
        txtNamaLengkap.setText(model.getValueAt(baris, 1).toString());
        txtNik.setText(model.getValueAt(baris, 2).toString());
        txtUsername.setText(model.getValueAt(baris, 3).toString());
        cmbRole.setSelectedItem(model.getValueAt(baris, 4).toString());
    }
});

    //inisilisasi combo box
    cmbRole.removeAllItems();
    cmbRole.addItem("admin");
    cmbRole.addItem("petugas");
    cmbRole.addItem("masyarakat");
 
 }

    //method tampilkan chart
    private void tampilkanSemuaChart() {
    // Panel Utama
    panelStatistik.removeAll();
    panelStatistik.setLayout(new GridLayout(2, 1, 10, 10)); // 2 baris, 1 kolom, jarak 10px

    // === GRAFIK 1: Pengaduan per Bulan (Bar Chart) ===
    DefaultCategoryDataset datasetBulan = new DefaultCategoryDataset();
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
         Statement stmt = conn.createStatement()) {

        String sql = "SELECT MONTH(tanggal) AS bulan, COUNT(*) AS jumlah FROM pengaduan GROUP BY MONTH(tanggal)";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int bulan = rs.getInt("bulan");
            int jumlah = rs.getInt("jumlah");
            String namaBulan = Month.of(bulan).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("id"));
            datasetBulan.addValue(jumlah, "Pengaduan", namaBulan);
        }
        rs.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat grafik bulanan: " + e.getMessage());
    }

    JFreeChart chartBulan = ChartFactory.createBarChart(
            "Jumlah Pengaduan per Bulan",
            "Bulan", "Jumlah", datasetBulan,
            PlotOrientation.VERTICAL, false, true, false);
    ChartPanel panelBulan = new ChartPanel(chartBulan);
    panelStatistik.add(panelBulan);

    // === GRAFIK 2: Pengaduan berdasarkan Status (Pie Chart) ===
    DefaultPieDataset datasetStatus = new DefaultPieDataset();
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
         Statement stmt = conn.createStatement()) {

        String sql = "SELECT status, COUNT(*) AS jumlah FROM pengaduan GROUP BY status";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String status = rs.getString("status");
            int jumlah = rs.getInt("jumlah");
            datasetStatus.setValue(status, jumlah);
        }
        rs.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat grafik status: " + e.getMessage());
    }

    JFreeChart chartStatus = ChartFactory.createPieChart(
            "Distribusi Status Pengaduan",
            datasetStatus, true, true, false);
    ChartPanel panelStatus = new ChartPanel(chartStatus);
    panelStatistik.add(panelStatus);

    // Refresh panel
    panelStatistik.revalidate();
    panelStatistik.repaint();
    panelStatistik.setPreferredSize(new Dimension(800, panelStatistik.getComponentCount() * 320));


}
//  // Method untuk mendapatkan path gambar
//    String getSelectedImagePath(JTable tblPengaduan, int selectedRow) {
//        try {
//            Object value = tblPengaduan.getValueAt(selectedRow, 5);
//            return (value != null) ? value.toString() : null;
//        } catch (Exception e) {
//            return null;
//        }
//    }
    //fungsi field cari atau search
    private void cariDataLaporan(String keyword) {
    String[] kolom = {"No", "Judul Laporan", "Tanggal", "Status"};
    DefaultTableModel model = new DefaultTableModel(null, kolom);

    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "SELECT * FROM pengaduan WHERE judul_laporan LIKE ? OR status LIKE ? OR tanggal LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        String query = "%" + keyword + "%";
        ps.setString(1, query);
        ps.setString(2, query);
        ps.setString(3, query);
        ResultSet rs = ps.executeQuery();

        int no = 1;
        while (rs.next()) {
            String judul = rs.getString("judul_laporan");
            String tanggal = rs.getString("tanggal");
            String status = rs.getString("status");

            model.addRow(new Object[]{no++, judul, tanggal, status});
        }

        tblPengaduan.setModel(model);

        rs.close();
        ps.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage());
    }
}
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Right = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pn_main = new javax.swing.JPanel();
        pn_line = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btn_Manajemen = new javax.swing.JLabel();
        pn_main1 = new javax.swing.JPanel();
        pn_line1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btn_Laporan = new javax.swing.JLabel();
        pn_main2 = new javax.swing.JPanel();
        pn_line2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btn_Analisis = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        jPanelGradient1 = new Palatte.JPanelGradient();
        panelRounded2 = new Palatte.PanelRounded();
        jLabel13 = new javax.swing.JLabel();
        lblPengaduan = new javax.swing.JLabel();
        panelRounded4 = new Palatte.PanelRounded();
        lblSelesai = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        panelRounded5 = new Palatte.PanelRounded();
        lblDiproses = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panelRounded3 = new Palatte.PanelRounded();
        jLabel3 = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        MainUtama = new javax.swing.JPanel();
        Manage = new javax.swing.JPanel();
        Manajemen = new javax.swing.JPanel();
        txtNamaLengkap = new javax.swing.JTextField();
        txtNik = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        cmbRole = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        AksiSimpan = new Palatte.PanelRounded();
        btnTambah = new javax.swing.JLabel();
        AksiEdit = new Palatte.PanelRounded();
        btnUbah = new javax.swing.JLabel();
        AksiHapus = new Palatte.PanelRounded();
        btnHapus = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Pengaduan = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPengaduan = new javax.swing.JTable();
        AksiSimpan1 = new Palatte.PanelRounded();
        btnDetail = new javax.swing.JLabel();
        AksiSimpan3 = new Palatte.PanelRounded();
        btnRefresh = new javax.swing.JLabel();
        PanelDetail = new javax.swing.JPanel();
        AksiSimpan2 = new Palatte.PanelRounded();
        btnEksport = new javax.swing.JLabel();
        JudulDetail = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taDeskripsiDetail = new javax.swing.JTextArea();
        txtTanggalDetail = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        dsFoto = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        AksiSimpan4 = new Palatte.PanelRounded();
        btnCari = new javax.swing.JLabel();
        Statistik = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        panelStatistik = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard Admin");

        Right.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/DA.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 188, 212));
        jLabel4.setText("Dashboard Admin");

        pn_main.setBackground(new java.awt.Color(255, 255, 255));

        pn_line.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pn_lineLayout = new javax.swing.GroupLayout(pn_line);
        pn_line.setLayout(pn_lineLayout);
        pn_lineLayout.setHorizontalGroup(
            pn_lineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pn_lineLayout.setVerticalGroup(
            pn_lineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/MU.png"))); // NOI18N

        btn_Manajemen.setBackground(new java.awt.Color(255, 255, 255));
        btn_Manajemen.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btn_Manajemen.setText("Manajemen User");
        btn_Manajemen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ManajemenMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ManajemenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ManajemenMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pn_mainLayout = new javax.swing.GroupLayout(pn_main);
        pn_main.setLayout(pn_mainLayout);
        pn_mainLayout.setHorizontalGroup(
            pn_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pn_line, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(btn_Manajemen)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        pn_mainLayout.setVerticalGroup(
            pn_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pn_mainLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btn_Manajemen))
                    .addComponent(pn_line, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pn_main1.setBackground(new java.awt.Color(255, 255, 255));

        pn_line1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pn_line1Layout = new javax.swing.GroupLayout(pn_line1);
        pn_line1.setLayout(pn_line1Layout);
        pn_line1Layout.setHorizontalGroup(
            pn_line1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pn_line1Layout.setVerticalGroup(
            pn_line1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/DM.png"))); // NOI18N

        btn_Laporan.setBackground(new java.awt.Color(255, 255, 255));
        btn_Laporan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btn_Laporan.setText("Data Pengaduan");
        btn_Laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_LaporanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_LaporanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_LaporanMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pn_main1Layout = new javax.swing.GroupLayout(pn_main1);
        pn_main1.setLayout(pn_main1Layout);
        pn_main1Layout.setHorizontalGroup(
            pn_main1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_main1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pn_line1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(btn_Laporan)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        pn_main1Layout.setVerticalGroup(
            pn_main1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_main1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_main1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pn_main1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btn_Laporan))
                    .addComponent(pn_line1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pn_main2.setBackground(new java.awt.Color(255, 255, 255));

        pn_line2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pn_line2Layout = new javax.swing.GroupLayout(pn_line2);
        pn_line2.setLayout(pn_line2Layout);
        pn_line2Layout.setHorizontalGroup(
            pn_line2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        pn_line2Layout.setVerticalGroup(
            pn_line2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/SP.png"))); // NOI18N

        btn_Analisis.setBackground(new java.awt.Color(255, 255, 255));
        btn_Analisis.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btn_Analisis.setText("Statistik Pengaduan");
        btn_Analisis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_AnalisisMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AnalisisMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AnalisisMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pn_main2Layout = new javax.swing.GroupLayout(pn_main2);
        pn_main2.setLayout(pn_main2Layout);
        pn_main2Layout.setHorizontalGroup(
            pn_main2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_main2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pn_line2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(btn_Analisis)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_main2Layout.setVerticalGroup(
            pn_main2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_main2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_main2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pn_main2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btn_Analisis))
                    .addComponent(pn_line2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/log30.png"))); // NOI18N
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(0, 153, 255));
        jLabel19.setText("Copyright © 2025");

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RightLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, RightLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pn_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnLogout)
                                    .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(pn_main2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pn_main1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(RightLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel19)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(RightLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel4)))
                .addGap(36, 36, 36)
                .addComponent(pn_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_main1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_main2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 406, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addContainerGap())
        );

        getContentPane().add(Right, java.awt.BorderLayout.LINE_START);

        Left.setLayout(new java.awt.BorderLayout());

        jPanelGradient1.setColorEnd(new java.awt.Color(171, 248, 242));
        jPanelGradient1.setColorStart(new java.awt.Color(85, 151, 201));

        panelRounded2.setBackground(new java.awt.Color(121, 120, 233));
        panelRounded2.setRoundBottomLeft(20);
        panelRounded2.setRoundBottomRight(20);
        panelRounded2.setRoundTopLeft(20);
        panelRounded2.setRoundTopRight(20);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total Pengaduan");

        lblPengaduan.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 24)); // NOI18N
        lblPengaduan.setForeground(new java.awt.Color(255, 255, 255));
        lblPengaduan.setText("0");

        javax.swing.GroupLayout panelRounded2Layout = new javax.swing.GroupLayout(panelRounded2);
        panelRounded2.setLayout(panelRounded2Layout);
        panelRounded2Layout.setHorizontalGroup(
            panelRounded2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRounded2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPengaduan)
                .addGap(16, 16, 16))
        );
        panelRounded2Layout.setVerticalGroup(
            panelRounded2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPengaduan)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        panelRounded4.setBackground(new java.awt.Color(152, 189, 255));
        panelRounded4.setRoundBottomLeft(20);
        panelRounded4.setRoundBottomRight(20);
        panelRounded4.setRoundTopLeft(20);
        panelRounded4.setRoundTopRight(20);

        lblSelesai.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 24)); // NOI18N
        lblSelesai.setForeground(new java.awt.Color(255, 255, 255));
        lblSelesai.setText("0");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Pengaduan Selesai");

        javax.swing.GroupLayout panelRounded4Layout = new javax.swing.GroupLayout(panelRounded4);
        panelRounded4.setLayout(panelRounded4Layout);
        panelRounded4Layout.setHorizontalGroup(
            panelRounded4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSelesai)
                .addGap(15, 15, 15))
            .addGroup(panelRounded4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        panelRounded4Layout.setVerticalGroup(
            panelRounded4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSelesai)
                .addGap(16, 16, 16))
        );

        panelRounded5.setBackground(new java.awt.Color(125, 160, 250));
        panelRounded5.setRoundBottomLeft(20);
        panelRounded5.setRoundBottomRight(20);
        panelRounded5.setRoundTopLeft(20);
        panelRounded5.setRoundTopRight(20);

        lblDiproses.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 24)); // NOI18N
        lblDiproses.setForeground(new java.awt.Color(255, 255, 255));
        lblDiproses.setText("0");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Pengaduan Diproses");

        javax.swing.GroupLayout panelRounded5Layout = new javax.swing.GroupLayout(panelRounded5);
        panelRounded5.setLayout(panelRounded5Layout);
        panelRounded5Layout.setHorizontalGroup(
            panelRounded5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiproses)
                .addGap(21, 21, 21))
            .addGroup(panelRounded5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        panelRounded5Layout.setVerticalGroup(
            panelRounded5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiproses)
                .addGap(20, 20, 20))
        );

        panelRounded3.setBackground(new java.awt.Color(22, 112, 217));
        panelRounded3.setRoundBottomLeft(20);
        panelRounded3.setRoundBottomRight(20);
        panelRounded3.setRoundTopLeft(20);
        panelRounded3.setRoundTopRight(20);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total User");

        lblUser.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 24)); // NOI18N
        lblUser.setForeground(new java.awt.Color(255, 255, 255));
        lblUser.setText("0");

        javax.swing.GroupLayout panelRounded3Layout = new javax.swing.GroupLayout(panelRounded3);
        panelRounded3.setLayout(panelRounded3Layout);
        panelRounded3Layout.setHorizontalGroup(
            panelRounded3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRounded3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(80, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUser)
                .addGap(14, 14, 14))
        );
        panelRounded3Layout.setVerticalGroup(
            panelRounded3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUser)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelGradient1Layout = new javax.swing.GroupLayout(jPanelGradient1);
        jPanelGradient1.setLayout(jPanelGradient1Layout);
        jPanelGradient1Layout.setHorizontalGroup(
            jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGradient1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(panelRounded3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(panelRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(panelRounded5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(panelRounded4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanelGradient1Layout.setVerticalGroup(
            jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGradient1Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelRounded3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRounded2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRounded5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRounded4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32))
        );

        Left.add(jPanelGradient1, java.awt.BorderLayout.PAGE_START);

        MainUtama.setBackground(new java.awt.Color(204, 204, 204));

        Manage.setBackground(new java.awt.Color(255, 255, 255));
        Manage.setLayout(new java.awt.CardLayout());

        Manajemen.setBackground(new java.awt.Color(255, 255, 255));

        txtNamaLengkap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaLengkapActionPerformed(evt);
            }
        });

        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Nama Lengkap");

        jLabel10.setText("Nik");

        jLabel11.setText("Paswword");

        jLabel12.setText("Username");

        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblUser);

        AksiSimpan.setBackground(new java.awt.Color(89, 209, 129));
        AksiSimpan.setRoundBottomLeft(20);
        AksiSimpan.setRoundBottomRight(20);
        AksiSimpan.setRoundTopLeft(20);
        AksiSimpan.setRoundTopRight(20);

        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambahMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpanLayout = new javax.swing.GroupLayout(AksiSimpan);
        AksiSimpan.setLayout(AksiSimpanLayout);
        AksiSimpanLayout.setHorizontalGroup(
            AksiSimpanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpanLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnTambah)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        AksiSimpanLayout.setVerticalGroup(
            AksiSimpanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AksiSimpanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTambah)
                .addContainerGap())
        );

        AksiEdit.setBackground(new java.awt.Color(0, 188, 212));
        AksiEdit.setRoundBottomLeft(20);
        AksiEdit.setRoundBottomRight(20);
        AksiEdit.setRoundTopLeft(20);
        AksiEdit.setRoundTopRight(20);

        btnUbah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUbah.setForeground(new java.awt.Color(255, 255, 255));
        btnUbah.setText("Edit");
        btnUbah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUbahMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiEditLayout = new javax.swing.GroupLayout(AksiEdit);
        AksiEdit.setLayout(AksiEditLayout);
        AksiEditLayout.setHorizontalGroup(
            AksiEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiEditLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnUbah)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        AksiEditLayout.setVerticalGroup(
            AksiEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnUbah)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AksiHapus.setBackground(new java.awt.Color(255, 107, 110));
        AksiHapus.setRoundBottomLeft(20);
        AksiHapus.setRoundBottomRight(20);
        AksiHapus.setRoundTopLeft(20);
        AksiHapus.setRoundTopRight(20);

        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiHapusLayout = new javax.swing.GroupLayout(AksiHapus);
        AksiHapus.setLayout(AksiHapusLayout);
        AksiHapusLayout.setHorizontalGroup(
            AksiHapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AksiHapusLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(btnHapus)
                .addGap(19, 19, 19))
        );
        AksiHapusLayout.setVerticalGroup(
            AksiHapusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiHapusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHapus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ManajemenLayout = new javax.swing.GroupLayout(Manajemen);
        Manajemen.setLayout(ManajemenLayout);
        ManajemenLayout.setHorizontalGroup(
            ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ManajemenLayout.createSequentialGroup()
                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ManajemenLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(AksiSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(AksiEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(AksiHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ManajemenLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ManajemenLayout.createSequentialGroup()
                                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNamaLengkap)
                                    .addComponent(jLabel6)
                                    .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                                    .addComponent(jLabel12))
                                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(ManajemenLayout.createSequentialGroup()
                                        .addGap(244, 244, 244)
                                        .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(ManajemenLayout.createSequentialGroup()
                                        .addGap(81, 81, 81)
                                        .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10))))))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        ManajemenLayout.setVerticalGroup(
            ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ManajemenLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10))
                .addGap(8, 8, 8)
                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaLengkap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ManajemenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(AksiSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AksiEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AksiHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        Manage.add(Manajemen, "card2");

        Pengaduan.setBackground(new java.awt.Color(255, 255, 255));

        tblPengaduan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblPengaduan);

        AksiSimpan1.setBackground(new java.awt.Color(78, 122, 255));
        AksiSimpan1.setRoundBottomLeft(20);
        AksiSimpan1.setRoundBottomRight(20);
        AksiSimpan1.setRoundTopLeft(20);
        AksiSimpan1.setRoundTopRight(20);

        btnDetail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDetail.setForeground(new java.awt.Color(255, 255, 255));
        btnDetail.setText("Detail");
        btnDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDetailMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan1Layout = new javax.swing.GroupLayout(AksiSimpan1);
        AksiSimpan1.setLayout(AksiSimpan1Layout);
        AksiSimpan1Layout.setHorizontalGroup(
            AksiSimpan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnDetail)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        AksiSimpan1Layout.setVerticalGroup(
            AksiSimpan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDetail)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AksiSimpan3.setBackground(new java.awt.Color(16, 33, 156));
        AksiSimpan3.setForeground(new java.awt.Color(255, 255, 255));
        AksiSimpan3.setRoundBottomLeft(20);
        AksiSimpan3.setRoundBottomRight(20);
        AksiSimpan3.setRoundTopLeft(20);
        AksiSimpan3.setRoundTopRight(20);

        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("Refresh");
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan3Layout = new javax.swing.GroupLayout(AksiSimpan3);
        AksiSimpan3.setLayout(AksiSimpan3Layout);
        AksiSimpan3Layout.setHorizontalGroup(
            AksiSimpan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnRefresh)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        AksiSimpan3Layout.setVerticalGroup(
            AksiSimpan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AksiSimpan3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefresh)
                .addContainerGap())
        );

        PanelDetail.setBackground(new java.awt.Color(255, 255, 255));
        PanelDetail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 188, 212), 2, true));

        AksiSimpan2.setBackground(new java.awt.Color(252, 2, 52));
        AksiSimpan2.setRoundBottomLeft(20);
        AksiSimpan2.setRoundBottomRight(20);
        AksiSimpan2.setRoundTopLeft(20);
        AksiSimpan2.setRoundTopRight(20);

        btnEksport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEksport.setForeground(new java.awt.Color(255, 255, 255));
        btnEksport.setText("Eksport");
        btnEksport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEksportMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan2Layout = new javax.swing.GroupLayout(AksiSimpan2);
        AksiSimpan2.setLayout(AksiSimpan2Layout);
        AksiSimpan2Layout.setHorizontalGroup(
            AksiSimpan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnEksport)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        AksiSimpan2Layout.setVerticalGroup(
            AksiSimpan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AksiSimpan2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEksport)
                .addContainerGap())
        );

        JudulDetail.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        JudulDetail.setText("JL");

        taDeskripsiDetail.setColumns(20);
        taDeskripsiDetail.setRows(5);
        jScrollPane3.setViewportView(taDeskripsiDetail);

        txtTanggalDetail.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        txtTanggalDetail.setText("TP");

        lblStatus.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        lblStatus.setText("SA");

        dsFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel2.setText("Detail Laporan");

        jLabel16.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        jLabel16.setText("Judul Laporan :");

        jLabel17.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        jLabel17.setText("Tanggal Pengaduan :");

        jLabel18.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        jLabel18.setText("Status Aduan :");

        javax.swing.GroupLayout PanelDetailLayout = new javax.swing.GroupLayout(PanelDetail);
        PanelDetail.setLayout(PanelDetailLayout);
        PanelDetailLayout.setHorizontalGroup(
            PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelDetailLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AksiSimpan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
            .addGroup(PanelDetailLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelDetailLayout.createSequentialGroup()
                        .addGroup(PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelDetailLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(JudulDetail))
                            .addGroup(PanelDetailLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(lblStatus))
                            .addGroup(PanelDetailLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(txtTanggalDetail)))
                        .addContainerGap(155, Short.MAX_VALUE))
                    .addGroup(PanelDetailLayout.createSequentialGroup()
                        .addGroup(PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(dsFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(PanelDetailLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelDetailLayout.setVerticalGroup(
            PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelDetailLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JudulDetail)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtTanggalDetail))
                .addGap(19, 19, 19)
                .addGroup(PanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dsFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AksiSimpan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        txtCari.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 188, 212), 2, true));

        AksiSimpan4.setBackground(new java.awt.Color(78, 122, 255));
        AksiSimpan4.setRoundBottomLeft(20);
        AksiSimpan4.setRoundBottomRight(20);
        AksiSimpan4.setRoundTopLeft(20);
        AksiSimpan4.setRoundTopRight(20);

        btnCari.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCariMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan4Layout = new javax.swing.GroupLayout(AksiSimpan4);
        AksiSimpan4.setLayout(AksiSimpan4Layout);
        AksiSimpan4Layout.setHorizontalGroup(
            AksiSimpan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnCari)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        AksiSimpan4Layout.setVerticalGroup(
            AksiSimpan4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCari)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PengaduanLayout = new javax.swing.GroupLayout(Pengaduan);
        Pengaduan.setLayout(PengaduanLayout);
        PengaduanLayout.setHorizontalGroup(
            PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PengaduanLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PengaduanLayout.createSequentialGroup()
                        .addGroup(PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PengaduanLayout.createSequentialGroup()
                                .addComponent(AksiSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(AksiSimpan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                        .addGap(18, 18, 18))
                    .addGroup(PengaduanLayout.createSequentialGroup()
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AksiSimpan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(PanelDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        PengaduanLayout.setVerticalGroup(
            PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PengaduanLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PengaduanLayout.createSequentialGroup()
                        .addGroup(PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AksiSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AksiSimpan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(PengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AksiSimpan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
                    .addComponent(PanelDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );

        Manage.add(Pengaduan, "card3");

        Statistik.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel5.setText("Laporan Statistik");

        javax.swing.GroupLayout panelStatistikLayout = new javax.swing.GroupLayout(panelStatistik);
        panelStatistik.setLayout(panelStatistikLayout);
        panelStatistikLayout.setHorizontalGroup(
            panelStatistikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
        );
        panelStatistikLayout.setVerticalGroup(
            panelStatistikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(panelStatistik);

        javax.swing.GroupLayout StatistikLayout = new javax.swing.GroupLayout(Statistik);
        Statistik.setLayout(StatistikLayout);
        StatistikLayout.setHorizontalGroup(
            StatistikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatistikLayout.createSequentialGroup()
                .addGroup(StatistikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StatistikLayout.createSequentialGroup()
                        .addGap(358, 358, 358)
                        .addComponent(jLabel5))
                    .addGroup(StatistikLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 805, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        StatistikLayout.setVerticalGroup(
            StatistikLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatistikLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Manage.add(Statistik, "card4");

        javax.swing.GroupLayout MainUtamaLayout = new javax.swing.GroupLayout(MainUtama);
        MainUtama.setLayout(MainUtamaLayout);
        MainUtamaLayout.setHorizontalGroup(
            MainUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainUtamaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Manage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        MainUtamaLayout.setVerticalGroup(
            MainUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainUtamaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Manage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Left.add(MainUtama, java.awt.BorderLayout.CENTER);

        getContentPane().add(Left, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ManajemenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ManajemenMouseClicked

    }//GEN-LAST:event_btn_ManajemenMouseClicked

    private void btn_ManajemenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ManajemenMouseEntered
        pn_main.setBackground(new Color(250,250,250) );
        pn_line.setBackground(new Color(0,188,212) );
    }//GEN-LAST:event_btn_ManajemenMouseEntered

    private void btn_ManajemenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ManajemenMouseExited
        pn_main.setBackground(new Color(255,255,255) );
        pn_line.setBackground(new Color(255,255,255) );
    }//GEN-LAST:event_btn_ManajemenMouseExited

    private void btn_LaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LaporanMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_LaporanMouseClicked

    private void btn_LaporanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LaporanMouseEntered
        pn_main1.setBackground(new Color(250,250,250) );
        pn_line1.setBackground(new Color(0,188,212) );
    }//GEN-LAST:event_btn_LaporanMouseEntered

    private void btn_LaporanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LaporanMouseExited
        pn_main1.setBackground(new Color(255,255,255) );
        pn_line1.setBackground(new Color(255,255,255) );
    }//GEN-LAST:event_btn_LaporanMouseExited

    private void btn_AnalisisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AnalisisMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_AnalisisMouseClicked

    private void btn_AnalisisMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AnalisisMouseEntered
        pn_main2.setBackground(new Color(250,250,250) );
        pn_line2.setBackground(new Color(0,188,212) );
    }//GEN-LAST:event_btn_AnalisisMouseEntered

    private void btn_AnalisisMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AnalisisMouseExited
        pn_main2.setBackground(new Color(255,255,255) );
        pn_line2.setBackground(new Color(255,255,255) );
    }//GEN-LAST:event_btn_AnalisisMouseExited

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
            new LoginForm().setVisible(true);
            this.dispose();
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        btnLogout.setOpaque(false);
        btnLogout.repaint();
    }//GEN-LAST:event_btnLogoutMouseExited

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        btnLogout.setOpaque(true);
        btnLogout.setBackground(new Color(220, 220, 220));
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void txtNamaLengkapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaLengkapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaLengkapActionPerformed

    private void btnTambahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambahMouseClicked
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "")) {
        String sql = "INSERT INTO users (nama_lengkap, nik, username, password, role) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, txtNamaLengkap.getText());
        pst.setString(2, txtNik.getText());
        pst.setString(3, txtUsername.getText());
        pst.setString(4, txtPassword.getText()); // Sebaiknya dienkripsi
        pst.setString(5, cmbRole.getSelectedItem().toString());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
        loadTable();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_btnTambahMouseClicked

    private void btnUbahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUbahMouseClicked
    int baris = tblUser.getSelectedRow();
    if (baris >= 0) {
        int id = Integer.parseInt(model.getValueAt(baris, 0).toString());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "")) {
            String sql = "UPDATE users SET nama_lengkap=?, nik=?, username=?, password=?, role=? WHERE id_user=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txtNamaLengkap.getText());
            pst.setString(2, txtNik.getText());
            pst.setString(3, txtUsername.getText());
            pst.setString(4, txtPassword.getText()); // sebaiknya hash
            pst.setString(5, cmbRole.getSelectedItem().toString());
            pst.setInt(6, id);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User berhasil diperbarui!");
            loadTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_btnUbahMouseClicked

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
    int baris = tblUser.getSelectedRow();
    if (baris >= 0) {
        int id = Integer.parseInt(model.getValueAt(baris, 0).toString());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "")) {
            String sql = "DELETE FROM users WHERE id_user=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
            loadTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_btnHapusMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         model.setRowCount(0); // clear tabel
         loadTable();  // ambil data baru dari database
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetailMouseClicked
        tampilkanDetailPengaduan();
    }//GEN-LAST:event_btnDetailMouseClicked

    private void btnEksportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEksportMouseClicked
        eksporDetailKePDF();
        
    }//GEN-LAST:event_btnEksportMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        loadDataPengaduan();
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void btnCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCariMouseClicked
        String keyword = txtCari.getText();
        cariDataLaporan(keyword);
    }//GEN-LAST:event_btnCariMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Palatte.PanelRounded AksiEdit;
    private Palatte.PanelRounded AksiHapus;
    private Palatte.PanelRounded AksiSimpan;
    private Palatte.PanelRounded AksiSimpan1;
    private Palatte.PanelRounded AksiSimpan2;
    private Palatte.PanelRounded AksiSimpan3;
    private Palatte.PanelRounded AksiSimpan4;
    private javax.swing.JLabel JudulDetail;
    private javax.swing.JPanel Left;
    private javax.swing.JPanel MainUtama;
    private javax.swing.JPanel Manage;
    private javax.swing.JPanel Manajemen;
    private javax.swing.JPanel PanelDetail;
    private javax.swing.JPanel Pengaduan;
    private javax.swing.JPanel Right;
    private javax.swing.JPanel Statistik;
    private javax.swing.JLabel btnCari;
    private javax.swing.JLabel btnDetail;
    private javax.swing.JLabel btnEksport;
    private javax.swing.JLabel btnHapus;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnRefresh;
    private javax.swing.JLabel btnTambah;
    private javax.swing.JLabel btnUbah;
    private javax.swing.JLabel btn_Analisis;
    private javax.swing.JLabel btn_Laporan;
    private javax.swing.JLabel btn_Manajemen;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JLabel dsFoto;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private Palatte.JPanelGradient jPanelGradient1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDiproses;
    private javax.swing.JLabel lblPengaduan;
    private javax.swing.JLabel lblSelesai;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUser;
    private Palatte.PanelRounded panelRounded2;
    private Palatte.PanelRounded panelRounded3;
    private Palatte.PanelRounded panelRounded4;
    private Palatte.PanelRounded panelRounded5;
    private javax.swing.JPanel panelStatistik;
    private javax.swing.JPanel pn_line;
    private javax.swing.JPanel pn_line1;
    private javax.swing.JPanel pn_line2;
    private javax.swing.JPanel pn_main;
    private javax.swing.JPanel pn_main1;
    private javax.swing.JPanel pn_main2;
    private javax.swing.JTextArea taDeskripsiDetail;
    private javax.swing.JTable tblPengaduan;
    private javax.swing.JTable tblUser;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNamaLengkap;
    private javax.swing.JTextField txtNik;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JLabel txtTanggalDetail;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    private void tampilkanUser() {
        try {
        model.setRowCount(0);
        Connection conn = Koneksi.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM users");

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_user"),
                rs.getString("nama_lengkap"),
                rs.getString("nik"),
                rs.getString("username"),
                rs.getString("role")
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    private void loadTable() {
        String[] kolom = {"ID", "Nama", "NIK", "Username", "Role"};
    model = new DefaultTableModel(null, kolom);
    tblUser.setModel(model);

    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "SELECT id_user, nama_lengkap, nik, username, role FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Object[] data = {
                rs.getInt("id_user"),
                rs.getString("nama_lengkap"),
                rs.getString("nik"),
                rs.getString("username"),
                rs.getString("role")
            };
            model.addRow(data);
            tblUser.setRowHeight(25);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data users: " + e.getMessage());
    }
  }
    
  //menampilkan data ke tabel pengaduan
  void loadDataPengaduan() {
     String[] kolom = {"No", "Judul Laporan", "Deskripsi", "Tanggal", "Status", "Foto"};
    DefaultTableModel model = new DefaultTableModel(null, kolom) {
        @Override
        public Class<?> getColumnClass(int column) {
            return column == 5 ? javax.swing.ImageIcon.class : Object.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    tblPengaduan.setRowHeight(80);
    tblPengaduan.setModel(model);

    try {
        java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        java.sql.Statement stmt = conn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM pengaduan");

        int no = 1;
        while (rs.next()) {
            String judul = rs.getString("judul_laporan");
            String deskripsi = rs.getString("deskripsi");
            String tanggal = rs.getString("tanggal");
            String status = rs.getString("status");
            String fotoPath = rs.getString("foto_path");

            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(fotoPath);
            java.awt.Image img = icon.getImage().getScaledInstance(100, 70, java.awt.Image.SCALE_SMOOTH);
            javax.swing.ImageIcon thumb = new javax.swing.ImageIcon(img);

            FotoData fotoData = new FotoData(fotoPath, thumb);
            Object[] data = {no++, judul, deskripsi, tanggal, status, fotoData};
            model.addRow(data);
        }

        rs.close(); stmt.close(); conn.close();
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
    }
    }

//    tombol detail pengaduan
    private void tampilkanDetailPengaduan() {
    int selectedRow = tblPengaduan.getSelectedRow();

    if (selectedRow != -1) {
        String judul = tblPengaduan.getValueAt(selectedRow, 1).toString();
        String deskripsi = tblPengaduan.getValueAt(selectedRow, 2).toString();
        String tanggal = tblPengaduan.getValueAt(selectedRow, 3).toString();
        String status = tblPengaduan.getValueAt(selectedRow, 4).toString();

        FotoData fotoData = (FotoData) tblPengaduan.getValueAt(selectedRow, 5);
        if (fotoData != null) {
            java.awt.Image scaled = fotoData.getThumbnail().getImage()
                .getScaledInstance(dsFoto.getWidth(), dsFoto.getHeight(), java.awt.Image.SCALE_SMOOTH);
            dsFoto.setIcon(new javax.swing.ImageIcon(scaled));
        } else {
            dsFoto.setIcon(null);
        }

        JudulDetail.setText(judul);
        taDeskripsiDetail.setText(deskripsi);
        txtTanggalDetail.setText(tanggal);
        lblStatus.setText(status);
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih salah satu data di tabel.");
    }
    }

    private void eksporDetailKePDF() {
    int selectedRow = tblPengaduan.getSelectedRow();
    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih data yang ingin diekspor terlebih dahulu.");
        return;
    }

    try {
        String judul = tblPengaduan.getValueAt(selectedRow, 1).toString();
        String deskripsi = tblPengaduan.getValueAt(selectedRow, 2).toString();
        String tanggal = tblPengaduan.getValueAt(selectedRow, 3).toString();
        String status = tblPengaduan.getValueAt(selectedRow, 4).toString();
        FotoData fotoData = (FotoData) tblPengaduan.getValueAt(selectedRow, 5);
        String fotoPath = (fotoData != null) ? fotoData.getPath() : null;

        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("Laporan_Pengaduan.pdf"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != javax.swing.JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fileChooser.getSelectedFile();
        com.lowagie.text.Document document = new com.lowagie.text.Document();
        com.lowagie.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
        document.open();

        com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 16);
        com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("Laporan Pengaduan", titleFont);
        title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        document.add(new com.lowagie.text.Paragraph("Judul Laporan : " + judul));
        document.add(new com.lowagie.text.Paragraph("Tanggal : " + tanggal));
        document.add(new com.lowagie.text.Paragraph("Status : " + status));
        document.add(new com.lowagie.text.Paragraph("Deskripsi :"));
        document.add(new com.lowagie.text.Paragraph(deskripsi));
        document.add(new com.lowagie.text.Paragraph(" "));
        document.add(new com.lowagie.text.Paragraph("Foto Laporan :"));
        document.add(new com.lowagie.text.Paragraph(" "));

        if (fotoPath != null && new java.io.File(fotoPath).exists()) {
            com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(fotoPath);
            img.scaleToFit(300, 300);
            img.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(img);
        }

        document.close();
        javax.swing.JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor ke PDF!");
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal mengekspor PDF: " + e.getMessage());
        e.printStackTrace();
    }
    }

    private void loadStatistikPerBulan() {
   
    }

    private void loadDataTampilkanDS() {
        try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");

        // Total User
        String sqlUser = "SELECT COUNT(*) AS total_user FROM users";
        Statement stUser = conn.createStatement();
        ResultSet rsUser = stUser.executeQuery(sqlUser);
        if (rsUser.next()) {
            lblUser.setText(rsUser.getString("total_user"));
        }

        // Total Pengaduan
        String sqlPengaduan = "SELECT COUNT(*) AS total_pengaduan FROM pengaduan";
        Statement stPeng = conn.createStatement();
        ResultSet rsPeng = stPeng.executeQuery(sqlPengaduan);
        if (rsPeng.next()) {
            lblPengaduan.setText(rsPeng.getString("total_pengaduan"));
        }

        // Pengaduan dengan status 'Diproses'
        String sqlDiproses = "SELECT COUNT(*) AS total_diproses FROM pengaduan WHERE status = 'Diproses'";
        Statement stDiproses = conn.createStatement();
        ResultSet rsDiproses = stDiproses.executeQuery(sqlDiproses);
        if (rsDiproses.next()) {
            lblDiproses.setText(rsDiproses.getString("total_diproses"));
        }

        // Pengaduan dengan status 'Selesai'
        String sqlSelesai = "SELECT COUNT(*) AS total_selesai FROM pengaduan WHERE status = 'Selesai'";
        Statement stSelesai = conn.createStatement();
        ResultSet rsSelesai = stSelesai.executeQuery(sqlSelesai);
        if (rsSelesai.next()) {
            lblSelesai.setText(rsSelesai.getString("total_selesai"));
        }

        // Tutup semua resources
        rsUser.close(); stUser.close();
        rsPeng.close(); stPeng.close();
        rsDiproses.close(); stDiproses.close();
        rsSelesai.close(); stSelesai.close();
        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data dashboard: " + e.getMessage());
    }
    }

}
