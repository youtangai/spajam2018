package com.pig.three.spajam2018;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseService {
    private FirebaseDatabase database;

    public FirebaseService() {
        //init database instance
        database = FirebaseDatabase.getInstance();
    }

    public DatabaseReference GetReference(String key) {
        DatabaseReference myRef = database.getReference(key);
        return myRef;
    }
}
