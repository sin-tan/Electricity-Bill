package electricity.billing;
import javax.swing.*;
import java.awt.*;
public class Splash extends JFrame{
    //to display frame apna code constructor banake uske andar likho
    Splash()
    {
        JFrame frame=new JFrame();
        setSize(700,800);
        frame.setVisible(true);
        //Image nahi ho pa rahi hai add
        ImageIcon icon=new ImageIcon(ClassLoader.getSystemResource("iconIm.png"));
       Image ic=icon.getImage().getScaledInstance(700,500,Image.SCALE_DEFAULT);
       ImageIcon scaledIcon = new ImageIcon(ic);
        JLabel image=new JLabel(icon);
        image.setBounds(0, 0, 700, 500); // Set the size and position of the label
       add(image);
       setVisible(true);

    }
    public static void main(String args[])
    {
        new Splash();//a constructor is called
    }

}
