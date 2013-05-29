package com.sebbelebben.smartpower;

/**
 * This interface defines some common features of various parts of a power strip.
 * Classes that might implement this is PowerStrip, PsSocket and Group. All need the
 * possibility of renaming and updating status.
 *
 * @author Johan Swetzén
 */
public interface PsPart {
    public void setName(String name, final Server.GenericStringListener listener);
    public void updateStatus(final Server.OnUpdateListener listener);
    public int getId();
}
