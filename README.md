# SimpleVanish

Hide a player to make them appear offline. 

## Commands

### `/vanish` | Toggles vanish mode for a player.

- **Permission:** `vanish.command`
- **Aliases:** `/v`, `/simplevanish`, `/hide`, `/sv`

### `/fake` | Sends a fake join or leave message

- **Permission:** `vanish.command.fake`
- **Arguments:** 
  - Player: `/fake <leave|join>`
  - Console: `/fake <leave|join> <player>`

### `/vanish-reload` | Reloads the plugin configuration and messages.

- **Permission:** `vanish.reload`
- **Aliases:** `/vreload`

### `/vsettings` | Adjust personal settings for vanish.

- **Permission:** `vanish.command`
- **Aliases:** `/vsettings`, `/vset`, `/v-settings`
- **Subcommands:**
    - `attack-entities`
        - Can you attack entities while vanished?
    - `break-blocks`
        - Can you break blocks while vanished?
    - `invulnerability`
        - Are you invulnerable while vanished?
    - `silent-join`
        - Will your join message to the server be disabled?
    - `silent-leave`
        - Will your leave message from the server be disabled?
    - `mobs-target`
        - Will mobs target you while you're vanished?
    - `night-vision`
        - Will you get night vision while you're vanished?
    - `open-container`
        - Will you be able to open specific containers that play sounds and an animation while vanished?
    - `vanish-persist`
        - Will vanish persist between relogs
    - `pick-up-items`
        - Will you pick up items while vanished?
    - `notifications`
        - Will you get notified about other people vanishing, logging in silently, leaving silently

---

## Permissions

### View Permissions

| Permission             | What it do                                                 |
|------------------------|------------------------------------------------------------|
| `vanish.view`          | See other vanished players while they are vanished.        |
| `vanish.view.messages` | See messages when other players vanish or log in silently. |
| `vanish.view.tablist`  | See vanished members in the tablist.                       |
| `vanish.view.vanished` | Vanished users are not hidden from you.                    |

### Command Permissions

| Permission                  | What it do                         |
|-----------------------------|------------------------------------|
| `vanish.command`            | Use the `/vanish` command.         |
| `vanish.command.fake` | Use the `/fake leave` and `/fake join`command. |

### Settings Permissions

#### Core Settings

| Permission                          | What it do                               |
|-------------------------------------|------------------------------------------|
| `vanish.settings`                   | Access to all vanish settings.           |
| `vanish.settings.core`              | Base permission for core vanish toggles. |
| `vanish.settings.core.persist`      | Allows toggling whether vanish persists. |
| `vanish.settings.core.night-vision` | Grants night vision while vanished.      |
| `vanish.settings.core.silent-join`  | Join the server silently.                |
| `vanish.settings.core.silent-leave` | Leave the server silently.               |

#### Interaction Settings

| Permission                                    | What it do                                 |
|-----------------------------------------------|--------------------------------------------|
| `vanish.settings.interaction`                 | Base permission for interaction toggles.   |
| `vanish.settings.interaction.break-blocks`    | Toggle breaking blocks while vanished.     |
| `vanish.settings.interaction.open-containers` | Toggle opening containers while vanished.  |
| `vanish.settings.interaction.attack-entities` | Toggle attacking entities while vanished.  |
| `vanish.settings.interaction.pick-up-items`   | Toggle picking up items while vanished.    |
| `vanish.settings.interaction.mobs-target`     | Toggle whether mobs target while vanished. |

#### Admin Settings

| Permission                           | What it do                             |
|--------------------------------------|----------------------------------------|
| `vanish.settings.admin`              | Base permission for admin toggles.     |
| `vanish.settings.admin.invulnerable` | Toggle invulnerability while vanished. |

### Reload Permissions

| Permission      | What it do                                    |
|-----------------|-----------------------------------------------|
| `vanish.reload` | Reload the plugin configuration and messages. |

---

## Configuration File

### MySQL Configuration

| Option Name     | Type    | What it do                     |
|-----------------|---------|--------------------------------|
| `enabled`       | Boolean | Toggle MySQL usage.            |
| `ip`            | String  | MySQL server address.          |
| `database-name` | String  | Database name for vanish data. |
| `username`      | String  | MySQL username.                |
| `password`      | String  | MySQL password.                |

### Chat Settings

| Option Name               | Type    | What it do                                          |
|---------------------------|---------|-----------------------------------------------------|
| `prevent-direct-messages` | Boolean | Blocks vanilla direct messages to vanished players. |
| `fake-join-on-vanish`     | Boolean | Sends a fake join message upon vanishing.           |
| `fake-leave-on-vanish`    | Boolean | Sends a fake leave message upon vanishing.          |
| `custom-message`          | =-=-=-= | =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                  |
| `enabled`                 | Boolean | Enables custom join/leave messages.                 |
| `join`                    | String  | Format for custom join message.                     |
| `leave`                   | String  | Format for custom leave message.                    |

### Removal Settings

| Option Name                             | Type    | What it do                                                              |
|-----------------------------------------|---------|-------------------------------------------------------------------------|
| `remove-from.tablist`                   | Boolean | Removes vanished players from the in-game tab list.                     |
| `remove-from.server-list`               | Boolean | Removes vanished players from the server list you see before connecting |
| `remove-from.required-sleeping-players` | Boolean | Excludes vanished players from sleeping calculations.                   |

### View Settings

| Option Name                | Type    | What it do                                                               |
|----------------------------|---------|--------------------------------------------------------------------------|
| `view.change-tablist`      | Boolean | Updates the tablist for viewers with permission. Format is in locale.yml |
| `view.glow-while-vanished` | Boolean | Adds glow effect to vanished players for those with permission.          |

### Interaction Prevention

| Option Name       | Type          | What it do                                              |
|-------------------|---------------|---------------------------------------------------------|
| `prevent-opening` | Material List | Prevent opening the following containers while vanished |

### Miscellaneous

| Option Name                  | Type    | What it do                              |
|------------------------------|---------|-----------------------------------------|
| `remind-while-vanished`      | Boolean | Toggles reminders for vanished players. |
| `remind-interval-in-seconds` | Boolean | Interval for vanish reminders.          |

