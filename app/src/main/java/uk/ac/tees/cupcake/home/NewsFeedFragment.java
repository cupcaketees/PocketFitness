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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        //getAllPublicPosts();

        getFollowersOnlyPosts();

        return view;
    }

    /**
     * Queries all posts from each user that are public, orders then by date of post.
     */
    private void getAllPublicPosts(){

        mPosts.clear();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                for(DocumentSnapshot documentSnapshot : documentSnapshots){

                    documentSnapshot.getReference()
                                    .collection("User Posts")
                                    .whereEqualTo("privacyLevel", 2)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot documentSnapshots) {

                                            for(DocumentSnapshot documentSnapshot1 : documentSnapshots){

                                                Post post = documentSnapshot1.toObject(Post.class);
                                                mPosts.add(post);
                                            }

                                            Collections.sort(mPosts, new Comparator<Post>() {
                                                @Override
                                                public int compare(Post o1, Post o2) {
                                                    return o2.getTimeStamp().compareTo(o1.getTimeStamp());
                                                }
                                            });

                                            mFeedAdapter.notifyDataSetChanged();
                                        }
                                    });
                }
            }
        });
    }

    /**
     * Queries to get only users followers posts that are not set to only me, orders then by date of post.
     */
    private void getFollowersOnlyPosts(){

        //mPosts.clear();
        String currentUserUid = FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getUid();

        collectionReference.document(currentUserUid)
                           .collection("Following")
                           .get()
                           .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                               @Override
                               public void onSuccess(QuerySnapshot documentSnapshots) {

                                   for(DocumentSnapshot documentSnapshot : documentSnapshots){

                                       collectionReference.document(documentSnapshot.getReference().getId())
                                                          .collection("User Posts")
                                                          .whereGreaterThanOrEqualTo("privacyLevel" , 1)
                                                          .get()
                                                          .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                              @Override
                                                              public void onSuccess(QuerySnapshot documentSnapshots) {

                                                                  for(DocumentSnapshot documentSnapshot1 : documentSnapshots){
                                                                      Post post = documentSnapshot1.toObject(Post.class);
                                                                      mPosts.add(post);
                                                                  }

                                                                  Collections.sort(mPosts, new Comparator<Post>() {
                                                                      @Override
                                                                      public int compare(Post o1, Post o2) {
                                                                          return o2.getTimeStamp().compareTo(o1.getTimeStamp());
                                                                      }
                                                                  });

                                                                  mFeedAdapter.notifyDataSetChanged();
                                                              }
                                                          });

                                   }
                               }
                           });

    }

}