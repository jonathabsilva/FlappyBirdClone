package com.android.newtecnology;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.omg.PortableInterceptor.Interceptor;

import java.util.Random;

import javax.xml.soap.Text;

public class FlappyBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaro;
	private Texture fundo, canoTopo, canoBaixo;
	private int Px=300, Py=0, tamanho,vAsa, queda=0, xCano, yCano, vCano=20;
	// vAsa = Velocidade da batida da asa
	// VCano = Velocidade com quem o cano se move

	// Usado para alterar a altura dos canos
	private int alturaRandomica;
	// Gera o numero randômico
	private Random numRandomico;

	// Durante a movimentação dos canos, o método de aumentar a pontuação é chamado várias vezes
	// Adicionou-se essa chave para saber se o ponto já foi adicionado, assim, não adicionar multiplas vezes
	private boolean habPonto = true;

	// Marca  início do jogo
	private boolean jogar = false;

	// Pontuação do jogador
	private int pontuacao=0;

	// Utilizado para adicioanr os textos à tela do cel
	private BitmapFont fonte, fonte2;

	// A variável i é utilizada para fazer o pássaro bater asas
	private float i;

	// Variáveis para pegar os tamanhos da tela do celular
	private int telaWidth, telaHeight;

	// Cria os retangulos e circulos imaginários
	private Rectangle retanguloCanoBaixo, retanguloCanoCima;
	private Circle passaroCirculo;

	// Função utilizada para desenhar na tela
	private ShapeRenderer shape;

	// Função só é executada uma vez
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		passaro = new Texture[3];
		passaro[0] = new Texture("passaro1.png");
		passaro[1] = new Texture("passaro2.png");
		passaro[2] = new Texture("passaro3.png");
		canoTopo = new Texture("cano_topo.png");
		canoBaixo = new Texture("cano_baixo.png");
		fundo = new Texture("fundo.png");

		// Cria o circulo e retangulos imaginários para delimitar os objetos da tela
		passaroCirculo = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoCima = new Rectangle();

		telaHeight = Gdx.graphics.getHeight();
		telaWidth =  Gdx.graphics.getWidth();

		// Configurando o texto a ser exibido na tela
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE); // Define a cor da fonte
		fonte.getData().setScale(6); // Define o tamanho da fonte

		//Configurando o texto de notificações
		fonte2 = new BitmapFont();
		fonte2.setColor(Color.RED);
		fonte2.getData().setScale(4);



		numRandomico = new Random();
		//alturaRandomica = numRandomico.nextInt(20);
		tamanho=100;
		Py = 100;
		Py = telaHeight/2;
		vAsa = 10;
		xCano = Gdx.graphics.getWidth();
		yCano = Gdx.graphics.getHeight()- canoTopo.getHeight();
	}

	// Função fica em loop
	@Override
	public void render () {

		batch.begin();

		if(jogar) {
			if (Py != 0) {

				i += Gdx.graphics.getDeltaTime() * vAsa;
				xCano -= vCano;
				if (xCano < (-1 * (canoBaixo.getWidth()))) {
					xCano = Gdx.graphics.getWidth();
					habPonto = !habPonto;
					// alturaRandomica = numRandomico.nextInt(20);
				}

				if (xCano < Px && habPonto) {
					pontuacao++;
					vCano = 20 + (int) pontuacao / 10;

					habPonto = !habPonto;
				}
			}

			if (Gdx.input.justTouched()) {
				queda = -20;
				jogar = true;
			}


			if (Py > 0 || (Py == 0 && queda < 0)) {
				Py -= queda;
				queda++;
			} else {
				Py = 0;
			}

		}else
			if(Gdx.input.justTouched())
				jogar= true;

		if(i>2){
			i=0;
		}

		// Desenha o circulo e retangulo imaginário
		passaroCirculo.set(Px + passaro[0].getWidth()/2,Py + passaro[0].getHeight()/2,tamanho);
		retanguloCanoCima.set(xCano,yCano,canoTopo.getWidth(),canoTopo.getHeight());
		retanguloCanoBaixo.set(xCano,0,canoBaixo.getWidth(),canoBaixo.getHeight());


		batch.draw(fundo,0,0,telaWidth,telaHeight);
		batch.draw(passaro[(int) i],Px,Py,tamanho,tamanho);
		batch.draw(canoTopo,xCano,yCano);
		batch.draw(canoBaixo,xCano,alturaRandomica);

		// fonte.draw(cenario onde quer se escrever, texto a ser inscrito, posX, posY);
		fonte.draw(batch,String.valueOf(pontuacao),telaWidth/2,100); // Escreve na tela um texto específico
		fonte2.draw(batch,("Cano Baixo: "+String.valueOf(alturaRandomica)),0,telaHeight-100);
		fonte2.draw(batch,("Cano Topo: "+String.valueOf(canoTopo.getHeight())),0,telaHeight-200);
		batch.end();

		// Desenhar formas na tela
		/*
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.circle(passaroCirculo.x,passaroCirculo.y,passaroCirculo.radius);
		shape.rect(retanguloCanoCima.x,retanguloCanoCima.y,retanguloCanoCima.width,retanguloCanoCima.height);
		shape.rect(retanguloCanoBaixo.x,retanguloCanoBaixo.y,retanguloCanoBaixo.width,retanguloCanoBaixo.height);
		shape.end();
		*/

		// Teste de colisão
		if(Intersector.overlaps(passaroCirculo,retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo,retanguloCanoCima))
			Gdx.app.log("Colisão","Houve colisão!");
	}
	

}
