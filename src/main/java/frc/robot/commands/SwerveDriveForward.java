package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrive;

public class SwerveDriveForward extends Command{
    private final SwerveDrive swerve;

    private double speed;

    public SwerveDriveForward(double speed) {
        swerve = new SwerveDrive();

        this.speed = speed;

        addRequirements(swerve);
    }

    @Override
    public void execute() {
        swerve.drive(new Translation2d(speed, 0), 0);
    }
}
