package io.biologeek.raspi.catfood.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Service
public class CatFoodServices {

	

	@Value("${step.interval}")
	private Long stepInterval;
	@Value("${step.per.revolution}")
	private Integer stepsPerRevolution;
	@Value("${sleeping.period}")
	private Long sleepingPeriod;
	
	/**
	 * Operates a 135 deg rotation to empty container, waits x seconds and gets back to original position
	 * @throws InterruptedException 
	 */
	public void feedTheCat() throws InterruptedException {
		final GpioController controller = GpioFactory.getInstance();
		
		 final GpioPinDigitalOutput[] pins = {
	                controller.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
	                controller.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
	                controller.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
	                controller.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW)};
		 
		 controller.setShutdownOptions(true, PinState.LOW, pins);
		 
		 
		 GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
		 
		 
		 motor.setStepInterval(stepInterval);
		 motor.setStepSequence(doubleStepDispensingSequence());
		 motor.setStepsPerRevolution(stepsPerRevolution); // To change
		 
		 // 135° revolution to empty container
		 motor.rotate(3/8);
		 
		 Thread.sleep(sleepingPeriod);
		 
		 // And back to original position
		 motor.rotate(-3/8);
	}
	
	

	private byte[] doubleStepDispensingSequence() {
		byte[] half_step_sequence = new byte[8];
        half_step_sequence[0] = (byte) 0b0001;
        half_step_sequence[1] = (byte) 0b0011;
        half_step_sequence[2] = (byte) 0b0010;
        half_step_sequence[3] = (byte) 0b0110;
        half_step_sequence[4] = (byte) 0b0100;
        half_step_sequence[5] = (byte) 0b1100;
        half_step_sequence[6] = (byte) 0b1000;
        half_step_sequence[7] = (byte) 0b1001;
        return half_step_sequence;
	}

}
