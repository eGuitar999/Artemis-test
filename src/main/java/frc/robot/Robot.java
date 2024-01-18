// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  private final CANSparkMax frontLeft = new CANSparkMax(0, MotorType.kBrushless);
  private final CANSparkMax frontRight = new CANSparkMax(0, MotorType.kBrushless);
  private final CANSparkMax rearLeft = new CANSparkMax(0, MotorType.kBrushless);
  private final CANSparkMax rearRight = new CANSparkMax(0, MotorType.kBrushless);

  private final RelativeEncoder leftEncoder = frontLeft.getEncoder();
  private final RelativeEncoder rightEncoder = frontRight.getEncoder();
  

  private final DifferentialDrive drive = new DifferentialDrive(frontLeft, frontRight);
  private final XboxController controller = new XboxController(0);

  @Override
  public void robotInit() {
    rearLeft.follow(frontLeft);
    rearRight.follow(frontRight);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Encoder Left", leftEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Right", rightEncoder.getPosition());
    double distance = (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);

  }

  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(-controller.getLeftY(), controller.getRightX());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
