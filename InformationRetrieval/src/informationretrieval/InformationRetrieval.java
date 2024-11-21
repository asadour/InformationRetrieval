/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationretrieval;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asadour
 */
public class InformationRetrieval 
{
private JFrame basic = null;
private String header[] = new String[] {"Author","Title","Booktitle","Journal","Year"};
private JTable tbl = null;
private DefaultTableModel dtm=null;
private JButton searchbutton= new JButton("Search");
private JTextField searcharea= new JTextField(35);
private JPanel search_bar = new JPanel();


    public InformationRetrieval()
    {
        MakeIndex index  = new MakeIndex();
        index.Indexing();//Make Indexing
        System.out.println("Indexing procedure finished!!");
        init();
    }
 
    public void init()
    {
        basic = new JFrame("Boolean Model Information Retrieval(Lemour Project)"); 
        basic.setLayout(new BorderLayout());
        basic.setSize(new Dimension(900,900));
        basic.setLocationRelativeTo(null);
        
        tbl = new JTable();
        tbl.setBounds(30,40,200,300);          
        JScrollPane sp=new JScrollPane(tbl);    
        basic.add(sp,BorderLayout.CENTER);              
        dtm = new DefaultTableModel(0, 0);  
        dtm.setColumnIdentifiers(header);
        tbl.setModel(dtm);/*JTable */
        
        search_bar.add(searcharea);
        search_bar.add(searchbutton);        
        searchbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                EmptyTable();
                onSearchCommand();
            }
        });
        basic.add(search_bar,BorderLayout.NORTH);
        
        basic.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        basic.setVisible(true);
    }
    
    public void addRow(Object[] st)/*add dynamically to JTable*/
    {
        dtm.addRow(st);
    }
    
    public void onSearchCommand()
    {
        Double[] scores ;
        String[] documents;
        String[] n = null;    /* as xml files our results*/
        ProcessSearch pro = new ProcessSearch(searcharea.getText().toLowerCase());                        
                   if(!pro.getQuery().equals("false"))
                   {
                        MakeQueries q = new MakeQueries("index" ,pro.getQuery());                        
                        try 
                        {
                              q.Queries();
                              n= new String[q.getXmlResults().length];                                 
                              documents=q.getDocs();
                              scores=q.getScores();
                              n=q.getXmlResults();    
                              for(int i =0;i<n.length;i++)
                                  System.out.println(n[i]);
                        }
                        catch (Exception ex)
                        {
                            Logger.getLogger(InformationRetrieval.class.getName()).log(Level.SEVERE, null, ex);
                        }
                   }
                   addResultsInTable(n);
    }
    
    public void addResultsInTable(String[] xmls_results)
    {
        for(int i=0;i<xmls_results.length;i++)
        {
            Publisher pub = new Publisher();
            StringTokenizer res = new StringTokenizer(xmls_results[i], "\n");

		while (res.hasMoreElements())
                {
			String cut = (String) res.nextElement();
                        if(cut.contains("<field><author>"))
                        {
                            cut=cut.replace("<field><author>", " ");
                            cut=cut.replace("</author></field>"," ");
                            cut=cut.trim();
                            pub.setAuthor(cut);
                        }
                         if(cut.contains("<field><title>"))
                        {
                            cut=cut.replace("<field><title>", " ");
                            cut=cut.replace("</title></field>"," ");
                            cut=cut=cut.trim();
                            pub.setTitle(cut);
                        }
                          if(cut.contains("<field><booktitle>"))
                        {
                            cut=cut.replace("<field><booktitle>", " ");
                            cut=cut.replace("</booktitle></field>"," ");
                            cut=cut.trim();
                            pub.setBooktitle(cut);
                        }
                           if(cut.contains("<field><journal>"))
                        {
                            cut=cut.replace("<field><journal>", " ");
                            cut=cut.replace("</journal></field>"," ");
                            cut=cut.trim();
                            pub.setJournal(cut);
                        }
                            if(cut.contains("<field><year>"))
                        {
                            cut=cut.replace("<field><year>", " ");
                            cut=cut.replace("</year></field>"," ");
                            cut=cut.trim();
                            pub.setYear(cut);
                        }
		}
                addRow(pub.getAll());
        }
    }
    
    public void EmptyTable()
    {
        int rows = tbl.getRowCount()-1;
        for(int i=rows;i>=0;i--)
        {
            dtm.removeRow(i);
        }
         dtm.fireTableDataChanged();
    }
    
}
