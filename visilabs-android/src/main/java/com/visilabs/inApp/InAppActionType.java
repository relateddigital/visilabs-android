package com.visilabs.inApp;

public enum InAppActionType {
    UNKNOWN {
        @Override
        public String toString() {
            return "unknown";
        }
    },
    MINI {
        @Override
        public String toString() {
            return "mini";
        }
    },
    FULL {
        @Override
        public String toString() {
            return "full";
        }
    },
    IMAGE_TEXT_BUTTON{
        public String toString(){
            return "image_text_button";
        }
    }
}