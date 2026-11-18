package edu.learning.framework.service;

/** Local demo credential policy corresponding to login.html. */
public final class LocalLoginService implements LoginService {
    @Override public boolean credentialsAccepted(String username, String password) {
        return "student".equals(username) && "selenium123".equals(password);
    }
}
