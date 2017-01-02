package pl.edu.pw.student.mini.gasstation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.util.Log;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputDataFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private boolean enableAutoFocus = true;
    private boolean enableFlash = false;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "InputDataFragment";
    private TextView resultTv;
    private TextView statusTv;
    private DatabaseReference databaseReference;

    public InputDataFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputDataFragment newInstance() {
        InputDataFragment fragment = new InputDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context ctx = this.getActivity();
        final EditText edittext = new EditText(ctx);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        View v = inflater.inflate(R.layout.fragment_input_data, container, false);
        resultTv = (TextView)v.findViewById(R.id.cameraResultTextView);
        statusTv = (TextView)v.findViewById(R.id.resultStatusTextView);
        Button cameraInputButton = (Button) v.findViewById(R.id.camera_button);
        cameraInputButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // launch Ocr capture activity.
                Intent intent = new Intent(v.getContext(), OcrCaptureActivity.class);

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        Button manualInputButton = (Button)v.findViewById(R.id.manual_input_button);
        manualInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

                alert.setMessage("Enter price to database");
                alert.setTitle("Manual input");

                alert.setView(edittext);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String price = edittext.getText().toString();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                });
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
        }
        /* for now we ignore this listener
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusTv.setText(R.string.ocr_success);
                    resultTv.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusTv.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusTv.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
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
