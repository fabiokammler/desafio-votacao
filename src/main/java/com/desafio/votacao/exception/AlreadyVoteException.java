package com.desafio.votacao.exception;

public class AlreadyVoteException extends RuntimeException {

    public AlreadyVoteException() {
    }

    public AlreadyVoteException(String message) {
        super(message);
    }
}
