package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JAntyaliasingCheckBox extends JCheckBox implements ActionListener {
    private final Graphics2D canvas;

    JAntyaliasingCheckBox(String name, Graphics2D canvas) {
        super(name);
        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                isSelected() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);

        canvas.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                isSelected() ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );
    }

}
