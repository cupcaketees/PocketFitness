package uk.ac.tees.cupcake.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import java.util.Date;
import java.util.TreeMap;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.FeedAdapter;
import uk.ac.tees.cupcake.feed.Post;

public class NewsFeedFragment extends Fragment {

    private FeedAdapter mFeedAdapter;
    private CollectionReference collectionReference;
    private ArrayList<Post> mPosts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        // Initialise
        RecyclerView recyclerView = view.findViewById(R.id.feed_recycler_view);
        collectionReference = FirebaseFirestore.getInstance().collection("Users");

        //Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        //Setup
        recyclerView.setLayoutManager(layoutManager);

        mFeedAdapter = new FeedAdapter(mPosts);
        recyclerView.setAdapter(mFeedAdapter);

        //Get all user posts.
        getAllPosts();

        return view;
    }

    /**
     * Gets all posts from each user, orders then by date of post.
     */
    private void getAllPosts(){
        TreeMap<Date, Post> allPosts = new TreeMap<>();

        collectionReference.get()
                           .addOnSuccessListener(documentSnapshots -> {
                               // Iterates "Users" collection
                               for(DocumentSnapshot documentSnapshot : documentSnapshots){
                                   //Users to "User Posts"
                                   documentSnapshot.getReference()
                                                   .collection("User Posts")
                                                   .get()
                                                   .addOnSuccessListener(documentSnapshots1 -> {
                                                       // Iterates all "Users Posts" and adds each one to allPosts
                                                       // TreeMap with TimeStamp as Key and Post as value.
                                                       for(DocumentSnapshot documentSnapshot1 : documentSnapshots1){
                                                           Post currentDoc = documentSnapshot1.toObject(Post.class);

                                                           allPosts.put(currentDoc.getTimeStamp(), currentDoc);
                                                       }

                                                       // Removes all current posts in mPost array, adds all values in allPost treeMap in descending order.
                                                       mPosts.clear();
                                                       mPosts.addAll(allPosts.descendingMap().values());

                                                       mFeedAdapter.notifyDataSetChanged();
                                                   });
                               }
                           });
    }

}