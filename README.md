# Controllable-Thread

Controllable Thread is essentially a class that wraps around Java's thread class. It uses an interface called Controllable with render and update methods. The render method is passed an interpolation value.
A control loop runs inside the thread, calling update and render as specified.
