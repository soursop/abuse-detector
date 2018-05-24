package com.abuse.event;


public interface Move extends Event {
    int from();
    long before();
    int receiver();
    int amount();
}
