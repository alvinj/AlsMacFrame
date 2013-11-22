package com.alvinalexander.mac.ui

import java.awt._
import java.awt.event._
import javax.swing._

/**
 * I converted this code to Scala (quickly) from a Java project.
 * I'll add that project's URL here when I find it.
 */
class DragWindowListener(jFrame: JFrame) extends MouseListener with MouseMotionListener {

  var startDragPoint: Point = _
  var startLocPoint: Point = _

  def getScreenLocation(e: MouseEvent): Point = {
    val cursor = e.getPoint
    val targetLocation = jFrame.getLocationOnScreen
    return new Point( 
            (targetLocation.getX + cursor.getX).asInstanceOf[Int], 
            (targetLocation.getY + cursor.getY).asInstanceOf[Int] )
  }

  def mousePressed(e: MouseEvent) {
    if (!e.isAltDown && !e.isControlDown && !e.isMetaDown && !e.isShiftDown) {
      this.startDragPoint = this.getScreenLocation(e)
      this.startLocPoint = jFrame.getLocation
    }
  }

  def mouseDragged(e: MouseEvent) {
    // only work if there are no meta keys in use
    if (!e.isAltDown && !e.isControlDown && !e.isMetaDown && !e.isShiftDown) {
      val current = this.getScreenLocation(e)
      val offset = new Point(
              current.getX.asInstanceOf[Int] - startDragPoint.getX.asInstanceOf[Int], 
              current.getY.asInstanceOf[Int] - startDragPoint.getY.asInstanceOf[Int])
      val newLocation = new Point(
              (this.startLocPoint.getX + offset.getX).asInstanceOf[Int],
              (this.startLocPoint.getY + offset.getY).asInstanceOf[Int])
      jFrame.setLocation(newLocation);
    }
  }

  def mouseReleased(e: MouseEvent) {}
  def mouseMoved(e: MouseEvent) {}
  def mouseClicked(e: MouseEvent) {}
  def mouseEntered(e: MouseEvent) {}
  def mouseExited(e: MouseEvent) {}

}










