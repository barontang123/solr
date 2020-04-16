package com.solr.test;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;

import org.apache.solr.common.SolrInputDocument;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }


    //solr服务器所在的地址，core0为自己创建的文档库目录
    private final static String SOLR_URL = "http://192.168.111.130:8080/solr/core0";

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

}
