particlesJS('particles-js', {
    particles: {
      number: { value: 200, density: { enable: true, value_area: 1200 } }, 
      color: { value: '#ffffff' },
      shape: { type: 'circle' },
      opacity: { value: 0.8 },
      size: { value: 2, random: true },
      move: {
        enable: true,
        speed: 1,
        direction: 'none',
        random: false,
        straight: false,
        out_mode: 'out'
      }
    },
    interactivity: {
      detect_on: 'window',
      events: { onmousemove: { enable: true } },
      modes: {
        trail: {
          delay: 0.005,
          quantity: 15,
          particles: { color: '#ffffff', opacity: 0.9, size: 2.5 }
        }
      }
    },
    retina_detect: true
  });
  
  document.addEventListener("DOMContentLoaded", function () {
    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");

    if (togglePassword && passwordInput) {
        const icon = togglePassword.querySelector("i");

        togglePassword.addEventListener("click", function () {
            const isPassword = passwordInput.type === "password";
            passwordInput.type = isPassword ? "text" : "password";

            // Alternar icono
            icon.classList.toggle("fa-eye");
            icon.classList.toggle("fa-eye-slash");
        });
    }
});
