package concurs.model;
public enum CompetitionType {
    DRAWING("Desen"),
    TREASURE_HUNT("Cautarea unei comori"),
    POETRY("Poezie"),
    MARATHON("Maraton");

    private final String displayName;

    CompetitionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}