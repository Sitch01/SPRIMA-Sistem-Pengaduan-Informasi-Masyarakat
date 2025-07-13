/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package dashboard;

//import com.mysql.cj.xdevapi.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import main.Koneksi;
import main.LoginForm;

// Fungsi untuk mengirim email pengaduan ke kepala desa
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import javax.swing.JPanel;

public class frOprator extends javax.swing.JFrame {
    private CardLayout cardLayout;
    private DefaultTableModel model;
    
    public frOprator() {
        initComponents();
        this.setSize(1080, 720);
        this.setLocationRelativeTo(null);
        cardLayout = (CardLayout) MainPanel.getLayout();
        MainPanel.add(PengaduanMasuk, "PengaduanMasuk");
        MainPanel.add(Laporan, "Laporan");
        loadDataPengaduanMasuk();
        loadDataLaporan();
        loadDataDS();
        
        
        
        // Panel manajemen user
            btn_Masuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(MainPanel, "PengaduanMasuk");
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Masuk.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Masuk.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Masuk.setBackground(Color.WHITE);
        }
    });
           
            // Panel data laporan
            btn_Laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(MainPanel, "Laporan");
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn_Laporan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_Laporan.setBackground(new Color(230, 230, 250));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn_Laporan.setBackground(Color.WHITE);
        }
    });
         
    }

    public void kirimEmailPengaduan(String toEmail, String subject, String body) {
    final String fromEmail = "gilamonyet873@gmail.com"; // Ganti dengan email pengirim
    final String password = "adze cupv bttx hgwt"; // Gunakan App Password Gmail

    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail, password);
        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
        JOptionPane.showMessageDialog(null, "Email berhasil dikirim!");
    } catch (MessagingException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal mengirim email: " + e.getMessage());
    }
}
    
    public void kirimWhatsApp(String nomorTujuan, String isiPesan) {
    try {
        String url = "https://api.whatsapp.com/send?phone=" + nomorTujuan + "&text=" + URLEncoder.encode(isiPesan, "UTF-8");
        Desktop.getDesktop().browse(new URI(url));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal membuka WhatsApp Web: " + e.getMessage());
    }
}

     // Fungsi gabungan tombol "Kirim"
