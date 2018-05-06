package no.ntnu.stud.dominih.groupten.switcheroo;

import java.util.ArrayList;
import java.util.List;

// This is a model class, so warnings are ignored to "make it look" better, instead
// of looking at what would theoretically be optimal.
@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess"})

/**
 * Model class, represents a single game, serves as the class for the respective JSON object
 * in Firebase.
 *
 *  @see GameTransaction
 */
class Game {

    private final String gameid;

    final String lastUpdate = Long.toHexString(System.currentTimeMillis());
    List<String> log = new ArrayList<>();
    List<String> players = new ArrayList<>();

    public Game(String gameid) {

        this.gameid = gameid;

    }

    public String getGameid() {
        return gameid;
    }
}
