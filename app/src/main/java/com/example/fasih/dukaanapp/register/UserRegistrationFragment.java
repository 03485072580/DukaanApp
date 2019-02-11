package com.example.fasih.dukaanapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.utils.Constants;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.StringManipulations;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fasih on 01/01/19.
 */

public class UserRegistrationFragment extends Fragment {

    private static final int RC_SIGN_IN_GOOGLE = 25;
    //facebook Stuff
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private EditText firstName, lastName, userName, email, password;
    private LinearLayout signUp;

    private String scope = Constants.scope = "user";
    private ProgressBar registerProgress;
    private CircleImageView middleLoginFacebook, leftLoginGoogle;
    private GoogleSignInClient googleSignInClient;
    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;
    private CallbackManager callbackManager;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_user, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        return view;
    }

    public void setupFragmentWidgets(View view) {
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        userName = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        signUp = view.findViewById(R.id.signUp);
        registerProgress = view.findViewById(R.id.registerProgress);
        middleLoginFacebook = view.findViewById(R.id.middleLogin);
        leftLoginGoogle = view.findViewById(R.id.leftLogin);

        //Listeners
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(lastName.getText().toString()) &&
                        !TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) &&
                        !TextUtils.isEmpty(email.getText().toString()))
                    setupDatabase();
                else
                    Toast.makeText(getActivity(), getString(R.string.warning_required_fields), Toast.LENGTH_SHORT).show();

            }
        });

        middleLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(UserRegistrationFragment.this
                        , Arrays.asList(EMAIL, PUBLIC_PROFILE));
            }
        });
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseMethods.setupFacebookLoginWithAccessToken(loginResult.getAccessToken(), scope);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        leftLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
            }
        });
    }


    /**
     * Firebase Setup Initialization
     */

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.activity_register));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                }

            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void checkIfUserNameAlreadyExists(final String username) {

        registerProgress.setVisibility(View.VISIBLE);

        Query query = myRef
                .child(getString(R.string.db_users_node))
                .orderByChild(getString(R.string.db_field_user_name))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerProgress.setVisibility(View.GONE);

                if (!dataSnapshot.exists()) {
                    firebaseMethods.updateProgress(registerProgress);
                    firebaseMethods.addNewUser(firstName.getText().toString(), lastName.getText().toString()
                            , username, email.getText().toString(), password.getText().toString()
                            , "", "", scope, "", "", false);

                }
                //it will execute only if a match found
                if (dataSnapshot.exists()) {
                    //Found a match
                    Toast.makeText(getActivity(), getString(R.string.conflict_username), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                registerProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupDatabase() {
        String username = userName.getText().toString();
        checkIfUserNameAlreadyExists(StringManipulations.toLowerCaseUsername(username));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseMethods.setupGoogleLoginWithAccount(account, scope);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG1234", "Google sign in failed", e);
                // ...
            }

        }
    }
}

/**
 * Todo Work: There may produce bug when user sign up with google etc
 * Todo Work: integrations and add payment Data and again sign up with Simple Login System. Fix Needs to be done Later
 * <p>
 * This is the very important code that is used to generate the key hash
 * <p>
 * private void generateKeyHashFB() {
 * try {
 * PackageInfo info = getActivity().getPackageManager().getPackageInfo(
 * "com.example.fasih.dukaanapp",
 * PackageManager.GET_SIGNATURES);
 * for (Signature signature : info.signatures) {
 * MessageDigest md = MessageDigest.getInstance("SHA");
 * md.update(signature.toByteArray());
 * Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
 * }
 * } catch (PackageManager.NameNotFoundException e) {
 * <p>
 * } catch (NoSuchAlgorithmException e) {
 * <p>
 * }
 * }
 ***/

/**
 * This is the very important code that is used to generate the key hash
 * <p>
 * private void generateKeyHashFB() {
 * try {
 * PackageInfo info = getActivity().getPackageManager().getPackageInfo(
 * "com.example.fasih.dukaanapp",
 * PackageManager.GET_SIGNATURES);
 * for (Signature signature : info.signatures) {
 * MessageDigest md = MessageDigest.getInstance("SHA");
 * md.update(signature.toByteArray());
 * Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
 * }
 * } catch (PackageManager.NameNotFoundException e) {
 * <p>
 * } catch (NoSuchAlgorithmException e) {
 * <p>
 * }
 * }
 ***/