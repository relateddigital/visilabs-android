/**
 * Copyright (c) 2015 Visilabs Ltd, All Rights Reserved.
 *
 * Please refer to your contract with Visilabs for the software license agreement.
 * If you do not have a contract, you do not have a license to use this software.
 */
package com.visilabs.android.json;

/**
 * The JSONException is thrown by the JSON.org classes then things are amiss.
 * 
 * @author JSON.org
 * @version 2
 */
public class JSONException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     * 
     * @param message Detail about the reason for the exception.
     */
    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
