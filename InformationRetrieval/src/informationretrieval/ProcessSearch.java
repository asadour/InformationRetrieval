/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationretrieval;

import java.util.ArrayList;
import static jdk.nashorn.internal.objects.NativeString.trim;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
/**
 *
 * @author asadour
 */
public class ProcessSearch
{
    private int count=0;
    private int counts=0;
    private ArrayList<String> priorityString = new ArrayList<String>();
    private String query="";
    private ArrayList<Character> b;
    private String[] a;
    
       public ProcessSearch(String str)
       {
            GetPriorities(str);          
       }
    public String getQuery()
    {
        return query;
    }
    private void queryStrings()
    {
        String str;
        str="#combine( ";
        String[] b;
        boolean checkOne=false;
        for(int i=0;i<priorityString.size();i++)
        {
            boolean check=false;
            b=priorityString.get(i).split(" ");
            for(int z=0;z<b.length;z++)
            {
            if(b[z].equals("and"))
            {
                checkOne=true;
                str+="#band(";
                for(int j=0;j<b.length;j++)
                {
                    if(!b[j].equals("and"))
                    {
                        str+=b[j]+" ";
                    }
                    if(b[j].equals("or"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#or(";
                        check=true;
                    }
                    if(b[j].equals("*"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#od(";
                        check=true;
                    }
                    if(b[j].startsWith("*"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#od"+b[j].substring(1)+"(";
                        check=true;
                    }
                    
                }
                str=str.trim();
                str+=")";
            }
            if(b[z].equals("or"))
            {
                checkOne=true;
                str+="#or(";
                for(int j=0;j<b.length;j++)
                {
                    if(!b[j].equals("or"))
                    {
                        str+=b[j]+" ";
                    }
                    if(b[j].equals("and"))
                    {
                        str=str.substring(0, str.length()-4);
                        str=str.trim();
                        str+=")";
                        str+="#band(";
                        check=true;
                    }
                    if(b[j].equals("*"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#od(";
                        check=true;
                    }
                    if(b[j].startsWith("*"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#od"+b[j].substring(1)+"(";
                        check=true;
                    }                   
                }
                str=str.trim();
                str+=")";
            }
            if(b[z].equals("*"))
            {
                checkOne=true;
                str+="#od(";
                for(int j=0;j<b.length;j++)
                {
                    if(!b[j].equals("*"))
                    {
                        str+=b[j]+" ";
                    }
                    if(b[j].equals("and"))
                    {
                        str=str.substring(0, str.length()-4);
                        str=str.trim();
                        str+=")";
                        str+="#band(";
                        check=true;
                    }
                    if(b[j].equals("or"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#or(";
                        check=true;
                    }
                }
                str=str.trim();
                str+=")";
            }
            if(b[z].startsWith("*"))
            {
                checkOne=true;
                str+="#od"+b[z].substring(1)+"(";
                for(int j=0;j<b.length;j++)
                {
                    if(!b[j].startsWith("*"))
                    {
                        str+=b[j]+" ";
                    }
                    if(b[j].equals("and"))
                    {
                        str=str.substring(0, str.length()-4);
                        str=str.trim();
                        str+=")";
                        str+="#band(";
                        check=true;
                    }
                    if(b[j].equals("or"))
                    {
                        str=str.substring(0, str.length()-3);
                        str=str.trim();
                        str+=")";
                        str+="#or(";
                        check=true;
                    }
                }
                str=str.trim();
                str+=")";
            }
            if(check==true)
            {
                break;
            }
            }
        }
        if(checkOne==false)
        {
            str+=priorityString.get(0);
        }
        str+=" )";
        query=str;
    }
    private void ReplaceTldAndOr()
    {
        if(query.contains("tldrand"))
        {
            query=query.replaceAll("tldrand","and");
        }
        if(query.contains("tldror"))
        {
            query=query.replaceAll("tldror","or");
        }
    }
    private boolean isValidWord(String str)
    {
        String check[]=str.split(" ");
        for(int i=0;i<check.length;i++)
        {
            if(check[i].equals("*"))
            {
                return true;
            }
            if((check[i].contains("*"))&&(!check[i].startsWith("*")))
            {
                return false;
            }
            Pattern p1 = Pattern.compile(" *\\d");
            boolean hasodNum = p1.matcher(check[i]).find();
            Pattern p = Pattern.compile("[^a-zA-Z0-9()]");
            boolean hasSpecialChar = p.matcher(check[i]).find();
            if(hasSpecialChar==true)
            {
                if(hasodNum==true)
                {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
    private String ChangeAndOr(String str)
    {
        if(str.contains("and"))
        {
            str=str.replaceAll("and","tldrand");
        }
        if(str.contains("or"))
        {
            str=str.replaceAll("or","tldror");
        }
        return str;
    }
    private String replaceQuotes(String str)
    {
        if(!str.contains("\""))
        {
            return str;
        }
        String f=" "+str;
        String s=" ";
        if(f.startsWith(" \"")&&f.endsWith("\""))
        {
            s="*1 "+f.substring(2, f.length()-1);
            s=ChangeAndOr(s);
            return s;
        }
        if(f.contains(" \"")&&f.contains("\" "))
        {
            String[] b=f.split(" \"");
            if(!b[0].equals(""))
            {
                s+=b[0]+" *1 ";
            }
            else
            {
                s="*1 ";
            }
            for(int i=1;i<b.length;i++)
            {
                if(b[i].contains("\" "))
                {
                    String[] g=b[i].split("\" ");
                    s+=g[0];
                    s=ChangeAndOr(s);
                    s+=" "+g[1];
                }
            }
        }
        return s.trim();
    }
    private boolean isValidString(String str)
    {
        boolean check=true;
        str=str.toLowerCase();
        String[] checkWith=new String[]{"and","or","*"};
        for(int i=0;i<checkWith.length;i++)
        {
            if(str.startsWith(checkWith[i]+" "))
            {
                return false;
            }
            if(str.contains(" "+checkWith[i]+" "))
            {
                if(str.contains(" "+checkWith[i]+" ("))
                {
                    check=true;
                }
                if(str.contains(") "+checkWith[i]+" "))
                {
                    check=true;
                }
                if(str.contains(" "+checkWith[i]+" )"))
                {
                    return false;
                }
                if(str.contains("( "+checkWith[i]+" "))
                {
                    return false;
                }
                if(str.contains("( "+checkWith[i]+" )"))
                {
                    return false;
                }
                if(str.contains(checkWith[i]+" *")||str.contains("* "+checkWith[i]))
                {
                    return false;
                }
            }
            if(str.contains("( )")||str.contains("()"))
            {
                return false;
            }
            Pattern p5 = Pattern.compile(" *\\d");
            Pattern p4 = Pattern.compile("\\w\\)\\w");
            Pattern p3 = Pattern.compile("\\d\\)\\d");
            Pattern p2 = Pattern.compile("\\d\\(\\d");
            Pattern p = Pattern.compile("\\w\\(\\w");
            Matcher m = p.matcher(str);
            if(m.find()) {
               return false;
            }
            m=p2.matcher(str);
            if(m.find()) {
               return false;
            }
            m=p3.matcher(str);
            if(m.find()) {
               return false;
            }
            m=p4.matcher(str);
            if(m.find()) {
               return false;
            }
            m=p5.matcher(str);
            if(m.find()) {
               return true;
            }
        }
        return check;
    }
    private void GetPriorities(String index)
    {
        index=index.replaceAll(" +", " ");
        //System.out.println(index);
        //System.out.println(isValidString(index));
        a = index.split("[\\(\\)]");
        //System.out.println(Arrays.toString(a));
        for(int i=0;i<a.length;i++)
        {
            if(!a[i].equals(""))
            {
                counts++;
            }
        }
        index=replaceQuotes(index);
        //System.out.println(isBalanced(index)+" "+isValidString(index)+" "+isValidWord(index)+" "+ counts+" "+count+" "+index);
        if(isBalanced(index)&&isValidString(index)&&isValidWord(index))
        {
            if(count!=0)
            {
                putPriorities(index);
            }
            else
            {
                priorityString.add(index);
            }
            for(int i=0;i<priorityString.size();i++)
            {
                //System.out.println(i+"   "+priorityString.get(i));
            }
            queryStrings();
            ReplaceTldAndOr();
        }
        else
        {
            //System.out.println("String not valid!");
            query="false";
        }
    }
    private boolean isBalanced(String str)
    {
        int countLeftParenthesis=0;
        int countRightParenthesis=0;
        if(str.length()==0)
        {
            return false;
        }
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i)=='(')
            {
                countLeftParenthesis++;
            }
            if(str.charAt(i)==')')
            {
                countRightParenthesis++;
            }
        }
        if(countLeftParenthesis==countRightParenthesis)
        {
            count=countLeftParenthesis*2;
            return true;
        }
        else
        {
            return false;
        }
    }
    private int putPriorities(String str)
    {
        ArrayList<Integer> a = new ArrayList<Integer>();
        b = new ArrayList<Character>();
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i)=='('||str.charAt(i)==')')
            {
                a.add(i);
                b.add(str.charAt(i));
            }
        }
        for(int i=0;i<a.size()-1;i++)
        {
            if(b.get(i)=='(')
            {
                priority(str,i,a);
            }
        }
        if(counts!=priorityString.size())
        {
            if(a.get(0)!=0)
            {
                priorityString.add(trim(str.substring(0,a.get(0))));
            }
            if(a.get(a.size()-1)!=str.length()-1)
            {
                priorityString.add(trim(str.substring(a.get(a.size()-1)+1,str.length())));
            }
        }
        for(int i=0;i<priorityString.size();i++)
        {
            priorityString.remove("");
        }
        return 0;
    }
    private void priority(String str, int k,ArrayList<Integer> a)
    {
        boolean check=false;
        for(int i=k+1;i<a.size();i++)
        {
            if(b.get(i)=='(')
            {
                priority(str,i,a);
            }
            if(b.get(i)==')')
            {
                if(b.get(k+1)==' '&&b.get(i-1)==' ')
                {
                    if(i==0)
                    {
                    i=1;
                    }
                    priorityString.add(trim(str.substring(a.get(k)+1,a.get(k+1))));
                    priorityString.add(trim(str.substring(a.get(i-1)+1,a.get(i))));
                    b.set(k,' ');
                    b.set(k+1, ' ');
                }
                else
                {
                    priorityString.add(trim(str.substring(a.get(k)+1,a.get(i))));
                    b.set(k,' ');
                }
                b.set(i, ' ');
                check=true;
                break;
            }
        }
    }
}
