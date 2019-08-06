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
