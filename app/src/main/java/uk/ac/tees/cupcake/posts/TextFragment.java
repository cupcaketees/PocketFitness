package uk.ac.tees.cupcake.posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.feed.Post;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class TextFragment extends Fragment {
    private static final String TAG = "TextFragment";
    private EditText text;
    private FirebaseFirestore firebaseFirestore;
    private String mCurrentUserId;
    private TextView nextFragment;
    private ImageView shareClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        text = view.findViewById(R.id.textPost);
        nextFragment = view.findViewById(R.id.postNext);
        shareClose = view.findViewById(R.id.exitPost);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        Spinner spinner = view.findViewById(R.id.spinnerDirectories);
        spinner.setVisibility(View.INVISIBLE);

        setupText();
        initialiseToolbar();

        return view;
    }

    /**
     * Sets on click listeners for the buttons
     * When user clicks post it'll add the post to their account.
     */
    private  void initialiseToolbar() {
        shareClose.setOnClickListener(v -> IntentUtils.invokeBaseView(getActivity(), MainActivity.class));

        nextFragment.setText("Post");
        nextFragment.setOnClickListener(v -> {
            setEnabled(false);

            Post post = new Post(mCurrentUserId,null, text.getText().toString());

            firebaseFirestore.collection("Users/" + mCurrentUserId + "/User Posts/")
                    .add(post)
                    .addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("index", 1);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

            setEnabled(true);
        });
    }

    /**
     * @param b - pass parameter when to disable/enable buttons on screen
     */
    private void setEnabled (boolean b) {
        shareClose.setEnabled(b);
        nextFragment.setEnabled(b);
        text.setEnabled(b);
    }

    /**
     * Prevents user entering to many lines.
     */
    private void setupText() {
        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (text.getLayout() != null) {
                    if (text.getLayout().getLineCount() > 4){
                        text.getText().delete(text.getText().length() - 1, text.getText().length());
                    }
                }
            }

        });
    }
}
