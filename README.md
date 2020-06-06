# FiveChess

This is a simple implementation of the game FiveChess, based on the codes architecture from [the game repository](https://github.com/vencc/game), and further developed by [Yuxi XIE](https://github.com/YuxiXie) and [Jingyi LIU](https://github.com/Jingyi-Liu).

## Run to Start Game

```shell
git clone https://github.com/YuxiXie/FiveChess.git
cd FiveChess
```

and then just run from the `src/frontend.Main`.

## Function Introduction

#### Home Page

We provide two options of the game mode: `Two-Players` / `Single-Player`

![Home Page](images/homepage.png)

#### Rule Page

If choosing `Two-Players` mode, we provide two options for the rule mode: `forbid-hand` / `normal`

![Home Page](images/rulepage.png)

#### Room Page

Below is an example of the room page in `forbid-hand` & `Two-Players` mode. The White needs to make her own judgement on whether the Black is forbidden. 

![Home Page](images/roompage.png)

If it's `normal` mode, when the global steps turns to 3 (2 black 1 white), the White needs to choose whether to switch identities.

![Home Page](images/roompage_norm.png)

If it's `Single-Player` mode, ths White is set as the Robot we developed, and the rule is the most primary here, _i.e._ no switch & no forbidden.

![Home Page](images/roompage_robot.png)