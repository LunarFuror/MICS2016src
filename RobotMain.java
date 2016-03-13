import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class RobotMain {

	public static void main(String[] args) {
		LegoPixy pixy = new LegoPixy(SensorPort.S4);
		EV3MediumRegulatedMotor booper = new EV3MediumRegulatedMotor(MotorPort.B);
		NXTRegulatedMotor camPan = new NXTRegulatedMotor(MotorPort.C);
		booper.setSpeed(booper.getMaxSpeed());
		
		Wheel wheel1 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.D), 49.5).offset(-65);
		Wheel wheel2 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.A), 49.5).offset(65);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		MovePilot pilot = new MovePilot(chassis);
		
		while (!Button.ENTER.isDown()) {
			//camPan.rotateTo(180); //move cam up or down
			//booper.rotateTo(360); //one full boop
			PixyRectangle ball = pixy.getBiggestBlob();
			int w = ball.width;
			int h = ball.height;
			int x = ball.x;
			int y = ball.y;
			LCD.clearDisplay();
			LCD.drawInt(w, 1, 1);
			LCD.drawInt(h, 1, 2);
			LCD.drawInt(x, 1, 3);
			LCD.drawInt(y, 1, 4);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(x>140){
				pilot.rotate(360, true);
				while(x>140){}
				pilot.stop();
			}
			else if(x<100){
				pilot.rotate(-360, true);
				while(x<100){}
				pilot.stop();
			}
			else if(y<190){
				pilot.travel(100);
				while(y<190){}
				pilot.stop();
			}
			else{
				booper.rotate(360);
			}
			
		}
		booper.close();
		pixy.close();
	}
}
