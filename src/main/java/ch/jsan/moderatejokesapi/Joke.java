package ch.jsan.moderatejokesapi;

public class Joke {

    public Joke() {}

    public Joke(String setup, String punchline, String type) {
        this.setup = setup;
        this.punchline = punchline;
        this.type = type;
    }
    public String setup;
    public String punchline;
    public String type;
}
