package server;

import java.util.prefs.Preferences;

public class SessionPref {
    private Preferences prefs;

    public void setPreference() {
        // This will define a node in which the preferences can be stored
        prefs = Preferences.userRoot().node(this.getClass().getName());
        String userName = "Test1";
        String PassWord = "Test2";


        // First we will get the values



        // now set the values
        //  prefs.putBoolean(ID1, false);
         prefs.put(userName , "Mark");
         prefs.put(PassWord, "Helloooo Europa");

        System.out.println(prefs.get(userName, "MusterMann"));

        System.out.println(prefs.get(PassWord, "1234"));




        // Delete the preference settings for the first value
        // prefs.remove(ID1);

    }

    public static void main(String[] args) {
        SessionPref test = new SessionPref();
        test.setPreference();


    }


}
