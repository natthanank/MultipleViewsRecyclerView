package com.natthanan.multipleviewrecyclerview.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by natthanan on 10/17/2017.
 */

public class RequestModel {

    @SerializedName("tk_device_owner")
    private String deviceOwner;
    @SerializedName("tk_app_name")
    private String appName;
    @SerializedName("tk_os")
    private String os;
    @SerializedName("password")
    private String password;
    @SerializedName("tk_app_version")
    private String appVersion;
    @SerializedName("tk_device_name")
    private String deviceName;
    @SerializedName("username")
    private String username;
    @SerializedName("tk_device_model")
    private String deviceModel;
    @SerializedName("tk_emulator_version")
    private String emulatorVersion;
    @SerializedName("tk_mac_address")
    private String macAddress;

    public String getDeviceOwner() {
        return deviceOwner;
    }

    public void setDeviceOwner(String deviceOwner) {
        this.deviceOwner = deviceOwner;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getEmulatorVersion() {
        return emulatorVersion;
    }

    public void setEmulatorVersion(String emulatorVersion) {
        this.emulatorVersion = emulatorVersion;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
