package android.tobin.drum;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public class PadFragment extends Fragment {

    int screenWidth;
    int screenHeight;
    int buttonWidth;
    int buttonHeight;

    static Button kick;
    static Button snare;
    static Button open;
    static Button closed;
    static Button tom;
    static Button symbol;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_pad, container, false);

        Point size = new Point();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        buttonWidth = (screenWidth / 3) - 10;
        buttonHeight = (screenHeight / 2) - 10;

        snare = (Button) view.findViewById(R.id.snare_button);
        snare.setWidth(buttonWidth);
        snare.setHeight(buttonHeight);
        snare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    MainActivity.playSnare();
                return false;
            }
        });

        kick = (Button) view.findViewById(R.id.kick_button);
        kick.setWidth(buttonWidth);
        kick.setHeight(buttonHeight);
        kick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.playKick();
                }
                return false;
            }
        });

        closed = (Button) view.findViewById(R.id.closed_button);
        closed.setWidth(buttonWidth);
        closed.setHeight(buttonHeight);
        closed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    MainActivity.playClosedHat();
                return false;
            }
        });

        open = (Button) view.findViewById(R.id.open_button);
        open.setWidth(buttonWidth);
        open.setHeight(buttonHeight);
        open.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    MainActivity.playOpenHat();
                return false;
            }
        });

        tom = (Button) view.findViewById(R.id.tom_button);
        tom.setWidth(buttonWidth);
        tom.setHeight(buttonHeight);
        tom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    MainActivity.playTom();
                return false;
            }
        });

        symbol = (Button) view.findViewById(R.id.symbol_button);
        symbol.setWidth(buttonWidth);
        symbol.setHeight(buttonHeight);
        symbol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    MainActivity.playSymbol();
                return false;
            }
        });

        return view;
    }
}
