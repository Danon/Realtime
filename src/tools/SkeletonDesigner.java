package tools;

import gameplay.Oval;
import gameplay.Point;
import ui.CustomUserInterface;
import ui.gfx.*;
import ui.gfx.resources.Resources;
import util.save.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public final class SkeletonDesigner extends JFrame implements MouseListener, MouseMotionListener, ActionListener {
    private final static CustomUserInterface ui = new CustomUserInterface();

    private final BufferedImage image;
    private final Graphics2D canvas;
    private final Drawer draw;
    private final SkeletonAnimation animation;
    private final SwingWorker<Integer, Integer> backgroundWork;
    private Graphics graphics;
    private int chosedFrame = 0;
    private boolean playing = false;
    private Skeleton choosedSkeleton;
    private JPanel pnlDrawable;
    private JSlider sldrProgress;
    private JPanel pnlButtonHolder;
    private JPanel pnlRightWrapper;
    private JPanel pnlTimeline;
    private JButton btnSave;
    private JButton btnCopy;
    private JButton btnPaste;
    private JButton btnRedraw;
    private JAntyaliasingCheckBox chckAntialiasing;
    private Skeleton copied;
    private Point mouse;
    private int pressedVectorId;

    private SkeletonDesigner(String animationName, int framesCount) throws IOException {
        // checks if you have to load an existing animation
        // or create a new one
        if (SaveManager.Animation.exists(animationName)) {
            animation = SaveManager.Animation.load(animationName);
        } else {
            if (framesCount < 1)
                throw new RuntimeException("How about nope? FramesCount can be lower than 1");

            if (framesCount > 12)
                throw new RuntimeException("How about nope? FramesCount can be higher than 12");

            animation = new SkeletonAnimation(animationName, framesCount, new Point(250, 250));
        }


        backgroundWork = new SwingWorker<Integer, Integer>() {
            @Override
            protected final Integer doInBackground() throws Exception {
                for (int i = 0; i < 180 * 3; i++) {
                    Thread.sleep(5);
                    this.publish(i);
                }
                return 180;
            }

            @Override
            protected final void process(final List<Integer> chunks) {
                sldrProgress.setValue(chunks.get(0) % 180);
            }
        };

        image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        canvas = image.createGraphics();
        draw = new Drawer(500, 500);
        draw.setCanvas(canvas);
        Drawer.invertPlane = true;
        Drawer.invertValue = 500;

        initWindow();
    }

    public static void main(String[] args) {
        Resources.load();
        new ListFiles().setVisible(true);
    }

    private void initWindow() {
        setTitle("Skeleton Designer");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {

        pnlDrawable = new JPanel();
        pnlDrawable.setPreferredSize(new Dimension(500, 500));
        pnlDrawable.setBorder(BorderFactory.createLineBorder(Color.black));

        pnlButtonHolder = new JPanel();
        pnlButtonHolder.setPreferredSize(new Dimension(100, 600));

        pnlRightWrapper = new JPanel();
        pnlRightWrapper.setPreferredSize(new Dimension(500, 600));

        pnlTimeline = new JPanel();
        pnlTimeline.setPreferredSize(new Dimension(500, 100));


        btnRedraw = new JButton("Redraw");
        btnRedraw.addActionListener(this);
        btnRedraw.setActionCommand("redraw");

        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        btnSave.setActionCommand("save");

        btnCopy = new JButton("Copy");
        btnCopy.addActionListener(this);
        btnCopy.setActionCommand("copy");

        btnPaste = new JButton("Paste");
        btnPaste.addActionListener(this);
        btnPaste.setActionCommand("paste");

        AbstractAction event = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreKeyFrame(e.getSource());
            }
        };

        for (int i = 0; i < animation.getSkeletonsCount(); i++) {
            JButton b = new JButton("btn" + i);
            b.addActionListener(event);
            pnlButtonHolder.add(b);
        }

        chckAntialiasing = new JAntyaliasingCheckBox("Antialiasing", canvas);

        sldrProgress = new JSlider(JSlider.HORIZONTAL, 0, 179, 0);
        sldrProgress.setPreferredSize(new Dimension(450, 80));
        sldrProgress.setMajorTickSpacing(18);
        sldrProgress.setMinorTickSpacing(3);
        sldrProgress.setPaintTicks(true);
        sldrProgress.setPaintLabels(true);
        sldrProgress.addChangeListener(e -> sldrProgress_OnChange());

        pnlTimeline.add(sldrProgress);

        pnlRightWrapper.add(pnlDrawable);
        pnlRightWrapper.add(pnlTimeline);
        this.add(pnlRightWrapper);

        pnlButtonHolder.add(btnRedraw);
        pnlButtonHolder.add(btnSave);
        pnlButtonHolder.add(btnCopy);
        pnlButtonHolder.add(btnPaste);
        pnlButtonHolder.add(chckAntialiasing);
        this.add(pnlButtonHolder);

        this.pack();
    }

    private void showWindow() {
        pnlDrawable.addMouseListener(this);
        pnlDrawable.addMouseMotionListener(this);
        setVisible(true);
        graphics = pnlDrawable.getGraphics();
        choosedSkeleton = new Skeleton(animation.getSkeleton(chosedFrame));
        sldrProgress_OnChange();
    }

    private void displaySkeleton(Skeleton skeleton) {
        choosedSkeleton = new Skeleton(skeleton);
        draw();
    }

    // static double[] bonesIds = { -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10 };

    private void btnCopy_OnClick() {
        copied = animation.getSkeleton(chosedFrame);
    }

    private void btnPaste_OnClick() {
        animation.setSkeleton(chosedFrame, new Skeleton(copied));
        displaySkeleton(copied);
    }

    private void restoreKeyFrame(Object source) {
        int id = Integer.parseInt(((JButton) source).getName().substring(3));
        choosedSkeleton = new Skeleton(animation.getSkeleton(chosedFrame = id));
        draw();
    }

    private void sldrProgress_OnChange() {
        int animationStep = sldrProgress.getValue();
        int idCurrent = animationStep / 20;
        double transition = (animationStep % 20.0) / 20.0;

        choosedSkeleton = Skeleton.Transition.cubic(                 // 9 - highest index of animation frames
                animation.getSkeleton(Math.max(0, idCurrent - 1) % (animation.getSkeletonsCount() - 1)),
                animation.getSkeleton(Math.max(0, idCurrent) % (animation.getSkeletonsCount() - 1)),
                animation.getSkeleton(Math.max(0, idCurrent + 1) % (animation.getSkeletonsCount() - 1)),
                animation.getSkeleton(Math.max(0, idCurrent + 2) % (animation.getSkeletonsCount() - 1)),
                transition
        );

        draw();
    }

    private void drawVectorSkeleton(Skeleton skeleton) {
        drawVectorSkeleton(skeleton, false);
    }

    private void drawVectorSkeleton(Skeleton skel, boolean grayScale) {
        canvas.setColor(grayScale ? Color.black : Color.white);

        VectorSkeleton skeleton = skel.toVectorSkeleton();

        canvas.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // left arm
        canvas.setColor(grayScale ? Color.black : Color.red);
        draw.curveCross(
                skeleton.getVector(0), skeleton.getVector(2), skeleton.getVector(4)
        );

        // left leg
        canvas.setColor(grayScale ? Color.black : Color.blue);
        draw.curveCross(
                animation.getSkeleton(chosedFrame).position, skeleton.getVector(6), skeleton.getVector(8)
        );

        // head
        canvas.setStroke(new BasicStroke(15));
        draw.borders(new Oval(
                Vector.transition(skeleton.getVector(1), skeleton.getVector(0), 0.5).asVector(),
                animation.getSkeleton(chosedFrame).head.getLength() / 2
        ));

        canvas.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // stomach
        draw.line(animation.getSkeleton(chosedFrame).position, skeleton.getVector(0));


        // right arm
        canvas.setColor(grayScale ? Color.black : Color.green);
        draw.curveCross(
                skeleton.getVector(0), skeleton.getVector(3), skeleton.getVector(5)
        );

        // right leg
        canvas.setColor(grayScale ? Color.black : Color.cyan);
        draw.curveCross(
                animation.getSkeleton(chosedFrame).position, skeleton.getVector(7).asVector(), skeleton.getVector(9).asVector()
        );


        canvas.setStroke(new BasicStroke(1));

        if (!grayScale) {
            for (Vector v : skeleton.getVectors()) {
                draw.borders(new Oval(v, 10));
            }
        }
    }

    private void draw() {
        draw.background(new Color(34, 34, 34));
        if (chosedFrame > 0) {
            drawVectorSkeleton(
                    new Skeleton(animation.getSkeleton(chosedFrame - 1)), true
            );
        }
        drawVectorSkeleton(choosedSkeleton);

        if (pressedVectorId > -1) {
            canvas.setColor(new Color(50, 50, 50));
            Vector joint = animation.getSkeleton(chosedFrame).getBoneJointPoint(pressedVectorId);
            Vector bone = animation.getSkeleton(chosedFrame).getBoneById(pressedVectorId).asVector();
            Vector res = joint.add(bone);
            draw.line(new Point(0, res.y), new Point(res.x - 10, res.y));
            draw.line(new Point(res.x, 500), new Point(res.x, res.y + 10));

            draw.textFormat("%.1fpx", new Point(7, res.y + 6), res.x);
            draw.textFormat("%.1fpx", new Point(res.x + 10, 483), res.y);
        }

        flip();
    }

    private void flip() {
        graphics.drawImage(image, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "redraw":
                draw();
                backgroundWork.execute();
                break;
            case "save":
                SaveManager.Animation.save(animation);
                ui.showInfo("saved");
                break;
            case "copy":
                btnCopy_OnClick();
                break;
            case "paste":
                btnPaste_OnClick();
                break;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressedVectorId = choosedSkeleton.toVectorSkeleton().getVectorIdUnder(
                new Point(e.getX(), e.getY()).invertY(500),
                10);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressedVectorId = -1;
        draw();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouse = new Point(e.getX(), e.getY()).invertY(500);
        if (playing) return;
        if (pressedVectorId > -1) {
            Vector joint = animation.getSkeleton(chosedFrame).getBoneJointPoint(pressedVectorId);
            double angle = joint.angleOf(mouse).getValue();

            DirectionalVector bone = animation.getSkeleton(chosedFrame).getBoneById(pressedVectorId);
            bone.direction.setValue(angle);
            choosedSkeleton = animation.getSkeleton(chosedFrame);

            Vector boneVector = bone.asVector().add(joint);
            this.setTitle(String.format(
                    "%s < %.1fpx, %.1fpx > %.2f° < %.1fpx, %.1fpx >",
                    animation.getName(),
                    joint.x, joint.y,
                    Math.toDegrees(angle),
                    boneVector.x, boneVector.y
            ));

            draw();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse = new Point(e.getX(), e.getY()).invertY(500);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static class ListFiles extends JFrame {
        ListFiles() {
            super("Load previous animation");
            this.setLayout(new GridLayout(0, 1));
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            final JList<String> skeletonFilesList = new JList<>(SaveManager.Animation.list());
            skeletonFilesList.setPreferredSize(new Dimension(300, 400));
            add(new JScrollPane(skeletonFilesList));

            JPanel panelWithButtons = new JPanel(new FlowLayout());

            JButton btnLoad = new JButton("Load");
            btnLoad.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new SkeletonDesigner(
                                String.valueOf(skeletonFilesList.getSelectedValue()),
                                -1
                        ).showWindow();
                    } catch (IOException ex) { // thrown when SkeletonDesigner can't load an aniamtion
                        ex.printStackTrace();
                    }
                }
            });

            JButton btnCreate = new JButton("New");
            btnCreate.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String animationName = ui.input("Animation name:", "Skeleton Designer", "New animation");
                    int framesCount = ui.input("Frame's count", "Skeleton Desginer", 10);
                    try {
                        new SkeletonDesigner(
                                animationName, framesCount
                        ).showWindow();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            panelWithButtons.add(btnLoad);
            panelWithButtons.add(btnCreate);
            this.add(panelWithButtons);
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
    }
}
