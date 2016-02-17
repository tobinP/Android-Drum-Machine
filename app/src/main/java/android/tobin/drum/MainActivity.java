package android.tobin.drum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import im.delight.android.audio.SoundManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static SoundManager mSoundManager;
    static Timer timer = new Timer("MetronomeTimer", true);

    static int i = 0;
//    static int[] kickArray = {  1, 0, 1, 0, 1, 0, 1, 0};
//    static int[] symbolArray = {0, 0, 0, 0, 0, 0, 0, 0};
//    static int[] snareArray = { 0, 0, 1, 0, 0, 0, 1, 0};
//    static int[] closedArray = {0, 1, 0, 1, 0, 1, 0, 1};
//    static int[] openArray = {  0, 0, 0, 0, 0, 1, 0, 0};
//    static int[] tomArray = {   0, 0, 0, 0, 0, 0, 0, 0};

    static String[] kickArray = {"1", "0", "1", "0", "1", "0", "1", "0"};
    static String[] snareArray = {"1", "0", "1", "0", "1", "0", "1", "0"};
    static String[] closedArray = {"1", "0", "1", "0", "1", "0", "1", "0"};
    static String[] openArray = {"1", "0", "1", "0", "1", "0", "1", "0"};
    static String[] tomArray = {"1", "0", "1", "0", "1", "0", "1", "0"};
    static String[] symbolArray = {"1", "0", "1", "0", "1", "0", "1", "0"};

    FragmentManager fragManager = getSupportFragmentManager();

    PadFragment padFrag;
    SequencerFragment seqFrag;
    boolean paused = false;
    boolean loopStarted = false;

    public static final String PREF = "pref";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        padFrag = new PadFragment();
        seqFrag = new SequencerFragment();
        fragManager.beginTransaction().replace(R.id.pad_frag_container, padFrag, "pad fragment").commit();
        fragManager.beginTransaction().replace(R.id.sequencer_frag_container, seqFrag, "sequencer fragment").commit();
        fragManager.beginTransaction().hide(seqFrag).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int maxSimultaneousStreams = 7;
        mSoundManager = new SoundManager(this, maxSimultaneousStreams);
        mSoundManager.start();
        mSoundManager.load(R.raw.snare);
        mSoundManager.load(R.raw.dirtkick);
        mSoundManager.load(R.raw.openhat);
        mSoundManager.load(R.raw.closedhat);
        mSoundManager.load(R.raw.floortom);
        mSoundManager.load(R.raw.symbol);
    }

    public  void startLoop(){

        TimerTask sequence = new TimerTask(){
            @Override
            public void run(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!paused) {

                                int previousTile = i;
                                TextView previousKickTile;
                                TextView previousSnareTile;
                                TextView previousClosedTile;
                                TextView previousOpenTile;
                                TextView previousTomTile;
                                TextView previousSymbolTile;

                                String prevKickTag;
                                String prevSnareTag;
                                String prevClosedTag;
                                String prevOpenTag;
                                String prevTomTag;
                                String prevSymbolTag;

                                if (previousTile > 0){
                                    prevKickTag = "kick" + previousTile;
                                    prevSnareTag = "snare" + previousTile;
                                    prevClosedTag = "closed" + previousTile;
                                    prevOpenTag = "open" + previousTile;
                                    prevTomTag = "tom" + previousTile;
                                    prevSymbolTag = "symbol" + previousTile;

                                    previousKickTile = (TextView) seqFrag.grid.findViewWithTag(prevKickTag);
                                    previousSnareTile = (TextView) seqFrag.grid.findViewWithTag(prevSnareTag);
                                    previousClosedTile = (TextView) seqFrag.grid.findViewWithTag(prevClosedTag);
                                    previousOpenTile = (TextView) seqFrag.grid.findViewWithTag(prevOpenTag);
                                    previousTomTile = (TextView) seqFrag.grid.findViewWithTag(prevTomTag);
                                    previousSymbolTile = (TextView) seqFrag.grid.findViewWithTag(prevSymbolTag);

                                    if (kickArray[i - 1].equals("1"))
                                        previousKickTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (snareArray[i - 1].equals("1"))
                                        previousSnareTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (closedArray[i - 1].equals("1"))
                                        previousClosedTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (openArray[i - 1].equals("1"))
                                        previousOpenTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (tomArray[i - 1].equals("1"))
                                        previousTomTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (symbolArray[i - 1].equals("1"))
                                        previousSymbolTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                }
                                else if (previousTile == 0){
                                    previousTile = 8;
                                    prevKickTag = "kick" + previousTile;
                                    prevSnareTag = "snare" + previousTile;
                                    prevClosedTag = "closed" + previousTile;
                                    prevOpenTag = "open" + previousTile;
                                    prevTomTag = "tom" + previousTile;
                                    prevSymbolTag = "symbol" + previousTile;

                                    previousKickTile = (TextView) seqFrag.grid.findViewWithTag(prevKickTag);
                                    previousSnareTile = (TextView) seqFrag.grid.findViewWithTag(prevSnareTag);
                                    previousClosedTile = (TextView) seqFrag.grid.findViewWithTag(prevClosedTag);
                                    previousOpenTile = (TextView) seqFrag.grid.findViewWithTag(prevOpenTag);
                                    previousTomTile = (TextView) seqFrag.grid.findViewWithTag(prevTomTag);
                                    previousSymbolTile = (TextView) seqFrag.grid.findViewWithTag(prevSymbolTag);

                                    if (kickArray[7].equals("1"))
                                        previousKickTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (snareArray[7].equals("1"))
                                        previousSnareTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (closedArray[7].equals("1"))
                                        previousClosedTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (openArray[7].equals("1"))
                                        previousOpenTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (tomArray[7].equals("1"))
                                        previousTomTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                    if (symbolArray[7].equals("1"))
                                        previousSymbolTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_on));

                                }



                                int currentColumn = i + 1;
                                String kickTag = "kick" + currentColumn;
                                String snareTag = "snare" + currentColumn;
                                String closedTag = "closed" + currentColumn;
                                String openTag = "open" + currentColumn;
                                String tomTag = "tom" + currentColumn;
                                String symbolTag = "symbol" + currentColumn;

                                TextView kickTile = (TextView) seqFrag.grid.findViewWithTag(kickTag);
                                TextView snareTile = (TextView) seqFrag.grid.findViewWithTag(snareTag);
                                TextView closedTile = (TextView) seqFrag.grid.findViewWithTag(closedTag);
                                TextView openTile = (TextView) seqFrag.grid.findViewWithTag(openTag);
                                TextView tomTile = (TextView) seqFrag.grid.findViewWithTag(tomTag);
                                TextView symbolTile = (TextView) seqFrag.grid.findViewWithTag(symbolTag);

                                if (kickArray[i].equals("1")) {
                                    mSoundManager.play(R.raw.dirtkick);
                                    PadFragment.kick.setPressed(true);
                                    kickTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_highlight));
                                } else
                                    PadFragment.kick.setPressed(false);

                                if (snareArray[i].equals("1")) {
                                    mSoundManager.play(R.raw.snare);
                                    PadFragment.snare.setPressed(true);
                                    snareTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_highlight));
                                } else
                                    PadFragment.snare.setPressed(false);

                                if (openArray[i].equals("1")) {
                                    mSoundManager.play(R.raw.openhat);
                                    PadFragment.open.setPressed(true);
                                    openTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_highlight));
                                } else
                                    PadFragment.open.setPressed(false);

                                if (closedArray[i].equals("1")) {
                                    mSoundManager.play(R.raw.closedhat);
                                    PadFragment.closed.setPressed(true);
                                    closedTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_highlight));
                                } else
                                    PadFragment.closed.setPressed(false);

                                if (tomArray[i].equals("1")) {
                                    mSoundManager.play(R.raw.floortom);
                                    PadFragment.tom.setPressed(true);
                                    tomTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_highlight));
                                } else
                                    PadFragment.tom.setPressed(false);

                                if (symbolArray[i].equals("1")) {
                                    mSoundManager.play(R.raw.symbol);
                                    PadFragment.symbol.setPressed(true);
                                    symbolTile.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.seq_tile_highlight));
                                } else
                                    PadFragment.symbol.setPressed(false);

                                i++;
                                if (i > 7)
                                    i = 0;
                            } else {
                                PadFragment.kick.setPressed(false);
                                PadFragment.snare.setPressed(false);
                                PadFragment.closed.setPressed(false);
                                PadFragment.open.setPressed(false);
                                PadFragment.tom.setPressed(false);
                                PadFragment.symbol.setPressed(false);
                            }
                        }
                    });
            }
        };
        timer.scheduleAtFixedRate(sequence, 250, 250);
    }

    public static void playKick() {
        if (mSoundManager != null) {
            mSoundManager.play(R.raw.dirtkick);
        }
    }

    public static void playSnare() {
        if (mSoundManager != null)
            mSoundManager.play(R.raw.snare);
    }

    public static void playOpenHat(){
        if (mSoundManager != null){
            mSoundManager.play(R.raw.openhat);
        }
    }

    public static void playClosedHat(){
        if (mSoundManager != null){
            mSoundManager.play(R.raw.closedhat);
        }
    }

    public static void playTom(){
        if (mSoundManager != null){
            mSoundManager.play(R.raw.floortom);
        }
    }

    public static void playSymbol(){
        if (mSoundManager != null){
            mSoundManager.play(R.raw.symbol);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pad_seq_item) {
            if (item.getTitle().equals("Sequencer View")){
                fragManager.beginTransaction().hide(padFrag).commit();
                fragManager.beginTransaction().show(seqFrag).commit();
                item.setTitle("Pad View");
            } else {
                fragManager.beginTransaction().show(padFrag).commit();
                fragManager.beginTransaction().hide(seqFrag).commit();
                item.setTitle("Sequencer View");
            }
        } else if (id == R.id.play_pause_item) {
            if (item.getTitle().equals("Play")){
                Log.d("main", "pressed play");
                if (!loopStarted) {
                    startLoop();
                    loopStarted = true;
                }
                paused = false;
                item.setTitle("Pause");
            } else {
                Log.d("main", "pressed pause");
//                timer.cancel();
                paused = true;
                item.setTitle("Play");
            }
        } else if (id == R.id.save_item){
//            BeatSaver bs = new BeatSaver();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            final EditText fileName = new EditText(this);

            fileName.setHint("Enter File Name");
            alertDialogBuilder.setView(fileName);

            alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String beatName = fileName.toString();

                    editor = sharedPref.edit();
                    editor.clear();
                    Set<String> beat = BeatSaver.saveBeat();
                    Toast.makeText(MainActivity.this, beat.toString(), Toast.LENGTH_SHORT).show();

//                    try {
                        editor.putStringSet("beatName", beat);
                        editor.putString("test", "the test");
//                    } catch (Exception e){}

                    editor.commit();
                }
            });

            alertDialogBuilder.show();


        } else if (id == R.id.load_item){
            Map<String, ?> beatMap = sharedPref.getAll();

            for (Object o : beatMap.entrySet()){
                Log.d("main", o.toString());
            }

            Set<String> beat = (Set<String>) beatMap.get("beatName");

            String test = sharedPref.getString("test", "null");
            Toast.makeText(MainActivity.this, beat.toString(), Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}