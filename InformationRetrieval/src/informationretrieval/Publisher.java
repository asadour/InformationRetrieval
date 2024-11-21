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
public class Publisher 
{
    private String author="-";
    private String title= "-";
    private String booktitle= "-";
    private String journal= "-";
    private String year= "-";
    
    public void setAuthor(String auth)
    {
        author = auth;
    }
    public void setTitle(String tit)
    {
        title = tit;
    }
    public void setBooktitle(String btit)
    {
        booktitle = btit;
    }
    public void setJournal(String jour)
    {
        journal = jour;
    }
    public void setYear(String y)
    {
        year = y;
    }
    
    public String[] getAll()
    {
        String pubs[]={author,title,booktitle,journal,year};         
        return pubs;
    }
    
}
