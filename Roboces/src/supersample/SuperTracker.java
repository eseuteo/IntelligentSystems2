package supersample;

import robocode.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * SuperTracker - a Super Sample Robot by CrazyBassoonist based on the robot
 * Tracker by Mathew Nelson and maintained by Flemming N. Larsen
 * <p/>
 * Locks onto a robot, moves close, fires when close.
 */
public class SuperTracker extends AdvancedRobot {
	int moveDirection = 1;// which way to move
	Random numberGenerator = new Random(0);
	private double[] params;  
	/**
	 * run:  Tracker's main run function
	 */
	
	
	public void run() {
		params = new double[5];
		tuneBot("C:\\Users\\Pepe\\workspace\\BattleRunner\\src\\a.txt");
		if(params[4]>0){
			setAllColors(Color.YELLOW);
		}else{
			setAllColors(Color.GREEN);
		}
		setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
		turnRadarRightRadians(Double.POSITIVE_INFINITY);//keep turning radar right
		}

	/**
	 * onScannedRobot: Here's the good stuff
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing = e.getBearingRadians() + getHeadingRadians();// enemies
																		// absolute
																		// bearing
		double latVel = e.getVelocity() * Math.sin(e.getHeadingRadians() - absBearing);// enemies
																						// later
																						// velocity
		double gunTurnAmt;// amount to turn our gun
		setTurnRadarLeftRadians(getRadarTurnRemainingRadians());// lock on the
																// radar
		if (numberGenerator.nextDouble() > params[1]) {
	/*		setMaxVelocity((params[2] * numberGenerator.nextDouble()) + params[3]);// randomly change speed
														// \\ VELOCIDAD + RANGO
														// + MINIMA VELOCIDAD
		*/
			setMaxVelocity(((8 - params[3]) * params[2] * numberGenerator.nextDouble()) + params[3]);// randomly change speed
														//  MAXVELOCIDAD = RANGO + MINIMA VELOCIDAD
														//Rango needed from 0 to maxvelocity(=8)-minvelocidad
		}
		if (e.getDistance() > params[0]) {// if distance is greater than 150 \\
									// DISTANCIA
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing - getGunHeadingRadians() + latVel / 22);// amount
																													// to
																													// turn
																													// our
																													// gun,
																													// lead
																													// just
																													// a
																													// little
																													// bit
			setTurnGunRightRadians(gunTurnAmt); // turn our gun
			setTurnRightRadians(
					robocode.util.Utils.normalRelativeAngle(absBearing - getHeadingRadians() + latVel / getVelocity()));// drive
																														// towards
																														// the
																														// enemies
																														// predicted
																														// future
																														// location
			setAhead((e.getDistance() - 140) * moveDirection);// move forward
			setFire(3);// fire
		} else {// if we are close enough...
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing - getGunHeadingRadians() + latVel / 15);// amount
																													// to
																													// turn
																													// our
																													// gun,
																													// lead
																													// just
																													// a
																													// little
																													// bit
			setTurnGunRightRadians(gunTurnAmt);// turn our gun
			setTurnLeft(-90 - e.getBearing()); // turn perpendicular to the
												// enemy
			setAhead((e.getDistance() - 140) * moveDirection);// move forward
			setFire(3);// fire
		}
	}

	public void onHitWall(HitWallEvent e) {
		moveDirection = -moveDirection;// reverse direction upon hitting a wall
	}

	/**
	 * onWin: Do a victory dance
	 */
	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {
			turnRight(30);
			turnLeft(30);
		}
	}

	private void tuneBot(String path) {
		Locale.setDefault(Locale.ENGLISH);
		try {
			int i = 0;
			Scanner sc = new Scanner(new FileReader(path));
			Scanner sc2 = new Scanner(sc.nextLine());
			
			while(sc2.hasNext()){
				params[i++] = sc2.nextDouble();
			}		
			
			sc2.close();
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
