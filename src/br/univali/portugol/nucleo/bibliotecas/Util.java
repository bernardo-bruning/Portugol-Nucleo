package br.univali.portugol.nucleo.bibliotecas;

import br.univali.portugol.nucleo.bibliotecas.base.Biblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.ReferenciaMatriz;
import br.univali.portugol.nucleo.bibliotecas.base.ReferenciaVetor;
import br.univali.portugol.nucleo.bibliotecas.base.TipoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.Autor;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoBiblioteca;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoFuncao;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.DocumentacaoParametro;
import br.univali.portugol.nucleo.bibliotecas.base.anotacoes.PropriedadesBiblioteca;
import java.io.File;
import java.util.Random;

@PropriedadesBiblioteca(tipo = TipoBiblioteca.COMPARTILHADA)
@DocumentacaoBiblioteca
(
    descricao = "Esta biblioteca contém diversas funções utilitárias",
    versao = "1.1"
)
public final class Util extends Biblioteca
{
    private Random random = new Random(System.currentTimeMillis());
    private long horaInicial = System.currentTimeMillis();
    
    @DocumentacaoFuncao
    (
        descricao = "Obtém o caminho utilizado pelo Sistema Operacional como diretório do usuário atual",
        retorno = "O diretório do usuário",
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }            
    )
    public String obter_diretorio_usuario() throws ErroExecucaoBiblioteca
    {
        try
        {                
            return new File(System.getProperty("user.home")).getAbsolutePath();
        }
        catch (Exception excecao)
        {
            throw new ErroExecucaoBiblioteca("Não foi possível obter o diretório do usuário");
        }
    }    
    
    @DocumentacaoFuncao
    (
        descricao = "Descobre o número de elementos existentes em um vetor",
        parametros = 
        {
            @DocumentacaoParametro(nome = "vetor", descricao = "o vetor em questão")
        },
        retorno = "O número de elementos existentes no vetor",
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }
    )
    public Integer numero_elementos(ReferenciaVetor<Object> vetor) throws ErroExecucaoBiblioteca
    {
        return vetor.numeroElementos();
    }
    
    @DocumentacaoFuncao
    (
        descricao = "Descobre o número de linhas existentes em uma matriz",
        parametros = 
        {
            @DocumentacaoParametro(nome = "matriz", descricao = "a matriz em questão")
        },
        retorno = "O número de linhas existentes na matriz",
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }
    )
    public Integer numero_linhas(ReferenciaMatriz<Object> matriz) throws ErroExecucaoBiblioteca
    {
        return matriz.numeroLinhas();
    }
    
    @DocumentacaoFuncao
    (
        descricao = "Descobre o número de colunas existentes em uma matriz",
        parametros = 
        {
            @DocumentacaoParametro(nome = "matriz", descricao = "a matriz em questão")
        },
        retorno = "O número de colunas existentes na matriz",
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }
    )
    public Integer numero_colunas(ReferenciaMatriz<Object> matriz) throws ErroExecucaoBiblioteca
    {
        return matriz.numeroColunas();
    }
    
    @DocumentacaoFuncao
    (
        descricao = "Sorteia um número aleatório entre os valores mínimo e máximo especificados",
        parametros = 
        {
            @DocumentacaoParametro(nome = "minimo", descricao = "o menor número que pode ser sorteado"),
            @DocumentacaoParametro(nome = "maximo", descricao = "o maior número que pode ser sorteado")
        },
        retorno = "O número sorteado",
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }        
    )
    public Integer sorteia(Integer minimo, Integer maximo) throws ErroExecucaoBiblioteca
    {
        if (minimo > maximo)
        {
            throw new ErroExecucaoBiblioteca(String.format("O valor mínimo (%d) é maior do que o valor máximo (%d)", minimo, maximo));
        }
        else if (minimo == maximo)
        {
            throw new ErroExecucaoBiblioteca(String.format("Os valores mínimo e máximo são iguais: %d", minimo));
        }
        
        return minimo + random.nextInt(maximo + 1 - minimo);
    }

    @DocumentacaoFuncao
    (
        descricao = "Pausa a execução do programa durante o intervalo de tempo especificado",
        parametros = 
        {
            @DocumentacaoParametro(nome = "intervalo", descricao = "o intervalo de tempo (em milissegundos) durante o qual o programa ficará pausado")
        },
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }        
    )    
    public void aguarde(Integer intervalo) throws ErroExecucaoBiblioteca, InterruptedException
    {
        Thread.sleep(intervalo);
    }
    
    @DocumentacaoFuncao
    (
        descricao = "Obtém o tempo decorrido (em milissegundos) desde que a biblioteca foi utilizada pela primeira vez",
        autores = 
        {
            @Autor(nome = "Luiz Fernando Noschang", email = "noschang@univali.br")
        }        
    )      
    public Integer tempo_decorrido() throws ErroExecucaoBiblioteca
    {
        return (int) (System.currentTimeMillis() - horaInicial);
    }
}
