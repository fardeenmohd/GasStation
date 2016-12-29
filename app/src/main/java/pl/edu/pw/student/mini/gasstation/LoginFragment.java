package pl.edu.pw.student.mini.gasstation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.R.attr.password;


/**
Login fragment where user can input username and password
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText usernameEditText = null;
    private EditText passwordEditText = null;
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
        android.support.v7.widget.AppCompatImageView gasImage = (android.support.v7.widget.AppCompatImageView)v.findViewById(R.id.login_gas_image);
        gasImage.setImageResource(R.mipmap.gaspump);
        Button loginButton = (Button) v.findViewById(R.id.login_button);
        Button registerButton = (Button) v.findViewById(R.id.register_button);
        usernameEditText = (EditText)v.findViewById(R.id.username_edit_text);
        passwordEditText = (EditText)v.findViewById(R.id.password_edit_text);
        final Context ctx = this.getActivity();
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String username = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
                Log.d("LoginFragment", "Username:  " + username);
                Log.d("LoginFragment", "password: " + pass);
                if(TextUtils.isEmpty(username)){
                    if(TextUtils.isEmpty(username)){
                        Toast.makeText(ctx , "Please enter username/email", Toast.LENGTH_SHORT).show();
                    }
                }

                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(ctx , "Please enter password", Toast.LENGTH_SHORT).show();
                }
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
