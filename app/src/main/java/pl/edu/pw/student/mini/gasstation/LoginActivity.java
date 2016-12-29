package pl.edu.pw.student.mini.gasstation;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we only create the fragment once, if state is restored then do nothing in order to retain state
        android.support.v4.app.Fragment existingFragment = getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (existingFragment == null || !existingFragment.getClass().equals(LoginActivity.class))
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, LoginFragment.newInstance())
                    .commit();
            Log.d("debug", "in onCreate() LoginActivity.java- Creating a login fragment");

        }

        Log.d("debug", "in onCreate() LoginActivity.java- Login fragment already created");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
