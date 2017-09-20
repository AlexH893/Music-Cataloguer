import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.Font;
//sources: stackoverflow, https://docs.oracle.com/

public class Main extends JFrame implements ActionListener {
    public JTextField txtName;
    public JTextField txtTitle;
    public JTextField txtLabel;
    JComboBox < String > genreCombo = new JComboBox(); //creating genre combo box
    JComboBox < Integer > yearCombo = new JComboBox(); // creating year combo box

    JButton btnSubmit = new JButton("Submit");
    JButton btnClear = new JButton("Clear");
    JMenuItem btnAbout = new JMenuItem("About");
    JMenuItem btnExit = new JMenuItem("Exit");
    static JLabel lblStatus = new JLabel("Enter information about an album and click submit.");
    static Main frame = new Main();
    static Connection conn = null;
    Statement s;


    JRadioButton rdbtnCd = new JRadioButton("CD");
    JRadioButton rdbtnVinyl = new JRadioButton("Vinyl");
    public Main() {
        /**
         * Adds components to the GUI.
         */
        getContentPane().setLayout(null);

        JLabel lblMusicCataloguer = new JLabel("Music Cataloguer");
        lblMusicCataloguer.setFont(new Font("Courier New", Font.PLAIN, 18));
        lblMusicCataloguer.setBounds(165, 28, 176, 20);
        setSize(new Dimension(500, 500)); //Setting default window size on startup
        getContentPane().add(lblMusicCataloguer);


        JLabel lblArtistName = new JLabel("Artist Name:");
        lblArtistName.setBounds(10, 88, 79, 14);
        getContentPane().add(lblArtistName);

        JLabel lblAlbumTitle = new JLabel("Album Title:");
        lblAlbumTitle.setBounds(10, 134, 79, 14);
        getContentPane().add(lblAlbumTitle);

        JLabel lblRecordLabel = new JLabel("Record Label:");
        lblRecordLabel.setBounds(10, 177, 89, 14);
        getContentPane().add(lblRecordLabel);

        JLabel lblGenre = new JLabel("Genre:");
        lblGenre.setBounds(10, 219, 46, 14);
        getContentPane().add(lblGenre);

        txtName = new JTextField();
        txtName.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtName.setBounds(91, 85, 165, 20);
        getContentPane().add(txtName);
        txtName.setColumns(10);


        /**
         * This listens for changes in the textfields so when the user goes to enter in a new
         * record the status label will reset colors.
         */
        txtName.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) //this section will execute only when user is editing the JTextField
                {
                    lblStatus.setForeground(Color.black);
                }
            }
        });


        txtTitle = new JTextField();
        txtTitle.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtTitle.setBounds(91, 131, 165, 20);
        getContentPane().add(txtTitle);
        txtTitle.setColumns(10);



        txtLabel = new JTextField();
        txtLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtLabel.setBounds(91, 174, 165, 20);
        getContentPane().add(txtLabel);
        txtLabel.setColumns(10);
        genreCombo.setFont(new Font("Courier New", Font.PLAIN, 11));


        genreCombo.setEditable(true); //enabling user to enter in a value
        genreCombo.setBounds(91, 217, 109, 20);
        getContentPane().add(genreCombo);

        /*
         * The genres couldn't be efficiently added with a loop since there are so many
         * different ones. This will cater to more towards rock and metal fans. Users 
         * are able to enter their own genres as well. 
         */
        genreCombo.addItem("Metalcore");
        genreCombo.addItem("Hardcore");
        genreCombo.addItem("Deathcore");
        genreCombo.addItem("Folk Metal");
        genreCombo.addItem("Death Metal");
        genreCombo.addItem("Black Metal");
        genreCombo.addItem("Power Metal");
        genreCombo.addItem("Heavy Metal");
        genreCombo.addItem("Grindcore");


        rdbtnCd.setBounds(10, 284, 109, 23);
        getContentPane().add(rdbtnCd);

        /**
         * Adding both radio buttons to a group, this prevents user from clicking both
         */
        ButtonGroup group = new ButtonGroup();
        group.add(rdbtnVinyl);
        group.add(rdbtnCd);
        rdbtnVinyl.setBounds(10, 310, 109, 23);
        getContentPane().add(rdbtnVinyl);
        yearCombo.setToolTipText("Tip: You can type in the year and press enter and it will reflect in the dropdown menu.");
        yearCombo.setFont(new Font("Courier New", Font.PLAIN, 11));


        yearCombo.setBounds(91, 257, 109, 20);
        getContentPane().add(yearCombo);

        //  yearCombo.addItem(2017);



        //Loop to add years to the combo box.
        for (int i = 1980; i <= 2018; i++) {
            yearCombo.addItem(i);
        }



        JLabel lblYearReleased = new JLabel("Year:");
        lblYearReleased.setBounds(10, 260, 46, 14);
        getContentPane().add(lblYearReleased);
        btnSubmit.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 18));


        btnSubmit.setBounds(195, 356, 115, 29);
        btnSubmit.addActionListener(this);
        getContentPane().add(btnSubmit);


        btnClear.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 11));
        btnClear.setBounds(215, 406, 79, 23);
        btnClear.addActionListener(this);
        getContentPane().add(btnClear);


        lblStatus.setBounds(125, 314, 305, 14);
        getContentPane().add(lblStatus);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);


        btnExit.addActionListener(this);


        mnFile.add(btnAbout);
        btnAbout.addActionListener(this);
        mnFile.add(btnExit);

    };

    /**
     * Launches the application.
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    frame.setVisible(true);
                    loadDriver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    /*
     * Code in this block will execute, depending on the button clicked.
     * Code for the submit, clear, and menu buttons.
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == btnSubmit) {
            int x = 1;

            /* VALIDATION
             * 
             *Check to see if a format option is selected. 
             * If not, user will have to select one before submitting. 	 
             */
            if (rdbtnCd.isSelected() == false && rdbtnVinyl.isSelected() == false) {
                JOptionPane.showMessageDialog(null, "You must select a format before submitting.");
            } else {
                //Check to see if fields are empty
                if (txtName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Artist name field cannot be empty.");
                } else {
                    if (txtTitle.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Title field cannot be empty.");
                    } else {

                        //Record label field will be allowed to be empty because
                        //music can be released without a label.

                        //Putting values entered into variables
                        String artistName = txtName.getText();
                        String albumTitle = txtTitle.getText();
                        String recordLabel = txtLabel.getText();
                        String genre = (String) genreCombo.getSelectedItem();
                        int year = (int) yearCombo.getSelectedItem();
                        String format = "";



                        //Connect to the database
                        connect();
                        try {
                            /**
                             *  Format selector
                             */
                            if (rdbtnCd.isSelected()) {
                                format = "CD";
                            } else if (rdbtnVinyl.isSelected()) {
                                format = "Vinyl";

                            }

                            /*
                             * If the CD option is selected the record will be added to the CD table.
                             * If the Vinyl option is selected it will be added to the Vinyl table.
                             */
                            if (rdbtnCd.isSelected()) {
                                String query = "INSERT INTO cds VALUES ('" + artistName + "','" + albumTitle + "','" + recordLabel + "','" + genre + "','" + year + "','" + format + "');";
                                Statement stmt = conn.createStatement();
                                stmt.executeUpdate(query); //This line executes the query
                                //JOptionPane.showMessageDialog(this, "Addition successful.");
                                lblStatus.setText("Addition successful.");
                                lblStatus.setText("");

                            } else if (rdbtnVinyl.isSelected()) {
                                String query = "INSERT INTO vinyl VALUES ('" + artistName + "','" + albumTitle + "','" + recordLabel + "','" + genre + "','" + year + "','" + format + "');";
                                Statement stmt = conn.createStatement();
                                stmt.executeUpdate(query); //This line executes the query
                            }

                            /*
                             * Clearing contents of text areas after completion of submission.
                             * Format option will be left in current state in the case of the user
                             * submitting multiples of the same format. Same goes for year.
                             */
                            txtName.setText("");
                            txtTitle.setText("");
                            txtLabel.setText("");
                            txtName.requestFocus(); //Setting focus to name field after submission.
                            genreCombo.setSelectedIndex(0);
                            lblStatus.setForeground(Color.green);
                            lblStatus.setText("Record added sucessfully.");
                        } catch (SQLException e1) {
                            lblStatus.setForeground(Color.red);
                            System.out.println("SQL error during INSERT");
                            e1.printStackTrace();

                        }
                    } //End of format check
                }
            }

        } //End of btnSubmit
        else if (e.getSource() == btnExit) {
            //This line will close the application
            frame.setVisible(false);
        } else if (e.getSource() == btnClear) { //Clears contents for added ease of use.
            txtName.setText("");
            txtTitle.setText("");
            txtLabel.setText("");
            txtName.requestFocus(); //Setting focus to name field after submission.
            genreCombo.setSelectedIndex(0);
        } else if (e.getSource() == btnAbout) {
            //This line will display information about the application.
            JOptionPane.showMessageDialog(null, "<html>This app was created by Alex Haefner with a little bit of help " +
                "from Stack Overflow.<br> I created this app to keep my development skills in Java sharp. " +
                "" +
                "I also have real world use<br> for this application as I have a collection of hundreds of CDs and vinyl " +
                "that were unorganized<br> and this was a good way to store the information about them.");
        }
    } //End of actionPerformed



    /**
     * Connecting to the DB
     */
    private void connect() {
        lblStatus.setText("Connecting to DB..");
        try { //Login details for the DB.
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/music";
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) { //Error is DB connection is unsuccessful .
            JOptionPane.showMessageDialog(null, "Cannot connect to database server");
            // System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }

    }

    /**
     * Loading the driver.
     */
    private static void loadDriver() {
        lblStatus.setText("Loading driver..");
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            lblStatus.setText("Driver error.");
        }
        lblStatus.setText("Driver loaded!");
    }

} //end of class