package com.visilabs.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Aynı aksiyon tipinin aynı anda birden fazla kez gösterilmesini engeller.
 *
 * <p>Her {@code customEvent}/{@code pageView} çağrısı yeni bir aksiyon isteği
 * tetikler. Kullanıcı aynı ekranda birkaç kez event gönderirse, koruma olmadan
 * her yanıt için yeni bir bell/banner/oyun üst üste açılır.
 *
 * <p>Ayrı bir Activity açan kurgular (SpinToWin, ScratchToWin, Survey ve
 * {@code FragmentActivity} olmayan host'lardaki fragment kurguları) için bu
 * sınıf kullanılır. Host'un kendi {@code FragmentManager}'ına eklenen
 * kurgularda ise fragment tag'i tek doğruluk kaynağıdır.
 */
public final class VisilabsActionGuard {

    public static final String TYPE_SPIN_TO_WIN = "spin_to_win";
    public static final String TYPE_SCRATCH_TO_WIN = "scratch_to_win";
    public static final String TYPE_SURVEY = "survey";

    private static final Set<String> SHOWING = Collections.synchronizedSet(new HashSet<String>());

    private VisilabsActionGuard() {
    }

    /**
     * Aksiyonu "gösteriliyor" olarak işaretler.
     *
     * @return zaten gösteriliyorsa {@code false}, aksi halde {@code true}
     */
    public static boolean acquire(String type) {
        return type != null && SHOWING.add(type);
    }

    public static void release(String type) {
        if (type != null) {
            SHOWING.remove(type);
        }
    }

    public static boolean isShowing(String type) {
        return type != null && SHOWING.contains(type);
    }
}
