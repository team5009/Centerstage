package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.max


class SimpleOdoMovement (private val myOpMode: LinearOpMode, robot : RobotTest, odometry : Odometry) {
    val bot = robot
    val odo = odometry

    // Adjust these numbers to suit your robot.
    private val ODOM_INCHES_PER_COUNT = 0.002969 //  GoBilda Odometry Pod (1/226.8)
    private val INVERT_DRIVE_ODOMETRY = false //  When driving FORWARD, the odometry value MUST increase.  If it does not, flip the value of this constant.
    private val INVERT_STRAFE_ODOMETRY = false //  When strafing to the LEFT, the odometry value MUST increase.  If it does not, flip the value of this constant.

    // Public Members
    var driveDistance = 0.0 // scaled axial distance (+ = forward)
    var strafeDistance = 0.0 // scaled lateral distance (+ = left)
    var heading = 0.0 // Latest Robot heading from IMU

    // Establish a proportional controller for each axis to calculate the required power to achieve a setpoint.
    var driveController: ProportionalController = ProportionalController(DRIVE_GAIN, DRIVE_ACCEL, DRIVE_MAX_AUTO, DRIVE_TOLERANCE, DRIVE_DEADBAND, false)
    var strafeController: ProportionalController = ProportionalController(STRAFE_GAIN, STRAFE_ACCEL, STRAFE_MAX_AUTO, STRAFE_TOLERANCE, STRAFE_DEADBAND, false)
    var yawController: ProportionalController = ProportionalController(YAW_GAIN, YAW_ACCEL, YAW_MAX_AUTO, YAW_TOLERANCE, YAW_DEADBAND, true)

    // ---  Private Members
    // Hardware interface Objects
    private val holdTimer = ElapsedTime() // User for any motion requiring a hold time or timeout.
    private var rawDriveOdometer = 0.0 // Unmodified axial odometer count
    private var driveOdometerOffset = 0.0 // Used to offset axial odometer
    private var rawStrafeOdometer = 0.0 // Unmodified lateral odometer count
    private var strafeOdometerOffset = 0.0 // Used to offset lateral odometer
    private var rawHeading = 0.0 // Unmodified heading (degrees)
    private var headingOffset = 0.0 // Used to offset heading
    var turnRate = 0.0 // Latest Robot Turn Rate from IMU
        private set
    private var showTelemetry = false

    /**
     * Robot Initialization:
     * Use the hardware map to Connect to devices.
     * Perform any set-up all the hardware devices.
     * @param showTelemetry  Set to true if you want telemetry to be displayed by the robot sensor/drive functions.
     */
    fun initialize(showTelemetry: Boolean) {
        // Initialize the hardware variables. Note that the strings used to 'get' each
        // motor/device must match the names assigned during the robot configuration.

        // !!!  Set the drive direction to ensure positive power drives each wheel forward.
        /*leftFrontDrive = setupDriveMotor("FL", DcMotorSimple.Direction.REVERSE)
        rightFrontDrive = setupDriveMotor("FR", DcMotorSimple.Direction.FORWARD)
        leftBackDrive = setupDriveMotor("BL", DcMotorSimple.Direction.REVERSE)
        rightBackDrive = setupDriveMotor("BR", DcMotorSimple.Direction.FORWARD)*/

        //  Connect to the encoder channels using the name of that channel.
        /*driveEncoder = myOpMode.hardwareMap.get(DcMotor::class.java, "axial")
        strafeEncoder = myOpMode.hardwareMap.get(DcMotor::class.java, "lateral")*/

        // Set all hubs to use the AUTO Bulk Caching mode for faster encoder reads
        val allHubs = myOpMode.hardwareMap.getAll(LynxModule::class.java)
        for (module in allHubs) {
            module.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }


        // zero out all the odometry readings.
        resetOdometry()

        // Set the desired telemetry state
        this.showTelemetry = showTelemetry
    }

    /**
     * Setup a drive motor with passed parameters.  Ensure encoder is reset.
     * @param deviceName  Text name associated with motor in Robot Configuration
     * @param direction   Desired direction to make the wheel run FORWARD with positive power input
     * @return the DcMotor object
     */
    private fun setupDriveMotor(deviceName: String, direction: DcMotorSimple.Direction): DcMotor {
        val aMotor = myOpMode.hardwareMap.get(DcMotor::class.java, deviceName)
        aMotor.direction = direction
        aMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER // Reset Encoders to zero
        aMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        aMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER // Requires motor encoder cables to be hooked up.
        return aMotor
    }

