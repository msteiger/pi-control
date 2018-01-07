package de.msteiger.picontrol.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.RaspiPinNumberingScheme;

@Component
public class GpioService {

    private static final Logger logger = LoggerFactory.getLogger(GpioService.class);

    private static final int MIN_PIN = 0;
    private static final int MAX_PIN = 10;

    private final GpioController gpio;
    private final Map<Integer, GpioPinDigitalOutput> pins = new HashMap<>();

    public GpioService() {
        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
        gpio = GpioFactory.getInstance();
        for (int i = MIN_PIN; i <= MAX_PIN; i++) {
            GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(i), "MyPin", PinState.LOW);
            pin.setShutdownOptions(true, PinState.LOW);
            pins.put(i, pin);
        }

    }

    /**
     * @param id
     */
    public void toggle(int id) {
        GpioPinDigitalOutput pin = pins.get(id);
        if (pin != null) {
            logger.info("Pulsing pin for 1 second: " + id);
            pin.pulse(1000, false);
        } else {
            logger.warn("Invalid pin: " + id);
        }
    }

}
