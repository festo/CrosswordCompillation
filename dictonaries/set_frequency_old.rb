require 'rubygems'
require 'sequel'
require 'unicode'

MemDB = Sequel.sqlite

MemDB.create_table :freqDB do
  primary_key :id
  String :word
  Bigint :freq
end

item = MemDB[:freqDB]


MemDB.transaction do 
	file = File.new(ARGV[0], "r:utf-8") 
	while (line = file.gets )
		 line = line.encode!('UTF-8', 'UTF-8', :invalid => :replace)
		 row = line.chomp.split(' ')
		 item.insert(:word => Unicode::upcase(row[0]), :freq => row[1])
	end
  	file.close
end # commit

puts "Table created"

DB = Sequel.sqlite('dictonaries.db')
DB.transaction do
	MemDB.transaction do 
		MemDB[:freqDB].each do |row|
			DB[:eng_hun].where(:answer => row[:word]).update(:frequency => row[:freq])
			puts row
		end	
	end
	DB[:eng_hun].where(:frequency < 1).delete
end
