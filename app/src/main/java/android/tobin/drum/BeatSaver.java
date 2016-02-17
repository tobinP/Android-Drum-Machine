package android.tobin.drum;

import java.util.HashSet;
import java.util.Set;

public class BeatSaver extends MainActivity {

    static String[][] beatArray = new String[6][8];
    static Set<String> beatSet = new HashSet<String>();

    public static Set<String> saveBeat(){

        String beatString = makeString("k", kickArray);
        beatSet.add(beatString);

        beatString = makeString("s", snareArray);
        beatSet.add(beatString);

        beatString = makeString("c", closedArray);
        beatSet.add(beatString);

        beatString = makeString("o", openArray);
        beatSet.add(beatString);

        beatString = makeString("t", tomArray);
        beatSet.add(beatString);

        beatString = makeString("sy", symbolArray);
        beatSet.add(beatString);

        return beatSet;
    }

    public static String makeString(String name, String[] array){
        String string = name;

        for (int i = 0; i < array.length; i++)
            string += array[i];

        return string;
    }

}
