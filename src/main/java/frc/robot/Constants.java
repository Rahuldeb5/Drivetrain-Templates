// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public interface Swerve {
    double WHEEL_RADIUS = Units.inchesToMeters(2);

    double WIDTH = 18.75;
    double LENGTH = 18.75;

    int GYRO_ID = 9;

    double MAX_SPEED = .5;
    double MAX_ROTATION = Math.PI / 2;

    SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
      new Translation2d[] {
        new Translation2d(WIDTH / 2, LENGTH / 2),
        new Translation2d(WIDTH / 2, -LENGTH / 2),
        new Translation2d(-WIDTH / 2, LENGTH / 2),
        new Translation2d(-WIDTH / 2, -LENGTH / 2)
      }
    );

    public interface Drive {
      double kP = 0;
      double kI = 0;
      double kD = 0;

      double kS = .3;
      double kV = 2.4;
      double kA = 0.07;

      double GEAR_RATIO = 5.36;

      double driveCurrentLimit = 40.0;
    }

    public interface Turn {
      double kP = 3;
      double kI = 0;
      double kD = 0.04;
    }

    public interface FrontLeft {
      int DRIVE_ID = 16;
      int TURN_ID = 15;
      int ENCODER_ID = 4; 
      Rotation2d ANGLE_OFFSET = Rotation2d.fromDegrees(125.419922);
      boolean INVERTED = true;
    }

    public interface FrontRight {
      int DRIVE_ID = 10;
      int TURN_ID = 17;
      int ENCODER_ID = 1; 
      Rotation2d ANGLE_OFFSET = Rotation2d.fromDegrees(-159.609375);
      boolean INVERTED = false;
    }
    
    public interface BackLeft {
      int DRIVE_ID = 14;
      int TURN_ID = 13;
      int ENCODER_ID = 3;
      Rotation2d ANGLE_OFFSET = Rotation2d.fromDegrees(-83.847656);
      boolean INVERTED = true;
    }
    
    public interface BackRight {
      int DRIVE_ID = 12;
      int TURN_ID = 11;
      int ENCODER_ID = 2; 
      Rotation2d ANGLE_OFFSET = Rotation2d.fromDegrees(40.605469);
      boolean INVERTED = false;
    }

    public interface AutoConstants {
      double kMaxSpeed = .5;
      double kMaxAcceleration = 1.0;

      double kPX = .5;
      double kPY = .5;
      double kPTheta = .5;

      double kMaxAngularSpeed = .1;
      double kMaxAngularAcceleration = .1;
    }
  }
}
