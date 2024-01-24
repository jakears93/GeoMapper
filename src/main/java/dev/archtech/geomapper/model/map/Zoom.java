package dev.archtech.geomapper.model.map;

public enum Zoom {
    WORLD("World", 1),
    STREET("Street", 15),
    STREET_PLUS_1("Street+", 16),
    STREET_PLUS_2("Street", 17),
    STREET_PLUS_3("Street", 18),
    BUILDING("Building", 20);

    private String label;
    private int level;

    Zoom(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public int getLevel() {
        return level;
    }
}
