import React from 'react';
import clsx from 'clsx';
import styles from './styles.module.css';

const FeatureList = [
  {
    title: 'Simple',
    Svg: require('@site/static/img/simplicity.svg').default,
    description: (
      <>
        Designed from the ground up for simplicity and with support for
        different programming styles.
      </>
    ),
  },
  {
    title: 'Predictable',
    Svg: require('@site/static/img/predictable.svg').default,
    description: (
      <>
        Let's you write reactive applications that behave consistently and are easy to debug and test.
      </>
    ),
  },
  {
    title: 'Composable',
    Svg: require('@site/static/img/composable.svg').default,
    description: (
      <>
        Grows with the complexity of the application and the size of the team.
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
