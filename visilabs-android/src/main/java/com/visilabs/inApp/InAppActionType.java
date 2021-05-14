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

    SMILE_RATING {
        @NonNull
        @Override
        public String toString() {
            return "smile_rating";
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
    },

    ALERT {
        @NonNull
        @Override
        public String toString() {
            return "alert";
        }
    },

    NPS_WITH_NUMBERS {
        @NonNull
        @Override
        public String toString() {
            return "nps_with_numbers";
        }
    },

    CAROUSEL {
        @NonNull
        @Override
        public String toString() {
            return "carousel"; //TODO: Check this string when the real data comes.
        }
    },

    NPS_AND_SECOND_POP_UP {
        @NonNull
        @Override
        public String toString() {
            return "nps_with_secondpopup";
        }
    }
}