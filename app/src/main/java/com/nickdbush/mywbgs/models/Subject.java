package com.nickdbush.mywbgs.models;

public class Subject {

    public static final Subject[] SUBJECTS = {
            new Subject("Physics", 0xffff0000),
            new Subject("Chemistry", 0xffe57a00),
            new Subject("Biology", 0xff5ccc33),
            new Subject("PRE", 0xff00ccff),
            new Subject("Psychology", 0xffb63df2),
            new Subject("PSHE", 0xffbf0000),
            new Subject("PE", 0xfff2b63d),
            new Subject("Music", 0xff00ff66),
            new Subject("French", 0xff00a2f2),
            new Subject("German", 0xffff40f2),
            new Subject("Spanish", 0xffff4040),
            new Subject("Mathematics", 0xfffff240),
            new Subject("Futher Mathematics", 0xff40ffa6),
            new Subject("History", 0xff0066bf),
            new Subject("Politics", 0xffbf308f),
            new Subject("Geography", 0xffd96236),
            new Subject("Economics", 0xffbfb630),
            new Subject("English", 0xff30bf8f),
            new Subject("Deign Technology", 0xff0061f2),
            new Subject("Classics", 0xffff408c),
            new Subject("Latin", 0xfff26100),
            new Subject("Computing", 0xffb8e600),
            new Subject("ICT", 0xff00f2e2),
            new Subject("Art", 0xff4330bf),
            new Subject("History of art", 0xffbf3043),
            new Subject("Free Period", 0xffcc7033),
            new Subject("Spanish", 0xff66ff00),
            new Subject("Games", 0xff00becc),
    };

    public static int getIdByName(String name) {
        for(int i = 0; i < SUBJECTS.length; i++) {
            if(SUBJECTS[i].NAME.equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }

    public final String NAME;
    public final int COLOR;

    public Subject(String name, int color) {
        this.NAME = name;
        this.COLOR = color;
    }

    public int getId() {
        for(int i = 0; i < SUBJECTS.length; i++) {
            if(SUBJECTS[i].NAME.equalsIgnoreCase(NAME))
                return i;
        }
        return -1;
    }
}
