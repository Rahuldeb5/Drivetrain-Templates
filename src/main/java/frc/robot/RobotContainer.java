// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.Swerve.AutoConstants;
import frc.robot.commands.SwerveDriveDrive;
import frc.robot.commands.SwerveDriveSetX;
import frc.robot.commands.auton.Mobility;
import frc.robot.subsystems.SwerveDrive;
import edu.wpi.first.math.trajectory.Trajectory;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final SwerveDrive swerve = SwerveDrive.getInstance();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final XboxController driverController = new XboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureDefaultCommand();
    configureBindings();
  }
  
  private void configureDefaultCommand() {
    swerve.setDefaultCommand(new SwerveDriveDrive(driverController));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    Trigger rightTrigger = new Trigger(() -> driverController.getRightTriggerAxis() > 0.5);
    rightTrigger.whileTrue(new SwerveDriveSetX());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

  // Use trajectories for more complicated paths

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    // return Autos.exampleAuto(m_exampleSubsystem);  

    TrajectoryConfig config = new TrajectoryConfig(AutoConstants.kMaxSpeed, AutoConstants.kMaxAcceleration)
        .setKinematics(Constants.Swerve.kinematics);

    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)),
        List.of(new Translation2d(3, 0)),
        new Pose2d(3, 0, new Rotation2d(0)),
        config);

    ProfiledPIDController thetaController = new ProfiledPIDController(AutoConstants.kPTheta, 0, 0, new TrapezoidProfile.Constraints(AutoConstants.kMaxAngularSpeed, AutoConstants.kMaxAngularAcceleration));
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        exampleTrajectory,
        swerve::getPose,
        Constants.Swerve.kinematics,
        new PIDController(AutoConstants.kPX, 0, 0),
        new PIDController(AutoConstants.kPY, 0, 0),
        thetaController,
        swerve::setDesiredStates,
        swerve);

    swerve.resetOdometry(exampleTrajectory.getInitialPose());

    return swerveControllerCommand.andThen(() -> swerve.drive(new Translation2d(0, 0), 0));
    
    // return new Mobility();
  }
}
