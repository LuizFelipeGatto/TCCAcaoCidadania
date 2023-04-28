import React from 'react';
import { Translate } from 'react-jhipster';
import { Link } from 'react-router-dom';
import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      <section className="row justify-content-center m-2 p-2">
        <div className="card col-md-2 text-center">
          <Link to="/familia">
            <div className="card-header">Família</div>
            <div className="card-body">
              <img src="content/images/familia.png" alt="Logo" />
            </div>
          </Link>
        </div>

        <div className="card col-md-2 text-center">
          <Link to="/cesta-descricao">
            <div className="card-header">Cesta</div>
            <div className="card-body">
              <img src="content/images/cesta.png" alt="Logo" />
            </div>
          </Link>
        </div>

        <div className="card col-md-2 text-center">
          <Link to="/unidade">
            <div className="card-header">Unidade</div>
            <div className="card-body">
              <img src="content/images/unidade.png" alt="Logo" />
            </div>
          </Link>
        </div>

        <div className="card col-md-2 text-center">
          <Link to="/doacao">
            <div className="card-header">Doação</div>
            <div className="card-body">
              <img src="content/images/doacao.png" alt="Logo" />
            </div>
          </Link>
        </div>

        <div className="card col-md-2 text-center">
          <Link to="/pessoa">
            <div className="card-header">Pessoa</div>
            <div className="card-body">
              <img src="content/images/pessoas.png" alt="Logo" />
            </div>
          </Link>
        </div>
        {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
      </section>
    </>
  );
};

export default EntitiesMenu;
