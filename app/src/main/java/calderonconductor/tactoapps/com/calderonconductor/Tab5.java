package calderonconductor.tactoapps.com.calderonconductor;

/**
 * Created by tacto on 2/10/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Tab5 extends Fragment {
    Button button5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab5, container, false);

        button5 = (Button) rootView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }
}

