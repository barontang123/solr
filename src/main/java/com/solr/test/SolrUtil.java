package com.solr.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class SolrUtil {

    //solr服务器所在的地址，core0为自己创建的文档库目录
    private final static String SOLR_URL = "http://10.1.2.35:8983/solr/core1";

    /**
     * 获取客户端的连接
     *
     * @return
     */
    public HttpSolrClient createSolrServer() {
        HttpSolrClient solr = null;
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();
        return solr;
    }

    /**
     * 往索引库添加文档
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void addDoc() throws SolrServerException, IOException {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "3");
        document.addField("name", "周星驰");
        document.addField("description", "喜剧之王");
        HttpSolrClient solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000)
                .withSocketTimeout(60000).build();
        solr.add(document);
        solr.commit();
        solr.close();
        System.out.println("添加成功");
    }

    /**
     * 根据ID从索引库删除文档
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteDocumentById() throws SolrServerException, IOException {
        HttpSolrClient server = new HttpSolrClient.Builder(SOLR_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000).build();

        server.deleteById("6");
        server.commit();
        server.close();
    }

    /**
     * 根据设定的查询条件进行文档字段的查询
     * @throws Exception
     */
    public void querySolr() throws Exception {

        HttpSolrClient server = new HttpSolrClient.Builder(SOLR_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000).build();
        SolrQuery query = new SolrQuery();

        //下面设置solr查询参数

        //query.set("q", "*:*");// 参数q  查询所有
        query.set("q", "钢铁侠");//相关查询，比如某条数据某个字段含有周、星、驰三个字  将会查询出来 ，这个作用适用于联想查询

        //参数fq, 给query增加过滤查询条件
        query.addFacetQuery("id:[0 TO 9]");
        query.addFilterQuery("description:一个逗比的码农");

        //参数df,给query设置默认搜索域，从哪个字段上查找
        query.set("df", "name");

        //参数sort,设置返回结果的排序规则
        query.setSort("id",SolrQuery.ORDER.desc);

        //设置分页参数
        query.setStart(0);
        query.setRows(10);

        //设置高亮显示以及结果的样式
        query.setHighlight(true);
        query.addHighlightField("name");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");

        //执行查询
        QueryResponse response = server.query(query);

        //获取返回结果
        SolrDocumentList resultList = response.getResults();

        for(SolrDocument document: resultList){
            System.out.println("id:"+document.get("id")+"   document:"+document.get("name")+"    description:"+document.get("description"));
        }

        //获取实体对象形式
        List<Person> persons = response.getBeans(Person.class);

        System.out.println(persons.get(0).getName());

    }

    public void deleteIndex() throws Exception {
        HttpSolrClient server = new HttpSolrClient.Builder(SOLR_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000).build();

        //根据id删除
             //UpdateResponse response = server.deleteById("3");
             //根据多个id删除
              // server.deleteById("3");
             //自动查询条件删除
            UpdateResponse response1 = server.deleteByQuery("name:周");
            UpdateResponse response2= server.commit();
         }


    //查询索引
  public static void selectIndex() throws Exception {
        HttpSolrClient server = new HttpSolrClient.Builder(SOLR_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000).build();

             // 查询对象
             SolrQuery query = new SolrQuery();
             //设置查询条件,名称“q”是固定的且必须的
             //搜索keywords域，keywords是复制域包括name和description
             query.set("q", "name:周星驰习近平");
            // 设置商品分类、关键字查询
            // query.setQuery("name:数据 AND price:11.1");
             // 设置价格范围
             //query.set("fq", "id:[1 TO 20]");
             query.addFacetQuery("id:[0 TO 9]");
             // 查询结果按照价格降序排序
             //query.set("sort", "id desc");
             query.addSort("id", SolrQuery.ORDER.desc);

              //设置高亮显示以及结果的样式
              query.setHighlight(true); // 开启高亮组件
              query.addHighlightField("title");// 高亮字段
              query.setHighlightSimplePre("<font color='red'>");// 标记
              query.setHighlightSimplePost("</font>");
              query.setHighlightSnippets(1);//结果分片数，默认为1
              query.setHighlightFragsize(1000);

             // 请求查询
             QueryResponse response = server.query(query);
             // 查询结果
             SolrDocumentList docs = response.getResults();
             // 查询文档总数
             System.out.println("查询文档总数" + docs.getNumFound());
             for (SolrDocument doc : docs) {
                     String id = (String) doc.get("id");
                     String name = ((ArrayList<String>)doc.get("name")).get(0);
                     String description = ((ArrayList<String>)doc.get("description")).get(0);
                     System.out.println(id);

                 }
         }


    // 分页和高亮
  public static void selectHeightLight() throws Exception {

             HttpSolrClient server = new HttpSolrClient.Builder(SOLR_URL)
              .withConnectionTimeout(10000)
              .withSocketTimeout(60000).build();

             // 查询对象
             SolrQuery query = new SolrQuery();
             // text是name、title等众多字段的复制域
             query.setQuery("name:习喜剧 and description:习喜剧");   //多条件搜索


             query.setSort("id",SolrQuery.ORDER.asc);
             //query.addFilterQuery("id:[3 TO *]");

             // 每页显示记录数
             int pageSize = 10;
             // 当前页码
             int curPage = 1;
             // 开始记录下标
             int begin = pageSize * (curPage - 1);
             // 起始下标
             query.setStart(begin);
             // 结束下标
             query.setRows(pageSize);
             // 设置高亮参数
             query.setHighlight(true); // 开启高亮组件
             query.addHighlightField("name");// 高亮字段
             query.addHighlightField("description");// 高亮字段
             query.setHighlightSimplePre("<span color='red'>");//前缀标记
             query.setHighlightSimplePost("</span>");// 后缀标记
             // 请求查询
             QueryResponse response = server.query(query);
             // 查询结果
             SolrDocumentList docs = response.getResults();
             // 查询文档总数
             System.out.println("查询文档总数" + docs.getNumFound());
             for (SolrDocument doc : docs) {
                     // 商品主键
                     String id = (String) doc.getFieldValue("id");
                     // 商品名称
                     String name = ((ArrayList<String>)doc.getFieldValue("name")).get(0);
                     System.out.println("id"+id+"姓名：" + name);
                     // 高亮信息
                     if(response.getHighlighting() != null) {
                             if(response.getHighlighting().get(id) != null) {
                                     // 取出高亮片段
                                     Map<String, List<String>> map = response.getHighlighting().get(id);
                                     if(map.get("name") != null) {
                                             for(String s : map.get("name")) {
                                                     System.out.println(s);
                                                 }
                                         }
                                 if(map.get("description") != null) {
                                     for(String s : map.get("description")) {
                                         System.out.println(s);
                                     }
                                 }
                                }
                         }
                 }
         }

    public static void main(String[] args) throws Exception {
        SolrUtil solr = new SolrUtil();
        //solr.addDoc();
        //solr.querySolr();

        //solr.deleteIndex();

        //solr.selectIndex();
        solr.selectHeightLight();

        HashMap<String,String> maps = new HashMap<>();
        maps.put("name","张三");
        maps.put("age","18");

      /*  for (Map.Entry<String,String> entry:maps.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }*/
    /*    Iterator<Map.Entry<String,String>> iterators =  maps.entrySet().iterator();

        while (iterators.hasNext())
        {
          Map.Entry<String,String> map =  iterators.next();
          System.out.println(map.getKey() + map.getValue());
        }*/

  /*    List<String> params = new ArrayList<>();
      params.add("one");
      params.add("two");
      params.add("three");
      params.add("four");

        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<Callable<String>> list= new ArrayList<Callable<String>>();

        for (String param:params) {
            list.add(new Job(param));
        }
        try{

            List<Future<String>> futures = executor.invokeAll(list);
            for (Future<String> future:futures) {
                String result = future.get();
                System.out.println(result);
            }

        }
        catch (Exception ex)
        {

        }

        Thread thread = new Thread(new myThreed(),"abc");
        thread.start();*/

    }



}
