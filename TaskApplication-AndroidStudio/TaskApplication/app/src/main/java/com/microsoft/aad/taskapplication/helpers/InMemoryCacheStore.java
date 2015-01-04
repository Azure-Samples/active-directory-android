package com.microsoft.aad.taskapplication.helpers;


import com.microsoft.aad.adal.CacheKey;
import com.microsoft.aad.adal.ITokenCacheStore;
import com.microsoft.aad.adal.ITokenStoreQuery;
import com.microsoft.aad.adal.TokenCacheItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class InMemoryCacheStore  implements ITokenCacheStore, ITokenStoreQuery {

    private static final long serialVersionUID = 1L;
    private static final String TAG = "InMemoryCacheStore";
    private static Object sLock = new Object();
    HashMap<String, TokenCacheItem> cache = new HashMap<>();

    private static final InMemoryCacheStore INSTANCE = new InMemoryCacheStore();

    private InMemoryCacheStore() {}

    public static InMemoryCacheStore getInstance() {
        return INSTANCE;
    }


    @Override
    public TokenCacheItem getItem(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        return cache.get(key);
    }

    @Override
    public void removeItem(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        cache.remove(key);
    }

    @Override
    public void setItem(String key, TokenCacheItem item) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        if (item == null) {
            throw new IllegalArgumentException("item");
        }

        cache.put(key, item);
    }

    @Override
    public void removeAll() {
        cache = new HashMap<>();
    }

    // Extra helper methods can be implemented here for queries

    /**
     * User can query over iterator values.
     */
    @Override
    public Iterator<TokenCacheItem> getAll() {

        Iterator<TokenCacheItem> values = cache.values().iterator();
        return values;
    }

    /**
     * Unique users with tokens.
     *
     * @return unique users
     */
    @Override
    public HashSet<String> getUniqueUsersWithTokenCache() {
        Iterator<TokenCacheItem> results = this.getAll();
        HashSet<String> users = new HashSet<String>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (item.getUserInfo() != null && !users.contains(item.getUserInfo().getUserId())) {
                users.add(item.getUserInfo().getUserId());
            }
        }

        return users;
    }

    /**
     * Tokens for resource.
     *
     * @param resource Resource identifier
     * @return list of {@link TokenCacheItem}
     */
    @Override
    public ArrayList<TokenCacheItem> getTokensForResource(String resource) {
        Iterator<TokenCacheItem> results = this.getAll();
        ArrayList<TokenCacheItem> tokenItems = new ArrayList<TokenCacheItem>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (item.getResource().equals(resource)) {
                tokenItems.add(item);
            }
        }

        return tokenItems;
    }

    /**
     * Get tokens for user.
     *
     * @param userid Userid
     * @return list of {@link TokenCacheItem}
     */
    @Override
    public ArrayList<TokenCacheItem> getTokensForUser(String userid) {
        Iterator<TokenCacheItem> results = this.getAll();
        ArrayList<TokenCacheItem> tokenItems = new ArrayList<TokenCacheItem>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (item.getUserInfo() != null
                    && item.getUserInfo().getUserId().equalsIgnoreCase(userid)) {
                tokenItems.add(item);
            }
        }

        return tokenItems;
    }

    /**
     * Clear tokens for user without additional retry.
     *
     * @param userid UserId
     */
    @Override
    public void clearTokensForUser(String userid) {
        ArrayList<TokenCacheItem> results = this.getTokensForUser(userid);

        for (TokenCacheItem item : results) {
            if (item.getUserInfo() != null
                    && item.getUserInfo().getUserId().equalsIgnoreCase(userid)) {
                this.removeItem(CacheKey.createCacheKey(item));
            }
        }
    }

    /**
     * Get tokens about to expire.
     *
     * @return list of {@link TokenCacheItem}
     */
    @Override
    public ArrayList<TokenCacheItem> getTokensAboutToExpire() {
        Iterator<TokenCacheItem> results = this.getAll();
        ArrayList<TokenCacheItem> tokenItems = new ArrayList<TokenCacheItem>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (isAboutToExpire(item.getExpiresOn())) {
                tokenItems.add(item);
            }
        }

        return tokenItems;
    }

    private boolean isAboutToExpire(Date expires) {
        Date validity = getTokenValidityTime().getTime();

        if (expires != null && expires.before(validity)) {
            return true;
        }

        return false;
    }

    private static final int TOKEN_VALIDITY_WINDOW = 10;

    private static Calendar getTokenValidityTime() {
        Calendar timeAhead = Calendar.getInstance();
        timeAhead.add(Calendar.SECOND, TOKEN_VALIDITY_WINDOW);
        return timeAhead;
    }

    @Override
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        return cache.containsKey(key);
    }
}
