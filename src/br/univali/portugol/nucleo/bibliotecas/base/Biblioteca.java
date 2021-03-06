package br.univali.portugol.nucleo.bibliotecas.base;

import br.univali.portugol.nucleo.Programa;
import br.univali.portugol.nucleo.asa.NoChamadaFuncao;
import br.univali.portugol.nucleo.asa.NoReferenciaVariavel;
import br.univali.portugol.nucleo.asa.TipoDado;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoConstante;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoFuncao;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.PropriedadesBiblioteca;
import br.univali.portugol.nucleo.execucao.erros.ErroExecucaoNaoTratado;
import br.univali.portugol.nucleo.mensagens.ErroExecucao;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe base para a construção de bibliotecas do Portugol. Todas as
 * bibliotecas escritas para o Portugol deverão estender esta classe e seguir as
 * seguintes regras de implementação:
 *
 * </p>
 * <ul>
 * <li><p>
 * A biblioteca deve estender a classe {@link Biblioteca}</p><br/></li>
 * <li>
 * <p>
 * A classe da biblioteca deve ser <strong>pública</strong>,
 * <strong>final</strong>
 * e não pode ser <strong>estática</strong>, <strong>anônima</strong>,
 * <strong>sintética</strong>,
 * <strong>membro</strong> ou <strong>local</strong>
 * </p>
 * <br/>
 * </li>
 * <li>
 * <p>
 * A biblioteca deve estar anotada com as anotações
 * {@link DocumentacaoBiblioteca} e {@link PropriedadesBiblioteca}
 * </p>
 * <br/>
 * </li>
 * <li><p>
 * A biblioteca deve exportar pelo menos uma função ou constante</p><br/></li>
 * <li>
 * <p>
 * Para que um método da classe seja exportado como uma função da biblioteca, o
 * método deve ser <strong>público</strong>, <strong> não estático</strong>,
 * estar anotado com a anotação {@link DocumentacaoFuncao} e jogar um
 * {@link ErroExecucao}
 * </p>
 * <br/>
 * </li>
 * <li>
 * <p>
 * A biblioteca não pode ter sobrecarga dos métodos públicos exportados como
 * funções. Já os métodos privados poderão ser sobrecarregados a qualquer
 * momento
 * </p>
 * <br/>
 * </li>
 * <li>
 * <p>
 * Para que um atributo da classe seja exportado como uma constante da
 * biblioteca, o atributo deve ser <strong>público</strong>,
 * <strong>final</strong>, <strong>estático</strong>, ter o nome todo em letras
 * maiúsculas e estar anotado com a anotação {@link DocumentacaoConstante}
 * </p>
 * <br/>
 * </li>
 * <li>
 * <p>
 * Os atributos da classe exportados como constantes da biblioteca, não
 * podederão ser vetor nem matriz, somente valores
 * </p>
 * <br/>
 * </li>
 *
 * <li>
 * <p>
 * O tipo de retorno e parâmetros dos métodos, e o tipo dos atributos
 * exportados, deverá ser compatível com os tipos de dados do Portugol, a saber:
 *
 * <br/>
 * <ul>
 * <li>{@link Integer} --&gt; {@link TipoDado#INTEIRO}<br/></li>
 * <li>{@link Double} --&gt; {@link TipoDado#REAL} <br/></li>
 * <li>{@link Boolean} --&gt; {@link TipoDado#LOGICO}<br/></li>
 * <li>{@link String} --&gt; {@link TipoDado#CADEIA}<br/></li>
 * <li>{@link Character} --&gt; {@link TipoDado#CARACTER}<br/></li>
 * <li><strong>void</strong> --&gt; {@link TipoDado#VAZIO}<br/></li>
 * </ul>
 * <br/>
 *
 * Com exceção do tipo <strong>void</strong>, não poderão ser utilizados os
 * tipos primitivos correspondentes, a saber: <strong>int</strong>,
 * <strong>double</strong>, <strong>boolean</strong> e <strong>char</strong>.
 * Além disso, o tipo <strong>void</strong> não pode ser utilizado nos
 * parâmetros dos métodos</strong>
 * </p>
 * <br/>
 * </li>
 * </ul>
 *
 * @author Luiz Fernando Noschang
 */
public abstract class Biblioteca
{
    private TipoBiblioteca tipo = getClass().getAnnotation(PropriedadesBiblioteca.class).tipo();
    private final Map<String, Method> cacheFuncoes;

    public Biblioteca()
    {
        cacheFuncoes = new TreeMap<>();
    }

    public final String getNome()
    {
        return getClass().getSimpleName();
    }

    public final TipoBiblioteca getTipo()
    {
        return tipo;
    }

    void setTipo(TipoBiblioteca tipo)
    {
        this.tipo = tipo;
    }

    public final Object getValorVariavel(NoReferenciaVariavel noReferencia) throws ErroExecucao
    {
        int linha = noReferencia.getTrechoCodigoFonteNome().getLinha();
        int coluna = noReferencia.getTrechoCodigoFonte().getColuna();

        try
        {
            Field variavel = this.getClass().getDeclaredField(noReferencia.getNome());

            return variavel.get(this);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException excecao)
        {
            throw traduzirExcecao(excecao, linha, coluna);
        }
    }

    public final Object chamarFuncao(NoChamadaFuncao noChamadaFuncao, Object... parametros) throws ErroExecucao, InterruptedException
    {
        String nome = noChamadaFuncao.getNome();
        int linha = noChamadaFuncao.getTrechoCodigoFonteNome().getLinha();
        int coluna = noChamadaFuncao.getTrechoCodigoFonteNome().getColuna();

        try
        {
            if (!cacheFuncoes.containsKey(nome))
            {
                for (Method funcao : this.getClass().getDeclaredMethods())
                {
                    if (Modifier.isPublic(funcao.getModifiers()) && funcao.getName().equals(nome))
                    {
                        cacheFuncoes.put(nome, funcao);
                    }
                }
            }

            return cacheFuncoes.get(nome).invoke(this, parametros);
        }
        catch (InvocationTargetException ex)
        {
            if (programaFoiInterrompido(ex))
            {
                throw getInterrupcao(ex);
            }
            else
            {
                throw traduzirExcecao((Exception) ex.getCause(), linha, coluna);
            }
        }
        catch (SecurityException | IllegalAccessException | IllegalArgumentException excecao)
        {
            throw traduzirExcecao(excecao, linha, coluna);
        }
    }

    private boolean programaFoiInterrompido(Throwable excecao)
    {
        while (excecao != null)
        {
            if (excecao.getCause() != null && excecao.getCause() instanceof InterruptedException)
            {
                return true;
            }

            excecao = excecao.getCause();
        }

        return false;
    }

    private InterruptedException getInterrupcao(Throwable excecao)
    {
        while (excecao != null)
        {
            if (excecao.getCause() != null && excecao.getCause() instanceof InterruptedException)
            {
                return (InterruptedException) excecao.getCause();
            }
            
            excecao = excecao.getCause();
        }

        return null;
    }

    private ErroExecucao traduzirExcecao(Exception excecao, int linha, int coluna)
    {
        if (!(excecao instanceof ErroExecucao))
        {
            excecao = new ErroExecucaoNaoTratado(excecao);
        }

        ErroExecucao erroExecucao = (ErroExecucao) excecao;

        erroExecucao.setLinha(linha);
        erroExecucao.setColuna(coluna);

        return erroExecucao;
    }

    /**
     * Este método será chamado automaticamente para inicializar as bibliotecas
     * do tipo {@link TipoBiblioteca#COMPARTILHADA}. O método será chamado
     * apenas uma vez, na primeira vez em que a biblioteca for carregada em
     * memória
     *
     * @throws ErroExecucaoBiblioteca
     *
     * @see TipoBiblioteca
     */
    protected void inicializar() throws ErroExecucaoBiblioteca
    {

    }

    /**
     * Este método será chamado automaticamente para inicializar as bibliotecas
     * do tipo {@link TipoBiblioteca#RESERVADA}. O método será chamado no início
     * da execução de cada {@link Programa}
     *
     * @param programa o programa no qual a biblioteca está registrada e
     * executando
     *
     * @param bibliotecasReservadas a lista das bibliotecas reservadas que foram
     * incluídas no programa antes da inlusão desta biblioteca. Para obter as
     * demais bibliotecas, o método {@link Biblioteca#bibliotecaRegistrada(Biblioteca)
     * }
     * deve ser sobrescrito.
     *
     * @throws ErroExecucaoBiblioteca
     *
     * @see TipoBiblioteca
     */
    protected void inicializar(Programa programa, List<Biblioteca> bibliotecasReservadas) throws ErroExecucaoBiblioteca
    {

    }

    /**
     * Este método será chamado automaticamente para finalizar a biblioteca no
     * término da execução de cada {@link Programa}
     *
     * <p>
     * Para as bibliotecas do tipo {@link TipoBiblioteca#COMPARTILHADA}, quando
     * houver um código de finalização, a biblioteca fica responsável por
     * garantir que o código de finalização seja executado apenas uma vez
     * </p>
     *
     * @throws ErroExecucaoBiblioteca
     *
     * @see GerenciadorBibliotecas
     *
     */
    protected void finalizar() throws ErroExecucaoBiblioteca
    {

    }

    /**
     * Método chamado automaticamente pelo {@link GerenciadorBibliotecas} toda
     * vez que uma nova biblioteca do tipo {@link TipoBiblioteca#RESERVADA} é
     * declarada no programa
     *
     * @param biblioteca
     *
     * @throws ErroExecucaoBiblioteca
     */
    protected void bibliotecaRegistrada(Biblioteca biblioteca) throws ErroExecucaoBiblioteca
    {

    }
}
