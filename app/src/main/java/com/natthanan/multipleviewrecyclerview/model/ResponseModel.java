package com.natthanan.multipleviewrecyclerview.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by natthanan on 10/17/2017.
 */

public class ResponseModel {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("entries")
    private Entries entries;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Entries getEntries() {
        return entries;
    }

    public void setEntries(Entries entries) {
        this.entries = entries;
    }


    public static class Entries {
        @SerializedName("u_id")
        private String id;
        @SerializedName("token")
        private String token;
        @SerializedName("name")
        private String name;
        @SerializedName("role")
        private String role;
        @SerializedName("allow_module")
        private List<String> allowModule;
        @SerializedName("allow_service")
        private List<String> allowService;
        @SerializedName("webapi")
        private List<WebApi> webApi;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<String> getAllowModule() {
            return allowModule;
        }

        public void setAllowModule(List<String> allowModule) {
            this.allowModule = allowModule;
        }

        public List<String> getAllowService() {
            return allowService;
        }

        public void setAllowService(List<String> allowService) {
            this.allowService = allowService;
        }

        public List<WebApi> getWebApi() {
            return webApi;
        }

        public void setWebApi(List<WebApi> webApi) {
            this.webApi = webApi;
        }

        public static class WebApi {
            @SerializedName("service_id")
            private int id;
            @SerializedName("service_key")
            private String key;
            @SerializedName("service_url")
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
