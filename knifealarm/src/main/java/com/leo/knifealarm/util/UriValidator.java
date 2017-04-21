package com.leo.knifealarm.util;

import android.net.Uri;

/**
 * Created by Leo on 2017/4/21.
 */

public class UriValidator {

    private String mUriStr;

    public UriValidator() {

    }

    public UriValidator(String uri) {
        mUriStr = uri;
    }

    public UriValidator(Uri uri) {
        mUriStr = uri.toString();
    }

    public void setUriString(String uriStr) {
        mUriStr = uriStr;
    }

    public boolean isVaild() {
        return verify();
    }

    private boolean verify() {
        // [scheme:][//host:port][/path][?query][#fragment]
        if (mUriStr == null || mUriStr.indexOf(' ') >= 0 || mUriStr.indexOf('\n') >= 0) {
            return false;
        }

        Uri uri = Uri.parse(mUriStr);
        String scheme = uri.getScheme();
        String auth = uri.getAuthority();
        if (scheme == null || auth == null) { // must has scheme and authority in android
            return false;
        }

        // Look for period in a domain but followed by at least a two-char TLD
        // Forget strings that don't have a valid-looking protocol
        int period = mUriStr.indexOf('.');
        if (period >= mUriStr.length() - 2) {
            return false;
        }
        int colon = mUriStr.indexOf(':');
        if (period < 0 && colon < 0) {
            return false;
        }
        if (colon >= 0) {
            if (period < 0 || period > colon) {
                // colon ends the protocol
                for (int i = 0; i < colon; i++) {
                    char c = mUriStr.charAt(i);
                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                        return false;
                    }
                }
            } else {
                // colon starts the port; crudely look for at least two numbers
                if (colon >= mUriStr.length() - 2) {
                    return false;
                }
                for (int i = colon + 1; i < colon + 3; i++) {
                    char c = mUriStr.charAt(i);
                    if (c < '0' || c > '9') {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
