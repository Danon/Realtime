package ui.gfx;

import app.Application;
import gameplay.Character;
import gameplay.*;
import gameplay.Point;
import gameplay.Rectangle;
import ui.Chat;
import util.Size;

import java.awt.*;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public final class Renderer implements IRenderObserver {
    private final Drawer draw;
    private final Graphics2D canvas;
    private final BufferedImage backBuffer;
    private final MasterOfShadows zed;

    private Graphics graphics;
    private Insets displaySize;
    private final Size viewSize;
    private Size windowSize;
    private ClientWorld world;

    private FontMetrics fontMetrics;

    public static int
            CAMERA_SIDE_MARGIN = 0,
            CAMERA_BOTTOM_MARGIN = 0,
            CAMERA_TOP_MARGIN = 0;

    public Renderer(Size viewSize) {
        backBuffer = new BufferedImage(viewSize.getWidth(), viewSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
        canvas = backBuffer.createGraphics();

        this.viewSize = viewSize;
        this.draw = new Drawer(viewSize.getWidth(), viewSize.getHeight());

        Font defaultFont = new Font("Arial", Font.PLAIN, 15);
        canvas.setFont(defaultFont);
        fontMetrics = canvas.getFontMetrics(defaultFont);
        draw.setCanvas(canvas);

        zed = new MasterOfShadows();
    }

    public void setScalingTo(Size size) {
        this.windowSize = size;
        this.displaySize = this.getDisplaySize();
    }

    public void drawOn(Graphics graphics) {
        this.graphics = graphics;
    }

    public void attachWorld(ClientWorld world) {
        this.world = world;
        prepareMasterOfShadows();
    }

    private void prepareMasterOfShadows() {
        zed.setBounds(0, 0, viewSize.getWidth(), viewSize.getHeight() + Floor.HEIGHT);
        for (Floor floor : world.getMap().getFloors()) {
            zed.addObstacle(floor.asShape());
        }
    }

    private Insets getDisplaySize() {
        int displayLeft, displayTop, displayWidth, displayHeight;

        if (Application.RunOptions.isUsedWithValue("-View", "Stretch")) {
            displayWidth = windowSize.getWidth();
            displayHeight = windowSize.getHeight();
        } else if (Application.RunOptions.isUsedWithValue("-View", "Normal")) {
            displayWidth = viewSize.getWidth();
            displayHeight = viewSize.getHeight();
        } else //if (Application.RunOptions.isUsedWithValue("-View", "Fit"))
        {
            displayWidth = windowSize.getWidth();
            displayHeight = windowSize.getHeight();

            double windowRatio = (double) windowSize.getWidth() / (double) windowSize.getHeight();
            double viewRatio = (double) viewSize.getWidth() / (double) viewSize.getHeight();

            if (windowRatio > viewRatio) {
                displayWidth = (int) ((double) windowSize.getHeight() * viewRatio);
            } else {
                displayHeight = (int) ((double) windowSize.getWidth() * 1 / viewRatio);
            }
        }

        displayLeft = (windowSize.getWidth() - displayWidth) / 2;
        displayTop = (windowSize.getHeight() - displayHeight) / 2;

        return new Insets(
                displayTop,
                displayLeft,
                displayHeight + displayTop,
                displayWidth + displayLeft
        );
    }

    private void drawToWindow() {
        graphics.drawImage(backBuffer,
                displaySize.left, displaySize.top, displaySize.right, displaySize.bottom,
                0, 0, viewSize.getWidth(), viewSize.getHeight(), null);
    }

    @Override
    public void render(gameplay.Character[] characters, int playerCharacterId) {
        draw.freeCamera();
        canvas.setColor(Color.black);
        draw.fill(new Rectangle(0, 0, viewSize.getWidth(), viewSize.getHeight()));

        Point playerPos = getPlayerPosition(characters, playerCharacterId);

        setRaycastingFieldClip(playerPos.addY(60));

        canvas.setColor(Color.decode("#549CAE"));
        draw.fill(new Rectangle(0, 0, viewSize.getWidth(), viewSize.getHeight()));

        Rectangle borders = new Rectangle(
                0, 0,
                viewSize.getWidth(),
                viewSize.getHeight());

        if (characters.length > 0) {
            draw.useCamera(playerPos.sub(viewSize.getWidth() / 2, viewSize.getHeight() / 2));
        } else {
            draw.useCamera(new Point(0, 0));
        }

        draw.getCamera().cap(borders, viewSize.getWidth(), viewSize.getHeight());

        drawBackground(borders);

        canvas.setClip(0, 0, displaySize.right, displaySize.bottom);

        drawFloors(world.getMap());
        drawLadders(world.getMap());

        for (gameplay.Character character : characters) {
            drawCharacter(character);
        }

        draw.freeCamera();

        if (Chat.historyShown()) {
            int height = 40;
            for (String message : Chat.preparedMessages) {
                draw.text("dupa:" + message, new Point(15, height += 18));
            }
        }
        if (Chat.textFieldShown()) {
            draw.borders(new Rectangle(10, viewSize.getHeight() - 40, 400, 20));
            draw.text(Chat.getText(), new Point(17, viewSize.getHeight() - 24));
            int textWidth = fontMetrics.stringWidth(Chat.getText());
            draw.line(new Point(15 + textWidth + 4, viewSize.getHeight() - 24),
                    new Point(15 + textWidth + 4, viewSize.getHeight() - 36)
            );
        }

        draw.useCamera();

        canvas.setColor(Color.red);

        drawToWindow();
    }

    private void drawCharacter(Character character) {
        String animationName;
        int frameI;
        if (character.isOnTheGround()) {
            if (character.isWalking()) {
                animationName = "run";
                frameI = character.common.runFrame;
            } else if (character.isBasicAttack()) {
                animationName = "basic";
                frameI = character.common.basicFrame;
            } else if (character.isShooting()) {
                animationName = "shooting";
                frameI = character.common.shootFrame;
            } else {
                animationName = "idle";
                frameI = 0;
            }
        } else {
            if (character.isClimbing()) {
                animationName = "climbig";
                frameI = character.common.climbFrame;
            } else if (character.isBasicAttack()) {
                animationName = "basic-air";
                frameI = character.common.basicFrame;
            } else if (character.isShooting()) {
                animationName = "midair-gun";
                frameI = character.common.shootFrame;
            } else {
                animationName = "midair";
                frameI = character.isJumping() ? character.common.jumpFrame : FrameAnimation.Speed.MidAir * 5;
            }
        }
        draw.frame(
                Resources.spritesheet.animation(animationName).getFrameIterate(frameI),
                character.getPosition().invertY(viewSize.getHeight()),
                DrawFrom.RightBottom,
                character.isTurnedRight() ? Flip.None : Flip.Horizontally
        );

        draw.text(String.format("%s %d", character.getDisplayName(), character.shared.hp),
                character.getPosition().invertY(viewSize.getHeight()).sub(50, 60)
        );
        Rectangle hpBar = new Rectangle(character.getPosition().invertY(viewSize.getHeight()).sub(20, 80), 50, 6);
        canvas.setColor(Color.red);
        draw.borders(hpBar);
        hpBar.width = (int) Math.round(hpBar.width * ((double) character.getHp() / 500.0));

        draw.fill(hpBar);
    }

    private void drawLadders(GameMap worldMap) {
        for (Ladder ladder : worldMap.getLadders()) {
            draw.ladder(ladder);
        }
    }

    private void drawFloors(GameMap worldMap) {
        for (Floor floor : worldMap.getFloors()) {
            draw.floor(floor);
        }
    }

    private void drawBackground(Rectangle borders) {
        for (int i = 0; i <= Math.ceil(borders.width / 167); i++) {
            draw.image("background.png", new Point(
                            CAMERA_SIDE_MARGIN + i * 167,
                            viewSize.getHeight() - borders.height),
                    DrawFrom.MiddleTop);
        }
    }

    private Point getPlayerPosition(Character[] characters, int playerCharacterId) {
        for (Character character : characters) {
            if (character.getCharacterId() == playerCharacterId) {
                return character.getPosition();
            }
        }
        throw new AssertionError("Index not contained in array.");
    }

    private Shape lightShape(Point player) {
        Point camera = draw.getCamera();
        final int mapHeight = viewSize.getHeight();

        Path2D shadowShape = new Path2D.Double();
        zed.getFieldOfView(player, new IntersectionIterable() {
            @Override
            public void iterateFirst(double x, double y) {
                shadowShape.moveTo(x - camera.getX(), mapHeight - y + camera.getY() + 32);
            }

            @Override
            public void iterateNext(double x, double y) {
                shadowShape.lineTo(x - camera.getX(), mapHeight - y + camera.getY() + 32);
            }
        });
        shadowShape.closePath();
        return shadowShape;
    }

    private void setRaycastingFieldClip(Point player) {
        canvas.setClip(lightShape(player));
    }
}
