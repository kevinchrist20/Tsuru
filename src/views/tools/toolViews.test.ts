import { describe, expect, it } from 'vitest'
import { ToolRoute } from '../../models/Tool'
import { toolViews } from './toolViews'

describe('toolViews registry', () => {
  it('has a view registered for every tool route', () => {
    for (const route of Object.values(ToolRoute)) {
      expect(toolViews[route], `missing view for ${route}`).toBeDefined()
    }
  })
})
