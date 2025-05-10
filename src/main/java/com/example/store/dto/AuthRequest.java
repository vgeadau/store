package com.example.store.dto;

/**
 * AuthRequest DTO used for authentication.
 * record functionality can be used only in this particular case.
 * Other DTO(s) such as Product and User, needs to be marked - XmlElement - in order to
 * be able to have API(s) provide either JSON or XML.
 */
public record AuthRequest(String username, String password) {

}