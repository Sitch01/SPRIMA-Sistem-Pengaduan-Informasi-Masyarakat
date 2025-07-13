/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package dashboard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import main.LoginForm;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import javax.swing.filechooser.FileNameExtensionFilter;
import main.Koneksi;





public class frUser extends javax.swing.JFrame {

private CardLayout cardLayout;
private boolean isLoggedIn = true; 
private boolean pengaduanAda = true;
private DefaultTableModel model;
private File selectedFile = null;
private com.raven.datechooser.DateChooser dateChooser;
private JPopupMenu popupMenu;
private String nikGlobal;
private String passwordBaru;

public static frUser instance;
    private String pathFoto;
    private String nikLogin;
    private String userId;
    private String idUser;
    private int sessionUserId;
    private String selectedFotoPath;



    public frUser(int id_user) {
        initComponents();
        this.sessionUserId = id_user; // ✅ HARUS di-set dulu sebelum load data

        Utama.add(BuatPengaduan, "BuatPengaduan");
        Utama.add(CekStatus, "CekStatus");
        Utama.add(EditProfil, "EditProfil");
        cardLayout = (CardLayout) Utama.getLayout();
        cardLayout.show(Utama, "Pengaduan"); // tampilkan panel awal
        instance = this;

        loadDataPengaduan();
        setupDropdownMenu();
        loadUserData(); // ✅ Pindahkan ke bawah setelah sessionUserId di-set
    

     
        // Inisialisasi model start
        DefaultTableModel model = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int column) {
            // Kolom ke-3 (index 2) adalah gambar
            if (column == 2) return ImageIcon.class;
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Biar tidak bisa diedit langsung di tabel
        }
    };
 

        // Set header kolom
        model.setColumnIdentifiers(new String[]{"Judul Laporan", "Deskripsi", "Foto", "Tanggal"});

        // Set model ke JTable
        tbl_Cek.setModel(model);

        // Sesuaikan tinggi baris agar muat gambar
        tbl_Cek.setRowHeight(80);

        // Opsional: Auto resize kolom
        tbl_Cek.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Simpan model agar bisa diakses global (jika perlu)
        this.model = model;
        
        loadData();
        //END

         //Start
        // Panel Buat Pengaduan
            btn_Buat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(Utama, "BuatPengaduan");
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Buat.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Buat.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Buat.setBackground(Color.WHITE);
        }
    });

       
    // Panel Cek Status
        btn_Cek.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (pengaduanAda) {
                cardLayout.show(Utama, "CekStatus");
            } else {
                JOptionPane.showMessageDialog(null, "Belum ada pengaduan yang tersedia.");
            }
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Cek.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Cek.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Cek.setBackground(Color.WHITE);
        }
    });

