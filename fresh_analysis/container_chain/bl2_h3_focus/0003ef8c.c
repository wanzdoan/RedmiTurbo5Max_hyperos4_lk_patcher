// File offset entry: 0x3ef8c

uint FUN_ffff0000b803ef8c(void)

{
  long *plVar1;
  
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  if (DAT_ffff0000b8128850 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff0000b803d4d4(0,0);
    if ((DAT_ffff0000b812727c & 1) == 0) {
      FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
      DAT_ffff0000b812727c = 1;
    }
    DAT_ffff0000b8128850 = plVar1;
    if (plVar1 == (long *)0x0) {
      return 0;
    }
  }
  if (*DAT_ffff0000b8128850 == 0) {
    return 0;
  }
  return *(int *)(*DAT_ffff0000b8128850 + 0x1c) + 0x1fU & 0xffffffe0;
}


