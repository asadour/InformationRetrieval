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
import java.io.File;
import lemurproject.indri.IndexEnvironment;
import lemurproject.indri.Specification;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asadour
 */


public class MakeIndex 
{
    public void Indexing()
    {
         try 
         {
            String [] stopwords = {"a", "an", "and", "are", "as", "at", "be", "by","for",
        "from", "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", 
        "to", "was", "were", "will", "with"};/*cut stopwords*/
            IndexEnvironment env = new IndexEnvironment();
            String[] fields = new String[]{"author","title","booktitle","journal","year"};
            /*which fields I need to watch-see my xml files in xml-s directory*/
            env.setIndexedFields(fields);
            env.setNumericField(fields[4], true);
            env.setMemory(500 * 1024 * 1024); 
            env.setStoreDocs(true);       
            Specification spec = env.getFileClassSpec("xml");/*use for indexing*/
            spec.include = fields;
            spec.index = fields;
            env.addFileClass(spec);
            env.setStemmer("krovetz");      /*I chose Krovetz stemming algorithm ,you can choose porter stemming*/     
            env.setStopwords(stopwords);        
            env.create("index");
            
            
            File filesDir = new File("xml-s"); 
            File[] files = filesDir.listFiles(); 
            int noOffiles = files.length; 
            for (int i = 0; i < noOffiles; i++) 
            { 
                env.addFile(files[i].getAbsolutePath(), "xml"); 
            }  
            env.close();
        } 
         catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
