package br.univali.portugol.nucleo.asa;

import br.univali.portugol.nucleo.execucao.Depurador;
import java.util.List;

/**
 * Representa uma chamada de função no código fonte.
 * <p>
 * Exemplo:  <code><pre>
 *
 *      funcao exemploDeChamadaDeFuncao()
 *      {
 *           inteiro fat = fatorial(5) // Usto é uma chamada à função 'fatorial' passando 1 parâmetro!
 *
 *           escreva("Isto é uma chamada à função 'escreva' passando ", 3, " parâmetros!")
 *      }
 *
 * </pre></code>
 *
 * @author Luiz Fernando Noschang
 * @version 1.0
 *
 */
public final class NoChamadaFuncao extends NoReferencia
{
    private List<NoExpressao> parametros;

    /**
     * @param escopo o escopo da função sendo referenciada. Se o escopo for
     * nulo, então é uma função do programa, senão é uma função de biblioteca.
     *
     * @param nome o nome da função que está sendo chamadada.
     *
     * @since 1.0
     */
    public NoChamadaFuncao(String escopo, String nome)
    {
        super(escopo, nome);
    }

    /**
     * Obtém a lista dos parâmetros que estão sendo passados para a função. Os
     * prâmetros podem ser qualquer tipo de expressão, inclusive outras chamadas
     * de função. A lista poderá estar vazia caso a função não necessite de
     * parâmetros.
     *
     * @return a lista de parâmetros passados para a função
     *
     * @since 1.0
     */
    public List<NoExpressao> getParametros()
    {
        return parametros;
    }

    /**
     * Define a lista de parâmetros que estão sendo passados para a função.
     *
     * @param parametros lista de parâmetros que estão sendo passados para a
     * função.
     *
     * @since 1.0
     */
    public void setParametros(List<NoExpressao> parametros)
    {
        this.parametros = parametros;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected TrechoCodigoFonte montarTrechoCodigoFonte()
    {
        int tamanhoTexto = 0;

        int linha = getTrechoCodigoFonteNome().getLinha();
        int coluna = getTrechoCodigoFonteNome().getColuna();

        tamanhoTexto = tamanhoTexto + getTrechoCodigoFonteNome().getTamanhoTexto() + 2;

        if (parametros != null)
        {
            for (NoExpressao parametro : parametros)
            {
                tamanhoTexto = tamanhoTexto + parametro.getTrechoCodigoFonte().getTamanhoTexto();
            }
        }

        return new TrechoCodigoFonte(linha, coluna, tamanhoTexto);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Object aceitar(VisitanteASA visitante) throws ExcecaoVisitaASA
    {
        return visitante.visitar(this);
    }

    @Override
    public boolean ehParavel(Depurador.Estado estado)
    {
        return (estado == Depurador.Estado.BREAK_POINT && pontoDeParadaEstaAtivo()) || estado == Depurador.Estado.STEP_OVER;
    }

}
