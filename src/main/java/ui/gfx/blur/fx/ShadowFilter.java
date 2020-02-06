package ui.gfx.blur.fx;

import lombok.Getter;
import lombok.Setter;
import ui.gfx.blur.filter.GaussianFilter;
import ui.gfx.blur.math.AbstractBufferedImageOp;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

@Getter
@Setter
public class ShadowFilter extends AbstractBufferedImageOp {
    private int radius = 5;
    private int xOffset = 5;
    private int yOffset = 5;
    private float opacity = 0.5f;
    private boolean addMargins = false;
    private boolean shadowOnly = false;
    private int shadowColor = 0xff000000;

    public ShadowFilter(int radius, int xOffset, int yOffset, float opacity) {
        this.radius = radius;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.opacity = opacity;
    }

    protected void transformSpace(Rectangle r) {
        if (addMargins) {
            r.width += Math.abs(xOffset) + 2 * radius;
            r.height += Math.abs(yOffset) + 2 * radius;
        }
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        if (dst == null) {
            if (addMargins) {
                ColorModel cm = src.getColorModel();
                dst = new BufferedImage(cm, cm.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), cm.isAlphaPremultiplied(), null);
            } else
                dst = createCompatibleDestImage(src, null);
        }

        // Make a black mask from the image's alpha channel 
        float[][] extractAlpha = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, opacity}
        };
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        new BandCombineOp(extractAlpha, null).filter(src.getRaster(), shadow.getRaster());
        shadow = new GaussianFilter(radius).filter(shadow, null);

        Graphics2D g = dst.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (addMargins) {
            int radius2 = radius / 2;
            int topShadow = Math.max(0, radius - yOffset);
            int leftShadow = Math.max(0, radius - xOffset);
            g.translate(topShadow, leftShadow);
        }
        g.drawRenderedImage(shadow, AffineTransform.getTranslateInstance(xOffset, yOffset));
        if (!shadowOnly) {
            g.setComposite(AlphaComposite.SrcOver);
            g.drawRenderedImage(src, null);
        }
        g.dispose();

        return dst;
    }
}
