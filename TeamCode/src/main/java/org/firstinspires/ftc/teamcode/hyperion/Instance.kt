package org.firstinspires.ftc.teamcode.instances

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.Ruboot
import org.firstinspires.ftc.teamcode.hyperion.odometry.Odometry

enum class Position {
    Left, Right, Middle, None, Top, Bottom
}
class AutoInstance(Instance: LinearOpMode, t: Telemetry) {
    private val instance = Instance
    private val bot = Ruboot(instance)

    val odo = Odometry(bot)

    var powerX = 0.0
    var powerY = 0.0
    var turn = 0.0
    var heading = 0.0

    /*fun followCurve(pathPoints: ArrayList<Curvepoint>, followAngle: Double) {
        val followMe = getFollowPointPath(pathPoints, odo.location, pathPoints[0].followDistance)

        //TODO: Some point make it so if there's a point even closer to the bot from the list, it will follow that point instead

        goToPosition(followMe.x, followMe.y, followAngle, followMe.moveSpeed, followMe.turnSpeed)

    }

    fun holdHeading() {
        while (instance.opModeIsActive()) {
            val diff = abs(refRad(heading) - refRad(odo.location.rot))

        }
    }

    private fun getFollowPointPath(
        pathPoints: ArrayList<Curvepoint>,
        location: Point,
        followRadius: Double
    ): Curvepoint {
        val follow = Curvepoint(pathPoints[0])
        for (i in 0 until pathPoints.size - 1) {
            val startLine = pathPoints[i]
            val endLine = pathPoints[i + 1]

            val intersections = lineCircleIntersec(location, followRadius, startLine.toPoint(), endLine.toPoint())

            var closestAngle = 1000000.0

            for (intersection in intersections) {
                val angle = atan2(intersection.y - odo.location.y, intersection.x - odo.location.x)
                val deltaAngle = abs(refAngle(angle - degToRad(odo.location.rot)))

                if (deltaAngle < closestAngle) {
                    closestAngle = deltaAngle
                    follow.setPoint(intersection)
                }
            }

        }
        return follow
    }

    private fun goToPosition(x: Double, y: Double, preferAngle: Double, power: Double, turnPower: Double) {
        val distanceToTarget = hypot(x - odo.location.x, y - odo.location.y)
        val absAngleToTarget = atan2(y - odo.location.y, x - odo.location.x)
        val relAngletoPoint = refAngle(absAngleToTarget - degToRad(odo.location.rot))

        val relXtoPoint = cos(relAngletoPoint) * distanceToTarget
        val relYtoPoint = sin(relAngletoPoint) * distanceToTarget

        // Make it so the power is in a constant ratio between 1 and 0
        val moveXpower = relXtoPoint / abs(relXtoPoint) + abs(relYtoPoint)
        val moveYPower = relYtoPoint / abs(relXtoPoint) + abs(relYtoPoint)

        powerX = moveXpower * power
        powerY = moveYPower * power

        val relTurnAngle = relAngletoPoint - PI + preferAngle
        turn = Range.clip(relTurnAngle / (PI / 6), -1.0, 1.0) * turnPower

        if (distanceToTarget < 10) {
            turn = 0.0
        }
    }
        }
    } */

}

