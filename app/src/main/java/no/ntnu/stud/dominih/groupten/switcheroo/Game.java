package no.ntnu.stud.dominih.groupten.switcheroo;

import java.util.ArrayList;
import java.util.List;

public class Game {

    String gameid;

    String lastUpdate = Long.toHexString(System.currentTimeMillis());
    List<String> log = new ArrayList<>();
    List<String> players = new ArrayList<>();

    public Game(String gameid) {

        this.gameid = gameid;

    }

}
