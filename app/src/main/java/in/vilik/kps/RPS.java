package in.vilik.kps;

/**
 * Created by vili on 23/03/2017.
 */

public enum RPS {
    Rock(0), Paper(1), Scissors(2), Player(3), Computer(4), Draw(5);

    private final int value;
    private RPS(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
