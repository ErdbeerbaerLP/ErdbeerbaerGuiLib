package de.erdbeerbaerlp.guilib.components;

import com.icafe4j.image.gif.GIFFrame;
import com.icafe4j.image.reader.GIFReader;
import de.erdbeerbaerlp.guilib.util.ImageUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GifThread extends Thread {
    private final ArrayList<Map.Entry<byte[], Integer>> gifData = new ArrayList<>();
    private final DynamicTexture outputTexture;
    private final InputStream is;
    private final boolean doGifLoop;

    public GifThread(final InputStream inputStream, final DynamicTexture outputTexture) {
        this(inputStream, outputTexture, true, true);
    }

    public GifThread(final InputStream inputStream, final DynamicTexture outputTexture, boolean keepAspectRatio, boolean doGifLoop) {
        this.is = inputStream;
        this.outputTexture = outputTexture;
        this.doGifLoop = doGifLoop;
        setDaemon(true);
        setName("Gif Renderer " + UUID.randomUUID().toString());
        List<GIFFrame> gifFrames;
        final GIFReader r = new GIFReader();
        try {
            r.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gifFrames = r.getGIFFrames();
        for (final GIFFrame frame : gifFrames) {
            try {
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                if (keepAspectRatio)
                    ImageIO.write(ImageUtil.scaleImageKeepAspectRatio(frame.getFrame(), outputTexture.getTextureData().getWidth(), outputTexture.getTextureData().getHeight()), "png", os);
                else
                    ImageIO.write(ImageUtil.scaleImage(frame.getFrame(), outputTexture.getTextureData().getWidth(), outputTexture.getTextureData().getHeight()), "png", os);
                gifData.add(new AbstractMap.SimpleEntry<>(os.toByteArray(), frame.getDelay()));
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //noinspection LoopConditionNotUpdatedInsideLoop
        do {
            for (final Map.Entry<byte[], Integer> frame : gifData) {
                try {
                    final ByteArrayInputStream is = new ByteArrayInputStream(frame.getKey());
                    NativeImage img;
                    do {
                        img = NativeImage.read(is);
                    } while (img.getBytes().length == 0);
                    outputTexture.setTextureData(img);
                    is.close();
                    sleep(frame.getValue() * 10);
                } catch (InterruptedException ignored) {
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } while (doGifLoop);
    }
}
