import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class ShowOneImageWithItsSize {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    static class ImageZoom {
        public int realWidth, realHeight;
        public int drawWidth, drawHeight;
        public int drawStartX, drawStartY;

        public ImageZoom(int w, int h) {
            realHeight = h;
            realWidth = w;
        }

        public void setWindowsSize(int w, int h) {
            if (h == 0 || realHeight == 0) {
                return;
            }
            double realRate = (double) realWidth / realHeight;
            double windowRate = (double) w / h;
            if (w >= realWidth && h >= realHeight) {
                drawStartX = (w - realWidth) / 2;
                drawStartY = (h - realHeight) / 2;
                drawWidth = realWidth;
                drawHeight = realHeight;
            } else if (realRate > windowRate) {
                // example,  readlw 3 readh 3, rate 1, windows w 2 windows h 1, rate 2
                drawHeight = h;
                drawWidth = (int) (drawHeight * realRate);
                drawStartY = 0;
                drawStartX = (w - drawWidth) / 2;
            } else {
                drawWidth = w;
                drawHeight = (int) (drawWidth / realRate);
                drawStartX = 0;
                drawStartY = (h - drawHeight) / 2;
            }
        }
    }

    static class ImagePanel extends JPanel {
        private int initWidth, initHeight;

        public ImagePanel(int w, int h) {
            super();
            initHeight = h;
            initWidth = w;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            try {
                BufferedImage myPicture = read(new File(imagePath));
                System.out.println(String.format("w:%d,h:%d", initWidth, initHeight));
                ImageZoom imageZoom=new ImageZoom(myPicture.getWidth(),myPicture.getHeight());
                imageZoom.setWindowsSize(initWidth,initHeight);
                g.drawImage(myPicture, imageZoom.drawStartX, imageZoom.drawStartY, imageZoom.drawWidth, imageZoom.drawHeight, this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    static String imagePath = "E:/picture/macro_1/IMG_1761.JPG";

    private static void createAndShowGUI() throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int myWidth = screenSize.width - 2;
        int myHeight = screenSize.height - 2;
        ImagePanel imagePanel = new ImagePanel(myWidth, myHeight);

        frame.getContentPane().add(imagePanel);

        //Display the window.
        //frame.pack();

        frame.setSize(new Dimension(myWidth, myHeight));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
