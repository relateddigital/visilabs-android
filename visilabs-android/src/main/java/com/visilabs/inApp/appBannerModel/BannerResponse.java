package com.visilabs.inApp.appBannerModel;

import com.visilabs.story.model.storylookingbanners.Story;

import java.io.Serializable;
import java.util.List;

public class BannerResponse {
        private String capping;

        private int VERSION;

        private List<String> FavoriteAttributeAction;

        private List<AppBanner> AppBanner;

        public void setCapping(String capping) {
            this.capping = capping;
        }

        public String getCapping() {
            return this.capping;
        }

        public void setVERSION(int VERSION) {
            this.VERSION = VERSION;
        }

        public int getVERSION() {
            return this.VERSION;
        }

        public void setFavoriteAttributeAction(List<String> FavoriteAttributeAction) {
            this.FavoriteAttributeAction = FavoriteAttributeAction;
        }

        public List<String> getFavoriteAttributeAction() {
            return this.FavoriteAttributeAction;
        }

        public void setAppBanner(List<AppBanner> AppBanner) {
            this.AppBanner = AppBanner;
        }

        public List<AppBanner> getAppBanner() {
            return this.AppBanner;
        }
    }

