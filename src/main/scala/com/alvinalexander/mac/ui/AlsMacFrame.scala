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
    f.setBackgroundColor(Color.WHITE)
    f.setAlphaComposite(0.96f)

    f.setTitle("Hello, world")
    f.setTitleColor(new Color(8, 8, 8))

    f.pack
    f.setLocationRelativeTo(null)
    f.setVisible(true)

}


/**
 * Original code at http://marioyohanes.com/2008/10/31/translucent-custom-shape-window/
 * 
 * This works if you run it with `sbt run`.
 * If/when you get NPEs about the images when running in Eclipse, use that command instead.
 * 
 * TODO Add window close listener.
 * TODO Add a way to set the title?
 * 
 */
class AlsMacFrame (mainPanel: JComponent) extends JFrame {
    
    // the caller should pass in their "main panel".
    //var mainPanel: JPanel = _
    var bottomPane: JComponent = _
    val cellConstraints = new CellConstraints
    var backgroundColor = new Color(0, 0, 0)
    var alphaComposite = 0.7f
    var cornerRadius = 20               // larger value makes corners more round

    // border row and columns sizes
    // TODO link these together
    // TODO update the total size after the east, west, north, and south sizes are set
    var northRowHeightInt = 40
    var southRowHeightInt = 10
    var westColumnWidthInt = 10
    var eastColumnWidthInt = 10
    var northRowHeight = "40px"
    var southRowHeight = "10px"
    var westColumnWidth = "10px"
    var eastColumnWidth = "10px"

    // upper-left corner icons
    val closeWindowImage = new ImageIcon(this.getClass.getResource("RedCircle.png"))
    val closeWindowHoverImage = new ImageIcon(this.getClass.getResource("RedCircleHover.png"))

    val minimizeWindowImage = new ImageIcon(this.getClass.getResource("OrangeCircle.png"))
    val minimizeWindowHoverImage = new ImageIcon(this.getClass.getResource("OrangeCircleHover.png"))

    val maximizeWindowImage = new ImageIcon(this.getClass.getResource("GreenCircle.png"))
    val maximizeWindowHoverImage = new ImageIcon(this.getClass.getResource("GreenCircleHover.png"))

    // upper-left corner icons
    val headerPanel = new HeaderPanel
    headerPanel.setOpaque(false)
    headerPanel.getButtonPanel.setOpaque(false)
    val closeWindowButton = headerPanel.getCloseButton
    val minimizeWindowButton = headerPanel.getMinimizeButton
    val maximizeWindowButton = headerPanel.getMaximizeButton
    configureUpperLeftCornerIconRolloverEffects

    // constructor stuff
    setUndecorated(true);
    setBackground(new Color(0,0,0,0))    // gets rid of colors in all four corners
    setContentPane(createContentPane)    // makes everything darker, adds height at bottom
    bottomPane = createBottomPane

    val contentPane = getContentPane
    contentPane.setLayout(new FormLayout(
        Array(
            new ColumnSpec(westColumnWidth),
            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
            new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
            new ColumnSpec(eastColumnWidth)
        ),
        Array(
            new RowSpec(northRowHeight),
            FormFactory.LINE_GAP_ROWSPEC,
            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
            FormFactory.LINE_GAP_ROWSPEC,
            new RowSpec(southRowHeight)
        ))
    )

    contentPane.add(headerPanel, cellConstraints.xyw(1, 1, 5))
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
                val shape = new RoundRectangle2D.Float(0, 0, getWidth, getHeight, cornerRadius, cornerRadius)
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
    
    override def setTitle(title: String) {
        this.headerPanel.getTitle.setText(title)
    }

    def setTitleColor(color: Color) {
        this.headerPanel.getTitle.setForeground(color)
    }

    def setTitleFont(f: Font) {
        this.headerPanel.setTitleFont(f)
    }

  def configureUpperLeftCornerIconRolloverEffects {
    closeWindowButton.setIcon(closeWindowImage)
    minimizeWindowButton.setIcon(minimizeWindowImage)
    maximizeWindowButton.setIcon(maximizeWindowImage)  

    closeWindowButton.addMouseListener(new MouseAdapter {
      override def mouseEntered(e: MouseEvent) { closeWindowButton.setIcon(closeWindowHoverImage) }
      override def mouseExited(e: MouseEvent) { closeWindowButton.setIcon(closeWindowImage) }
    })
    minimizeWindowButton.addMouseListener(new MouseAdapter {
      override def mouseEntered(e: MouseEvent) { minimizeWindowButton.setIcon(minimizeWindowHoverImage) }
      override def mouseExited(e: MouseEvent) { minimizeWindowButton.setIcon(minimizeWindowImage) }
    })
    maximizeWindowButton.addMouseListener(new MouseAdapter {
      override def mouseEntered(e: MouseEvent) { maximizeWindowButton.setIcon(maximizeWindowHoverImage) }
      override def mouseExited(e: MouseEvent) { maximizeWindowButton.setIcon(maximizeWindowImage) }
    })
  }

}










