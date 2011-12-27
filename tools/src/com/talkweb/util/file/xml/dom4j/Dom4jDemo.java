package com.talkweb.util.file.xml.dom4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

//dom4j生成XML文档及解析2008-07-23 22:19

public class Dom4jDemo {
/**建立一个XML文档,文档名由输入属性决定
* @param param filename 需建立的文件名
* @return返回操作结果, 0表失败, 1表成功
*/
public int createXMLFile(String filename){
       /** 返回操作结果, 0表失败, 1表成功 */
       int returnValue = 0;
       /** 建立document对象 */
       Document document = DocumentHelper.createDocument();
       /** 建立XML文档的根books */
       Element booksElement = document.addElement("books");
       /** 加入一行注释 */
       booksElement.addComment("This is a test for dom4j, holen, 2004.9.11");
       /** 加入第一个book节点 */
       Element bookElement = booksElement.addElement("book");
       /** 加入show属性内容 */
       bookElement.addAttribute("show","yes");
       /** 加入title节点 */
       Element titleElement = bookElement.addElement("title");
       /** 为title设置内容 */
       titleElement.setText("Dom4j Tutorials");
       
       /** 类似的完成后两个book */
       bookElement = booksElement.addElement("book");
       bookElement.addAttribute("show","yes");
       titleElement = bookElement.addElement("title");
       titleElement.setText("Lucene Studing");
       bookElement = booksElement.addElement("book");
       bookElement.addAttribute("show","no");
       titleElement = bookElement.addElement("title");
       titleElement.setText("Lucene in Action");
       
       /** 加入owner节点 */
       Element ownerElement = booksElement.addElement("owner");
       ownerElement.setText("O'Reilly");
       
       try{
           /** 将document中的内容写入文件中 */
           XMLWriter writer = new XMLWriter(new FileWriter(new java.io. File(filename)));
           writer.write(document);
           writer.close();
           /** 执行成功,需返回1 */
           returnValue = 1;
       }catch(Exception ex){
           ex.printStackTrace();
       }
              
       return returnValue;
    }
/**
     * 修改XML文件中内容,并另存为一个新文件
     * 重点掌握dom4j中如何添加节点,修改节点,删除节点
     * @param filename 修改对象文件
     * @param newfilename 修改后另存为该文件
     * @return 返回操作结果, 0表失败, 1表成功
     */
    public int modiXMLFile(String filename,String newfilename){
       int returnValue = 0;
       try{
           SAXReader saxReader = new SAXReader(); 
           Document document = saxReader.read(new java.io.File(filename));
           /** 修改内容之一: 如果book节点中show属性的内容为yes,则修改成no */
           /** 先用xpath查找对象 */
           List list = document.selectNodes("/books/book/@show" ); 
           Iterator iter = list.iterator();
           while(iter.hasNext()){
              Attribute attribute = (Attribute)iter.next();
              if(attribute.getValue().equals("yes")){
                  attribute.setValue("no");
              }   
           }
           
           /**
            * 修改内容之二: 把owner项内容改为"测试修改"
            * 并在owner节点中加入date节点,date节点的内容为2004-09-11,还为date节点添加一个属性type
            */
           list = document.selectNodes("/books/owner" );
           iter = list.iterator();
           if(iter.hasNext()){
              Element ownerElement = (Element)iter.next();
              ownerElement.setText("测试修改");
              Element dateElement = ownerElement.addElement("date");
              dateElement.setText("2008-09-11");
              dateElement.addAttribute("type","日期");
           }
           
           /** 修改内容之三: 若title内容为Dom4j Tutorials,则删除该节点 */
           list = document.selectNodes("/books/book");
           iter = list.iterator();
           while(iter.hasNext()){
              Element bookElement = (Element)iter.next();
              Iterator iterator = bookElement.elementIterator("title");
              while(iterator.hasNext()){
                  Element titleElement=(Element)iterator.next();
                  if(titleElement.getText().equals("Dom4j Tutorials")){
                     bookElement.remove(titleElement);
                  }
              }
           }          
           
           try{
               /** 格式化输出,类型IE浏览一样 */
               OutputFormat format = OutputFormat.createPrettyPrint();
               /** 指定XML编码 */
//               format.setEncoding("GBK");
              /** 将document中的内容写入文件中 */
//              XMLWriter writer = new XMLWriter(new FileWriter(new File(newfilename)),format);
               //保证编码为UTF-8，支持中文写入
              XMLWriter writer = new XMLWriter(new FileOutputStream(new File(newfilename)),format);
              writer.write(document);
              writer.close();
              /** 执行成功,需返回1 */
              returnValue = 1;
           }catch(Exception ex){
              ex.printStackTrace();
           }
           
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return returnValue;
    }
    /**
     * 格式化XML文档,并解决中文问题
     * @param filename
     * @return
     */
    public int formatXMLFile(String filename){
       int returnValue = 0;
       try{
           SAXReader saxReader = new SAXReader(); 
           Document document = saxReader.read(new File(filename));
           XMLWriter writer = null;
           /** 格式化输出,类型IE浏览一样 */
           OutputFormat format = OutputFormat.createPrettyPrint();
           /** 指定XML编码 */
           format.setEncoding("GBK");
           writer= new XMLWriter(new FileWriter(new File(filename)),format);
           writer.write(document);
           writer.close();      
           /** 执行成功,需返回1 */
           returnValue = 1;     
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return returnValue;
    }
}
 
