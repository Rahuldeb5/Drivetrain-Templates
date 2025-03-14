package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Swerve;

public class SwerveDrive extends SubsystemBase {

    private final SwerveModule frontLeft, frontRight, rearLeft, rearRight;

    private final SwerveDriveOdometry odometry;

    private static final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
        new Translation2d(Units.inchesToMeters(Swerve.LENGTH / 2), Units.inchesToMeters(Swerve.WIDTH / 2)),
        new Translation2d(Units.inchesToMeters(Swerve.LENGTH / 2), Units.inchesToMeters(-Swerve.WIDTH / 2)),
        new Translation2d(Units.inchesToMeters(-Swerve.LENGTH / 2), Units.inchesToMeters(Swerve.LENGTH / 2)),
        new Translation2d(Units.inchesToMeters(-Swerve.LENGTH / 2), Units.inchesToMeters(-Swerve.LENGTH / 2))
    );

    private final Pigeon2 gyro;
    
    public SwerveDrive() {
        frontLeft = new SwerveModule(Swerve.FrontLeft.DRIVE_ID, Swerve.FrontLeft.TURN_ID, Swerve.FrontLeft.ENCODER_ID, Swerve.FrontLeft.ANGLE_OFFSET, Swerve.FrontLeft.INVERTED);
        frontRight = new SwerveModule(Swerve.FrontRight.DRIVE_ID, Swerve.FrontRight.TURN_ID, Swerve.FrontRight.ENCODER_ID, Swerve.FrontRight.ANGLE_OFFSET, Swerve.FrontRight.INVERTED);
        rearLeft = new SwerveModule(Swerve.BackLeft.DRIVE_ID, Swerve.BackLeft.TURN_ID, Swerve.BackLeft.ENCODER_ID, Swerve.BackLeft.ANGLE_OFFSET, Swerve.BackLeft.INVERTED);
        rearRight = new SwerveModule(Swerve.BackRight.DRIVE_ID, Swerve.BackRight.TURN_ID, Swerve.BackRight.ENCODER_ID, Swerve.BackRight.ANGLE_OFFSET, Swerve.BackRight.INVERTED);

        gyro = new Pigeon2(Swerve.GYRO_ID, "*");

        odometry = new SwerveDriveOdometry(kinematics, 
            gyro.getRotation2d(), 
            new SwerveModulePosition[] {
                frontLeft.getModulePosition(),
                frontRight.getModulePosition(),
                rearLeft.getModulePosition(),
                rearRight.getModulePosition()
            }
        );
    }

    public void drive(Translation2d velocity, double rotation) {
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            velocity.getX(),
            velocity.getY(),
            rotation,
            gyro.getRotation2d());

        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Swerve.MAX_SPEED);

        frontLeft.setDesiredState(swerveModuleStates[0]);
        frontRight.setDesiredState(swerveModuleStates[1]);
        rearLeft.setDesiredState(swerveModuleStates[2]);
        rearRight.setDesiredState(swerveModuleStates[3]);
    }
    
    public void setX() {
        frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
        frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
        rearLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
        rearRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    }

    public static SwerveDriveKinematics getKinematics() {
        return kinematics;
    }

    @Override
    public void periodic() {
        odometry.update(
            gyro.getRotation2d(),
            new SwerveModulePosition[] {
                frontLeft.getModulePosition(),
                frontRight.getModulePosition(),
                rearLeft.getModulePosition(),
                rearRight.getModulePosition()
            }
        );
    }
}
