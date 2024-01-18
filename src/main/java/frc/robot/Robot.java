// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  //Object initialization
  private final XboxController controller = new XboxController(0);

  private final CANSparkMax frontLeft = new CANSparkMax(0, MotorType.kBrushless);
  private final CANSparkMax frontRight = new CANSparkMax(0, MotorType.kBrushless);
  private final CANSparkMax rearLeft = new CANSparkMax(0, MotorType.kBrushless);
  private final CANSparkMax rearRight = new CANSparkMax(0, MotorType.kBrushless);

  private final DifferentialDrive drive = new DifferentialDrive(frontLeft, frontRight);

  private final RelativeEncoder leftEncoder = frontLeft.getEncoder();
  private final RelativeEncoder rightEncoder = frontRight.getEncoder();
  private final double revToWheel = 198/2108;
  private final double revToFoot = 1188 * Math.PI / 25296;
  
  private final WPI_Pigeon2 gyro = new WPI_Pigeon2(6);
  private double gyroSetpoint = 0;

  @Override
  public void robotInit() {
    rearLeft.follow(frontLeft);
    rearRight.follow(frontRight);
    frontLeft.setInverted(true);

    drive.setMaxOutput(0.2);

    leftEncoder.setPositionConversionFactor(revToFoot);
    rightEncoder.setPositionConversionFactor(revToFoot);
  }

  @Override
  public void robotPeriodic() {
    double distance = (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
    SmartDashboard.putNumber("Encoder Left", leftEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Right", rightEncoder.getPosition());
  }

  @Override
  public void autonomousInit() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    gyro.setYaw(0);
  }

  @Override
  public void autonomousPeriodic() {
    //Auto-correcting to angle
    double gyroError = gyroSetpoint - gyro.getAngle();
    drive.tankDrive(-gyroError * 0.04, gyroError * 0.04);
  }

  @Override
  public void teleopInit() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  @Override
  public void teleopPeriodic() {
    drive.arcadeDrive(-controller.getLeftY(), -controller.getRightX());
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
