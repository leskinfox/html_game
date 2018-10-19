package services.players;

public class Battle {
    private final long MAX_GAME_TIME = 10 * 60 * 1000;
    private final long WAIT_TIME = 5 * 1000;

    private long timeStartBattle;

    //two-element arrays
    private Player[] player = new Player[2];
    private long[] rating = new long[2];
    private String[] name = new String[2];
    private long[] health = new long[2];
    private long[] damage = new long[2];
    private boolean[] fExit = new boolean[2];
    private String[] log = new String[2];

    public Battle(Player player1, Player player2) {
        player[0] = player1; player[1] = player2;
        rating[0] = player1.getRating(); rating[1] = player2.getRating();
        name[0] = player1.getName(); name[1] = player2.getName();
        health[0] = player1.getLife(); health[1] = player2.getLife();
        damage[0] = player1.getDamage(); damage[1] = player2.getDamage();
        fExit[0] = false; fExit[1] = false;
        log[0] = ""; log[1] = "";
        player1.setBattle(this);
        player2.setBattle(this);
        player1.setIdInBattle(0);
        player2.setIdInBattle(1);
    }

    boolean isDead(int id) {
        return health[id]<=0;
    }

    // id attacks id2
    public void attack(int id) {
        if (!isGame())
            return;
        int id2 = id == 0 ? 1 : 0;
        if (isDead(id)) {
            log[id] += "<br>Вы убиты";
            lose(id);
            return;
        }
        if (isExit(id2)) {
            log[id] += "<br>" + name[id2] + " убежал";
            win(id);
            return;
        }
        takeAttack(id2);
        log[id] += "<br>Вы ударили" + name[id2] + "на " + damage[id] + "урона";
        if (isDead(id2)) {
            log[id] += "<br>Вы убили" + name[id2];
            win(id);
        }
    }

    private void takeAttack(int id) {
        int id2 = id == 0 ? 1 : 0;
        health[id] = health[id] < damage[id2] ? 0 : health[id] - damage[id2];
        log[id] += "<br>" + name[id2] + " ударил Вас " + "на " + damage[id2] + "урона";

    }

    private void win(int id) {
        log[id] += "<br>Вы выйграли! +1 рейтинга, +1 урона, +1 жизни";
        player[id].takeWin();
        exit(id);
        stopGame();
    }

    private void lose(int id) {
        log[id] += "<br>Вы проиграли! -1 рейтинга, +1 урона, +1 жизни";
        player[id].takeLose();
        exit(id);
        stopGame();
    }

    public String getLog(int id) {
        return log[id];
    }

    public String getMyName(int id) {
        return name[id];
    }

    public long getMyHealth(int id) {
        return health[id];
    }

    public long getMyDamage(int id) {
        return damage[id];
    }

    public long getMyRating(int id) {
        return rating[id];
    }

    public long getEnemyRating(int id) {
        id = id == 0 ? 1 : 0;
        return rating[id];
    }

    public String getEnemyName(int id) {
        id = id == 0 ? 1 : 0;
        return name[id];
    }

    public long getEnemyHealth(int id) {
        id = id == 0 ? 1 : 0;
        return health[id];
    }

    public long getEnemyDamage(int id) {
        id = id == 0 ? 1 : 0;
        return damage[id];
    }

    public boolean isExit(int id) {
        return fExit[id];
    }

    public void exit(int id) {
        fExit[id] = true;
    }

    public void stopGame() {
        timeStartBattle = 0;
    }

    public boolean isGame() {
        return isStartGame() && (!isExit(0) || !isExit(1));
    }

    public boolean isStartGame() {
        long timeInGame = System.currentTimeMillis() - timeStartBattle;
        return (timeInGame > WAIT_TIME) && (timeInGame < MAX_GAME_TIME);
    }

}
