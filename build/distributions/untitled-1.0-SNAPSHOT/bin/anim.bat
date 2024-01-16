@echo off
for /l %%x in (1, 1, 360) do (
    set "r=%%x"
    .\untitled.bat render -inf scena2 --fname ./anim/test_path_small_%%x.png  --nr 2 --md 2 --w 720 --h 720 --r %%x
)
