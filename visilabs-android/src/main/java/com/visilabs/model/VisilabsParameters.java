package com.visilabs.model;

import android.content.Context;

import java.io.Serializable;

public class VisilabsParameters implements Serializable {
    private String organizationId;
    private String siteId;
    private String segmentUrl;
    private String dataSource;
    private String realTimeUrl;
    private String channel;
    private int requestTimeoutSeconds;
    private String restUrl;
    private String encryptedDataSource;
    private String targetUrl;
    private String actionUrl;
    private String geofenceUrl;
    private boolean geofenceEnabled;

    private String sdkType;

    public VisilabsParameters(
            String organizationId,
            String siteId,
            String segmentUrl,
            String dataSource,
            String realTimeUrl,
            String channel,
            int requestTimeoutSeconds,
            String restUrl,
            String encryptedDataSource,
            String targetUrl,
            String actionUrl,
            String geofenceUrl,
            boolean geofenceEnabled,
            String sdkType) {

        this.organizationId = organizationId;
        this.siteId = siteId;
        this.segmentUrl = segmentUrl;
        this.dataSource = dataSource;
        this.realTimeUrl = realTimeUrl;
        this.channel = channel;
        this.requestTimeoutSeconds = requestTimeoutSeconds;
        this.restUrl = restUrl;
        this.encryptedDataSource = encryptedDataSource;
        this.targetUrl = targetUrl;
        this.actionUrl = actionUrl;
        this.geofenceUrl = geofenceUrl;
        this.geofenceEnabled = geofenceEnabled;
        this.sdkType = sdkType;
    }

    public String getOrganizationId() {
        return this.organizationId;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public String getSegmentUrl() {
        return this.segmentUrl;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public String getRealTimeUrl() {
        return this.realTimeUrl;
    }

    public String getChannel() {
        return this.channel;
    }

    public int getRequestTimeoutSeconds() {
        return this.requestTimeoutSeconds;
    }

    public String getRestUrl() {
        return this.restUrl;
    }

    public String getEncryptedDataSource() {
        return this.encryptedDataSource;
    }

    public String getTargetUrl() {
        return this.targetUrl;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public String getGeofenceUrl() {
        return this.geofenceUrl;
    }

    public boolean getGeofenceEnabled() {
        return this.geofenceEnabled;
    }

    public String getSdkType() {
        return this.sdkType;
    }
}
