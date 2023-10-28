@echo off
for /l %%x in (1, 1, 360) do (
    set "r=%%x"
    .\untitled.bat demo --fname ./anim/test_path_small_%%x.png --a 3 --sc 15 --nr 5 --w 720 --h 720 --r %%x
)
