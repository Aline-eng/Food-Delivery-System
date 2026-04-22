public class AgentUnavailableException extends RuntimeException {
    public AgentUnavailableException(String agentName) {
        super("Delivery agent '" + agentName + "' is currently unavailable.");
    }
}
