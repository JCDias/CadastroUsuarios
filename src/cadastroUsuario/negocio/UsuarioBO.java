/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroUsuario.negocio;

import cadastroUsuario.Excecoes.CamposVaziosException;
import cadastroUsuario.Excecoes.CpfInvalidoException;
import cadastroUsuario.Excecoes.CpfJaCadastradoException;
import cadastroUsuario.Excecoes.DataInvalidaException;
import cadastroUsuario.Excecoes.EmailInvalidoException;
import cadastroUsuario.Excecoes.NomeInvalidoException;
import cadastroUsuario.dados.UsuarioDAO;
import cadastroUsuario.entidades.Usuario;
import cadastroUsuario.utilitarios.Utils;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SEC04
 */
public class UsuarioBO {

    // Validar campos
    public void ValidaDados(String nome, String cpf, String data, String rg, String rua, String numero, String bairro, String cidade, String celular, String email) {

        //Validar campos preenchidos
        if (nome.equals("") || cpf.equals(".   .   -") || data.equals("/  /") || rg.equals("") || numero.equals("") || bairro.equals("") || celular.equals("(  )     -")) {
            throw new CamposVaziosException();
        } else {
            //Validar tamanho do nome
            if (nome.length() < 3) {
                throw new NomeInvalidoException();
            }

            //Validar CPF
            if (!isCPF(cpf)) {
                throw new CpfInvalidoException();
            }

            //Pegar no pc da unimontes o valida idade e substituir aki
            //Validar Data de nascimento
            if (ValidadData(data) == false) {
                throw new DataInvalidaException();
            } else {
                Utils util = new Utils();
                data = data.replace("/", "-");
                int idade = util.calculaIdade(data, "dd-MM-yyyy");
                System.out.println(idade);
                if (idade < 10 || idade > 99) {
                    throw new DataInvalidaException();
                }
            }

            //Valida email
            if (!email.equals("")) {
                if (validaEmail(email) == false) {
                    throw new EmailInvalidoException();
                }
            }
        }
        
    }
    // Fim validar campos

    //Verifica se o cpf informado já esta cadastrado e salvar
    public void verificaCPF(Usuario usuario) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        Usuario user = usuarioDAO.selecionarCpfIgual(usuario.getCpf());
        
        if (user == null) {
            usuarioDAO.cadastrarUsuario(usuario);
        } else {
            throw new CpfJaCadastradoException();
        }
    }

    //Validar CPF
    public static boolean isCPF(String CPF) {
// considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }
        
        char dig10, dig11;
        int sm, i, r, num, peso;

// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
// Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
// converte o i-esimo caractere do CPF em um numero:
// por exemplo, transforma o caractere '0' no inteiro 0         
// (48 eh a posicao de '0' na tabela ASCII)         
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
// Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

// Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }
    
    public static String imprimeCPF(String CPF) {
        return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "."
                + CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
    }

    //Corrigir valida data pesquisar validar data de nascimento
    //Validar data
    public boolean ValidadData(String data) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        try {
            df.parse(data);
            return true;
            
        } catch (ParseException ex) {
            return false;
        }
    }

    //Valida email
    public boolean validaEmail(String email) {

        // Validar E-mail
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(email);
        if (m.find()) {
            return true;
        }
        return false;
    }
}

//Leia mais em: Validando o CPF em uma Aplicação Java http://www.devmedia.com.br/validando-o-cpf-em-uma-aplicacao-java/22097#ixzz3W4aPqXlo

