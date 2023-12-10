# Vwoops

Provides granular control over which blocks Endermen can pick up.

[![Static Badge](https://img.shields.io/badge/Mod_Loader-Fabric-lightyellow)](https://fabricmc.net/)
[![Static Badge](https://img.shields.io/badge/Requires-Fabric_API-lightyellow)](https://modrinth.com/mod/fabric-api/)
![Static Badge](https://img.shields.io/badge/Environment-Client%20or%20Server-purple)
![GitHub](https://img.shields.io/github/license/dicedpixels/vwoops?label=License)

<br>

### Usage

The mod uses two terms, **Allow** and **Deny**. Allowing a specific block will permit the Enderman to pick up the
specified block. Denying will stop the Enderman from picking up the specified block.

#### Client

Use the configuration screen to allow or deny blocks.

#### Server

Use the `/vwoops` command with any of the following sub-commands.

| Sub-command                 | Description                   |
|-----------------------------|-------------------------------|
| `allow <block: identifier>` | Allow block with specified ID |
| `allow *`                   | Allow all blocks              |
| `deny <block: identifier>`  | Deny block with specified ID  |
| `deny *`                    | Allow all blocks              |

Block identifiers are fetched from the `ENDERMAN_HOLDABLE` tag and are autocompleted and filtered based on the
sub-command.

All configurations are saved to `vwoops.json` in the `config` folder.

```json
{
  "first-run": false,
  "allowed-blocks": [
    "minecraft:allium",
    "minecraft:azure_bluet"
  ]
}
```