package com.example.addandretrievemultipledoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



import com.example.addandretrievemultipledoc.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";


    // connect into firebase fireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference docRef = db.document("Notebook/First Note");

    // for make manually for remove addSnapshotListener, but that's manually
    //    private ListenerRegistration noteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
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

                    data += "Id : " + idDocument + "\nTitle : " +
                            title + "\nDescription : " + description + "\n\n";

                }
                binding.textViewDocument.setText(data);
            }
        });

    }


    public void addNote(View v){
        String title = binding.title.getText().toString();
        String description = binding.description.getText().toString();

        Note note = new Note(title,description);
        notebookRef.add(note);

    }

    public void updateDescription(View v){
        // TODO How to merge, if the Data is empty, the Data will created but only description data added
        // Map<String, Object> noteDescription = new HashMap<>();
        // noteDescription.put(KEY_DESCRIPTION,binding.description.getText().toString());
        // merge the new description with the old title
        // docRef.set(noteDescription, SetOptions.merge());
        // TODO How to update the object only, but if used update
        // the data can't be created data description if the data is empty or null
        docRef.update(KEY_DESCRIPTION,binding.description.getText().toString());
    }
    // delete one field on note data
    public void deleteDescription(View v){

        // for simply
        docRef.update(KEY_DESCRIPTION,FieldValue.delete())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Delete Description Success!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error while deleted description!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }

    // delete entire of note data
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
    public void listNote(View v){
        Intent intent = new Intent(this,ListFirebase.class);
        startActivity(intent);
    }

    public void loadNote(View v){
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setIdDocument(documentSnapshot.getId());

                            String idDocument = note.getIdDocument();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            data += "Id Document : " + idDocument +
                                    "\nTitle : " + title + "\n" +
                                    "Description : " + description + "\n\n";
                        }
                        binding.textViewDocument.setText(data);
                    }
                });

    }


}
