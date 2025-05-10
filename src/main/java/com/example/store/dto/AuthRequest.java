package com.example.store.dto;

/**
 * AuthRequest DTO used for authentication.
 * record functionality can be used only in this particular case.
 * Other DTO(s) such as Product and User, needs to be marked - XmlElement - in order to
 * be able to have API(s) provide either JSON or XML.
 * The downside of using Java 17's record is IMO the fact that methods are no longer called
 * getUsername or getPassword instead we have methods named: username(), password() breaking the standard naming rules.
 */
public record AuthRequest(String username, String password) {

}