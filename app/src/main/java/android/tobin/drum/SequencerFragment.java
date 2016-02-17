package android.tobin.drum;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;

public class SequencerFragment extends Fragment {

    GridLayout grid;
    int screenWidth;
    int screenHeight;
    int buttonWidth;
    int buttonHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sequencer, container, false);

        Point size = new Point();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        buttonWidth = screenWidth / 9;
        buttonHeight = (screenHeight / 6) ;

        Log.d("sequence", "x: " + screenWidth);
        Log.d("sequence", "y: " + screenHeight);

        grid = (GridLayout) view.findViewById(R.id.grid);

        createButtons(MainActivity.symbolArray, 0, "symbol");
        createButtons(MainActivity.tomArray, 1, "tom");
        createButtons(MainActivity.openArray, 2, "open");
        createButtons(MainActivity.closedArray, 3, "closed");
        createButtons(MainActivity.snareArray, 4, "snare");
        createButtons(MainActivity.kickArray, 5, "kick");

        return view;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        grid = (GridLayout) getActivity().findViewById(R.id.grid);
//    }

    public void createButtons(String[] array, int row, String tag){

        final String[] finalArray = array;
            for (int i = 1; i < 9; i++) {
                TextView t = new TextView(getActivity());
                t.setTag(tag + i);
                t.setWidth(buttonWidth);
                t.setHeight(buttonHeight);

                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tagString = v.getTag().toString();
                        String index = tagString.substring(tagString.length() - 1);
                        Log.d("sequence", index);
                        int tag = Integer.parseInt(index);
                        if (v.getBackground().getConstantState().equals(ContextCompat.getDrawable(getContext(), R.drawable.seq_tile_off).getConstantState())){
                            v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.seq_tile_on));
                            finalArray[tag - 1] = "1";
                        } else {
                            v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.seq_tile_off));
                            finalArray[tag - 1] = "0";
                        }
                    }
                });

                if (finalArray[i - 1].equals("0"))
                    t.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.seq_tile_off));
                else
                    t.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.seq_tile_on));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(i);
                t.setLayoutParams(params);
                grid.addView(t);
            }
    }
}