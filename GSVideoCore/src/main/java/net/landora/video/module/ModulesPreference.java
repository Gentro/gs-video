/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.video.module;

import java.util.List;
import net.landora.video.preferences.LocalPreferenceObject;
import net.landora.video.preferences.PreferenceObject;

/**
 *
 * @author bdickie
 */
public enum ModulesPreference {
    DisabledModules(false, new String[0]);
    
    private ModulesPreference(boolean global, String defaultValue) {
        if (global)
            pref = new PreferenceObject(getClass(), name(), defaultValue);
        else
            pref = new LocalPreferenceObject(getClass(), name(), defaultValue);
    }
    
    private ModulesPreference(boolean global, int defaultValue) {
        if (global)
            pref = new PreferenceObject(getClass(), name(), defaultValue);
        else
            pref = new LocalPreferenceObject(getClass(), name(), defaultValue);
    }
    
    private ModulesPreference(boolean global, boolean defaultValue) {
        if (global)
            pref = new PreferenceObject(getClass(), name(), defaultValue);
        else
            pref = new LocalPreferenceObject(getClass(), name(), defaultValue);
    }
    
    private ModulesPreference(boolean global, String[] defaultValue) {
        if (global)
            pref = new PreferenceObject(getClass(), name(), defaultValue);
        else
            pref = new LocalPreferenceObject(getClass(), name(), defaultValue);
    }
    
    private ModulesPreference(boolean global, long defaultValue) {
        if (global)
            pref = new PreferenceObject(getClass(), name(), defaultValue);
        else
            pref = new LocalPreferenceObject(getClass(), name(), defaultValue);
    }
    
    private PreferenceObject pref;

    public void setStringList(List<String> value) {
        pref.setStringList(value);
    }

    public void setString(String value) {
        pref.setString(value);
    }

    public void setInt(int value) {
        pref.setInt(value);
    }

    public void setBoolean(boolean value) {
        pref.setBoolean(value);
    }

    public List<String> getStringList() {
        return pref.getStringList();
    }

    public String getString() {
        return pref.getString();
    }

    public String getPrefName() {
        return pref.getPrefName();
    }

    public int getInt() {
        return pref.getInt();
    }

    public boolean getBoolean() {
        return pref.getBoolean();
    }

    public void setLong(long value) {
        pref.setLong(value);
    }

    public long getLong() {
        return pref.getLong();
    }
    
}
