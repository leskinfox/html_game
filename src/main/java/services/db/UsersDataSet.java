package services.db;

public class UsersDataSet {

    private long id;
    private String login;
    private String password;
    private long rating;
    private long life;
    private long damage;

    public UsersDataSet(long id, String login, String password, long rating, long life, long damage) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.rating = rating;
        this.life = life;
        this.damage = damage;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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
}
