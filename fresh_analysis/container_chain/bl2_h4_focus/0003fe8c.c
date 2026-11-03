// File offset entry: 0x3fe8c

void FUN_ffff0000b803fe8c(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (DAT_ffff0000b812989c != 0) {
    return;
  }
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298b0 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff0000b803d960(0,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    DAT_ffff0000b81298b0 = plVar1;
    if (plVar1 != (long *)0x0) goto LAB_ffff0000b803ff1c;
    plVar1 = (long *)FUN_ffff0000b803d960(0,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    DAT_ffff0000b81298b0 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff0000b803ff54;
  }
  else {
LAB_ffff0000b803ff1c:
    if (*DAT_ffff0000b81298b0 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff0000b803ff54;
    }
    uVar2 = *(int *)(*DAT_ffff0000b81298b0 + 0x18) * 4 + 0x7cU & 0xffffff80;
  }
  uVar3 = 0;
  if (*DAT_ffff0000b81298b0 != 0) {
    uVar3 = *(int *)(*DAT_ffff0000b81298b0 + 0x1c) + 0x1fU >> 3 & 0x1ffffffc;
  }
LAB_ffff0000b803ff54:
  DAT_ffff0000b812989c = uVar2 * uVar3;
  return;
}


