package com.visilabs.inApp;

public enum FontFamily {


    Monospace {
        @Override
        public String toString() {
            return "monospace";
        }
    },
    SansSerif {
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
