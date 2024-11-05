# Vwoops

Provides granular control over which blocks Endermen can pick up.

[![Static Badge](https://img.shields.io/badge/Mod_Loader-Fabric-lightyellow)](https://fabricmc.net/)
[![Static Badge](https://img.shields.io/badge/Requires-Fabric_API-lightyellow)](https://modrinth.com/mod/fabric-api/)
![Static Badge](https://img.shields.io/badge/Environment-Client%20or%20Server-purple)
![GitHub](https://img.shields.io/github/license/dicedpixels/vwoops?label=License)

## Usage

The mod expands upon the `minecraft:enderman_holdable` block tag and provides granular control of blocks in this tag without modifying the tag itself.

Blocks can be added to a list of "holdable" blocks. Adding a block to this list will permit Endermen to pick up the block. Removing a block from this list will stop Endermen from picking up the block.

### Single-player

Since block tags can be specified per world through data packs, single-player worlds are configured on a per-world basis. Use the `/vwoops` command to open the config screen.

### Server

Use the `/vwoops` command with any of the following sub-commands.

| Sub-command                 | Description                                                                 |
|-----------------------------|-----------------------------------------------------------------------------|
| `add <block-identifier>`    | Adds the block with the specified ID to the list of "holdable" blocks.      |
| `add *`                     | Adds all blocks to the list of "holdable" blocks.                           |
| `remove <block-identifier>` | Removes the block with the specified ID from the list of "holdable" blocks. |
| `remove *`                  | Removes all blocks from the list of "holdable" blocks.                      |

Block identifiers are fetched from the `minecraft:enderman_holdable` block tag and are autocompleted and filtered based on the
sub-command.

Configurations are saved to `vwoops.json` in the root of the world directory.

```json
[
  "minecraft:allium",
  "minecraft:azure_bluet"
]
```
