import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRootPane;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

import java.io.FileReader;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.Iterator;

public class MyDatabase implements ActionListener,ListSelectionListener,ItemListener
{  
      JFrame myLogInFrame;
      JPanel mainPanel,tablePanel,operationButtonPanel,dataBaseInfo;
      JTextField searchTF;
      JButton viewDataBTN,logInBTN,createTableBTN,insertDataBTN,updateTableBTN,deleteTableBTN,deleteData,tempAddBTN,rowCountBTN,insertColumnBTN,insertColumnBTN1,modifyColumnBTN,modifyColumnBTN1;
      JTextField user,pass;;

      JFrame tempFrame;
      
      DatabaseMetaData metaData ;
      Statement statement;
      Connection dBConnection;
      
      DefaultListModel<String> l1;
      JList<String> list;
      static MyDatabase myDB;
      
      String userID,userPass;
      String tableName;

      ArrayList tablesName=new ArrayList();

      String tempColumnsName[];

      JFrame mainUI;
      JScrollPane pane;

      JTextField []tempTextField,tempTextField1,tempTextField2;
      JLabel []tempLabel,tempLabel1,tempLabel2;

      int fwidth,fheight;
      int count;
      int columnLength=4000;

      int column,rows;
      String columnNullAndNot="NULL";
      String columnTypes="NUMBER";

      String columnRecision,columnScale,modifyColumnName;

      JComboBox<String> box,boxType,boxColumnName;
                
      public static void main(String[] args) 
      {
            myDB=new MyDatabase();
            myDB.LogInPage(); 
      }


      public void LogInPage()
      {
      myLogInFrame=new JFrame();
      
      JLabel logInInfo=new JLabel("Log In");
      logInInfo.setSize(100,20);
      logInInfo.setLocation(70,10);
      myLogInFrame.add(logInInfo);
      
      user=new JTextField();
      user.setSize(130,20);
      user.setLocation(30,40);
      myLogInFrame.add(user);
        
      pass=new JTextField();
      pass.setSize(130,20);
      pass.setLocation(30,70);
      myLogInFrame.add(pass);
      
      logInBTN=new JButton("Submit");
      logInBTN.setSize(100,20);
      logInBTN.setLocation(40,100);
      logInBTN.addActionListener(this);
      myLogInFrame.add(logInBTN);
      
      myLogInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myLogInFrame.setSize(190,180);
      myLogInFrame.setLocation(300,300);
      myLogInFrame.setLayout(null);
      myLogInFrame.setUndecorated(true);
      myLogInFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
      myLogInFrame.setVisible(true);



            }

