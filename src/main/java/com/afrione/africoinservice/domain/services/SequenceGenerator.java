package com.afrione.africoinservice.domain.services;


public interface SequenceGenerator {
    String nextSequenceId();
    String generateCode(int size);
    String generateID(int alphaLength, int numericLength);
}
