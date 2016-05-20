package paintapp;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class paintApp extends JFrame{

    JButton brush, line, ellipse, rect, stroke, fill;

    //monitors what shape to draw
    int currentAction = 1;

    Color strokeColor = Color.BLACK, fillColor = Color.black;

    //main class
    public static void main(String[] args) {

        new paintApp();
       
    }


    //logic
    public paintApp(){

        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        JPanel buttonPanel = new JPanel();
       //box that will hold all the buttons
        Box theBox = Box.createHorizontalBox();

        //button icons, uses makeMeButtons method below
        brush = makeMeButtons ("C:\\Users\\Toshiba\\Pictures\\javaPics\\brush.png",1);
        line = makeMeButtons ("C:\\Users\\Toshiba\\Pictures\\javaPics\\Line.png",2);
        ellipse = makeMeButtons ("C:\\Users\\Toshiba\\Pictures\\javaPics\\Ellipse.png",3);
        rect = makeMeButtons ("C:\\Users\\Toshiba\\Pictures\\javaPics\\Rectangle.png",4);
        

        stroke = makeMeColorButtons ("C:\\Users\\Toshiba\\Pictures\\javaPics\\Stroke.png",5, true);
        fill = makeMeColorButtons ("C:\\Users\\Toshiba\\Pictures\\javaPics\\Fill.png",6, false);

        //adds buttons to box
        theBox.add(brush);
        theBox.add(line);
        theBox.add(ellipse);
        theBox.add(rect);
        
        theBox.add(stroke);
        theBox.add(fill);

        buttonPanel.add(theBox);
        //sets position of box containing button icons
        this.add(buttonPanel, BorderLayout.SOUTH);
        //sets position of drawing board
        this.add(new drawingBoard(), BorderLayout.CENTER);
        
        this.setVisible(true);

    }

    //creates buttons based on image
    public JButton makeMeButtons(String icon, final int actionNum ){

        JButton but = new JButton();
        Icon ic = new ImageIcon(icon);
        but.setIcon(ic);

        but.addActionListener(new ActionListener(){
        //sets current action integer so each individual buttons action can be performed correctly
            public void actionPerformed (ActionEvent e){
                currentAction = actionNum;
            }
        });

        return but;
    }
//creates buttons for stroke and fill
    public JButton makeMeColorButtons(String icon, final int actionNum, final boolean stroke ){

        JButton but = new JButton();
        Icon ic = new ImageIcon(icon);
        but.setIcon(ic);

        but.addActionListener(new ActionListener(){

            //lets you choose a color for stroke and fill
            public void actionPerformed (ActionEvent e){
                if (stroke){
                    strokeColor = JColorChooser.showDialog(null, "Pick a Stroke",Color.BLACK);
                }else {
                   fillColor = JColorChooser.showDialog(null, "Pick a Fill",Color.BLACK);
                }
            }
        });

        return but;
    }


    private class drawingBoard extends JComponent{

     
     
        //creates an array list which monitors a drawn shapes stroke and fill
        ArrayList<Shape> shape = new ArrayList<Shape>();
        ArrayList<Color> shapeFill = new ArrayList<Color>();
        ArrayList<Color> shapeStroke = new ArrayList<Color>();



        Point mouseStart, mouseEnd;
        //monitors drawing board
        public drawingBoard() {


            this.addMouseListener(new MouseAdapter(){
                //when mouse is pressed get X and Y coordinates
                public void mousePressed(MouseEvent e){
                    mouseStart = new Point(e.getX(), e.getY());
                    mouseEnd = mouseStart;
                    repaint();
                }

                public void mouseReleased(MouseEvent e){

                    if (currentAction !=1 ){

                       Shape aShape = null;

                    //creates the shape by getting the starting x & y and finishing x and y positions
                    if (currentAction ==2){
                        aShape = drawLine (mouseStart.x, mouseStart.y, e.getX(), e.getY());
                    }
                    if(currentAction ==4){
                     aShape = drawRectangle( mouseStart.x, mouseStart.y,e.getX(), e.getY());
	             	              
                    }
                    else
                    if (currentAction ==3) {
                         aShape = drawEllipse( mouseStart.x, mouseStart.y,  e.getX(), e.getY());
                         
                    }
                   


                     //adds the shape, fill color and stroke color to array list
                    shape.add (aShape);
                    shapeFill.add(fillColor);
                    shapeStroke.add(strokeColor);

                    mouseStart = null;
                    mouseEnd = null;

                    repaint();

                }
                }
            });

            this.addMouseMotionListener(new MouseMotionAdapter(){
                //get the x and y positions after the mouse is dragged, used for the brush function
                public void mouseDragged(MouseEvent e){
                    if (currentAction ==1){

                    int x = e.getX();
                    int y = e.getY();

                    Shape aShape =null;

                    aShape = drawBrush(x,y,5,5);
                    shape.add (aShape);
                    shapeFill.add(fillColor);
                    shapeStroke.add(strokeColor);

                    mouseEnd = new Point (e.getX(), e.getY());
                    repaint();
                }
                }
            });
        }

        @Override
        public void paint (Graphics g){

        //defines shapes to be drawn
            Graphics2D graphSettings = (Graphics2D)g;


            
            //defines line width of drawn shape
            graphSettings.setStroke(new BasicStroke(4));
            //cycles through stroke and fills
            Iterator<Color> strokeCounters = shapeStroke.iterator();
            Iterator<Color> fillCounters = shapeFill.iterator();

            for (Shape s: shape){
                //sets the next stroke from above array list
                graphSettings.setPaint(strokeCounters.next());
                graphSettings.draw(s);
                //sets the next fill from above array
               graphSettings.setPaint(fillCounters.next());
                graphSettings.fill(s);
            }


        }

        private Rectangle2D.Float drawRectangle (int x1, int y1, int x2, int y2){
            //logic for creating rectangles
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);

            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);

            return new Rectangle2D.Float(x, y, width, height);

        }
 
        private Ellipse2D.Float drawEllipse (int x1, int y1, int x2, int y2){
            //logic for creating ellipses
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);

            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);

            return new Ellipse2D.Float(x, y, width, height);


    }

        private Line2D.Float drawLine (int x1, int y1, int x2, int y2){
            //logic for drawing lines
            return new Line2D.Float(x1 ,y1, x2, y2);
        }

        private Ellipse2D.Float drawBrush (int x1, int y1, int width, int height){
            //logic for drawing with the brush
            return new Ellipse2D.Float(x1 ,y1, width, height);
        }


    }


}
