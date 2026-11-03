// File offset entry: 0x3e564

undefined4 FUN_ffff0000b803e564(void)

{
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  return *(undefined4 *)(DAT_ffff0000b8128850 + 0x1c);
}


