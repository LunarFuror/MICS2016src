import java.util.concurrent.atomic.AtomicInteger;
import lejos.hardware.Button;
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
		final LegoPixy pixy = new LegoPixy(SensorPort.S4);
		EV3MediumRegulatedMotor booper = new EV3MediumRegulatedMotor(MotorPort.B);
		EV3MediumRegulatedMotor camPan = new EV3MediumRegulatedMotor(MotorPort.C);
		booper.setSpeed(booper.getMaxSpeed());
		
		Wheel wheel1 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.D), 49.5).offset(-65);
		Wheel wheel2 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.A), 49.5).offset(65);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		MovePilot pilot = new MovePilot(chassis);
		pilot.setAngularSpeed(45);
		final PixyRectangle ball = pixy.getBiggestBlob();
		//this is supposed to update xy outside of everything
		Thread th = new Thread(){
			public void run(){
				while(!Button.ENTER.isDown()){
					ball.setRect(pixy.getBiggestBlob());
					
					LCD.clearDisplay();
					LCD.drawString("X :" + ball.x, 0, 1);
					LCD.drawString("Y :" + ball.y, 0, 2);
					LCD.drawString("W :" + ball.width, 0, 3);
					LCD.drawString("H :" + ball.height, 0, 4);
					LCD.drawString("CX:" + ball.x + (ball.width/2), 0, 3);
					LCD.drawString("CY:" + ball.y + (ball.height/2), 0, 4);
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		//start that shit
		th.start();
		
		while (th.isAlive()) {
			/*
			//deal with x and y
			if(x.get()>140){
				pilot.rotateLeft();
				while(pilot.isMoving()){
					if(x.get()>140){}
					else
						pilot.stop();
				}
			}
			else if(x.get()<100){
				pilot.rotateRight();
				while(pilot.isMoving()){
				if(x.get()<100){}
				else
					pilot.stop();
				}
			}
			else if(y.get()<190){
				pilot.forward();
				while(pilot.isMoving()){
					if(y.get()<190){}
					else
						pilot.stop();
				}
			}
			else{
				booper.rotate(360);
			}
			*/
		}
		booper.close();
		pixy.close();
	}
}
