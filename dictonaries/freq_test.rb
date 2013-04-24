require 'rubygems'
require 'sequel'
require 'unicode'

freqDB = Sequel.connect(:adapter => :sqlite, :database => 'enghun.db')

fmax = freqDB[:words].max(:frequency)
rows = freqDB[:words]
max = 0

n = (freqDB[:words].min(:frequency).to_f / fmax.to_f)

dividing_point = Array.new
dividing_point.push(0)
dividing_point.push((1-n).to_f / 10.to_f)
for i in 1..8
	dividing_point.push( dividing_point.last + ((1-n).to_f / 10.to_f))
end

freqDB.transaction do 
	rows.each{
		|r|

		freq = (r[:frequency].to_f / fmax.to_f)
		
		$i = 0
		while( freq >= dividing_point[$i].to_f  && $i < 10)
			$i += 1
		end

		if $i > max
			max = $i
			puts "MAX: #{11-max}"
		end
	}

end # commit