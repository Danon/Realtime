package tools.mapDesigner;

import app.Application;
import gameplay.*;
import gameplay.Point;
import gameplay.Rectangle;
import ui.CustomUserInterface;
import ui.gfx.Camera;
import ui.gfx.DrawFrom;
import ui.gfx.Drawer;
import ui.gfx.Renderer;
import ui.gfx.resources.Resources;
import util.TimePassed;
import util.save.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public final class MapDesigner extends JFrame implements KeyListener, MouseListener, MouseMotionListener,
        MouseWheelListener, WindowListener {
    private final static boolean AutoSelectOnCreate = true;
    private final static boolean PersistOperation = false;

    private final static CustomUserInterface ui = new CustomUserInterface();
    private final Graphics panelGraphics;
    private final BufferedImage image;
    private final Graphics2D canvas;
    private final Drawer draw;
    private final GameMap map;

    private Operation operation = Operation.None;

    private Point mouse = new Point();

    private int panelWidth, panelHeight;
    private int floorTiles = 4, ladderTiles = 4;

    JPanel panel;

    private HashSet<Floor> selectedFloors = new HashSet<>();
    private HashSet<Ladder> selectedLadders = new HashSet<>();

    private Floor hoveredFloor;
    private Ladder hoveredLadder;

    boolean vert, hor;

    private int suwak = 3;

    private String[] suwaks = new String[]{"Accuracy", "Stretch", "Blur", "Radius", "Creating"};

    private MapDesigner(String mapName, int mapWidth, int mapHeight) {
        if (SaveManager.Map.exists(mapName)) {
            map = SaveManager.Map.load(mapName);
            map.accept();
        } else {
            map = new GameMap(mapName, mapWidth, mapHeight);
        }

        this.setTitle("Map Designer");

        panelWidth = Math.min(Math.max(map.getWidth(), 100), 1080);
        panelHeight = Math.min(Math.max(map.getHeight(), 100), 720);
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.getContentPane().add(panel);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        panelGraphics = panel.getGraphics();
        image = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_4BYTE_ABGR);
        canvas = image.createGraphics();

        draw = new Drawer(canvas, panelWidth, panelHeight);
        draw.useCamera(new Camera());

        setLocationRelativeTo(null);
        Resources.load();
        Renderer.CAMERA_SIDE_MARGIN = 100;
        Renderer.CAMERA_TOP_MARGIN = 100;
        Renderer.CAMERA_BOTTOM_MARGIN = 100;
        buildBlockTiles();
        buildMatrix();
    }

    private void clearSelection() {
        selectedFloors.clear();
        selectedLadders.clear();
    }

    private void toggleSelected(Floor floor) {
        if (floor == null) return;
        if (selectedFloors.contains(floor)) {
            selectedFloors.remove(floor);
        } else {
            selectedFloors.add(floor);
        }
    }

    private void toggleSelected(Ladder ladder) {
        if (ladder == null) return;
        if (selectedLadders.contains(ladder)) {
            selectedLadders.remove(ladder);
        } else {
            selectedLadders.add(ladder);
        }
    }

    private void select(Floor floor) {
        if (!selectedFloors.contains(floor)) {
            selectedFloors.add(floor);
        }
    }

    private void select(Ladder ladder) {
        if (!selectedLadders.contains(ladder)) {
            selectedLadders.add(ladder);
        }
    }

    private void selectOnly(Floor floor) {
        clearSelection();
        selectedFloors.add(floor);

    }

    private void selectOnly(Ladder ladder) {
        clearSelection();
        selectedLadders.add(ladder);
    }


    private String debugText = "";
    private long beforeTime;
    private Angle angle = new Angle();

    void drawnInTime(double ms) {
        debugText = String.format("%.2fms (%d fps)", ms, (int) (1000 / ms));
    }


    private void refreshDraw() {
        beforeTime = System.nanoTime();
        draw.freeCamera();
        draw.background(Color.orange);
        draw.useCamera();
        /*
        if (mouse.x < scrollMargin) {
            draw.getCamera().moveX( -(scrollMargin + mouse.getX()) / 10 );
        }
        if (mouse.y < scrollMargin) {
            draw.getCamera().moveY(  (scrollMargin + mouse.getY()) / 10 );
        }
        if (mouse.x > panelWidth - scrollMargin) {
            draw.getCamera().moveX( (mouse.getX() - (panelWidth - scrollMargin)) / 10 );
        }
        if (mouse.y > panelHeight - scrollMargin) {
            draw.getCamera().moveY(-(mouse.getY() - (panelHeight - scrollMargin)) / 10);
        }*/

        // canvas.setClip(mouse.getX() - 50, mouse.getY() - 50, 100, 100);

        Rectangle borders = new Rectangle(
                -1, -1,
                map.getWidth() + 1,
                map.getHeight() + 1
        );

        canvas.setColor(Color.black);
        draw.borders(borders);

        draw.getCamera().cap(borders, panelWidth, panelHeight);

        //setRaycastingFieldClip();

        if (vert) {
            draw.image("scrollVert.png", mouse.subX(40), DrawFrom.Center);
        }
        if (hor) {
            draw.image("scrollHor.png", mouse.addY(40).subX(20), DrawFrom.Center);
        }


        for (Floor floor : map.getFloors()) {
            draw.floor(floor);
            Rectangle border = new Rectangle(
                    floor.getLeft() - 2,
                    panelHeight - floor.getTop() - 1,
                    floor.getTiles() * 32 + 2,
                    32 + 3
            );

            if (selectedFloors.contains(floor)) {
                drawBorder(border);
            }

            if (hoveredFloor != null && hoveredFloor.equals(floor)) {
                canvas.setColor(Color.BLUE);
                border.expand(2);
                draw.borders(border);
            }
        }

        for (Ladder ladder : map.getLadders()) {
            draw.ladder(ladder);

            Rectangle border = new Rectangle(
                    ladder.getLeft() - 2 - 4,
                    panelHeight - ladder.getPeek() - 1,
                    32 + 3,
                    ladder.getHeightTiles() * 32 + 4
            );

            if (selectedLadders.contains(ladder)) {
                drawBorder(border);
            }
            if (hoveredLadder != null && hoveredLadder.equals(ladder)) {
                canvas.setColor(Color.BLUE);
                border.expand(2);
                draw.borders(border);
            }

        }
        canvas.setColor(Color.white);

        switch (operation) {
            case CreateFloor:
                snapMouse();
                draw.borders(new Rectangle(
                        mouse.getX() - floorTiles * 16, mouse.getY() - 16, 32 * floorTiles, 32
                ));
                break;

            case CreateLadder:
                snapMouse();
                draw.borders(new Rectangle(
                        mouse.getX() - 16, mouse.getY() - 16, 32, 32 * ladderTiles
                ));

                break;

            case Delete:
                draw.image("delete.png", mouse.subX(8), DrawFrom.Center);
                // operation = Operation.None;

                break;
        }

        canvas.setColor(Color.red);
        canvas.drawString(debugText, 50, 50);

        debugText = angle.toString();

        flip();
    }

    private void drawBorder(Rectangle border) {
        canvas.setColor(Color.PINK);
        draw.line(
                new Point(border.x - 1, panelHeight),
                new Point(border.x - 1, 0));
        draw.line(
                new Point(border.x + border.width, panelHeight),
                new Point(border.x + border.width + 1, 0));
        draw.line(
                new Point(0, border.y + border.height - 1),
                new Point(panelWidth, border.y + border.height - 1));
        draw.line(
                new Point(0, border.y - 1),
                new Point(panelWidth, border.y - 1));
        canvas.setColor(Color.RED);
        draw.borders(border);
    }


    private void clearTiles() {
        for (int i = 0; i < tileCountX; i++) {
            for (int j = 0; j < tileCountY; j++) {
                tiles[i][j] = false;
            }
        }
    }

    static int tileSize = 3;
    static int VIEW_RADIUS = 750 / tileSize;
    static double acc = 12.0;

    boolean[][] tiles, blocks;

    BufferedImage shade;
    Graphics2D shadeCns;

    int matrixSize = 4;
    int shrinkSize = 4;

    BufferedImageOp op;

    void buildMatrix() {
        float[] matrix = new float[matrixSize * matrixSize];
        for (int i = 0; i < matrixSize * matrixSize; i++) {
            matrix[i] = 1.0f / matrixSize / matrixSize;
        }
        op = new ConvolveOp(new Kernel(matrixSize, matrixSize, matrix));
    }

    void setBlock(int x, int y) {
        if (x > -1 && y > -1)
            if (x < tileCountX && y < tileCountY)
                blocks[x][y] = true;
    }

    int tileCountX, tileCountY;

    void buildBlockTiles() {
        tileCountX = (int) Math.ceil(map.getWidth() / tileSize) + shrinkSize * 2;
        tileCountY = (int) Math.ceil(map.getHeight() / tileSize) + shrinkSize * 2;

        tiles = new boolean[tileCountX + shrinkSize * 2][tileCountY + shrinkSize * 2];
        shade = new BufferedImage(tileCountX, tileCountY, BufferedImage.TYPE_INT_ARGB);
        clearTiles();

        blocks = new boolean[tileCountX + shrinkSize * 2][tileCountY + shrinkSize * 2];

        map.accept();
        for (Floor floor : map.getFloors()) {
            for (int i = 0; i < floor.getWidth() / tileSize; i++) {
                for (int j = 0; j < Floor.HEIGHT / tileSize; j++)
                    setBlock((floor.getLeft() + shrinkSize) / tileSize + i, tileCountY - (floor.getTop() + shrinkSize) / tileSize + j);
            }
        }
    }

    void FOV() {
        clearTiles();
        for (int i = 0; i < 360 * acc; i++) {
            DoFov(
                    Math.cos((double) i * 0.01745328d / acc),
                    Math.sin((double) i * 0.01745328d / acc)
            );
        }
    }

    void DoFov(double x, double y) {
        float ox = (float) mouse.x / tileSize + 0.5f;
        float oy = (float) mouse.y / tileSize + 0.5f;

        for (int i = 0; i < VIEW_RADIUS; i++) {
            if ((int) ox > -1 && (int) oy > -1)
                if ((int) ox < tileCountX && (int) oy < tileCountY) {

                    tiles[(int) ox][(int) oy] = true;//Set the tile to visible.
                    if (blocks[(int) ox][(int) oy]) {
                        return;
                    }
                    ox += x;
                    oy += y;
                }
        }
    }


    public void drawRayCasting() {
        TimePassed measureRayCasting = new TimePassed("FPS");

        TimePassed clearing = new TimePassed();
        shadeCns = shade.createGraphics();
        shadeCns.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        shadeCns.fillRect(0, 0, shade.getWidth(), shade.getHeight());
        shadeCns.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        clearing.storeTime();

        TimePassed fov = new TimePassed();
        FOV();
        fov.storeTime();

        TimePassed drawingFog = new TimePassed();
        shadeCns.setColor(Color.black);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (!tiles[i][j]) {
                    shadeCns.fillRect(i, j, 1, 1);
                }
            }
        }
        drawingFog.storeTime();

        TimePassed blur = new TimePassed();
        shade = op.filter(shade, null);
        blur.storeTime();

        TimePassed printing = new TimePassed();
        canvas.drawImage(shade,
                0, 0, map.getWidth(), map.getHeight(),
                shrinkSize, +shrinkSize * 2, shade.getWidth() - shrinkSize * 2, shade.getHeight() - shrinkSize * 2,
                null);
        printing.storeTime();

        measureRayCasting.storeTime();

        double wholeTime = measureRayCasting.restoreTime();
        System.out.println(measureRayCasting);

        double _clear = clearing.restoreTime();
        double _fov = fov.restoreTime();
        double _fog = drawingFog.restoreTime();
        double _blur = blur.restoreTime();
        double _print = printing.restoreTime();

        part("clear", _clear, wholeTime);
        part("fov", _fov, wholeTime);
        part("fog", _fog, wholeTime);
        part("blur", _blur, wholeTime);
        part("print", _print, wholeTime);
    }

    private void part(String name, double val, double wholeTime) {
        System.out.println(String.format(
                "  %s: %d%% = %.2f",
                name,
                (int) (val / wholeTime * 100.0),
                val
        ));
    }

    private void setHandlers() {
        this.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.addMouseWheelListener(this);
        this.addWindowListener(this);
    }


    private void flip() {
        panelGraphics.drawImage(image, 0, 0, null);
    }

    public static class ListFiles extends JFrame {
        ListFiles() {
            super("Load previous map");
            setLayout(new GridLayout(0, 1));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            final JList<String> mapFilesList = new JList<>(SaveManager.Map.list());
            mapFilesList.setSelectedIndex(1);
            mapFilesList.setPreferredSize(new Dimension(300, 400));
            add(new JScrollPane(mapFilesList));

            JPanel panelWithButtons = new JPanel(new FlowLayout());

            JButton btnLoad = new JButton("Load");
            btnLoad.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new MapDesigner(
                            String.valueOf(mapFilesList.getSelectedValue()), 0, 0
                    ).setHandlers();
                }
            });
            JButton btnCreate = new JButton("New");
            btnCreate.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String mapName = ui.input("Map name:", "Map Designer", "New map");
                    String width = ui.input("Map width (px)", "Map Designer", "640");
                    String height = ui.input("Map height (px)", "Map Designer", "480");
                    new MapDesigner(
                            mapName,
                            Math.min(Math.max(Integer.parseInt(width), 640), 1080),
                            Math.min(Math.max(Integer.parseInt(height), 480), 720)
                    ).setHandlers();
                }
            });

            panelWithButtons.add(btnLoad);
            panelWithButtons.add(btnCreate);
            add(panelWithButtons);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }


    void updateTitle() {
        this.setTitle(String.format("Map: \"%s\" (%d/%d) (%dpx / %dpx) :  %s",
                map.getName(),
                mouse.getX(),
                mouse.getY(),
                draw.getCamera().getX(),
                draw.getCamera().getY(),
                suwaks[suwak]
        ));
    }

    public static void main(String[] args) {
        Application.RunOptions.getOptionByName("-Debug").setUsed(false);
        new ListFiles();
    }

    Point centerOf(Floor floor) {
        return new Point(floor.getLeft() + floor.getTiles() * 16, panelHeight - floor.getTop() + 16);
    }

    int snapTo = 8;

    void snapMouse() {
        mouse.x = Math.round(mouse.x / snapTo) * snapTo;
        mouse.y = Math.round(mouse.y / snapTo) * snapTo;
    }

    Floor floorOnPoint(Point point) {
        for (Floor floor : map.getFloors()) {
            if (floor.getLeft() <= point.getX() && point.getX() <= floor.getRight()) {
                if (floor.getTop() - 32 <= point.getY() && point.getY() <= floor.getTop()) {
                    return floor;
                }
            }
        }
        return null;
    }

    Ladder ladderOnPoint(Point point) {
        for (Ladder ladder : map.getLadders()) {
            if (ladder.getLeft() <= point.getX() && point.getX() <= ladder.getLeft() + 32) {
                if (ladder.getPeek() >= point.getY() && point.getY() >= ladder.getBottom()) {
                    return ladder;
                }
            }
        }
        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'q':
                suwak = 0;
                break;
            case 'w':
                suwak = 1;
                break;
            case 'e':
                suwak = 2;
                break;
            case 'r':
                suwak = 3;
                break;
            case 't':
                suwak = 4;
                break;
            case 's':
                operation = Operation.CreateFloor;
                break;
            case 'd':
                operation = Operation.CreateLadder;
                break;
            case 'x':
                operation = Operation.Delete;
                break;

            case 'b':
                clearSelection();
                break;

            case 'g':
                map.add(new Floor(0, 32, (int) Math.ceil(map.getWidth() / 32.0)));
                break;
        }
        hor = e.isShiftDown();
        vert = e.isControlDown();
        refreshDraw();
        updateTitle();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        hor = e.isShiftDown();
        vert = e.isControlDown();
        refreshDraw();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: // left click

                switch (operation) {
                    case None:
                        Floor f = floorOnPoint(mouse.invertY(panelHeight));
                        Ladder l = ladderOnPoint(mouse.invertY(panelHeight));
                        if (!e.isShiftDown()) {
                            select(f);
                            select(l);
                        } else {
                            toggleSelected(f);
                            toggleSelected(l);
                        }
                        break;

                    case CreateFloor:
                        snapMouse();
                        Floor floor2 = new Floor(mouse.getX() - floorTiles * 16, panelHeight - mouse.getY() + 16, floorTiles);
                        map.add(floor2);
                        if (AutoSelectOnCreate) {
                            selectOnly(floor2);
                        }
                        if (!PersistOperation) {
                            operation = Operation.None;
                        }
                        break;

                    case CreateLadder:
                        snapMouse();
                        Ladder ladder2 = new Ladder(mouse.getX() - 12, panelHeight - mouse.getY() - ladderTiles * 32 + 16, ladderTiles);
                        map.add(ladder2);
                        if (AutoSelectOnCreate) {
                            selectOnly(ladder2);
                        }
                        if (!PersistOperation) {
                            operation = Operation.None;
                        }
                        break;

                    case Delete:
                        Floor floor3 = floorOnPoint(mouse.invertY(panelHeight));
                        Ladder ladder3 = ladderOnPoint(mouse.invertY(panelHeight));
                        if (floor3 != null) {
                            map.delete(floor3);
                            selectedFloors.remove(floor3);
                        }
                        if (ladder3 != null) {
                            map.delete(ladder3);
                            selectedLadders.remove(ladder3);
                        }
                        map.accept();
                        if (!PersistOperation) {
                            operation = Operation.None;
                        }
                        break;
                }
                break;
            case MouseEvent.BUTTON3: // right click
                operation = Operation.None;
                break;
        }
        map.accept();
        SaveManager.Map.save(map);
        refreshDraw();
        updateTitle();
    }

    Floor takingFloor;
    Ladder takingLadder;

    @Override
    public void mousePressed(MouseEvent e) {
        takingFloor = floorOnPoint(mouse.invertY(panelHeight));
        takingLadder = ladderOnPoint(mouse.invertY(panelHeight));
        if (takingFloor != null) {
            floorTiles = takingFloor.getTiles();
        }
        if (takingLadder != null) {
            ladderTiles = takingLadder.getHeightTiles();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (takingFloor != null || takingLadder != null) {
            mouseClicked(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (takingFloor != null) {
            operation = Operation.CreateFloor;
            map.delete(takingFloor);
            selectedFloors.remove(takingFloor);
        } else if (takingLadder != null) {
            operation = Operation.CreateLadder;
            map.delete(takingLadder);
            selectedLadders.remove(takingLadder);
        }
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.x = e.getX() + draw.getCamera().getX();
        mouse.y = e.getY() - draw.getCamera().getY();

        hoveredFloor = floorOnPoint(mouse.invertY(panelHeight));
        hoveredLadder = ladderOnPoint(mouse.invertY(panelHeight));

        updateTitle();
        refreshDraw();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rot = -(int) Math.signum(e.getWheelRotation());
        switch (suwak) {
            case 4:
                int cameraX = draw.getCamera().getX();
                int cameraY = draw.getCamera().getY();

                mouse.x = e.getX() + cameraX;
                mouse.y = e.getY() - cameraY;

                if (hor || vert) {
                    if (hor = e.isShiftDown()) {
                        cameraX += rot * 16;
                    }
                    if (vert = e.isControlDown()) {
                        cameraY -= rot * 16;
                    }
                    draw.useCamera(new Camera(cameraX, cameraY));
                } else {
                    switch (operation) {
                        case CreateFloor:
                            floorTiles = Math.max(1, Math.min(floorTiles - rot, 18));
                            break;
                        case CreateLadder:
                            ladderTiles = Math.max(3, Math.min(ladderTiles - rot, 8));
                            break;
                    }
                }
                updateTitle();
                refreshDraw();
                break;
            case 0: // accurancy
                acc = Math.min(12.0, Math.max(0.5, acc + rot * 0.2));
                break;
            case 1: // strech
                tileSize = Math.min(14, Math.max(1, tileSize + rot));
                buildBlockTiles();
                break;
            case 2: // blur
                matrixSize = Math.min(10, Math.max(1, matrixSize + rot));
                buildMatrix();
                break;
            case 3: // radius
                VIEW_RADIUS = Math.min(700 / tileSize, Math.max(0, VIEW_RADIUS + rot * 4));
                break;
        }
    }


    @Override
    public void windowClosing(WindowEvent e) {
        JOptionPane.showMessageDialog(null, "The map is saved automatically, " +
                        "you can shutdown the program",
                "Map Designer", INFORMATION_MESSAGE);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}

