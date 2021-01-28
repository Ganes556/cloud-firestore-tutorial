package com.example.addandretrievemultipledoc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.addandretrievemultipledoc.databinding.ActivityListBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;

public class ListFirebase extends  AppCompatActivity{
    private ActivityListBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        for (int i = 1; i <= 20; i++) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            Button btn = new Button(this);
//            btn.setId(i);
//            final int id_ = btn.getId();
//            btn.setText("button " + id_);
//            btn.setBackgroundColor(Color.rgb(70, 80, 90));
//            binding.layout.addView(btn);
//            Button btn1 = ((Button) findViewById(id_));
//            btn1.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),
//                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
//                            .show();
//                }
//            });
//        }
        // make dinamic button with firebase
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Note note = documentSnapshot.toObject(Note.class);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 30;
                            Button btn = new Button(ListFirebase.this);
                            btn.setText(note.getTitle());
                            btn.setBackgroundColor(Color.parseColor("#817366"));
                            binding.layout.addView(btn,params);

                        }
                    }
                });

    }
}
