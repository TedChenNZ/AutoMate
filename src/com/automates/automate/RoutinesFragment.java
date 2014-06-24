package com.automates.automate;

import com.automates.automate.routines.actions.SoundProfiles;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class RoutinesFragment extends Fragment {
	
	public RoutinesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routines, container, false);
        
        
    	final Button loginButton = (Button) rootView.findViewById(R.id.testButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                testButton(v);
            }
        });
        return rootView;
    }
	
	public void testButton(View v) {
		SoundProfiles.setSilent(this.getActivity());
	}
	

}