public void tombolKirimPengaduan(String judul, String deskripsi, String tanggal) {
    String isi = "Pengaduan Masuk:\n\nJudul: " + judul + "\nDeskripsi: " + deskripsi + "\nTanggal: " + tanggal;

    String[] opsi = {"Kirim via Email", "Kirim via WhatsApp"};
    int pilihan = JOptionPane.showOptionDialog(null, "Pilih metode pengiriman:", "Kirim Pengaduan",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opsi, opsi[0]);

    if (pilihan == 0) {
        kirimEmailPengaduan("saktipalopo7@email.com", "Pengaduan Masuk", isi);
    } else if (pilihan == 1) {
        kirimWhatsApp("6281243082374", isi); // Ganti dengan nomor WA kepala desa
    }
}
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

        tblLaporan.setModel(model);

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

        jPanelGradient1 = new Palatte.JPanelGradient();
        pn_main = new javax.swing.JPanel();
        pn_line = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btn_Masuk = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pn_main1 = new javax.swing.JPanel();
        pn_line1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btn_Laporan = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        Main = new javax.swing.JPanel();
        jPanelGradient2 = new Palatte.JPanelGradient();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        MainPanel = new javax.swing.JPanel();
        PengaduanMasuk = new javax.swing.JPanel();
        AksiSimpan = new Palatte.PanelRounded();
        btnTambah = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPengaduanMasuk = new javax.swing.JTable();
        AksiSimpan1 = new Palatte.PanelRounded();
        btnTambah1 = new javax.swing.JLabel();
        AksiSimpan2 = new Palatte.PanelRounded();
        btnDiproses = new javax.swing.JLabel();
        AksiSimpan3 = new Palatte.PanelRounded();
        btnSelesai = new javax.swing.JLabel();
        Laporan = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();
        panelCari = new javax.swing.JPanel();
        lblJudulDetail = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taDeskripsiDetail = new javax.swing.JTextArea();
        txtTanggalDetail = new javax.swing.JTextField();
        lblStatusDetail = new javax.swing.JLabel();
        lblFotoDetail = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        AksiSimpan4 = new Palatte.PanelRounded();
        btnCari = new javax.swing.JLabel();
        panelRounded5 = new Palatte.PanelRounded();
        lblDiproses = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        panelRounded4 = new Palatte.PanelRounded();
        lblSelesai = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard Operator");

        jPanelGradient1.setColorEnd(new java.awt.Color(238, 249, 255));
        jPanelGradient1.setColorStart(new java.awt.Color(255, 255, 255));

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

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/op.png"))); // NOI18N

        btn_Masuk.setBackground(new java.awt.Color(255, 255, 255));
        btn_Masuk.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btn_Masuk.setText("Pengaduan Masuk");
        btn_Masuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_MasukMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_MasukMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_MasukMouseExited(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Masuk)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        pn_mainLayout.setVerticalGroup(
            pn_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_mainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pn_line, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pn_mainLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btn_Masuk)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 188, 212));
        jLabel1.setText("OPERATOR");

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

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/lp.png"))); // NOI18N

        btn_Laporan.setBackground(new java.awt.Color(255, 255, 255));
        btn_Laporan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btn_Laporan.setText("Laporan");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Laporan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/oprt.png"))); // NOI18N

        jLabel5.setForeground(new java.awt.Color(0, 153, 255));
        jLabel5.setText("Copyright © 2025");

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

        javax.swing.GroupLayout jPanelGradient1Layout = new javax.swing.GroupLayout(jPanelGradient1);
        jPanelGradient1.setLayout(jPanelGradient1Layout);
        jPanelGradient1Layout.setHorizontalGroup(
            jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGradient1Layout.createSequentialGroup()
                .addGroup(jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGradient1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGradient1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(11, 11, 11))))
                    .addGroup(jPanelGradient1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLogout)
                            .addGroup(jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(pn_main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pn_main1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanelGradient1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel5)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanelGradient1Layout.setVerticalGroup(
            jPanelGradient1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGradient1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addComponent(pn_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_main1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 428, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        getContentPane().add(jPanelGradient1, java.awt.BorderLayout.LINE_START);

        Main.setLayout(new java.awt.BorderLayout());

        jPanelGradient2.setColorEnd(new java.awt.Color(5, 209, 248));
        jPanelGradient2.setColorStart(new java.awt.Color(5, 209, 248));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ddesa.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI Variable", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Laporan Pengaduan");

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel10.setText("Desa Karang Karangan");

        javax.swing.GroupLayout jPanelGradient2Layout = new javax.swing.GroupLayout(jPanelGradient2);
        jPanelGradient2.setLayout(jPanelGradient2Layout);
        jPanelGradient2Layout.setHorizontalGroup(
            jPanelGradient2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGradient2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelGradient2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 654, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(19, 19, 19))
        );
        jPanelGradient2Layout.setVerticalGroup(
            jPanelGradient2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelGradient2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelGradient2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGradient2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addComponent(jLabel6))
                .addGap(10, 10, 10))
        );

        Main.add(jPanelGradient2, java.awt.BorderLayout.PAGE_START);

        MainPanel.setBackground(new java.awt.Color(255, 255, 255));
        MainPanel.setLayout(new java.awt.CardLayout());

        AksiSimpan.setBackground(new java.awt.Color(11, 143, 248));
        AksiSimpan.setRoundBottomLeft(20);
        AksiSimpan.setRoundBottomRight(20);
        AksiSimpan.setRoundTopLeft(20);
        AksiSimpan.setRoundTopRight(20);

        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Kirim");
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

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel2.setText("Laporan Masuk");

        tblPengaduanMasuk.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPengaduanMasuk);

        AksiSimpan1.setBackground(new java.awt.Color(154, 183, 209));
        AksiSimpan1.setRoundBottomLeft(20);
        AksiSimpan1.setRoundBottomRight(20);
        AksiSimpan1.setRoundTopLeft(20);
        AksiSimpan1.setRoundTopRight(20);

        btnTambah1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambah1.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah1.setText("Refresh");
        btnTambah1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambah1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan1Layout = new javax.swing.GroupLayout(AksiSimpan1);
        AksiSimpan1.setLayout(AksiSimpan1Layout);
        AksiSimpan1Layout.setHorizontalGroup(
            AksiSimpan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnTambah1)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        AksiSimpan1Layout.setVerticalGroup(
            AksiSimpan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambah1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AksiSimpan2.setBackground(new java.awt.Color(22, 133, 255));
        AksiSimpan2.setRoundBottomLeft(20);
        AksiSimpan2.setRoundBottomRight(20);
        AksiSimpan2.setRoundTopLeft(20);
        AksiSimpan2.setRoundTopRight(20);

        btnDiproses.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDiproses.setForeground(new java.awt.Color(255, 255, 255));
        btnDiproses.setText("Diproses");
        btnDiproses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDiprosesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan2Layout = new javax.swing.GroupLayout(AksiSimpan2);
        AksiSimpan2.setLayout(AksiSimpan2Layout);
        AksiSimpan2Layout.setHorizontalGroup(
            AksiSimpan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnDiproses)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        AksiSimpan2Layout.setVerticalGroup(
            AksiSimpan2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDiproses)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AksiSimpan3.setBackground(new java.awt.Color(59, 218, 157));
        AksiSimpan3.setRoundBottomLeft(20);
        AksiSimpan3.setRoundBottomRight(20);
        AksiSimpan3.setRoundTopLeft(20);
        AksiSimpan3.setRoundTopRight(20);

        btnSelesai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSelesai.setForeground(new java.awt.Color(255, 255, 255));
        btnSelesai.setText("Selesai");
        btnSelesai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSelesaiMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout AksiSimpan3Layout = new javax.swing.GroupLayout(AksiSimpan3);
        AksiSimpan3.setLayout(AksiSimpan3Layout);
        AksiSimpan3Layout.setHorizontalGroup(
            AksiSimpan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnSelesai)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        AksiSimpan3Layout.setVerticalGroup(
            AksiSimpan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AksiSimpan3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSelesai)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PengaduanMasukLayout = new javax.swing.GroupLayout(PengaduanMasuk);
        PengaduanMasuk.setLayout(PengaduanMasukLayout);
        PengaduanMasukLayout.setHorizontalGroup(
            PengaduanMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PengaduanMasukLayout.createSequentialGroup()
                .addGroup(PengaduanMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PengaduanMasukLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PengaduanMasukLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(AksiSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AksiSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AksiSimpan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AksiSimpan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(PengaduanMasukLayout.createSequentialGroup()
                .addGap(377, 377, 377)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PengaduanMasukLayout.setVerticalGroup(
            PengaduanMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PengaduanMasukLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(PengaduanMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PengaduanMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(AksiSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AksiSimpan2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AksiSimpan3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(AksiSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
        );

        MainPanel.add(PengaduanMasuk, "card2");

        Laporan.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel3.setText("Laporan");

        tblLaporan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLaporan.setGridColor(new java.awt.Color(255, 255, 255));
        tblLaporan.setSelectionBackground(new java.awt.Color(51, 153, 255));
        tblLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLaporanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblLaporan);

        panelCari.setBackground(new java.awt.Color(255, 255, 255));
        panelCari.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 188, 212), 2, true));

        lblJudulDetail.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lblJudulDetail.setText("JD");

        taDeskripsiDetail.setColumns(20);
        taDeskripsiDetail.setRows(5);
        jScrollPane3.setViewportView(taDeskripsiDetail);

        lblStatusDetail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatusDetail.setText("S");

        lblFotoDetail.setBackground(new java.awt.Color(204, 204, 204));
        lblFotoDetail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel12.setText("JUDUL LAPORAN :");

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel13.setText("STATUS ADUAN :");

        jLabel14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel14.setText("TANGGAL ADUAN :");

        jLabel15.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel15.setText("DESKRIPSI LAPORAN :");

        javax.swing.GroupLayout panelCariLayout = new javax.swing.GroupLayout(panelCari);
        panelCari.setLayout(panelCariLayout);
        panelCariLayout.setHorizontalGroup(
            panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCariLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(lblStatusDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
            .addGroup(panelCariLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCariLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(32, 32, 32)
                        .addComponent(lblJudulDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(panelCariLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(txtTanggalDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(panelCariLayout.createSequentialGroup()
                        .addGroup(panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblFotoDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelCariLayout.setVerticalGroup(
            panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCariLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJudulDetail)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusDetail)
                    .addComponent(jLabel13))
                .addGap(12, 12, 12)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTanggalDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblFotoDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        txtCari.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 188, 212), 2, true));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel11.setText("Cari Laporan");

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

        panelRounded5.setBackground(new java.awt.Color(125, 160, 250));
        panelRounded5.setRoundBottomLeft(20);
        panelRounded5.setRoundBottomRight(20);
        panelRounded5.setRoundTopLeft(20);
        panelRounded5.setRoundTopRight(20);

        lblDiproses.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 24)); // NOI18N
        lblDiproses.setForeground(new java.awt.Color(255, 255, 255));
        lblDiproses.setText("0");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Pengaduan Diproses");

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
                .addComponent(jLabel16)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelRounded5Layout.setVerticalGroup(
            panelRounded5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDiproses)
                .addGap(20, 20, 20))
        );

        panelRounded4.setBackground(new java.awt.Color(152, 189, 255));
        panelRounded4.setRoundBottomLeft(20);
        panelRounded4.setRoundBottomRight(20);
        panelRounded4.setRoundTopLeft(20);
        panelRounded4.setRoundTopRight(20);

        lblSelesai.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 24)); // NOI18N
        lblSelesai.setForeground(new java.awt.Color(255, 255, 255));
        lblSelesai.setText("0");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Pengaduan Selesai");

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
                .addComponent(jLabel17)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        panelRounded4Layout.setVerticalGroup(
            panelRounded4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRounded4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSelesai)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout LaporanLayout = new javax.swing.GroupLayout(Laporan);
        Laporan.setLayout(LaporanLayout);
        LaporanLayout.setHorizontalGroup(
            LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LaporanLayout.createSequentialGroup()
                        .addGroup(LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(LaporanLayout.createSequentialGroup()
                                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AksiSimpan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelRounded5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelRounded4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))
                    .addGroup(LaporanLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))))
            .addGroup(LaporanLayout.createSequentialGroup()
                .addGap(383, 383, 383)
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        LaporanLayout.setVerticalGroup(
            LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LaporanLayout.createSequentialGroup()
                .addGroup(LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LaporanLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LaporanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelRounded4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelRounded5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LaporanLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(AksiSimpan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(LaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addComponent(panelCari, javax.swing.GroupLayout.PREFERRED_SIZE, 480, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );

        MainPanel.add(Laporan, "card3");

        Main.add(MainPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(Main, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_MasukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_MasukMouseClicked

    }//GEN-LAST:event_btn_MasukMouseClicked

    private void btn_MasukMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_MasukMouseEntered
        pn_main.setBackground(new Color(250,250,250) );
        pn_line.setBackground(new Color(0,188,212) );
    }//GEN-LAST:event_btn_MasukMouseEntered

    private void btn_MasukMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_MasukMouseExited
        pn_main.setBackground(new Color(255,255,255) );
        pn_line.setBackground(new Color(255,255,255) );
    }//GEN-LAST:event_btn_MasukMouseExited

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

    private void btnTambahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambahMouseClicked
        int selectedRow = tblPengaduanMasuk.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih salah satu pengaduan yang ingin dikirim.");
            return;
        }

        // Ambil data dari tabel
        String judul = tblPengaduanMasuk.getValueAt(selectedRow, 1).toString();
        String deskripsi = tblPengaduanMasuk.getValueAt(selectedRow, 2).toString();
        String tanggal = tblPengaduanMasuk.getValueAt(selectedRow, 3).toString();

        // Panggil fungsi kirim
        tombolKirimPengaduan(judul, deskripsi, tanggal);
    }//GEN-LAST:event_btnTambahMouseClicked

    private void btnTambah1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambah1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambah1MouseClicked

    private void btnDiprosesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDiprosesMouseClicked
         int selectedRow = tblPengaduanMasuk.getSelectedRow();
    if (selectedRow != -1) {
        int id = (int) tblPengaduanMasuk.getValueAt(selectedRow, 0); // Ambil kolom ID

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
            String sql = "UPDATE pengaduan SET status = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "Diproses");
            pst.setInt(2, id);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi 'Diproses'");
                loadDataPengaduanMasuk(); // refresh data tabel
                // Panggil metode refresh FrAdmin & FrUser jika perlu
                 // ✅ Sinkronisasi ke FrAdmin dan FrUser
                if (frAdmin.instance != null) {
                    frAdmin.instance.loadDataPengaduan();
                }
                if (frUser.instance != null) {
                    frUser.instance.loadDataPengaduan();
                }
            }

            pst.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih satu baris data terlebih dahulu.");
    }
    }//GEN-LAST:event_btnDiprosesMouseClicked

    private void btnSelesaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelesaiMouseClicked
       int selectedRow = tblPengaduanMasuk.getSelectedRow();
    if (selectedRow != -1) {
        int id = (int) tblPengaduanMasuk.getValueAt(selectedRow, 0); // Ambil kolom ID

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
            String sql = "UPDATE pengaduan SET status = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "Selesai");
            pst.setInt(2, id);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi 'Selesai'");
                loadDataPengaduanMasuk(); // refresh data tabel
                // Panggil metode refresh FrAdmin & FrUser jika perlu
                if (frAdmin.instance != null) {
                    frAdmin.instance.loadDataPengaduan();
                }
                if (frUser.instance != null) {
                    frUser.instance.loadDataPengaduan();
                }
            }

            pst.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih satu baris data terlebih dahulu.");
    }
    }//GEN-LAST:event_btnSelesaiMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        int pilihan = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilihan == JOptionPane.YES_OPTION) {
            new LoginForm().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        btnLogout.setOpaque(true);
        btnLogout.setBackground(new Color(220, 220, 220));
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        btnLogout.setOpaque(false);
        btnLogout.repaint();
    }//GEN-LAST:event_btnLogoutMouseExited

    private void btnCariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCariMouseClicked
                String keyword = txtCari.getText();
                cariDataLaporan(keyword);
    }//GEN-LAST:event_btnCariMouseClicked

    private void tblLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLaporanMouseClicked
         tampilkanDetailLaporan();
    }//GEN-LAST:event_tblLaporanMouseClicked

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
            java.util.logging.Logger.getLogger(frOprator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frOprator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frOprator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frOprator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frOprator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Palatte.PanelRounded AksiSimpan;
    private Palatte.PanelRounded AksiSimpan1;
    private Palatte.PanelRounded AksiSimpan2;
    private Palatte.PanelRounded AksiSimpan3;
    private Palatte.PanelRounded AksiSimpan4;
    private javax.swing.JPanel Laporan;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JPanel PengaduanMasuk;
    private javax.swing.JLabel btnCari;
    private javax.swing.JLabel btnDiproses;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnSelesai;
    private javax.swing.JLabel btnTambah;
    private javax.swing.JLabel btnTambah1;
    private javax.swing.JLabel btn_Laporan;
    private javax.swing.JLabel btn_Masuk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private Palatte.JPanelGradient jPanelGradient1;
    private Palatte.JPanelGradient jPanelGradient2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDiproses;
    private javax.swing.JLabel lblFotoDetail;
    private javax.swing.JLabel lblJudulDetail;
    private javax.swing.JLabel lblSelesai;
    private javax.swing.JLabel lblStatusDetail;
    private javax.swing.JPanel panelCari;
    private Palatte.PanelRounded panelRounded4;
    private Palatte.PanelRounded panelRounded5;
    private javax.swing.JPanel pn_line;
    private javax.swing.JPanel pn_line1;
    private javax.swing.JPanel pn_main;
    private javax.swing.JPanel pn_main1;
    private javax.swing.JTextArea taDeskripsiDetail;
    private javax.swing.JTable tblLaporan;
    private javax.swing.JTable tblPengaduanMasuk;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtTanggalDetail;
    // End of variables declaration//GEN-END:variables

    private void loadDataPengaduanMasuk() {
     String[] kolom = {"ID", "Judul", "Deskripsi", "Tanggal", "Status", "Foto"};
    model = new DefaultTableModel(null, kolom) {
        @Override
        public Class<?> getColumnClass(int column) {
            return (column == 5) ? ImageIcon.class : Object.class;
        }
    };
    tblPengaduanMasuk.setModel(model);

    // Atur tinggi baris dan lebar kolom gambar
    tblPengaduanMasuk.setRowHeight(100);
    tblPengaduanMasuk.getColumnModel().getColumn(5).setPreferredWidth(130);

    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "SELECT * FROM pengaduan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String judul = rs.getString("judul_laporan");
            String deskripsi = rs.getString("deskripsi");
            String tanggal = rs.getString("tanggal");
            String status = rs.getString("status");
            String fotoPath = rs.getString("foto_path");

            ImageIcon icon = null;
            if (fotoPath != null && !fotoPath.isEmpty()) {
                File file = new File(fotoPath);
                if (file.exists()) {
                    ImageIcon originalIcon = new ImageIcon(fotoPath);
                    Image img = originalIcon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                }
            }

            Object[] data = { id, judul, deskripsi, tanggal, status, icon };
            model.addRow(data);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
    }
    }

