#include <SPI.h>
#include <Ethernet.h>
#include <Servo.h>
int led = 4;
//Servo microservo;
int pos = 0;
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };   //physical mac address
byte ip[] = { 192, 168, 0, 190 };                      // ip in lan (that's what you need to use in your browser. ("192.168.1.178")
byte gateway[] = { 192, 168, 0, 1 };                   // internet access via router
byte subnet[] = { 255, 255, 255, 0 };                  //subnet mask
EthernetServer server(80);                             //server port
int arrSize=300;
char header[300];
int bufferSize = 0;
String level;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  pinMode(led, OUTPUT);
  //microservo.attach(7);
  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip, gateway, subnet);
  server.begin();
  Serial.print("server is at ");
  Serial.println(Ethernet.localIP());
}


void loop() {
  // Create a client connection
  EthernetClient client = server.available();
  if (client) {
    boolean currentLineIsBlank = true;
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        if (bufferSize < arrSize) header[bufferSize++] = c;
        
        //if HTTP request has ended
        if (c == '\n' && currentLineIsBlank) {
          Serial.println(header);
          if (strstr(header, "XXXXXX") != NULL) {
            client.println("HTTP/1.1 200 OK"); //send new page
            client.println("Content-Type: text/html");
            client.println("Connection: close");  // the connection will be closed after completion of the response
            client.println();
            client.println("<HTML>");
            client.println("<HEAD>");
            client.println("<meta name='apple-mobile-web-app-capable' content='yes' />");
            client.println("<meta name='apple-mobile-web-app-status-bar-style' content='black-translucent' />");
            client.println("<link rel='stylesheet' type='text/css' href='https://randomnerdtutorials.com/ethernetcss.css' />");
            client.println("<TITLE>Random Nerd Tutorials Project</TITLE>");
            client.println("</HEAD>");
            client.println("<BODY>");
            client.println("<H1>Random Nerd Tutorials Project</H1>");
            client.println("<hr />");
            client.println("<br />");
            client.println("<H2>Arduino with Ethernet Shield</H2>");
            client.println("<br />");
            client.println("<a href=\"/?button1on\"\">Pump On</a>");
            client.println("<a href=\"/?button1off\"\">Pump Off</a><br />");
            client.println("<br />");
            client.println("<br />");
            client.println("<p>Created by Rui Santos. Visit https://randomnerdtutorials.com for more projects!</p>");
            client.println("<br />");
            client.println("</BODY>");
            client.println("</HTML>");
            //controls the Arduino if you press the buttons
            if (strstr(header, "?button1on") != NULL) {
              if (digitalRead(led) == LOW) {
                Serial.println("from low to high");
                digitalWrite(led, HIGH);
              }
            }
            if (strstr(header, "?button1off") != NULL) {
              if (digitalRead(led) == HIGH) {
                Serial.println("from high to low");
                digitalWrite(led, LOW);
              }
            }

            if (digitalRead(led) == HIGH) {
              client.println("ON");
            } else  {
              client.println("OFF");
            }

          } else {
            // wrong user/pass
            client.println("HTTP/1.1 401 Unauthorized");
            //client.println("HTTP/1.1 401 Authorization Required");
            client.println("WWW-Authenticate: Basic realm=\"Secure\"");
            client.println("Content-Type: text/html");
            client.println("Cache-Control: no-cache, no-store, must-revalidate");
            client.println("Pragma: no-cache");
            client.println("Expires: 0");
            client.println();
            client.println("<html>Text to send if user hits Cancel button</html>"); // really need this for the popup!
          }
          bufferSize = 0;
          StrClear(header, arrSize);
          break;
        }
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
        }
        else if (c != '\r') {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    Serial.println("client disconnected");
  }
}

// sets every element of str to 0 (clears array)
void StrClear(char *str, int length) {
  for (int i = 0; i < length; i++) {
    str[i] = 0;
  }
}
