
import java.util.*;

public class RestaurantService implements OrderObserver {

    private static RestaurantService instance;
    private Map<String, Restaurant> restaurants;

    private RestaurantService() {
        this.restaurants = new HashMap<>();
    }

    public static synchronized RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    public synchronized void addRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
    }

    public synchronized Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    public synchronized List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants.values());
    }

    public synchronized List<Restaurant> getOpenRestaurants() {
        List<Restaurant> openRestaurants = new ArrayList<>();
        for (Restaurant restaurant : restaurants.values()) {
            if (restaurant.isOpen()) {
                openRestaurants.add(restaurant);
            }
        }
        return openRestaurants;
    }

    public synchronized void addMenuItemToRestaurant(String restaurantId, MenuItem item) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant != null) {
            restaurant.getMenu().addItem(item);
        }
    }

    public synchronized void removeMenuItemFromRestaurant(String restaurantId, String itemId) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant != null) {
            restaurant.getMenu().removeItem(itemId);
        }
    }

    public synchronized List<MenuItem> searchMenuItems(String restaurantId, String keyword) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            return new ArrayList<>();
        }

        List<MenuItem> results = new ArrayList<>();
        for (MenuItem item : restaurant.getMenu().getAllItems()) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())
                    || item.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public synchronized MenuItem getMenuItem(String restaurantId, String itemId) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            return null;
        }
        return restaurant.getMenu().getItem(itemId);
    }

    public synchronized void updateRestaurantStatus(String restaurantId, boolean isOpen) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant != null) {
            restaurant.setOpen(isOpen);
        }
    }

    public synchronized int getTotalRestaurants() {
        return restaurants.size();
    }

    @Override
    public void update(Order order) {
        Restaurant restaurant = restaurants.get(order.getRestaurantId());
        if (restaurant != null) {
            restaurant.update(order);
        }
    }
}
