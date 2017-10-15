package ui;

import app.Application;
import gameplay.ClientWorld;
import network.KeysState;
import ui.gfx.IRenderObserver;
import ui.gfx.Renderer;
import util.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements IWorldUpdateObserver, KeyListener, MouseMotionListener, MouseListener {
    private static final int VIEW_WIDTH = 800, VIEW_HEIGHT = 600;

    private final Renderer renderer = new Renderer(new Size(VIEW_WIDTH, VIEW_HEIGHT));
    private final IKeyStateNotifier keyStateNotifier;
    private final Size windowSize;

    private ClientWorld world;
    private Chat chat;

    private KeysState keysState = new KeysState();
    private KeysState prevKeysState = new KeysState();
    private boolean left, right, prevLeft, prevRight;

    GameWindow(IKeyStateNotifier keyStateNotifier, Size windowSize, ClientWorld clientWorld, Chat chat) {
        this.windowSize = windowSize;

        this.keyStateNotifier = keyStateNotifier;

        this.world = clientWorld;
        renderer.attachWorld(world);

        this.chat = chat;
        renderer.attachChat(chat);
    }

    void showGameWindow() {
        this.setTitle("Realtime | Game playing...");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Graphics graphics = showAndGetWindowGraphics();
        renderer.setScalingTo(new Size(this.getWidth(), this.getHeight()));
        renderer.drawOn(graphics);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    IRenderObserver getRenderObserver() {
        return this.renderer;
    }

    private Graphics showAndGetWindowGraphics() {
        String type = Application.RunOptions.getText("-Display");

        if (type.equals("Fullscreen")) {
            return useFullscreen();
        }

        if (type.equals("Window")) {
            this.setSize(windowSize.getWidth(), windowSize.getHeight());
            return useWindowed();
        }

        throw new RuntimeException("Invalid display type");
    }

    private Graphics useFullscreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            this.setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full screen not supported.");
        }

        return this.getGraphics();
    }

    private Graphics useWindowed() {
        JPanel viewport = new JPanel();
        viewport.setSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
        this.getContentPane().add(viewport);

        this.setVisible(true);
        return viewport.getGraphics();
    }

    @Override
    public void worldUpdated() {
        // if keysState changed
        if ((!keysState.equals(prevKeysState)) || (prevLeft != left) || (prevRight != right)) {
            keyStateNotifier.sendCurrentMove(keysState, left, right);
            prevKeysState.set(keysState);
            prevLeft = left;
            prevRight = right;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (chat.isTextFieldShown()) {
            chat.keyPressed(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (!chat.toggleTextField()) {
                    if (!chat.isTextEmpty()) {
                        keyStateNotifier.sendTextMessage(chat.getText());
                        chat.showHistory();
                        chat.clearText();
                    }
                }
                return;
            case KeyEvent.VK_ESCAPE:
                if (chat.isTextEmpty()) {
                    chat.hideTextField();
                } else {
                    chat.clearText();
                }
                return;
        }
        if (chat.isTextFieldShown()) return;
        switch (e.getKeyChar()) {
            case 'w':
                keysState.Up = true;
                break;
            case 's':
                keysState.Down = true;
                break;
            case 'a':
                keysState.Left = true;
                break;
            case 'd':
                keysState.Right = true;
                break;
        }
        this.world.movePlayerCharacter(keysState); // client side interpolation
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (chat.isTextFieldShown()) {
            return;
        }
        switch (e.getKeyChar()) {
            case 'w':
                keysState.Up = false;
                break;
            case 's':
                keysState.Down = false;
                break;
            case 'a':
                keysState.Left = false;
                break;
            case 'd':
                keysState.Right = false;
                break;
        }
        this.world.movePlayerCharacter(keysState); // client side interpolation
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                left = true;
                break;
            case MouseEvent.BUTTON3:
                right = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                left = false;
                break;
            case MouseEvent.BUTTON3:
                right = false;
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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
}
