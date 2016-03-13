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
	static int x = 0;
	static int y = 0;
	static int h = 0;
	static int w = 0;
	public static void main(String[] args) {
		final LegoPixy pixy = new LegoPixy(SensorPort.S4);
		EV3MediumRegulatedMotor booper = new EV3MediumRegulatedMotor(MotorPort.B);
		NXTRegulatedMotor camPan = new NXTRegulatedMotor(MotorPort.C);
		booper.setSpeed(booper.getMaxSpeed());
		
		Wheel wheel1 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.D), 49.5).offset(-65);
		Wheel wheel2 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.A), 49.5).offset(65);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		MovePilot pilot = new MovePilot(chassis);
		
		//this is supposed to update xy outside of everything
		Thread th = new Thread(){
			public void run(){
				while(!Button.ENTER.isDown()){
					PixyRectangle ball = pixy.getBiggestBlob();
					x = ball.x;
					y = ball.y;
					w = ball.width;
					h = ball.height;
					
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
				}
			}
		};
		//start that shit
		th.start();
		
		while (!Button.ENTER.isDown()) {
			//deal with x and y
			if(x>140){
				pilot.rotateLeft();
				while(x>140){}
				pilot.stop();
			}
			else if(x<100){
				pilot.rotateRight();
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
