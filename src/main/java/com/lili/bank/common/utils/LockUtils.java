package com.lili.bank.common.utils;
import java.util.concurrent.ConcurrentHashMap;

// Lock utility class that supports a locking mechanism with an expiration time.
public class LockUtils {

    // Stores the expiration time of each lock. The key is the lock name, and the value is the expiration timestamp.
    private static final ConcurrentHashMap<String, Long> lockExpireMap = new ConcurrentHashMap<>();


    /**
     * Attempt to acquire a lock.
     *
     * @param key     The key of the lock.
     * @param timeout The expiration time of the lock in milliseconds.
     * @return Returns true if the lock is successfully acquired; otherwise, returns false.
     */
    public static boolean lock(String key, long timeout) {
        long currentTime = System.currentTimeMillis();
        // Try to atomically update the expiration time of the lock.
        Long previousExpireTime = lockExpireMap.putIfAbsent(key, currentTime + timeout);
        if (previousExpireTime == null) {
            // The lock does not exist, and the first attempt to acquire the lock succeeds.
            return true;
        }

        if (currentTime > previousExpireTime) {
            // The lock has expired. Try to acquire the ReentrantLock to update the expiration time.
            //  real scenes synchronized LockUtils.class can optimize
            synchronized (LockUtils.class) {
                // Check again if the lock has expired to avoid redundant operations in a multi - threaded environment.
                Long latestExpireTime = lockExpireMap.get(key);
                if (latestExpireTime != null && currentTime > latestExpireTime) {
                    // The lock is indeed expired. Update the expiration time.
                    lockExpireMap.put(key, currentTime + timeout);
                    return true;
                }
            }
        }
        // The lock is not expired or the update fails, so the attempt to acquire the lock fails.
        return false;
    }

    /**
     * Release the lock.
     *
     * @param key The key of the lock.
     */
    public static void unlock(String key) {
        // Try to remove the lock.
        lockExpireMap.remove(key);
    }

}