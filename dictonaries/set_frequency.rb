require 'rubygems'
require 'sequel'
require 'unicode'

DB = Sequel.sqlite('dictonaries.db')
item = DB[:freqDB]

DB.transaction do 
	file = File.new(ARGV[0], "r:utf-8") 
	while (line = file.gets )
		 line = line.encode!('UTF-8', 'UTF-8', :invalid => :replace)
		 row = line.chomp.split(' ')
		 item.insert(:word => Unicode::upcase(row[0]), :freq => row[1])
	end
  	file.close
end # commit

puts "Table filled"

# DB[:eng_hun].join(:freqDB, :word => :answer)