package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.Constants.Swerve.Drive;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public final class Configs {
    public static final class MAXSwerveModule {
        public static final SparkMaxConfig turnConfig = new SparkMaxConfig();

        static {
            turnConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(20);
                // .smartCurrentLimit(200)
                // .inverted(true)
                // .openLoopRampRate(0.25)
        }
    }

    public static final class PhoenixSwerveModule {
        public static final TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        
        static {
            driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
            driveConfig.Slot0.withKP(Drive.kP).withKI(Drive.kI).withKD(Drive.kD)
                .withKS(Drive.kS).withKV(Drive.kV).withKA(Drive.kA);
            driveConfig.Feedback.SensorToMechanismRatio = Drive.GEAR_RATIO;
            driveConfig.TorqueCurrent.PeakForwardTorqueCurrent = Drive.driveCurrentLimit;
            driveConfig.TorqueCurrent.PeakReverseTorqueCurrent = -Drive.driveCurrentLimit;
            driveConfig.CurrentLimits.StatorCurrentLimit = Drive.driveCurrentLimit;
            driveConfig.CurrentLimits.StatorCurrentLimitEnable = true;
            driveConfig.ClosedLoopRamps.TorqueClosedLoopRampPeriod = 0.02;
        }
    }
}
