package com.syduck.hitchcommons.state;

public interface State {
    /**
     * 获取Key
     */
    String getKey();

    /**
     * 获取
     */
    State[] getValues();

    public boolean is(State state);


    /**
     * 获取状态对象
     */
    static State getState(String key) {
        return StateFactory.getState(key);
    }


}