    /**
     * Read all input devices to determine the robot's motion
     * always return true so this can be used in "while" loop conditions
     * @return true
     */
    fun readSensors(): Boolean {
        odo.calculate()
        rawDriveOdometer = bot.rightEncoder.currentPosition * if (INVERT_DRIVE_ODOMETRY) -1.0 else 1.0
        rawStrafeOdometer = bot.backEncoder.currentPosition * if (INVERT_STRAFE_ODOMETRY) -1.0 else 1.0
        driveDistance = (rawDriveOdometer - driveOdometerOffset) * ODOM_INCHES_PER_COUNT
        strafeDistance = (rawStrafeOdometer - strafeOdometerOffset) * ODOM_INCHES_PER_COUNT
        val orientation = bot.imu.robotYawPitchRollAngles
        val angularVelocity = bot.imu.getRobotAngularVelocity(AngleUnit.DEGREES)
        rawHeading = orientation.getYaw(AngleUnit.DEGREES)
        heading = rawHeading - headingOffset
        turnRate = angularVelocity.zRotationRate.toDouble()
        if (showTelemetry) {
            myOpMode.telemetry.addData("Odom Ax:Lat", "${rawDriveOdometer - driveOdometerOffset}, ${rawStrafeOdometer - strafeOdometerOffset}")
            myOpMode.telemetry.addData("Dist Ax:Lat", "${driveDistance}, ${strafeDistance}")
            myOpMode.telemetry.addData("Head Deg:Rate", "${heading}, ${turnRate}")
        }
        return true // do this so this function can be included in the condition for a while loop to keep values fresh.
    }
    //  ########################  Mid level control functions.  #############################3#
    /**
     * Drive in the axial (forward/reverse) direction, maintain the current heading and don't drift sideways
     * @param distanceInches  Distance to travel.  +ve = forward, -ve = reverse.
     * @param power Maximum power to apply.  This number should always be positive.
     * @param holdTime Minimum time (sec) required to hold the final position.  0 = no hold.
     */
    fun drive(distanceInches: Double, power: Double, holdTime: Double) {
        resetOdometry()
        driveController.reset(distanceInches, power) // achieve desired drive distance
        strafeController.reset(0.0) // Maintain zero strafe drift
        yawController.reset() // Maintain last turn heading
        holdTimer.reset()
        while (myOpMode.opModeIsActive() && readSensors()) {

            // implement desired axis powers
            moveRobot(driveController.getOutput(driveDistance), strafeController.getOutput(strafeDistance), yawController.getOutput(heading))

            // Time to exit?
            if (driveController.inPosition() && yawController.inPosition()) {
                if (holdTimer.time() > holdTime) {
                    break // Exit loop if we are in position, and have been there long enough.
                }
            } else {
                holdTimer.reset()
            }
            myOpMode.sleep(10)
        }
        stopRobot()
    }

    /**
     * Strafe in the lateral (left/right) direction, maintain the current heading and don't drift fwd/bwd
     * @param distanceInches  Distance to travel.  +ve = left, -ve = right.
     * @param power Maximum power to apply.  This number should always be positive.
     * @param holdTime Minimum time (sec) required to hold the final position.  0 = no hold.
     */
    fun strafe(distanceInches: Double, power: Double, holdTime: Double) {
        resetOdometry()
        driveController.reset(0.0) //  Maintain zero drive drift
        strafeController.reset(distanceInches, power) // Achieve desired Strafe distance
        yawController.reset() // Maintain last turn angle
        holdTimer.reset()
        while (myOpMode.opModeIsActive() && readSensors()) {

            // implement desired axis powers
            moveRobot(driveController.getOutput(driveDistance), strafeController.getOutput(strafeDistance), yawController.getOutput(heading))

            // Time to exit?
            if (strafeController.inPosition() && yawController.inPosition()) {
                if (holdTimer.time() > holdTime) {
                    break // Exit loop if we are in position, and have been there long enough.
                }
            } else {
                holdTimer.reset()
            }
            myOpMode.sleep(10)
        }
        stopRobot()
    }

