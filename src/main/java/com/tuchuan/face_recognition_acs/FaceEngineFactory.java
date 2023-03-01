package com.tuchuan.face_recognition_acs;

import com.arcsoft.face.EngineConfiguration;
import org.springframework.beans.factory.annotation.Value;

import javax.print.attribute.standard.MediaSize;

public class FaceEngineFactory {
    @Value("${config.arcface-sdk.app-id}")
    private String appId;
    @Value("${config.arcface-sdk.sdk-key}")
    private String sdkKey;
    private String activeKey;
    private EngineConfiguration engineConfiguration;
    public FaceEngineFactory(String appId, String sdkKey, String activeKey, EngineConfiguration engineConfiguration) {
        this.appId = appId;
        this.sdkKey = sdkKey;
        this.activeKey = activeKey;
        this.engineConfiguration = engineConfiguration;
    }
}
