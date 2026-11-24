<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <main>
    <RouterView />
  </main>
</template>

<style>
body {
  background-color: var(--color-dark-shade);
  color: var(--color-text-color);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

h1 {
  font-size: var(--text-h1);
  font-weight: var(--font-weight-bold);
}

h2 {
  font-size: var(--text-h2);
  font-weight: var(--font-weight-semibold);
}

h3 {
  font-size: var(--text-h3);
  font-weight: var(--font-weight-semibold);
}

h4 {
  font-size: var(--text-h4);
  font-weight: var(--font-weight-semibold);
}

h5 {
  font-size: var(--text-h5);
  font-weight: var(--font-weight-semibold);
}

h6 {
  font-size: var(--text-h6);
  font-weight: var(--font-weight-medium);
}

.subtitle1 {
  font-size: var(--text-subtitle1);
  font-weight: var(--font-weight-medium);
}

.subtitle2 {
  font-size: var(--text-subtitle2);
  font-weight: var(--font-weight-medium);
}

.body1 {
  font-size: var(--text-body1);
  font-weight: var(--font-weight-normal);
}

.body2 {
  font-size: var(--text-body2);
  font-weight: var(--font-weight-normal);
}

.button {
  font-size: var(--text-button);
  font-weight: var(--font-weight-medium);
}

.caption {
  font-size: var(--text-caption);
  font-weight: var(--font-weight-light);
}

.overline {
  font-size: var(--text-overline);
  font-weight: var(--font-weight-medium);
}
</style>
