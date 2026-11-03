// File offset entry: 0x3fa00

void FUN_ffff0000b803fa00(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (DAT_ffff0000b812883c != 0) {
    return;
  }
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
    if (plVar1 != (long *)0x0) goto LAB_ffff0000b803fa90;
    plVar1 = (long *)FUN_ffff0000b803d4d4(0,0);
    if ((DAT_ffff0000b812727c & 1) == 0) {
      FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
      DAT_ffff0000b812727c = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    DAT_ffff0000b8128850 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff0000b803fac8;
  }
  else {
LAB_ffff0000b803fa90:
    if (*DAT_ffff0000b8128850 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff0000b803fac8;
    }
    uVar2 = *(int *)(*DAT_ffff0000b8128850 + 0x18) * 4 + 0x7cU & 0xffffff80;
  }
  uVar3 = 0;
  if (*DAT_ffff0000b8128850 != 0) {
    uVar3 = *(int *)(*DAT_ffff0000b8128850 + 0x1c) + 0x1fU >> 3 & 0x1ffffffc;
  }
LAB_ffff0000b803fac8:
  DAT_ffff0000b812883c = uVar2 * uVar3;
  return;
}


