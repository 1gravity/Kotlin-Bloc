/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */

// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  // By default, Docusaurus generates a sidebar from the docs folder structure
  // tutorialSidebar: [{type: 'autogenerated', dirName: '.'}],

  // this is how we would create a grouped sidebar ('Architecture' as group label)
  // architectureSidebar: {
  //   'Architecture': ['architecture/architecture_overview', 'architecture/bloc/bloc', 'architecture/bloc/bloc_example'],
  // },

  "introSidebar": [
    {
      "type": "doc",
      "id": "introduction/intro"
    }
  ],
  "architectureSidebar": [
    {
      "type": "doc",
      "id": "architecture/architecture_overview"
    },
    {
      "Bloc": [
        {
          "type": "doc",
          "id": "architecture/bloc/bloc"
        },
        {
          "type": "doc",
          "id": "architecture/bloc/reducer"
        },
        {
          "type": "doc",
          "id": "architecture/bloc/thunk"
        },
        {
          "type": "doc",
          "id": "architecture/bloc/initializer"
        },
        {
          "type": "doc",
          "id": "architecture/bloc/bloc_builder"
        },
        {
          "type": "doc",
          "id": "architecture/bloc/lifecycle"
        },
        {
          "type": "doc",
          "id": "architecture/bloc/bloc_context"
        },
      ]
    },
    {
      "Bloc State": [
        {
          "type": "doc",
          "id": "architecture/blocstate/bloc_state"
        },
        {
          "type": "doc",
          "id": "architecture/blocstate/bloc_state_builder"
        }
      ]
    },
    {
      "Bloc Owner et al.": [
        {
          "type": "doc",
          "id": "architecture/blocowner/bloc_owner"
        },
        {
          "type": "doc",
          "id": "architecture/blocowner/bloc_observable"
        }
      ]
    },
  ],
  "extensionsSidebar": [
    {
      "type": "doc",
      "id": "extensions/overview"
    },
    { 
      "Android": [
        {
          "type": "doc",
          "id": "extensions/android/android_bloc_context"
        },
        {
          "type": "doc",
          "id": "extensions/android/android_subscription"
        }
      ]
    },
    {
      "type": "doc",
      "id": "extensions/ios"
    },
    {
      "type": "doc",
      "id": "extensions/redux"
    }
  ],
  "exampleSidebar": [
    {
      "type": "doc",
      "id": "examples/examples"
    }
  ]

  // But you can create a sidebar manually
  /*
  tutorialSidebar: [
    {
      type: 'category',
      label: 'Tutorial',
      items: ['hello'],
    },
  ],
   */
};

module.exports = sidebars;
