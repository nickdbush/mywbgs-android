package com.nickdbush.mywbgs.models;

public class Subject {

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
