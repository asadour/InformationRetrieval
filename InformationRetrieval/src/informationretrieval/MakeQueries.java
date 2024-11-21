/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationretrieval;

/**
 *
 * @author asadour
 */

import java.util.Arrays;
import java.util.Collections;
import lemurproject.indri.ParsedDocument;
import lemurproject.indri.QueryAnnotation;
import lemurproject.indri.QueryEnvironment;
import lemurproject.indri.ScoredExtentResult;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asadour
 */

/*MakeQueries class manipulates the results after searching*/
public class MakeQueries 
{
    private String path;
    private String query;
    private String[] xmlresults ;
    private Double[] scores;
    private String[] docs ;
    public MakeQueries(String path , String query)
    {
        this.path=path;
        this.query=query;
    }
    public String[] getDocs()
    {
        return docs;
    }
    public Double[] getScores()
    {
        return scores;
    }
    public String[] getXmlResults()
     {
        return xmlresults;
     }
    public  void Queries() throws Exception
    {
        QueryEnvironment queryEnvironment = new QueryEnvironment();
        queryEnvironment.addIndex(path);
        String myQuery =query;        
        QueryAnnotation results=queryEnvironment.runAnnotatedQuery(myQuery, 1000);
        xmlresults = new String[results.getResults().length];
        ScoredExtentResult[] results1 = queryEnvironment.runQuery(myQuery,results.getResults().length);
        ParsedDocument[] text = queryEnvironment.documents(results1);
        ParsedDocument.TermExtent[] termtext = new ParsedDocument.TermExtent[1000];
        String[] terms = new String[1000];
        scores= new Double[results1.length];
        docs = new String[results1.length];        
        for (int i = 0; i < text.length; i++) 
        {
            termtext= text[i].positions;
            xmlresults[i] =text[i].content;           
        }     
        for(int i=0;i<results1.length;i++)
        {           
           scores[i]=results1[i].score;
           docs[i]="DOC"+results1[i].document;
        }
            int n=scores.length;
            double temp=0;
            String temp1="";
            String temp_doc="";
       for(int i=0; i < n; i++)/*simple bubble sort for all results*/
       {  
                 for(int j=1; j < (n-i); j++)
                 {  
                          if(scores[j-1] < scores[j])
                          {  
                                 //swap elements  
                                 temp = scores[j-1];  
                                 scores[j-1] = scores[j];  
                                 scores[j] = temp;  
                                 
                                 temp1 = xmlresults[j-1];  
                                 xmlresults[j-1] = xmlresults[j];  
                                 xmlresults[j] = temp1;  
                                 
                                 temp_doc = docs[j-1];  
                                 docs[j-1] = docs[j];  
                                 docs[j] = temp_doc;  
                          }                            
                 }  
         }  /*finish of bubble sort*/    
    }    
}
