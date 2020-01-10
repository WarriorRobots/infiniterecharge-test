/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  static final int ID_SHOOTER = 1;
  static TalonSRX shooter;

  static final int CLICKS_PER_REV = 4096; // https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#sensor-resolution
  static final double ESTIMATED_VOLTAGE = 1; // Typical motor output as percent
  static final int NATIVE_MAX_VELOCITY = 9326; // Native units per 100ms at typical motor output TODO calculate native units / 100ms at 100%

  static final double MAX_VELOCITY = 4400; // guess from observations on 20/01/09
  static double RPM=0;
  static double voltage=0;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    SmartDashboard.putNumber("Voltage", 0);
    // SmartDashboard.putNumber("Desired RPM", 0);
    shooter = new TalonSRX(ID_SHOOTER);
    shooter.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    shooter.setSensorPhase(false);
    // shooter.config_kF(0, ESTIMATED_VOLTAGE*1023/NATIVE_MAX_VELOCITY, 10); // https://phoenix-documentation.readthedocs.io/en/latest/ch16_ClosedLoop.html#calculating-velocity-feed-forward-gain-kf
    // shooter.config_kP(0, .1, 10);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // RPM = SmartDashboard.getNumber("Desired RPM", 0);
    SmartDashboard.putNumber("Encoder", shooter.getSelectedSensorPosition());
    SmartDashboard.putNumber("Native units/100ms", shooter.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Actual RPM", shooter.getSelectedSensorVelocity(0)*600/CLICKS_PER_REV);
    // (native / 100ms) * (600ms / m) * (rev/native) = rev / m

    voltage = SmartDashboard.getNumber("Voltage", 0);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // shooter.set(ControlMode.Velocity, RPM);
    shooter.set(ControlMode.PercentOutput, voltage);
  }
}
