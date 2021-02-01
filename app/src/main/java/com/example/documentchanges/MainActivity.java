package com.example.documentchanges;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.documentchanges.databinding.ActivityMainBinding;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentSnapshot lastResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // usually used Log.d() for easier to detect the error
                    return;
                }


                // only showed the data has changed, so if data change is 1 then the data in dc
                // only got 1 document
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    // QueryDocumentSnapshot have guarantee data and can't have null data
                    // DocumentSnapshot not have guarantee data being return and can have null data
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    Note note = documentSnapshot.toObject(Note.class);

                    // if want showed the description or something else
                    // data += note.getDescription();

                    String idDocument = documentSnapshot.getId();
                    // oldIndex is index position before there changed
                    int oldIndex = dc.getOldIndex();
                    // newIndex is index position after there changed
                    int newIndex = dc.getNewIndex();
                    switch (dc.getType()) {
                        case ADDED:
                            binding.textViewDocument.append("\nAdded : " + idDocument + "\noldIndex : " + oldIndex
                                    + "\nnewIndex : " + newIndex);
                            break;
                        case REMOVED:
                            binding.textViewDocument.append("\nRemoved : " + idDocument + "\noldIndex : " + oldIndex
                                    + "\nnewIndex : " + newIndex);
                            break;
                        case MODIFIED:
                            binding.textViewDocument.append("\nModified : " + idDocument + "\noldIndex : " + oldIndex
                                    + "\nnewIndex : " + newIndex);
                            break;
                    }

                }

            }
        });
    }

    public void addNote(View v) {
        String title = binding.editTitle.getText().toString();
        String description = binding.editDescription.getText().toString();
        if (binding.editPriority.getText().toString().length() == 0)
            binding.editPriority.setText("0");
        int priority = Integer.parseInt(binding.editPriority.getText().toString());

        Note note = new Note(title, description, priority);
        notebookRef.add(note);

    }

    public void deleteNote(View v) {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            notebookRef.document(documentSnapshot.getId()).delete();
                        }
                    }
                });
    }


    public void loadNotes(View v) {


        notebookRef.orderBy("priority")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
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


}