package pl.edu.pw.student.mini.gasstation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private RecyclerView historyView;
    private ArrayList<HistoryElement> data = new ArrayList<>();

    private HistoryAdapter adapter;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    LinearLayoutManager llm;

    public HistoryFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adapter = new HistoryAdapter(data);
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        historyView = (RecyclerView)v.findViewById(R.id.history_list);
        llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        historyView.setLayoutManager(llm);
        historyView.addItemDecoration( new SimpleItemDecoration(getActivity()));
        historyView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
        databaseReference.child("users").child(currentUser.getUid()).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot historyElement : dataSnapshot.getChildren()){
                    //If it's a map it means we received an object like HistoryElement
                    if(historyElement.getValue() instanceof Map){
                        Map<String, Object> map = (Map<String, Object>) historyElement.getValue();
                        Log.i("onDataChange()", "in HistoryFragment we received " + map.toString());
                        //We can use the built-in JSON-to-POJO serializer/deserializer. See http://stackoverflow.com/questions/30933328/how-to-convert-firebase-data-to-java-object
                        HistoryElement element = historyElement.getValue(HistoryElement.class);
                        data.add(element);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}
        else
        {
            Toast.makeText(getActivity(), "Please Login To Access History or Update the Prices", Toast.LENGTH_LONG).show();
        }
        return v;
    }






}
