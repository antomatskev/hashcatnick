# hashcatnick
P2P network for hashcat usage.

This project is meant for using [hascat](https://hashcat.net/hashcat/) in a peer to peer network. So
cracking passwords could be distributed between the network's users. Every user can propose own password crack.
There will be a live queue of passwords to crack. Also, we'll consider implementing rating
system to arrange this queue.

To start the main node start your jar file with argument main like this:
* java -jar hashcatnick.jar main

Also, you can choose a port on which to start, i.e.:
* java -jar hashcatnick.jar 1234
* java -jar hashcatnick.jar 4321 main