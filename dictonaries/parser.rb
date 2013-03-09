require 'rubygems'
require 'sequel'

DB = Sequel.sqlite('dictonaries.db')
item = DB[:hun_eng]

unless ARGV.count > 0
  abort("Adjon meg egy bemeneti fájlt!")
end

counter = 0
key = ""

# Egy tranzakciokent szurja be, igy sokkal gyorsabb
DB.transaction do 
  read_key = true #Eloszor kulcsot olvas
  file = File.new(ARGV[0], "r:utf-8") 
  while (line = file.gets )
      line = line.encode!('UTF-8', 'UTF-8', :invalid => :replace)
      if line == "\n" # Ures sor utan mindig kulcs jon
        read_key = true
      else
        if read_key
          key = line
          read_key = false
        elsif !(/('|-|!|\*|&|\\|,|\~|\.)/ =~ key.chomp) && !(/\s/ =~ key.chomp) && key.length > 2 && key.length < 21
          # 2 es 19 koze essen a hossz, length -1-el kell szamolni
          if !(/(~|<|>|\?)/ =~ line.chomp)
            # A valasz nem tartalmaz spec karaktereket
            item.insert(:answer => key.chomp, :length => key.length-1, :clue => line.chomp)
            counter = counter + 1
            # puts "item.insert(:answer => #{key.chomp}, :length => #{key.length-1}, :clue => #{line.chomp})"
            # puts "Beszúrt sor: #{counter} db"
          end
        end
      end
  end
  file.close
end #Commit

puts "Kigyüjtött szópárok száma: #{counter} db"