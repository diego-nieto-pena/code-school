package com.patterns.creational.singleton;
public class ThreadSafeSingleton {
    private static volatile ThreadSafeSingleton singleton;

    public static ThreadSafeSingleton getInstance() {
        // 1. when a thread calls the getInstance() method check if the instance variable is null
        if (singleton == null) {
            // 2. once nullity check is done, the thread acquire the lock
            synchronized (ThreadSafeSingleton.class) {
                // 3. check once again if the instance is null (it could be updated by other thread, before lock acquire)
                if (singleton == null) {
                    // 4. as the variable is declared as volatile, any thread accessing it in the future will get the most recent value
                    singleton = new ThreadSafeSingleton();
                }
            }
        }
        return singleton;
    }
}