//    // Panel Edit Profil
//        btn_Edit.addMouseListener(new java.awt.event.MouseAdapter() {
//        public void mouseClicked(java.awt.event.MouseEvent evt) {
//           
//        }
//
//        public void mouseEntered(java.awt.event.MouseEvent evt) {
//            btn_Edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            btn_Edit.setBackground(new Color(230, 230, 250));
//        }
//
//        public void mouseExited(java.awt.event.MouseEvent evt) {
//            btn_Edit.setBackground(Color.WHITE);
//        }
//    });

        lblPetunjuk.setText(
    "<html><body style='width: 280px; font-family:Segoe UI;'>" +
    "<h3 style='color:#007bff;'>Petunjuk Pengisian Laporan Pengaduan</h3>" +
    "<ol>" +
    "<li><b>Judul Laporan:</b> Tulis secara singkat dan jelas mengenai topik pengaduan Anda.</li>" +
    "<li><b>Tanggal Pengaduan:</b> Pastikan tanggal sesuai dengan waktu kejadian.</li>" +
    "<li><b>Upload Foto:</b> Sertakan Foto, unggah bukti visual seperti foto kondisi atau kejadian.</li>" +
    "<li><b>Deskripsi Laporan:</b> Jelaskan secara rinci mengenai kejadian yang Anda alami. Sertakan informasi seperti:</li>" +
    "<ul>" +
        "<li>Waktu dan tempat kejadian</li>" +
        "<li>Nama pihak yang terlibat (jika diketahui)</li>" +
        "<li>Uraian kronologi kejadian</li>" +
    "</ul>" +
    "<li><b>Bahasa yang Digunakan:</b> Gunakan bahasa yang sopan dan tidak mengandung unsur SARA atau provokasi.</li>" +
    "<li><b>Data yang Diberikan:</b> Pastikan semua data yang diisi benar dan dapat dipertanggungjawabkan.</li>" +
    "</ol>" +
    "<hr>" +
    "<p><b>Catatan Penting:</b><br>" +
    "Laporan yang masuk akan diverifikasi terlebih dahulu oleh petugas. Pastikan laporan Anda tidak mengandung unsur hoaks atau fitnah, karena hal tersebut dapat dikenakan sanksi hukum sesuai peraturan perundang-undangan yang berlaku." +
    "</p>" +
    "<p style='color:darkred;'><i>Semua data bersifat rahasia dan hanya digunakan untuk keperluan penanganan pengaduan.</i></p>" +
    "</body></html>"
    );
        lblPetunjuk.setBackground(Color.WHITE);
        lblPetunjuk.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        //end
}

    private frUser() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    public void resetForm() {
    tfJudul.setText("");
    taDeskripsi.setText("");
    txtTanggal.setText("");
    lblFoto.setIcon(null);
    selectedFile = null;
}


   public void setUSER(String user) {
    this.nikGlobal = user;
    lblNamaUser.setText(user);
}
   
   
    //drop down
    private void setupDropdownMenu() {
        popupMenu = new JPopupMenu();

        // Menu: Edit Profil
        JMenuItem itemEditProfil = new JMenuItem("Edit Profil");
        itemEditProfil.setIcon(new ImageIcon(getClass().getResource("/image/editpr.png"))); // ikon edit
        itemEditProfil.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Edit Profil diklik");
            // atau: new EditProfilForm().setVisible(true);
             if (isLoggedIn) {
                cardLayout.show(Utama, "EditProfil");
            } else {
                JOptionPane.showMessageDialog(null, "Anda belum login.");
            }
        });

        // Menu: Logout
        JMenuItem itemLogout = new JMenuItem("Logout");
        itemLogout.setIcon(new ImageIcon(getClass().getResource("/image/log16.png"))); // ikon logout
        itemLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true); // ganti dengan form login kamu
                dispose();
            }
        });

        // Tambahkan ke popup
        popupMenu.add(itemEditProfil);
        popupMenu.addSeparator();
        popupMenu.add(itemLogout);

        // Tampilkan saat lblMenu diklik
        lblMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(lblMenu, e.getX(), e.getY());
            }
        });
    }
    
    //menampilkan data load
    void loadDataPengaduan() {
    String[] kolom = {"Judul", "Deskripsi", "Foto", "Tanggal", "Status"};
    DefaultTableModel model = new DefaultTableModel(null, kolom) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Membuat semua sel tidak bisa diedit
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) return ImageIcon.class; // Kolom foto
            return String.class;
        }
    };

    tbl_Cek.setModel(model);
    tbl_Cek.setRowHeight(60); // Supaya gambar terlihat

    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "SELECT judul_laporan, deskripsi, foto_path, tanggal, status FROM pengaduan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String judul = rs.getString("judul_laporan");
            String deskripsi = rs.getString("deskripsi");
            String tanggal = rs.getString("tanggal");
            String status = rs.getString("status");
            String fotoPath = rs.getString("foto_path");

            ImageIcon icon = null;
            if (fotoPath != null && !fotoPath.isEmpty()) {
                ImageIcon original = new ImageIcon(fotoPath);
                Image scaled = original.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);
            }

            Object[] data = {judul, deskripsi, icon, tanggal, status};
            model.addRow(data);
        }

        rs.close();
        stmt.close();
        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data pengaduan: " + e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser1 = new com.raven.datechooser.DateChooser();
        BarInformasi = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Main = new javax.swing.JPanel();
        Navbar = new javax.swing.JPanel();
        lblNamaUser = new javax.swing.JLabel();
        lblMenu = new javax.swing.JLabel();
        Buat = new javax.swing.JPanel();
        line = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btn_Buat = new javax.swing.JLabel();
        Buat1 = new javax.swing.JPanel();
        line1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btn_Cek = new javax.swing.JLabel();
        Utama = new javax.swing.JPanel();
        CekStatus = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnRefersh = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_Cek = new javax.swing.JTable();
        EditProfil = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtTempatLahir = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtTanggalLahir = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taAlamat = new javax.swing.JTextArea();
        btnUploudPF = new javax.swing.JButton();
        txtNoTelpon = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        lblPreview = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblFT = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblNIK = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        lblTempatLahir = new javax.swing.JLabel();
        lblNoTelpon = new javax.swing.JLabel();
        lblAlamat = new javax.swing.JLabel();
        lblTanggalLahir = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        BuatPengaduan = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        FormOnline = new javax.swing.JPanel();
        Bar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnIsi = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        tfJudul = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDeskripsi = new javax.swing.JTextArea();
        lblFoto = new javax.swing.JLabel();
        upload = new javax.swing.JButton();
        AksiSimpan3 = new Palatte.PanelRounded();
        btnKirim = new javax.swing.JLabel();
        pnpInfo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblPetunjuk = new javax.swing.JLabel();

        dateChooser1.setTextRefernce(txtTanggal);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Halaman User");

        BarInformasi.setBackground(new java.awt.Color(51, 153, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("SISTEM INFORMASI LAYANAN PENGADUAN MASYARAKAT");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DESA KARANG KARANGAN");

        javax.swing.GroupLayout BarInformasiLayout = new javax.swing.GroupLayout(BarInformasi);
        BarInformasi.setLayout(BarInformasiLayout);
        BarInformasiLayout.setHorizontalGroup(
            BarInformasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarInformasiLayout.createSequentialGroup()
                .addGroup(BarInformasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BarInformasiLayout.createSequentialGroup()
                        .addGap(362, 362, 362)
                        .addComponent(jLabel1))
                    .addGroup(BarInformasiLayout.createSequentialGroup()
                        .addGap(447, 447, 447)
                        .addComponent(jLabel5)))
                .addContainerGap(377, Short.MAX_VALUE))
        );
        BarInformasiLayout.setVerticalGroup(
            BarInformasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarInformasiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        getContentPane().add(BarInformasi, java.awt.BorderLayout.PAGE_START);

        Navbar.setBackground(new java.awt.Color(255, 255, 255));

        lblNamaUser.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblNamaUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-user-20.png"))); // NOI18N
        lblNamaUser.setText("User");

        lblMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-arrow-down-20.png"))); // NOI18N

        Buat.setBackground(new java.awt.Color(255, 255, 255));

        line.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout lineLayout = new javax.swing.GroupLayout(line);
        line.setLayout(lineLayout);
        lineLayout.setHorizontalGroup(
            lineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lineLayout.setVerticalGroup(
            lineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-plus-20.png"))); // NOI18N

        btn_Buat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Buat.setText("Buat Pengaduan");
        btn_Buat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_BuatMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_BuatMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_BuatMouseExited(evt);
            }
        });

        javax.swing.GroupLayout BuatLayout = new javax.swing.GroupLayout(Buat);
        Buat.setLayout(BuatLayout);
        BuatLayout.setHorizontalGroup(
            BuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuatLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Buat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(line, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        BuatLayout.setVerticalGroup(
            BuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BuatLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(BuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Buat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(line, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Buat1.setBackground(new java.awt.Color(255, 255, 255));

        line1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout line1Layout = new javax.swing.GroupLayout(line1);
        line1.setLayout(line1Layout);
        line1Layout.setHorizontalGroup(
            line1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        line1Layout.setVerticalGroup(
            line1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-check-20.png"))); // NOI18N

        btn_Cek.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Cek.setText("Cek Status Pengaduan");
        btn_Cek.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_CekMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_CekMouseExited(evt);
            }
        });

        javax.swing.GroupLayout Buat1Layout = new javax.swing.GroupLayout(Buat1);
        Buat1.setLayout(Buat1Layout);
        Buat1Layout.setHorizontalGroup(
            Buat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Buat1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Cek, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(line1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Buat1Layout.setVerticalGroup(
            Buat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Buat1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Buat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Cek, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(line1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout NavbarLayout = new javax.swing.GroupLayout(Navbar);
        Navbar.setLayout(NavbarLayout);
        NavbarLayout.setHorizontalGroup(
            NavbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavbarLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(Buat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Buat1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNamaUser)
                .addGap(18, 18, 18)
                .addComponent(lblMenu)
                .addGap(13, 13, 13))
        );
        NavbarLayout.setVerticalGroup(
            NavbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Buat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Buat1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNamaUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(NavbarLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblMenu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Utama.setBackground(new java.awt.Color(255, 255, 255));
        Utama.setLayout(new java.awt.CardLayout());

        CekStatus.setBackground(new java.awt.Color(255, 255, 255));
        CekStatus.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel12.setText("CEK STATUS LAYANAN PENGADUAN");

        btnRefersh.setText("Refresh");
        btnRefersh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefershActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(418, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(317, 317, 317)
                .addComponent(btnRefersh)
                .addGap(17, 17, 17))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(btnRefersh))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        CekStatus.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        tbl_Cek.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tbl_Cek);

        CekStatus.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        Utama.add(CekStatus, "card3");

        EditProfil.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel13.setText("EDIT PROFIL");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 188, 212), 2));

        btnSimpan.setBackground(new java.awt.Color(51, 51, 255));
        btnSimpan.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Edit");

        jLabel16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel16.setText("Tempat Lahir");

        jLabel18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel18.setText("Tanggal Lahir");

        jLabel19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel19.setText("Alamat");

        taAlamat.setColumns(20);
        taAlamat.setRows(5);
        jScrollPane3.setViewportView(taAlamat);

        btnUploudPF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUploudPF.setText("Upload Foto");
        btnUploudPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploudPFActionPerformed(evt);
            }
        });

        txtNoTelpon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoTelponActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel20.setText("No Telpon");

        lblPreview.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addContainerGap(368, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNoTelpon, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTempatLahir)
                            .addComponent(txtTanggalLahir))
                        .addGap(163, 163, 163))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(btnSimpan))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnUploudPF)
                                .addGap(18, 18, 18)
                                .addComponent(lblPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(26, 26, 26)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTempatLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTanggalLahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(txtNoTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUploudPF)
                    .addComponent(lblPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(btnSimpan)
                .addGap(15, 15, 15))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 188, 212), 2));

        lblFT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Data Diri");

        lblNIK.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblNIK.setText("NIK");

        lblNama.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblNama.setText("Nama Lengkap");

        lblTempatLahir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblTempatLahir.setText("Tempat Lahir");

        lblNoTelpon.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblNoTelpon.setText("NoTelpon");

        lblAlamat.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblAlamat.setText("Alamat");

        lblTanggalLahir.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblTanggalLahir.setText("Tanggal Lahir");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(lblFT, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 96, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNama)
                    .addComponent(lblNIK)
                    .addComponent(lblNoTelpon)
                    .addComponent(lblTanggalLahir)
                    .addComponent(lblAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTempatLahir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addGap(29, 29, 29)
                .addComponent(lblFT, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(lblNIK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblTempatLahir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTanggalLahir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNoTelpon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAlamat)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout EditProfilLayout = new javax.swing.GroupLayout(EditProfil);
        EditProfil.setLayout(EditProfilLayout);
        EditProfilLayout.setHorizontalGroup(
            EditProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditProfilLayout.createSequentialGroup()
                .addGap(472, 472, 472)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(EditProfilLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );
        EditProfilLayout.setVerticalGroup(
            EditProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditProfilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(39, 39, 39)
                .addGroup(EditProfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Utama.add(EditProfil, "card4");

        BuatPengaduan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        jLabel11.setText("FORM PENGADUAN");

        FormOnline.setBackground(new java.awt.Color(255, 255, 255));
        FormOnline.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 188, 212), 2, true));
        FormOnline.setLayout(new java.awt.BorderLayout());

        Bar.setBackground(new java.awt.Color(0, 188, 212));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel2.setText("Isi Pengaduan");

        javax.swing.GroupLayout BarLayout = new javax.swing.GroupLayout(Bar);
        Bar.setLayout(BarLayout);
        BarLayout.setHorizontalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addContainerGap(428, Short.MAX_VALUE))
        );
        BarLayout.setVerticalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(15, 15, 15))
        );

        FormOnline.add(Bar, java.awt.BorderLayout.PAGE_START);

        pnIsi.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setText("Judul Laporan");

        jLabel17.setText("Tanggal Pengaduan");

        txtTanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalActionPerformed(evt);
            }
        });

        jLabel15.setText("Deskripsi Laporan");

        taDeskripsi.setColumns(20);
        taDeskripsi.setRows(5);
        jScrollPane1.setViewportView(taDeskripsi);

        lblFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        upload.setText("Upload Foto");
        upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadActionPerformed(evt);
            }
        });

        AksiSimpan3.setBackground(new java.awt.Color(16, 33, 156));
        AksiSimpan3.setForeground(new java.awt.Color(255, 255, 255));
        AksiSimpan3.setRoundBottomLeft(20);
        AksiSimpan3.setRoundBottomRight(20);
        AksiSimpan3.setRoundTopLeft(20);
        AksiSimpan3.setRoundTopRight(20);

        btnKirim.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKirim.setForeground(new java.awt.Color(255, 255, 255));
        btnKirim.setText("Kirim");
        btnKirim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKirimMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan3Layout = new javax.swing.GroupLayout(AksiSimpan3);
        AksiSimpan3.setLayout(AksiSimpan3Layout);
        AksiSimpan3Layout.setHorizontalGroup(
            AksiSimpan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnKirim)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        AksiSimpan3Layout.setVerticalGroup(
            AksiSimpan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AksiSimpan3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnKirim)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnIsiLayout = new javax.swing.GroupLayout(pnIsi);
        pnIsi.setLayout(pnIsiLayout);
        pnIsiLayout.setHorizontalGroup(
            pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIsiLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AksiSimpan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upload)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnIsiLayout.createSequentialGroup()
                        .addGroup(pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(61, 61, 61)
                        .addGroup(pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        pnIsiLayout.setVerticalGroup(
            pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIsiLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnIsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(upload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AksiSimpan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        FormOnline.add(pnIsi, java.awt.BorderLayout.CENTER);

        pnpInfo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 188, 212), 2, true));
        pnpInfo.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(0, 188, 212));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel3.setText("Informasi Petunjuk");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3)
                .addContainerGap(246, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnpInfo.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        lblPetunjuk.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblPetunjuk, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPetunjuk, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnpInfo.add(jPanel4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout BuatPengaduanLayout = new javax.swing.GroupLayout(BuatPengaduan);
        BuatPengaduan.setLayout(BuatPengaduanLayout);
        BuatPengaduanLayout.setHorizontalGroup(
            BuatPengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BuatPengaduanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(410, 410, 410))
            .addGroup(BuatPengaduanLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(FormOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(pnpInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        BuatPengaduanLayout.setVerticalGroup(
            BuatPengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BuatPengaduanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(BuatPengaduanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnpInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FormOnline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        Utama.add(BuatPengaduan, "card2");

        javax.swing.GroupLayout MainLayout = new javax.swing.GroupLayout(Main);
        Main.setLayout(MainLayout);
        MainLayout.setHorizontalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Utama, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
                .addContainerGap())
        );
        MainLayout.setVerticalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Utama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(Main, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_BuatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BuatMouseEntered
        Buat.setBackground(new Color(250,250,250) );
        line.setBackground(new Color(0,188,212) );
    }//GEN-LAST:event_btn_BuatMouseEntered

    private void btn_BuatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BuatMouseExited
        Buat.setBackground(new Color(255,255,255) );
        line.setBackground(new Color(255,255,255) );
    }//GEN-LAST:event_btn_BuatMouseExited

    private void btn_CekMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CekMouseEntered
        Buat1.setBackground(new Color(250,250,250) );
        line1.setBackground(new Color(0,188,212) );
    }//GEN-LAST:event_btn_CekMouseEntered

    private void btn_CekMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CekMouseExited
        Buat1.setBackground(new Color(255,255,255) );
        line1.setBackground(new Color(255,255,255) );
    }//GEN-LAST:event_btn_CekMouseExited

    private void btn_BuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BuatMouseClicked

    }//GEN-LAST:event_btn_BuatMouseClicked

    private void uploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadActionPerformed

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Foto");
    int result = fileChooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
        selectedFile = fileChooser.getSelectedFile();
        Image image = new ImageIcon(selectedFile.getAbsolutePath()).getImage();

        int width = lblFoto.getWidth();
        int height = lblFoto.getHeight();

        // Cegah error jika width/height masih 0
        if (width == 0 || height == 0) {
            width = 150;  // Atur default width
            height = 150; // Atur default height
        }

        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        lblFoto.setIcon(new ImageIcon(scaledImage));
    }

    }//GEN-LAST:event_uploadActionPerformed

    private void txtTanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalActionPerformed
        
    }//GEN-LAST:event_txtTanggalActionPerformed

    private void btnRefershActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefershActionPerformed
         model.setRowCount(0); // clear tabel
         loadDataPengaduan();  // ambil data baru dari database
    }//GEN-LAST:event_btnRefershActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpanEditProfil();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnKirimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnKirimMouseClicked
        String judul = tfJudul.getText().trim();
        String deskripsi = taDeskripsi.getText().trim();
        String inputTanggal = txtTanggal.getText().trim();
        String tanggal = "";

        // Format tanggal dari dd-MM-yyyy → yyyy-MM-dd (untuk MySQL)
        try {
            SimpleDateFormat from = new SimpleDateFormat("dd-MM-yyyy"); // Format dari DateChooser
            SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");   // Format MySQL
            Date parsed = from.parse(inputTanggal);
            tanggal = to.format(parsed);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Format tanggal tidak valid!");
            return;
        }

        if (judul.isEmpty() || deskripsi.isEmpty() || tanggal.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(null, "Harap lengkapi semua field termasuk foto.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");

            String sql = "INSERT INTO pengaduan (judul_laporan, deskripsi, foto_path, tanggal) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, judul);
            pst.setString(2, deskripsi);
            pst.setString(3, selectedFile.getAbsolutePath());
            pst.setString(4, tanggal); // format aman untuk MySQL

            pst.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(null, "Pengaduan berhasil dikirim.");
            resetForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnKirimMouseClicked

    private void btnUploudPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploudPFActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Foto");
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        selectedFotoPath = selectedFile.getAbsolutePath();

        ImageIcon icon = new ImageIcon(selectedFotoPath);
        Image image = icon.getImage().getScaledInstance(lblPreview.getWidth(), lblPreview.getHeight(), Image.SCALE_SMOOTH);
        lblPreview.setIcon(new ImageIcon(image));
    }
    }//GEN-LAST:event_btnUploudPFActionPerformed

    private void txtNoTelponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoTelponActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoTelponActionPerformed

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
            java.util.logging.Logger.getLogger(frUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Palatte.PanelRounded AksiSimpan3;
    private javax.swing.JPanel Bar;
    private javax.swing.JPanel BarInformasi;
    private javax.swing.JPanel Buat;
    private javax.swing.JPanel Buat1;
    private javax.swing.JPanel BuatPengaduan;
    private javax.swing.JPanel CekStatus;
    private javax.swing.JPanel EditProfil;
    private javax.swing.JPanel FormOnline;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel Navbar;
    private javax.swing.JPanel Utama;
    private javax.swing.JLabel btnKirim;
    private javax.swing.JButton btnRefersh;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUploudPF;
    private javax.swing.JLabel btn_Buat;
    private javax.swing.JLabel btn_Cek;
    private com.raven.datechooser.DateChooser dateChooser1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblFT;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblNIK;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNamaUser;
    private javax.swing.JLabel lblNoTelpon;
    private javax.swing.JLabel lblPetunjuk;
    private javax.swing.JLabel lblPreview;
    private javax.swing.JLabel lblTanggalLahir;
    private javax.swing.JLabel lblTempatLahir;
    private javax.swing.JPanel line;
    private javax.swing.JPanel line1;
    private javax.swing.JPanel pnIsi;
    private javax.swing.JPanel pnpInfo;
    private javax.swing.JTextArea taAlamat;
    private javax.swing.JTextArea taDeskripsi;
    private javax.swing.JTable tbl_Cek;
    private javax.swing.JTextField tfJudul;
    private javax.swing.JTextField txtNoTelpon;
    private javax.swing.JTextField txtTanggal;
    private javax.swing.JTextField txtTanggalLahir;
    private javax.swing.JTextField txtTempatLahir;
    private javax.swing.JButton upload;
    // End of variables declaration//GEN-END:variables
    //Cek status pengaduan
    private void loadData() {
     DefaultTableModel model = new DefaultTableModel(new String[]{"Judul", "Deskripsi", "Foto", "Tanggal", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Semua kolom non-editable
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) return ImageIcon.class; // Kolom foto
            return String.class;
        }
    };

    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT judul_laporan, deskripsi, foto_path, tanggal, status FROM pengaduan");

        while (rs.next()) {
            String judul = rs.getString("judul_laporan");
            String deskripsi = rs.getString("deskripsi");
            String tanggal = rs.getString("tanggal");
            String status = rs.getString("status");

            String fotoPath = rs.getString("foto_path");
            ImageIcon icon = null;
            if (fotoPath != null && !fotoPath.isEmpty()) {
                ImageIcon originalIcon = new ImageIcon(fotoPath);
                Image image = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                icon = new ImageIcon(image);
            }

            Object[] rowData = {judul, deskripsi, icon, tanggal, status};
            model.addRow(rowData);
        }

        tbl_Cek.setModel(model);
        tbl_Cek.setRowHeight(100);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
    }
  }

    private void loadUserData() {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "SELECT * FROM users WHERE id_user = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, sessionUserId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Sesuaikan label ini dengan label di panel Data Diri kamu
            lblNIK.setText(rs.getString("nik"));
            lblNama.setText(rs.getString("nama_lengkap"));
            lblTempatLahir.setText(rs.getString("tempat_lahir"));
            lblTanggalLahir.setText(rs.getString("tanggal_lahir"));
            lblNoTelpon.setText(rs.getString("no_telpon"));
            lblAlamat.setText(rs.getString("alamat"));

            String fotoPath = rs.getString("foto");
            if (fotoPath != null && !fotoPath.equals("")) {
                ImageIcon imageIcon = new ImageIcon(fotoPath);
                Image image = imageIcon.getImage().getScaledInstance(lblFT.getWidth(), lblFT.getHeight(), Image.SCALE_SMOOTH);
                lblFT.setIcon(new ImageIcon(image));
            }
        }
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal memuat data user.");
    }
 }
    
    public void simpanEditProfil() {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "UPDATE users SET tempat_lahir=?, tanggal_lahir=?, no_telpon=?, alamat=?, foto=? WHERE id_user=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, txtTempatLahir.getText());
        stmt.setString(2, txtTanggalLahir.getText());
        stmt.setString(3, txtNoTelpon.getText());
        stmt.setString(4, taAlamat.getText());

        // Upload foto: Simpan path file yang dipilih
        String fotoPath = selectedFotoPath; // Simpan dari tombol upload
        stmt.setString(5, fotoPath);

        stmt.setInt(6, sessionUserId);
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "Profil berhasil diperbarui.");
        loadUserData(); // Refresh panel data diri
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
}
