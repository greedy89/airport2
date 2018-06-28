package com.example.user.airport2;

import java.util.Calendar;

public class Absen {
    private String waktu;
    private String tag;

    public Absen() {

    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Absen{" +
                "waktu='" + waktu + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
