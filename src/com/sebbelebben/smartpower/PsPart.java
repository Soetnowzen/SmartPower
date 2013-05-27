package com.sebbelebben.smartpower;

/**
 * Created by johan on 25/05/2013.
 * This interface defines some common features of various parts of a power strip.
 * Classes that might implement this is PowerStrip, PsSocket and Group. All need the
 * possibility of renaming.
 */
public interface PsPart {
    public void setName(String name, final Server.OnSetNameReceiveListener listener);
    public void updateStatus(final Server.OnUpdateListener listener);
}
