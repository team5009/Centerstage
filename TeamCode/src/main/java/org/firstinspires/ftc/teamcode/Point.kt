package org.firstinspires.ftc.teamcode

class Point {
    var x = 0.0
    var y = 0.0
    var rot = 0.0
    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(x: Double, y: Double, rot: Double) {
        this.x = x
        this.y = y
        this.rot = rot
    }
}