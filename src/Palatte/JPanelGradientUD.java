
package Palatte;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class JPanelGradientUD extends JPanel {

    private Color colorStart = Color.decode("#0D41E1");
    private Color colorEnd = Color.decode("#09A6F3");
    
    public Color getColorStart() {
        return colorStart;
    }
    
    public void setColorStart(Color colorStart) {
        this.colorStart = colorStart;
    }
    
    public Color getColorEnd() {
        return colorEnd;
    }
    
    public void setColorEnd(Color colorEnd) {
        this.colorEnd = colorEnd;
    }
    
    public JPanelGradientUD() {
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
         0, 0, colorStart, 
        0, getHeight(), colorEnd 
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    
    
}
