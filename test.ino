#include <SoftwareSerial.h>
/* Programa el modulo bluetooth HC-06 con un nuevo: 
  NOMBRE  (Nombre de 20 caracteres)
  PIN     (Clave de cuatro numeros)
  BPS     (Velocidad de conexion en baudios)
  
  CONEXIONES:
  ARDUINO   BLUETOOTH
  5V        VCC
  GND       GND
  PIN 2     TX
  PIN 3     RX
 */
SoftwareSerial blue(2, 3);   //Crea conexion al bluetooth - PIN 2 a TX y PIN 3 a RX

int leds;
char NOMBRE[21]  = "Fred HC-06"; // Nombre de 20 caracteres maximo
char BPS         = '4';     // 1=1200 , 2=2400, 3=4800, 4=9600, 5=19200, 6=38400, 7=57600, 8=115200
char PASS[5]    = "1234";   // PIN O CLAVE de 4 caracteres numericos     
int estado = 'k';         // inicia apagado
int timeLoop = 500;
unsigned long TiempoAhora = 0;
 
void setup(){
    blue.begin(9600); // inicialmente la comunicacion serial a 9600 Baudios (velocidad de fabrica)
    
    blue.print("AT");  // Inicializa comando AT
    delay(1000);
 
    blue.print("AT+NAME"); // Configura el nuevo nombre 
    blue.print(NOMBRE);
    delay(1000);                  // espera 1 segundo
 
    blue.print("AT+BAUD");  // Configura la nueva velocidad 
    blue.print(BPS); 
    delay(1000);
 
    blue.print("AT+PIN");   // Configura el nuevo PIN
    blue.print(PASS); 
    delay(1000);  

    for(leds=4;leds<14;leds = leds +1){
      pinMode(leds,OUTPUT);
    }
      
}
 
void loop(){
 if(blue.available()>0){        // lee el bluetooth y almacena en estado
    estado = blue.read(); // aquí se almacena lo que llegue
  }
  if(estado=='a'){ 
    allLow();         
    digitalWrite(13,HIGH);   
  }
  if(estado=='b'){         
   digitalWrite(13,LOW);
  }
  if(estado=='c'){  
    allLow();        
    digitalWrite(4,HIGH);   
  }
  if(estado=='d'){          
   digitalWrite(4,LOW);
  }  
  if(estado=='e'){  
    allLow();         
    digitalWrite(5,HIGH);   
  }
  if(estado=='f'){          
   digitalWrite(5,LOW);
  }  
  if(estado=='g'){     
    allLow();      
    digitalWrite(8,HIGH);   
  }
  if(estado=='h'){          
   digitalWrite(8,LOW);
  }
  if(estado=='i'){    
    allLow();       
    digitalWrite(10,HIGH);   
  }
  if(estado=='j'){          
   digitalWrite(10,LOW);
  }
  if(estado=='k'){ 
    allLow();
  }
  if(estado=='l'){ 
    allHigh();
  }
  if(estado=='m'){
    timeLoop = 500;
    loopLeds();
  }
  if(estado=='o'){
    timeLoop = 1000;
    loopLeds();
  }
  if(estado=='p'){
    timeLoop = 1500;
    loopLeds();
  }
  if(estado=='q'){
    timeLoop = 2000;
    loopLeds();
  }
}

void loopLeds(){
    allLow();
    digitalWrite(4,HIGH);
    delay(timeLoop);
    digitalWrite(4,LOW);
    delay(timeLoop);
    digitalWrite(5,HIGH);
    delay(timeLoop);
    digitalWrite(5,LOW);
    delay(timeLoop);
    digitalWrite(8,HIGH);
    delay(timeLoop);
    digitalWrite(8,LOW);
    delay(timeLoop);
    digitalWrite(10,HIGH);
    delay(timeLoop);
    digitalWrite(10,LOW);
    delay(timeLoop);
    digitalWrite(13,HIGH);
    delay(timeLoop);
    digitalWrite(13,LOW);
}

void allLow(){
    digitalWrite(4,LOW);
    digitalWrite(5,LOW);
    digitalWrite(8,LOW);
    digitalWrite(10,LOW);
    digitalWrite(13,LOW);
}

void allHigh(){
    digitalWrite(4,HIGH);
    digitalWrite(5,HIGH);
    digitalWrite(8,HIGH);
    digitalWrite(10,HIGH);
    digitalWrite(13,HIGH);
}
