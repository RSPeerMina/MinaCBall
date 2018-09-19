# MinaCBall
Cannonball smelter for RSPeer

Currently very simple, no GUI no progress log. It does however print what it's doing to log, which I used to debug and left it in.

Features:
- Low sleep time for efficiently, but not too low it should warrant insta ban
- Toggle run if above 20 % and not smelting/banking
- Withdraw mould at startup if not in invent, stops script if none found in bank
- Stops when out of Steel bars

Possible bug that needs testing:

- Might stop doing anything on leveling up

Instruction:
- Download MinaCBall.class and place it in RSPeer\scripts folder, tick "local scripts" on script selector to find it
- Preferably start the script at Edgeville
- Make sure you have Ammo mould either in the bank or inventory

