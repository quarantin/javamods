function EveryOneMinute()
	print("Testing custom save file")
	local writer = getSaveFileWriter("my-custom-file.txt", true)
	writer:println("Hello World!!!")
	writer:close()
	print("Testing custom save file done")
end

Events.EveryOneMinute.Add(EveryOneMinute)
