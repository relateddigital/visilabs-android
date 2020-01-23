/**
 * Copyright (c) 2015 Visilabs Ltd, All Rights Reserved.
 *
 * Please refer to your contract with Visilabs for the software license agreement.
 * If you do not have a contract, you do not have a license to use this software.
 */
package com.visilabs.android.json;

/**
 * The <code>JSONString</code> interface allows a <code>toJSONString()</code> method so that a class can change the behavior of <code>JSONObject.toString()</code>, <code>JSONArray.toString()</code>,
 * and <code>JSONWriter.value(</code>Object<code>)</code>. The <code>toJSONString</code> method will be used instead of the default behavior
 * of using the Object's <code>toString()</code> method and quoting the result.
 */
public interface JSONString {
    /**
     * The <code>toJSONString</code> method allows a class to produce its own JSON
     * serialization.
     * 
     * @return A strictly syntactically correct JSON text.
     */
    public String toJSONString();
}
