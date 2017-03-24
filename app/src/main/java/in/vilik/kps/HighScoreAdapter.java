package in.vilik.kps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by vili on 24/03/2017.
 */

class HighScoreAdapter extends RecyclerView.Adapter<HighScoreViewHolder>{

    private ArrayList<HighScores.Entry> model;

    public HighScoreAdapter(ArrayList<HighScores.Entry> model) {
        this.model = model;
    }

    @Override
    public HighScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new HighScoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HighScoreViewHolder holder, int position) {
        HighScores.Entry entry = model.get(position);
        final String name = entry.name;
        final int score = entry.score;

        holder.name.setText(name);
        holder.score.setText(score + "");
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
