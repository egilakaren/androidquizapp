package br.com.android.work.android_quizapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.android.work.android_quizapp.Common.Common;
import br.com.android.work.android_quizapp.Interface.ItemClickListener;
import br.com.android.work.android_quizapp.Interface.RankingCallback;
import br.com.android.work.android_quizapp.Model.QuestionScore;
import br.com.android.work.android_quizapp.Model.Ranking;
import br.com.android.work.android_quizapp.ViewHolder.RankingViewHolder;

public class RankingFragment extends Fragment {

    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;

    int sum = 0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);

        rankingList = (RecyclerView) myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUsername(), new RankingCallback<Ranking>() {
            @Override
            public void callback(Ranking ranking) {
                rankingTbl.child(ranking.getUsername()).setValue(ranking);
            }
        });
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(Ranking.class, R.layout.layout_ranking, RankingViewHolder.class, rankingTbl.orderByChild("score")) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, Ranking model, int position) {
                viewHolder.txt_name.setText(model.getUsername());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;
    }

    private void updateScore(final String username, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    QuestionScore ques = data.getValue(QuestionScore.class);
                    sum += Integer.parseInt(ques.getScore());
                }
                Ranking ranking = new Ranking(username, sum);
                callback.callback(ranking);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
