package com.natthanan.multipleviewsrecyclerview.exception;

/**
 * Created by natthanan on 10/18/2017.
 */

public class NullBaseAdapterException extends NullPointerException {

    public NullBaseAdapterException() {
        super("Your BaseAdapter is null. initialize it first.");
    }
}
