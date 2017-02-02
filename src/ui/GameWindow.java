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

public class GameWindow extends JFrame implements IWorldUpdateObserver,
        KeyListener, MouseMotionListener, MouseListener {
    private ClientWorld world;

    private final IKeyStateNotifier keyStateNotifier;

    private final Renderer renderer;
    private Graphics windowGraphics;
    private final Size windowSize;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;

    GameWindow(IKeyStateNotifier keyStateNotifier, Size windowSize) {
        this.windowSize = windowSize;
        renderer = new Renderer(new Size(VIEW_WIDTH, VIEW_HEIGHT));

        this.keyStateNotifier = keyStateNotifier;
    }

    IRenderObserver getRenderObserver() {
        return this.renderer;
    }

    void attachWorld(ClientWorld world) {
        this.world = world;
        renderer.attachWorld(world);
    }

    void showGameWindow() {
        this.setTitle("Game");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        if (Application.RunOptions.isUsed("-ForceFullscreen")) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

            if (gd.isFullScreenSupported()) {
                this.setUndecorated(true);
                gd.setFullScreenWindow(this);
            } else {
                System.err.println("Full screen not supported.");
            }
        } else {
            if (Application.RunOptions.isUsed("-Windowed")) {
                this.setSize(windowSize.getWidth(), windowSize.getHeight());
                JPanel viewport = new JPanel();
                viewport.setSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
                this.getContentPane().add(viewport);

                this.setVisible(true);
                windowGraphics = viewport.getGraphics();
            } else {
                this.setSize(windowSize.getWidth(), windowSize.getHeight());
                this.setUndecorated(true);
                this.setVisible(true);
                windowGraphics = this.getGraphics();
            }
        }

        renderer.setScalingTo(new Size(this.getWidth(), this.getHeight()));
        renderer.drawOn(windowGraphics);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    private KeysState keysState = new KeysState();
    private KeysState prevKeysState = new KeysState();
    private boolean left, right, prevLeft, prevRight;

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
    
    /*
       ControlAction and Mouse Events
    */

    @Override
    public void keyTyped(KeyEvent e) {
        if (Chat.textFieldShown()) {
            Chat.keyPressed(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (!Chat.toggleTextField()) {
                    if (!Chat.textEmpty()) {
                        keyStateNotifier.sendTextMessage(Chat.getText());
                        Chat.showHistory();
                        Chat.clearText();
                    }
                }
                return;
            case KeyEvent.VK_ESCAPE:
                if (Chat.textEmpty()) {
                    Chat.hideTextField();
                } else {
                    Chat.clearText();
                }
                return;
        }
        if (Chat.textFieldShown()) return;
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


    /**
     * ControlAction released.
     *
     * @param e event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (Chat.textFieldShown()) {
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
