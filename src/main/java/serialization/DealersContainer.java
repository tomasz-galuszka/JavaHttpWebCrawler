package serialization;

import data.db.Car;
import data.db.Dealer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DealersContainer implements Serializable {

    private static final long serialVersionUID = -4235058141916080579L;
    private Map<Dealer, Set<Car>> offersmap;
    private int page;
    private int totalCount;

    public DealersContainer() {
        offersmap = new HashMap<Dealer, Set<Car>>();
    }

    public void put(Car c) {
        if (c.getDealer() == null) {
            return;
        }

        if (!offersmap.containsKey(c.getDealer())) {
            Set<Car> set = new HashSet<Car>();
            set.add(c);
            offersmap.put(c.getDealer(), set);
        } else {
            offersmap.get(c.getDealer()).add(c);
        }
    }

    public Map<Dealer, Set<Car>> getOffersmap() {
        return offersmap;
    }

    public void setOffersmap(Map<Dealer, Set<Car>> offersmap) {
        this.offersmap = offersmap;
    }

    public void printSize() {
        Set<Dealer> keySet = offersmap.keySet();
        int size = 0;
        for (Dealer dealer : keySet) {
            size += offersmap.get(dealer).size();
        }
        System.out.println("Page: " + page);
        System.out.println("Total pages: " + totalCount);
        float percent = page * 100 / (float) totalCount;
        System.out.println("Download status: " + String.format("%.2f", percent) + "%");
        System.out.println("Downloaded dealers: " + size);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCount() {
        if (totalCount < 2000) {
            totalCount = page + 1;
        }
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}