package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrive;

public class SwerveDriveForward extends Command{
    private final SwerveDrive swerve;

    private double speed;

    private Timer timer = new Timer();

    public SwerveDriveForward(double speed) {
        swerve = SwerveDrive.getInstance();

        this.speed = speed;

        addRequirements(swerve);
    }


    @Override
    public void execute() {
        swerve.drive(new Translation2d(0, speed), 0);
    }
}
