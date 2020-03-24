package com.visilabs.inApp;

import androidx.annotation.NonNull;

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
    IMAGE_TEXT_BUTTON {
        public String toString() {
            return "image_text_button";
        }
    },
    FULL_IMAGE {
        @NonNull
        @Override
        public String toString() {
            return "full_image";
        }
    },

    IMAGE_BUTTON {
        @NonNull
        @Override
        public String toString() {
            return "image_button";
        }
    },

    NPS {
        @NonNull
        @Override
        public String toString() {
            return "nps";
        }
    }
}