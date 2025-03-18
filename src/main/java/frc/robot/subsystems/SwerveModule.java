package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Configs;
import frc.robot.Constants.Swerve;

public class SwerveModule {
    private final TalonFX driveMotor;
    private final SparkMax turnMotor;

    private final CANcoder turnEncoder;
    private final PIDController turnController;

    private Rotation2d angleOffset;

    public SwerveModule(int driveId, int turnMotorId, int turnEncoderId, Rotation2d angleOffset, boolean driveInverted) {
        driveMotor = new TalonFX(driveId, "*");
        turnMotor = new SparkMax(turnMotorId, MotorType.kBrushless);

        turnEncoder = new CANcoder(turnEncoderId, "*");
        turnController = new PIDController(Swerve.Turn.kP, Swerve.Turn.kI, Swerve.Turn.kD);
        turnController.enableContinuousInput(-Math.PI, Math.PI);

        this.angleOffset = angleOffset;

        turnMotor.configure(Configs.MAXSwerveModule.turnConfig,
            ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        MotorOutputConfigs motorOutputConfigs =
            new MotorOutputConfigs()
                .withInverted(driveInverted ? InvertedValue.CounterClockwise_Positive : InvertedValue.Clockwise_Positive)
                .withNeutralMode(NeutralModeValue.Brake);

        driveMotor.getConfigurator().apply(Configs.PhoenixSwerveModule.driveConfig.withMotorOutput(motorOutputConfigs));
        driveMotor.setPosition(0);
    }

    public double getPosition() {
        return driveMotor.getPosition().getValueAsDouble();
    }

    public double getVelocity() {
        return driveMotor.getVelocity().getValueAsDouble();
    }

    public Rotation2d getAngle() {
        return Rotation2d.fromRotations(turnEncoder.getPosition().getValueAsDouble()).minus(angleOffset);
    }

    public SwerveModulePosition getModulePosition() {
        return new SwerveModulePosition(getPosition(), getAngle());
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(getVelocity(), getAngle());
    }

    public void setDesiredState(SwerveModuleState desiredState) {
        desiredState.optimize(getAngle());
        desiredState.cosineScale(getAngle());

        // if(desiredState.speedMetersPerSecond < 0.05) {
        //     driveMotor.setControl(new VelocityVoltage(0));
        //     turnMotor.setVoltage(0);
        // }
        
        driveMotor.setControl(new VelocityVoltage(desiredState.speedMetersPerSecond).withEnableFOC(true));
        turnMotor.setVoltage(turnController.calculate(getAngle().getRadians(), desiredState.angle.getRadians()));
    }
}
