package de.blutmondgilde.modpackaddons.util;

import de.blutmondgilde.modpackaddons.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class ImageUtils {

    public static void loadFromUrl() throws IOException {
        final BufferedImage img = ImageIO.read(new URL(Config.pathOrUrl.get()));

        Minecraft.getInstance().getMainWindow().setWindowIcon(getScaledImage(img, 16), getScaledImage(img, 32));
    }


    public static void loadFromPath() throws IOException {
        String[] folders = Config.pathOrUrl.get().split("/");
        Path path = FMLPaths.GAMEDIR.get();
        for (int i = 0; i < folders.length; i++) {
            path = path.resolve(folders[i]);
        }
        final BufferedImage img = ImageIO.read(path.toFile());

        Minecraft.getInstance().getMainWindow().setWindowIcon(getScaledImage(img, 16), getScaledImage(img, 32));
    }

    private static ByteArrayInputStream getScaledImage(final BufferedImage bufferedImage, final int size) throws IOException {
        Image image = bufferedImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage temp = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = temp.createGraphics();
        graphics2D.drawImage(image, 0, 0, size, size, null);
        graphics2D.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(temp, "png", out);

        return new ByteArrayInputStream(out.toByteArray());
    }
}

