package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveDrive;

public class SwerveDriveDrive extends Command{
    private final SwerveDrive swerve;

    private final Supplier<Translation2d> speed;
    private final DoubleSupplier rotation;

    public SwerveDriveDrive(XboxController driverController) {
        swerve = new SwerveDrive();

        speed = () -> {
            double x = MathUtil.applyDeadband(driverController.getLeftX(), 0.05);
            double y = -MathUtil.applyDeadband(driverController.getLeftY(), 0.05);

            x *= Constants.Swerve.MAX_SPEED;
            y *= Constants.Swerve.MAX_SPEED;

            return new Translation2d(x, y);
        };

        rotation = () -> {
            double rot = -MathUtil.applyDeadband(driverController.getRightX(), 0.05);
            return rot * Constants.Swerve.MAX_ROTATION;
        };

        addRequirements(swerve);
    }

    @Override
    public void execute() {
        swerve.drive(speed.get(), rotation.getAsDouble());
    }
}
