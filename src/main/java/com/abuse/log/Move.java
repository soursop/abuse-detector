package com.abuse.log;


import com.abuse.log.Log;

public interface Move extends Log {
    int from();
    long before();
    int receiver();
    int amount();
}
