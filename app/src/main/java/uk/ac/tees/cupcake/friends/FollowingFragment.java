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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.SearchFriendsAdapter;
import uk.ac.tees.cupcake.feed.Post;

public class FollowingFragment extends Fragment {
    private static final String TAG = "FollowingFragment";
    private View view;
    private SearchFriendsAdapter adapter;
    private String currentUserUid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_recycler_view, container, false);
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initialise();
        return view;
    }

    private void initialise() {
        List<String> followers = new ArrayList<>();
        ArrayList<Post> profiles = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Users/" + currentUserUid + "/User Following")
                .get()
                .addOnSuccessListener(documentSnapshots -> {

                    for(DocumentSnapshot documentSnapshot: documentSnapshots) {
                        followers.add(documentSnapshot.getId());
                        Log.d(TAG, "initialise: " + followers.size());
                    }


                    for(int i=0; i< followers.size(); i++) {
                        FirebaseFirestore.getInstance()
                                .collection("Users")
                                .document(followers.get(i))
                                .get()
                                .addOnSuccessListener(v -> {
                                    Post temp  = v.toObject(Post.class);
                                    temp.setUserUid(v.getId());
                                    profiles.add(temp);
                                    adapter.notifyDataSetChanged();
                                });
                    }

                    adapter = new SearchFriendsAdapter(profiles, getActivity());

                    RecyclerView recyclerView = view.findViewById(R.id.myRecycleView);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(adapter);
                });
    }


}
