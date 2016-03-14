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
		NXTRegulatedMotor camPan = new NXTRegulatedMotor(MotorPort.C);
		booper.setSpeed(booper.getMaxSpeed());
		
		AtomicInteger x = new AtomicInteger(0);
		AtomicInteger y = new AtomicInteger(0);
		AtomicInteger w = new AtomicInteger(0);
		AtomicInteger h = new AtomicInteger(0);
		
		Wheel wheel1 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.D), 49.5).offset(-65);
		Wheel wheel2 = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.A), 49.5).offset(65);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		MovePilot pilot = new MovePilot(chassis);
		
		//this is supposed to update xy outside of everything
		Thread th = new Thread(){
			public void run(){
				while(!Button.ENTER.isDown()){
					PixyRectangle ball = pixy.getBiggestBlob();
					x.set(ball.x);
					y.set(ball.y);
					w.set(ball.width);
					h.set(ball.height);
					
					LCD.clearDisplay();
					LCD.drawString("X:" + x.get(), 0, 1);
					LCD.drawString("Y:" + y.get(), 0, 2);
					LCD.drawString("W:" + w.get(), 0, 3);
					LCD.drawString("H:" + h.get(), 0, 4);
					
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
		
		while (th.isAlive()) {
			//deal with x and y
			if(x.get()>140){
				pilot.rotate(360, true);
				while(pilot.isMoving()){
					if(x.get()>140){}
					else
						pilot.stop();
				}
			}
			else if(x.get()<100){
				pilot.rotate(-360, true);
				while(pilot.isMoving()){
				if(x.get()<100){}
				else
					pilot.stop();
				}
			}
			else if(y.get()<190){
				pilot.travel(100);
				while(pilot.isMoving()){
					if(y.get()<190){}
					else
						pilot.stop();
				}
			}
			else{
				booper.rotate(360);
			}
		}
		booper.close();
		pixy.close();
	}
}
