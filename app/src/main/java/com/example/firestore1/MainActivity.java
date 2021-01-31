package com.example.firestore1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.firestore1.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";


    // connect into firebase fireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // not only can make DocumentReference but also we can make CollectionReference
    private DocumentReference noteRef = db.document("Notebook/First Note");
    
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
        // and then make save that value into noteListener = noteRef.addSnapshotListener(new EventListener<DocumentSnapshot>(){ blabla)};
        // addition word 'this' into listener for makes that automatic detach from listener
        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,error.toString());
                    return;
                }

                String title = "",description = "";

                if(documentSnapshot.exists()){
                    Note note = documentSnapshot.toObject(Note.class);
                    title = note.getTitle();
                    description = note.getDescription();
                    if (description == null) description = "";
                }
                binding.textViewDocument.setText("The title --> " + title + "\n The description --> " + description);
            }
        });
    }
    // make detach that's Listener for make not "PROSES BERCABANG KETIKA APPS DI JALANKAN"
    // then take off into onStop function like
        //    @Override
        //    protected void onStop() {
        //        super.onStop();
        //        noteListener.remove();
        //    }

    public void saveNote(View v){
        String title = binding.editTextTitle.getText().toString();
        String description = binding.editTextDescription.getText().toString();

        // used map object
            // Map<String, String> note = new HashMap<>();
            // note.put(KEY_TITLE,title);
            // note.put(KEY_DESCRIPTION,description);
        // used class object
            Note note = new Note(title,description);
        // that's can make more shortly, db.document("Notebook/First Note").set(note)
        // can make like this --> db.collection("Notebook").document("First Note").set(note)

        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Fail Saved", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }

    public void updateDescription(View v){
        // TODO How to merge, if the Data is empty, the Data will created but only description data added
            // Map<String, Object> noteDescription = new HashMap<>();
            // noteDescription.put(KEY_DESCRIPTION,binding.editTextDescription.getText().toString());
            // merge the new description with the old title
            // noteRef.set(noteDescription, SetOptions.merge());
        // TODO How to update the object only, but if used update
            // the data can't be created data description if the data is empty or null
            noteRef.update(KEY_DESCRIPTION,binding.editTextDescription.getText().toString());
    }
    // delete one field on note data
    public void deleteDescription(View v){

        //Map<String, Object> note = new HashMap<>();
        //note.put(KEY_DESCRIPTION, FieldValue.delete());
        //noteRef.update(note);

        // for simply
        noteRef.update(KEY_DESCRIPTION,FieldValue.delete())
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
        noteRef.delete();
    }
    public void loadNote(View v){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            // get value from document directly
                                 //String title = documentSnapshot.getString(KEY_TITLE);
                                //String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            // mengubah  documentSnapshot menjadi object pada class note
                            Note note = documentSnapshot.toObject(Note.class);

                            // todo used custom java object
                                String title = note.getTitle();
                                String description = note.getDescription();

                            // can initial into map object like this, Map<String, object> note = documentSnapshot.getData());
                            binding.textViewDocument.setText("The title --> " + title + "\n The description --> " + description);
                        }else{
                            Toast.makeText(MainActivity.this, "Document is does not exist!", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }


}
