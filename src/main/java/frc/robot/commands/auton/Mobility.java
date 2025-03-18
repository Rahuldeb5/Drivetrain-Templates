package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.commands.SwerveDriveForward;

public class Mobility extends SequentialCommandGroup {
    
    public Mobility() {
        addCommands(
            new WaitCommand(1),
            new SwerveDriveForward(Constants.Swerve.AutoConstants.kMaxSpeed).withTimeout(3)
        );
    }
    
}
