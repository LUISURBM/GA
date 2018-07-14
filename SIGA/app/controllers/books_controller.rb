class BooksController < ApplicationController

	def index
		@users = User.all
	end
	
end
