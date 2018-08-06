import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class PictureSelector {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    class ImageZoom {
        public int realWidth, realHeight;
        public int drawWidth, drawHeight;
        public int drawStartX, drawStartY;

        public ImageZoom(int w, int h) {
            realHeight = h;
            realWidth = w;
            System.out.println(String.format("image1 w:%d, image1 h:%d",realWidth,realHeight));
        }

        public void setWindowsSize(int w, int h) {
            if (h == 0 || realHeight == 0) {
                return;
            }
            double realRate = (double) realWidth / realHeight;
            double windowRate = (double) w / h;
            System.out.println(String.format("image w:%d, image h:%d, image rate:%f, windows w:%d window h:%d window rate:%f",
                    realWidth,realHeight,realRate,
                    w,h,windowRate));
            if (w >= realWidth && h >= realHeight) {
                drawStartX = (w - realWidth) / 2;
                drawStartY = (h - realHeight) / 2;
                drawWidth = realWidth;
                drawHeight = realHeight;
            } else if (realRate < windowRate) {
                // example,  readlw 3 readh 3, rate 1, windows w 2 windows h 1, rate 2
                System.out.println("realRate < windowRate");
                drawHeight = h;
                drawWidth = (int) (drawHeight * realRate);
                drawStartY = 0;
                drawStartX = (w - drawWidth) / 2;
            } else {
                // example,  readlw 3 readh 3, rate 1, windows w 2 windows h 4, rate 0.5
                System.out.println("realRate > windowRate");
                drawWidth = w;
                drawHeight = (int) (drawWidth / realRate);
                drawStartX = 0;
                drawStartY = (h - drawHeight) / 2;
            }
        }
    }

    class ImagePanel extends JPanel {
        private int initWidth, initHeight;



        public void setRealSize(int w,int h){
            initHeight = h;
            initWidth = w;
            System.out.println(String.format("image panel set size:w:%d, h:%d",w,h));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            try {
                BufferedImage myPicture = read(new File(imagePath));
                System.out.println(String.format("image w:%d, image h:%d",myPicture.getWidth(),myPicture.getHeight()));
                ImageZoom imageZoom=new ImageZoom(myPicture.getWidth(),myPicture.getHeight());
                imageZoom.setWindowsSize(initWidth,initHeight);
                System.out.println(String.format("x:%d, y:%d; w:%d,h:%d", imageZoom.drawStartX, imageZoom.drawStartY, imageZoom.drawWidth, imageZoom.drawHeight));
                g.drawImage(myPicture, imageZoom.drawStartX, imageZoom.drawStartY, imageZoom.drawWidth, imageZoom.drawHeight, this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    String imagePath = "E:/picture/macro_1/IMG_1761.JPG";



    JTextField imageFolderTf=new JTextField();
    JButton loadBt =new JButton("Load");
    JLabel infoLineLb=new JLabel();
    JButton previousBt=new JButton("<-");
    JButton nextBt=new JButton("->");
    JCheckBox keepBox=new JCheckBox();
    JPanel mainPanel=new JPanel();
    JPanel controlPanel=new JPanel();
    ImagePanel imagePanel=new ImagePanel();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    Dimension frameSize;
    private void createAndShowGUI() throws IOException {
        //Create and set up the window.
        final JFrame frame = new JFrame("Pictures selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        //mainLayout.setLayout(new FlowLayout());
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));

        loadBt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.out.println(String.format(e.getActionCommand()));
            }
        });
        imageFolderTf.setColumns(40);
        imageFolderTf.setMaximumSize(new Dimension(200,20));
        controlPanel.add(imageFolderTf);
        controlPanel.add(loadBt);
        controlPanel.add(infoLineLb);
        controlPanel.add(previousBt);
        controlPanel.add(nextBt);

        controlPanel.add(keepBox);
        mainPanel.add(controlPanel);
        mainPanel.add(imagePanel);
        frame.getContentPane().add(mainPanel);
        frame.setSize(new Dimension(screenSize.width-100, screenSize.height-100));
        frame.setVisible(true);
        frameSize=frame.getSize();
        imagePanel.setRealSize(frameSize.width,frameSize.height-controlPanel.getHeight());

        frame.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                frameSize=frame.getSize();
                imagePanel.setRealSize(frameSize.width,frameSize.height-controlPanel.getHeight());
            }

            public void componentMoved(ComponentEvent e) {

            }

            public void componentShown(ComponentEvent e) {

            }

            public void componentHidden(ComponentEvent e) {

            }
        });
    }



    public void run(){
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
    public static void main(String[] args) {
        new PictureSelector().run();
    }
}
