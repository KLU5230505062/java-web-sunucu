import java.io.*;
import java.net.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        int port = 1989;

        try (ServerSocket sunucu = new ServerSocket(port)) {
            System.out.println("Sunucu " + port + " portunda çalışıyor...");
            System.out.println("IP Adresi: " + InetAddress.getLocalHost().getHostAddress());

            while (true) {
                Socket istemci = sunucu.accept();
                System.out.println("Yeni bağlantı: " + istemci.getInetAddress().getHostAddress());
                istemciyiIsle(istemci);
            }

        } catch (IOException e) {
            System.out.println("Sunucu başlatılırken hata oluştu: " + e.getMessage());
        }
    }

    private static void istemciyiIsle(Socket istemci) {
        try {
            BufferedReader giris = new BufferedReader(new InputStreamReader(istemci.getInputStream()));
            PrintWriter cikis = new PrintWriter(istemci.getOutputStream(), true);

            String satir;
            while ((satir = giris.readLine()) != null) {
                if (satir.isEmpty()) break;
                if (satir.startsWith("GET")) {
                    System.out.println("İstek: " + satir);
                }
            }

            String baslik = "HTTP/1.1 200 OK\r\n" +
                    "Date: " + new Date() + "\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Connection: close\r\n\r\n";

            String html = htmlIcerik();

            cikis.print(baslik);
            cikis.print(html);
            cikis.flush();

            System.out.println("Yanıt gönderildi.");

            istemci.close();
        } catch (IOException e) {
            System.out.println("İstemci hatası: " + e.getMessage());
        }
    }

    private static String htmlIcerik() {
        return "<html>" +
                "<head>" +
                "<title>Basit Web Sunucusu</title>" +
                "<style>" +
                "body { background-color:#f4f4f9; font-family:Arial; color:#333; margin:30px; }" +
                "h1 { color:#007bff; }" +
                "h2 { color:#28a745; }" +
                ".biyo { background:#fff; padding:15px; border-radius:8px; box-shadow:0 2px 6px rgba(0,0,0,0.1); }" +
                ".biyo p { font-size:1.1em; line-height:1.5; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Ayşegül Avcı - Java Web Sunucusu</h1>" +
                "<h2>Öğrenci No: 123456789</h2>" +
                "<div class='biyo'>" +
                "<p>Merhaba, ben Ayşegül Avcı. Java diliyle socket programlama kullanarak basit bir web sunucusu yaptım. " +
                "Sunucu 1989 portundan çalışıyor ve tarayıcıdan gelen istekleri karşılayarak bu sayfayı gösteriyor.</p>" +
                "<p style='color:#dc3545; font-style:italic;'>Bu sayfa HTML ve stil özellikleriyle biçimlendirilmiştir.</p>" +
                "</div>" +
                "<hr>" +
                "<p><b>Not:</b> Bu çıktı ödevin HTML bölümüdür.</p>" +
                "</body>" +
                "</html>";
    }
}
