package com.nickdbush.mywbgs.models;

public class Subject {

    // private static int colour_index;
    // private static String[] colours = {
    //         "#e00000",
    //         "#c7a932",
    //         "#28a168",
    //         "#3892e0",
    //         "#e000b4",
    //         "#d40000",
    //         "#c9d435",
    //         "#32c7a9",
    //         "#0055d4",
    //         "#c73281",
    //         "#ba542f",
    //         "#74ad00",
    //         "#32bdc7",
    //         "#0017ad",
    //         "#ad0045",
    //         "#e05a00",
    //         "#3ce000",
    //         "#2889a1",
    //         "#7100d4",
    //         "#e0003c",
    //         "#ba6300",
    //         "#00a115",
    //         "#359fd4",
    //         "#7828a1",
    //         "#a10015",
    //         "#e09600",
    //         "#38e07b",
    //         "#0063ba",
    //         "#d100e0"
    // };
    // public static int getNextColour() {
    //     colour_index = (colour_index + 1) % colours.length;
    //     return Color.parseColor(colours[colour_index]);
    // }

    public static final Subject[] SUBJECTS = {
            new Subject("Physics"),
            new Subject("Chemistry"),
            new Subject("Biology"),
            new Subject("PRE"),
            new Subject("Psychology"),
            new Subject("PSHE"),
            new Subject("PE"),
            new Subject("Music"),
            new Subject("French"),
            new Subject("German"),
            new Subject("Spanish"),
            new Subject("Mathematics"),
            new Subject("Futher Mathematics"),
            new Subject("History"),
            new Subject("Politics"),
            new Subject("Geography"),
            new Subject("Economics"),
            new Subject("English"),
            new Subject("Deign Technology"),
            new Subject("Classics"),
            new Subject("Latin"),
            new Subject("Computing"),
            new Subject("ICT"),
            new Subject("Art"),
            new Subject("History of art"),
            new Subject("Free Period"),
            new Subject("Spanish"),
            new Subject("Games")
    };
    public final String NAME;

    public Subject(String name) {
        this.NAME = name;
        // this.COLOR = getNextColour();
    }
    // public final int COLOR;

    public static int getIdByName(String name) {
        for (int i = 0; i < SUBJECTS.length; i++) {
            if (SUBJECTS[i].NAME.equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }

    public int getId() {
        for (int i = 0; i < SUBJECTS.length; i++) {
            if (SUBJECTS[i].NAME.equalsIgnoreCase(NAME))
                return i;
        }
        return -1;
    }
}
