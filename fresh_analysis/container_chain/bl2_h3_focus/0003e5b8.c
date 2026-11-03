// File offset entry: 0x3e5b8

void FUN_ffff0000b803e5b8(void)

{
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  FUN_ffff0000b803da68(DAT_ffff0000b8128850);
  return;
}


