package tn.univ.tp.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonneRequest(String nom, String prenom, Integer age) {
}
