@echo off
for /l %%x in (1, 1, 15) do (
    set "md=%%x"
    .\untitled.bat render --infile scena2 --fname ./test_md_%%x.png --nr 2 --w 1080 --h 720 --md %%x
)
