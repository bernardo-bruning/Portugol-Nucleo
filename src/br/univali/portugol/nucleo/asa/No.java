package br.univali.portugol.nucleo.asa;

import br.univali.portugol.nucleo.execucao.Depurador;
import br.univali.portugol.nucleo.execucao.PontoParada;

/**
 * Esta é a classe base para todos os nós da ASA.
 *
 * @author Luiz Fernando Noschang
 * @version 1.0
 *
 * @see ArvoreSintaticaAbstrata
 */
public abstract class No
{
    private NoBloco pai = null;
    private PontoParada pontoParada;

    /**
     * Este método serve para dar suporte ao caminhamento da ASA utilizando o
     * padrão visitor. As classes filhas, ao implementar este método deverão
     * chamar o método <code>visitar</code> do visitante e retornar o objeto
     * resultante da chamada. Exemplo:
     * <br>
     * <pre><code>
     *      public SubClasse extends No
     *      {
     *          &#64;Override
     *          public Object aceitar(VisitanteASA visitante) throws ExcecaoVisitaASA
     *          {
     *              return visitante.visitar(this);
     *          }
     *      }
     * </code></pre>
     *
     * @param visitante o visitante que irá percorrer a ASA.
     * @return um objeto qualquer retornado pelo visitante durante o
     * caminhamento na ASA.
     * @throws ExcecaoVisitaASA
     * @since 1.0
     */
    public abstract Object aceitar(VisitanteASA visitante) throws ExcecaoVisitaASA;

    public No()
    {
        this(null);
    }

    protected No(NoBloco pai)
    {
        pontoParada = new PontoParada(this);//todos os nós têm pontos de parada desativados por padrão
        setPai(pai);
    }

    public void setPai(NoBloco pai)
    {
        this.pai = pai;
    }

    /**
     *
     * @param estado Estado de execução do depurador.
     * @return responde se o no pode ser parado em função do estado do
     * depurador. Por exemplo, alguns nós só para quando o depurador está no
     * estado BREAK_POINT.
     */
    public boolean ehParavel(Depurador.Estado estado)
    {
        return pontoParada.estaAtivo() && estado == Depurador.Estado.BREAK_POINT;
    }

    protected boolean temPai()
    {
        return pai != null;
    }

    protected NoBloco getPai()
    {
        if (!temPai())
        {
            throw new IllegalStateException("O objeto não tem um pai!");
        }
        return pai;
    }

    public void definirPontoParada(boolean ativado)
    {
        pontoParada.setAtivo(ativado);
    }

    public boolean pontoDeParadaEstaAtivo()
    {
        return pontoParada.estaAtivo();
    }
}
