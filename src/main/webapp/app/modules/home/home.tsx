import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { translate } from 'react-jhipster';
import { EntitiesMenu } from '../../shared/layout/menus/entities';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      {/* <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col> */}
      {/* <span className="hipster rounded" /> */}
      <Col md="12">
        <h2>
          <Translate contentKey="home.title">Citizenship Action</Translate>
        </h2>
        {account?.login ? (
          <div>
            <p className="lead">Esta é a página dos módulos</p>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
            <div className="">
              <EntitiesMenu
                // icon="th-list"
                // name={translate('global.menu.entities.main')}
                // id="entity-menu"
                data-cy="entity"
                // style={{ maxHeight: '80vh', overflow: 'auto' }}
              />
            </div>
          </div>
        ) : (
          // <div>
          //   <Alert color="warning">
          //     <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

          //     <Link to="/login" className="alert-link">
          //       <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
          //     </Link>
          //     <Translate contentKey="global.messages.info.authenticated.suffix">
          //       , you can try the default accounts:
          //       <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
          //       <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
          //     </Translate>
          //   </Alert>

          //   <Alert color="warning">
          //     <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
          //     <Link to="/account/register" className="alert-link">
          //       <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
          //     </Link>
          //   </Alert>
          // </div>
          <>
            <div className="jumbotron">
              <hr className="my-3"></hr>
            </div>
            <section className="container">
              <div className="row">
                <div className="col-md-5">
                  <img src="content/images/logoAcao.png" alt="Logo" />
                </div>
                <div className="col-md-6">
                  <h3 className="titleDois">
                    Olá, somos a <span className="font-weight-bold">Ação Cidadania!</span>
                  </h3>
                  <p className="p">
                    A ONG auxilia várias famílias carentes na entrega de alimentos e cestas básicas. Atuante na região Sul de Minas Gerais,
                    o movimento segue ativo e presente em mais de 10 cidades. Além da doação de comida aos que precisam, também há a
                    preocupação por parte da organização em manter as pessoas informadas a respeito de seus direitos.
                  </p>
                  <a href="/#" className="buttonStyle">
                    Saiba mais
                  </a>
                </div>
              </div>
            </section>
          </>
        )}
        {/* <p>
          <Translate contentKey="home.question">If you have any question on JHipster:</Translate>
        </p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.homepage">JHipster homepage</Translate>
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.stackoverflow">JHipster on Stack Overflow</Translate>
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.bugtracker">JHipster bug tracker</Translate>
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.chat">JHipster public chat room</Translate>
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.follow">follow @jhipster on Twitter</Translate>
            </a>
          </li>
        </ul>

        <p>
          <Translate contentKey="home.like">If you like JHipster, do not forget to give us a star on</Translate>{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p> */}
      </Col>
    </Row>
  );
};

export default Home;
