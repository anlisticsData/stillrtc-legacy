package br.com.stilldistribuidora.partners.Base.Repository.ConfigurationsDomain;

import java.io.Serializable;

public class ConfigurationModel implements Serializable {
    private  long id;
    private  String uuu_config;
    private  String content;

    public void setUuu_config(String uuu_config) {
        this.uuu_config = uuu_config;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ConfigurationModel(){}
    public ConfigurationModel(String uuu_config, String content) {
        this.uuu_config = uuu_config.toUpperCase();
        this.content = content;
    }

    public ConfigurationModel(long itemId, String uuid_config, String content) {
        this.id = itemId;
        this.uuu_config = uuu_config;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getUuu_config() {
        return uuu_config;
    }

    public String getContent() {
        return content;
    }
}
