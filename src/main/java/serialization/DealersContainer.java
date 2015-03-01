package serialization;

import data.db.Car;
import data.db.Dealer;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DealersContainer implements Serializable {

    private static final long serialVersionUID = -4235058141916080579L;
    private ConcurrentHashMap<Dealer, Set<Car>> offersMap;
    private Map<String, Integer> crawlerCurrentPage = new HashMap<String, Integer>();
    private int totalCount;

    public Map<String, Integer> getCrawlerCurrentPage() {
        return crawlerCurrentPage;
    }
    private boolean isPremiumDownloaded = false;


    public void clearOffers() {
        offersMap.clear();
    }

    public void setCurrentPage(String crawlerName, Integer page) {
        crawlerCurrentPage.put(crawlerName, page);
    }

    public DealersContainer() {
        offersMap = new ConcurrentHashMap<Dealer, Set<Car>>();
    }

    public synchronized void put(Car c) {
        if (c.getDealer() == null) {
            return;
        }

        if (!offersMap.containsKey(c.getDealer())) {
            Set<Car> set = Collections.synchronizedSet(new HashSet<Car>());
            set.add(c);
            offersMap.put(c.getDealer(), set);

        } else {
            offersMap.get(c.getDealer()).add(c);
        }
    }

    public ConcurrentHashMap<Dealer, Set<Car>> getOffersMap() {
        return offersMap;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isPremiumDownloaded() {
        return isPremiumDownloaded;
    }

    public void setPremiumDownloaded(boolean isPremiumDownloaded) {
        this.isPremiumDownloaded = isPremiumDownloaded;
    }

    public int getTotalCount() {
        return totalCount;
    }
}