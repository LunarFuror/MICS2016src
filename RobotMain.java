import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class RobotMain {
	public enum Phase {
		CENTER,
		APPROACH,
		BOOP
	}
	
	public static void main(String[] args) {
		final double CENTER = 140;
		final double LEFT_DEADZONE = 20;
		final double RIGHT_DEADZONE = 20;
		final double ANGLE_FACTOR = 0.289;
		EV3MediumRegulatedMotor booper = new EV3MediumRegulatedMotor(MotorPort.C);
		EV3MediumRegulatedMotor camArm = new EV3MediumRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.D);
		Wheel wheel1 = WheeledChassis.modelWheel(right, 43.5).offset(-80).invert(true);
		Wheel wheel2 = WheeledChassis.modelWheel(left, 43.5).offset(80).invert(true);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
		LegoPixy pixy = new LegoPixy(SensorPort.S4);
		Phase p = Phase.CENTER;
		
		booper.rotate(360);		
		while(Button.ENTER.isUp())
		{
			PixyRectangle rec = pixy.getBiggestBlob();
			LCD.clearDisplay();
			LCD.drawString("X :" + rec.getCenterX(), 0, 1);
			LCD.drawString("Y :" + rec.getCenterY(), 0, 2);
			LCD.drawString("W :" + rec.getWidth(), 0, 3);
			LCD.drawString("H :" + rec.getHeight(), 0, 4);
			LCD.drawString("PHASE :" + p.toString(), 0, 6);
			//center the ball before moving
			if(p == Phase.CENTER){
				if (rec.getCenterX() < CENTER - LEFT_DEADZONE)
				{
					double move = (CENTER - rec.getCenterX()) * ANGLE_FACTOR * -1;
					pilot.rotate(move);
				}
				else if (rec.getCenterX() > CENTER + RIGHT_DEADZONE)
				{
					double move = (rec.getCenterX() - CENTER) * ANGLE_FACTOR;
					pilot.rotate(move);
				}
				else{
					p = Phase.APPROACH;
				}
			}
			
			//ball centered? then approach the ball
			if(p == Phase.APPROACH){
				//determine roughly how far away it is now that we're centered
				//move that distance
				//check centered
				if (rec.getCenterX() < CENTER - LEFT_DEADZONE || rec.getCenterX() > CENTER + RIGHT_DEADZONE){
					p = Phase.CENTER;
				}
				//check distance
				else if(rec.getCenterY() > 10){
					p = Phase.APPROACH;
				}
				//both good, better boop it.
				else{
					p = Phase.BOOP;
				}
			}
			
			//Ball centered AND close? Then the cam is down and we need to boop.
			if(p == Phase.BOOP){
				//move forward slowly, we can either check this or put a procedure here
				//boop the ball!
				//cam up if it was put down
				//start process over
			}
		}
		pixy.close();
		left.close();
		right.close();
		
		try 
		{
			Thread.sleep(200);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}

