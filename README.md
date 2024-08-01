# Block-Entity-Limiter
A plugin that limits the number of blocks or entity in each chunk.

**Config**
```yaml
# Block list: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
# Entity list: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/package-summary.html

# BLOCK_NAME/ENTITY_NAME: number_of_blocks_in_each_chunk
TNT: 10
MINECART: 100
SNOWBALL: 200

# Send this message to players if they bet more than the set number of blocks
Message: "You have reached the limit of this block or entity in this chunk."
```

# How to build plugin
Install maven
Open terminal and...
```
git clone https://github.com/HaiYTB/Block-Entity-Limiter && cd Block-Entity-Limiter
```

**Build command:**
```
mvn clean package
```
or
```
mvn clean build
```
