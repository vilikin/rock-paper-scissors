package in.vilik.kps;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity {

    final int ROUNDS = 10;
    final int SPEED = 1000;

    int playerScore = 0;
    int computerScore = 0;

    TextView playerScoreView;
    TextView computerScoreView;
    TextView statusTextView;

    ImageView playerImage;
    ImageView computerImage;

    MyDialog dialog;
    Handler handler;

    SoundPool pool;
    boolean soundsLoaded;
    int winSound, defeatSound;

    HighScores highScores;

    public void updateScores() {
        playerScoreView.setText(getResources().getString(R.string.player_score, playerScore));
        computerScoreView.setText(getResources().getString(R.string.computer_score, computerScore));

        ObjectAnimator animator = ObjectAnimator.ofFloat(statusTextView, "scaleX", 2);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerScoreView = (TextView) findViewById(R.id.player_score);
        computerScoreView = (TextView) findViewById(R.id.computer_score);
        statusTextView = (TextView) findViewById(R.id.won_lost);

        playerImage = (ImageView) findViewById(R.id.player_hand);
        computerImage = (ImageView) findViewById(R.id.computer_hand);

        highScores = new HighScores(this);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
            attrBuilder.setUsage(AudioAttributes.USAGE_GAME);
            AudioAttributes attr = attrBuilder.build();

            builder.setAudioAttributes(attr);

            pool = builder.build();
        } else {
            pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        winSound = pool.load(this, R.raw.victory, 1);
        defeatSound = pool.load(this, R.raw.defeat, 1);

        updateScores();

        startGame();
    }

    public void startGame() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
                RPS[] possibleChoices = {RPS.Paper, RPS.Rock, RPS.Scissors};

                for (int i = 0; i < ROUNDS; i++) {
                    Intent intent = new Intent("statusUpdate");
                    intent.putExtra("action", "prompt");
                    manager.sendBroadcast(intent);

                    while (dialog == null || !dialog.isChosen()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    RPS playerChoice = RPS.values()[dialog.getChoice()];

                    dialog = null;

                    intent = new Intent("statusUpdate");
                    intent.putExtra("action", "shake");
                    manager.sendBroadcast(intent);

                    try {
                        Thread.sleep(SPEED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int random = (int) (Math.random() * possibleChoices.length);
                    RPS computerChoice = possibleChoices[random];

                    RPS winner = getWinner(playerChoice, computerChoice);

                    intent = new Intent("statusUpdate");
                    intent.putExtra("action", "result");
                    intent.putExtra("winner", winner.getValue());
                    intent.putExtra("player", playerChoice.getValue());
                    intent.putExtra("computer", computerChoice.getValue());
                    manager.sendBroadcast(intent);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent("statusUpdate");
                intent.putExtra("action", "gameover");
                manager.sendBroadcast(intent);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                intent = new Intent("statusUpdate");
                intent.putExtra("action", "end");
                manager.sendBroadcast(intent);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();

                switch (extras.getString("action", "")) {
                    case "shake":
                        shakeHands();
                        break;
                    case "result":
                        RPS playerChoice = RPS.values()[extras.getInt("player")];
                        playerImage.setImageDrawable(getImage(playerChoice));
                        RPS computerChoice = RPS.values()[extras.getInt("computer")];
                        computerImage.setImageDrawable(getImage(computerChoice));
                        RPS winner = RPS.values()[extras.getInt("winner")];

                        if (winner == RPS.Player) {
                            pool.play(winSound, 1, 1, 1, 0, 1f);
                            statusTextView.setText(getResources().getString(R.string.you_won));
                            playerScore++;
                        } else if (winner == RPS.Computer) {
                            pool.play(defeatSound, 1, 1, 1, 0, 1f);
                            statusTextView.setText(getResources().getString(R.string.you_lost));
                            computerScore++;
                        } else {
                            statusTextView.setText(getResources().getString(R.string.draw));
                        }

                        updateScores();

                        break;
                    case "prompt":
                        String[] choices = {"Rock", "Paper", "Scissors"};
                        dialog = new MyDialog(GameActivity.this, "Your choice?", choices);
                        break;
                    case "gameover":
                        //ArrayList<HighScores.Entry> scores = highScores.getBestScores(5);

                        if (playerScore > computerScore) {
                            pool.play(winSound, 1, 1, 1, 0, 1f);
                            statusTextView.setText(getResources().getString(R.string.game_over_win));
                        } else if (playerScore < computerScore) {
                            pool.play(defeatSound, 1, 1, 1, 0, 1f);
                            statusTextView.setText(getResources().getString(R.string.game_over_loss));
                        } else {
                            statusTextView.setText(getResources().getString(R.string.game_over_draw));
                        }

                        updateScores();

                        break;
                    case "end":
                        PromptDialog.open(GameActivity.this, "Enter your name", new PromptDialog.Listener() {
                            @Override
                            public void onComplete(@Nullable String text) {
                                if (text != null) {
                                    highScores.add(text, playerScore);
                                }

                                Intent i = new Intent(GameActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });

                        break;
                }
            }
        }, new IntentFilter("statusUpdate"));

        t.start();
    }

    private void shakeHands() {
        playerImage.setImageDrawable(getImage(RPS.Rock));
        computerImage.setImageDrawable(getImage(RPS.Rock));

        ObjectAnimator playerAnim = ObjectAnimator.ofFloat(playerImage, "rotation", 180);
        playerAnim.setDuration(SPEED / 2);
        playerAnim.setRepeatMode(ObjectAnimator.REVERSE);
        playerAnim.setRepeatCount(1);

        ObjectAnimator computerAnim = ObjectAnimator.ofFloat(computerImage, "rotation", -180);
        computerAnim.setDuration(SPEED / 2);
        computerAnim.setRepeatMode(ObjectAnimator.REVERSE);
        computerAnim.setRepeatCount(1);

        playerAnim.start();
        computerAnim.start();
    }

    private RPS getWinner(RPS playerChoice, RPS computerChoice) {
        if (playerChoice == computerChoice) {
            return RPS.Draw;
        } else if ((playerChoice == RPS.Rock && computerChoice == RPS.Scissors) ||
                (playerChoice == RPS.Scissors && computerChoice == RPS.Paper) ||
                (playerChoice == RPS.Paper && computerChoice == RPS.Rock)) {
            return RPS.Player;
        } else {
            return RPS.Computer;
        }
    }

    private Drawable getImage(RPS rps) {
        switch (rps) {
            case Rock:
                return ResourcesCompat.getDrawable(getResources(), R.drawable.rock, null);
            case Scissors:
                return ResourcesCompat.getDrawable(getResources(), R.drawable.scissors, null);
            case Paper:
                return ResourcesCompat.getDrawable(getResources(), R.drawable.paper, null);
            default:
                return ResourcesCompat.getDrawable(getResources(), R.drawable.rock, null);
        }
    }
}
