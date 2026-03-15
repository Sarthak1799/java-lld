
import java.util.*;

public class DeliveryService implements OrderObserver {

    private static DeliveryService instance;
    private Map<String, DeliveryAgent> deliveryAgents;

    private DeliveryService() {
        this.deliveryAgents = new HashMap<>();
    }

    public static synchronized DeliveryService getInstance() {
        if (instance == null) {
            instance = new DeliveryService();
        }
        return instance;
    }

    public synchronized void registerDeliveryAgent(DeliveryAgent agent) {
        deliveryAgents.put(agent.getId(), agent);
    }

    public synchronized DeliveryAgent getDeliveryAgent(String agentId) {
        return deliveryAgents.get(agentId);
    }

    public synchronized List<DeliveryAgent> getAllDeliveryAgents() {
        return new ArrayList<>(deliveryAgents.values());
    }

    public synchronized List<DeliveryAgent> getAvailableDeliveryAgents() {
        List<DeliveryAgent> available = new ArrayList<>();
        for (DeliveryAgent agent : deliveryAgents.values()) {
            if (agent.isAvailable()) {
                available.add(agent);
            }
        }
        return available;
    }

    public synchronized void assignOrderToAgent(String agentId, Order order) {
        DeliveryAgent agent = deliveryAgents.get(agentId);
        if (agent != null) {
            agent.addOrder(order);
            order.setDeliveryAgentId(agentId);
        }
    }

    public synchronized void markOrderAsDelivered(String agentId, String orderId) {
        DeliveryAgent agent = deliveryAgents.get(agentId);
        if (agent != null) {
            agent.completeOrder(orderId);
        }
    }

    public synchronized List<Order> getAgentAssignedOrders(String agentId) {
        DeliveryAgent agent = deliveryAgents.get(agentId);
        if (agent != null) {
            return agent.getAssignedOrders();
        }
        return new ArrayList<>();
    }

    public synchronized void updateAgentAvailability(String agentId, boolean available) {
        DeliveryAgent agent = deliveryAgents.get(agentId);
        if (agent != null) {
            agent.setAvailable(available);
        }
    }

    public synchronized DeliveryAgent findNearestAvailableAgent() {
        List<DeliveryAgent> available = getAvailableDeliveryAgents();
        if (!available.isEmpty()) {
            // Simple implementation: return agent with least orders
            return available.stream()
                    .min(Comparator.comparingInt(DeliveryAgent::getAssignedOrderCount))
                    .orElse(null);
        }
        return null;
    }

    public synchronized int getTotalDeliveryAgents() {
        return deliveryAgents.size();
    }

    @Override
    public void update(Order order) {
        if (order.getDeliveryAgentId() != null) {
            DeliveryAgent agent = deliveryAgents.get(order.getDeliveryAgentId());
            if (agent != null) {
                agent.update(order);
            }
        }
    }
}
