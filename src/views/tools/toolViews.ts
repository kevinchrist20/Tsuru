import type { Component } from 'vue'
import { ToolRoute } from '../../models/Tool'
import Base64View from './Base64View.vue'

/**
 * Registry of implemented tool views.
 *
 * Add new tools here as they are built. Any route without an entry falls back to
 * `ComingSoonView` so unfinished tools never render the wrong screen.
 */
export const toolViews: Partial<Record<ToolRoute, Component>> = {
  [ToolRoute.Base64]: Base64View,
}
