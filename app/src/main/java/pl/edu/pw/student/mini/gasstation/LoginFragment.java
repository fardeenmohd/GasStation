package pl.edu.pw.student.mini.gasstation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Login fragment where user can input username and password
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText usernameEditText = null;
    private EditText passwordEditText = null;
    private EditText fuelUsageEditText = null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView loginInfo = null;
    private ProgressDialog progressDialog = null;
    private FirebaseAuth firebaseAuth = null;
    private String fuelUsage = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container,
                false);
        Log.d("LoginFragment", "onCreateView()");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this.getActivity());
        android.support.v7.widget.AppCompatImageView gasImage = (android.support.v7.widget.AppCompatImageView) v.findViewById(R.id.login_gas_image);

        final Button loginButton = (Button) v.findViewById(R.id.login_button);
        final Button registerButton = (Button) v.findViewById(R.id.register_button);
        final Button logoutButton = (Button) v.findViewById(R.id.logout_button);
        final Button sumbitFuelUsageButton = (Button) v.findViewById(R.id.fuel_usage_submit);
        loginInfo = (TextView) v.findViewById(R.id.login_info);
        usernameEditText = (EditText) v.findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) v.findViewById(R.id.password_edit_text);
        fuelUsageEditText = (EditText) v.findViewById(R.id.fuel_usage_tv);
        fuelUsageEditText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final Context ctx = this.getActivity();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    loginButton.setVisibility(View.INVISIBLE);
                    registerButton.setVisibility(View.INVISIBLE);
                    usernameEditText.setVisibility(View.INVISIBLE);
                    passwordEditText.setVisibility(View.INVISIBLE);
                    fuelUsageEditText.setVisibility(View.VISIBLE);
                    sumbitFuelUsageButton.setVisibility(View.VISIBLE);
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Toast.makeText(ctx, "User logged in", Toast.LENGTH_SHORT).show();
                    loginInfo.setText("Hello, \n" + user.getEmail());

                } else {
                    // User is signed out
                    loginButton.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                    usernameEditText.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.INVISIBLE);
                    fuelUsageEditText.setVisibility(View.INVISIBLE);
                    sumbitFuelUsageButton.setVisibility(View.INVISIBLE);
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    //Toast.makeText(getActivity(), "User is logged out", Toast.LENGTH_SHORT).show();
                    loginInfo.setText("Please Login or Register");
                }
                // ...
            }
        };

        firebaseAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(user.getUid()).child("fuelUsage").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fuelUsage = (String) dataSnapshot.getValue();
                    fuelUsageEditText.setText(fuelUsage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        sumbitFuelUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    String fuelUsageString = fuelUsageEditText.getText().toString();
                    if(isDouble(fuelUsageString)) {
                        databaseReference.child("users").child(user.getUid()).child("fuelUsage").setValue(fuelUsageString);
                        Toast.makeText(ctx, "Successfully submitted your fuel economy of: " + fuelUsageString, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ctx, "Please enter a valid value for fuel usage", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(ctx, "Please enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(ctx, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Please wait, logging in now...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //user successfully registered and logged in
                                    Toast.makeText(ctx, "Login successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(ctx, "Login failed! Please try again", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }


                            }
                        });

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
                Log.d("LoginFragment", "Email:  " + email);
                Log.d("LoginFragment", "password: " + pass);
                if (TextUtils.isEmpty(email)) {
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(ctx, "Please enter email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(ctx, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Please wait, registering user now...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //user successfully registered and logged in
                                    Toast.makeText(ctx, "Registration successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(ctx, "Registration failed! Please try again", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }


                            }
                        });

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
            }
        });


        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
