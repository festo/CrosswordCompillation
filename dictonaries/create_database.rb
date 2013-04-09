require 'rubygems'
require 'sequel'
require 'unicode'

puts "Memory DB létrehozása ..."

memoryDB = Sequel.sqlite

memoryDB.create_table? :words do
  primary_key :id
  String :answer
  Integer :length
  String :clue
  Bigint :frequency

  index :id
  index :length
end

memoryDB.create_table :freqDB do
  primary_key :id
  String :word
  Bigint :freq
end

item = memoryDB[:words]
counter = 0
key = ""

file_lines = %x{wc -l #{ARGV[1]}}.split.first.to_i
line_count = 0

puts "Szavak beolvasása ..."
memoryDB.transaction do 
  read_key = true #Eloszor kulcsot olvas
  file = File.new(ARGV[1], "r:utf-8") 
  while (line = file.gets )
  	  line_count = line_count + 1
      line = line.encode!('UTF-8', 'UTF-8', :invalid => :replace)
      if line == "\n" # Ures sor utan mindig kulcs jon
        read_key = true
      else
        if read_key
          key = line
          read_key = false
        elsif !(/('|-|!|\*|&|\\|,|„|”|™|\~|\.)/ =~ key.chomp) && !(/\s/ =~ key.chomp) && key.length > 2 && key.length < 21
          # 2 es 19 koze essen a hossz, length -1-el kell szamolni
          if !(/(~|<|>|\?)/ =~ line.chomp)
            # A valasz nem tartalmaz spec karaktereket
            item.insert(:answer => Unicode::upcase(key.chomp), :length => key.length-1, :clue => line.chomp)
            counter = counter + 1
            # puts "item.insert(:answer => #{Unicode::upcase(key.chomp)}, :length => #{key.length-1}, :clue => #{line.chomp})"
            # puts "Beszúrt sor: #{counter} db"
          end
        end
      end
      if counter % 100
      	print "\r#{(line_count % file_lines) / 10000}%"
      end
  end
  file.close
end #Commit
print "\r100%\n"
puts "Kigyüjtött szópárok száma: #{counter} db!"

puts "Gyakoriságok beolvasása ..."

item = memoryDB[:freqDB]
counter = 0

file_lines = %x{wc -l #{ARGV[2]}}.split.first.to_i
line_count = 0
memoryDB.transaction do 
	file = File.new(ARGV[2], "r:utf-8") 
	while (line = file.gets )
		line_count = line_count + 1
		line = line.encode!('UTF-8', 'UTF-8', :invalid => :replace)
		row = line.chomp.split(' ')
		item.insert(:word => Unicode::upcase(row[0]), :freq => row[1])
		counter = counter + 1

		if counter % 100
      		print "\r#{(line_count % file_lines) / 10000}%"
      	end
	end
  	file.close
end # commit

print "\r100%\n"
puts "Kigyüjtött gyakoriságok száma: #{counter} db!"

puts "Eredmények összefésülése ..."
freqDB = Sequel.connect(:adapter => :sqlite, :database => ARGV[0]+'.db')
freqDB.create_table? :words do
  primary_key :id
  String :answer
  Integer :length
  String :clue
  Bigint :frequency

  index :id
  index :length
end

freqDB[:words].delete

item = freqDB[:words]

rows = memoryDB[:words].join(:freqDB, :word => :answer)
count = rows.count

freqDB.transaction do 
	rows.each{
		|r|
		item.insert(:answer => r[:answer], :length => r[:length], :clue => r[:clue], :frequency => r[:freq])
		counter = counter + 1
		if counter % 100
	      	print "\r#{(counter % count) / 10000}%"
	    end
	}

end # commit

print "\r100%\n"
puts "Kész!"