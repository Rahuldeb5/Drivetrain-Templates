package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Swerve;

public class SwerveDrive extends SubsystemBase {

    private final static SwerveDrive instance;

    static {
        instance = new SwerveDrive (
            new SwerveModule(Swerve.FrontLeft.DRIVE_ID, Swerve.FrontLeft.TURN_ID, Swerve.FrontLeft.ENCODER_ID, Swerve.FrontLeft.ANGLE_OFFSET, true),
            new SwerveModule(Swerve.FrontRight.DRIVE_ID, Swerve.FrontRight.TURN_ID, Swerve.FrontRight.ENCODER_ID, Swerve.FrontRight.ANGLE_OFFSET, false),
            new SwerveModule(Swerve.BackLeft.DRIVE_ID, Swerve.BackLeft.TURN_ID, Swerve.BackLeft.ENCODER_ID, Swerve.BackLeft.ANGLE_OFFSET, true),
            new SwerveModule(Swerve.BackRight.DRIVE_ID, Swerve.BackRight.TURN_ID, Swerve.BackRight.ENCODER_ID, Swerve.BackRight.ANGLE_OFFSET, false)
        );
    }

    
    public static SwerveDrive getInstance() {
        return instance;
    }

    private final SwerveModule[] modules;

    private final SwerveDriveOdometry odometry;

    private SwerveModuleState[] currentStates;

    private final Pigeon2 gyro;

    public SwerveDrive(SwerveModule... modules) {
        this.modules = modules;

        gyro = new Pigeon2(Swerve.GYRO_ID, "*");

        odometry = new SwerveDriveOdometry(
            Swerve.kinematics,
            gyro.getRotation2d(), 
            new SwerveModulePosition[] {
                modules[0].getModulePosition(),
                modules[1].getModulePosition(),
                modules[2].getModulePosition(),
                modules[3].getModulePosition()
            });

        currentStates = new SwerveModuleState[] {};
    }

    public void drive(Translation2d velocity, double rot) {
        ChassisSpeeds speeds = new ChassisSpeeds(
            velocity.getX(), velocity.getY(), rot);

        SwerveModuleState[] states = Swerve.kinematics.toSwerveModuleStates(speeds);

        SwerveDriveKinematics.desaturateWheelSpeeds(states, Swerve.MAX_SPEED);

        currentStates = states;

        setDesiredStates(states);
    }

    public void setXMode() {
        SwerveModuleState[] states = {
            new SwerveModuleState(0, Rotation2d.fromDegrees(135)),
            new SwerveModuleState(0, Rotation2d.fromDegrees(225)),
            new SwerveModuleState(0, Rotation2d.fromDegrees(315)),
            new SwerveModuleState(0, Rotation2d.fromDegrees(45)),
        };

        currentStates = states;
        setDesiredStates(states);
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public void setDesiredStates(SwerveModuleState[] states) {
        modules[0].setDesiredState(states[0]);
        modules[1].setDesiredState(states[1]);
        modules[2].setDesiredState(states[2]);
        modules[3].setDesiredState(states[3]);
    }

    public void resetOdometry(Pose2d newPose) {
        odometry.resetPosition(
            gyro.getRotation2d(),
            new SwerveModulePosition[] {
                modules[0].getModulePosition(),
                modules[1].getModulePosition(),
                modules[2].getModulePosition(),
                modules[3].getModulePosition()
            }, newPose);
    }

    @Override
    public void periodic() {
        odometry.update(
            gyro.getRotation2d(),
            new SwerveModulePosition[] {
                modules[0].getModulePosition(),
                modules[1].getModulePosition(),
                modules[2].getModulePosition(),
                modules[3].getModulePosition()
            });

        // for(int i=0; i<4; i++) {
        //     if(modules != null) SmartDashboard.putNumber("Module " + i + " angle", modules[i].getAngle().getRotations());
        //     if(currentStates != null) SmartDashboard.putNumber("State angle " + i, currentStates[i].angle.getRotations());
        // }
    }

}