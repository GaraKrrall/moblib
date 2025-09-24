package com.kaplanlib.client.toast;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RpgToast {

    private static final List<RpgToast> TOASTS = new ArrayList<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private final Text message;
    private final long duration;
    private final long startTime;

    private RpgToast(long duration, Text message) {
        this.duration = duration;
        this.message = message;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Çağırmak için:
     * RpgToast.show(3, Text.literal("Seviye Atladın!"));
     */
    public static void show(int seconds, Text message) {
        TOASTS.add(new RpgToast(seconds * 1000L, message));
    }

    /**
     * HudMixin veya ayrı bir Hud render eventinde çağrılmalı
     */
    public static void render(DrawContext context, float tickDelta) {
        if (TOASTS.isEmpty()) return;

        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();

        Iterator<RpgToast> it = TOASTS.iterator();
        int index = 0;

        while (it.hasNext()) {
            RpgToast toast = it.next();
            long elapsed = System.currentTimeMillis() - toast.startTime;

            if (elapsed > toast.duration) {
                it.remove();
                continue;
            }

            float progress = (float) elapsed / toast.duration;
            float alpha;

            // fade-in (ilk 0.2)
            if (progress < 0.2f) {
                alpha = progress / 0.2f;
            }
            // fade-out (son 0.2)
            else if (progress > 0.8f) {
                alpha = 1f - ((progress - 0.8f) / 0.2f);
            } else {
                alpha = 1f;
            }

            // RGBA alpha uygula
            int textColor = ((int) (alpha * 255) << 24) | 0xFFE0C341; // altın sarısı
            int bgColor = ((int) (alpha * 180) << 24) | 0x000000; // yarı saydam siyah

            String msg = toast.message.getString();
            int textWidth = textRenderer.getWidth(msg);

            int boxWidth = textWidth + 20;
            int boxHeight = 20;
            int x = (screenWidth / 2) - (boxWidth / 2);
            int y = 20 + index * (boxHeight + 5);

            // arka plan
            context.fill(x, y, x + boxWidth, y + boxHeight, bgColor);

            // kenarlık
            context.drawBorder(x, y, boxWidth, boxHeight, 0x66FFFFFF);

            // metin
            context.drawCenteredTextWithShadow(textRenderer, msg,
                    screenWidth / 2, y + (boxHeight / 2) - 4, textColor);

            index++;
        }
    }
}
