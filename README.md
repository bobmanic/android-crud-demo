# android-crud-demo
Experiments with REST Web Services consumed by Android.

This project is dependent on `https://github.com/ssxv/micro-services-crud-demo.
Before you can start this, you need to set that up.


## You'll need
* Android studios

## To get this running
* Add external dependencies from ```app/libs``` folder.
* In the ```com.satyendra.constants.Constants.java```, set the ```URL``` to the URL of the ```micro-services-crud-demo```.
```
use "ipconfig" in command prompt or "ifconfig" in termanal to get the IP address of your machine.

In Constants.java
public static final String URL = "http://<your-IP-address>:8080/student/";
``` 
* Make sure you have ```micro-services-crud-demo``` up and running.
* That's it.
* If you decide to run it on an external device, make sure the device is connected to same network as that of your machine running the ```micro-services-crud-demo```.