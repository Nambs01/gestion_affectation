/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GestionAffectation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;


/**
 *
 * @author Admin
 */
public class AjoutAffecter extends javax.swing.JFrame {
    DatabaseConnection dbConnection = new DatabaseConnection();
    Connection connexion = dbConnection.getConnection();
    PreparedStatement pStmt;
    PreparedStatement pStmt1;
    PreparedStatement pStmt2;
    PreparedStatement pStmt3;
    Statement stmt;
    java.util.Date date;
    java.sql.Date sqlDate;
    String num = null;
    String adresseMail, numAffec, dateAffec, datePriseServicePdf, civilite, nom, prenoms, poste, ancienLieu, nouveauLieu;
    /**
     * Creates new form AjoutAffeceter
     */
    public AjoutAffecter() {
        initComponents();
        remplirChamp();
    }
    
    public void remplirChamp(){
        AvantAffectation aa = new AvantAffectation();
        num = aa.getNumAffecter();
        numEmpAffecTxt.setText(num);
        
        String requet = "SELECT lieu FROM employe WHERE numEmp = "+num+"";
        try {
            pStmt = connexion.prepareStatement(requet);
            ResultSet rs;
            rs = pStmt.executeQuery();
            while(rs.next()){
                ancienLieuTxt.setText(rs.getString(1));
                NouveauLieuDispo(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void NouveauLieuDispo(String ancienLieu){
        try {
            this.stmt =  (java.sql.Statement) connexion.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM lieu WHERE idLieu <> '"+ancienLieu+"'");
            
            while(rs.next()){
                listeNouveauLieu.addItem(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(AjoutEmploye.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ajouterAffectation(){

        date = datePriseService.getDate();
        sqlDate = new java.sql.Date(date.getTime());
        String numEmpString = numEmpAffecTxt.getText();
        int numEmpInt = Integer.parseInt(numEmpString);
        String requet = "INSERT INTO affecter(numEmpAffec, ancienLieu, nouveauLieu, datePriseService) VALUES("+numEmpInt+", ?, ?, ?)";
        String requet2 = "UPDATE employe SET lieu = "+listeNouveauLieu.getSelectedItem().toString()+" WHERE numEmp = ?";
        try {
            pStmt = connexion.prepareStatement(requet);
            pStmt.setString(1, ancienLieuTxt.getText());
            pStmt.setString(2, listeNouveauLieu.getSelectedItem().toString());
            pStmt.setDate(3, sqlDate);
            pStmt.execute();
            
            pStmt2 = connexion.prepareStatement(requet2);
            pStmt2.setInt(1, numEmpInt);
            pStmt2.execute();
            
            JOptionPane.showMessageDialog(null, "Affectation bien effectuée");
            
            
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String requet0 = "SELECT * FROM affecter WHERE numaffec = ( SELECT max(numaffec) FROM affecter)";
            try{
                pStmt = connexion.prepareStatement(requet0);

                 ResultSet rs;
                 rs = pStmt.executeQuery();
                 if(rs.next()){
                     numAffec = rs.getString(1);
                     dateAffec = formatter.format(rs.getDate(5));
                     datePriseServicePdf = formatter.format(rs.getDate(6));
                 }

            }catch (SQLException ex) {
                Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
            }

            String requetInfoEmp = "SELECT * FROM employe WHERE numemp= '"+ numEmpAffecTxt.getText() +"'";
                 try {
                     pStmt2 = connexion.prepareStatement(requetInfoEmp);
                     ResultSet rs1;
                     rs1 = pStmt2.executeQuery();
                     if(rs1.next()){
                         nom = rs1.getString(2);
                         prenoms = rs1.getString(3);
                         civilite = rs1.getString(4);
                         adresseMail = rs1.getString(5);
                         poste = rs1.getString(6);
                     }
                 } catch (SQLException ex) {
                     Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
                 }

             String requetInfoAncienLieu ="SELECT * FROM lieu WHERE idlieu= '"+ ancienLieuTxt.getText() +"'"; 
                 try{
                     pStmt1 = connexion.prepareStatement(requetInfoAncienLieu);
                     ResultSet rs1;
                     rs1 = pStmt1.executeQuery();
                     if(rs1.next()){
                         ancienLieu = rs1.getString(2);
                     }

                 } catch (SQLException ex) {
                     Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
                 }

             String requetInfoNouveauLieu ="SELECT * FROM lieu WHERE idlieu= '"+ listeNouveauLieu.getSelectedItem().toString() +"'"; 
                 try{
                     pStmt3 = connexion.prepareStatement(requetInfoNouveauLieu);
                     ResultSet rs1;
                     rs1 = pStmt3.executeQuery();
                     if(rs1.next()){
                         nouveauLieu = rs1.getString(2);
                     }

                 } catch (SQLException ex) {
                     Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
                 }

            GeneratePDF();
            sendMail();
                        
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Affectation non effectuée! \n Veuillez réessayer svp");
            Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }        
    
    public void GeneratePDF() {
             
        String path = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\Gestion des affectations Java\\pdf\\Arrêté d'affectation N°"+numAffec+".pdf";
            Document document = new Document();
            try {
                
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(path));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                document.open();
                
                
                PdfPTable p1 = new PdfPTable(1);
                p1.setWidthPercentage(30);
               
                PdfPCell c1 = new PdfPCell( new Phrase("Arrêté N°" +numAffec+ " du "+dateAffec));
                p1.addCell(c1).setBorderWidth(0);
                document.add(p1);
                
                document.add(new Paragraph(" "));

                Paragraph p2 = new Paragraph(civilite+" "+nom+" "+prenoms+", qui occupe le poste de "+poste+" à "+ancienLieu+", est affecté à "+nouveauLieu+" pour compter de la date de prise de service "+datePriseServicePdf+".");
                document.add(p2);

                document.add(new Paragraph(" "));

                Paragraph p3 = new Paragraph("Le présent communiqué sera enregistré et communiqué partout où besoin sera.");
                document.add(p3);

                document.close();
                
                JOptionPane.showMessageDialog(null, "PDF Ielecharger");
                
            } catch (DocumentException ex) {
                Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur de telechargement de PDF");
            }  
    }

    public void sendMail (){
        String FromEmail = "angelonambs@gmail.com";
        String FromEmailPasswords = "acggafidracqzhsl";
        String Subject = "Affectation";
        String text = civilite+" "+nom+" "+prenoms+", qui occupe le poste de "+poste+" à "+ancienLieu+", est affecté à "+nouveauLieu+" pour compter de la date de prise de service "+datePriseServicePdf+".\n\n"
                + "Le présent communiqué sera enregistré et communiqué partout où besoin sera.";
        
        Properties properties = new Properties();
            properties.put("mail.smtp.auth","true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.trust", "*");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
        try{
            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication  getPasswordAuthentication(){
                    return new PasswordAuthentication(FromEmail, FromEmailPasswords) ;
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(adresseMail));
            message.setSubject(Subject);
            message.setContent(text,"text/plain");
            Transport.send(message);
            
            JOptionPane.showMessageDialog(null, "Success: email envoyé");
            
        }catch(HeadlessException | MessagingException ex){
            Logger.getLogger(AjoutAffecter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erreur: email non envoyé");
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

        jLabel4 = new javax.swing.JLabel();
        datePriseService = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ancienLieuTxt = new javax.swing.JTextField();
        numEmpAffecTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        listeNouveauLieu = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Code postale de son ancien lieu de travail :");

        datePriseService.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Code postale du lieu de son affectation :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Date de la prise de service :");

        ancienLieuTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ancienLieuTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        ancienLieuTxt.setFocusable(false);
        ancienLieuTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ancienLieuTxtActionPerformed(evt);
            }
        });

        numEmpAffecTxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        numEmpAffecTxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        numEmpAffecTxt.setFocusable(false);
        numEmpAffecTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numEmpAffecTxtActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Confirmer");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Annuler");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Numéro de l'employé(e) à affecter:");

        listeNouveauLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        listeNouveauLieu.setBorder(null);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setBackground(new java.awt.Color(0, 102, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ajout Affectation");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ancienLieuTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(numEmpAffecTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(datePriseService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(listeNouveauLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numEmpAffecTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ancienLieuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listeNouveauLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePriseService, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ancienLieuTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ancienLieuTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ancienLieuTxtActionPerformed

    private void numEmpAffecTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numEmpAffecTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numEmpAffecTxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ajouterAffectation();
        
        
                
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(AjoutAffecter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AjoutAffecter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AjoutAffecter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AjoutAffecter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AjoutAffecter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ancienLieuTxt;
    private com.toedter.calendar.JDateChooser datePriseService;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> listeNouveauLieu;
    private javax.swing.JTextField numEmpAffecTxt;
    // End of variables declaration//GEN-END:variables
}
