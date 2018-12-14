package com.itheima.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.*;

import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.File;
import java.io.IOException;

public class LuceneFirst {
    /*  @Test
      public void createIndex() throws Exception {
          //1、创建一个Director对象，指定索引库保存的位置。
          //把索引库保存在内存中
          //Directory directory = new RAMDirectory();
          //把索引库保存在磁盘
          Directory directory = FSDirectory.open(new File("E:\\develop\\index").toPath());
          //2、基于Directory对象创建一个IndexWriter对象

          IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
          //3、读取磁盘上的文件，对应每个文件创建一个文档对象。
          File dir = new File("F:\\cn.itheima.source\\就业班\\Lucene\\02.参考资料\\searchsource");
          File[] files = dir.listFiles();
          for (File f :
                  files) {
              //取文件名
              String fileName = f.getName();
              //文件的路径
              String filePath = f.getPath();
              //文件的内容
              String fileContent = FileUtils.readFileToString(f, "utf-8");
              //文件的大小
              long fileSize = FileUtils.sizeOf(f);
              //创建Field
              //参数1：域的名称，参数2：域的内容，参数3：是否存储
              Field fieldName = new TextField("name", fileName, Field.Store.YES);
              Field fieldPath = new TextField("path", filePath, Field.Store.YES);

              Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
              Field fieldSize = new TextField("size", fileSize + "", Field.Store.YES);

              //创建文档对象
              Document document = new Document();
              //向文档对象中添加域
              document.add(fieldName);
              document.add(fieldPath);
              document.add(fieldContent);
              document.add(fieldSize);

              //5、把文档对象写入索引库
              indexWriter.addDocument(document);
          }
          //6、关闭indexwriter对象
          indexWriter.close();
      }*/
    @Test
    public void createIndex() throws Exception {
        //1.创建一个Director对象,指定索引库保存位置
        //把索引保存在内存中
        //Directory directory=new RAMDirectory();
        //把索引库保存在磁盘
        Directory directory = FSDirectory.open(new File("E:\\develop\\index").toPath());
        //2.基于Directory对象创建一个indexWriter对象
        IndexWriterConfig indexWriterConfig=new IndexWriterConfig(new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir = new File("F:\\cn.itheima.source\\就业班\\Lucene\\02.参考资料\\searchsource");
        File[] files = dir.listFiles();
        for (File f : files) {
            //取文件名
            String fileName = f.getName();
            //取文件的路径
            String filePath = f.getPath();
            //取文件的内容
            String fileContent = FileUtils.readFileToString(f, "utf-8");
            //取文件的大小
            long fileSize = FileUtils.sizeOf(f);
            //创建Filed
            //参数1：域的名称，参数2：域的内容，参数3：是否存储
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            //Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldPath=new StoredField("path",filePath);
            Field fieldContent = new TextField("context", fileContent, Field.Store.YES);
            //Field fieldSize = new TextField("size", fileSize + "", Field.Store.YES);
            Field fieldSizeValue = new LongPoint("size", fileSize);
            Field fieldSizeStory=new StoredField("size",fileSize);
            //创建文档对象
            Document document = new Document();
            //向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSizeValue);
            document.add(fieldSizeStory);
            //5.将文档对象写入到索引库
            indexWriter.addDocument(document);
        }
        //6.关闭indexwriter对象
        indexWriter.close();
    }

    @Test
    public void searchIndex() throws IOException {
        //创建一个Directory对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("E:\\develop\\index").toPath());
        //基于Directory对象创建一个indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建一个indexSearch对象，构造方法中传入indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建一个Query对象，TermQuery
        Query query = new TermQuery(new Term("context", "spring"));
        //执行查询，得到一个TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 10);
        //取查询结果的总记录数
        System.out.println("总记录数：" + topDocs.totalHits);
        //去文档列表
        ScoreDoc[] docs = topDocs.scoreDocs;
        //打印文档中的内容
        for (ScoreDoc doc : docs) {
            int docId = doc.doc;
            //获取document对象
            Document document = indexSearcher.doc(docId);
            System.out.println(document.getField("name"));
            System.out.println(document.getField("path"));
           // System.out.println(document.getField("context"));
            System.out.println(document.getField("size"));
            System.out.println("==========寂寞的分割线===================");

        }
        //关闭indexReader
        indexReader.close();
    }
}
