// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');
``
/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Kotlin Bloc',
  tagline: 'A simple, adaptable, predictable and composable UI framework for Kotlin Multiplatform',
  url: 'https://1gravity.github.io/',
  baseUrl: '/Kotlin-Bloc/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: '1gravity', // Usually your GitHub org/user name.
  projectName: 'Kotlin-Bloc', // Usually your repo name.
  trailingSlash: false,

  // Even if you don't use internalization, you can use this field to set useful
  // metadata like html lang. For example, if your site is Chinese, you may want
  // to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: 'Kotlin Bloc',
        logo: {
          alt: 'Kotlin Bloc Logo',
          src: 'img/logo.png',
        },
        items: [
          {
            type: 'doc',
            docId: 'getting_started/overview',
            position: 'left',
            label: 'Getting Started', 
          },
          {
            type: 'doc',
            docId: 'architecture/architecture_overview',
            position: 'left',
            label: 'Concepts', 
          },
          {
            type: 'doc',
            docId: 'extensions/overview',
            position: 'left',
            label: 'Extensions', 
          },
          {
            type: 'doc',
            docId: 'examples/examples',
            position: 'left',
            label: 'Examples', 
          },
          {
            href: 'https://github.com/1gravity/Kotlin-Bloc',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Docs',
            items: [
              {
                label: 'API Documentation',
                to: 'https://1gravity.github.io/Kotlin-Bloc/dokka/',
              },
            ],
          },
          {
            title: 'Community',
            items: [
              {
                label: 'Stack Overflow',
                href: 'https://stackoverflow.com/questions/tagged/kotlin-bloc',
              }
            ],
          },
          {
            title: 'More',
            items: [
              {
                label: 'GitHub',
                href: 'https://github.com/1gravity/Kotlin-Bloc',
              }
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Emanuel Moecklin<br>Built with Docusaurus`,
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
      },
    }),
};

module.exports = config;
