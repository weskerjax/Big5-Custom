
專案 [ Big5-Custom ]
===================================================

Big5-Custom 主要在解決自造字對應的問題，基礎的 Big5 有太多字無法對應上，而香港或其他擴充字集又無法準確提供完整的對應。



程式邏輯概觀
===================================================

從對應表 moz18-b2u.txt, moz18-u2b.txt 產生 Java 用的字元編碼器，利用高低位元建立二維的對應表，使用二維方式可以降低記憶體用量，因為對應表的字有很多都是空缺的。
 
Big5 到 Unicode 只會用到 moz18-b2u.txt，但 Unicode 到 Big5 則會用到 moz18-u2b.txt 加 moz18-b2u.txt，為了彌補自訂編碼的對應，所以在自訂編碼的定義只要修改 moz18-b2u.txt 即可。



目錄描述
===================================================

/src/org/orion/nio.charset
: 	字元編碼器主程式目錄

/src/generator
: 	用來產生編碼程式的工具目錄，之後不需要封裝到 Big5-Custom.jar 裡    



程式描述
===================================================

org.orion.nio.charset.Big5_Custom.java
: 	編碼器主程式，透過產生器產生

org.orion.nio.charset.CharsetProvider.java
: 	字元編碼器連結器，用來向 JVM 提交自訂的編碼器
    
generator.Big5_Custom_Test.java
: 	用來測試編碼器是否正確

generator.Big5_Custom.java
: 	編碼器主程式樣版

generator.Generator.java
: 	編碼程式產生器，會去解析對應表 moz18-b2u.txt, moz18-u2b.txt 來產生轉換陣列

generator.moz18-b2u.txt
: 	Big5 到 Unicode 對應表

generator.moz18-b2u.txt.bak
: 	Big5 到 Unicode Mozilla 1.8 原始對應表(backup)

generator.moz18-u2b.txt
: 	Unicode 到 Big5 對應表

generator.moz18-u2b.txt.bak
: 	Unicode 到 Big5 Mozilla 1.8 原始對應表(backup)



部屬 Big5-Custom.jar
===================================================

請將 Big5-Custom.jar 複製到 `C:\Program Files\Java\jre7\lib\ext` 及 `C:\Program Files (x86)\Java\jre7\lib\ext`



Mozilla 1.8 對應表來源
===================================================

Mozilla 1.8 Charset <http://moztw.org/docs/big5/>



網路文件備註
===================================================

> You should make sure the jar is loaded by the main classloader. You can achieve this by adding the jar to the JVM's @jre/lib/ext@ extension directory, or by adding it to the classpath of the main program. For example if you are using Tomcat, add @-cp /path/to/jutf7.jar@ to Tomcat's startup script. <br/>
> @env JAVA_OPTS="-cp /path/to/jutf7-1.0.0.jar" scala@


