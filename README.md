# EventBridge -> SQS PingPong

Demo app that uses AWS EventBridge, AWS SQS, and Scala 3 to ping and pong an ever incrementing integer.

```
        _____ +1 _____
       /              \
      v                |
 |---------|      |-----------|
 | Primary |      | Secondary |
 |---------|      |-----------|
      |                ^
       \              /
        ------ +1 ----
```

SQS Queue -> Scala -> EventBridge -> SQS Queue ♲

## Running

```
sbt run
```

## EventBridge Rules

### Rule 1

`brandon_funland_primary`

#### Event Pattern

```json
{
  "source": [
    "funland-primary"
  ]
}
```

#### Target

|Type|Name|Input|
|----|----|-----|
|SQS queue|s2-BrandonsFunLand-secondary|Part of the matched event: $.detail|

### Rule 2

`brandon_funland_secondary`

#### Event Pattern

```json
{
  "source": [
    "funland-secondary"
  ]
}
```

#### Target

|Type|Name|Input|
|----|----|-----|
|SQS queue|s2-BrandonsFunLand|Part of the matched event: $.detail|
