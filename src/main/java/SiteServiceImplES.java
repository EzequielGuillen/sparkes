import com.google.gson.Gson;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


public class SiteServiceImplES implements ISiteService {

    private static ObjectMapper objectMapper;

    @Override
    public Collection<Site> getSites() {

        RestHighLevelClient client = CreateConnection.makeConnection();

        SearchRequest searchRequest = new SearchRequest("sites");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        try {

            SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            SearchHit[] searchHits = hits.getHits();
            List<Site> sites = new ArrayList<>() ;
            for (SearchHit hit : searchHits) {
                sites.add(new Gson().fromJson( hit.getSourceAsString(), Site.class));
            }

            return sites;


        } catch(ElasticsearchException e) {
            System.out.println(e.getMessage());
        } catch (java.io.IOException ex){
            System.out.println(ex.getMessage());
        }

        try {
            CreateConnection.closeConnection(client);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    @Override
    public Site getSite(String id) {
        RestHighLevelClient client = CreateConnection.makeConnection();

        GetRequest getRequest = new GetRequest("sites", id);
        try {
            GetResponse response = client.get(getRequest,RequestOptions.DEFAULT);
            Site result = new Gson().fromJson( response.getSourceAsString(), Site.class);
            return result;
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }

        try {
            CreateConnection.closeConnection(client);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    public int addSite(Site site) throws ApiException {

        RestHighLevelClient client = CreateConnection.makeConnection();

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("id", site.getId());
        dataMap.put("name", site.getName());

        IndexRequest indexRequest = new IndexRequest("sites").id(site.getId()).source(dataMap);

        try {
            IndexResponse response = client.index(indexRequest,RequestOptions.DEFAULT);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        try {
            CreateConnection.closeConnection(client);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        return 1;
    }

    @Override
    public Site updateSite(String id, Site site) throws ApiException {


        RestHighLevelClient client = CreateConnection.makeConnection();

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("id", site.getId());
        dataMap.put("name", site.getName());

        UpdateRequest updateRequest = new UpdateRequest("sites",id).doc(dataMap);

        try {

            UpdateResponse response = client.update(updateRequest,RequestOptions.DEFAULT);

            if(response.status().getStatus()==200){
                return getSite(id);
            }

        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }

        try {
            CreateConnection.closeConnection(client);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    @Override
    public Site deleteSite(String id) throws ApiException {

        RestHighLevelClient client = CreateConnection.makeConnection();

        DeleteRequest deleteRequest = new DeleteRequest("sites",id);

        try {

            Site site = getSite(id);
            DeleteResponse response = client.delete(deleteRequest,RequestOptions.DEFAULT);
            return site;
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }

        try {
            CreateConnection.closeConnection(client);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }

        return null;
    }

    @Override
    public boolean siteExists(String id) {

        RestHighLevelClient client = CreateConnection.makeConnection();

        GetRequest getRequest = new GetRequest(
                "sites",
                id);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        try {
            boolean response = client.exists(getRequest,RequestOptions.DEFAULT);
            return response;
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }

        try {
            CreateConnection.closeConnection(client);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }

        return false;
    }

}
