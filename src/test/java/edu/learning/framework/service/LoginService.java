package edu.learning.framework.service;

/** Small service contract to illustrate abstraction between workflows and implementation. */
public interface LoginService {
    boolean credentialsAccepted(String username, String password);
}
