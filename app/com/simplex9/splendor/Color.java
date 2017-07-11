package com.simplex9.splendor;

/**
 * Created by hongbo on 7/10/17.
 */
public enum Color {
    WHITE(0), BLUE(1), GREEN(2), RED(3), ONYX(4);

    int color;
    Color(int color) {
        this.color = color;
    }

    public int id() {
        return color;
    }

    static Color[] colors = Color.values();

    public static int size() {
        return colors.length;
    }

    public static Color getColor(int color){
        return colors[color];
    }
}
