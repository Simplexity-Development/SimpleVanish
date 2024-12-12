/vanish -> Enables/Disables vanish state

## Vanish State:

### `for people who do not have permission`:
**On enable:**
- Hides the player
- Removes the player from the tab list
  - Flag or Setting to prevent this
- Sends a 'fake leave' message
  - Should have a `-silent` flag to prevent message

**On disable:**
- Shows the player
- Adds the player back to the tab list
- Sends a 'fake join' message
  - Should have a `-silent` flag to prevent message

### `for people who have view permission`:
**On enable:**
- Adds the configured prefix to the tab list name
  - Config should have option to disable and configure the message
- Send message that the other person vanished
  - This should have a Setting to disable

**On disable:**
- Remove configured prefix
- Send message that the other person has un-vanished
  - This should have a Setting to disable

### `for Player`:
**On enable:**
- Adds glow
  - Should have a *Config* option
- Set invulnerable
  - Setting
  - Permission
- Give Night Vision
  - Setting
  - Permission

**On disable:**
- Remove glow
- Set invulnerable false
- Remove night vision
- check not from potion/is infinite/something

### `Settings Listeners`:
- Checks:
  - is instance of Player? (if applicable)
  - is vanished?
  - has permission to disable this check **and** check is disabled?
  - cancel event
- Containers
  - Run checks
  - Prevent open animation, open inventory for player without the animation
- Break Blocks
  - Run checks
- Mob Target
  - Run checks
- Attack Entity
  - Run checks
  - if Entity not living, check permissions and settings for block break instead
- Pick Up Item
  - Run checks
  
### `Other Listeners`:
- Join Server
  - Does not have `view` permission? There are users who are vanished?
    - Hide vanished users
    - Remove vanished user from tab list
  - Has `vanish` permission? Was vanished? Has `persist` permission? Has Persist Setting enabled?
    - Run vanish event
    - Cancel join message
    - Send users with `view` permission custom join message
  - Has `silent join` permission?
    - Cancel join message
    - Send users with `view` permission custom join message
- Leave Server
  - Is vanished?
    - Remove from cache
    - Cancel leave message
    - Send users with `view` permission custom leave message
  - Has `silent leave` permission?
    - Cancel leave message
    - Send users with `view` permission custom leave message

## Commands

**Non-Setting Commands**

- `/fake <leave | join>`
  - Will call the 'fake leave event' or 'fake join event'
  - Will display the exit message
  - Does not require being vanished
- `/vreload`
  - Reloads the configuration and Locale

**Settings**
- `/vsetting [setting]`
  - If no setting is provided it will output the current settings
  - if player has permission add subcommand to autofill
  - save setting changes to SQL and update cache
- `/vanish`
  - Technically a settings change. Does not extend the subcommand but it does update the Vanish Settings
	 


  

  
