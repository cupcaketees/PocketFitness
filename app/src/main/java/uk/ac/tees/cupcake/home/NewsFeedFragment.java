package uk.ac.tees.cupcake.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import uk.ac.tees.cupcake.posts.CreatePostActivity;

public class NewsFeedFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FeedAdapter mFeedAdapter;
    private CollectionReference collectionReference;
    private ArrayList<Post> mPosts = new ArrayList<>();

    private TextView createPostTextView;
    private Spinner feedFilterSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        // Initialise
        RecyclerView recyclerView = view.findViewById(R.id.feed_recycler_view);
        collectionReference = FirebaseFirestore.getInstance().collection("Users");
        feedFilterSpinner = view.findViewById(R.id.feed_fragment_filter_spinner);
        createPostTextView = view.findViewById(R.id.feed_fragment_create_post_text_view);

        // setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.feedFilter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedFilterSpinner.setAdapter(adapter);
        feedFilterSpinner.setOnItemSelectedListener(this);

        createPostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreatePostActivity.class));
            }
        });

        //Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        //Setup
        recyclerView.setLayoutManager(layoutManager);

        mFeedAdapter = new FeedAdapter(mPosts);
        recyclerView.setAdapter(mFeedAdapter);

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

        mPosts.clear();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

        if(text.equalsIgnoreCase("Public Feed")){
            getAllPublicPosts();
        }

        if(text.equalsIgnoreCase("Following Feed")){
            getFollowersOnlyPosts();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}