# Natural Progression Changelog (1.19.4)

## 2.3.5

### Initial Port to 1.19.4

Woof, that was a pain in the butt. Nothing major, just lots of internal changes I had to make because of changes Mojang
and/or Forge made to:

- Damage Sources (used for splintering & crushing damage when punching w/o the right tool)
    - Requires a freaking access transformer to use. Come on Mojang, this is unacceptable.
- Creative Tabs
    - Not only was it pain to a custom tab at all, but now I'm required to sort things manually rather than sorting by
      order it was added in code, which sucks. I hope you at least like the current creative tab sorting I implemented
      🤞
- World Gen
    - I had done a lot of stuff with `DeferredRegistry<?>` which I could no longer use and had to manually port as a
      datapack

Overall, 0/10 would not recommend. I hope you enjoy though, and give me shout on
the [bug tracker](https://github.com/oitsjustjose/natural-progression/issues) if you see anything not behaving!