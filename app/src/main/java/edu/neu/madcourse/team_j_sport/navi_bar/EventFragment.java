package edu.neu.madcourse.team_j_sport.navi_bar;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.neu.madcourse.team_j_sport.EventList.AddEvent;
import edu.neu.madcourse.team_j_sport.EventList.EventAdapter;
import edu.neu.madcourse.team_j_sport.EventList.ItemEvent;
import edu.neu.madcourse.team_j_sport.R;

public class EventFragment extends Fragment {

  private final ArrayList<ItemEvent> itemEvents = new ArrayList<>();

  View view;
  SharedPreferences sharedPreferences;

  public EventFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_event, container, false);
    sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
    initEventList();
    initFloatingBtn();
    return view;
  }

  private void initFloatingBtn() {
    FloatingActionButton fab = view.findViewById(R.id.Event_FAB);
    fab.setOnClickListener(
            view -> {
              Intent intent = new Intent(getActivity(), AddEvent.class);
              startActivity(intent);
            });
  }

  private void initEventList() {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef;

    myRef = database.getReference().child("Events");

    myRef
        .orderByKey()
        .addChildEventListener(
            new ChildEventListener() {
              @Override
              public void onChildAdded(
                  @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HashMap hashMap = (HashMap) snapshot.getValue();
                assert hashMap != null;
                itemEvents.add(
                    new ItemEvent(
                        Objects.requireNonNull(hashMap.get("title")).toString(),
                        Objects.requireNonNull(hashMap.get("time")).toString(),
                        Objects.requireNonNull(hashMap.get("summary")).toString(),
                        Objects.requireNonNull(hashMap.get("contact")).toString(),
                        Objects.requireNonNull(hashMap.get("location")).toString(),
                        snapshot.getKey()));
                createRecyclerView();
              }

              @Override
              public void onChildChanged(
                  @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

              @Override
              public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

              @Override
              public void onChildMoved(
                  @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });
  }

  private void createRecyclerView() {
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    RecyclerView recyclerView = view.findViewById(R.id.rv_event_list);
    recyclerView.setHasFixedSize(true);

    EventAdapter eventAdapter = new EventAdapter(itemEvents, getActivity().getApplicationContext());

    recyclerView.setAdapter(eventAdapter);
    recyclerView.setLayoutManager(layoutManager);
  }
}
