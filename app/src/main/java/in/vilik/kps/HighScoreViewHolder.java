package in.vilik.kps;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vili on 24/03/2017.
 */

class HighScoreViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView score;

    public HighScoreViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.nameView);
        score = (TextView)itemView.findViewById(R.id.scoreView);
    }
}
