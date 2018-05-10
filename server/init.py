import os
from flask import Flask
from flask import render_template
from flask import request, Response
from werkzeug import secure_filename

from neural_style import train

app = Flask(__name__)
UPLOAD_FOLDER = './img'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
ALLOWED_EXTENSIONS = set(['png','jpg','JPG','PNG','jpeg','JPEG'])


@app.route('/', methods=['GET'])
def hello():
	return render_template('index.html')

@app.route('/', methods=['POST'])
def hello_post():
	f = request.files['file']
	if f and allowed_file(f.filename):
		fname = secure_filename(f.filename)
		print("Upload Status: " + fname)
		f.save(os.path.join(app.config['UPLOAD_FOLDER'], fname))
	return render_template('index.html')

def allowed_file(filename):
	return '.' in filename and \
		filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/upload', methods=['GET','POST'])
def index():
	if request.method == 'POST':
		flist = request.files.getlist("file")
		for f in flist:
		# f = request.files['file']
			if f and allowed_file(f.filename):
				fname = secure_filename(f.filename)
				print("Upload Status: " + fname)
				f.save(os.path.join(app.config['UPLOAD_FOLDER'], fname))

	# train the img ang get three style result
	train("/root/server/img/cropped.jpg", "/root/server/img/style/style1.jpg", 200, "/root/server/img/s1.jpg")
	train("/root/server/img/cropped.jpg", "/root/server/img/style/style2.jpg", 200, "/root/server/img/s2.jpg")
	train("/root/server/img/cropped.jpg", "/root/server/img/style/style3.jpg", 200, "/root/server/img/s3.jpg")

	return render_template('index.html')

@app.route('/img/<imgid>', methods=['GET'])
def returnImg(imgid):
	image = file("img/{}.jpg".format(imgid))
	resp = Response(image, mimetype="image/jpeg")
	return resp

if __name__ == '__main__':
	app.run(debug=True, host='0.0.0.0')
