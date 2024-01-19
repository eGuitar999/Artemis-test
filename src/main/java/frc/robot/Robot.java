// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {
  //Object initialization
  private final XboxController controller = new XboxController(0);
  //private final Timer timer = new Timer();

  private final CANSparkMax frontLeft = new CANSparkMax(4, MotorType.kBrushless);
  private final CANSparkMax frontRight = new CANSparkMax(3, MotorType.kBrushless);
  private final CANSparkMax rearLeft = new CANSparkMax(2, MotorType.kBrushless);
  private final CANSparkMax rearRight = new CANSparkMax(1, MotorType.kBrushless);

  private final DifferentialDrive drive = new DifferentialDrive(frontLeft, frontRight);

  private final RelativeEncoder leftEncoder = frontLeft.getEncoder();
  private final RelativeEncoder rightEncoder = frontRight.getEncoder();
  private final double revToWheel = 198/2108;
  private final double revToFoot = 1188 * Math.PI / 25296;
  
  private final WPI_Pigeon2 gyro = new WPI_Pigeon2(6);
  //private double gyroSetpoint = 0;

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");

  private double x;
  private double y;
  private double area;

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
    
    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    area = ta.getDouble(0.0);

    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
  }

  @Override
  public void autonomousInit() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    gyro.setYaw(0);

  /*
    timer.reset();
    timer.start();
  */
  }

  @Override
  public void autonomousPeriodic() {
    //Auto distance from tag, centring
    double errorArea = 7 - area;
    double errorX = x;

    double leftSpeed = errorArea * 0.5;// + errorX *0.5;
    double rightSpeed = errorArea * 0.5;// - errorX *0.5;

    drive.tankDrive(leftSpeed, rightSpeed);
  
  /*
    //Auto-correcting to angle
    double gyroError = gyroSetpoint - gyro.getAngle();
    drive.tankDrive(-gyroError * 0.04, gyroError * 0.04);
  */

  /*
    //Forward Auto (testing straight line)
    if (timer.get() < 2) {
    frontLeft.set(0.2);
    frontRight.set(0.2);
    } else {
      frontLeft.stopMotor();
      frontRight.stopMotor();
    }
  */
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
