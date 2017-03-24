package in.vilik.kps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    ArrayList<HighScores.Entry> scores;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    HighScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scores = new HighScores(this).getBestScores(100);

        recyclerView = (RecyclerView)findViewById(R.id.highScoreList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new HighScoreAdapter(scores);

        recyclerView.setAdapter(adapter);
    }
}
