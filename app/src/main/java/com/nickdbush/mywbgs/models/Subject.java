package com.nickdbush.mywbgs.models;

public class Subject {

    public static final Subject[] SUBJECTS = {
            new Subject("Physics", 0xff0000),
            new Subject("Chemistry", 0xe57a00),
            new Subject("Biology", 0x5ccc33),
            new Subject("PRE", 0x00ccff),
            new Subject("Psychology", 0xb63df2),
            new Subject("PSHE", 0xbf0000),
            new Subject("PE", 0xf2b63d),
            new Subject("Music", 0x00ff66),
            new Subject("French", 0x00a2f2),
            new Subject("German", 0xff40f2),
            new Subject("Spanish", 0xff4040),
            new Subject("Mathematics", 0xfff240),
            new Subject("Futher Mathematics", 0x40ffa6),
            new Subject("History", 0x0066bf),
            new Subject("Politics", 0xbf308f),
            new Subject("Geography", 0xd96236),
            new Subject("Economics", 0xbfb630),
            new Subject("English", 0x30bf8f),
            new Subject("Deign Technology", 0x0061f2),
            new Subject("Classics", 0xff408c),
            new Subject("Latin", 0xf26100),
            new Subject("Computing", 0xb8e600),
            new Subject("ICT", 0x00f2e2),
            new Subject("Art", 0x4330bf),
            new Subject("History of art", 0xbf3043),
            new Subject("Free Period", 0xcc7033),
            new Subject("Spanish", 0x66ff00),
    };

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
