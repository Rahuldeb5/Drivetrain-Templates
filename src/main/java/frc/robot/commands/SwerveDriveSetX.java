package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrive;

public class SwerveDriveSetX extends Command{
    private final SwerveDrive swerve;

    public SwerveDriveSetX() {
        swerve = new SwerveDrive();
        addRequirements(swerve);
    }

    @Override
    public void execute() {
        swerve.setX();
    }
}
