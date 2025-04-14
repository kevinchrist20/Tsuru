<script setup lang="ts">
import { RouterView } from 'vue-router';
</script>

<template>
  <main>
    <RouterView />
  </main>
</template>

<style>

body {
  background-color: theme('colors.dark-shade');
  color: theme('colors.text-color');
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

h1 {
  font-size: theme('fontSize.h1');
  font-weight: theme('fontWeight.bold');
}

h2 {
  font-size: theme('fontSize.h2');
  font-weight: theme('fontWeight.semibold');
}

h3 {
  font-size: theme('fontSize.h3');
  font-weight: theme('fontWeight.semibold');
}

h4 {
  font-size: theme('fontSize.h4');
  font-weight: theme('fontWeight.semibold');
}

h5 {
  font-size: theme('fontSize.h5');
  font-weight: theme('fontWeight.semibold');
}

h6 {
  font-size: theme('fontSize.h6');
  font-weight: theme('fontWeight.medium');
}

.subtitle1 {
  font-size: theme('fontSize.subtitle1');
  font-weight: theme('fontWeight.medium');
}

.subtitle2 {
  font-size: theme('fontSize.subtitle2');
  font-weight: theme('fontWeight.medium');
}

.body1 {
  font-size: theme('fontSize.body1');
  font-weight: theme('fontWeight.normal');
}

.body2 {
  font-size: theme('fontSize.body2');
  font-weight: theme('fontWeight.normal');
}

.button {
  font-size: theme('fontSize.button');
  font-weight: theme('fontWeight.medium');
}

.caption {
  font-size: theme('fontSize.caption');
  font-weight: theme('fontWeight.light');
}

.overline {
  font-size: theme('fontSize.overline');
  font-weight: theme('fontWeight.medium');
}
</style>