    /**
     * Rotate to an absolute heading/direction
     * @param headingDeg  Heading to obtain.  +ve = CCW, -ve = CW.
     * @param power Maximum power to apply.  This number should always be positive.
     * @param holdTime Minimum time (sec) required to hold the final position.  0 = no hold.
     */
    fun turnTo(headingDeg: Double, power: Double, holdTime: Double) {
        yawController.reset(headingDeg, power)
        while (myOpMode.opModeIsActive() && readSensors()) {

            // implement desired axis powers
            moveRobot(0.0, 0.0, yawController.getOutput(heading))

            // Time to exit?
            if (yawController.inPosition()) {
                if (holdTimer.time() > holdTime) {
                    break // Exit loop if we are in position, and have been there long enough.
                }
            } else {
                holdTimer.reset()
            }
            myOpMode.sleep(10)
        }
        stopRobot()
    }
    //  ########################  Low level control functions.  ###############################
    /**
     * Drive the wheel motors to obtain the requested axes motions
     * @param drive     Fwd/Rev axis power
     * @param strafe    Left/Right axis power
     * @param yaw       Yaw axis power
     */
    fun moveRobot(drive: Double, strafe: Double, yaw: Double) {
        var lF = drive - strafe - yaw
        var rF = drive + strafe + yaw
        var lB = drive + strafe - yaw
        var rB = drive - strafe + yaw
        var max = max(abs(lF), abs(rF))
        max = max(max, abs(lB))
        max = max(max, abs(rB))

        //normalize the motor values
        if (max > 1.0) {
            lF /= max
            rF /= max
            lB /= max
            rB /= max
        }

        //send power to the motors
        bot.move(lF, rF, lB, rB)
        if (showTelemetry) {
            myOpMode.telemetry.addData("Axes D:S:Y", "${drive}, ${strafe}, ${yaw}")
            myOpMode.telemetry.addData("Wheels lf:rf:lb:rb", "${lF}, ${rF}, ${lB}, ${rB}")
            myOpMode.telemetry.update() //  Assume this is the last thing done in the loop.
        }
    }

    /**
     * Stop all motors.
     */
    fun stopRobot() {
        moveRobot(0.0, 0.0, 0.0)
    }

    /**
     * Set odometry counts and distances to zero.
     */
    fun resetOdometry() {
        readSensors()
        driveOdometerOffset = rawDriveOdometer
        driveDistance = 0.0
        driveController.reset(0.0)
        strafeOdometerOffset = rawStrafeOdometer
        strafeDistance = 0.0
        strafeController.reset(0.0)
    }

    /**
     * Reset the robot heading to zero degrees, and also lock that heading into heading controller.
     */
    fun resetHeading() {
        readSensors()
        headingOffset = rawHeading
        yawController.reset(0.0)
        heading = 0.0
    }

    /**
     * Set the drive telemetry on or off
     */
    fun showTelemetry(show: Boolean) {
        showTelemetry = show
    }

    companion object {
        private const val DRIVE_GAIN = 0.11 // Strength of axial position control
        private const val DRIVE_ACCEL = 2.0 // Acceleration limit.  Percent Power change per second.  1.0 = 0-100% power in 1 sec.
        private const val DRIVE_TOLERANCE = 0.8 // Controller is is "inPosition" if position error is < +/- this amount
        private const val DRIVE_DEADBAND = 0.2 // Error less than this causes zero output.  Must be smaller than DRIVE_TOLERANCE
        private const val DRIVE_MAX_AUTO = 0.6 // "default" Maximum Axial power limit during autonomous

        private const val STRAFE_GAIN = 0.12 // Strength of lateral position control
        private const val STRAFE_ACCEL = 1.5 // Acceleration limit.  Percent Power change per second.  1.0 = 0-100% power in 1 sec.
        private const val STRAFE_TOLERANCE = 1.0 // Controller is is "inPosition" if position error is < +/- this amount
        private const val STRAFE_DEADBAND = 0.2 // Error less than this causes zero output.  Must be smaller than DRIVE_TOLERANCE
        private const val STRAFE_MAX_AUTO = 0.6 // "default" Maximum Lateral power limit during autonomous

        private const val YAW_GAIN = 0.03 // Strength of Yaw position control
        private const val YAW_ACCEL = 2.0 // Acceleration limit.  Percent Power change per second.  1.0 = 0-100% power in 1 sec.
        private const val YAW_TOLERANCE = 1.0 // Controller is is "inPosition" if position error is < +/- this amount
        private const val YAW_DEADBAND = 0.25 // Error less than this causes zero output.  Must be smaller than DRIVE_TOLERANCE
        private const val YAW_MAX_AUTO = 0.6 // "default" Maximum Yaw power limit during autonomous
    }
}