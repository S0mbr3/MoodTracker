package com.s0mbr3.moodtracker.models;

/**
 * Singleton holder to mimic alarm manager triggering the event to refresh the activity in front of the
 * user when the alarm ring
 *
 * @see com.s0mbr3.moodtracker.controllers.AlarmReceiver
 */
public class HumorUpdater {
    private UpdateAfterAlarm mUpdaterListener;

    private HumorUpdater() {
    }

    public interface UpdateAfterAlarm{
        void updaterAfterAlarm();
    }

    public void setUpdaterListener(UpdateAfterAlarm updaterListener){
        this.mUpdaterListener = updaterListener;
    }

    public void updateTrigger(){
        if(mUpdaterListener !=null)mUpdaterListener.updaterAfterAlarm();
    }

    private static class HumorUpdaterHolder{
        private final static HumorUpdater instance = new HumorUpdater();
    }

    public static HumorUpdater getInstance(){
        return HumorUpdaterHolder.instance;
    }
}

