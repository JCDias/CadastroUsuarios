/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroUsuario.dados;

import cadastroUsuario.entidades.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class UsuarioDAO {

    //Verificar se o cpf digitado já esta cadastrado
    private static final String SQL_SELECT_CPF_IGUAL = "SELECT  NOME, CPF FROM USUARIOS WHERE CPF = ?";

    public Usuario selecionarCpfIgual(String cpf) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        Usuario user = null;

        try {

            conexao = BancoDadosUtil.getConnection();

            comando = conexao.prepareStatement(SQL_SELECT_CPF_IGUAL);
            comando.setString(1, cpf);
            resultado = comando.executeQuery();

            if (resultado.next()) {
                user = new Usuario();
                user.setNome(resultado.getString("NOME"));
                user.setCpf(resultado.getString("CPF"));
            }

            conexao.commit();

        } catch (Exception e) {
            if (conexao != null) {
                conexao.rollback();
            }
        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
        return user;
    }

    //Cadastrar Usuario
    private static final String SQL_INSERT_USUARIO = "INSERT INTO USUARIOS( NOME, CPF, RG, DATA_NASC, SEXO, RUA, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, TELEFONE, CELULAR, EMAIL, MALHA, TEMPO, CATEGORIA, TIPO, DIA, HORARIO, DATA_CADASTRO )\n"
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public void cadastrarUsuario(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {

            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_INSERT_USUARIO);

            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getCpf());
            comando.setString(3, usuario.getRg());
            comando.setString(4, usuario.getDataNasc());
            comando.setString(5, usuario.getSexo());
            comando.setString(6, usuario.getRua());
            comando.setString(7, usuario.getNumero());
            comando.setString(8, usuario.getComplemento());
            comando.setString(9, usuario.getBairro());
            comando.setString(10, usuario.getCidade());
            comando.setString(11, usuario.getTelefone());
            comando.setString(12, usuario.getCelular());
            comando.setString(13, usuario.getEmail());
            comando.setString(14, usuario.getMalha());
            comando.setString(15, usuario.getTempo());
            comando.setString(16, usuario.getCategoria());
            comando.setString(17, usuario.getTipo());
            comando.setString(18, usuario.getDia());
            comando.setString(19, usuario.getHorario());
            comando.setString(20, usuario.getDataCadastro());

            comando.execute();
            conexao.commit();

        } catch (Exception e) {
            if (conexao != null) {
                conexao.rollback();
            }

        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
    }
    
    //Selecionar Usuarios para gerar relatório
    private static final String SQL_SELECT_RELATORIO_USUARIOS = "SELECT NOME, CPF, MALHA, TEMPO, CATEGORIA, TIPO FROM USUARIOS";
    public ArrayList<Usuario> SelecionarAtividade() throws SQLException {
        ArrayList<Usuario> listaTodosUsuarios = new ArrayList<>();
        Usuario usuarioCadastrado = null;

        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {

            conexao = BancoDadosUtil.getConnection();

            comando = conexao.prepareStatement(SQL_SELECT_RELATORIO_USUARIOS);
            

            resultado = comando.executeQuery();
            listaTodosUsuarios.removeAll(listaTodosUsuarios);

            //percorrendo os registros encontrados  
            if (resultado.next()) {
                listaTodosUsuarios = new ArrayList<Usuario>();
                do {
                    //instanciando objeto   
                    usuarioCadastrado = new Usuario();


                    /*setando atributos de acordo com os seus tipos primitivos*/
                    usuarioCadastrado.setNome(resultado.getString("NOME"));
                    usuarioCadastrado.setCpf(resultado.getString("CPF"));
                    usuarioCadastrado.setMalha(resultado.getString("MALHA"));
                    System.out.println("Tempo: " +resultado.getString("TEMPO"));
                    if(resultado.getString("TEMPO").equals("")){
                        usuarioCadastrado.setTempo("-");
                    }else{
                        usuarioCadastrado.setTempo(resultado.getString("TEMPO"));
                    }
                    
                    usuarioCadastrado.setCategoria(resultado.getString("CATEGORIA"));
                    usuarioCadastrado.setTipo(resultado.getString("TIPO"));
                    
                    //add a lista de objetos encontrados e setados  
                    listaTodosUsuarios.add(usuarioCadastrado);
                } while (resultado.next());
            }

            conexao.commit();

        } catch (Exception e) {
            if (conexao != null) {
                conexao.rollback();
            }
            throw new RuntimeException(e);

        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
        return listaTodosUsuarios;
    }
}
