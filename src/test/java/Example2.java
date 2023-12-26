import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import net.java.games.input.usb.UsagePage;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
public class Example2 {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "mid")
    String mid;
    @Property(name = "pid")
    String pid;

    int vendorId;
    int productId;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);

            vendorId = Integer.decode(mid);
            productId = Integer.decode(pid);
        }
    }

    @Test
    @DisplayName("purejavahidapi sample powered by hid4java api")
    void test1() throws Exception {

        HidDevice dev = null;

        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
        // Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);

        // Get HID services using custom specification
        HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);

        hidServices.start();

        // Provide a list of attached devices
Debug.println("scanning");
        for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
            System.out.println(hidDevice.getManufacturer() + "/" + hidDevice.getProduct() + " ... usagePage: " + UsagePage.map(hidDevice.getUsagePage()));
            if (hidDevice.getVendorId() == vendorId && hidDevice.getProductId() == productId) {
                dev = hidDevice;
                break;
            }
        }

        if (dev == null) {
            throw new IllegalStateException("no device for vid: " + vendorId + ", pid: " + productId);
        }

Debug.println("device found: " + dev.getProduct() + ", " + dev.getPath());
        dev.open();

        AtomicInteger c = new AtomicInteger();
        dev.addInputReportListener(e -> {
            System.out.printf("onInputReport[%3d]: id %d len %d data ", c.getAndIncrement(), e.getReportId(), e.getReport().length);
            for (int i = 0; i < e.getReport().length; i++)
                System.out.printf("%02X ", e.getReport()[i]);
            System.out.println();
        });

        Thread.sleep(3 * 60 * 1000);
    }
}
