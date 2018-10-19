package services.players;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayersService {
    private final Map<String, Player> players;

    public PlayersService() {
        players = new ConcurrentHashMap<>();
    }

    public boolean exist(String sessionId) {
        return players.containsKey(sessionId);
    }

    public void add(String sessionId, Player player) {
        players.put(sessionId, player);
    }

    public Player get(String sessionId) {
        return players.get(sessionId);
    }

    public boolean searchEnemy(Player player) {
        for(Player enemy : players.values())
            if (!enemy.getName().equals(player.getName()) && enemy.call(player))
                return true;
        return false;
    }

    public void remove(String sessionId) {
        players.remove(sessionId);
    }
}
