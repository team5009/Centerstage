package org.firstinspires.ftc.teamcode.misc

fun refAngle(angleIn: Double): Double {
    if (angleIn < 0) {
        return -((-angleIn + 180) % 360) + 180
    }
    return (angleIn + 180) % 360 - 180
}

fun refRad(angleIn: Double): Double {
    if (angleIn < 0) {
        return -((-angleIn + Math.PI) % (2 * Math.PI)) + Math.PI
    }
    return (angleIn + Math.PI) % (2 * Math.PI) - Math.PI
}