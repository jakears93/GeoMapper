package dev.archtech.geomapper.model.map;

public enum MarkerColour {
    RED("FF0000", "red"),
    GREEN("00FF00", "green"),
    BLUE("0000FF", "blue"),
    WHITE("000000", "white"),
    BLACK("FFFFFF", "black"),
    GREY("888888", "grey");

    private String hexCode;
    private String colourString;

    MarkerColour(String hexCode) {
        this.hexCode = hexCode;
    }

    MarkerColour(String hexCode, String colourString) {
        this.hexCode = hexCode;
        this.colourString = colourString;
    }

    public String getHexCode() {
        return hexCode;
    }

    public String getColourString() {
        return colourString;
    }
}
