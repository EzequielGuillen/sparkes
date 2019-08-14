import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SiteServiceMapImpl implements ISiteService {

    private Map<String, Site> siteMap;

    public SiteServiceMapImpl() {
        siteMap = new HashMap<String, Site>();
    }

    @Override
    public Collection<Site> getSites() {
        return ( siteMap.values());
    }

    @Override
    public Site getSite(String id) {
        return siteMap.get(id);
    }

    @Override
    public int addSite(Site site) throws ApiException {
        siteMap.put(site.getId(),site);
        return siteMap.size();
    }

    @Override
    public Site updateSite(String id, Site site) throws ApiException {
        if ( siteMap.containsKey(id) ) {
            siteMap.replace(id,site);
            return siteMap.get(id);
        }
        return null;
    }

    @Override
    public Site deleteSite(String id) throws ApiException {
        if ( siteMap.containsKey(id) ) {
            Site site = siteMap.get(id);
            siteMap.remove(id);
            return site;
        }
        return null;
    }

    @Override
    public boolean siteExists(String id) {
        return siteMap.containsKey(id);
    }
}
