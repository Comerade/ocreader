package email.schaal.ocreader.util;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Target;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generate text and background color for Feeds
 */
public class FeedColors {
    private final Palette palette;

    public enum Type {
        TEXT,
        BACKGROUND
    }

    FeedColors(@NonNull Palette palette) {
        this.palette = palette;
    }

    FeedColors(@Nullable @ColorInt Integer color) {
        final Palette.Swatch swatch;

        if(color != null) {
            swatch = new Palette.Swatch(color, 1);
        } else {
            swatch = null;
        }

        palette = new Palette.Builder(Collections.singletonList(swatch)).addTarget(Target.MUTED).generate();
    }

    @ColorInt public int getColor(@NonNull Type type, @ColorInt int defaultColor) {
        final Palette.Swatch swatch;

        switch (type) {
            case TEXT:
                swatch = palette.getDominantSwatch();
                break;
            case BACKGROUND:
                swatch = palette.getMutedSwatch();
                break;
            default:
                swatch = null;
                break;
        }

        if(swatch != null)
            return swatch.getRgb();
        else
            return defaultColor;
    }
}
