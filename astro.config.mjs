import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import starlightHeadingBadges from 'starlight-heading-badges'

// https://astro.build/config
export default defineConfig({
	site: 'https://subtixx.github.io',
	base: 'OmniCraft',
	integrations: [
		starlight({
			plugins: [starlightHeadingBadges()],
			title: 'Omnicraft Documentation',
			social: {
				github: 'https://github.com/subtixx/omnicraft',
			},
			sidebar: [
				{
					label: 'Guides',
					autogenerate: { directory: 'guides' },
				},
				{
					label: 'Items',
					autogenerate: { directory: 'items' },
				},
				{
					label: 'Blocks',
					autogenerate: { directory: 'blocks' },
				},
				{
					label: 'Recipes',
					autogenerate: { directory: 'recipes' },
				},
			],
		}),
	],
});
