import { ToolRoute } from './Tool'

/**
 * Inline SVG path data per tool, rendered as stroke icons on 24x24 grids.
 */
export const toolIcons: Record<ToolRoute, string[]> = {
  [ToolRoute.Base64]: [
    'M4 7V5a1 1 0 0 1 1-1h2',
    'M20 7V5a1 1 0 0 0-1-1h-2',
    'M4 17v2a1 1 0 0 0 1 1h2',
    'M20 17v2a1 1 0 0 1-1 1h-2',
    'M8 12h8',
  ],
  [ToolRoute.Base64Image]: [
    'M3 5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z',
    'M8.5 10a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z',
    'm21 15-5-5L5 21',
  ],
  [ToolRoute.Slugs]: [
    'm9 15 6-6',
    'M11 5l2-2a4 4 0 0 1 5.66 5.66l-2 2',
    'M13 19l-2 2a4 4 0 0 1-5.66-5.66l2-2',
  ],
  [ToolRoute.UUID]: [
    'M4 9h16',
    'M4 15h16',
    'M10 3 8 21',
    'M16 3l-2 18',
  ],
}
