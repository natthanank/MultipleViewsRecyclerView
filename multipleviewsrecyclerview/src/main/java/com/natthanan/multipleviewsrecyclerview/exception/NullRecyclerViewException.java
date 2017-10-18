package com.natthanan.multipleviewsrecyclerview.exception;

/**
 * Created by natthanan on 10/18/2017.
 */

public class NullRecyclerViewException extends RuntimeException {

    public NullRecyclerViewException() {
        super("Your RecyclerView is null or it doesn't have adapter.");
    }
}
