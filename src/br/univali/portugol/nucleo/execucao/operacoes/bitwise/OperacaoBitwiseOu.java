package br.univali.portugol.nucleo.execucao.operacoes.bitwise;

import br.univali.portugol.nucleo.execucao.operacoes.Operacao;

public class OperacaoBitwiseOu extends Operacao
{

    public Integer executar(Integer a, Integer b)
    {
        return a | b;
    }
        
}
