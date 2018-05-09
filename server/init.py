import os
from flask import Flask
from flask import render_template
from flask import request
from werkzeug import secure_filename

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
	return render_template('index.html')


if __name__ == '__main__':
	app.run(debug=True, host='0.0.0.0')