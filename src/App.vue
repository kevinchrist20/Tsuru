<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterView } from 'vue-router'
import UpdateDialog from './components/UpdateDialog.vue'
import { useUpdater } from './composables/useUpdater'

const updater = useUpdater()

onMounted(() => {
  void updater.initialize()
})
</script>

<template>
  <RouterView v-slot="{ Component, route }">
    <Transition name="page" mode="out-in">
      <component :is="Component" :key="route.path" />
    </Transition>
  </RouterView>

  <UpdateDialog />
</template>
