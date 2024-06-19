set positional-arguments

_help:
  @just -l

# Generates docs site using dokka
docs:
  #!/bin/bash
  set -euo pipefail

  ./dokka/gen.sh

# Runs tests
test:
  #!/bin/bash
  set -euo pipefail

  mvn test