//    private void updateStatusPengaduan(int id, String diproses) {
//        try {
//        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
//        String sql = "UPDATE pengaduan SET status = ? WHERE id = ?";
//        PreparedStatement pst = conn.prepareStatement(sql);
//        pst.setString(1, status);
//        pst.setInt(2, id);
//
//        int result = pst.executeUpdate();
//        if (result > 0) {
//            JOptionPane.showMessageDialog(this, "Status berhasil diperbarui menjadi: " + status);
//            loadDataPengaduanMasuk(); // refresh tabel
//        } else {
//            JOptionPane.showMessageDialog(this, "Gagal memperbarui status.");
//        }
//
//        pst.close();
//        conn.close();
//    } catch (Exception e) {
//        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//    }
//    }

    private void loadDataLaporan() {
    String[] kolom = {"No", "Judul Laporan", "Tanggal", "Status"};
    DefaultTableModel model = new DefaultTableModel(null, kolom) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Seluruh tabel non-editable
        }
    };

    tblLaporan.setModel(model);
    tblLaporan.setRowHeight(30); // Boleh disesuaikan

    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
        String sql = "SELECT judul_laporan, tanggal, status FROM pengaduan";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        int no = 1;
        while (rs.next()) {
            String judul = rs.getString("judul_laporan");
            String tanggal = rs.getString("tanggal");
            String status = rs.getString("status");

            Object[] data = {no++, judul, tanggal, status};
            model.addRow(data);
        }

        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data laporan: " + e.getMessage());
    }
    }

    private void tampilkanDetailLaporan() {
     int selectedRow = tblLaporan.getSelectedRow();
    if (selectedRow != -1) {
        int row = tblLaporan.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) tblLaporan.getModel();

        String judul = model.getValueAt(row, 1).toString();     // Judul
        String tanggal = model.getValueAt(row, 2).toString();   // Tanggal
        String status = model.getValueAt(row, 3).toString();    // Status

        // Tampilkan ke GUI
        lblJudulDetail.setText(judul);
        txtTanggalDetail.setText(tanggal);
        lblStatusDetail.setText(status);

        // Ambil deskripsi dan gambar dari database langsung berdasarkan judul
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
            String sql = "SELECT deskripsi, foto_path FROM pengaduan WHERE judul_laporan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, judul);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String deskripsi = rs.getString("deskripsi");
                taDeskripsiDetail.setText(deskripsi);

                String fotoPath = rs.getString("foto_path");
                if (fotoPath != null && !fotoPath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(fotoPath);
                    Image image = icon.getImage().getScaledInstance(lblFotoDetail.getWidth(), lblFotoDetail.getHeight(), Image.SCALE_SMOOTH);
                    lblFotoDetail.setIcon(new ImageIcon(image));
                } else {
                    lblFotoDetail.setIcon(null);
                }
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat detail laporan: " + e.getMessage());
        }
    }
    }

    private void loadDataDS() {
        try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprima_projek", "root", "");
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
        rsDiproses.close(); stDiproses.close();
        rsSelesai.close(); stSelesai.close();
        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data dashboard: " + e.getMessage());
    }
    }

   
}
