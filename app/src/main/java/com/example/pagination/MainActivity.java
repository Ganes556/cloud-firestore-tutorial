package com.example.pagination;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pagination.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentSnapshot lastResult;

    private int halNumber = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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


    public void loadNotes(View v){
        notebookRef.document("PWxfHdsPhzLi5hadTz3D")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                notebookRef.orderBy("priority")
                        .endAt(documentSnapshot)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data = "";
                                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                                    Note note = queryDocumentSnapshot.toObject(Note.class);
                                    note.setIdDocument(queryDocumentSnapshot.getId());

                                    String id = note.getIdDocument();
                                    String title = note.getTitle();
                                    String description = note.getDescription();
                                    int priority = note.getPriority();

                                    data += "ID : " + id + "\nTitle : " + title + "\nDescription : "
                                            + description + "\nPriority : "
                                            + priority + "\n\n";

                                }
                                binding.textViewDocument.setText(data);
                            }
                        });
            }
        });

    }

    public void pagination(View v){
        Query query;

        if(lastResult == null || halNumber == 0){
            halNumber = 1;
            query = notebookRef.orderBy("priority")
                    .limit(2);
        }else {

            query = notebookRef.orderBy("priority")
                    .limit(2)
                    .startAfter(lastResult);
        }

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            Note note = queryDocumentSnapshot.toObject(Note.class);
                            note.setIdDocument(queryDocumentSnapshot.getId());

                            String id = note.getIdDocument();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            int priority = note.getPriority();

                            data += "ID : " + id + "\nTitle : " + title + "\nDescription : "
                                    + description + "\nPriority : "
                                    + priority + "\n\n";

                        }


                        if(queryDocumentSnapshots.size() > 0){
                            binding.textViewDocument.setText(data);
                            binding.page.setText("Hal " + halNumber);
                            lastResult = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size()-1);
                            halNumber++;
                            if(queryDocumentSnapshots.size() == 1) halNumber = 0;
                        }
                    }
                });
    }
}