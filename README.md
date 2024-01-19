
Noted các bad practice gặp phải:
1. Trong quá trình implement logging thì trong dependency telegrambot đã có sẵn depen log và bị conflict với các dependency mới add vào nên phải exlcusion. 
Do đó file pom.xml mới có 1 loạt script exclusion

Happy case thì chỉ cần add 3 dependency như log4j-api, log4j-core, log4j-slf4j-impl

1 dependency để binding từ sl4j -> log4j nếu cần slf4j-api

1 dependency để đọc file yaml nếu dùng config file yml jackson-dataformat-yaml

cài maven helper plugin để check Dependency Analyzer cho dễ thấy các dependency bị conflict.