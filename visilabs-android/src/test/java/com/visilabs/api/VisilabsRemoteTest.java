package com.visilabs.api;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VisilabsRemoteTest {

    @Test
    public void removeReservedKeys_targetRequest_removesReservedAndBlankKeysWithoutCme() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("OM.exVisitorID", "id");
        properties.put("OM.cookieID", "cookie");
        properties.put("OM.zid", "zone");
        properties.put("OM.cat", "x");
        properties.put("OM.pv", "y");
        properties.put("", "empty");
        properties.put(" ", "blank");
        properties.put(null, "null");

        VisilabsRemote.removeReservedKeys(properties, VisilabsTargetRequest.RESERVED_KEYS);

        assertEquals(2, properties.size());
        assertEquals("x", properties.get("OM.cat"));
        assertEquals("y", properties.get("OM.pv"));
    }

    @Test
    public void removeReservedKeys_searchRequest_keepsZoneId() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("OM.oid", "org");
        properties.put("OM.zid", "zone");
        properties.put("OM.cat", "x");

        VisilabsRemote.removeReservedKeys(properties, VisilabsSearchRequest.RESERVED_KEYS);

        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("OM.zid"));
        assertTrue(properties.containsKey("OM.cat"));
    }

    @Test
    public void removeReservedKeys_nullOrEmpty_doesNothing() {
        VisilabsRemote.removeReservedKeys(null, VisilabsTargetRequest.RESERVED_KEYS);
        HashMap<String, String> properties = new HashMap<>();
        VisilabsRemote.removeReservedKeys(properties, VisilabsTargetRequest.RESERVED_KEYS);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void removeNullEntries_removesNullKeysAndValues() {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", null);
        map.put(null, "2");
        map.put("c", "");

        VisilabsRemote.removeNullEntries(map);

        assertEquals(2, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("", map.get("c"));
        assertFalse(map.containsKey(null));
        assertFalse(map.containsKey("b"));
        VisilabsRemote.removeNullEntries(null);
    }

    @Test
    public void getRequestUrl_returnsEmptyWhenRequestCannotBeCreated() {
        assertEquals("", VisilabsRemote.getRequestUrl(new FakeCall(null)));
    }

    @Test
    public void getRequestUrl_returnsUrl() {
        Request request = new Request.Builder().url("https://lgr.visilabs.net/ds/om.gif?a=1").build();
        assertEquals("https://lgr.visilabs.net/ds/om.gif?a=1", VisilabsRemote.getRequestUrl(new FakeCall(request)));
    }

    private static class FakeCall implements Call<Void> {
        private final Request mRequest;

        FakeCall(Request request) {
            mRequest = request;
        }

        @Override
        public Request request() {
            if (mRequest == null) {
                throw new IllegalArgumentException("Query map contained null value for key 'OM.vb'.");
            }
            return mRequest;
        }

        @Override
        public Response<Void> execute() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void enqueue(Callback<Void> callback) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call<Void> clone() {
            return this;
        }

        @Override
        public Timeout timeout() {
            return Timeout.NONE;
        }
    }
}
