package com.visilabs.inApp;

import androidx.annotation.NonNull;

public enum FontFamily {


    Monospace {
        @Override
        public String toString() {
            return "monospace";
        }
    },
    SansaSerif {
        @Override
        public String toString() {
            return "sansserif";
        }
    },
    Serif {
        @Override
        public String toString() {
            return "serif";
        }
    },
    Default{
        public String toString(){
            return "default";
        }
    }
}
