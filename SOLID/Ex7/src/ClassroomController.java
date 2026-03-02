public class ClassroomController {

    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) {
        this.reg = reg;
    }

    public void startClass() {

        PowerControl projectorPower = reg.getDevice(PowerControl.class);
        InputConnectable projectorInput = reg.getDevice(InputConnectable.class);

        projectorPower.powerOn();
        projectorInput.connectInput("HDMI-1");

        BrightnessControl lights = reg.getDevice(BrightnessControl.class);
        lights.setBrightness(60);

        TemperatureControl ac = reg.getDevice(TemperatureControl.class);
        ac.setTemperatureC(24);

        AttendanceCapable scanner = reg.getDevice(AttendanceCapable.class);
        System.out.println("Attendance scanned: present=" + scanner.scanAttendance());
    }

    public void endClass() {
    System.out.println("Shutdown sequence:");

    for (PowerControl device : reg.getAll(PowerControl.class)) {
        device.powerOff();
    }
}
}