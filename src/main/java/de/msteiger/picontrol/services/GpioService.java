package de.msteiger.picontrol.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GpioService {

    private static final Logger logger = LoggerFactory.getLogger(GpioService.class);

//    private final GpioController gpio = GpioFactory.getInstance();
//    private final GpioPinDigitalOutput pin1;
//
//    public GpioService() {
//
//        // provision gpio pin #01 as an output pin and turn on
//        pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyPin", PinState.LOW);
//
//        // set shutdown state for this pin
//        pin1.setShutdownOptions(true, PinState.LOW);
//    }
//
//    /**
//     * @param id
//     */
    public void toggle(String id) {
        logger.debug("Pulsing pin " + id);
//        pin1.pulse(1000, false);
    }

}
