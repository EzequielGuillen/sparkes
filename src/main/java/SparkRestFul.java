import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class SparkRestFul {

    public static void main(String[] args) {

        final ISiteService service = new SiteServiceImplES();

        get("/sites", (req,res) -> {

            res.type("application/json");
            List<Site> sites = new ArrayList<>( service.getSites());
            return new Gson().toJson(new StandarResponde( StatusResponse.SUCCESS, ( new Gson().toJsonTree( sites) ) ) );

        });

        get("/sites/:id", (req,res) -> {

            res.type("application/json");
            Site site = service.getSite(req.params(":id"));
            return new Gson().toJson(new StandarResponde( StatusResponse.SUCCESS, (new Gson().toJsonTree( site)) ) );

        });

        post("/sites", (req,res) -> {

            res.type("application/json");
            Site site = new Gson().fromJson( req.body(), Site.class);
            int id = service.addSite(site);
            return new Gson().toJson( new StandarResponde( StatusResponse.SUCCESS, new Gson().toJsonTree(site)) );

        });

        put("/sites/:id", (req,res) -> {

            res.type("application/json");
            Site site = new Gson().fromJson( req.body(), Site.class);
            Site result = service.updateSite(req.params(":id"), site);
            return new Gson().toJson( new StandarResponde( StatusResponse.SUCCESS, new Gson().toJsonTree(result)) );

        } );

        delete("/sites/:id", (req,res) -> {

            res.type("application/json");
            Site result = service.deleteSite(req.params(":id"));
            return new Gson().toJson( new StandarResponde( StatusResponse.SUCCESS, new Gson().toJsonTree(result)) );

        });

        options("/sites/:id", (req,res) -> {

            res.type("application/json");
            boolean result = service.siteExists(req.params(":id"));
            return new Gson().toJson( new StandarResponde( StatusResponse.SUCCESS, new Gson().toJsonTree(result)) );

        } );

    }

}
