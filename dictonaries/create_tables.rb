require 'rubygems'
require 'sequel'

DB = Sequel.connect(:adapter => :sqlite, :database => 'dictonaries.db')

DB.create_table? :eng_hun do
  primary_key :id
  String :answer
  Integer :length
  String :clue
  index :id
  index :length
end

DB.create_table? :hun_eng do
  primary_key :id
  String :answer
  Integer :length
  String :clue
  index :id
  index :length
end