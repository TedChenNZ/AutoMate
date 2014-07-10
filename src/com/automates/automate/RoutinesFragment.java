package com.automates.automate;

import com.automates.automate.routines.settings.*;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RoutinesFragment extends Fragment {
	private TextView testText;

	public RoutinesFragment(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routines, container, false);
        
        
        
    	final Button testButton = (Button) rootView.findViewById(R.id.testButton);
    	testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                testButton(v);
            }
        });
        
        
        
        return rootView;
        
        
    }
	
	public void testButton(View v) {
		Context context = this.getActivity();
//		SoundProfiles.setSoundProfile(this.getActivity(), SoundProfiles.NORMAL_NO_VIBRATE);
//		int m = SoundProfiles.getMode(this.getActivity());
//		testText.setText(Integer.toString(m));
		Data.setDataEnabled(context, true);
		
		testText.setText(Boolean.toString(Data.getDataEnabled(context)));
	}
	

}
