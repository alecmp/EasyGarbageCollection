package com.alessandro.easygarbagecollection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    DatabaseReference mRef;
    FirebaseRecyclerAdapter<TrashCan, TrashCanViewHolder> mAdapter;
    protected static final Query mQuery =
            FirebaseDatabase.getInstance().getReference().child("Navigation").limitToLast(50);
    ArrayList<TrashCan> trashCanArrayList;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
       // mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("Navigation");
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        trashCanArrayList = new ArrayList<>();

                        for (DataSnapshot trashCanSnapshot : dataSnapshot.getChildren()) {
                            TrashCan trashCan = trashCanSnapshot.getValue(TrashCan.class);
                            trashCanArrayList.add(trashCan);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseRecyclerOptions<TrashCan> options =
                new FirebaseRecyclerOptions.Builder<TrashCan>()
                        .setQuery(mQuery, TrashCan.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<TrashCan, TrashCanViewHolder>(options) {
            @Override
            public TrashCanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new TrashCanViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trashcan_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(TrashCanViewHolder holder, int position, TrashCan model) {
                Log.w("ONBIND", ":"+model.getCode());
                holder.bind(model);


            }
        } ;

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
