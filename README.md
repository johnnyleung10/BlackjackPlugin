# Blackjack Plugin

## About
A Minecraft version of Blackjack played straight from the inventory. Features a live chip betting system, a customizable dealer, and more. https://www.spigotmc.org/resources/blackjack-overdrive.72121/

## Installing
Take the provided "BlackjackPlugin.jar" and place it in the "/plugins" folder and the rest is automatic. Make sure you have the required plugins under "Dependencies". If you wish to do custom configuration changes, see "Configuration".

## Permissions
WIP

## Building
The plugin comes with all the necessary libraries to build from (under "/lib") and classpaths. Recommended IDE is Intelli J Idea.

## Dependencies
The Blackjack Plugin is mainly built from the Spigot Plugin with the economy compatibility system from Vault. In order to run it on a server, the Vault plugin is needed as well as an economy system listed under its GitHub.

Links:
- https://getbukkit.org/download/spigot
- https://www.spigotmc.org/resources/vault.34315
- https://github.com/MilkBowl/Vault

## Advanced Configuration
All configurations are found in the config.yml file in "/plugins/BlackjackPlugin/".

Edit the name of the GUI.
```
BlackjackGUI: 
  Title: '&5&lBlackjack'
```

Change the minimum bet required.
```
BlackjackGame:
  MinBet: 10
```

Change the minimum buy-in required.
```
BlackjackGame:
  MinBuyIn: 10
```

Change the skin of the dealer by putting the specified username under "Head:". Change the name of the dealer under "Name:".
```
BlackjackGame:
 Dealer:
    Head: 'BruceWayne'
    Name: '&bDealer Bruce'
