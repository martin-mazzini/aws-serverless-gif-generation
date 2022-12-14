package lambda.imageprocessing.gifgeneration;

import lambda.imageprocessing.ImageFragment;
import lambda.imageprocessing.ImageProcessingUtils;
import lambda.imageprocessing.gifwriter.GifWriter;
import lambda.imageprocessing.gifwriter.impl.GifWriterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecursiveGifGenerator implements GifGenerator{


    private FrameGeneratorHelper frameGeneratorHelper = new FrameGeneratorHelper();
    private ImageProcessingUtils imageProcessingUtils = new ImageProcessingUtils();
    private GifWriter gifWriter = new GifWriterImpl();
    private static final Logger logger = LoggerFactory.getLogger(RecursiveGifGenerator.class);

    public List<BufferedImage> generateFrames(BufferedImage srcImage) throws IOException {

        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        List<BufferedImage> imgs = new ArrayList<>();
        imgs.add(srcImage);

        int minimumPixelSize = 4;
        while (width >= minimumPixelSize && height >= minimumPixelSize) {
            BufferedImage resized = imageProcessingUtils.resize(srcImage, width, height);
            ProcessFragment processFragment = input -> resized;
            BufferedImage bufferedImage = frameGeneratorHelper.generateFrames(srcImage, resized.getWidth(), resized.getHeight(), processFragment);
            imgs.add(bufferedImage);
            width = width / 2;
            height = height / 2;
        }
        return imgs;
    }



    @Override
    public GifWriter getGifWriter() {
        return gifWriter;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }



}
