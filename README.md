# Collector route optimizer
Waste management as an IoT-enabled Service in Smart Cities

# Problem
One of the main concerns with our environment has been solid waste management, which impacts the
health and environment of our society.

The detection, monitoring and management of wastes is one of the primary problems of the present era.

The traditional way of manually monitoring the wastes in waste bins is a cumbersome process and utilizes
more human effort, time and cost which can easily be avoided with technology.

# Solution

Provide a real time indicator of the garbage level in a trashcan at any given time.

Using the data we can optimize waste collection routes and ultimately reduce fuel consumption. It allows
trash collectors to plan their daily/weekly pick up schedule.

# Methodology
An ultrasonic sensor will be placed on the interior side of the lid, the one facing the solid waste. As the
trash increases, the distance between the ultrasonic sensor and the trash decreases. This live data will be
sent to our micro-controller.

Micro-controller then processes the data and sends to cloud database with the help of WiFi module.

