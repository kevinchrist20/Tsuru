import type { Component } from 'vue'
import { ToolRoute } from '../../models/Tool'
import Base32View from './Base32View.vue'
import Base64ImageView from './Base64ImageView.vue'
import Base64View from './Base64View.vue'
import BinaryView from './BinaryView.vue'
import HexView from './HexView.vue'
import HtmlView from './HtmlView.vue'
import JwtView from './JwtView.vue'
import SlugView from './SlugView.vue'
import UrlView from './UrlView.vue'
import UuidView from './UuidView.vue'

/**
 * Registry of implemented tool views.
 *
 * Add new tools here as they are built. Any route without an entry falls back to
 * `ComingSoonView` so unfinished tools never render the wrong screen.
 */
export const toolViews: Partial<Record<ToolRoute, Component>> = {
  [ToolRoute.Base64]: Base64View,
  [ToolRoute.Base64Image]: Base64ImageView,
  [ToolRoute.Url]: UrlView,
  [ToolRoute.Html]: HtmlView,
  [ToolRoute.Hex]: HexView,
  [ToolRoute.Binary]: BinaryView,
  [ToolRoute.Base32]: Base32View,
  [ToolRoute.Slugs]: SlugView,
  [ToolRoute.UUID]: UuidView,
  [ToolRoute.Jwt]: JwtView,
}
