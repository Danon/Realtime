package ui;

public class CharacterEndScore {
    private int kills, deaths, minionsKilled, lvl;

    public CharacterEndScore(int kills, int deaths, int minionsKilled, int lvl) {
        this.kills = kills;
        this.deaths = deaths;
        this.minionsKilled = minionsKilled;
        this.lvl = lvl;
    }

    public int getKillsCount() {
        return kills;
    }

    public int getDeathsCount() {
        return deaths;
    }

    public int getMinionsKilledCount() {
        return minionsKilled;
    }

    public int getLvl() {
        return lvl;
    }
}
