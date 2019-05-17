package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockTS1;
  private TorpedoStore mockTS2;

  @BeforeEach
  public void init(){
    mockTS1=mock(TorpedoStore.class);
    mockTS2=mock(TorpedoStore.class);
    this.ship = new GT4500(mockTS1, mockTS2);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS1.fire(1)).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    verify(mockTS1, times(1)).fire(1);

    // Assert

  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange

    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS2.isEmpty()).thenReturn(false);
    when(mockTS1.fire(1)).thenReturn(true);
    when(mockTS2.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockTS1, times(1)).fire(1);
    verify(mockTS2, times(1)).fire(1);
  }
   /*
  Fire single while both torpedostores are empty, expected: false
  Fire single while only one torpedostore has bullet (the one in order), expected: true
  Fire single while only one torpedostore has bullet (the one that should rest), expected: true
  Fire all while only one torpedostore has bullet, expected: false
  Fire all while both torpedostores are empty, expected: false
   */

  @Test
  public void fireTorpedo_single_empty() {
    //Arrange
    when(mockTS1.isEmpty()).thenReturn(true);
    when(mockTS2.isEmpty()).thenReturn(true);
    when(mockTS1.fire(1)).thenReturn(false);
    when(mockTS2.fire(1)).thenReturn(false);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);

    //Verify
    verify(mockTS1,times(0)).fire(1);
    verify(mockTS2,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_single_oneBullet_in_order() {
    //Arrange
    ship.setWasPrimaryFiredLast(false);
    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS2.isEmpty()).thenReturn(true);
    when(mockTS1.fire(1)).thenReturn(true);
    when(mockTS2.fire(1)).thenReturn(false);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);

    //Verify
    verify(mockTS1,times(1)).fire(1);
    verify(mockTS2,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_single_oneBullet_out_of_order() {
    //Arrange
    ship.setWasPrimaryFiredLast(false);
    when(mockTS1.isEmpty()).thenReturn(true);
    when(mockTS2.isEmpty()).thenReturn(false);
    when(mockTS1.fire(1)).thenReturn(false);
    when(mockTS2.fire(1)).thenReturn(true);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);

    //Verify
    verify(mockTS1,times(0)).fire(1);
    verify(mockTS2,times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_all_oneBullet() {
    //Arrange
    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS2.isEmpty()).thenReturn(true);
    when(mockTS1.fire(1)).thenReturn(true);
    when(mockTS2.fire(1)).thenReturn(false);

    //Act
    ship.fireTorpedo(FiringMode.ALL);

    //Verify
    verify(mockTS1,times(0)).fire(1);
    verify(mockTS2,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_all_empty() {
    //Arrange
    when(mockTS1.isEmpty()).thenReturn(true);
    when(mockTS2.isEmpty()).thenReturn(true);
    when(mockTS1.fire(1)).thenReturn(false);
    when(mockTS2.fire(1)).thenReturn(false);

    //Act
    ship.fireTorpedo(FiringMode.ALL);

    //Verify
    verify(mockTS1,times(0)).fire(1);
    verify(mockTS2,times(0)).fire(1);
  }

  @Test
  public void check_PrimaryFiredLast() {
    //Arrange
    ship.setWasPrimaryFiredLast(false);
    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS2.isEmpty()).thenReturn(false);
    when(mockTS1.fire(1)).thenReturn(true);
    when(mockTS2.fire(1)).thenReturn(true);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.getWasPrimaryFiredLast();

    //Verify
    assertEquals(true,result,"PrimaryFiredLast");
  }

  @Test
  public void check_notPrimaryFiredLast() {
    //Arrange
    ship.setWasPrimaryFiredLast(true);
    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS2.isEmpty()).thenReturn(false);
    when(mockTS1.fire(1)).thenReturn(true);
    when(mockTS2.fire(1)).thenReturn(true);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.getWasPrimaryFiredLast();

    //Verify
    assertEquals(false,result,"PrimaryNotFiredLast");
  }

  @Test
  public void check_notPrimaryFiredLast_primaryBullet() {
    //Arrange
    ship.setWasPrimaryFiredLast(true);
    when(mockTS1.isEmpty()).thenReturn(false);
    when(mockTS2.isEmpty()).thenReturn(true);
    when(mockTS1.fire(1)).thenReturn(true);
    when(mockTS2.fire(1)).thenReturn(false);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.getWasPrimaryFiredLast();

    //Verify
    assertEquals(true,result,"PrimaryNotFiredLast_StillFired");
  }

  @Test
  public void test_SingleEmpty_PrimaryFired() {
    //Arrange
    ship.setWasPrimaryFiredLast(true);
    when(mockTS1.isEmpty()).thenReturn(true);
    when(mockTS2.isEmpty()).thenReturn(true);
    when(mockTS1.fire(1)).thenReturn(false);
    when(mockTS2.fire(1)).thenReturn(false);

    //Act
    ship.fireTorpedo(FiringMode.SINGLE);

    //Verify
    verify(mockTS1,times(0)).fire(1);
    verify(mockTS2,times(0)).fire(1);
  }


}
