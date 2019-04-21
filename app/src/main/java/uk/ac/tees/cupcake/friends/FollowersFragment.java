package uk.ac.tees.cupcake.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FollowersFragment extends Fragment {

    private View view;

    private SearchFriendsAdapter adapter;

    private String mCurrentUserUid;

    private CollectionReference usersCollection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_recycler_view, container, false);

        usersCollection = FirebaseFirestore.getInstance().collection("Users");
        mCurrentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initialise();

        return view;
    }

    private void initialise() {
        ArrayList<UserProfile> profiles = new ArrayList<>();
        adapter = new SearchFriendsAdapter(profiles);

        usersCollection.document(mCurrentUserUid)
                .collection("Followers")
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
                                    profiles.add(otherProfileDoc.toObject(UserProfile.class));

                                    adapter.notifyDataSetChanged();
                                });
                    }

                    RecyclerView recyclerView = view.findViewById(R.id.myRecycleView);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(adapter);
                });
    }
}