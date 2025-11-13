# 🃏UNO Game
A console-based game written in java. It enables up to 8 players to be able to play in a local network (LAN). Using only java's standard libraries.

# 🎮Features
- Supports **up to 8 players** (the code can be edited to take more but remember there are only 108 cards)
- No GUI. Fully **Console-based**
- Used only **Java Standard Libraries**
- Classic UNO rules with action cards (Skip, Draw 2 , Wild Cards ,etc.)

![Gameplay GIF](docs/gameplay.gif)

# ⚙️How To Run
1. Clone/Download this repo.
2. Compile all `.java` files inside `/src/`:
```bash
javac *.java
```
3. Start the server first
```bash
java Server
```
4. Start The clients on other devices or the same server device.
```bash
java Client
```
5. Run the client file, Input a name and the host's local ip. Once the client joins, It will clear the screen for them.
6. When you're ready to start type `start` inside the server terminal.

# 🕹️How to Play
- Clients can see only their cards, the last played card, current Players and their cards count
- When it's your turn place a card by typing its index (Starting from 0)
- If you don't have any card with same color or number/value, You can draw a card by typing `draw`
- If you have 2 cards , You have to say UNO before placing your card. You can do that by typing `uno n` where `n` is the index of the card you want to place
- If you noticed someone placed the card without saying UNO (It hasn't been implemented in code. But since it's local you can use your voice) ,You can make them draw 2 cards by typing `uno` before the next player starts their turn.
- Place all your cards and you win

## 🐛Note
The code has some bugs that you might encounter but It shouldn't be game breaking. Mostly some unhandled scenarios (for example typing a color number that doesnt exist)
If you find any of these bugs making the game unplayable ,Please tell me so I can fix it ASAP.

## License
This project is licensed under the [MIT License](LICENSE).

