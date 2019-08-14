import java.util.Collection;
import java.util.Optional;

public interface ISiteService {

    public Collection<Site> getSites();

    public Site getSite(String id);

    public int addSite(Site site) throws ApiException;

    public Site updateSite(String id, Site site) throws ApiException;

    public Site deleteSite(String id) throws ApiException;

    public boolean siteExists(String id);

}
