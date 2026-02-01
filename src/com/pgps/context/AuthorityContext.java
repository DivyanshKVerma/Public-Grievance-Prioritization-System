package com.pgps.context;

public class AuthorityContext {

    // Simulated logged-in authority
    private static int currentAuthorityId = 101;

    public static int getCurrentAuthorityId() {
        return currentAuthorityId;
    }

    public static void setCurrentAuthorityId(int authorityId) {
        currentAuthorityId = authorityId;
    }
}