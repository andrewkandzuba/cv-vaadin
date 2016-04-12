package org.ak.crossteam.doc.utils;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public interface Text {
    static String splitAndJoinBack(String v, char s, char j){
        return Joiner.on(j).join(Splitter.on(s).split(v));
    }
}
