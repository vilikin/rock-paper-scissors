package in.vilik.kps;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button)findViewById(R.id.btnStart);
        Button btnHighscore = (Button)findViewById(R.id.btnHighscore);

        Animation btn1Animation = AnimationUtils.loadAnimation(this, R.anim.btn1);
        Animation btn2Animation = AnimationUtils.loadAnimation(this, R.anim.btn2);

        btnStart.startAnimation(btn1Animation);
        btnHighscore.startAnimation(btn2Animation);

        TextView title = (TextView)findViewById(R.id.title);
        ObjectAnimator animator = ObjectAnimator.ofFloat(title, "alpha", 0);
        animator.setDuration(1000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(title, "rotation", 360);
        animator2.setDuration(3000);
        animator2.setRepeatCount(ObjectAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator2);
        set.start();
    }

    public void startGame(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void openHighScores(View view) {
        Intent i = new Intent(this, HighScoreActivity.class);
        startActivity(i);
    }
}