      public void DBConnection()
      {
            try
            {      
                  FileReader reader=new FileReader("db.properties");     
                  Properties p=new Properties();  
                  p.load(reader);  
                  String driver=p.getProperty("driver");
                  String url=p.getProperty("url");
                  Class.forName(driver.trim());
                  dBConnection=DriverManager.getConnection(url.trim(),userID,userPass);
                  metaData= dBConnection.getMetaData();
                  statement=dBConnection.createStatement();
    
                  String[] types = {"TABLE"};
                  ResultSet tables = metaData.getTables(null, null,null, types);
                  int i=0;
                  while (tables.next()) 
                  {
                        tablesName.add(tables.getString("TABLE_NAME"));
                        i++;  
                  }

                  myDB.UI();
            }
            catch(SQLException e)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,e);
                  LogInPage();
            }
            catch(Exception e)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,e);
                  //System.out.println(e);
            }
      }


      public void UI()
      {
            mainUI=new JFrame();
            Toolkit t=mainUI.getToolkit();
            Dimension screenSize=t.getScreenSize();
            int width=screenSize.width*8/8;
            int height=screenSize.height*7/6;  
            fheight=height-180;
            fwidth=width+5; 
                      
            mainPanel=new JPanel();
            mainPanel.setLayout(null);

            dataBaseInfo= new JPanel();
            dataBaseInfo.setBounds(20,20,fwidth-50,35);
            dataBaseInfo.setBackground(Color.RED);
            mainPanel.add(dataBaseInfo);

            operationButtonPanel= new JPanel();
            operationButtonPanel.setBounds(235,60,fwidth-265,45); 
            operationButtonPanel.setBackground(Color.RED);
            mainPanel.add(operationButtonPanel);

            viewDataBTN=new JButton("View Data");
            viewDataBTN.addActionListener(this);
            operationButtonPanel.add(viewDataBTN);

            insertDataBTN=new JButton("Insert Row");
            insertDataBTN.addActionListener(this);
            operationButtonPanel.add(insertDataBTN);

            insertColumnBTN= new JButton("Insert Column");
            insertColumnBTN.addActionListener(this);
            operationButtonPanel.add(insertColumnBTN);

            modifyColumnBTN=new JButton("Modify Column");
            modifyColumnBTN.addActionListener(this);
            operationButtonPanel.add(modifyColumnBTN);

            rowCountBTN=new JButton("Row Count");
            rowCountBTN.addActionListener(this);
            operationButtonPanel.add(rowCountBTN);

            updateTableBTN=new JButton("Update Table");
            operationButtonPanel.add(updateTableBTN);

            deleteTableBTN=new JButton("Delete Table");
            operationButtonPanel.add(deleteTableBTN);

            createTableBTN=new JButton("Create Table");
            operationButtonPanel.add(createTableBTN);

            deleteData=new JButton("Delete Table");
            operationButtonPanel.add(deleteData);
                      
            searchTF=new JTextField();
            Border tableSearch = BorderFactory.createTitledBorder("Table Search");
            searchTF.setBounds(20,60,200,45); 
            searchTF.setBackground(Color.RED);
            searchTF.setBorder(tableSearch);
            mainPanel.add(searchTF);
            
            l1 = new DefaultListModel<>();
            list = new JList<>(l1); 
            list.setVisibleRowCount(1);
            list.addListSelectionListener(this);


            System.out.println();
            System.out.println();
                  
            Iterator i=tablesName.iterator();
            while(i.hasNext())
            {
                  l1.addElement((String)i.next());
            }

            Border blackline = BorderFactory.createTitledBorder("Table List");
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds( 20,115,200,660); 
            scrollPane.setBackground(Color.RED);
            scrollPane.setBorder(blackline);
            scrollPane.setViewportView(list);
            list.setLayoutOrientation(JList.VERTICAL);
            mainPanel.add(scrollPane);   

            JLabel dataBaseInfoLabel=new JLabel();
            try{
                  //+"  supp. SQL Keywords: "+metaData.getSQLKeywords()
                  dataBaseInfoLabel.setText("URL : "+metaData.getURL() +" User name: "+metaData.getUserName()+" DMMD name :"+metaData.getDatabaseProductName() +"  DBMS version :"+metaData.getDatabaseProductVersion() +"  Driver name :"+metaData.getDriverName()+"  Driver version :"+metaData.getDriverVersion());
            }
            catch(Exception e)
            { 
                  JOptionPane.showMessageDialog(myLogInFrame,e);
                  //System.out.println(e);
            }

            dataBaseInfo.add(dataBaseInfoLabel);
                   
            mainUI.add(mainPanel);
            mainUI.setSize(fwidth,fheight); 
            mainUI.setLocation(0,0);
            mainUI.setTitle("MyDatabase");
            mainUI.setVisible(true);
            mainUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            }


      public void insertTableValue()
      {
            tempLabel=new JLabel[column];
            tempTextField=new JTextField[column];
            
            tempFrame=new JFrame("Insert Data");

            JPanel p=new JPanel();
            p.setLayout(new GridLayout(0,2,10,10));
            //p.setBackground(Color.red);
            
            for(int i=0;i<column;i++)
            {
                  tempLabel[i]=new JLabel("     "+tempColumnsName[i]);
                  tempTextField[i]=new JTextField();
                  p.add(tempLabel[i]);
                  p.add(tempTextField[i]);
            }

            tempAddBTN=new JButton(" insertRow ");
            tempAddBTN.addActionListener(this);
            p.add(tempAddBTN);

            tempFrame.add(p);

            column=column+1;

            tempFrame.setSize(600,40*column);
            tempFrame.setLocation(1,1);
            tempFrame.setResizable(false);
            //tempFrame.setLayout(null);
            tempFrame.setUndecorated(true);
            tempFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
            tempFrame.setVisible(true);
      }

      public void modifyColumn()
      {
            tempFrame=new JFrame("Modify Column");

            JPanel p=new JPanel();
            p.setLayout(new GridLayout(0,2,10,10));
            //p.setBackground(Color.red);

            tempLabel1=new JLabel[7];
            tempTextField1=new JTextField[4];
            
            tempLabel1[0]=new JLabel("  Column");
            p.add(tempLabel1[0]);

            boxColumnName = new JComboBox<>(tempColumnsName);
            boxColumnName.addItemListener(this);
            p.add(boxColumnName);

            tempLabel1[1]=new JLabel(" Nullable");
            p.add(tempLabel1[1]);

            box = new JComboBox<>(new String[]{"NULL","NOT NULL"});
            box.addItemListener(this);
            p.add(box);

            tempLabel1[2]=new JLabel("  DataType");
            p.add(tempLabel1[2]);

            boxType = new JComboBox<>(new String[]{"NUMBER","VARCHAR2","DATE","TIMESTAMP","CHAR","CLOB","BLOB","NVARCHAR2","BINARY_FLOAT","BINARY_DOUBLE"});
            boxType.addItemListener(this);
            p.add(boxType);

            tempLabel1[3]=new JLabel("  Enter Length");
            p.add(tempLabel1[3]);

            tempTextField1[0]=new JTextField();
            tempTextField1[0].setEnabled(false);
            p.add(tempTextField1[0]);

            tempLabel1[4]=new JLabel("  Precision");
            p.add(tempLabel1[4]);

            tempTextField1[1]=new JTextField();
            p.add(tempTextField1[1]);

            tempLabel1[5]=new JLabel("  Scale");
            p.add(tempLabel1[5]);

            tempTextField1[2]=new JTextField();
            p.add(tempTextField1[2]);


            modifyColumnBTN1=new JButton("  Modify Column ");
            modifyColumnBTN1.addActionListener(this);
            p.add(modifyColumnBTN1);

            tempFrame.add(p);

            tempFrame.setSize(400,40*7);
            tempFrame.setLocation(1,1);
            tempFrame.setResizable(false);
            //tempFrame.setLayout(null);
            tempFrame.setUndecorated(true);
            tempFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
            tempFrame.setVisible(true);
      }

      public void modifyColumn1()
      {
            String s=" ";

      

            if(columnTypes=="NUMBER" || columnTypes=="DATE" || columnTypes=="TIMESTAMP" || columnTypes=="CLOB" ||columnTypes=="BLOB" || columnTypes=="BINARY_FLOAT" ||columnTypes=="BINARY_DOUBLE")
                  s="ALTER TABLE  "+tableName+" MODIFY "+modifyColumnName+" "+columnTypes+" "+columnNullAndNot;
            else
                  s="ALTER TABLE  "+tableName+" MODIFY "+modifyColumnName+" "+columnTypes+"("+columnLength+") ";
      
            //columnRecision=tempTextField1[1].getText();
            //columnScale=tempTextField1[2].getText();
           
            System.out.println(s);

            //assert condition:"message";

            try{
                  int x=statement.executeUpdate(s);
                  JOptionPane.showMessageDialog(myLogInFrame,"SuccessFul");
            }
            catch(SQLException e)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,e);
                  //System.out.println(e);
            }
            catch(Exception i)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,i);
                  //System.out.println(i);
            }

      }

      public void insertColumn()
      {
            
            tempFrame=new JFrame("Insert Column");

            JPanel p=new JPanel();
            p.setLayout(new GridLayout(0,2,10,10));
            //p.setBackground(Color.red);

            tempLabel2=new JLabel[6];
            tempTextField2=new JTextField[4];
            
            tempLabel2[0]=new JLabel("  Enter Column Name");
            p.add(tempLabel2[0]);
            tempTextField2[0]=new JTextField();
            p.add(tempTextField2[0]);

            tempLabel2[1]=new JLabel("  Select Column NULL or NOT");
            p.add(tempLabel2[1]);
            box = new JComboBox<>(new String[]{"NULL","NOT NULL"});
            box.addItemListener(this);
            p.add(box);

            tempLabel2[2]=new JLabel("  Select Column Type");
            p.add(tempLabel2[2]);
            boxType = new JComboBox<>(new String[]{"NUMBER","VARCHAR2","DATE","TIMESTAMP","CHAR","CLOB","BLOB","NVARCHAR2","BINARY_FLOAT","BINARY_DOUBLE"});
            boxType.addItemListener(this);
            p.add(boxType);

            tempLabel2[3]=new JLabel("  Enter Length");
            p.add(tempLabel2[3]);
            tempTextField2[1]=new JTextField();
            tempTextField2[1].setEnabled(false);
            p.add(tempTextField2[1]);

            tempLabel2[4]=new JLabel("  Precision");
            p.add(tempLabel2[4]);
            tempTextField2[2]=new JTextField();
            p.add(tempTextField2[2]);

            tempLabel2[5]=new JLabel("  Scale");
            p.add(tempLabel2[5]);
            tempTextField2[3]=new JTextField();
            p.add(tempTextField2[3]);


            insertColumnBTN1=new JButton("  Insert Column ");
            insertColumnBTN1.addActionListener(this);
            p.add(insertColumnBTN1);

            tempFrame.add(p);

            tempFrame.setSize(400,40*7);
            tempFrame.setLocation(1,1);
            tempFrame.setResizable(false);
            //tempFrame.setLayout(null);
            tempFrame.setUndecorated(true);
            tempFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
            tempFrame.setVisible(true);
      }

      public void insertColumn1(){

            columnLength=Integer.parseInt(tempTextField2[1].getText());
            columnRecision=tempTextField2[2].getText();
            columnScale=tempTextField2[3].getText();
           
            String s="ALTER TABLE  "+tableName+" ADD "+modifyColumnName+" "+columnTypes+"("+columnLength+") "+columnNullAndNot;
            System.out.println(s);



            try{
                  int x=statement.executeUpdate(s);
                  JOptionPane.showMessageDialog(myLogInFrame,"SuccessFul");
            }
            catch(SQLException e)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,e);
                  //System.out.println(e);
            }
            catch(Exception i)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,i);
                  //System.out.println(i);
            }

      }

      

      public void insertTableValue1()
      {
            try{

                  String q="insert into "+tableName+"(";
                  int i;
                  for(i=0;i<column-2;i++)
                  {
                        q=q+tempColumnsName[i]+",";
                  }
                  q=q+tempColumnsName[i];

                  q=q+") values('";

                  for(i=0;i<column-2;i++)
                  {
                        q=q+tempTextField[i].getText()+"','";
                  }
                  q=q+tempTextField[i].getText()+"')";

                  System.out.println(q);

                  int x=statement.executeUpdate(q);
                  JOptionPane.showMessageDialog(myLogInFrame,"SuccessFul");
            }
            catch(SQLException e)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,e);
                  //System.out.println(e);
            }
            catch(Exception i)
            {
                  JOptionPane.showMessageDialog(myLogInFrame,i);
                  //System.out.println(i);
            }
      }
   
      public void tableModelView(String tableName) throws Exception
      {
            ResultSet result1=statement.executeQuery("select *from "+tableName);
            ResultSetMetaData resultSetMetaData = result1.getMetaData();

            DefaultTableModel model = new DefaultTableModel();

            column= resultSetMetaData.getColumnCount();

            Object []a={"Column Name", "Data Type" ,  "Nullable"  ,  "Default" ,    "Primary Key"}; 

            for (int i=0;i<a.length ;i++ ) 
            {
                  model.addColumn(a[i]);
            }

            Object columnsName[]=new Object[5]; 


            tempColumnsName=new String[column];

            for (int i=0;i<column ;i++ ) 
            {
                  tempColumnsName[i]=resultSetMetaData.getColumnName(i+1);
                  columnsName[0] =resultSetMetaData.getColumnName(i+1);
                  columnsName[1]=resultSetMetaData.getColumnTypeName(i+1);
                  if(1==resultSetMetaData.isNullable(i+1))
                        columnsName[2]="Yes";
                  else {
                        columnsName[2]="No";
                  }
                  columnsName[3]=resultSetMetaData.getSchemaName(i+1);
                  columnsName[4]=resultSetMetaData.getPrecision(i+1);
                                    
                  model.insertRow(i, columnsName);
            }
                  
                         
            JTable table = new JTable(model);
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.yellow);
            pane = new JScrollPane(table);
            //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setEnabled(false);
            pane.setBounds(235,115,fwidth-265,660);
            Border tableNameb = BorderFactory.createTitledBorder(tableName+"  Table Structure");
            pane.setBackground(Color.RED);
            pane.setBorder(tableNameb);
            mainPanel.add(pane);  
            column= resultSetMetaData.getColumnCount();     
      }

      public void tableDateView(String tableName) throws Exception
      {
            ResultSet result1=statement.executeQuery("select *from "+tableName);
            ResultSetMetaData resultSetMetaData = result1.getMetaData();
            column= resultSetMetaData.getColumnCount();
            String columnsName[]=new String[column];
            int count=1;
            int temp=0;
            while(column>0)
            {                           
                  columnsName[temp]=resultSetMetaData.getColumnName(count);
                  count++;
                  temp++;
                  column--;
            }
            
            DefaultTableModel model = new DefaultTableModel();
            
            Object columnValues[]=new Object[columnsName.length];

            for(int i=0 ; i<columnsName.length;i++)
                  model.addColumn(columnsName[i]);

                  int i=0;
                  while (result1.next()) 
                  { 
                        // int id = rs.getInt("id");
                        //String name = result1.getString("name");
                        // String email = result1.getString("pass");
                        for(int i1=0;i1<columnsName.length;i1++){
                              columnValues[i1]=result1.getString(columnsName[i1]);
                        }
                        model.insertRow(i, columnValues);
                        i++;
                  }
            rows=i;
            JTable table = new JTable(model);
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.yellow);
            pane = new JScrollPane(table);
            //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setEnabled(false);
            pane.setBounds(235,115,fwidth-265,660);
            Border tableNameb = BorderFactory.createTitledBorder(tableName+" Table Data");
            pane.setBackground(Color.RED);
            pane.setBorder(tableNameb);
            mainPanel.add(pane); 
            column= resultSetMetaData.getColumnCount();      
      }

      @Override
      public void valueChanged(ListSelectionEvent listSelectionEvent)
      {
            if(pane != null)
                  mainPanel.remove(pane);

            boolean adjust = listSelectionEvent.getValueIsAdjusting();
            if (!adjust) 
            {        
                  JList list = (JList) listSelectionEvent.getSource();
                  int selections[] = list.getSelectedIndices();
                  Object selectionValues[] = list.getSelectedValues();
                  for (int i = 0, n = selections.length; i < n; i++) 
                  {
                        if (i == 0) 
                        {
                              tableName=list.getSelectedValue().toString();
                              try
                              {
                                    tableModelView(tableName);
                              }
                              catch(Exception e)
                              { 
                                    JOptionPane.showMessageDialog(myLogInFrame,e);
                                    //System.out.println(e);
                              }            
                        }
                  }
            }
      }

      @Override
      public void itemStateChanged(ItemEvent e) 
      {
            if(box==e.getSource())
                  if (e.getStateChange() == ItemEvent.SELECTED) {
                      columnNullAndNot=e.getItem().toString();
                  }
            if(boxType==e.getSource())
                  if (e.getStateChange() == ItemEvent.SELECTED) {
                        columnTypes=e.getItem().toString();
                        if(columnTypes=="NUMBER" || columnTypes=="DATE" || columnTypes=="TIMESTAMP" || columnTypes=="CLOB" ||columnTypes=="BLOB" || columnTypes=="BINARY_FLOAT" ||columnTypes=="BINARY_DOUBLE")
                        {
                              tempTextField1[0].setEnabled(false);
                              tempTextField2[1].setEnabled(false);
                        }
                        else{
                              tempTextField1[0].setEnabled(true);
                              tempTextField2[1].setEnabled(true);
                        }
                  }
            if(boxColumnName==e.getSource())
                  if (e.getStateChange() == ItemEvent.SELECTED) {
                      modifyColumnName=e.getItem().toString();
                  }

      }

      @Override
      public void actionPerformed(ActionEvent evt)
      {
      
            if(logInBTN==evt.getSource())
            {
            
                  userID=user.getText().trim();
                  userPass=pass.getText().trim();
                  myLogInFrame.dispose();
                  myDB.DBConnection();
            }
            else if(viewDataBTN==evt.getSource())
            {
                  try
                  {
                         if(pane != null)
                              mainPanel.remove(pane);

                        tableDateView(tableName);
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                   //     System.out.println(e);
                  }
            }
            else if(insertDataBTN==evt.getSource())
            {
                  try
                  {
                        insertTableValue();
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }

            else if(tempAddBTN==evt.getSource())
            {
                  try
                  {
                        insertTableValue1();
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }

            else if(rowCountBTN==evt.getSource())
            {
                  try
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,"Number of Rows : "+rows);
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }

            else if(insertColumnBTN==evt.getSource())
            {
                  try
                  {
                        insertColumn();
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }

            else if(insertColumnBTN1==evt.getSource())
            {
                  try
                  {
                        insertColumn1();
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }

            else if(modifyColumnBTN==evt.getSource())
            {
                  try
                  {
                        modifyColumn();
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }
            else if(modifyColumnBTN1==evt.getSource())
            {
                  try
                  {
                        modifyColumn1();
                  }
                  catch(Exception e)
                  {
                        JOptionPane.showMessageDialog(myLogInFrame,e);
                        //System.out.println(e);
                  }
            }


            


      }


}












            /*

            public void createTable(String tableName)
            {
                  statement.executeUpdate("create table love(name varchar(10),pass varchar(10))");
            }

       

            
            public void deleteData(String tableName){
                  String t5="101";
                  Statement s=c.createStatement();
                  int a=s.executeUpdate("delete from emp101 where name='"+t5+"'");
                  System.out.println(a);
                  ResultSet result1=s.executeQuery("select*from emp101");
                  while(result1.next()){
                  System.out.println(result1.getString(1));
                  System.out.println(result1.getString(2));}
                  catch(SQLException e){
                  System.out.println(e);}
                  catch(Exception i){
                  System.out.println(i);}
            }

        

            public void databaseTypeInfo()
            {
                  ResultSet resultSet=metaData.getTypeInfo();
                  while(resultSet.next())
                  {
                        String typeName=resultSet.getString("TYPE_NAME");
                        short dataType=resultSet.getShort("DATA_TYPE");
                        System.out.println(typeName+"="+ dataType); 
                  }
            }
    

      public void tableUpdate()
      {
            try{
            String s1="update emp101 set name='neesa333' where pass='Anand'";
            String s2="insert emp101 values('bjp2018','modi2030')";
            String s3="delete from emp101 where pass='modi2028'";
            statement.addBatch(s1);
            statement.addBatch(s2);
            statement.addBatch(s3);
            statement.execureBatch();
            ResultSet=s.executeQuery("select *from emp101");
            while(result1.next()){
            System.out.println("name"+ result1.getString(1));
            System.out.println("pass"+result1.getString(2)); }}
            catch(Exception e)
            {
                  System.out.println(e);  
            }
      }

      public void insertTable()
      {
            try{
            String s2="insert emp101 values('bjp2018','modi2030')";
            statement.addBatch(s1);
            statement.addBatch(s2);
            statement.addBatch(s3);
            statement.execureBatch();
            ResultSet=s.executeQuery("select *from emp101");
            while(result1.next()){
            System.out.println("name"+ result1.getString(1));
            System.out.println("pass"+result1.getString(2)); }}
            catch(Exception e)
            {
                  System.out.println(e);  
            }
      }    */









        /*
                        Driver d=new oracle.jdbc.OracleDriver();
                        Properties dbProp=new Properties();
                        dbProp.put("user","system");
                        dbProp.put("password","Anand");
                        Connection c=d.connect("jdbc:oracle:thin:@localhost:1521:xe",dbProp); 



                        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                        Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Anand");


                        System.setProperty("jdbc.drivers","oracle.jdbc.driver.OracleDriver");
                        Properties dbProp=new Properties();
                        dbProp.put("user","system");
                        dbProp.put("password","Anand");
                        Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",dbProp);

 */