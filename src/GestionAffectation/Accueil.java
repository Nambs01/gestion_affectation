/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GestionAffectation;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class Accueil extends javax.swing.JFrame {
    DatabaseConnection dbConnection = new DatabaseConnection();
    Connection connexion = dbConnection.getConnection();
    Statement stmt;
    PreparedStatement pStmt;
    PreparedStatement pStmt2;
    PreparedStatement pStmt3;
    PreparedStatement pStmt4;
    DefaultTableModel modelTableLieu = new DefaultTableModel();
    DefaultTableModel modelTableEmploye = new DefaultTableModel();
    DefaultTableModel modelTableAffecter = new DefaultTableModel();
    static String idLieuSelectionne;
    static String numEmpSelectionne;
    static String numAffecSelectionne;

    java.util.Date date;
    java.sql.Date sqlDate;
    java.util.Date date2;
    java.sql.Date sqlDate2;
    
    /**
     * Creates new form Accueil
     */
    public Accueil() {
        initComponents();
        
        //Table lieu
        TableLieu.setModel(modelTableLieu);
        modelTableLieu.addColumn("Code postale");
        modelTableLieu.addColumn("Désignation");
        modelTableLieu.addColumn("Province");
        afficherListeLieu();
        
        //Tableau employe
        TableEmploye.setModel(modelTableEmploye);
        modelTableEmploye.addColumn("N° Matricule");
        modelTableEmploye.addColumn("Nom");
        modelTableEmploye.addColumn("Prénoms");
        modelTableEmploye.addColumn("Civilité");
        modelTableEmploye.addColumn("Mail");
        modelTableEmploye.addColumn("Poste");
        modelTableEmploye.addColumn("Lieu");
        afficherListeEmploye();
        
        //Tableau affecter
        TableAffecter.setModel(modelTableAffecter);
        modelTableAffecter.addColumn("N° Affectation");
        modelTableAffecter.addColumn("N° Matricule");
        modelTableAffecter.addColumn("Ancien lieu de travail");
        modelTableAffecter.addColumn("Nouveau Lieu de travail");
        modelTableAffecter.addColumn("Date d'affectation");
        modelTableAffecter.addColumn("Date de la prise de Service");
        afficherListeAffecter();
    }
    
       
    /////////////////////
    /////// Lieu ////////
    /////////////////////

    public void libererChampLieu(){
        idLieuTxt.setText("");
        designTxt.setText("");
        provinceTxt.setText("");
    }
    
    public void afficherListeLieu() {
        modelTableLieu.setRowCount(0);
        try {
            this.stmt =  (Statement) connexion.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM lieu ORDER BY idlieu ASC");
            
           while (rs.next()) {
                modelTableLieu.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void afficherDetailLieu(){
        try{
            int row = TableLieu.getSelectedRow();
            Accueil.idLieuSelectionne = (String) (TableLieu.getModel().getValueAt(row, 0));
            String requet = "SELECT * FROM lieu WHERE idLieu = '"+idLieuSelectionne+"'";
            pStmt = connexion.prepareStatement(requet);
            ResultSet rs;
            rs = pStmt.executeQuery();
            
            if(rs.next()){
                String t1 = rs.getString("idLieu");
                idLieuTxt.setText(t1);
                String t2 = rs.getString("design");
                designTxt.setText(t2);
                String t3 = rs.getString("province");
                provinceTxt.setText(t3);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void supprimerLieu() {
        try {
            if(idLieuTxt.getText().length()!=0){
                String requet = "DELETE FROM lieu WHERE idLieu = ?";
                pStmt = connexion.prepareStatement(requet);
                pStmt.setString(1, idLieuTxt.getText());
                if(JOptionPane.showConfirmDialog(null, "Voulez vous vraiment effectuer cette operation?",
                    "Suppression d'un lieu", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION){
                    pStmt.execute();
                    JOptionPane.showMessageDialog(null, "Suppresion bien effectuée"    );
                }
            }else {
                JOptionPane.showMessageDialog(null, "Veuillez selectionner le lieu que vous desirer supprimer");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
        libererChampLieu();
        afficherListeLieu();
    }
    
    public String getIdLieu(){
        return idLieuSelectionne;
    }
    
    /////////////////////
    ////// Employe //////
    /////////////////////

    public void libererChampEmploye(){
        numEmpTxt.setText("");
        nomTxt.setText("");
        prenomsTxt.setText("");
        civiliteTxt.setText("");
        mailTxt.setText("");
        posteTxt.setText("");
        lieuTxt.setText("");
        designationTxt.setText("");
        valueRecherche.setText("");
    }
    
    public void afficherListeEmploye() {
        modelTableEmploye.setRowCount(0);
        try {
            this.stmt =  (Statement) connexion.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM employe ORDER BY numemp ASC");
            
           while (rs.next()) {
                modelTableEmploye.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3),
                                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void afficherDetailEmploye(){
        try{
            int row = TableEmploye.getSelectedRow();
            Accueil.numEmpSelectionne = (String) (TableEmploye.getModel().getValueAt(row, 0));
            String requet = "SELECT * FROM employe WHERE numEmp = '"+numEmpSelectionne+"'";
            pStmt = connexion.prepareStatement(requet);
            
            ResultSet rs;
            rs = pStmt.executeQuery();
            
            
            if(rs.next()){
                numEmpTxt.setText(rs.getString(1));
                nomTxt.setText(rs.getString(2));
                prenomsTxt.setText(rs.getString(3));
                civiliteTxt.setText(rs.getString(4));
                mailTxt.setText(rs.getString(5));
                posteTxt.setText(rs.getString(6));
                lieuTxt.setText(rs.getString(7));
                getDesignation(rs.getString("lieu"));
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void getDesignation(String lieu){
        String requet2 = "SELECT design FROM lieu WHERE idLieu = ?";
        try {
            pStmt2 = connexion.prepareStatement(requet2);
            pStmt2.setString(1, lieu);
            ResultSet rs2;
            rs2 = pStmt2.executeQuery();
            if(rs2.next()){
                designationTxt.setText(rs2.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    void supprimerEmploye() {
        try {
            if(numEmpTxt.getText().length()!=0){
                String requet = "DELETE FROM employe WHERE numEmp = "+numEmpTxt.getText()+"";
                pStmt = connexion.prepareStatement(requet);
//                pStmt.setString(1, numEmpTxt.getText());
                if(JOptionPane.showConfirmDialog(null, "Voulez vous vraiment effectuer cette operation?",
                    "Suppression d'un(e) employé(e)", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION){
                    pStmt.execute();
                    JOptionPane.showMessageDialog(null, "Suppresion bien effectuée"    );
                }
            }else {
                JOptionPane.showMessageDialog(null, "Veuillez selectionner l'employé que vous desirer supprimer");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
        libererChampEmploye();
        afficherListeEmploye();
    }
    
    public String getnumEmp(){
        return numEmpSelectionne;
    }
    
    
    /////////////////////
    ////// Affecter /////
    /////////////////////
    
    public void libererChampAffecter(){
        numAffecTxt.setText("");
        numEmpAffecTxt.setText("");
        nomAffecTxt.setText("");
        prenomsAffecTxt.setText("");
        ancienLieuTxt.setText("");
        nouveauLieuTxt.setText("");
        lieuTxt.setText("");
        dateAffecTxt.setText("");
        datePriseServiceTxt.setText("");
        Date1Txt.setDate(null);
        Date2Txt.setDate(null);
    }
    
    public void afficherListeAffecter() {
        modelTableAffecter.setRowCount(0);
        try {
            this.stmt =  (Statement) connexion.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM affecter ORDER BY numaffec ASC");
            
           while (rs.next()) {
                modelTableAffecter.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3),
                                            rs.getString(4), rs.getString(5), rs.getString(6)});
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void afficherDetailAffecter(){
        try{
            int row = TableAffecter.getSelectedRow();
            Accueil.numAffecSelectionne = (String) (TableAffecter.getModel().getValueAt(row, 0));
            String requet = "SELECT * FROM affecter WHERE numAffec = '"+numAffecSelectionne+"'";
            pStmt = connexion.prepareStatement(requet);
            
            ResultSet rs;
            rs = pStmt.executeQuery();
            
            
            if(rs.next()){
                numAffecTxt.setText(rs.getString(1));
                numEmpAffecTxt.setText(rs.getString(2));
                dateAffecTxt.setText(rs.getString(5));
                datePriseServiceTxt.setText(rs.getString(6));
                getEmployeInfo(rs.getString(2));
                getAncienLieuInfo(rs.getString(3));
                getNouveauLieuInfo(rs.getString(4));
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void getEmployeInfo(String lieu){
        String numAffecString = lieu;
        int numEmpInt = Integer.parseInt(numAffecString);
        String requet2 = "SELECT nom, prenoms FROM employe WHERE numEmp = "+numEmpInt+"";
        try {
            pStmt2 = connexion.prepareStatement(requet2);
            ResultSet rs2;
            rs2 = pStmt2.executeQuery();
            if(rs2.next()){
                nomAffecTxt.setText(rs2.getString(1));
                prenomsAffecTxt.setText(rs2.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    void getAncienLieuInfo(String lieu){
        String requet3 = "SELECT design FROM lieu WHERE idLieu = '"+lieu+"'";
        try {
            pStmt3 = connexion.prepareStatement(requet3);
            ResultSet rs2;
            rs2 = pStmt3.executeQuery();
            if(rs2.next()){
                ancienLieuTxt.setText(rs2.getString("design")+" ("+lieu+")");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    void getNouveauLieuInfo(String lieu){
        String requet4 = "SELECT design FROM lieu WHERE idLieu = ?";
        try {
            pStmt4 = connexion.prepareStatement(requet4);
            pStmt4.setString(1, lieu);
            ResultSet rs2;
            rs2 = pStmt4.executeQuery();
            if(rs2.next()){
                nouveauLieuTxt.setText(rs2.getString(1)+" ("+lieu+")");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public String getnumAffec(){
        return numAffecSelectionne;
    }
    
//    void supprimerAffecter() {
//        try {
//            if(numEmpTxt.getText().length()!=0){
//                String requet = "DELETE FROM employe WHERE numEmp = "+numEmpTxt.getText()+"";
//                pStmt = connexion.prepareStatement(requet);
////                pStmt.setString(1, numEmpTxt.getText());
//                if(JOptionPane.showConfirmDialog(null, "Voulez vous vraiment effectuer cette operation?",
//                    "Suppression d'un(e) employé(e)", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION){
//                    pStmt.execute();
//                    JOptionPane.showMessageDialog(null, "Suppresion bien effectuée"    );
//                }
//            }else {
//                JOptionPane.showMessageDialog(null, "Veuillez selectionner l'employé que vous desirer supprimer");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        libererChampEmploye();
//        afficherListeEmploye();
//    }
    
//    public String getnumAffec(){
//        return numEmpSelectionne;
//    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        employesBTN = new javax.swing.JButton();
        affecterBTN = new javax.swing.JButton();
        lieuBTN = new javax.swing.JButton();
        QuitterApplication = new javax.swing.JButton();
        container = new javax.swing.JPanel();
        employes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableEmploye = new javax.swing.JTable();
        ajouterEmployeBTN = new javax.swing.JButton();
        supprimerEmployeBTN = new javax.swing.JButton();
        ouvrirModificationEmployeBTN = new javax.swing.JButton();
        actualiserTableEmployeBTN = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        mailTxt = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        numEmpTxt = new javax.swing.JLabel();
        prenomsTxt = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lieuTxt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        nomTxt = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        civiliteTxt = new javax.swing.JLabel();
        posteTxt = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        designationTxt = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        valueRecherche = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        ouvrirHistoriqueBTN = new javax.swing.JButton();
        affecter = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableAffecter = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        numEmpAffecTxt = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        numAffecTxt = new javax.swing.JLabel();
        nomAffecTxt = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        prenomsAffecTxt = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ancienLieuTxt = new javax.swing.JLabel();
        nouveauLieuTxt = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        dateAffecTxt = new javax.swing.JLabel();
        datePriseServiceTxt = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        actualiserAffecter = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        ajouterAffecterBTN = new javax.swing.JButton();
        ouvrirModificationAffecterBTN = new javax.swing.JButton();
        supprimerAffectationBTN = new javax.swing.JButton();
        Date1Txt = new com.toedter.calendar.JDateChooser();
        Date2Txt = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        rechercherEntreDeuxDateBTN = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        lieu = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        designTxt = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        idLieuTxt = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        provinceTxt = new javax.swing.JLabel();
        ouvrirAjouterLieu = new javax.swing.JButton();
        supprimerLieuBTN = new javax.swing.JButton();
        actualiserTableLieuBTN = new javax.swing.JButton();
        ouvrirModificationLieuBTN = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableLieu = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        Title = new javax.swing.JPanel();
        accueilTitle = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        employesTitle = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        affecterTitle = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lieuTitle = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestion d'Affectation");
        setBackground(new java.awt.Color(255, 255, 255));
        setLocation(new java.awt.Point(200, 60));
        setUndecorated(true);
        setResizable(false);

        jPanel5.setBackground(new java.awt.Color(0, 51, 102));

        employesBTN.setBackground(new java.awt.Color(0, 102, 255));
        employesBTN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        employesBTN.setForeground(new java.awt.Color(255, 255, 255));
        employesBTN.setText("EMPLOYES");
        employesBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        employesBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employesBTNActionPerformed(evt);
            }
        });

        affecterBTN.setBackground(new java.awt.Color(0, 102, 255));
        affecterBTN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        affecterBTN.setForeground(new java.awt.Color(255, 255, 255));
        affecterBTN.setText("AFFECTER");
        affecterBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        affecterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                affecterBTNActionPerformed(evt);
            }
        });

        lieuBTN.setBackground(new java.awt.Color(0, 102, 255));
        lieuBTN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lieuBTN.setForeground(new java.awt.Color(255, 255, 255));
        lieuBTN.setText("LIEU");
        lieuBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lieuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lieuBTNActionPerformed(evt);
            }
        });

        QuitterApplication.setBackground(new java.awt.Color(153, 0, 0));
        QuitterApplication.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        QuitterApplication.setForeground(new java.awt.Color(255, 255, 255));
        QuitterApplication.setText("Quitter");
        QuitterApplication.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        QuitterApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitterApplicationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employesBTN, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(lieuBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(affecterBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(QuitterApplication, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(176, 176, 176)
                .addComponent(employesBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(affecterBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lieuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(QuitterApplication, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        container.setBackground(new java.awt.Color(255, 255, 255));
        container.setLayout(new java.awt.CardLayout());

        employes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TableEmploye.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TableEmploye.setModel(new javax.swing.table.DefaultTableModel(
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
        TableEmploye.setRowHeight(30);
        TableEmploye.setRowMargin(1);
        TableEmploye.setSelectionBackground(new java.awt.Color(0, 153, 255));
        TableEmploye.setSelectionForeground(new java.awt.Color(255, 255, 255));
        TableEmploye.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TableEmploye.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TableEmploye.setUpdateSelectionOnSort(false);
        TableEmploye.setVerifyInputWhenFocusTarget(false);
        TableEmploye.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableEmployeMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableEmploye);

        ajouterEmployeBTN.setBackground(new java.awt.Color(0, 102, 51));
        ajouterEmployeBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ajouterEmployeBTN.setForeground(new java.awt.Color(255, 255, 255));
        ajouterEmployeBTN.setText("Ajouter");
        ajouterEmployeBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajouterEmployeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterEmployeBTNActionPerformed(evt);
            }
        });

        supprimerEmployeBTN.setBackground(new java.awt.Color(204, 0, 0));
        supprimerEmployeBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        supprimerEmployeBTN.setForeground(new java.awt.Color(255, 255, 255));
        supprimerEmployeBTN.setText("Supprimer");
        supprimerEmployeBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        supprimerEmployeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerEmployeBTNActionPerformed(evt);
            }
        });

        ouvrirModificationEmployeBTN.setBackground(new java.awt.Color(202, 162, 4));
        ouvrirModificationEmployeBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ouvrirModificationEmployeBTN.setForeground(new java.awt.Color(255, 255, 255));
        ouvrirModificationEmployeBTN.setText("Modifier");
        ouvrirModificationEmployeBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ouvrirModificationEmployeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirModificationEmployeBTNActionPerformed(evt);
            }
        });

        actualiserTableEmployeBTN.setBackground(new java.awt.Color(0, 102, 255));
        actualiserTableEmployeBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        actualiserTableEmployeBTN.setForeground(new java.awt.Color(255, 255, 255));
        actualiserTableEmployeBTN.setText("Actualiser");
        actualiserTableEmployeBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        actualiserTableEmployeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualiserTableEmployeBTNActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Mail :");

        mailTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("N° Matricule :");

        numEmpTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        prenomsTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Nom :");

        lieuTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Poste :");

        nomTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Lieu :");

        civiliteTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        posteTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Prénoms :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Civilité :");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Désignation :");

        designationTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numEmpTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prenomsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(civiliteTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designationTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lieuTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(mailTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(posteTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(484, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(mailTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(numEmpTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(posteTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lieuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prenomsTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(civiliteTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(designationTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel8.setBackground(new java.awt.Color(0, 51, 102));

        jLabel25.setBackground(new java.awt.Color(0, 51, 102));
        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("    Information d'un employé(e)");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(0, 51, 102));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("    Liste des employés");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        valueRecherche.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        valueRecherche.setToolTipText("Nom ou Prenoms*");
        valueRecherche.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        valueRecherche.setCaretColor(new java.awt.Color(0, 0, 51));

        jButton2.setBackground(new java.awt.Color(0, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Rechercher");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        ouvrirHistoriqueBTN.setBackground(new java.awt.Color(0, 102, 255));
        ouvrirHistoriqueBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ouvrirHistoriqueBTN.setForeground(new java.awt.Color(255, 255, 255));
        ouvrirHistoriqueBTN.setText("Historique d'Affectation");
        ouvrirHistoriqueBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ouvrirHistoriqueBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirHistoriqueBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout employesLayout = new javax.swing.GroupLayout(employes);
        employes.setLayout(employesLayout);
        employesLayout.setHorizontalGroup(
            employesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(employesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(employesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(valueRecherche)
                    .addComponent(jButton2)
                    .addComponent(ouvrirHistoriqueBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employesLayout.createSequentialGroup()
                .addGap(0, 24, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(employesLayout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addComponent(ajouterEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ouvrirModificationEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supprimerEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(actualiserTableEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        employesLayout.setVerticalGroup(
            employesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employesLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(employesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employesLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(valueRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(ouvrirHistoriqueBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(employesLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(employesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ajouterEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ouvrirModificationEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supprimerEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(actualiserTableEmployeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        valueRecherche.getAccessibleContext().setAccessibleName("");

        container.add(employes, "card3");

        affecter.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TableAffecter.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TableAffecter.setModel(new javax.swing.table.DefaultTableModel(
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
        TableAffecter.setRowHeight(30);
        TableAffecter.setRowMargin(1);
        TableAffecter.setSelectionBackground(new java.awt.Color(0, 153, 255));
        TableAffecter.setSelectionForeground(new java.awt.Color(255, 255, 255));
        TableAffecter.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TableAffecter.setUpdateSelectionOnSort(false);
        TableAffecter.setVerifyInputWhenFocusTarget(false);
        TableAffecter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableAffecterMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableAffecter);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("N° Matricule :");

        numEmpAffecTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Numéro affectation :");

        numAffecTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        nomAffecTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Nom :");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Prénoms :");

        prenomsAffecTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Ancien lieu :");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Nouveau lieu :");

        ancienLieuTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        nouveauLieuTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Date d'affecatation :");

        dateAffecTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        datePriseServiceTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Date de la prise de service :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(numEmpAffecTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(numAffecTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nomAffecTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prenomsAffecTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nouveauLieuTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ancienLieuTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(datePriseServiceTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(dateAffecTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateAffecTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(numAffecTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(numEmpAffecTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(59, 59, 59))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(datePriseServiceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nouveauLieuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nomAffecTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(prenomsAffecTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(ancienLieuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        actualiserAffecter.setBackground(new java.awt.Color(0, 102, 255));
        actualiserAffecter.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        actualiserAffecter.setForeground(new java.awt.Color(255, 255, 255));
        actualiserAffecter.setText("Actualiser");
        actualiserAffecter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        actualiserAffecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualiserAffecterActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(0, 51, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("    Information d'une affectation ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel13.setBackground(new java.awt.Color(0, 51, 102));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("    Liste des affectations");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        ajouterAffecterBTN.setBackground(new java.awt.Color(0, 102, 51));
        ajouterAffecterBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ajouterAffecterBTN.setForeground(new java.awt.Color(255, 255, 255));
        ajouterAffecterBTN.setText("Ajouter");
        ajouterAffecterBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajouterAffecterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajouterAffecterBTNActionPerformed(evt);
            }
        });

        ouvrirModificationAffecterBTN.setBackground(new java.awt.Color(202, 162, 4));
        ouvrirModificationAffecterBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ouvrirModificationAffecterBTN.setForeground(new java.awt.Color(255, 255, 255));
        ouvrirModificationAffecterBTN.setText("Modifier");
        ouvrirModificationAffecterBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ouvrirModificationAffecterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirModificationAffecterBTNActionPerformed(evt);
            }
        });

        supprimerAffectationBTN.setBackground(new java.awt.Color(204, 0, 0));
        supprimerAffectationBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        supprimerAffectationBTN.setForeground(new java.awt.Color(255, 255, 255));
        supprimerAffectationBTN.setText("Supprimer");
        supprimerAffectationBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        supprimerAffectationBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerAffectationBTNActionPerformed(evt);
            }
        });

        Date1Txt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        Date2Txt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setText("Affectation entre deux date :");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("&");

        rechercherEntreDeuxDateBTN.setBackground(new java.awt.Color(0, 102, 255));
        rechercherEntreDeuxDateBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rechercherEntreDeuxDateBTN.setForeground(new java.awt.Color(255, 255, 255));
        rechercherEntreDeuxDateBTN.setText("Rechercher");
        rechercherEntreDeuxDateBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rechercherEntreDeuxDateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rechercherEntreDeuxDateBTNActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Liste des employés non affecter");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout affecterLayout = new javax.swing.GroupLayout(affecter);
        affecter.setLayout(affecterLayout);
        affecterLayout.setHorizontalGroup(
            affecterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(affecterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, affecterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ajouterAffecterBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ouvrirModificationAffecterBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supprimerAffectationBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(actualiserAffecter, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(328, 328, 328))
            .addGroup(affecterLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(affecterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1059, Short.MAX_VALUE)
                    .addGroup(affecterLayout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(affecterLayout.createSequentialGroup()
                        .addComponent(Date1Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Date2Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(rechercherEntreDeuxDateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(25, 25, 25))
        );
        affecterLayout.setVerticalGroup(
            affecterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(affecterLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(affecterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(affecterLayout.createSequentialGroup()
                        .addGroup(affecterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Date2Txt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(Date1Txt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(affecterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(actualiserAffecter, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ajouterAffecterBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ouvrirModificationAffecterBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supprimerAffectationBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(rechercherEntreDeuxDateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        container.add(affecter, "card4");

        lieu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setDoubleBuffered(false);

        designTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Désignation :");

        idLieuTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Id Lieu :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Province :");

        provinceTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(idLieuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(provinceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(designTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(idLieuTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(provinceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(designTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ouvrirAjouterLieu.setBackground(new java.awt.Color(0, 102, 51));
        ouvrirAjouterLieu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ouvrirAjouterLieu.setForeground(new java.awt.Color(255, 255, 255));
        ouvrirAjouterLieu.setText("Ajouter");
        ouvrirAjouterLieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ouvrirAjouterLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirAjouterLieuActionPerformed(evt);
            }
        });

        supprimerLieuBTN.setBackground(new java.awt.Color(204, 0, 0));
        supprimerLieuBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        supprimerLieuBTN.setForeground(new java.awt.Color(255, 255, 255));
        supprimerLieuBTN.setText("Supprimer");
        supprimerLieuBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        supprimerLieuBTN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supprimerLieuBTNMouseClicked(evt);
            }
        });
        supprimerLieuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerLieuBTNActionPerformed(evt);
            }
        });

        actualiserTableLieuBTN.setBackground(new java.awt.Color(0, 102, 204));
        actualiserTableLieuBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        actualiserTableLieuBTN.setForeground(new java.awt.Color(255, 255, 255));
        actualiserTableLieuBTN.setText("Actualiser");
        actualiserTableLieuBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        actualiserTableLieuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualiserTableLieuBTNActionPerformed(evt);
            }
        });

        ouvrirModificationLieuBTN.setBackground(new java.awt.Color(195, 156, 4));
        ouvrirModificationLieuBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ouvrirModificationLieuBTN.setForeground(new java.awt.Color(255, 255, 255));
        ouvrirModificationLieuBTN.setText("Modifier");
        ouvrirModificationLieuBTN.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ouvrirModificationLieuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirModificationLieuBTNActionPerformed(evt);
            }
        });

        TableLieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TableLieu.setModel(new javax.swing.table.DefaultTableModel(
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
        TableLieu.setRowHeight(30);
        TableLieu.setRowMargin(1);
        TableLieu.setSelectionBackground(new java.awt.Color(0, 153, 255));
        TableLieu.setSelectionForeground(new java.awt.Color(255, 255, 255));
        TableLieu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TableLieu.setUpdateSelectionOnSort(false);
        TableLieu.setVerifyInputWhenFocusTarget(false);
        TableLieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableLieuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableLieu);

        jPanel11.setBackground(new java.awt.Color(0, 51, 102));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("    Information d'une Annexe");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jPanel12.setBackground(new java.awt.Color(0, 51, 102));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("    Liste des Annexes");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout lieuLayout = new javax.swing.GroupLayout(lieu);
        lieu.setLayout(lieuLayout);
        lieuLayout.setHorizontalGroup(
            lieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(lieuLayout.createSequentialGroup()
                .addGroup(lieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lieuLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(lieuLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(lieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1015, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(lieuLayout.createSequentialGroup()
                                .addGap(280, 280, 280)
                                .addComponent(ouvrirAjouterLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ouvrirModificationLieuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(supprimerLieuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(actualiserTableLieuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        lieuLayout.setVerticalGroup(
            lieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lieuLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(lieuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualiserTableLieuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supprimerLieuBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ouvrirModificationLieuBTN, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(ouvrirAjouterLieu, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(49, 49, 49))
        );

        lieuLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {actualiserTableLieuBTN, ouvrirAjouterLieu, ouvrirModificationLieuBTN, supprimerLieuBTN});

        jPanel1.getAccessibleContext().setAccessibleName("Info");

        container.add(lieu, "card5");

        Title.setLayout(new java.awt.CardLayout());

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Gestion d'Affectation");
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout accueilTitleLayout = new javax.swing.GroupLayout(accueilTitle);
        accueilTitle.setLayout(accueilTitleLayout);
        accueilTitleLayout.setHorizontalGroup(
            accueilTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accueilTitleLayout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 46, Short.MAX_VALUE))
        );
        accueilTitleLayout.setVerticalGroup(
            accueilTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accueilTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addContainerGap())
        );

        Title.add(accueilTitle, "card4");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Employes");
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout employesTitleLayout = new javax.swing.GroupLayout(employesTitle);
        employesTitle.setLayout(employesTitleLayout);
        employesTitleLayout.setHorizontalGroup(
            employesTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employesTitleLayout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 46, Short.MAX_VALUE))
        );
        employesTitleLayout.setVerticalGroup(
            employesTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employesTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addContainerGap())
        );

        Title.add(employesTitle, "card5");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Affectation");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout affecterTitleLayout = new javax.swing.GroupLayout(affecterTitle);
        affecterTitle.setLayout(affecterTitleLayout);
        affecterTitleLayout.setHorizontalGroup(
            affecterTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(affecterTitleLayout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 46, Short.MAX_VALUE))
        );
        affecterTitleLayout.setVerticalGroup(
            affecterTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(affecterTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addContainerGap())
        );

        Title.add(affecterTitle, "card2");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Lieu d'Annexe");
        jLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout lieuTitleLayout = new javax.swing.GroupLayout(lieuTitle);
        lieuTitle.setLayout(lieuTitleLayout);
        lieuTitleLayout.setHorizontalGroup(
            lieuTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lieuTitleLayout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 1071, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 46, Short.MAX_VALUE))
        );
        lieuTitleLayout.setVerticalGroup(
            lieuTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lieuTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addContainerGap())
        );

        Title.add(lieuTitle, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lieuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lieuBTNActionPerformed
        container.removeAll();
        container.repaint();
        container.revalidate();
        
        container.add(lieu);
        container.repaint();
        container.revalidate();
        
        Title.removeAll();
        Title.repaint();
        Title.revalidate();
        
        Title.add(lieuTitle);
        Title.repaint();
        Title.revalidate();
    }//GEN-LAST:event_lieuBTNActionPerformed

    private void affecterBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_affecterBTNActionPerformed
        container.removeAll();
        container.repaint();
        container.revalidate();
        
        container.add(affecter);
        container.repaint();
        container.revalidate();
        
        Title.removeAll();
        Title.repaint();
        Title.revalidate();
        
        Title.add(affecterTitle);
        Title.repaint();
        Title.revalidate();
    }//GEN-LAST:event_affecterBTNActionPerformed

    private void employesBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employesBTNActionPerformed
        container.removeAll();
        container.repaint();
        container.revalidate();
        
        container.add(employes);
        container.repaint();
        container.revalidate();
        
        Title.removeAll();
        Title.repaint();
        Title.revalidate();
        
        Title.add(employesTitle);
        Title.repaint();
        Title.revalidate();
    }//GEN-LAST:event_employesBTNActionPerformed

    private void ouvrirAjouterLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirAjouterLieuActionPerformed
        AjoutLieu al = new AjoutLieu();
        al.setVisible(true);
    }//GEN-LAST:event_ouvrirAjouterLieuActionPerformed

    private void supprimerLieuBTNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supprimerLieuBTNMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_supprimerLieuBTNMouseClicked

    private void supprimerLieuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerLieuBTNActionPerformed
        supprimerLieu();
    }//GEN-LAST:event_supprimerLieuBTNActionPerformed

    private void actualiserTableLieuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualiserTableLieuBTNActionPerformed
        libererChampLieu();
        afficherListeLieu();
    }//GEN-LAST:event_actualiserTableLieuBTNActionPerformed

    private void ouvrirModificationLieuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirModificationLieuBTNActionPerformed
        if(idLieuTxt.getText().length() != 0){
            ModificationLieu ml = new ModificationLieu();
            ml.setVisible(true);
        }else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner le lieu que vous desirer modifier");
        }
    }//GEN-LAST:event_ouvrirModificationLieuBTNActionPerformed

    private void TableLieuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableLieuMouseClicked
        afficherDetailLieu();
    }//GEN-LAST:event_TableLieuMouseClicked

    private void ajouterEmployeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterEmployeBTNActionPerformed
        AjoutEmploye ae = new AjoutEmploye();
        ae.setVisible(true);
    }//GEN-LAST:event_ajouterEmployeBTNActionPerformed

    private void supprimerEmployeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerEmployeBTNActionPerformed
        supprimerEmploye();
    }//GEN-LAST:event_supprimerEmployeBTNActionPerformed

    private void ouvrirModificationEmployeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirModificationEmployeBTNActionPerformed
        if(numEmpTxt.getText().length() != 0){
            ModificationEmploye me = new ModificationEmploye();
            me.setVisible(true);
        }else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner l'employé(e) dont vous desirer modifier l'information");
        }
    }//GEN-LAST:event_ouvrirModificationEmployeBTNActionPerformed

    private void TableEmployeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableEmployeMouseClicked
        afficherDetailEmploye();
    }//GEN-LAST:event_TableEmployeMouseClicked

    private void actualiserTableEmployeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualiserTableEmployeBTNActionPerformed
        afficherListeEmploye();
        libererChampEmploye();
        
    }//GEN-LAST:event_actualiserTableEmployeBTNActionPerformed

    private void TableAffecterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableAffecterMouseClicked
        afficherDetailAffecter();
    }//GEN-LAST:event_TableAffecterMouseClicked

    private void actualiserAffecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualiserAffecterActionPerformed
        afficherListeAffecter();
        libererChampAffecter();
    }//GEN-LAST:event_actualiserAffecterActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String sql = "SELECT * FROM employe WHERE nom LIKE '%" + valueRecherche.getText() + "%' OR prenoms LIKE '%" + valueRecherche.getText() + "%' ORDER";
        modelTableEmploye.setRowCount(0);
        try {
            this.stmt =  (Statement) connexion.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            
           while (rs.next()) {
                modelTableEmploye.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3),
                                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ouvrirHistoriqueBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirHistoriqueBTNActionPerformed
        if(numEmpTxt.getText().length() != 0){
            String numEmpString = numEmpTxt.getText();
            int numEmpInt = Integer.parseInt(numEmpString);    
            
            try {
                this.stmt =  (Statement) connexion.createStatement();
                ResultSet rs;
                rs = stmt.executeQuery("SELECT * FROM affecter WHERE numEmpAffec = "+numEmpInt+"");
                
                int i =0;
                while (rs.next()) {
                    i++;
                }
                
                if(i!=0){
                    HistoriqueAffectation  ha= new HistoriqueAffectation();
                    ha.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Aucun historique d'affecation trouvé");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner l'employé(e) dont vous desirer voir l'historique de ses affecations");
        }                                                   
    }//GEN-LAST:event_ouvrirHistoriqueBTNActionPerformed

    private void ajouterAffecterBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajouterAffecterBTNActionPerformed
        AvantAffectation aa = new AvantAffectation();
        aa.setVisible(true);
    }//GEN-LAST:event_ajouterAffecterBTNActionPerformed

    private void ouvrirModificationAffecterBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirModificationAffecterBTNActionPerformed
        if(numAffecTxt.getText().length()!=0){
            ModificationAffecter ma = new ModificationAffecter();
            ma.setVisible(true);
        }else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner l'affectation que vous desirez modifier");
        }
    }//GEN-LAST:event_ouvrirModificationAffecterBTNActionPerformed

    private void supprimerAffectationBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerAffectationBTNActionPerformed
        supprimerAffectation();
    }//GEN-LAST:event_supprimerAffectationBTNActionPerformed

    private void rechercherEntreDeuxDateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechercherEntreDeuxDateBTNActionPerformed

        date = Date1Txt.getDate();
        date2 = Date2Txt.getDate();

        if(date != null && date2 != null){
            rechercheEntre2Dates();
        }else{
            JOptionPane.showMessageDialog(null, "Veuillez bien remplir les champs de recherche");
        }
    }//GEN-LAST:event_rechercherEntreDeuxDateBTNActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            this.stmt =  (Statement) connexion.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM employe WHERE numEmp NOT IN ( SELECT numEmpAffec FROM affecter) ORDER BY numemp ASC");
            
            int i = 0;
            while(rs.next()){
                i++;
            }
            
            if(i!=0){
                ListeEmployesJamaisAffecte lna = new ListeEmployesJamaisAffecte();
                lna.setVisible(true);   
            }else{
                JOptionPane.showMessageDialog(null, "La liste est vide");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void QuitterApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitterApplicationActionPerformed
        if(JOptionPane.showConfirmDialog(null, "Voulez vous vraiment fermer cette application?",
                    "Quitter l'application!", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION){
                    System.exit(0);                    
                }
    }//GEN-LAST:event_QuitterApplicationActionPerformed

    void rechercheEntre2Dates(){
        date = Date1Txt.getDate();
        sqlDate = new java.sql.Date(date.getTime());
        date2 = Date2Txt.getDate();
        sqlDate2 = new java.sql.Date(date2.getTime());
        
        modelTableAffecter.setRowCount(0);
        try {
            String requet = "SELECT * FROM affecter WHERE dateAffec BETWEEN ? AND ?";
            pStmt = connexion.prepareStatement(requet);
            pStmt.setDate(1, sqlDate);
            pStmt.setDate(2, sqlDate2);
            ResultSet rs;
            rs = pStmt.executeQuery();
            
           while (rs.next()) {
                modelTableAffecter.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3),
                                            rs.getString(4), rs.getString(5), rs.getString(6)});
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    void supprimerAffectation() {
        try {
            if(numAffecTxt.getText().length()!=0){
                String numAffecString = numAffecTxt.getText();
                int numAffecInt = Integer.parseInt(numAffecString);
                String requet = "DELETE FROM affecter WHERE numAffec = "+numAffecInt+"";
                pStmt = connexion.prepareStatement(requet);
                if(JOptionPane.showConfirmDialog(null, "Voulez vous vraiment effectuer cette operation?",
                    "Suppression d'une affectation", JOptionPane.YES_NO_OPTION)==JOptionPane.OK_OPTION){
                    pStmt.execute();
                    JOptionPane.showMessageDialog(null, "Suppresion bien effectuée");
                }
            }else {
                JOptionPane.showMessageDialog(null, "Veuillez selectionner l'affectation que vous desirer supprimer");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
        }
        libererChampAffecter();
        afficherListeAffecter();
    }
    
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
            java.util.logging.Logger.getLogger(Accueil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Accueil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Accueil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Accueil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Accueil().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser Date1Txt;
    private com.toedter.calendar.JDateChooser Date2Txt;
    private javax.swing.JButton QuitterApplication;
    private javax.swing.JTable TableAffecter;
    private javax.swing.JTable TableEmploye;
    private javax.swing.JTable TableLieu;
    private javax.swing.JPanel Title;
    private javax.swing.JPanel accueilTitle;
    private javax.swing.JButton actualiserAffecter;
    private javax.swing.JButton actualiserTableEmployeBTN;
    private javax.swing.JButton actualiserTableLieuBTN;
    private javax.swing.JPanel affecter;
    private javax.swing.JButton affecterBTN;
    private javax.swing.JPanel affecterTitle;
    private javax.swing.JButton ajouterAffecterBTN;
    private javax.swing.JButton ajouterEmployeBTN;
    private javax.swing.JLabel ancienLieuTxt;
    private javax.swing.JLabel civiliteTxt;
    private javax.swing.JPanel container;
    private javax.swing.JLabel dateAffecTxt;
    private javax.swing.JLabel datePriseServiceTxt;
    private javax.swing.JLabel designTxt;
    private javax.swing.JLabel designationTxt;
    private javax.swing.JPanel employes;
    private javax.swing.JButton employesBTN;
    private javax.swing.JPanel employesTitle;
    private javax.swing.JLabel idLieuTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel lieu;
    private javax.swing.JButton lieuBTN;
    private javax.swing.JPanel lieuTitle;
    private javax.swing.JLabel lieuTxt;
    private javax.swing.JLabel mailTxt;
    private javax.swing.JLabel nomAffecTxt;
    private javax.swing.JLabel nomTxt;
    private javax.swing.JLabel nouveauLieuTxt;
    private javax.swing.JLabel numAffecTxt;
    private javax.swing.JLabel numEmpAffecTxt;
    private javax.swing.JLabel numEmpTxt;
    private javax.swing.JButton ouvrirAjouterLieu;
    private javax.swing.JButton ouvrirHistoriqueBTN;
    private javax.swing.JButton ouvrirModificationAffecterBTN;
    private javax.swing.JButton ouvrirModificationEmployeBTN;
    private javax.swing.JButton ouvrirModificationLieuBTN;
    private javax.swing.JLabel posteTxt;
    private javax.swing.JLabel prenomsAffecTxt;
    private javax.swing.JLabel prenomsTxt;
    private javax.swing.JLabel provinceTxt;
    private javax.swing.JToggleButton rechercherEntreDeuxDateBTN;
    private javax.swing.JButton supprimerAffectationBTN;
    private javax.swing.JButton supprimerEmployeBTN;
    private javax.swing.JButton supprimerLieuBTN;
    private javax.swing.JTextField valueRecherche;
    // End of variables declaration//GEN-END:variables
}
