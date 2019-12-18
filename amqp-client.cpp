//reference
//1 https://dzone.com/articles/playing-with-raspberry-pi-arduino-nodemcu-and-mqtt
//2 https://stackoverflow.com/questions/44865473/rabbitmq-with-arduino-uno

#include <SPI.h>
#include <PubSubClient.h>
#include <Ethernet.h>
#include <EthernetServer.h>
#include <EthernetClient.h>

//declare variables
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED  };
byte ip[] = { 192, 168, 0, 190 };                      // ip in lan (that's what you need to use in your browser. ("192.168.1.178")
byte gateway[] = { 192, 168, 0, 1 };                   // internet access via router
byte subnet[] = { 255, 255, 255, 0 };
byte server[] = { 192, 168, 0, 104 };

const char* topic = "potentiometer";

const char* clientName = "testvhost";
 
String stringone = "192.168.0.104:15672";

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.println(topic);
  //convert byte to char
  payload[length] = '\0';
  String strPayload = String((char*)payload);
  Serial.println(strPayload);
  int valoc = strPayload.lastIndexOf(',');
  String val = strPayload.substring(valoc+1);
  Serial.println(val);
}

EthernetClient ethClient;
PubSubClient client(server, 5672, callback, ethClient);

void wifiConnect() {
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);
    WiFi.begin(ssid, password);
    //-------
    Ethernet.begin(mac, ip, gateway, subnet);
    delay(1500);
    
    
    if (client.connect("myClientID")) {
        Serial.print("Connected to MQTT broker at ");
        Serial.print(server);
        Serial.print(" as ");
        Serial.println(clientName);
        Serial.print("Topic is: ");
        Serial.println(topic);
    }
    else {
        Serial.println("MQTT connect failed");
        Serial.println("Will reset and try again...");
        abort();
    }
}

void setup() {
    Serial.begin(9600);
    client.setServer(server, 1883);
    wifiConnect();
    delay(10);
}



void loop() {
  client.loop();
}
