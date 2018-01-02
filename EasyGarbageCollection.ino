#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>
#include <Adafruit_NeoPixel.h>
#include <time.h>
#include <NtpClientLib.h>

#define TRIGGERPIN D1
#define ECHOPIN    D2


#define FIREBASE_HOST "easy-garbage-collection-c4f8b.firebaseio.com"
#define FIREBASE_AUTH "****************************************"
#define WIFI_SSID "**********"
#define WIFI_PASSWORD "***********"


void setup()
{
// Debug console
  Serial.begin(9600);
  pinMode(TRIGGERPIN, OUTPUT);
  pinMode(ECHOPIN, INPUT);
 
 //connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  //NTP setup
  NTP.begin("pool.ntp.org", 1, true);
  NTP.setInterval(60000);
  NTP.begin("pool.ntp.org", 1, true);
  NTP.setInterval(60000);

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
 
}

void loop()
{

  long duration, distance;
  digitalWrite(TRIGGERPIN, LOW);  
  delayMicroseconds(3); 
  
  digitalWrite(TRIGGERPIN, HIGH);
  delayMicroseconds(12); 
  
  digitalWrite(TRIGGERPIN, LOW);
  duration = pulseIn(ECHOPIN, HIGH);
  distance = (duration/2) / 29.1;
  Firebase.set("Navigation/TC001/fillingLevel", distance);
  
  Serial.print(distance);
  Serial.println("Cm");

  Serial.print("Got NTP time: ");
  Serial.println(NTP.getTimeDateString());
  String lastUpdate = NTP.getTimeDateString();
  Firebase.setString("Navigation/TC001/lastUpdate", lastUpdate);

  delay(3500);

}
