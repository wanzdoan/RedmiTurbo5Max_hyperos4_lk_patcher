package androidx.appsearch.builtintypes;

/* JADX INFO: loaded from: classes.dex */
public abstract class SportsEvent extends Event {
    public abstract SportsTeam getAwayTeam();

    public abstract String getAwayTeamAccessoryScore();

    public abstract String getAwayTeamScore();

    public abstract double getAwayTeamWinProbability();

    public abstract String getGameTemporalState();

    public abstract SportsTeam getHomeTeam();

    public abstract String getHomeTeamAccessoryScore();

    public abstract String getHomeTeamScore();

    public abstract double getHomeTeamWinProbability();

    public abstract String getNotableDetail();

    public abstract Organization getOrganizer();

    public abstract long getResult();

    public abstract String getSport();

    public abstract long getSportsEventStatus();

    public abstract String getSportsEventStatusLabel();

    public abstract boolean isPlaceHomeTeamAtStart();
}
