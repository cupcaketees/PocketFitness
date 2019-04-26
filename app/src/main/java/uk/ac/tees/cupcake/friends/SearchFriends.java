package uk.ac.tees.cupcake.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.adapters.SearchFriendsAdapter;


public abstract class SearchFriends extends Fragment {
    private View view;

    private static final String TAG = "SearchFriends";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_recycler_view, container, false);
        initialise("");
        return view;
    }



    public void initialise(String friendType) {
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");

        ArrayList<UserProfile> profiles = new ArrayList<>();

        SearchFriendsAdapter adapter =new SearchFriendsAdapter(profiles,friendType);
        FirebaseFirestore.getInstance().collection("Users").document(getActivity().getIntent().getStringExtra("id"))
                .collection(friendType)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (!documentSnapshot.exists()) {
                            return;
                        }

                        String id = documentSnapshot.getId();

                        usersCollection.document(id)
                                .get()
                                .addOnSuccessListener(otherProfileDoc -> {
                                    if(otherProfileDoc.exists()) {

                                        profiles.add(otherProfileDoc.toObject(UserProfile.class));
                                        adapter.notifyDataSetChanged();
                                    }

                                });
                    }

                    RecyclerView recyclerView = view.findViewById(R.id.myRecycleView);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(adapter);
                });
    }
}
