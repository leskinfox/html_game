package services.players;

import services.db.DBService;
import services.db.UsersDataSet;

import java.sql.SQLException;

public class Player {
    private final DBService dbService;
    private final String sessionId;
    private final String name;
    private long rating;
    private long life;
    private long damage;
    private boolean ready;
    private Battle battle;
    private int idInBattle;

    public Player(DBService dbService, String sessionId, UsersDataSet user) {
        this.dbService = dbService;
        this.sessionId = sessionId;
        name = user.getLogin();
        rating = user.getRating();
        life = user.getLife();
        damage = user.getDamage();
        ready = false;
        battle = null;
        idInBattle = 0;
    }

    boolean call(Player enemy) {
        if(ready) {
            ready = false;
            enemy.ready = false;
            new Battle(this, enemy);
            return true;
        }
        return false;
    }

    public void takeWin() {
        rating++;
        damage++;
        life++;
        save();
    }

    public void takeLose() {
        rating--;
        damage++;
        life++;
        save();
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void setIdInBattle(int idInBattle) {
        this.idInBattle = idInBattle;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void exit() {
        if (battle != null) {
            battle.exit(idInBattle);
            battle = null;
        }
    }

    public boolean hasBattle() {
        return battle != null;
    }

    public boolean isReady() {
        return ready;
    }

    public String getName() {
        return name;
    }

    public long getRating() {
        return rating;
    }

    public long getLife() {
        return life;
    }

    public long getDamage() {
        return damage;
    }

    public Battle getBattle() {
        return battle;
    }

    public int getIdInBattle() {
        return idInBattle;
    }

    public String getSessionId() {
        return sessionId;
    }

    private void save() {
        try {
            dbService.updateUser(sessionId, name, rating, life, damage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
