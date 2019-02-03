package com.s0mbr3.moodtracker.models;

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

