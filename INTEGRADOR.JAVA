import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {
    private JPanel panelInicio;
    private JButton ingresarBotonInicio;

    private JPanel panelLogin;
    private JTextField textUsuario;
    private JPasswordField textPass;
    private JButton ingresarBoton;
    private JButton crearUsuarioBoton;

    private JPanel panelRegistro;
    private JTextField textNombreCompleto;
    private JTextField textCorreo;
    private JPasswordField textPassword;
    private JPasswordField textConfirmPassword;
    private JButton crearCuentaBoton;

    Connection conexion;
    Statement st;
    ResultSet rs;

    public Login() {
        // Configuración de la ventana
        setTitle("GameDoc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new CardLayout());

        // Inicializar paneles
        initPanelInicio();
        initPanelLogin();
        initPanelRegistro();

        // Agregar paneles a la ventana
        add(panelInicio, "inicio");
        add(panelLogin, "login");
        add(panelRegistro, "registro");

        // Mostrar la pantalla inicial
        mostrarPanelInicio();
        setVisible(true);
    }

    private void initPanelInicio() {
        panelInicio = new JPanel();
        panelInicio.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel("GAMEDOC", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));

        ingresarBotonInicio = new JButton("Ingresar");
        ingresarBotonInicio.setFont(new Font("Arial", Font.PLAIN, 16));

        panelInicio.add(logoLabel, BorderLayout.CENTER);
        panelInicio.add(ingresarBotonInicio, BorderLayout.SOUTH);

        // Acción para cambiar a la pantalla de login al hacer clic en "Ingresar"
        ingresarBotonInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelLogin();
            }
        });
    }

    private void initPanelLogin() {
        panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(3, 2, 10, 10));

        textUsuario = new JTextField();
        textPass = new JPasswordField();
        ingresarBoton = new JButton("Iniciar sesión");
        crearUsuarioBoton = new JButton("Crear usuario");

        panelLogin.add(new JLabel("Nombre:"));
        panelLogin.add(textUsuario);
        panelLogin.add(new JLabel("Contraseña:"));
        panelLogin.add(textPass);
        panelLogin.add(ingresarBoton);
        panelLogin.add(crearUsuarioBoton);

        // Acción para iniciar sesión
        ingresarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarUsuario();
            }
        });

        // Acción para mostrar el panel de registro
        crearUsuarioBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanelRegistro();
            }
        });
    }

    private void initPanelRegistro() {
        panelRegistro = new JPanel();
        panelRegistro.setLayout(new GridLayout(5, 2, 10, 10));

        textNombreCompleto = new JTextField();
        textCorreo = new JTextField();
        textPassword = new JPasswordField();
        textConfirmPassword = new JPasswordField();
        crearCuentaBoton = new JButton("Crear cuenta");

        panelRegistro.add(new JLabel("Nombre completo:"));
        panelRegistro.add(textNombreCompleto);
        panelRegistro.add(new JLabel("Correo:"));
        panelRegistro.add(textCorreo);
        panelRegistro.add(new JLabel("Password:"));
        panelRegistro.add(textPassword);
        panelRegistro.add(new JLabel("Confirm Password:"));
        panelRegistro.add(textConfirmPassword);
        panelRegistro.add(crearCuentaBoton);

        // Acción para crear cuenta
        crearCuentaBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
            }
        });
    }

    private void conectar() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyectoIntegrador3272024II", "root", "1234");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }
    }

    private void validarUsuario() {
        conectar();
        String usuario = textUsuario.getText();
        String pass = new String(textPass.getPassword());
        try {
            st = conexion.createStatement();
            rs = st.executeQuery("SELECT * FROM usuarios WHERE usuario ='" + usuario + "' AND pass = '" + pass + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Las credenciales del usuario son correctas");
                // Aquí puedes abrir la ventana principal del programa
            } else {
                JOptionPane.showMessageDialog(this, "Las credenciales no son correctas");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void crearUsuario() {
        conectar();
        String nombreCompleto = textNombreCompleto.getText();
        String correo = textCorreo.getText();
        String password = new String(textPassword.getPassword());
        String confirmPassword = new String(textConfirmPassword.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
            return;
        }

        try {
            st = conexion.createStatement();
            st.executeUpdate("INSERT INTO usuarios (nombre, correo, pass) VALUES ('" + nombreCompleto + "', '" + correo + "', '" + password + "')");
            JOptionPane.showMessageDialog(this, "Usuario creado con éxito");
            mostrarPanelLogin();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al crear usuario: " + e.getMessage());
        }
    }

    private void mostrarPanelInicio() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "inicio");
    }

    private void mostrarPanelLogin() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "login");
    }

    private void mostrarPanelRegistro() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "registro");
    }

    public static void main(String[] args) {
        new Login();
    }
}