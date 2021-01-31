package com.example.firebase_learning_queris;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebase_learning_queris.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    // make automatic fetch data from firebase into textView
    @Override
    protected void onStart() {
        super.onStart();
        // and then make save that value into noteListener = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>(){ blabla)};
        // addition word 'this' into listener for makes that automatic detach from listener


        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;

                }
                String data = "";
                for(QueryDocumentSnapshot documentSnapshot : value){
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setIdDocument(documentSnapshot.getId());

                    String idDocument = note.getIdDocument();
                    String title = note.getTitle();
                    String description = note.getDescription();
                    int priority = note.getPriority();

                    data += "Id : " + idDocument + "\nTitle : " +
                            title + "\nDescription : " + description +
                            "\nPriority : " +  priority + "\n\n";

                }
                binding.textViewDocument.setText(data);
            }
        });

    }


    public void addNote(View v){
        String title = binding.editTitle.getText().toString();
        String description = binding.editDescription.getText().toString();
        if(binding.editPriority.getText().toString().length() == 0)binding.editPriority.setText("0");
        int priority = Integer.parseInt(binding.editPriority.getText().toString());

        Note note = new Note(title, description, priority);
        notebookRef.add(note);

    }

    public void deleteNote(View v){
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            notebookRef.document(documentSnapshot.getId()).delete();
                        }
                    }
                });
    }


    public void loadNote(View v){
//        if(binding.editTitle.getText().toString().length() == 0) binding.editTitle.setText("");
        Task task1 = notebookRef.whereGreaterThan("priority", 2).get();
        Task task2 = notebookRef.whereLessThan("priority", 2).get();

        // make 'or' query via merge 2 task are contain less and greater than the value
        Task<List<QuerySnapshot>> mergeTask = Tasks.whenAllSuccess(task1,task1);
        mergeTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                String data = "";
                for(QuerySnapshot queryDocumentSnapshots : querySnapshots){
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Note note = documentSnapshot.toObject(Note.class);
                        note.setIdDocument(documentSnapshot.getId());

                        String idDocument = note.getIdDocument();
                        String title = note.getTitle();
                        String description = note.getDescription();
                        int priority = note.getPriority();
                        data += "Id : " + idDocument + "\nTitle : " +
                                title + "\nDescription : " + description +
                                "\nPriority : " +  priority + "\n\n";
                    }

                }
                binding.textViewDocument.setText(data);
            }
        });


    }
}