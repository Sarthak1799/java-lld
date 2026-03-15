package services;
import java.util.*;
import models.*;

public class TraderService {
    private static TraderService instance;
    private Map<String, Trader> traders;

    private TraderService() {
        this.traders = new HashMap<>();
    }

    public static TraderService getInstance() {
        if (instance == null) {
            synchronized (TraderService.class) {
                if (instance == null) {
                    instance = new TraderService();
                }
            }
        }
        return instance;
    }

    public synchronized void registerTrader(Trader trader) {
        traders.put(trader.getId(), trader);
    }

    public Trader getTraderById(String traderId) {
        return traders.get(traderId);
    }

    public List<Trader> getAllTraders() {
        return new ArrayList<>(traders.values());
    }
}