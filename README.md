# Command Runner
An easy way to run a massive amount of commands.

## Installation
Just downloaded the JAR from the releases tab and drop it into your plugins folder and start the server, edit `config.yml`, add your commands to the `commands` file, reload, enjoy.

## Configuration
### Threshold (`threshold`)
How many times the command should run before it fails out completely. If it suceeds, it will only run once. This option **must** be greater than or equal 1 or the plugin will fail.

### Clear Commands (`clear-on-finish`)
If `true`, the plugin will empty out the commands file once it's done.

### Dispatch As (`dispatch-as`)
This will determine who to run the command as. If a username, it will try to get the player...best to leave this to `console`.

### Use Placeholders (`use-placeholders`)
Enable the use of the `placeholders`.

### Placeholders (`placeholders`)
This is a list of placeholders you can use in the plugin. Say you have a group called `premium`, you can add the placeholder...
```
- "%p%:premium`
```
So you only have to it all in one place in case you ever change that `premium` to `vip`. That's not a super practical use, but you get where I'm going. Usually people won't need this, as having a lot could cause lag in the startup of the server.


## Donate
### If you're enjoying my work, please consider donating to help me continue doing this kind of work.
[![Donate](https://az743702.vo.msecnd.net/cdn/kofi1.png?v=f)](https://ko-fi.com/636QU7F12V5F)
