package com.alvinalexander.mac.ui

import java.awt._
import java.awt.event._
import java.awt.geom.RoundRectangle2D
import javax.swing._
import javax.swing.SwingUtilities
import javax.swing.LayoutStyle.ComponentPlacement
import javax.swing.event.MouseInputListener
import com.jgoodies.forms.factories._
import com.jgoodies.forms.layout._

/**
 * Used to test AlsMacFrame. Should be a rectangular shape, with rounder corners,
 * with a black border around a white jpanel.
 */
object AlsMacFrameTester extends App {

    val whitePanel = new JPanel
    whitePanel.setBackground(Color.WHITE)
    whitePanel.setSize(new Dimension(400,300))
    whitePanel.setPreferredSize(new Dimension(400,300))
    val f = new AlsMacFrame(whitePanel)
    f.setBackgroundColor(Color.BLACK)
    f.setAlphaComposite(0.6f)
    f.pack
    f.setLocationRelativeTo(null)
    f.setVisible(true)

}


/**
 * Original code at http://marioyohanes.com/2008/10/31/translucent-custom-shape-window/
 * 
 * TODO Add window close listener.
 * TODO Add a way to set the title?
 * 
 */
class AlsMacFrame (mainPanel: JPanel) extends JFrame {
    
    // the caller should pass in their "main panel".
    //var mainPanel: JPanel = _
    var bottomPane: JComponent = _
    val cellConstraints = new CellConstraints
    var backgroundColor = new Color(0, 0, 0)
    var alphaComposite = 0.7f

    // border sizes
    // TODO link these together
    // TODO update the total size after the east, west, north, and south sizes are set
    var northRowHeightInt = 10
    var southRowHeightInt = 10
    var westColumnWidthInt = 10
    var eastColumnWidthInt = 10
    var northRowHeight = "10px"
    var southRowHeight = "10px"
    var westColumnWidth = "10px"
    var eastColumnWidth = "10px"

    // constructor stuff
    setUndecorated(true);
    setBackground(new Color(0,0,0,0))    // gets rid of colors in all four corners
    setContentPane(createContentPane)    // makes everything darker, adds height at bottom
    bottomPane = createBottomPane

    val contentPane = getContentPane
    contentPane.setLayout(new FormLayout(
        //new Array[ColumnSpec] {
        Array(
            new ColumnSpec(westColumnWidth),
            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
            new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
            new ColumnSpec(eastColumnWidth)
        ),
        //new Array[RowSpec] {
        Array(
            new RowSpec(northRowHeight),
            FormFactory.LINE_GAP_ROWSPEC,
            new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
            FormFactory.LINE_GAP_ROWSPEC,
            new RowSpec(southRowHeight)
        ))
    )

    contentPane.add(mainPanel, cellConstraints.xy(3, 3))
    contentPane.add(bottomPane, cellConstraints.xyw(1, 5, 5))

    // automatically calculate size
    val theWidth = mainPanel.getPreferredSize().width + eastColumnWidthInt + westColumnWidthInt
    val theHeight = mainPanel.getPreferredSize().width + northRowHeightInt + southRowHeightInt
    setSize(theWidth, theHeight)
    
    // make mainframe draggable
    val dwl = new DragWindowListener(this)
    this.addMouseListener(dwl)
    this.addMouseMotionListener(dwl)


    def createBottomPane: JComponent = {
        val fillerComponent = new JComponent {}
        fillerComponent.setLayout(new FlowLayout(FlowLayout.RIGHT))
        fillerComponent
    }

    def createContentPane =
        new JComponent() {
            override def paintComponent(g: Graphics){
                val g2 = g.create.asInstanceOf[Graphics2D]
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                val old = g2.getComposite
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaComposite))
                g2.setColor(backgroundColor)
                val shape = new RoundRectangle2D.Float(0, 0, getWidth, getHeight, 20, 20)
                g2.fill(shape)
                g2.setComposite(old)
                g2.dispose
            }
        }

    def setAlphaComposite(alphaComposite: Float) {
        this.alphaComposite = alphaComposite
    }

    def setBackgroundColor(backgroundColor: Color) {
        this.backgroundColor = backgroundColor
    }

}










