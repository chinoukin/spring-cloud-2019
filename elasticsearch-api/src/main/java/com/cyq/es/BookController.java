package com.cyq.es;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.OffsetDateTime;

@RequestMapping("/book")
@RestController
public class BookController {

    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private RestHighLevelClient client;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> indexDoc(@RequestBody Book book) {
        XContentBuilder builder = null;
        IndexRequest request = new IndexRequest("book");

        try {
            builder = XContentFactory.jsonBuilder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            builder.startObject()
                    .field("id", book.getId())
                    .field("name", book.getName())
                    .field("author", book.getAuthor())
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.id(book.getId()).opType("create").source(builder);
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("增加成功：" + OffsetDateTime.now() + ",书名:《" + book.getName() + "》");
        return new ResponseEntity<>("save executed!", HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object searchDoc() throws Exception {
        logger.debug("根据作者曹雪芹查找书");
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("author", "chinoukin");
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("author", "曹雪芹");
        searchSourceBuilder.query(matchQueryBuilder);

        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        SearchHit[] hits = response.getHits().getHits();
        String[] results = new String[hits.length];
        int index = 0;
        for (SearchHit hit : hits) {
            results[index] = hit.getSourceAsString();
            index++;
        }
        return results;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Object getDoc(@PathVariable("id") String id) throws Exception {
        logger.debug("找书，id=" + id);
        GetRequest request = new GetRequest("book");
        request.id(id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        return response.getSourceAsMap();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> deleteDoc(@PathVariable("id") String id) throws Exception {
        logger.debug("删除书，id=" + id);
        DeleteRequest request = new DeleteRequest("book");
        request.id(id);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        return new ResponseEntity<>("id:" + id + " deleted!", HttpStatus.OK);
    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public ResponseEntity<String> exception() throws Exception {
        logger.debug("制造异常");
        int exp = 5 / 0;
        System.out.println(exp);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
