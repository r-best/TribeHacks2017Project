# TribeHacks2017Project
## Alternate Title: Have at thee, Todd Howard

The goal of this 2D-sidescrolling platformer game is to defeat Todd Howard and take his place as director of Bethesda Game Studios

You will traverse through the different rooms, eventually reaching the room where Todd Howard awaits. You and Todd are both equipped with the same basic close-range attack (press E), although his reaches a bit further. Todd also has the assistance of [the Mysterious Stranger](http://fallout.wikia.com/wiki/Mysterious_Stranger_(character)), who has a small chance to appear and help him out every time he attacks you.

In the future I hope to implement a wider variety of attacks for both Todd and the player, as well as better AI and a wider variety of movement patterns for Todd, and maybe a few minibosses before him. Todd also has access to the game's developer console; He is able to do a variety of things through it, such as changing the player's walking/running speed, altering the intensity of gravity, spawning in new entities, and many other fun tricks. The only command he doesn't have access to is the one used to spawn in Todd Howards. We don't need dozens of Todds duplicating themselves.

## Controls
- **Movement**: 'A' and 'D' keys
- **Jump**: 'W' or Spacebar
- **Run**: Hold Shift
- **Talk to NPCs / Close-range attack**: 'E'
- **Go through doors**: 'S' (unimplemented, currently you only have to touch the door)
- **Open developer console**: '~' (located below Escape key)
- **Exit game**: Escape

## Developer Console Commands
- **settargetfps [number]**
  - Sets the game's target FPS, use with caution
- **spawn [entity name] [x] [y] [quantity]**
  - Spawns the given entity at the given x and y coordinates
  - Current compatible entities are 'npc' and 'todd'
  - 'Quantity' is optional (defaults to 1)
    - if specified, each entity will be spawned slightly above the one before it to avoid spawning entities inside one another
- **event [type] [args...]**
  - Initiates an event of the given type with the given parameters
  - Only current compatible type is 'dialogue'; all args following will become part of the text box content
- **gravity [option]**
  - Has 4 options:
    - *restore* : Restores gravity to its default state
    - *magnify [multiplier]* : Multiplies gravity by the given number
    - *reverse* : Reverses the pull of gravity
    - *zero* : Sets gravity to zero
- **player [option]**
  - Has 3 options:
    - *flipcontrols* : Swaps the functions of the player's up & down and left & right keys
    - *multiplyspeed [multiplier]* : Multiplies the player's speed by the given number
    - *multiplyjump [multiplier]* : Multiplies the player's jump height by the given number

### Special Thanks to:
- Todd Howard
- [The McElroy brothers](https://www.youtube.com/watch?v=_1jGnFt78H8)
