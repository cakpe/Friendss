package com.akponyai.friends;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    EditText usernameText;
    EditText passwordText;
    Button loginButton;

    SignInButton googleButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;

    private static int RC_SIGN_IN = 9001;
    private static final String TAG = "GOOGLEACTIVITY";



    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.signupButton);

        googleButton = findViewById(R.id.sign_in_button);


        //configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



        //Disable login button for now

        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });*/

        findViewById(R.id.textViewsignup).setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d("Intent Data", "data -- " + data.getData());
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d("Login", "hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        finish();
    }












    public void onClick(View view){
        switch(view.getId()){
            case R.id.textViewsignup:
                startActivity(new Intent(this,SignUpActivity.class));
                break;
        }
    }

    public void addUser() {
        String name = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(password)) {
            String id = databaseUsers.push().getKey();

            Users user = new Users(name, password);

            databaseUsers.child(id).setValue(user);

            Toast.makeText(this, "User profile created", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Enter username or password", Toast.LENGTH_LONG).show();
        }

    }

}
