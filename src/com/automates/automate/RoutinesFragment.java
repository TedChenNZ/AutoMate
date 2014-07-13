package com.automates.automate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RoutinesFragment extends Fragment implements PropertyChangeListener {
//	private TextView testText;

	public RoutinesFragment(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routines, container, false);
        
        
        
//    	final Button testButton = (Button) rootView.findViewById(R.id.testButton);
//    	testButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                testButton(v);
//            }
//        });
        
        PhoneState.getInstance().addChangeListener(this);
        
        return rootView;
        
        
    }
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Log.d("RoutinesFragment", "PropertyChangeEvent recieved");
		
	}
	
//	public void testButton(View v) {
//		Context context = this.getActivity();
////		SoundProfiles.setSoundProfile(this.getActivity(), SoundProfiles.NORMAL_NO_VIBRATE);
////		int m = SoundProfiles.getMode(this.getActivity());
////		testText.setText(Integer.toString(m));
//		Data.setDataEnabled(context, true);
//		
//		testText.setText(Boolean.toString(Data.getDataEnabled(context)));
//	}
	

}
