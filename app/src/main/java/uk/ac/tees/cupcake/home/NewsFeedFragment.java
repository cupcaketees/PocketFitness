package uk.ac.tees.cupcake.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.FeedAdapter;
import uk.ac.tees.cupcake.feed.Post;

public class NewsFeedFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        recyclerView = rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);


        getPosts();

        return  rootView;
    }
    
    

    private void getPosts() {


        List<Post> posts = new ArrayList<>();
        FeedAdapter feedAdapter = new FeedAdapter(posts);
        recyclerView.setAdapter(feedAdapter);

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");

        collectionReference.get().addOnSuccessListener(querySnapshot -> {

            for (DocumentSnapshot d : querySnapshot) {
                d.getReference().collection("User Posts")
                        .orderBy("date", Query.Direction.DESCENDING).limit(100)
                        .get().addOnSuccessListener(snapshot -> {
                    posts.addAll(snapshot.toObjects(Post.class));
                    feedAdapter.notifyDataSetChanged();
                });
            }
        });

    }

}
